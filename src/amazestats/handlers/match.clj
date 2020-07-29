(ns amazestats.handlers.match
  (:require [amazestats.database.competition :as competition-db]
            [amazestats.database.match :as db]
            [amazestats.database.season :as season-db]
            [amazestats.util.response :refer [bad-request
                                              forbidden
                                              not-found
                                              ok]]))

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

(defn update-match-referee
  "Updates the given `match`'s referee.
  The user updating the referee must be authenticated as a competition
  administrator, otherwise a 403 will be returned. On success, the updated
  field will be returned in a 200 response."
  [match request]
  (let [match (db/get-match-by-id (Integer. match))]
    (if (nil? match)
      (not-found "The match does not exist.")

      (let [competition (db/get-competition-id-for-match (:id match))
            competition-admin? (competition-db/competition-admin?
                                 competition
                                 (get-in request [:identity :user-id]))]
        (if (not competition-admin?)
          (forbidden
            {:message
             "The user must be a competition admin to update referees."}) 

          (let [referee (Integer. (get-in request [:body :id]))
                referee-map (db/set-match-referee (:id match) referee)]
            (if (nil? referee-map)
              (bad-request "Could not appoint team as referee.")
              (ok referee-map))))))))
