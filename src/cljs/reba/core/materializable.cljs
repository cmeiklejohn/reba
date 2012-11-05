(ns
  ^{:author "Christopher Meiklejohn"
    :doc "Materializable implementation."}
  reba.core.materializable
  (:require [goog.dom :as dom]
            [reba.core.observable :as observable]))

(defprotocol Materializable
  "Materializable protocol, which handles materializing views and
  drawing them into the DOM."
  (add! [object materializer-name node materializer-fn]
        "Setup a materializer and trigger immediately. Given an object,
        materializer name, DOM node and function, bind a watcher which
        will, when the object is changed, generate a materialized
        view."))

(defn- watch! [node materializer-fn value]
  "Watcher function for materializer. When an object is changed,
  this function is triggered to re-render the content into the
  DOM."
  (set! (.-innerHTML (dom/getElement node)) (apply materializer-fn [value])))

(extend-type Atom
  Materializable
  (add! [object materializer-name node materializer-fn]
    (observable/add! object materializer-name
                     (partial watch! node materializer-fn))
    (swap! object (fn [] (deref object)))))
