(ns amazestats.handlers
  (:require [amazestats.authentication :as auth]))
                                 
(defn get-token
  "Create a response with a newly generated authentication token."
  [request]
  {:status 200
   :body {:id (get-in request [:identity :user-id])
          :token (auth/create-token
                  (get-in request [:identity :user-id]))}})
