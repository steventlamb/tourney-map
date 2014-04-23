
;; eventually this app will have two components:
;; * run the app, which scrapes 50 states into 50 edn files
;; * lesser used utilities, like making the csv -> edn

(defn !!cache-csv []
  "this is broken, but ideally it'll pull down the csv url
and store it locally"
  (spit (config :csv-local-path) (slurp (config :csv-source-url))))


(defn !!make-zipcode-maps-from-csv-file []
  (csv/raw-csv-string-to-maps (slurp (config :csv-source-url))))


(defn repack-zip-csv-seq-as-map [zip-csv-seq]
  "I modeled the data wrong. I don't want to traverse a list of 45000 maps for every single tourney. That would take forever."
  (letfn [(pad-to-5-len [string]
            (apply str (concat (repeat (- 5 (.length string)) "O") (seq string))))]
  (apply hash-map (mapcat #(vector (pad-to-5-len (% :id)) %) zip-csv-seq))))


(defn !!repack-csv []
  "a throwaway function I used to manipulate some data to a faster structure."
  (->> (!!read-zips)
      csv/repack-zip-csv-seq-as-map
      (!!save-to-edn (str (config :project-root-dir) "/data/zipcodes.edn"))))
