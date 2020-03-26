(ns amazestats.handlers.match-test
  (:require [clojure.test :refer :all]
            [amazestats.handlers.match :refer :all]
            [amazestats.database.match :as db]
            [amazestats.database.season :as season-db]))


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
