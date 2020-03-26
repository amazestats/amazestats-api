(ns amazestats.database.core
  (:import com.mchange.v2.c3p0.ComboPooledDataSource)
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.tools.logging :as log]
            [environ.core :refer [env]]
            [jdbc.pool.c3p0 :as pool]
            [amazestats.util.parse :refer [join]]))

(def db-uri
  (let [db-url (env :database-url)]
    (if (not (nil? db-url))
      (java.net.URI. db-url))))

(def user-and-password
  (if (not (nil? db-uri))
    (if (not (nil? (.getUserInfo db-uri)))
      (clojure.string/split (.getUserInfo db-uri) #":"))))

(defn generate-spec-from-uri []
  (pool/make-datasource-spec
    {:classname "org.postgresql.Driver"
     :subprotocol "postgresql"
     :user (get user-and-password 0)
     :password (get user-and-password 1)
     :subname
     (if (= -1 (.getPort db-uri))
       (format "//%s%s"
               (.getHost db-uri)
               (.getPath db-uri))
       (format "//%s:%s%s"
               (.getHost db-uri)
               (.getPort db-uri)
               (.getPath db-uri)))}))

(def db-spec
  (if (not (nil? db-uri))
    (generate-spec-from-uri)
    {:dbtype "postgresql"
     :host "database"
     :dbname (env :postgres-db)
     :user (env :postgres-user)
     :password (env :postgres-password)}))

(defn get-teams
  []
  (try
    (jdbc/query db-spec ["SELECT * FROM teams"])
    (catch org.postgresql.util.PSQLException e
      (log/error e "Failed to get teams.")
      nil)))

(defn get-team-by-key
  [team-key]
  (first
    (try
      (jdbc/query db-spec ["SELECT * FROM teams WHERE key = ?" team-key])
      (catch org.postgresql.util.PSQLException e
        (log/error e "Failed to get team:" team-key)
        nil))))

(defn create-team!
  [team-name team-key division]
  (first
    (try
      (jdbc/insert! db-spec :teams {:name team-name
                                    :key team-key
                                    :division division})
      (catch org.postgresql.util.PSQLException e
        (log/error e "Failed to create team:" team-name)
        nil))))

(defn find-teams-by-division-id
  [division-id]
  (try
    (jdbc/query
      db-spec
      ["SELECT * FROM teams WHERE division = ?" division-id])
    (catch org.postgresql.util.PSQLException e
      (log/error e "Failed to get teams for division:" division-id)
      nil)))

(defn find-teams-by-division-key
  [division-key]
  (try
    (jdbc/query
      db-spec
      [(join "SELECT t.* FROM teams t"
             "INNER JOIN divisions d"
             "ON t.division = d.id"
             "WHERE d.key = ?")
       division-key])
    (catch org.postgresql.util.PSQLException e
      (log/error e "Failed to get teams for division:" division-key)
      nil)))

(defn find-sets-by-match
  [match-id]
  (try
    (jdbc/query
      db-spec
      ["SELECT * FROM sets WHERE match = ?" match-id])
    (catch org.postgresql.util.PSQLException e
      (log/error e "Failed to get sets for match:" match-id)
      ())))

(defn attach-sets-to-match
  [matches]
  (map (fn [match]
         (assoc match :sets (find-sets-by-match (:id match))))
       matches))

(defn find-matches-by-team
  [team-id]
  (let [matches
        (try (jdbc/query
               db-spec
               ["SELECT * FROM matches WHERE home_team = ? OR away_TEAM = ?"
                team-id team-id])
             (catch org.postgresql.util.PSQLException e
               (log/error e "Failed to get matches for team:" team-id)
               ()))]
    (attach-sets-to-match matches)))

(defn find-matches-by-division
  [division-id]
  (let [matches
        (try (jdbc/query
               db-spec
               ["SELECT * FROM matches WHERE division = ?"
               division-id])
             (catch org.postgresql.util.PSQLException e
               (log/error e "Failed to get matches for division:" division-id)
               ()))]
    (attach-sets-to-match matches)))
