(ns amazestats.util.filter)

(defn filter-password
  [user]
  (dissoc user :password))
