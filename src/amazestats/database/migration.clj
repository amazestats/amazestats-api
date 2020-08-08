(ns amazestats.database.migration
  (:require [amazestats.database.core :refer [db-spec]]
            [clojure.tools.logging :as log]
            [migratus.core :as migratus]
            [migratus.database :as migratus-db]
            [migratus.protocols :as proto]))

(def config {:store                 :database
             :migration-dir         "sql/migrations/"
             :init-script           "init.sql"
             :migration-table-name  "migrations"
             :db                    db-spec})

(defn mark-complete
  "Mark a `migration` as completed."
  [migration]
  (log/info "Marking:" (proto/id migration))
  (let [db (:db config)
        table-name (migratus-db/migration-table-name config)
        migration-name (:name migration)
        migration-id (proto/id migration)]
    (migratus-db/mark-complete db table-name migration-name migration-id)))

(defn mark-all-migrations-complete
  "Marks all migrations as completed without running them."
  []
  (let [store (proto/make-store config)]
    (try
      (proto/connect store)
      (let [migrations (->> (migratus/uncompleted-migrations config store)
                            (sort-by proto/id))]
        (when (seq migrations)
          (loop [[migration & more] migrations]
            (when migration
              (mark-complete migration)
              (recur more)))))

      (catch Exception e
        (log/error "Error occurred during migration marking:" e))
      (finally (proto/disconnect store)))))

(defn create-migration-table
  "Create the migration table."
  []
  (proto/connect (proto/make-store config)))

(defn init
  "Initializes the database and marks all migrations as completed."
  []
  (migratus/init config)
  (create-migration-table)
  (log/info "Marking migrations as complete during initialization.")
  (mark-all-migrations-complete))

(defn migrate
  "Run uncompleted migrations."
  []
  (migratus/migrate config))

(defn rollback
  []
  (migratus/rollback config))

(defn create
  "Create new migration.
  The migration will be created in the migration directory."
  []
  (migratus/create config))
              
