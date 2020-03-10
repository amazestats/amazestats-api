(ns amazestats.main
  (:require [ring.middleware.defaults :refer [api-defaults wrap-defaults]]
            [amazestats.routes :refer [handlers]]))

(def handler (wrap-defaults handlers api-defaults))
