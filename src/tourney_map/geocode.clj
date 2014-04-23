(ns tourney-map.geocode)

(defn geocode-tourneys [zips tourneys]
  "for each tourney, look up its zipcode in zips and add"
  (letfn [(geocode [tourney]
            (let [zipdata (zips (tourney :zip))]
              (assoc tourney :lat (get zipdata :lat "") :lng (get zipdata :lng ""))))]
    (map geocode tourneys)))
