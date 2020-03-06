(ns wolley.main
  (:require [ring.middleware.defaults :refer [api-defaults wrap-defaults]]
            [wolley.routes :refer [handlers]]))

(def handler (wrap-defaults handlers api-defaults))
