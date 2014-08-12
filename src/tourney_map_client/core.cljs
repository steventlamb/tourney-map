(ns tourney-map-client.core
  (:require [ajax.core :refer [GET POST]]
            [dommy.core :as dommy])
  (:require-macros [tourney-map.config :as config]
                   [dommy.macros :refer [sel1]]))

;; read config data into something more usable
(def config (config/get-config))
(def map-center (clj->js ((config :ui) :map-center)))
(def map-id (get-in config [:ui :map-id]))
(def state-select-id (get-in config [:ui :state-select-id]))
(def tile-url (get-in config [:ui :tile-url]))
(def attribution (get-in config [:ui :attribution]))
(def tileAttrs (clj->js {:maxZoom 18 :attribution attribution}))

;; make map global
(def gis-map (-> js/L (.map map-id) (.setView map-center 8)))

(def read-state-abbrev #(-> % .-target .-value))

(def build-state-url #(str "data/" % ".edn"))

(defn randomize-coordinate [coordinate]
  (let [r (.random js/Math)
        adjusted-r (- r 0.5)
        scaled-r (/ adjusted-r 100)
        adjusted (+ (js/parseFloat coordinate) scaled-r)] adjusted))

(defn plot-tourneys [tourneys]
  (let [geocoded-tourneys (filter #(not (or (= (:lat %) "") (= (:lng %) ""))) tourneys)]
    (doseq [tourney geocoded-tourneys]
      (let [lat (-> tourney :lat randomize-coordinate)
            lng (-> tourney :lng randomize-coordinate)]
        (-> js/L
            (.marker (clj->js [lat lng]))
            (.addTo gis-map)
            (.bindPopup (:body tourney)))))))

(defn get-state-data [event]
  (-> event
      read-state-abbrev
      build-state-url
      (GET {:handler plot-tourneys})))

(defn init! []
    (-> js/L
        (.tileLayer tile-url tileAttrs)
        (.addTo gis-map))
    (dommy/listen! (sel1 "#state-select") :click get-state-data))

(init!)
