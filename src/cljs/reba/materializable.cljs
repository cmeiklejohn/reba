(ns reba.materializable)

(defn materializer-watch! [node materializer-name x y a b]
  "Watcher function for materializer."

  (def materialized (apply (materializer-name (meta y)) [b]))

  ;; Update DOM.
  (set! (.-innerHTML (.getElementById js/document node)) materialized)

  (alter-meta! y (fn [m]
    (merge m {(keyword (str "rendered" materializer-name)) materialized})))
  )

(defn add-materializer! [node object materializer-name materializer-fn initial-value]
  "Setup a materializer."

  ;; Add meta-data.
  (alter-meta! object (fn [m] (merge m {materializer-name materializer-fn})))

  ;; Setup watcher.
  (add-watch object materializer-name (partial materializer-watch! node materializer-name))

  ;; Trigger materialization.
  (swap! object (fn [] initial-value)))
