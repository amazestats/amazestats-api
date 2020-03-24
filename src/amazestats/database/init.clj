(ns amazestats.database.init
  (:require [amazestats.database.core :refer [db-spec]]
            [amazestats.database.user :as user-db]
            [clojure.java.io :as io]
            [clojure.java.jdbc :as jdbc]))

(defn initialized? []
  (not (empty?
         (jdbc/query
           db-spec
           [(slurp (io/resource "sql/initialized.sql"))]))))

(defn init []
  (jdbc/execute! db-spec [(slurp (io/resource "sql/schema.sql"))])
  (user-db/create-user! "admin" "admin"))

