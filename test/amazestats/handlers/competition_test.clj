(ns amazestats.handlers.competition-test
  (:require [clojure.test :refer :all]
            [amazestats.handlers.competition :refer :all]
            [amazestats.database.competition :as db]))

(deftest get-all-competitions-test

  (testing "200 No competitions exist"
    (with-redefs [db/get-all-competitions (fn [] '())]
      (is (= (get-all-competitions)
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
      (with-redefs [db/get-all-competitions (fn [] competitions)]
        (is (= (get-all-competitions)
               {:status 200
                :headers {}
                :body {:competitions competitions}}))))))

  (testing "500 Error occurred in DB"
    (with-redefs [db/get-all-competitions (fn [] nil)]
      (is (= (get-all-competitions)
             {:status 500
              :headers {}
              :body {:message "Internal server error"}}))))

(deftest get-competition-test

  (testing "200 Get correct competition"
    (let [competition {:id 1
                       :key "korpen-volleyboll"
                       :name "Korpen Volleyboll"}]
      (with-redefs [db/get-competition-by-id (fn [_] competition)]
        (is (= (get-competition-by-id "1")
               {:status 200
                :headers {}
                :body {:competition competition}})))))

  (testing "404 Competition does not exist"
    (with-redefs [db/get-competition-by-id (fn [_] nil)]
      (is (= (get-competition-by-id "1")
             {:status 404
              :headers {}
              :body {:message "Competition does not exist."}})))))

(deftest get-competition-admins-test

  (testing "200 Get admins OK"
    (with-redefs [db/get-competition-admins (fn [_] '({:admin 1
                                                       :alias "Ken"}
                                                      {:admin 2
                                                       :alias "Barbie"}))]
      (is (= (get-competition-admins "1")
             {:status 200
              :headers {}
              :body {:admins '({:id 1
                                :alias "Ken"}
                               {:id 2
                                :alias "Barbie"})}}))))


  (testing "404 Competition does not exist"
    (with-redefs [db/get-competition-admins (fn [_] '())
                  db/get-competition-by-id (fn [_] nil)]
      (is (= (get-competition-admins "1")
             {:status 404
              :headers {}
              :body {:message "Competition does not exist."}}))))

  (testing "200 Empty list of admins"
    (with-redefs [db/get-competition-admins (fn [_] '())
                  db/get-competition-by-id (fn [_] {:id 1
                                                    :key "korpen"
                                                    :name "Korpen V"})]
      (is (= (get-competition-admins "1")
             {:status 200
              :headers {}
              :body {:admins '()}})))))
  
