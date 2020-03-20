(ns amazestats.handlers
  (:require [clojure.string :as string]
            [ring.util.response :refer [bad-request created not-found]]
            [amazestats.authentication :as auth]
            [amazestats.database :as db]
            [amazestats.util.response :refer [conflict ok]]
            [amazestats.util.validators :refer [valid-alias? valid-password?]]))

(defn get-token
  "Create a response with a newly generated authentication token."
  [request]
  {:status 200
   :body {:id (get-in request [:identity :user-id])
          :token (auth/create-token
                  (get-in request [:identity :user-id]))}})

(defn get-users
  "Create response with list of users."
  [_request]
  (ok {:users (db/get-users)}))

(defn get-user
  "Retrieve specific user by its user ID."
  [id]
  (if (not (integer? id)) ;; This check may or may not be unnecessary
                          ;; Might want to check what the database does
    (bad-request {:message "Invalid ID."})
    (let [user (db/get-user id)]
      (if (not (nil? user))
        (ok {:user (db/get-user id)})
        (not-found {:message "User does not exist."})))))

(defn create-user!
  "Create and persist new user. The request must include alias and password, and
   the alias cannot be in use. Responds with 201 upon successful user creation,
   409 if the alias is already in use or 400 if the given parameters are not
   valid."
  [request]
  (let [user-alias (get-in request [:body :alias])
        password (get-in request [:body :password])]

    (if (not (valid-alias? user-alias))
      (bad-request {:message "Alias is not valid."})

      (if (not (valid-password? password))
        (bad-request {:message "Password is not valid."})

        (let [user (db/create-user! user-alias password)]
          (if (nil? user)
            (conflict {:message "Alias is already in use."})
            (created (str "/api/users/" (:id user)))))))))

(defn filter-division
  [col]
  (map (fn [x] (dissoc x :division)) col))

(defn create-key
  [name]
  (string/replace
   (string/replace
    (string/lower-case name)
    " " "-")
   "_" "-"))

(defn get-division
  [division]
  (let [division-teams (filter-division
                        (db/find-teams-by-division-id (:id division)))
        division-matches (filter-division
                          (db/find-matches-by-division (:id division)))]
    (ok {:division (assoc division
                          :teams division-teams
                          :matches division-matches)})))

(defn get-division-by-id
  [id]
  (let [division (db/get-division-by-id (Integer. id))]
    (if (not (nil? division))
      (get-division division)
      (not-found "Division does not exist."))))

(defn find-division-by-key
  [division-key]
  (let [division (db/find-division-by-key division-key)]
    (if (not (nil? division))
      (get-division division)
      (not-found "Division does not exist."))))

(defn get-divisions
  [request]
  (let [division-key (get-in request [:params :key])]
    (if (not (nil? division-key))
      (find-division-by-key division-key)
      (ok {:divisions (db/get-divisions)}))))

(defn create-division!
  [request]
  (let [division-name (get-in request [:body :name])
        division-key (create-key division-name)
        division (db/create-division! division-name division-key)]
    (if (not (nil? division))
      (created (str "/api/divisions/" (:id division)))
      (bad-request (str "Could not create division: " division-name)))))

(defn get-teams
  [request]
  (let [params (:params request)
        team-key (:teamKey params)]
    (if (not (nil? team-key))
      (ok {:team (db/get-team-by-key team-key)})
      (let [division-id (:divisionId params)
            division-key (:divisionKey params)
            teams (if (not (nil? division-id))
                    (db/find-teams-by-division-id (Integer. division-id))
                    (if (not (nil? division-key))
                      (db/find-teams-by-division-key division-key)
                      (db/get-teams)))]
        (ok {:teams teams})))))

(defn create-team!
  [request]
  (let [body (:body request)
        team-name (:name body)
        team-key (create-key team-name)
        division (:division body)
        team (db/create-team! team-name team-key division)]
    (if (not (nil? team))
      (created (str "/api/teams/" (:id team)))
      (bad-request (str "Could not create team: " team-name)))))

(defn get-matches
  [request]
  (let [params (:params request)
        division (:division params)
        team (:team params)]

    (if (not (nil? team))
      ;; If we were given a team we only get the matches for that specfic
      ;; team. This also limits per division, as a team only plays in one
      ;; division at this moment.
      {:status 200
       :body {:matches (db/find-matches-by-team (Integer. team))}}

      ;; Otherwise we should be given the division at least, and limit our
      ;; match selection based on that.
      (if (not (nil? division))
        {:status 200
         :body {:matches (db/find-matches-by-division (Integer. division))}}

        ;; If we are not given either teams or division we have a too broad
        ;; search, as the resulting list may become large.
        {:status 400
         :body {:message "Either team or division must be given."}}))))
