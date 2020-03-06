(ns wolley.handlers
  (:require [clojure.tools.logging :as log]))

(def matches (list
               {:division "17"
                :home-team "1"
                :away-team "2"}
               {:division "16"
                :home-team "3"
                :away-team "4"}
               {:division "17"
                :home-team "2"
                :away-team "5"}))

(defn get-matches-for-division
  [division]
  (filter (fn [match] (= (:division match) division)) matches))

(defn get-matches-for-team
  [team division]
  ;; I suppose logically, for the current use cases, matches filtered by team
  ;; will by design also be filtered by division. But hey, maybe it changes ...
  (let [filtered-matches (if (not (nil? division))
                           (get-matches-for-division division)
                           matches)]
    (filter (fn [match] (or (= (:home-team match) team)
                            (= (:away-team match) team)))
            matches)))

(defn get-matches
  [request]
  (let [body (:body request)
        params (:params request)
        division (:division params)
        team (:team params)]

    (if (not (nil? team))
      ;; If we were given a team we only get the matches for that specfic
      ;; team. Eventually also limited by division.
      {:status 200
       :body {:matches (get-matches-for-team team division)}}

      ;; Otherwise we should be given the division at least, and limit our
      ;; match selection based on that.
      (if (not (nil? division))
        {:status 200
         :body {:matches (get-matches-for-division division)}}

        ;; If we are not given either teams or division we have a too broad
        ;; search, as the resulting list may become large.
        {:status 400
          :body {:message "Either team or division must be given."}}))))
