(ns wolley.routes
  (:require [compojure.core :refer :all]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [ring.middleware.params :refer [wrap-params]]
            [wolley.handlers :refer [get-matches]]
            [wolley.middleware :refer [wrap-content-type]]))

(defroutes api-routes
  (context "/api" []
           (context "/matches" []
                    (GET "/" request (get-matches request)))
           (GET "/" request {:status 200
                             :body {:message "Hello API!"}})))

;; Apply middleware to accept/send JSON data
(def handlers
  (-> (routes api-routes)
      (wrap-params)
      (wrap-json-body {:keywords? true})
      (wrap-content-type "application/json")
      (wrap-json-response)))
