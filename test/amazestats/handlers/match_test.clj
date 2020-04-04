(ns amazestats.handlers.match-test
  (:require [amazestats.database.competition :as competition-db]
            [amazestats.database.match :as db]
            [amazestats.database.season :as season-db]
            [amazestats.handlers.match :refer :all]
            [clojure.test :refer :all]))

(deftest get-match-by-id-test

  (testing "200 Match returned"
    (let [match {:id 17
                 :season 1
                 :home-team 1
                 :away-team 2}]
      (with-redefs [db/get-match-by-id (fn [_] match)]
        (is (= (get-match-by-id "17")
               {:status 200
                :headers {}
                :body {:match match}})))))

  (testing "404 Match does not exist"
    (with-redefs [db/get-match-by-id (fn [_] nil)]
      (is (= (get-match-by-id "17")
             {:status 404
              :headers {}
              :body {:message "Match does not exist."}})))))

(deftest find-matches-by-season-test

  (testing "200 Matches found"
    (let [matches (list {:id 1
                         :season 1
                         :home-team 1
                         :away-team 2}
                        {:id 2
                         :season 2
                         :home-team 3
                         :away-team 4})]
      (with-redefs [db/get-matches-by-season (fn [_] matches)]
        (is (= (find-matches-by-season "1")
               {:status 200
                :headers {}
                :body {:matches matches}})))))

  (testing "200 No matches found"
    (with-redefs [db/get-matches-by-season (fn [_] ())
                  season-db/get-season-by-id (fn [_] 1)]
      (is (= (find-matches-by-season "1")
             {:status 200
              :headers {}
              :body {:matches ()}}))))

  (testing "404 Season does not exist"
    (with-redefs [db/get-matches-by-season (fn [_] ())
                  season-db/get-season-by-id (fn [_] nil)]
      (is (= (find-matches-by-season "1")
             {:status 404
              :headers {}
              :body {:message "Season does not exist."}})))))

(deftest update-match-referee-test

  (testing "404 The match does not exist"
    (with-redefs [db/set-match-referee (fn [_ _r] nil)
                  db/get-competition-id-for-match (fn [_] 17)
                  db/get-match-by-id (fn [_] nil)
                  competition-db/competition-admin? (fn [_c _u] true)]
      (is (= (update-match-referee "20" {:identity {:user-id 5}
                                         :body {:referee "17"}})
             {:status 404
              :headers {}
              :body {:message "The match does not exist."}}))))

  (testing "403 If the user is not a competition admin"
    (with-redefs [competition-db/competition-admin? (fn [_c _u] false)
                  db/get-match-by-id (fn [_] {:id 20})
                  db/get-competition-id-for-match (fn [_] 17)]
      (is (= (update-match-referee "20" {:identity {:user-id 5}
                                         :body {:referee "17"}})
             {:status 403
              :headers {}
              :body
              {:message
               "The user must be a competition admin to update referees."}}))))

  (testing "400 The given referee does not match a team id"
    (with-redefs [db/set-match-referee (fn [_ _r] nil)
                  db/get-competition-id-for-match (fn [_] 17)
                  db/get-match-by-id (fn [_] {:id 20})
                  competition-db/competition-admin? (fn [_c _u] true)]
      (is (= (update-match-referee "20" {:identity {:user-id 6}
                                         :body {:referee "17"}})
             {:status 400
              :headers {}
              :body {:message "Could not appoint team as referee."}}))))

  (testing "200 The updated resource is returned"
    (with-redefs [db/set-match-referee (fn [_ ref] {:referee ref})
                  db/get-match-by-id (fn [_] {:id 20})
                  db/get-competition-id-for-match (fn [_] 17)
                  competition-db/competition-admin? (fn [_c _u] true)]
      (is (= (update-match-referee "20" {:identity {:user-id 5}
                                         :body {:referee "17"}})
             {:status 200
              :headers {}
              :body {:referee 17}})))))
