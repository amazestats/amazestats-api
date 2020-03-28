(ns amazestats.handlers.division-test
  (:require [clojure.test :refer :all]
            [amazestats.handlers.division :refer :all]
            [amazestats.database.division :as db]
            [amazestats.database.competition :as competition-db]))

(deftest get-division-by-id-test

  (testing "404 Division does not exist"
    (with-redefs [db/get-division-by-id (fn [_] nil)]
      (is (= (get-division-by-id "17")
             {:status 404
              :headers {}
              :body {:message "Division does not exist in competition."}}))))

  (testing "200 Get correct division"
    (let [division {:id 17 :key "division-1" :name "Division 1" :competition 1}]
      (with-redefs [db/get-division-by-id (fn [_] division)]
        (is (= (get-division-by-id "17")
               {:status 200
                :headers {}
                :body {:division division}}))))))

(deftest get-all-divisions-test

  (testing "200 Divisions exists"
    (let [divisions (list {:id 1
                           :key "div-1"
                           :name "Div 1"
                           :competition 1}
                          {:id 1
                           :key "div-2"
                           :name "Div 2"
                           :competition 1})]
      (with-redefs [db/get-all-divisions (fn [] divisions)]
        (is (= (get-all-divisions)
               {:status 200
                :headers {}
                :body {:divisions divisions}}))))))

(deftest find-divisions-by-key-test

  (testing "200 Found division"
    (let [divisions (list
                      {:id 1 :key "div-1" :name "Div 1" :competition 1})]
      (with-redefs [db/get-divisions-by-key (fn [_] divisions)]
        (is (= (find-divisions-by-key "div-1")
               {:status 200
                :headers {}
                :body {:divisions divisions}})))))

  (testing "200 Divisions not found"
    (with-redefs [db/get-divisions-by-key (fn [_] ())]
      (is (= (find-divisions-by-key "premier-league")
             {:status 200
              :headers {}
              :body {:divisions ()}})))))

(deftest find-divisions-by-competition-test

  (testing "200 Found divisions"
    (let [divisions (list
                      {:id 1 :key "div-1" :name "Div 1" :competition 1}
                      {:id 1 :key "div-2" :name "Div 2" :competition 1})] 
      (with-redefs [db/get-divisions-by-competition (fn [_] divisions)]
        (is (= (find-divisions-by-competition "1")
               {:status 200
                :headers {}
                :body {:divisions divisions}})))))

  (testing "200 No divisions found"
    (with-redefs [db/get-divisions-by-competition (fn [_] (list))
                  competition-db/get-competition-by-id (fn [_] {:id 1
                                                                :key "name"
                                                                :name "Name"})]
      (is (= (find-divisions-by-competition "1")
             {:status 200
              :headers {}
              :body {:divisions (list)}}))))

  (testing "404 Competition does not exist"
    (with-redefs [db/get-divisions-by-competition (fn [_] (list))
                  competition-db/get-competition-by-id (fn [_] nil)]
      (is (= (find-divisions-by-competition "1")
             {:status 404
              :headers {}
              :body {:message "Competition does not exist."}})))))
