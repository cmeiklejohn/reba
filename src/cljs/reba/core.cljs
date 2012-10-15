(ns reba.core
  (:require [clojure.string :as string]
            [hiccups.runtime :as hiccusrt])
  (:require-macros [hiccups.core :as hiccups]))

(defn render-watcher! [x y a b]
  "
  Watcher function, which applies the render function to the iref it's
  bound to and stashes the rendered content back in the objects meta
  data. The render function, which is applied is assumed to be
  referentially transparent.
  "
  (alter-meta! y (fn [m]
    (merge m {:rendered (apply (:render-fn (meta y)) [b])}))))

(defn render! [x initial render-fn]
  "
  Given an iref, store a render function and bind a watcher to call
  that function on change.  Immediately execute the watcher with initial
  value.

  render-fn will be called like so (apply render-fn changed-value)
  "
  (alter-meta! x (fn [m] (merge m {:render-fn render-fn})))
  (add-watch x :render-watcher render-watcher!)
  (swap! x (fn [] initial)))

(defn generate-names [names]
  "Generate the HTML elements for the names list."
  [:ul (for [x names] [:li x])])

(defn main []
  (def names (atom []))
  (render! names ["Chris" "Simon"] (fn [x] (hiccups/html (generate-names x))))
  (swap! names (fn [] ["Chris" "Simon" "Joan"]))
  (.log js/console (:rendered (meta names))))
