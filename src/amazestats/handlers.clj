(ns amazestats.handlers
  (:require [ring.util.response :refer [bad-request created not-found]]
            [amazestats.authentication :as auth]
            [amazestats.database.core :as db]))
                                 
(defn get-token
  "Create a response with a newly generated authentication token."
  [request]
  {:status 200
   :body {:id (get-in request [:identity :user-id])
          :token (auth/create-token
                  (get-in request [:identity :user-id]))}})
