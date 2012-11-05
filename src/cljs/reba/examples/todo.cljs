(ns
  ^{:author "Christopher Meiklejohn"
    :doc "To-Do List Example"}
  reba.examples.todo
  (:require [hiccups.runtime :as hiccusrt]
            [reba.materializable :as materializable]
            [reba.eventable :as eventable]
            [reba.computable :as computable]
            [goog.dom :as dom])
  (:require-macros [hiccups.core :as hiccups]))

;; Define completed predicate.
(defn- completed? [item]
  "Return whether an item is completed yet."
  (true? (:completed (deref item))))

;; Define oustanding predicate.
(defn- outstanding? [item]
  "Return whether an item is outstanding."
  (false? (:completed (deref item))))

;; Define list generator.
(defn- generate-list [items]
  "Return the HTML to generate a list."
    (hiccups/html
      (for [item items]
        (let [i (deref item)]
          [:li (:name i)]))))

;; Define add event handler.
(defn- add-event-handler [items event]
  "Event handler for click events on the form."
  (let [element-id "new-todo-name"
        new-todo-name (.-value (dom/getElement element-id))]
    (set! (.-value (dom/getElement element-id)) "")
    (conj (deref items) (create-item new-todo-name false))))

;; Define item builder.
(defn- create-item [name completed]
  "Given a name and completed status, return a task."
  (atom {:name name :completed completed}))

(defn main []
  "Initialize the to-do application."

  ;; Define a list of items.
  (def list-of-items (atom []))

  ;; Define materializers for the list which generate HTML counts which
  ;; are inserted into the DOM.
  (materializable/add! list-of-items :num-total "num-total"
                       (fn [items] (count items)))

  (materializable/add! list-of-items :num-oustanding "num-outstanding"
                       (fn [items] (count (filter completed? items))))

  ;; Define computed references which are derived from the list, and
  ;; then bind materializers to the computed references.
  (def list-of-completed-items (atom []))
  (computable/add! list-of-items list-of-completed-items "completed-list"
                   (fn [items] (filter completed? items)))
  (materializable/add! list-of-completed-items :completed-list-view "completed"
                       generate-list)

  (def list-of-oustanding-items (atom []))
  (computable/add! list-of-items list-of-oustanding-items "oustanding-list"
                 (fn [items] (filter outstanding? items)))
  (materializable/add! list-of-oustanding-items :completed-list-view "outstanding"
                     generate-list)

  ;; Define an event listener for the add-todo form, and have it operate
  ;; on the list reference.
  (eventable/add! list-of-items "add-todo" "click" add-event-handler)

  ;; Set the list of items and start things.
  (swap! list-of-items (fn [] [(create-item "Fix program" false)
                               (create-item "Write a new program" false)
                               (create-item "Do laundry" true)]))
)
