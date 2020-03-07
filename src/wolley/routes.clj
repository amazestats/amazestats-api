(ns wolley.routes
  (:require [compojure.core :refer :all]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [ring.middleware.params :refer [wrap-params]]
            [wolley.handlers :refer [create-division!
                                     create-team!
                                     get-division
                                     get-divisions
                                     get-matches
                                     get-teams]]
            [wolley.middleware :refer [wrap-content-type]]
            [ring.middleware.cors :refer [wrap-cors]]))

(defroutes api-routes
  (context "/api" []
           (context "/divisions" []
                    (GET "/" request (get-divisions request))
                    (GET "/:id" [id] (get-division id))
                    (POST "/" request (create-division! request)))
           (context "/matches" []
                    (GET "/" request (get-matches request)))
           (context "/teams" []
                    (GET "/" request (get-teams request))
                    (POST "/" request (create-team! request)))))

;; Apply middleware to accept/send JSON data
(def handlers
  (-> (routes api-routes)
      (wrap-cors :access-control-allow-origin [#".*"]
                 :access-control-allow-methods [:get :put :post :delete])
      (wrap-params)
      (wrap-json-body {:keywords? true})
      (wrap-content-type "application/json")
      (wrap-json-response)))
