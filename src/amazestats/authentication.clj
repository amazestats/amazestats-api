(ns amazestats.authentication
  (:require [buddy.auth.backends :as backends]
            [buddy.hashers :as hasher]
            [buddy.sign.jwt :as jwt]
            [clj-time.core :as time]
            [environ.core :refer [env]]
            [amazestats.database :as db]))

;; Secret key used when generating tokens.
(def secret-key (env :secret-key))

(defn expire-date []
  (time/plus (time/now) (time/minutes
                         (read-string (env :token-expiration-period)))))

(defn create-token
  "Create a JWT token containing user ID and expiration date."
  [user-id]
  (jwt/sign {:user-id user-id :exp (expire-date)} secret-key))

(defn basic-authfn
  "Authentication function for basic authentication backend. Checks that the
   user credentials are valid and returns an identity map if successful."
  [_request auth-data]
  (let [username (:username auth-data)
        password (:password auth-data)
        user (db/get-user-by-alias username)
        user-id (:id user)]
    (if (and (not (nil? user-id))
             (hasher/check password (:password user)))
      {:user-id user-id}
      nil)))

(def basic-backend (backends/basic {:realm "amazestats"
                                    :authfn basic-authfn}))

(def token-backend (backends/jws {:secret secret-key
                                  :token-name "Bearer"}))