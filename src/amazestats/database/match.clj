(ns amazestats.database.match
  (:import org.postgresql.util.PSQLException)
  (:require [amazestats.database.core :refer [db-spec]]
            [clojure.java.jdbc :as jdbc]
            [clojure.tools.logging :as log]))

(defn get-match-by-id
  [id]
  (first
    (try
      (jdbc/query db-spec ["SELECT * FROM matches WHERE id = ?" id])
      (catch org.postgresql.util.PSQLException e (log/error e)))))

(defn get-matches-by-season
  [season]
  (try
    (jdbc/query db-spec ["SELECT * FROM matches WHERE seasons = ?" season])
    (catch org.postgresql.util.PSQLException e (log/error e))))
