(ns amazestats.util.response)

(defn ok
  [body]
  {:status 200
   :body body})

(defn conflict
  [body]
  {:status 409
   :body body})

(defn internal-error
  []
  {:status 500
   :body {:message "Internal server error"}})

(defn forbidden
  []
  {:status 403
   :body {:message "Forbidden"}})

(defn unauthorized
  []
  {:status 401
   :body {:message "You must be authenticated to access this resource."}})
