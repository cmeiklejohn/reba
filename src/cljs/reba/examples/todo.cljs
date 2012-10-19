(ns
  ^{:author "Christopher Meiklejohn"
    :doc "To-Do List Example"}
  reba.examples.todo
  (:require [hiccups.runtime :as hiccusrt]
            [reba.materializable :as materializable]
            [reba.eventable :as eventable])
  (:require-macros [hiccups.core :as hiccups]))

;; Define completed predicate.
(defn completed? [item]
  "Return whether an item is completed yet."
  (true? (:completed item)))

;; Define oustanding predicate.
(defn outstanding? [item]
  "Return whether an item is outstanding."
  (false? (:completed item)))

;; Define item builder.
(defn create-item [name completed]
  "Given a name and completed status, return a task."
  {:name name :completed completed})

(defn main []
  "Initialize the to-do application."

  ;; Define list of items.
  (def list-of-items (atom [(create-item "Fix program" false)
                            (create-item "Write a new program" false)
                            (create-item "Do laundry" true)]))

  ;; Add materializer to generate the completed list.
  (materializable/add-materializer! list-of-items :completed-list-view "completed"
                                    (fn [items]
                                      (hiccups/html
                                        (for [{:keys [name]}
                                          (filter completed? items)] [:li name]))))

  ;; Add materializer to generate the outstanding list.
  (materializable/add-materializer! list-of-items :outstanding-list-view "outstanding"
                                    (fn [items]
                                      (hiccups/html
                                        (for [{:keys [name]}
                                          (filter outstanding? items)] [:li name]))))

  ;; Add materializer to generate the total indicator.
  (materializable/add-materializer! list-of-items :num-total "num-total"
                                    (fn [items] (count items)))

  ;; Add materializer to generate the oustanding indicator.
  (materializable/add-materializer! list-of-items :num-oustanding "num-outstanding"
                                    (fn [items] (count (filter completed? items))))

  ;; Bind event listener for the form for when items are added.
  (eventable/add-listener! list-of-items "add-todo" "click" (fn [items event]
    (def new-todo-name (.-value (.getElementById js/document "new-todo-name")))
    (set! (.-value (.getElementById js/document "new-todo-name")) "")
    (conj (deref items) (create-item new-todo-name false)))))
