(ns amazestats.database.team
  (:import org.postgresql.util.PSQLException)
  (:require [amazestats.database.core :refer [db-spec]]
            [clojure.java.jdbc :as jdbc]
            [clojure.tools.logging :as log]))

(defn create-team!
  "Creates an unactivated team."
  [competition team-name team-key]
  (first
    (try
      (jdbc/insert! db-spec :teams {:name team-name
                                    :key team-key
                                    :competition competition})
      (catch org.postgresql.util.PSQLException e
        (log/error "Failed to create team:" team-name e)))))

(defn get-team-by-id
  [id]
  (first
    (try
      (jdbc/query db-spec ["SELECT * FROM teams WHERE id = ?" id])
      (catch org.postgresql.util.PSQLException e
        (log/error "Failed to get team:" id e)))))

(defn get-all-teams
  []
  (try
    (jdbc/query db-spec ["SELECT * FROM teams"])
    (catch org.postgresql.util.PSQLException e (log/error e))))

(defn get-teams-by-competition
  [competition]
  (try
    (jdbc/query db-spec ["SELECT * FROM teams WHERE competition = ?"
                         competition])
    (catch org.postgresql.util.PSQLException e
      (log/error "Failed to get teams." e))))

(defn get-teams-by-division
  [division-id]
  (try
    (jdbc/query
      db-spec
      ["SELECT * FROM teams WHERE division = ?" division-id])
    (catch org.postgresql.util.PSQLException e
      (log/error "Failed to get teams for division:" division-id e))))

(defn get-teams-by-key
  [team-key]
  (try
    (jdbc/query db-spec ["SELECT * FROM teams WHERE key = ?" team-key])
    (catch org.postgresql.util.PSQLException e (log/error e))))
