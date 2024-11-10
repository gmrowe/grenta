(ns grenta.main-test
  (:require
   [clojure.test :refer [deftest is testing]]
   [grenta.main :as sut]))

(deftest -main-test
  (testing "The main method"
    (is (= "Hello World\n" (with-out-str (sut/-main))))))
