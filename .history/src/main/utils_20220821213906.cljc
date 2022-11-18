(ns main.utils
  (:refer-clojure :exclude [spit]))

(def contents
  (clojure.core/atom ""))

(defmacro spit [location]
  (clojure.core/spit location @contents))