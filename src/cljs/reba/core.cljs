(ns reba.core
  (:require [hiccups.runtime :as hiccusrt]
            [reba.materializable :as materializable])
  (:require-macros [hiccups.core :as hiccups]))

(defn generate-list-of-names [names]
  (hiccups/html [:ul (for [x names] [:li x])]))

(defn main []
  ;; Define list of names.
  (def list-of-names (atom []))

  ;; Naive changer.
  (defn changeme []
    (swap! list-of-names (fn [] ["Chris"])))

  ;; Add materializers with default value.
  (materializable/add-materializer! "container" list-of-names :list-view generate-list-of-names ["Chris" "Simon" "Joan"]))

