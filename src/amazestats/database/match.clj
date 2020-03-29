(ns amazestats.database.match
  (:import org.postgresql.util.PSQLException)
  (:require [amazestats.database.core :refer [db-spec]]
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
