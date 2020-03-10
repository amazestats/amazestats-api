(ns wolley.routes
  (:require [clojure.tools.logging :as log]
            [compojure.core :refer :all]
            [ring.middleware.cors :refer [wrap-cors]]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.resource :refer [wrap-resource]]
            [wolley.handlers :refer [create-division!
                                     create-team!
                                     create-user!
                                     get-division-by-id
                                     get-divisions
                                     get-matches
                                     get-teams
                                     get-user
                                     get-users]]
            [wolley.middleware :refer [wrap-content-type]]))

(defroutes api-routes
  (context "/api" []
           (context "/divisions" []
                    (GET "/" request (get-divisions request))
                    (GET "/:id" [id] (get-division-by-id id))
                    (POST "/" request (create-division! request)))
           (context "/matches" []
                    (GET "/" request (get-matches request)))
           (context "/teams" []
                    (GET "/" request (get-teams request))
                    (POST "/" request (create-team! request)))
           (context "/users" []
                    (GET "/" request (get-users request))
                    (POST "/" request (create-user! request))
                    (GET "/:id" [id] (get-user id)))))


;; Apply middleware to accept/send JSON data
(def handlers
  (-> (routes api-routes)
      (wrap-params)
      (wrap-json-body {:keywords? true})
      (wrap-content-type "application/json")
      (wrap-json-response)
      (wrap-resource "/public")
      (wrap-cors :access-control-allow-origin [#".*"]
                 :access-control-allow-methods [:get :put :post :delete])))
