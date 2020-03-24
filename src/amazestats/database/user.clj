(ns amazestats.database.user
  (:require [amazestats.database.core :refer [db-spec]]
            [buddy.hashers :as hasher]
            [clojure.java.jdbc :as jdbc]
            [clojure.tools.logging :as log]))

(defn create-user! [alias password]
  (first
    (try
      (jdbc/insert! db-spec
                    :amaze_users
                    {:alias alias
                     :password (hasher/derive password)})
      (catch org.postgresql.util.PSQLException e
        (log/error e "Failed to create user:" alias)
        nil))))

(defn get-users []
  (try
    (jdbc/query db-spec ["SELECT * FROM amaze_users"])
    (catch org.postgresql.util.PSQLException e
      (log/error e "Failed to get users.")
      nil)))

(defn get-user [id]
  (first
    (try
      (jdbc/query db-spec ["SELECT * FROM amaze_users WHERE id = ?" id])
      (catch org.postgresql.util.PSQLException e
        (log/error "Failed to get user:" id)
        nil))))

(defn get-user-by-alias
  [alias]
  (first
    (try
      (jdbc/query db-spec ["SELECT * FROM amaze_users WHERE alias = ?" alias])
      (catch org.postgresql.util.PSQLException e
        (log/error "Failed to get user:" alias)
        nil))))
