(ns main.utils
  (:refer-clojure :exclude [spit]))

(def contents
  (clojure.core/atom "init"))

(def contents-contents
  (deref contents))

(defmacro spit [location]
  (clojure.core/spit location @contents))