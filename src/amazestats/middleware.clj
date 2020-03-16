(ns amazestats.middleware
  (:require [buddy.auth :refer [authenticated?]]
            [ring.util.response :as response]
            [amazestats.util.response :refer [unauthorized]]))

(defn wrap-content-type
  [handler content-type]
  (fn [request] (response/content-type (handler request) content-type)))

(defn wrap-enforce-authentication
  "Wraps handler with check that user is authenticated. If the user is not
   authenticated a 401 will be returned. Otherwise the handler will take care of
   the request."
  [handler]
  (fn [request]
    (if-not (authenticated? request)
      (unauthorized)
      (handler request))))
