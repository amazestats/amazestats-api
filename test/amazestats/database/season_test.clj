(ns amazestats.database.season-test
  (:require [amazestats.database.season :refer :all]
            [clojure.test :refer :all]
            [clojure.java.jdbc :as jdbc]))

(deftest get-season-test

  (testing "Returns season map when entity exists"
    (let [season {:id 16
                  :key "2019"
                  :name "2019"
                  :division 4}]
      (with-redefs [jdbc/query (fn [_s _q] (list season))]
        (is (= (get-season-by-id 1)
               season)))))

  (testing "Returns nil when season does not exist"
    (with-redefs [jdbc/query (fn [_s _q] ())]
      (is (= (get-season-by-id 1)
             nil)))))

(deftest get-seasons-test

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
