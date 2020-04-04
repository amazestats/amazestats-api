(ns amazestats.util.response
  (:require [clojure.string]))

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

(defn bad-request
  [message]
  {:status 400
   :headers {}
   :body {:message message}})

(defn unauthorized
  []
  {:status 401
   :headers {}
   :body {:message "You must be authenticated to access this resource."}})

(defn forbidden
  ([] (forbidden {:message "Forbidden"}))
  ([body]
   {:status 403
    :headers {}
    :body body}))

(defn not-found
  [& message]
  {:status 404
   :headers {}
   :body {:message (str (clojure.string/join " " message))}})

(defn conflict
  [message]
  {:status 409
   :headers {}
   :body {:message message}})


;;; Server Errors

(defn internal-error
  []
  {:status 500
   :headers {}
   :body {:message "Internal server error"}})
