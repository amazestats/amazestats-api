(ns wolley.util.parse
  (:require [clojure.string :as string]))

(defn join [& strings] (string/join " " strings))
