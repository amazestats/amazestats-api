(ns wolley.database
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.tools.logging :as log]
            [environ.core :refer [env]]))

(def db-spec {:dbtype "postgresql"
              :host "database"
              :dbname (env :postgres-db)
              :user (env :postgres-user)
              :password (env :postgres-password)})

(defn get-division-by-id
  [id]
  (first
    (try
      (jdbc/query db-spec ["SELECT * FROM divisions WHERE id = ?" id])
      (catch org.postgresql.util.PSQLException e
        (log/error e "Failed to get division:" id)
        nil))))

(defn find-division-by-key
  [division-key]
  (first ;; Key should be unique, let's treat this as a unique response
    (try
      (jdbc/query db-spec
                  ["SELECT * FROM divisions WHERE key = ?" division-key])
      (catch org.postgresql.util.PSQLException e
        (log/error e "Failed to get division:" division-key)
        nil))))

(defn get-divisions
  []
  (try
    (jdbc/query db-spec ["SELECT * FROM divisions"])
    (catch org.postgresql.util.PSQLException e
      (log/error e "Failed to get divisions.")
      nil)))

(defn create-division!
  [division-name division-key]
  (first
    (try
      (jdbc/insert! db-spec :divisions {:name division-name
                                        :key division-key})
      (catch org.postgresql.util.PSQLException e
        (log/error e "Failed to create division:" division-name)
        nil))))

(defn get-teams
  []
  (try
    (jdbc/query db-spec ["SELECT * FROM teams"])
    (catch org.postgresql.util.PSQLException e
      (log/error e "Failed to get teams.")
      nil)))

(defn create-team!
  [team-name division]
  (first
    (try
      (jdbc/insert! db-spec :teams {:name team-name
                                    :division division})
      (catch org.postgresql.util.PSQLException e
        (log/error e "Failed to create team:" team-name)
        nil))))

(defn find-teams-by-division
  [division-id]
  (try
    (jdbc/query
      db-spec
      ["SELECT * FROM teams WHERE division = ?" division-id])
    (catch org.postgresql.util.PSQLException e
      (log/error e "Failed to get teams for division:" division-id)
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
