(ns wolley.handlers
  (:require [clojure.tools.logging :as log]
            [wolley.database :as db]))

(defn get-matches
  [request]
  (let [body (:body request)
        params (:params request)
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
