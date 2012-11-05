(ns
  ^{:author "Christopher Meiklejohn"
    :doc "Eventable implementation."}
  reba.core.eventable
  (:require [goog.events :as events]
            [goog.dom :as dom]))

(defprotocol Eventable
  "Eventable protocol, which handles binding events the the DOM which
  will trigger operations back on a data structure."
  (add! [object node event-type event-fn]
        "Add an event listener to a particular DOM node. Given a
        mutatable object, a target DOM node, event and reaction, bind an
        event listener to the DOM, will apply that function to both the
        object and event, returning a new value which is swapped into
        the original object."))

(extend-type Atom
  Eventable
  (add! [object node event-type event-fn]
    (events/listen (dom/getElement node) event-type
      (partial (fn [object event]
                 (.preventDefault event)
                 (swap! object (fn [] (apply event-fn [object event])))) object)
      "false")))
