; Core library implementation.

(ns reba.core
  (:require [reba.manipulator :as manipulator]
            [reba.observable :as observable]))

(defn main []
  (manipulator/bind-dom!))
