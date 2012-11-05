(ns
  ^{:author "Christopher Meiklejohn"
    :doc "Computable implementation."}
  reba.computable
  (:require [goog.dom :as dom]
            [reba.observable :as observable]))

(defprotocol Computable
  "Computable protocol, which provides a mechanism to bind objects so
  that changes propagate to associated objects."
  (bind! [source destination]
         "Create a binding between two objects, whereby changes to the
         first object propgate to the bound object."))

(extend-type Atom
  Computable
  (bind! [source destination derivation-name derivation-fn]
    (observable/add! source derivation-name
                     (fn [] (swap! destination derivation-fn)))))
