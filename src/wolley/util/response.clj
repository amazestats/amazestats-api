(ns wolley.util.response)

(defn ok
  [body]
  {:status 200
   :body body})

(defn internal-error
  []
  {:status 500
   :body {:message "Internal server error"}})