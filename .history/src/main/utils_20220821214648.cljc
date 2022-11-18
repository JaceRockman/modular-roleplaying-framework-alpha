(ns main.utils
  (:refer-clojure :exclude [spit]))

(def contents
  (clojure.core/atom "init"))

(defn contents-contents
  []
  (deref contents))

(defn update-contents
  [new-contents]
  (reset! contents new-contents))

(update-contents "a")

(defmacro spit [location]
  (clojure.core/spit location @contents))