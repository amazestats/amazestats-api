(ns amazestats.util.validators-test
  (:require [clojure.test :refer :all]
            [amazestats.util.validators :refer :all]))

(def too-short-alias "ei")
(def too-long-alias "uinsduasndouiasndaoisdaoiasndaosidnasoidnaoidnsaidsi")

(deftest valid-alias-test

  (testing "Alias is too short."
    (is (= false (valid-alias? too-short-alias))))

  (testing "Alias is too long."
    (is (= false (valid-alias? too-long-alias))))

  (testing "Alias contain spaces."
    (is (= false (valid-alias? "emil nilsson"))))

  (testing "Alias contain exclamation mark."
    (is (= false (valid-alias? "emii!"))))

  (testing "Alias contain greater than."
    (is (= false (valid-alias? "e>mini"))))

  (testing "Alias contain semi-colon."
    (is (= false (valid-alias? "emi;ni"))))

  (testing "Alias is fine."
    (is (= true (valid-alias? "emini")))))
