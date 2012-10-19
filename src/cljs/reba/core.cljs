(ns reba.core
  (:require [hiccups.runtime :as hiccusrt]
            [reba.materializable :as materializable])
  (:require-macros [hiccups.core :as hiccups]))

(defn main []
  "Initialize the web application."

  ;; Define list of items.
  (def list-of-items (atom [{ :name "Fix program" :completed false }
                            { :name "Write new program" :completed false }
                            { :name "Do laundry" :completed true }]))

  ;; Define completed predicate.
  (defn completed? [item]
    "Return whether an item is completed yet."
    (true? (:completed item)))

  ;; Add materializer to generate the list.
  (materializable/add-materializer! list-of-items :list-view "items"
                                    (fn [items]
                                      (hiccups/html (for [x items] [:li x]))))

  ;; Add materializer to generate the total indicator.
  (materializable/add-materializer! list-of-items :num-total "num-total"
                                    (fn [items] (count items)))

  ;; Add materializer to generate the oustanding indicator.
  (materializable/add-materializer! list-of-items :num-oustanding "num-outstanding"
                                    (fn [items] (count (filter completed? items))))

  ;; Bind events to a particular view.
  (materializable/add-listener! list-of-items :list-view, "items" "click"
                                (fn [object event] ["Chris"]))

  ;; Bind event listener for the form for when items are added.
  (.addEventListener
    (.getElementById js/document "add_todo") "click"
    (fn [x]
      (.preventDefault x)
      (swap! list-of-items (fn []
          (conj (deref list-of-items)
                (.-value (.getElementById js/document "new_todo_name")))))) "false")
)
