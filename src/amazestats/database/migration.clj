(ns amazestats.database.migration
  (:require [amazestats.database.core :refer [db-spec]]
            [migratus.core :as migratus]))

(def config {:store                 :database
             :migration-dir         "sql/migrations/"
             :init-script           "init.sql"
             :migration-table-name  "migrations"
             :db                    db-spec})

(defn init
  []
  (migratus/init config))

(defn migrate
  []
  (migratus/migrate config))

(defn rollback
  []
  (migratus/rollback config))

(defn create
  []
  (migratus/create config))
              
