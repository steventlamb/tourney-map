(defproject tourney-map "0.1.1"
  :description "Build a static website for finding chess tournaments."
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-2202"]
                 [clojure-csv/clojure-csv "2.0.1"]
                 [hiccup "1.0.5"]
                 [cljs-ajax "0.2.3"]
                 [prismatic/dommy "0.1.2"]
                 [enlive "1.1.5"]]
  :main tourney-map.main
  :plugins [[lein-cljsbuild "1.0.3"]]
  :profiles
  {:dev
   {:dependencies 
    [
     [org.clojars.gjahad/debug-repl "0.3.3"]
     ;; debug repl:
     ;; * to use: add [alex-and-georges.debug-repl :refer :all] to :require
     ;;           and put (debug-repl) in code.
     ]
    }}
  :cljsbuild
  {:builds
   [
    {:source-paths ["src/tourney_map_client"]
     :compiler {:output-to "static/js/main.js"
                :output-dir "static/js/"
                :externs ["src/tourney_map_client/externs.js"]
                :source-map "static/js/main.js.map"
                :optimizations :whitespace
                :pretty-print true}}]})

