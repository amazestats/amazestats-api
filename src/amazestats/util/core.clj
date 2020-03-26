(ns amazestats.util.core
  (:require [clojure.string :as string]))

(defn create-key
  [name]
  (string/replace
   (string/replace
    (string/lower-case name)
    " " "-")
   "_" "-"))

