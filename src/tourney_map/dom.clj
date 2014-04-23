(ns tourney-map.dom
  (:require [hiccup.page :as page]
            [tourney-map.config :refer [config]]
            [hiccup.core :refer [html]]))

(defn index []
  (page/html5
   [:head
    [:title "Chess Map"]
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
    [:link {:rel "stylesheet" :href "http://cdn.leafletjs.com/leaflet-0.5/leaflet.css"}]]
   [:body
    [:div#controls
     [:select {:id (-> config :ui :state-select-id)}
      (for [state (config :tourney-states)] [:option {:value state} state])]]
    [:div {:id ((config :ui) :map-id) :style "height: 600px; width: 900px;"}]
    [:script {:src "http://cdn.leafletjs.com/leaflet-0.5/leaflet.js"}]
    [:script {:type "text/javascript" :src "js/main.js"}]
    ]))
