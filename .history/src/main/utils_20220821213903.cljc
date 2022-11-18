(ns main.utils
  (:refer-clojure :exclude [spit]))

(def contents
  (clojure.core/atom ""))

(defmacro spit [location]
  (clojure.core/spit location @contents))

(defmacro eval [contents]
  (clojure.core/eval contents))