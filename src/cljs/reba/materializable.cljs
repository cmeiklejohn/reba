(ns reba.materializable)

(defn materializer-watch! [node materializer-name x y a b]
  "Watcher function for materializer."

  (def materialized (apply (materializer-name (meta y)) [b]))

  ;; Update DOM.
  (set! (.-innerHTML (.getElementById js/document node)) materialized)

  ;; Saves the rendered content to the metadata.
  (alter-meta! y (fn [m]
    (merge m {(keyword (str "rendered" materializer-name)) materialized}))))

(defn add-materializer! [object materializer-name node materializer-fn]
  "Setup a materializer."

  ;; Add meta-data.
  (alter-meta! object (fn [m] (merge m {materializer-name materializer-fn})))

  ;; Setup watcher.
  (add-watch object materializer-name
             (partial materializer-watch! node materializer-name))

  ;; Trigger materialization.
  (swap! object (fn [] @object)))

(defn add-listener! [object materializer-name node event-type event-fn]
  "Add an event listener."

  ;; Bind event listener to the element in the DOM.
  (.addEventListener
    (.getElementById js/document node) event-type
    (partial (fn [object event]
               (.preventDefault x)
               (swap! object (fn [] (apply event-fn [object event])))) object)
    "false"))
