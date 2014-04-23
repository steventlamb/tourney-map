(ns tourney-map.ops
  (:require [clojure.edn :as edn]
            [tourney-map.scrape :as scrape]
            [tourney-map.config :as config]
            [tourney-map.dom :as dom]
            [tourney-map.csv :as csv]
            [net.cgrand.enlive-html :as html]))

;; tools for building seed data for development. There are raw static
;; assets downloaded from the web, then parsed into usable EDN
;; data. In the normal course of development, the necessary EDNs will
;; exist

;; Raw Static Assets Manifest: 
;; * zipcodes.csv - Downloaded from a free
;;   website, gets parsed into an edn file to be useful. This file need
;;   not be present to build the static site, so long as zipcodes.edn
;;   exists.

