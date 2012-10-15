(ns reba.core
  (:require [clojure.string :as string]))

(defn render-watcher! [x y a b]
  "Watcher which executes the render function when changed."
  (alter-meta! y (fn [m] (merge m { :rendered (apply (:render-fn (meta y)) [b]) }))))

(defn main []
  (def my-name (atom (str "Chris")
                     :meta { :render-fn string/upper-case }))
  (add-watch my-name :render-watcher render-watcher!)
  (swap! my-name (fn [] (str "Simon")))
  (.log js/console (:rendered (meta my-name))))
