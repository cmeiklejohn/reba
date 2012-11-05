(ns
  ^{:author "Christopher Meiklejohn"
    :doc "Computable implementation."}
  reba.computable
  (:require [goog.dom :as dom]
            [reba.observable :as observable]))

(defprotocol Computable
  "Computable protocol, which provides a mechanism to bind objects so
  that changes propagate to associated objects."
  (add! [source destination derivation-name derivation-fn]
         "Create a binding between two objects, whereby changes to the
         first object propgate to the bound object."))

(extend-type Atom
  Computable
  (add! [source destination derivation-name derivation-fn]
    (observable/add! source derivation-name
                     (fn [x] (swap! destination (fn [] (apply derivation-fn [x])))))))
