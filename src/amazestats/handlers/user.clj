(ns amazestats.handlers.user
  (:require [amazestats.database.user :as db]
            [amazestats.util.core :refer [create-key]]
            [amazestats.util.filter :refer [filter-password]]
            [amazestats.util.response :refer [bad-request
                                              conflict
                                              created
                                              not-found
                                              ok]]
            [amazestats.util.validators :refer [valid-alias? valid-password?]]))

(defn get-all-users
  "Create response with list of users."
  []
  (ok {:users (map
                (fn [user] (filter-password user))
                (db/get-all-users))}))

(defn get-user-by-id
  "Retrieve specific user by its user ID."
  [id]
  (let [user (filter-password (db/get-user-by-id (Integer. id)))]
    (if (not (nil? user))
      (ok {:user user})
      (not-found "User does not exist."))))

(defn create-user!
  "Create and persist new user. The request must include alias and password, and
   the alias cannot be in use. Responds with 201 upon successful user creation,
   409 if the alias is already in use or 400 if the given parameters are not
   valid."
  [request]
  (let [user-alias (get-in request [:body :alias])
        password (get-in request [:body :password])]

    (if (not (valid-alias? user-alias))
      (bad-request "Alias is not valid.")

      (if (not (valid-password? password))
        (bad-request "Password is not valid.")

        (let [user (db/create-user! user-alias password)]
          (if (nil? user)
            (conflict "Alias is already in use.")
            (created (str "/api/users/" (:id user)))))))))

