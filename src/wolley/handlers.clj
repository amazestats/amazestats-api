(ns wolley.handlers
  (:require [clojure.tools.logging :as log]
            [ring.util.response :refer [bad-request created not-found]]
            [wolley.database :as db]
            [wolley.util.response :refer [internal-error ok]]))

(defn get-divisions
  [request]
  (ok {:divisions (db/get-divisions)}))

(defn create-division!
  [request]
  (let [division-name (get-in request [:body :name])
        division (db/create-division! division-name)]
    (if (not (nil? division))
      (created (str "/api/divisions/" (:id division)))
      (bad-request (str "Could not create division: " division-name)))))

(defn get-teams
  [request]
  (let [params (:params request)
        division (:division params)
        teams (if (not (nil? division))
                (db/find-teams-by-division (Integer. division))
                (db/get-teams))]
    {:status 200
     :body {:teams teams}}))

(defn create-team!
  [request]
  (let [body (:body request)
        team-name (:name body)
        division (:division body)
        team (db/create-team! team-name division)]
    (if (not (nil? team))
      (created (str "/api/teams/" (:id team)))
      (bad-request (str "Could not create team: " team-name)))))

(defn get-matches
  [request]
  (let [params (:params request)
        division (:division params)
        team (:team params)]

    (if (not (nil? team))
      ;; If we were given a team we only get the matches for that specfic
      ;; team. This also limits per division, as a team only plays in one
      ;; division at this moment.
      {:status 200
       :body {:matches (db/find-matches-by-team (Integer. team))}}

      ;; Otherwise we should be given the division at least, and limit our
      ;; match selection based on that.
      (if (not (nil? division))
        {:status 200
         :body {:matches (db/find-matches-by-division (Integer. division))}}

        ;; If we are not given either teams or division we have a too broad
        ;; search, as the resulting list may become large.
        {:status 400
          :body {:message "Either team or division must be given."}}))))
