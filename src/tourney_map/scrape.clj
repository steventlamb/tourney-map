(ns tourney-map.scrape
  (:require
   [clojure.string :as string]
   [net.cgrand.enlive-html :as html]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; utility functions
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def nil-if-empty-string #(if (string/blank? %) nil %))
(defn extract-zip-code [text]
  (->> (re-find #" [0-9]{5}" text) (drop 1) (apply str) (nil-if-empty-string)))
(def extract-city-state #(apply str (re-find #"[a-zA-Z-]{3,}, [a-zA-Z]{2}" %)))
(def missing-location? #(= "" (% :city-state) (% :zip)))
(def in-other-state? #(not (nil? (% :other-state))))
(def bold-tag? #(= (get % :tag) :b))

(defn text-or-nil [form]
  "if form is empty, or blank, or whatever, return nil.
Otherwise, return form as text."
  (let [unwrapped (if (sequential? form) (first form) form)
        text (html/text unwrapped)]
  (if (string/blank? text) nil text)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; parse functions
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn parse-heading [heading]
  "Parse dom heading to produce a map, where values exist

  Each heading is represented by a <p class=\"heading1\"></p>
  tag. Within this tag, there may be data in <b></b> tags, which will
  be, first, the date, and then another state, which we keep around
  but don't really care about."
  (let [[first-bold? remaining] (split-with bold-tag? (heading :content))
        [other second-bold?] (split-with (complement bold-tag?) remaining)
        date (text-or-nil first-bold?)
        name (text-or-nil other)
        other-state (text-or-nil second-bold?)]
    (into {}
        {:date date
         :name (if (nil? name) nil (string/replace name "   " ""))
         :other-state other-state})))

(defn parse-listing [listing]
  {:body listing
   :city-state (extract-city-state listing)
   :zip (extract-zip-code listing)})

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; parse functions
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn scrape-tourneys [state-page]
  (let [headings (html/select state-page [:p.heading1])
        listings (map html/text (html/select state-page [:p.listing]))
        pairs (map
               ;; TODO: run parse heading/parse body and merge these two here
               (fn [heading listing]
                 (let [heading-data (parse-heading heading)]
                   {:name (heading-data :name)
                    :date (heading-data :date)
                    :other-state (heading-data :other-state)
                    :body listing
                    :city-state (extract-city-state listing)
                    :zip (extract-zip-code listing)}))
               headings listings)
        ]
    pairs))

(defn scrape-states-from-upcoming-page [page]
  (let [links (html/select page [:td :a])
        hrefs (map #(get (get % :attrs) :href) links)
        state-links (filter #(not (= (.indexOf % "STATE") -1)) hrefs)
        state-chunks (map #(.substring % (.indexOf % "STATE")) state-links)
        states (map #(.substring % 6) state-chunks)]
    (vec states)))
