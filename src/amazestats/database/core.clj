(ns amazestats.database.core
  (:import com.mchange.v2.c3p0.ComboPooledDataSource
           java.net.URI
           org.postgresql.util.PSQLException)
  (:require [clojure.string]
            [environ.core :refer [env]]
            [jdbc.pool.c3p0 :as pool]
            [amazestats.util.parse :refer [join]]))

(def db-uri
  (let [db-url (env :database-url)]
    (if (not (nil? db-url))
      (java.net.URI. db-url))))

(def user-and-password
  (if (not (nil? db-uri))
    (if (not (nil? (.getUserInfo db-uri)))
      (clojure.string/split (.getUserInfo db-uri) #":"))))

(defn generate-spec-from-uri []
  (pool/make-datasource-spec
    {:classname "org.postgresql.Driver"
     :subprotocol "postgresql"
     :user (get user-and-password 0)
     :password (get user-and-password 1)
     :subname
     (if (= -1 (.getPort db-uri))
       (format "//%s%s"
               (.getHost db-uri)
               (.getPath db-uri))
       (format "//%s:%s%s"
               (.getHost db-uri)
               (.getPort db-uri)
               (.getPath db-uri)))}))

(def db-spec
  (if (not (nil? db-uri))
    (generate-spec-from-uri)
    {:dbtype "postgresql"
     :host "database"
     :dbname (env :postgres-db)
     :user (env :postgres-user)
     :password (env :postgres-password)}))
