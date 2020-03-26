(ns amazestats.database.season
  (:import org.postgresql.util.PSQLException)
  (:require [amazestats.database.core :refer [db-spec]]
            [amazestats.util.parse :refer [join]]
            [clojure.java.jdbc :as jdbc]
            [clojure.tools.logging :as log]))

(defn get-season-by-id
  "Create season map for given season.
  Find season by its `id` and return it as a map. Returns nil if an error
  occurs or no season is found."
  [id]
  (try
    (let [season (first (jdbc/query
                          db-spec
                          [(join "SELECT s.*, d.competition"
                                 "FROM seasons s"
                                 "INNER JOIN divisions d"
                                 "ON s.division = d.id"
                                 "WHERE s.id = ?")
                           id]))]
      (if (nil? season)
        nil
        (assoc season :matches (jdbc/query
                                 db-spec
                                 [(join "SELECT * FROM matches"
                                        "WHERE season = ?")
                                  (:id season)]))))
    (catch org.postgresql.util.PSQLException e (log/error e))))

(defn get-seasons-by-division
  "Create list of all seasons in a `division`.
  If an error occurs returns nil, otherwise a list of season maps."
  [division]
  (try
    (jdbc/query db-spec [(join "SELECT s.*, d.competition"
                               "FROM seasons s"
                               "INNER JOIN divisions d"
                               "ON s.division = d.id"
                               "WHERE division = ?")
                         division])
    (catch org.postgresql.util.PSQLException e
      (log/error "Failed to get seasons for division:" division e))))
