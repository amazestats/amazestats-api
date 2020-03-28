(ns amazestats.database.season-test
  (:require [amazestats.database.season :refer :all]
            [clojure.test :refer :all]
            [clojure.java.jdbc :as jdbc]))

(deftest get-seasons-by-division-test

  (testing "Returns valid list of season maps"
    (let [seasons (list {:id 1
                         :key "2019"
                         :name "2019"
                         :division 4}
                        {:id 2
                         :key "2018"
                         :name "2018"
                         :division 4})]
      (with-redefs [jdbc/query (fn [_s _q] seasons)]
        (is (= (get-seasons-by-division 4)
               seasons)))))

  (testing "Returns empty list if no seasons exist in division"
    (with-redefs [jdbc/query (fn [_s _q] ())]
      (is (= (get-seasons-by-division 4)
             ())))))
