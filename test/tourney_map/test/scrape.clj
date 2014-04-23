(ns tourney-map.test.scrape
  (:use clojure.test
        tourney-map.scrape))

(deftest test-scrape
  (testing "extract-zip-code"
    (is (= true (nil? (extract-zip-code "123 Main St"))))
    (is (= "19147" (extract-zip-code "123 Main Street, Philadelphia, PA 19147-2102")))))

;; TODO: figure out why these tests fail, they seem like they should pass.
(deftest test-bold
  (testing "test bold-tag?"
    (is (= false (bold-tag? [])))
    (is (= false (bold-tag? {})))
    (is (= true (bold-tag? {:tag :b :content '()})))
    (is (= false (bold-tag? 1)))))

(deftest test-parse-heading
  (testing "parse empty heading"
    (is (= {:date nil :name nil :other-state nil}
           (parse-heading {:tag :b}))))
  (testing "parse no date heading"
    (let [test-data '{:tag :p,
               :attrs {:class "heading1"},
               :content ({:tag :b,
                          :attrs nil,
                          :content ("Apr. 11-13")}
                         "   WKU MasterMind Open   "
                         {:tag :b,
                          :attrs nil,
                          :content ("Kentucky")})}]
      (is (= {:date "Apr. 11-13" :name "WKU MasterMind Open"
              :other-state "Kentucky"}
           (parse-heading test-data)))
)))

