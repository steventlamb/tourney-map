(ns tourney-map.config
(:require [clojure.edn :as edn]))

(def base-config (edn/read-string (slurp "config.edn")))

(defn make-config [config keep-private]
  "Base config contains a :private map in it, which should be
flattened for the server and dropped for the client"
  (into {}
        (concat
         (if keep-private (get config :private {}) {})
                (dissoc config :private))))

(def config (make-config base-config true))

(defmacro get-config [] (make-config base-config false))

