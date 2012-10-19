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
