(ns amazestats.handlers.user-test
  (:require [clojure.test :refer :all]
            [amazestats.handlers.user :refer :all]
            [amazestats.database.user :as db]))

(def valid-password "somethingcool")
(def too-long-alias "2139129391239219312949125821491298128999999999999999")

(deftest get-users-test

  (testing "200 Empty list on empty database"
    (with-redefs [db/get-all-users (fn [] ())]
      (is (= {:status 200
              :headers {}
              :body {:users ()}}
             (get-all-users)))))

  (testing "200 Correct list of users returned"
    (with-redefs [db/get-all-users (fn []
                                     (list {:id 17
                                            :alias "emini"
                                            :password valid-password}
                                           {:id 19
                                            :alias "birk"
                                            :password valid-password}))]
      (is (= (get-all-users)
             {:status 200
              :headers {}
              :body {:users (list {:id 17 :alias "emini"}
                                  {:id 19 :alias "birk"})}})))))

(deftest get-user-test

  (testing "200 correct user data"
    (with-redefs [db/get-user-by-id (fn [_] {:id 99
                                             :alias "emini"
                                             :password valid-password})]
      (is (= {:status 200
              :headers {}
              :body {:user {:id 99 :alias "emini"}}}
             (get-user-by-id "99")))))

  (testing "404 when user does not exist"
    (with-redefs [db/get-user-by-id (fn [_] nil)]
      (is (= (get-user-by-id "1000")
             {:status 404
              :headers {}
              :body {:message "User does not exist."}})))))

(deftest create-user-test
  (testing "400 when alias is missing"
    (with-redefs [db/create-user! (fn [_alias _password] nil)]
      (is (= (create-user! {:body {}})
             {:status 400
              :headers {}
              :body {:message "Alias is not valid."}}))))

  (testing "400 when alias is too long"
    (with-redefs [db/create-user! (fn [_alias _password] nil)]
      (is (= (create-user! {:body {:alias too-long-alias}})
             {:status 400
              :headers {}
              :body {:message "Alias is not valid."}}))))

  (testing "400 when alias contains bad characters"
    (with-redefs [db/create-user! (fn [_alias _password] nil)]
      (is (= (create-user! {:body {:alias "Flora Diamond"
                                   :password valid-password}})
             {:status 400
              :headers {}
              :body {:message "Alias is not valid."}}))))

  (testing "409 when alias is already used"
    (with-redefs [db/create-user! (fn [_alias _password] nil)]
      (is (= (create-user! {:body {:alias "emini"
                                   :password valid-password}})
             {:status 409
              :headers {}
              :body {:message "Alias is already in use."}}))))

  (testing "400 When password is too short"
    (with-redefs [db/create-user! (fn [_alias _password] nil)]
      (is (= (create-user! {:body {:alias "emini"
                                   :password "tooshor"}})
             {:status 400
              :headers {}
              :body {:message "Password is not valid."}}))))

  (testing "400 When password is missing"
    (with-redefs [db/create-user! (fn [_alias _password] nil)]
      (is (= (create-user! {:body {:alias "emini"}})
             {:status 400
              :headers {}
              :body {:message "Password is not valid."}}))))

  (testing "201 when correct input"
    (with-redefs [db/create-user! (fn [alias password] {:id 17
                                                        :alias alias
                                                        :password password})]
      (is (= (create-user! {:body {:alias "emini" :password valid-password}})
             {:status 201
              :headers {"Location" "/api/users/17"}})))))
