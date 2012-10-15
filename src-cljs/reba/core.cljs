(ns reba.core
  (:require [clojure.string :as string]))

(defn render-watcher! [x y a b]
  "Watcher function, which applies the render function to
  the iref it's bound to and stashes the rendered content
  back in the objects meta data.  The render function, which is
  applied is assumed to be referentially transparent."
  (alter-meta! y (fn [m]
    (merge m { :rendered (apply (:render-fn (meta y)) [b]) }))))

(defn main []
  (def my-name (atom (str "Chris")
                     :meta { :render-fn string/upper-case }))
  (add-watch my-name :render-watcher render-watcher!)
  (swap! my-name (fn [] (str "Simon")))
  (.log js/console (:rendered (meta my-name))))
