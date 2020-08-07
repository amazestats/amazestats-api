(ns amazestats.routes
  (:require [buddy.auth.middleware :refer [wrap-authentication]]
            [compojure.core :refer [context
                                    defroutes
                                    DELETE
                                    GET
                                    POST
                                    PUT
                                    routes
                                    wrap-routes]]
            [compojure.route :as route]
            [ring.middleware.cors :refer [wrap-cors]]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.util.response :refer [not-found]]
            [amazestats.authentication :as auth]
            [amazestats.handlers :refer [get-token]]
            [amazestats.handlers.competition :refer [get-all-competitions
                                                     get-competition-admins
                                                     get-competition-by-id]]
            [amazestats.handlers.division :refer [create-division!
                                                  find-divisions-by-competition
                                                  find-divisions-by-key
                                                  get-division-by-id
                                                  get-all-divisions]]
            [amazestats.handlers.match :refer [find-matches-by-season
                                               get-match-by-id
                                               update-match-referee!
                                               remove-match-referee!]]
            [amazestats.handlers.season :refer [find-seasons-by-division
                                                get-season-by-id]]
            [amazestats.handlers.team :refer [create-team!
                                              find-teams-by-competition
                                              find-teams-by-division
                                              find-teams-by-key
                                              get-team-by-id
                                              get-all-teams]]
            [amazestats.handlers.user :refer [create-user!
                                              get-user-by-id
                                              get-all-users]]
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
    (context "/competitions/:competition/teams" [competition]
      (POST "/" request (create-team! competition request)))
    (context "/competitions/:competition/divisions" [competition]
      (POST "/" request (create-division! competition request)))
    (context "/matches/:match" [match]
             (PUT "/referee" request (update-match-referee! match request))
             (DELETE "/referee" request (remove-match-referee! match request)))))


;;; PUBLIC ROUTES

;; These routes are available to any API consumer.

(defroutes public-routes

  (context "/api/competitions" []
           (GET "/" _ (get-all-competitions))
           (GET "/:id" [id] (get-competition-by-id id))
           (GET "/:competition/divisions" [competition]
                (find-divisions-by-competition competition))
           (GET "/:competition/teams" [competition]
                (find-teams-by-competition competition))
           (GET "/:competition/admins" [competition]
                (get-competition-admins competition)))

  (context "/api/divisions" []
           (GET "/" [key] (if (nil? key)
                            (get-all-divisions)
                            (find-divisions-by-key key)))
           (GET "/:id" [id] (get-division-by-id id))
           (GET "/:division/teams" [division]
                (find-teams-by-division division))
           (GET "/:division/seasons" [division]
                (find-seasons-by-division division)))

  (context "/api/teams" []
           (GET "/" [key] (if (nil? key)
                            (get-all-teams)
                            (find-teams-by-key key)))
           (GET "/:id" [id] (get-team-by-id id)))

  (context "/api/seasons" []
           (GET "/:id" [id] (get-season-by-id id))
           (GET "/:season/matches" [season]
                (find-matches-by-season season)))

  (context "/api/matches" []
           (GET "/:id" [id] (get-match-by-id id)))

  (context "/api/users" []
           (GET "/" _ (get-all-users))
           (POST "/" req (create-user! req))
           (GET "/:id" [id] (get-user-by-id id)))

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
