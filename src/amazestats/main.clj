(ns amazestats.main
  (:require [ring.middleware.defaults :refer [api-defaults wrap-defaults]]
            [amazestats.database.init :as db]
            [amazestats.database.migration :as migration]
            [amazestats.routes :refer [handlers]]
            [clojure.tools.logging :as log]))

(defn init []
  (if (db/initialized?)
    (do (log/info "Running database migrations..")
        (migration/migrate))
    (do (log/info "Initializing database...")
        (db/init))))

(def handler (wrap-defaults handlers api-defaults))
