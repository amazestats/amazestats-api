(ns amazestats.main
  (:require [ring.middleware.defaults :refer [api-defaults wrap-defaults]]
            [amazestats.database :as db]
            [amazestats.routes :refer [handlers]]
            [clojure.tools.logging :as log]))

(defn init []
  (if-let [initialized (db/initialized?)]
    (log/info "Skipping database initialization as databasea already exists.")
    (do (log/info "Initializing database...")
        (db/init))))

(def handler (wrap-defaults handlers api-defaults))
