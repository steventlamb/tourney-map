(ns tourney-map-client.core
  (:require [ajax.core :refer [GET POST]]
            [dommy.core :as dommy])
  (:require-macros [tourney-map.config :as config]
                   [dommy.macros :refer [sel1]]))

(def config (config/get-config))
(def map-id ((config :ui) :map-id))
(def state-select-id ((config :ui) :state-select-id))
(def tile-url ((config :ui) :tile-url))
(def attribution ((config :ui) :attribution))
(def map-center (clj->js ((config :ui) :map-center)))
(def tileAttrs (clj->js {:maxZoom 18
                         :attribution attribution}))

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
  (let [map (-> js/L (.map map-id) (.setView map-center 8))
        tile-layer (-> js/L
                       (.tileLayer tile-url tileAttrs)
                       (.addTo map))]
    (dommy/listen! (sel1 "#state-select") :click get-state-data)))


(init!)
