(ns wolley.routes
  (:require [compojure.core :refer :all]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [ring.middleware.params :refer [wrap-params]]
            [wolley.handlers :refer [create-division!
                                     create-team!
                                     get-divisions
                                     get-matches
                                     get-teams]]
            [wolley.middleware :refer [wrap-content-type]]))

(defroutes api-routes
  (context "/api" []
           (context "/divisions" []
                    (GET "/" request (get-divisions request))
                    (POST "/" request (create-division! request)))
           (context "/matches" []
                    (GET "/" request (get-matches request)))
           (context "/teams" []
                    (GET "/" request (get-teams request))
                    (POST "/" request (create-team! request)))))

;; Apply middleware to accept/send JSON data
(def handlers
  (-> (routes api-routes)
      (wrap-params)
      (wrap-json-body {:keywords? true})
      (wrap-content-type "application/json")
      (wrap-json-response)))
