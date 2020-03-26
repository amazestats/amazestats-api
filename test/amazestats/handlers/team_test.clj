(ns amazestats.handlers.team-test
  (:require [clojure.test :refer :all]
            [amazestats.handlers.team :refer :all]
            [amazestats.database.team :as db]
            [amazestats.database.competition :as competition-db]
            [amazestats.database.division :as division-db]))

(deftest get-team-by-id-test
  
  (testing "200 Team found"
    (let [team {:id 17
                :key "arsenal"
                :name "Arsenal"
                :comeptition 1
                :division 1}]
      (with-redefs [db/get-team-by-id (fn [_] team)]
        (is (= (get-team-by-id "17")
               {:status 200
                :headers {}
                :body {:team team}})))))

  (testing "404 Team does not exist"
    (with-redefs [db/get-team-by-id (fn [_] nil)]
      (is (= (get-team-by-id "17")
             {:status 404
              :headers {}
              :body {:message "Team does not exist."}})))))

(deftest get-all-teams-test

  (testing "200 Teams found"
    (let [teams
          (list
            {:id 1 :key "bury" :name "Bury" :competition 1 :division 1}
            {:id 2 :key "derby" :name "Derby" :competition 1 :division 1})]
      (with-redefs [db/get-all-teams (fn [] teams)]
        (is (= (get-all-teams)
               {:status 200
                :headers {}
                :body {:teams teams}})))))

  (testing "200 No teams found"
    (with-redefs [db/get-all-teams (fn [] (list))]
      (is (= (get-all-teams)
             {:status 200
              :headers {}
              :body {:teams (list)}})))))

(deftest find-teams-by-competition-test
  
  (testing "200 Teams found"
    (let [teams
          (list
            {:id 1 :key "bury" :name "Bury" :competition 1 :division 1}
            {:id 2 :key "derby" :name "Derby" :competition 1 :division 1})]
      (with-redefs [db/get-teams-by-competition (fn [_] teams)]
        (is (= (find-teams-by-competition "1")
               {:status 200
                :headers {}
                :body {:teams teams}})))))

  (testing "200 No teams found, competition exists"
    (with-redefs [db/get-teams-by-competition (fn [_] (list))
                  competition-db/get-competition-by-id (fn [_] {:id 1
                                                                :key "key"
                                                                :name "Key"})]
      (is (= (find-teams-by-competition "1")
             {:status 200
              :headers {}
              :body {:teams (list)}}))))

  (testing "404 Competition does not exist"
    (with-redefs [db/get-teams-by-competition (fn [_] (list))
                  competition-db/get-competition-by-id (fn [_] nil)]
      (is (= (find-teams-by-competition "1")
             {:status 404
              :headers {}
              :body {:message "Competition does not exist."}})))))


(deftest find-teams-by-division-test
  
  (testing "200 Teams found"
    (let [teams
          (list
            {:id 1 :key "bury" :name "Bury" :competition 1 :division 1}
            {:id 2 :key "derby" :name "Derby" :competition 1 :division 1})]
      (with-redefs [db/get-teams-by-division (fn [_] teams)]
        (is (= (find-teams-by-division "1")
               {:status 200
                :headers {}
                :body {:teams teams}})))))

  (testing "200 No teams found, division exists"
    (with-redefs [db/get-teams-by-division (fn [_] (list))
                  division-db/get-division-by-id (fn [_] {:id 1
                                                          :competition 1
                                                          :key "key"
                                                          :name "Key"})]
      (is (= (find-teams-by-division "1")
             {:status 200
              :headers {}
              :body {:teams (list)}}))))

  (testing "404 Division does not exist"
    (with-redefs [db/get-teams-by-division (fn [_] (list))
                  division-db/get-division-by-id (fn [_] nil)]
      (is (= (find-teams-by-division "1")
             {:status 404
              :headers {}
              :body {:message "Division does not exist."}})))))
