(ns
  ^{:author "Christopher Meiklejohn"
    :doc "Materialized view implementation."}
  reba.materializable)

(defn materializer-watch! [node materializer-name x y a b]
  "
  Watcher function for materializer.

  When an object is changed, this function is triggered to re-render the content
  into the DOM.
  "

  ;; Generate materialized content.
  (def materialized (apply (materializer-name (meta y)) [b]))

  ;; Update DOM.
  (set! (.-innerHTML (.getElementById js/document node)) materialized)

  ;; Saves the rendered content to the metadata.
  (alter-meta! y (fn [m]
    (merge m {(keyword (str "rendered-" materializer-name)) materialized}))))

(defn add-materializer! [object materializer-name node materializer-fn]
  "
  Setup a materializer and trigger immediately.

  Given an object, materializer name, DOM node and function, bind a watcher
  which will, when the object is changed, generate a materialized view.
  "

  ;; Add meta-data.
  (alter-meta! object (fn [m] (merge m {materializer-name materializer-fn})))

  ;; Setup watcher.
  (add-watch object materializer-name
             (partial materializer-watch! node materializer-name))

  ;; Trigger materialization.
  (swap! object (fn [] @object)))

(defn add-listener! [object materializer-name node event-type event-fn]
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
