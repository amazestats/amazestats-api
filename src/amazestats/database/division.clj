(ns amazestats.database.division
  (:require [amazestats.database.core :refer [db-spec]]
            [clojure.java.jdbc :as jdbc]
            [clojure.tools.logging :as log]
            [amazestats.util.parse :refer [join]]))

(defn get-division-by-id
  [id]
  (first
    (try
      (jdbc/query db-spec ["SELECT * FROM divisions WHERE id = ?" id])
      (catch org.postgresql.util.PSQLException e
        (log/error e "Failed to get division:" id)
        nil))))

(defn find-division-by-key
  [division-key]
  (first ;; Key should be unique, let's treat this as a unique response
    (try
      (jdbc/query db-spec
                  ["SELECT * FROM divisions WHERE key = ?" division-key])
      (catch org.postgresql.util.PSQLException e
        (log/error e "Failed to get division:" division-key)
        nil))))

(defn get-divisions
  []
  (try
    (jdbc/query db-spec ["SELECT * FROM divisions"])
    (catch org.postgresql.util.PSQLException e
      (log/error e "Failed to get divisions.")
      nil)))

(defn create-division!
  [division-name division-key]
  (first
    (try
      (jdbc/insert! db-spec :divisions {:name division-name
                                        :key division-key})
      (catch org.postgresql.util.PSQLException e
        (log/error e "Failed to create division:" division-name)
        nil))))

