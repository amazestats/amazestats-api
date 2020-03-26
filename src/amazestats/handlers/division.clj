(ns amazestats.handlers.division
  (:require [ring.util.response :refer [bad-request created not-found]]
            [amazestats.database.core :as core-db]
            [amazestats.database.division :as db]
            [amazestats.util.core :refer [create-key]]
            [amazestats.util.response :refer [ok]]))

(defn filter-division
  [col]
  (map (fn [x] (dissoc x :division)) col))

(defn get-division
  [division]
  (let [division-teams (filter-division
                        (core-db/find-teams-by-division-id (:id division)))
        division-matches (filter-division
                          (core-db/find-matches-by-division (:id division)))]
    (ok {:division (assoc division
                          :teams division-teams
                          :matches division-matches)})))

(defn get-division-by-id
  [id]
  (let [division (db/get-division-by-id (Integer. id))]
    (if (not (nil? division))
      (get-division division)
      (not-found "Division does not exist."))))

(defn find-division-by-key
  [division-key]
  (let [division (db/find-division-by-key division-key)]
    (if (not (nil? division))
      (get-division division)
      (not-found "Division does not exist."))))

(defn get-divisions
  [request]
  (let [division-key (get-in request [:params :key])]
    (if (not (nil? division-key))
      (find-division-by-key division-key)
      (ok {:divisions (db/get-divisions)}))))

(defn create-division!
  [request]
  (let [division-name (get-in request [:body :name])
        division-key (create-key division-name)
        division (db/create-division! division-name division-key)]
    (if (not (nil? division))
      (created (str "/api/divisions/" (:id division)))
      (bad-request (str "Could not create division: " division-name)))))

