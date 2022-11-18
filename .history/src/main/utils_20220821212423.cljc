(ns main.utils
  (:refer-clojure :exclude [spit]))

(defmacro spit [location contents]
  (clojure.core/spit location contents))