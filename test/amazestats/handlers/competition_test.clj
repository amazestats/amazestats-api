(ns amazestats.handlers.competition-test
  (:require [clojure.test :refer :all]
            [amazestats.handlers.competition :refer :all]
            [amazestats.database :as db]))

(deftest get-competitions-test

  (testing "200 No competitions exist"
    (with-redefs [db/get-competitions (fn [] '())]
      (is (= (get-competitions)
             {:status 200
              :headers {}
              :body {:competitions '()}}))))

  (testing "200 Retrieved correct list"
    (let [competitions '({:id 1
                          :key "korpen-volleyboll"
                          :name "Korpen Volleyboll"}
                         {:id 3
                          :key "korpen-innebandy"
                          :name "Korpen Innebandy"})]
      (with-redefs [db/get-competitions (fn [] competitions)]
        (is (= (get-competitions)
               {:status 200
                :headers {}
                :body {:competitions competitions}}))))))

  (testing "500 Error occured in DB"
    (with-redefs [db/get-competitions (fn [] nil)]
      (is (= (get-competitions)
             {:status 500
              :headers {}
              :body {:message "Internal server error"}}))))

(deftest get-competition-test

  (testing "200 Get correct competition"
    (let [competition {:id 1
                       :key "korpen-volleyboll"
                       :name "Korpen Volleyboll"}]
      (with-redefs [db/get-competition (fn [n] competition)]
        (is (= (get-competition "1")
               {:status 200
                :headers {}
                :body {:competition competition}})))))

  (testing "404 Competition does not exist"
    (with-redefs [db/get-competition (fn [n] nil)]
      (is (= (get-competition "1")
             {:status 404
              :headers {}
              :body {:message "Competition does not exist."}})))))
