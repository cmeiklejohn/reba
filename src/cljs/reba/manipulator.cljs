; Naive DOM-manipulation interface.

(ns reba.manipulator)

(defn render-dom! [html]
  "Given HTML, redraw the entire DOM."
  (set! (.-innerHTML (.getElementById js/document "application")) html))

(defn bind-dom! []
  "Bind a global DOM object which will redraw when we provide it content."
  (def dom (atom ""))
  (add-watch dom :watch-change (fn [key x old-state new-state] (render-dom! new-state))))
