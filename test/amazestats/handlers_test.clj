(ns amazestats.handlers-test
  (:require [clojure.test :refer :all]
            [amazestats.handlers :refer :all]
            [amazestats.database :as db]))

(deftest get-users-test
  (testing "200 Empty list on empty database"
    (with-redefs [db/get-users (fn [] ())]
      (is (= {:status 200
              :body {:users ()}}
             (get-users nil)))))
  (testing "200 Correct list of users returned"
    (with-redefs [db/get-users (fn [] ({:id 17 :alias "emini"}
                                       {:id 19 :alias "birk"}))]
      (is (= {:status 200
              :body {:users ({:id 17 :alias "emini"}
                             {:id 19 :alias "birk"})}}
             (get-users nil))))))

(deftest get-user-test
  (testing "200 correct user data", 
    (with-redefs [db/get-user (fn [id] {:id id :alias "emini"})]
      (is (= {:status 200
              :body {:user {:id 99 :alias "emini"}}}
             (get-user 99)))))
  (testing "404 when user does not exist"
    (with-redefs [db/get-user (fn [id] nil)]
      (let [response (get-user 1000)]
        (is (and (= 404 (:status response))
                 (= "User does not exist."
                    (get-in response [:body :message])))))))

  (testing "400 when ID is bad (non-int)"
    (with-redefs [db/get-user (fn [id] (is false))] ;; we should not call db
      (let [response (get-user "femton")]
        (is (and (= 400 (:status response))
                 (= "Invalid ID."
                    (get-in response [:body :message]))))))))

(deftest create-user-test
  (testing "400 when alias is missing"
    (with-redefs [db/create-user! (fn [alias] (is false))] ;; should not be called
      (let [response (create-user! {:body {}})]
        (is (and (= 400 (:status response))
                 (= "Alias must be provided."
                   (get-in response [:body :message])))))))

  (testing "400 when alias is too long"
    (with-redefs [db/create-user! (fn [alias] nil)]
      (let [response
            (create-user!
              {:body
               {:alias "2139129391239219312949125821491298128999999999999999"}})]
        (is (and (= 400 (:status response))
                 (= "Alias is not valid."
                    (get-in response [:body :message])))))))
      
  (testing "400 when alias contains bad characters"
    (with-redefs [db/create-user! (fn [alias] nil)]
      (let [response (create-user! {:body {:alias "Flora Diamond"}})]
        (is (and (= 400 (:status response))
                 (= "Alias is not valid."
                    (get-in response [:body :message])))))))

  (testing "409 when alias is already used"
    (with-redefs [db/create-user! (fn [alias] nil)]
      (let [response (create-user! {:body {:alias "emini"}})]
        (is (and (= 409 (:status response))
                 (= "Alias is already in use."
                    (get-in response [:body :message])))))))

  (testing "201 when correct input"
    (with-redefs [db/create-user! (fn [alias] {:id 17 :alias alias})]
      (let [response (create-user! {:body {:alias "emini"}})]
        (is (and (= 201 (:status response))
                 (= {"Location" "api/users/17"})))))))