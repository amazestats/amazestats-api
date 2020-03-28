(ns amazestats.database.division
  (:import org.postgresql.util.PSQLException)
  (:require [amazestats.database.core :refer [db-spec]]
            [clojure.java.jdbc :as jdbc]
            [clojure.tools.logging :as log]
            [amazestats.util.parse :refer [join]]))

(defn get-all-divisions
  []
  (try
    (jdbc/query db-spec ["SELECT * FROM divisions"])
    (catch org.postgresql.util.PSQLException e (log/error e))))

(defn get-divisions-by-competition
  [competition]
  (try
    (jdbc/query db-spec ["SELECT * FROM divisions WHERE competition = ?"
                         competition])
    (catch org.postgresql.util.PSQLException e
      (log/error "Failed to get divisions in competition:" competition "." e)
      nil)))

(defn get-division-by-id
  [id]
  (first
    (try
      (jdbc/query db-spec ["SELECT * FROM divisions WHERE id = ?" id])
      (catch org.postgresql.util.PSQLException e
        (log/error e "Failed to get division:" id)
        nil))))

(defn get-divisions-by-key
  [division-key]
  (try
    (jdbc/query db-spec ["SELECT * FROM divisions WHERE key = ?" division-key])
    (catch org.postgresql.util.PSQLException e
      (log/error e "Failed to get division:" division-key))))

(defn create-division!
  [competition division-name division-key]
  (first
    (try
      (jdbc/insert! db-spec :divisions {:name division-name
                                        :key division-key
                                        :competition competition})
      (catch org.postgresql.util.PSQLException e
        (log/error "Failed to create division:" division-name e)))))

