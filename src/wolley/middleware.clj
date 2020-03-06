(ns wolley.middleware
  (:require [ring.util.response :as response]))

(defn wrap-content-type
  [handler content-type]
  (fn [request] (response/content-type (handler request) content-type)))
