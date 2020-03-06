(ns wolley.main
  (:require [ring.middleware.defaults :refer [api-defaults wrap-defaults]]
            [wolley.routes :refer [handlers]]))

(def -main (wrap-defaults handlers api-defaults))
