(ns amazestats.authentication
  (:require [buddy.auth.backends :as backends]
            [buddy.hashers :as hasher]
            [buddy.sign.jwt :as jwt]
            [clj-time.core :as time]
            [clj-time.format :as time-format]
            [environ.core :refer [env]]
            [amazestats.database.user :as db]))

;; Secret key used when generating tokens.
(def secret-key (env :secret-key))

(defn expire-date []
  (time/plus (time/now) (time/minutes
                         (read-string (env :token-expiration-period)))))

(defn create-token
  "Create a token containing user ID and expiration date.
  Returns said token and its expiration date as a map."
  [user-id]
  (let [exp (expire-date)
        token (jwt/sign {:user-id user-id :exp exp} secret-key)]
    {:token token
     :exp (time-format/unparse (time-format/formatters :date-time) exp)}))

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
