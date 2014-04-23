(ns tourney-map.io
  (:require [clojure.edn :as edn]
            [clojure.java.io :as java-io]
            [clojure.java.shell :refer [sh]]
            [tourney-map.scrape :as scrape]
            [tourney-map.config :refer [config]]
            [tourney-map.geocode :as geocode]
            [tourney-map.dom :as dom]
            [tourney-map.csv :as csv]
            [net.cgrand.enlive-html :as html]))

;; todo: use this!
(defmacro notify-download-required [form]
  (try
    form
    (catch Exception e
      (throw (Exception. "You need to download stuff.")))))


;;;;;;;;;;;;;;;;
;; scrape io
;;;;;;;;;;;;;;;;

(def !!get-and-parse-html
  "a utility function for grabbing any url and parsing it to an `html`
object"
  #(html/html-resource (java.net.URL. %)))

(defn !!get-states-from-upcoming-page []
  "Not immediately useful, except for validating that the state list
in config is still correct."
  (-> (config :upcoming-gateway-page-url)
      (!!get-and-parse-html)
      (scrape/scrape-states-from-upcoming-page)))

;;;;;;;;;;;;;;;;
;; data io
;;;;;;;;;;;;;;;;

(defn !!write-to-static-dir [filename thing]
  (doto (str (config :static-dir) filename)
    (java-io/make-parents)
    (spit thing)))

(defn !!delete-static-dir []
  (sh "rm" "-rf" (config :static-dir)))

(defn !!read-zips []
  (-> (config :zipcodes-local-path)
      slurp
      edn/read-string))

(defn !!write-index []
  (!!write-to-static-dir "index.html" (dom/index)))

(defn !!export-tourneys [zips state-code]
  (->> state-code
       (str (config :tourney-url-prefix))
       (!!get-and-parse-html)
       (scrape/scrape-tourneys)
       (filter (complement scrape/missing-location?))
       (filter (complement scrape/in-other-state?))
       (geocode/geocode-tourneys zips)
       (prn-str)
       (!!write-to-static-dir (str "/data/" state-code ".edn"))))

