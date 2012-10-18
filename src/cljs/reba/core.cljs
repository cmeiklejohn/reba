(ns reba.core
  (:require [hiccups.runtime :as hiccusrt]
            [reba.materializable :as materializable])
  (:require-macros [hiccups.core :as hiccups]))

;; Define list of names.
(def list-of-names (atom []))

(defn main []
  "Initialize the web application."

  ;; Add materializers with default value.
  (materializable/add-materializer! list-of-names
                                    :list-view
                                    "container"
                                    (fn [names]
                                      (hiccups/html [:ul (for [x names] [:li x])]))
                                    ["Chris" "Simon" "Joan"]))

  ;; Bind events to a particular view.
  (materializable/add-listener! list-of-names
                                :list-view,
                                "container"
                                "click"
                                (fn [object event] ["Chris"]))

