(ns amazestats.database.init
  (:require [amazestats.database
             [core :refer [db-spec]]
             [user :as user-db]
             [competition :as competition-db]]
            [clojure.tools.logging :as log]
            [clojure.java.io :as io]
            [clojure.java.jdbc :as jdbc]))

(defn initialized? []
  (not (empty?
         (jdbc/query
           db-spec
           [(slurp (io/resource "sql/initialized.sql"))]))))

(defn init []
  (log/debug "Creating database tables...")
  (jdbc/execute! db-spec [(slurp (io/resource "sql/schema.sql"))])

  (log/info "Creating initial competition and admin...")
  (let [user (user-db/create-user! "admin" "admin")
        competition (competition-db/create-competition! "Korpen Volleyboll"
                                                        "korpen-volleyboll")]
    (competition-db/add-competition-admin! (:id competition)
                                           (:id user))))
