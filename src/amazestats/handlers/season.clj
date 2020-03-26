(ns amazestats.handlers.season
  (:require [amazestats.database.division :as division-db]
            [amazestats.database.season :as db]
            [amazestats.util.response :refer [not-found
                                              ok]]
            [clojure.tools.logging :as log]))

(defn get-season-by-id
  [season]
  (let [season (db/get-season-by-id (Integer. season))]
    (log/debug "Get season from database:" season)
    (if (nil? season)
      (not-found "Season does not exist.")
      (ok {:season season}))))

(defn find-seasons-by-division
  [division]
  (let [division (Integer. division)
        seasons (db/get-seasons-by-division division)]
    (log/debug "Got seasons from database:" seasons)
    (if (and (empty? seasons)
             (nil? (division-db/get-division-by-id division)))
      (not-found "Division does not exist.")
      (ok {:seasons seasons}))))
