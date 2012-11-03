(ns
  ^{:author "Christopher Meiklejohn"
    :doc "Observables implementation."}
  reba.observable
  (:require [goog.dom :as dom]))

(defprotocol Observable
  "Observable protocol, which handles triggering functions when objects
  change."
  (add! [object observer-name observer-fn]
        "Bind a watcher to fire when the object changes."))

(extend-type Atom
  Observable
  (add! [object observer-name observer-fn]

    ;; Setup watcher.
    (add-watch object observer-name observer-fn)))
