(ns wolley.database
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.tools.logging :as log]
            [environ.core :refer [env]]))

(def db-spec {:dbtype "postgresql"
              :host "database"
              :dbname (env :postgres-db)
              :user (env :postgres-user)
              :password (env :postgres-password)})

(defn find-matches-by-team
  [team-id]
  (try
    (jdbc/query
      db-spec
      ["SELECT * FROM matches WHERE home_team = ? OR away_team = ?"
       team-id team-id])
    (catch org.postgresql.util.PSQLException e
      (log/error e "Failed to get matches for team:" team-id)
      nil)))

(defn find-matches-by-division
  [division-id]
  (try
    (jdbc/query
      db-spec
      ["SELECT * FROM matches WHERE division = ?" division-id])
    (catch org.postgresql.util.PSQLException e
      (log/error e "Failed to get matches for division:" division-id)
      nil)))
