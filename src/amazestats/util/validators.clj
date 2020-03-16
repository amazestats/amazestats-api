(ns amazestats.util.validators)

(defn valid-alias?  [alias]
  (and (string? alias)
       (<= 3 (count alias))
       (>= 50 (count alias))
       (not (nil? (re-matches #"[a-zA-Z0-9_]+" alias)))))

(defn valid-password? [password]
  (and (string? password)
       (<= 8 (count password))))
