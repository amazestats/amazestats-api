(ns amazestats.handlers.team
  (:require [amazestats.database.team :as db]
            [amazestats.database.competition :as competition-db]
            [amazestats.database.division :as division-db]
            [amazestats.util.core :refer [create-key]]
            [amazestats.util.response :refer [bad-request
                                              created
                                              internal-error
                                              not-found
                                              ok]]))

(defn get-team-by-id
  [id]
  (let [team (db/get-team-by-id (Integer. id))] 
    (if (nil? team)
      (not-found "Team does not exist.")
      (ok {:team team}))))

(defn get-all-teams
  []
  (let [teams (db/get-all-teams)]
    (if (nil? teams)
      (internal-error)
      (ok {:teams teams}))))

(defn find-teams-by-competition
  [competition]
  (let [competition (Integer. competition)
        teams (db/get-teams-by-competition competition)]
    (if (and (empty? teams)
             (nil? (competition-db/get-competition-by-id competition)))
      (not-found "Competition does not exist.")
      (ok {:teams teams}))))

(defn find-teams-by-division
  ""
  [division]
  (let [division (Integer. division)
        teams (db/get-teams-by-division division)]
    (if (and (empty? teams)
             (nil? (division-db/get-division-by-id division)))
      (not-found "Division does not exist.")
      (ok {:teams teams}))))

(defn create-team!
  [competition request]
  (let [competition (Integer. competition)
        body (:body request)
        team-name (:name body)
        team-key (create-key team-name)
        team (db/create-team! competition team-name team-key)]
    (if (not (nil? team))
      (created (str "/api/competitions/" competition "/teams/" (:id team)))
      (bad-request (str "Could not create team: " team-name)))))
