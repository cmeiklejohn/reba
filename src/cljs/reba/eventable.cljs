(ns
  ^{:author "Christopher Meiklejohn"
    :doc "Event listening library."}
  reba.eventable)

(defn add-listener! [object node event-type event-fn]
  "
  Add an event listener to a particular materialized view.

  Given a mutatable object, a particular materialized view of that object,
  a target DOM node, event and reaction, bind an event listener to the DOM,
  will apply that function to both the object and event, returning a new
  value which is swapped into the original object.
  "

  ;; Bind event listener to the element in the DOM.
  (.addEventListener
    (.getElementById js/document node) event-type
    (partial (fn [object event]
               (.preventDefault x)
               (swap! object (fn [] (apply event-fn [object event])))) object)
    "false"))
