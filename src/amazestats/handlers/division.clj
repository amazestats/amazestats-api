(ns amazestats.handlers.division
  (:require [amazestats.database.competition :as competition-db]
            [amazestats.database.division :as db]
            [amazestats.util.core :refer [create-key]]
            [amazestats.util.response :refer [bad-request
                                              created
                                              forbidden
                                              internal-error
                                              not-found
                                              ok]]
            [clojure.tools.logging :as log]))

(defn get-division-by-id
  [id]
  (log/info "Getting division by ID:" id)
  (let [division (db/get-division-by-id (Integer. id))]
    (if (nil? division)
      (not-found "Division does not exist in competition.")
      (ok {:division division}))))

(defn get-all-divisions
  []
  (let [divisions (db/get-all-divisions)]
    (if (nil? divisions)
      (internal-error)
      (ok {:divisions divisions}))))

(defn find-divisions-by-competition
  [competition]
  (log/info "Finding divisions in competition:" competition)
  (let [competition (Integer. competition)
        divisions (db/get-divisions-by-competition competition)]

    ;; If the divisions list is empty we have to verify if the competition
    ;; exists to decide whether to serve a 404 or 200 with empty list.
    (if (and (empty? divisions)
             (nil? (competition-db/get-competition-by-id competition)))
      (not-found "Competition does not exist.")
      (ok {:divisions divisions}))))

(defn find-divisions-by-key
  [division-key]
  (let [divisions (db/get-divisions-by-key division-key)]
    (if (nil? divisions)
      (internal-error)
      (ok {:divisions divisions}))))

(defn create-division!
  [competition request]
  (let [competition (Integer. competition)
        admin? (competition-db/competition-admin?
                 competition
                 (get-in request [:identity :user-id]))]
    (if-not admin?
      (forbidden)
      (let [division-name (get-in request [:body :name])
            division-key (create-key division-name)
            division (db/create-division! competition
                                          division-name
                                          division-key)]
        (if (not (nil? division))
          (created (str "/api/divisions/" (:id division)))
          (bad-request (str "Could not create division: " division-name)))))))

