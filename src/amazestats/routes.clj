(ns amazestats.routes
  (:require [buddy.auth.middleware :refer [wrap-authentication]]
            [compojure.core :refer [context
                                    defroutes
                                    GET
                                    POST
                                    routes
                                    wrap-routes]]
            [compojure.route :as route]
            [ring.middleware.cors :refer [wrap-cors]]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.util.response :refer [not-found]]
            [amazestats.authentication :as auth]
            [amazestats.handlers :refer [create-team!
                                         create-user!
                                         get-matches
                                         get-teams
                                         get-token
                                         get-user
                                         get-users]]
            [amazestats.handlers.competition :refer [get-competition
                                                     get-competitions]]
            [amazestats.handlers.division :refer [create-division!
                                                  get-division-by-id
                                                  get-divisions]]
            [amazestats.middleware :refer [wrap-content-type
                                           wrap-enforce-authentication]]))

;;; PRIVATE ROUTES

;; These routes are available to authenticated API consumers. Most routes will
;; require a token based authentication (using JWT), whilst the token request,
;; for example, will require basic authentication.

(defroutes basic-routes
  (context "/api" []
    (POST "/token" request (get-token request))))

(defroutes token-routes
  (context "/api" []
    (context "/teams" []
      (POST "/" request (create-team! request)))
    (context "/divisions" []
      (POST "/" request (create-division! request)))))


;;; PUBLIC ROUTES

;; These routes are available to any API consumer.

(defroutes public-routes
  (context "/api" []
           (context "/competitions" []
                    (GET "/" _ (get-competitions))
                    (GET "/:id" [id] (get-competition id)))
    (context "/divisions" []
      (GET "/" request (get-divisions request))
      (GET "/:id" [id] (get-division-by-id id)))
    (context "/matches" []
      (GET "/" request (get-matches request)))
    (context "/teams" []
      (GET "/" request (get-teams request)))
    (context "/users" []
      (GET "/" request (get-users request))
      (POST "/" request (create-user! request))
      (GET "/:id" [id] (get-user id))))
  (route/not-found (not-found {:message "Resource does not exist."})))


;; Wrap our routes/handlers with required middleware.
(def handlers
  (-> (routes

       ;; Wrap private routes with enforced authentication.
       ;; NOTE: These cannot be wrapped until the route has been matched as the
       ;; wrappers otherwise would hinder later matching.
       (-> basic-routes
           (wrap-routes wrap-enforce-authentication)
           (wrap-routes wrap-authentication auth/basic-backend))
       (-> token-routes
           (wrap-routes wrap-enforce-authentication))

       ;; We place the public routes last in the chain as it contains the
       ;; page not found route.
       public-routes)

      ;; NOTE: We want to wrap the public routes with this token-based
      ;; authentication backend as well to enable non-mandatory authorization.
      (wrap-routes wrap-authentication auth/token-backend)

      ;; Wrap all routes, private and public alike, with the common wrappers.
      (wrap-params)
      (wrap-json-body {:keywords? true})
      (wrap-content-type "application/json")
      (wrap-json-response)
      (wrap-resource "/public")
      (wrap-cors :access-control-allow-origin [#".*"]
                 :access-control-allow-methods [:get :put :post :delete])))
