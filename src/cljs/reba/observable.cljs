(ns
  ^{:author "Christopher Meiklejohn"
    :doc "Observable implementation."}
  reba.observable
  (:require [goog.dom :as dom]))

(defprotocol Observable
  "Observable protocol, which handles triggering functions when
  references change."
  (add! [object observer-name observer-fn]
        "Add a named observer function to a reference."
  (remove! [object observer-name]
        "Remove a named observer function from a reference."))

(extend-type Atom
  Observable
  (add! [object observer-name observer-fn]
    (add-watch object observer-name observer-fn))
  (remove! [object observer-name]
    (remove-watch object observer-name)))
