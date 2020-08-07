(ns amazestats.handlers.competition
  (:require [amazestats.database.competition :as db]
            [amazestats.util.response :refer [internal-error not-found ok]]
            [clojure.set :as set]))

(defn get-all-competitions
  "Create a response map containing all competitions.
  The response status will be 200 as long as no errors occur in the database
  layer, in which case the status will be 500."
  []
  (let [competitions (db/get-all-competitions)]
    (if (nil? competitions)
      (internal-error)
      (ok {:competitions competitions}))))

(defn get-competition-by-id
  "Creates a response map for a competition given its `id`.
  If the `id` is not valid, i.e. the competition does not exist, a 404 is
  returned. Otherwise the competition is returned in a 200 response."
  [id]
  (let [competition (db/get-competition-by-id (Integer. id))]
    (if (nil? competition)
      (not-found "Competition does not exist.")
      (ok {:competition competition}))))

(defn get-competition-admins
  "Create a response with all admins for a competition with given `id`.
  Returns a 404 if the competition does not exist, otherwise a map containing
  the `id` and `alias` for all admins in the competition."
  [competition-id]
  (let [competition-id (Integer. competition-id)
        admins (db/get-competition-admins competition-id)]
    (if (and (empty? admins) (nil? (db/get-competition-by-id competition-id)))
      (not-found "Competition does not exist.")
      (let [admins (map (fn [admin]
                          (set/rename-keys admin {:admin :id}))
                        admins)]
        (ok {:admins admins})))))
