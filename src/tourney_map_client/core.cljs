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

(defn plot-tourneys [tourneys]
  (.log js/console (str tourneys))
)

(defn get-state-data [event]
  (.log js/console event)
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
