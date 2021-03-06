(ns tourney-map.main
  (:require [tourney-map.config :refer [config]]
            [tourney-map.io :as io]))

(defn -main []
  (let [zips (io/!!read-zips)]
    (letfn [(println-justified [action description]
              (println (format "%-20s%s" action description)))]

      (io/!!delete-static-dir)
      (println-justified "DELETED" "static directory before writing to it.")

    (assert
     (= (config :tourney-states)
        (io/!!get-states-from-upcoming-page)))
    (println-justified "CONFIRMED" "states urls haven't changed.")

    (dorun (pmap #(io/!!export-tourneys zips %) (config :tourney-states)))
    (println-justified "WROTE" "geocoded .edn files to static/data/")

    (io/!!write-index)
    (println-justified "WROTE" "index.html to static/"))))

