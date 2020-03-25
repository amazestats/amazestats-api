(ns amazestats.database.competition
  (:require [amazestats.database.core :refer [db-spec]]
            [clojure.java.jdbc :as jdbc]
            [clojure.tools.logging :as log]
            [amazestats.util.parse :refer [join]]))

(defn create-competition!
  "Create competition.
  Returns the competition object persisted in the database if successful. If an
  error occurs `nil` is returned."
  [competition-name competition-key]
  (try
    (jdbc/insert! db-spec :competitions {:name competition-name
                                         :key competition-key})
    (catch org.postgresql.util.PSQLException e
      (log/error "Failed to create competition:" competition-name))))

(defn get-competitions
  "Create list of competitions from database.
  If an error occurs in the database, `nil` is returned instead."
  []
  (try
    (jdbc/query db-spec ["SELECT id, key, name FROM competitions"])
    (catch org.postgresql.util.PSQLException e
      (log/error "Failed to get competitions." e))))

(defn get-competition
  "Get a competition from the database by its `id`.
  If the competition does not exist or if an error occurs, `nil` is returned."
  [id]
  (first
    (try
      (jdbc/query
        db-spec
        [(join "SELECT c.id, c.key, c.name"
               "FROM competitions c"
               "WHERE id = ?") id])
      (catch org.postgresql.util.PSQLException e
        (log/error "Failed to get competition with ID:" id e)))))

(defn add-competition-admin!
  "Add `user` as admin for `competition`.
  Returns a map of the admin created on success, or nil if the `user` already
  is an admin in the `competition`."
  [competition user]
  (first
    (try (jdbc/insert! db-spec
                       :competition_admins
                       {:admin user
                        :competition competition})
         (catch org.postgresql.util.PSQLException _ nil))))

(defn remove-competition-admin!
  "Remove `user` as admin for `competition`.
  Returns the ID of the `user` removed on success. If the entry is missing in
  the database, the ID returned will be 0. Returns `nil`on failure."
  [competition user]
  (first
    (try (jdbc/delete! db-spec
                       :competition_admins
                       ["competition = ? AND admin = ?"
                        competition user])
         (catch org.postgresql.util.PSQLException e
           (log/error "Failed to remove admin with ID" user
                      "from competition with ID" competition "."
                      e)))))

(defn get-competition-admins
  "Get list of `competition` admins.
  On failure returns `nil`. Note that this will return the empty list both when
  the `competition` does not have any admins and when the `competition` does not
  exist."
  [competition]
  (try
    (jdbc/query
      db-spec
      [(join "SELECT a.admin, u.alias"
             "FROM competition_admins a"
             "INNER JOIN amaze_users u"
             "ON a.admin = u.id"
             "WHERE a.competition = ?")
       competition])
    (catch org.postgresql.util.PSQLException e
      (log/error "Failed to get admins for competition:" competition e))))
