(ns amazestats.util.response)

;;; Success

(defn ok
  [body]
  {:status 200
   :headers {}
   :body body})

(defn created
  [location-uri]
  {:status 201
   :headers {"Location" location-uri}})


;;; Client Errors

(defn unauthorized
  []
  {:status 401
   :headers {}
   :body {:message "You must be authenticated to access this resource."}})

(defn forbidden
  []
  {:status 403
   :headers {}
   :body {:message "Forbidden"}})

(defn not-found
  [message]
  {:status 404
   :headers {}
   :body {:message message}})

(defn conflict
  [body]
  {:status 409
   :headers {}
   :body body})


;;; Server Errors

(defn internal-error
  []
  {:status 500
   :headers {}
   :body {:message "Internal server error"}})
