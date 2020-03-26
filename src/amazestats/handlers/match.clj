(ns amazestats.handlers.match
  (:require [amazestats.database.match :as db]
            [amazestats.database.season :as season-db]
            [amazestats.util.response :refer [not-found ok]]))

(defn get-match-by-id
  [id]
  (let [match (db/get-match-by-id (Integer. id))]
    (if (nil? match)
      (not-found "Match does not exist.")
      (ok {:match match}))))

(defn find-matches-by-season
  [season]
  (let [season (Integer. season)
        matches (db/get-matches-by-season season)]
    (if (and (empty? matches)
             (nil? (season-db/get-season-by-id season)))
      (not-found "Season does not exist.")
      (ok {:matches matches}))))

