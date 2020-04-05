(ns amazestats.handlers
  (:require [amazestats.authentication :as auth]
            [amazestats.util.response :refer [ok]]))
                                 
(defn get-token
  "Create a response with a newly generated authentication token."
  [request]
  (let [user-id (get-in request [:identity :user-id])
        token-data (auth/create-token user-id)
        token (:token token-data)
        expire-date (:exp token-data)]
    (ok {:id user-id
         :token token
         :expiration-date expire-date})))
