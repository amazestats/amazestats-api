(ns amazestats.handlers.competition
  (:require [amazestats.database.competition :as db]
            [amazestats.util.response :refer [internal-error not-found ok]]))

(defn get-competitions
  "Create a response map containing all competitions.
  The response status will be 200 as long as no errors occur in the database
  layer, in which case the status will be 500."
  []
  (let [competitions (db/get-competitions)]
    (if (nil? competitions)
      (internal-error)
      (ok {:competitions competitions}))))

(defn get-competition
  "Creates a response map for a competition given its `id`.
  If the `id` is not valid, i.e. the competition does not exist, a 404 is
  returned. Otherwise the competition is returned in a 200 response."
  [id]
  (let [competition (db/get-competition (Integer. id))]
    (if (nil? competition)
      (not-found "Competition does not exist.")
      (ok {:competition competition}))))
