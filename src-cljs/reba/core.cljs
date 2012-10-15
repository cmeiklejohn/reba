(ns reba.core
  (:require [clojure.string :as string]))

(defn render-watcher! [x y a b]
  "Watcher function, which applies the render function to the iref it's
  bound to and stashes the rendered content back in the objects meta
  data. The render function, which is applied is assumed to be
  referentially transparent."
  (alter-meta! y (fn [m]
    (merge m {:rendered (apply (:render-fn (meta y)) [b])}))))

(defn watch-and-render! [x render-fn]
  "Given an iref, store a render function and bind a watcher to call
  that function on change."
  (alter-meta! x (fn [m] (merge m {:render-fn render-fn})))
  (add-watch x :render-watcher render-watcher!))

(defn main []
  (def my-name (atom (str "Chris")))
  (watch-and-render! my-name string/upper-case)
  (swap! my-name (fn [] (str "Simon")))
  (.log js/console (:rendered (meta my-name))))
