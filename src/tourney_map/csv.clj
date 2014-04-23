(ns tourney-map.csv
  (:require [clojure-csv.core :as csv]))

(defn wrap-zip-data [coll]
  "take a vector of strings representing a csv row
and return hash map of the data used in this app."
  (let [[e1 e2 e3 e4 e5 e6 e7] (take 7 coll)]
    {:id e1 :city e3 :state e4 :lat e6 :lng e7}))

(defn raw-csv-string-to-maps [csv-string]
  "take a string of CSV data and produce a hash map of data
used by this app."
  (map wrap-zip-data (drop 1 (csv/parse-csv csv-string))))


