(ns reba.core
  (:require [hiccups.runtime :as hiccusrt]
            [reba.materializable :as materializable])
  (:require-macros [hiccups.core :as hiccups]))

(defn main []
  "Initialize the web application."

  ;; Define list of items.
  (def list-of-items (atom ["Chris" "Simon" "Joan"]))

  ;; Add materializer to generate the list.
  (materializable/add-materializer! list-of-items :list-view "items"
                                    (fn [items]
                                      (hiccups/html (for [x items] [:li x]))))

  ;; Add materializer to generate the total indicator.
  (materializable/add-materializer! list-of-items :num_total "num_total"
                                    (fn [items] (count items)))

  ;; Add materializer to generate the oustanding indicator.
  (materializable/add-materializer! list-of-items :num_oustanding "num_outstanding"
                                    (fn [items] (count items)))

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
