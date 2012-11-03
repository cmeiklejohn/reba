(ns
  ^{:author "Christopher Meiklejohn"
    :doc "To-Do List Example"}
  reba.examples.todo
  (:require [hiccups.runtime :as hiccusrt]
            [reba.materializable :as materializable]
            [reba.eventable :as eventable]
            [goog.dom :as dom])
  (:require-macros [hiccups.core :as hiccups]))

;; Define completed predicate.
(defn completed? [item]
  "Return whether an item is completed yet."
  (true? (:completed (deref item))))

;; Define oustanding predicate.
(defn outstanding? [item]
  "Return whether an item is outstanding."
  (false? (:completed (deref item))))

;; Define list generator.
(defn generate-list [filter-fn items]
  "Return the HTML to generate a list."
    (hiccups/html
      (for [item (filter filter-fn items)]
        (let [i (deref item)]
          [:li (:name i)]))))

;; Define add event handler.
(defn add-event-handler [items event]
  "Event handler for click events on the form."
  (let [element-id "new-todo-name"
        new-todo-name (.-value (dom/getElement element-id))]
    (set! (.-value (dom/getElement element-id)) "")
    (conj (deref items) (create-item new-todo-name false))))

;; Define item builder.
(defn create-item [name completed]
  "Given a name and completed status, return a task."
  (atom {:name name :completed completed}))

(defn main []
  "Initialize the to-do application."

  ;; Define list of items.
  (def list-of-items (atom [(create-item "Fix program" false)
                            (create-item "Write a new program" false)
                            (create-item "Do laundry" true)]))

  ;; Add materializer to generate the completed list.
  (materializable/add! list-of-items :completed-list-view "completed"
                                    (partial generate-list completed?))

  ;; Add materializer to generate the outstanding list.
  (materializable/add! list-of-items :outstanding-list-view "outstanding"
                                    (partial generate-list outstanding?))

  ;; Add materializer to generate the total indicator.
  (materializable/add! list-of-items :num-total "num-total"
                                    (fn [items] (count items)))

  ;; Add materializer to generate the oustanding indicator.
  (materializable/add! list-of-items :num-oustanding "num-outstanding"
                                    (fn [items] (count (filter completed? items))))

  ;; Bind event listener for the form for when items are added.
  (eventable/add! list-of-items "add-todo" "click" add-event-handler))
