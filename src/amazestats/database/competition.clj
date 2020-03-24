(ns amazestats.database.competition
  (:require [amazestats.database.core :refer [db-spec]]
            [clojure.java.jdbc :as jdbc]
            [clojure.tools.logging :as log]
            [amazestats.util.parse :refer [join]]))

(defn get-competitions
  "Create list of competitions from database.
  If an error occurs in the database, `nil` is returned instead."
  []
  (try
    (jdbc/query db-spec ["SELECT id, key, name FROM competitions"])
    (catch org.postgresql.util.PSQLException e
      (log/error "Failed to get competitions." e))))

(defn get-competition
  "Get a competition from the database by its `id`.
  If the competition does not exist or if an error occurs, `nil` is returned."
  [id]
  (first
    (try
      (jdbc/query
        db-spec
        [(join "SELECT c.id, c.key, c.name"
               "FROM competitions c"
               "WHERE id = ?") id])
      (catch org.postgresql.util.PSQLException e
        (log/error "Failed to get competition with ID:" id e)))))
