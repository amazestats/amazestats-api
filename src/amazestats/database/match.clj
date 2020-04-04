(ns amazestats.database.match
  (:import org.postgresql.util.PSQLException)
  (:require [amazestats.database.core :refer [db-spec]]
            [amazestats.util.parse :refer [join]]
            [clojure.java.jdbc :as jdbc]
            [clojure.tools.logging :as log]))

(defn get-match-by-id
  [id]
  (first
    (try
      (let [matches (jdbc/query
                      db-spec
                      ["SELECT * FROM matches WHERE id = ?" id])]
        (list
          (if (seq matches)
            (assoc (first matches)
                   :sets
                   (jdbc/query
                     db-spec
                     ["SELECT * FROM sets WHERE match = ?" id])))))
      (catch org.postgresql.util.PSQLException e (log/error e)))))

(defn get-matches-by-season
  [season]
  (try
    (let [matches (jdbc/query
                    db-spec
                    ["SELECT * FROM matches WHERE season = ?" season])]
      (map (fn [match]
             (assoc match
                    :sets
                    (jdbc/query
                      db-spec
                      ["SELECT * FROM sets WHERE match = ?" (:id match)])))
           matches))
    (catch org.postgresql.util.PSQLException e (log/error e))))

(defn set-match-referee
  "Updates a `match` with a new team as referee.
  Will return the updated column and value as a map if successful, otherwise
  nil."
  [match referee-team]
  (try
    (let [update-count (first (jdbc/update! db-spec
                                            :matches
                                            {:referee referee-team}
                                            ["id = ?" match]))]
      (if (> update-count 0)
        {:referee referee-team}
        nil))
    (catch org.postgresql.util.PSQLException e (log/error e))))

(defn get-competition-id-for-match
  "Finds the competition id for a given `match`."
  [match]
  (let [rows (try
               (jdbc/query
                 db-spec
                 [(join "SELECT d.competition"
                        "FROM matches m"
                        "INNER JOIN seasons s"
                        "ON s.id = m.season"
                        "INNER JOIN divisions d"
                        "ON d.id = s.division"
                        "WHERE m.id = ?")
                  match])
               (catch org.postgresql.util.PSQLException e (log/error e)))]
    (if (not (seq rows))
      nil
      (:competition (first rows)))))
