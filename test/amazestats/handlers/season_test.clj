(ns amazestats.handlers.season-test
  (:require [amazestats.database.division :as division-db]
            [amazestats.database.season :as db]
            [amazestats.handlers.season :refer :all]
            [clojure.test :refer :all]))

(deftest get-season-by-id-test

  (testing "200 Season exists"
    (let [season {:id 1
                  :key "2019"
                  :name "2019"
                  :competition 1
                  :division 1}]
      (with-redefs [db/get-season-by-id (fn [_] season)]
        (is (= (get-season-by-id "1")
               {:status 200
                :headers {}
                :body {:season season}})))))

  (testing "404 Season does not exist"
    (with-redefs [db/get-season-by-id (fn [_] nil)]
      (is (= (get-season-by-id "1")
             {:status 404
              :headers {}
              :body {:message "Season does not exist."}})))))

(deftest find-seasons-by-division-test
  
  (testing "200 Found seasons"
    (let [seasons (list
                    {:id 1 :key "1" :name "1" :competition 1 :division 1}
                    {:id 2 :key "2" :name "2" :competition 1 :division 1})]
      (with-redefs [db/get-seasons-by-division (fn [_] seasons)]
        (is (= (find-seasons-by-division "1")
               {:status 200
                :headers {}
                :body {:seasons seasons}})))))

  (testing "200 No seasons found"
    (with-redefs [db/get-seasons-by-division (fn [_] (list))
                  division-db/get-division-by-id (fn [_] {:id 1
                                                          :key "1"
                                                          :name "1"})]
      (is (= (find-seasons-by-division "1")
             {:status 200
              :headers {}
              :body {:seasons (list)}}))))

  (testing "404 Division does not exist"
    (with-redefs [db/get-seasons-by-division (fn [_] (list))
                  division-db/get-division-by-id (fn [_] nil)]
      (is (= (find-seasons-by-division "1")
             {:status 404
              :headers {}
              :body {:message "Division does not exist."}})))))
