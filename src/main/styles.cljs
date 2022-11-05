(ns main.styles
  (:require [clojure.string :as str]
            [main.helpers :as helpers :refer [<sub]]))

(defn active-mode [] (<sub [:style-mode]))

(def dark-mode
  {:background "#121212"
   :surface "#ffffff0f"
   :high-emphasis "#ffffffdd"
   :medium-emphasis "#ffffffaa"
   :disabled "#ffffff50"
   :primary "#ba2020"
   :secondary "#03dac5"
   :error "#cf6679"
   :on-background "#ffffff"
   :on-surface "#ffffff"
   :on-primary "#ffffff"
   :on-secondary "#000000"
   :on-error "#000000"})

(def light-mode
  {:background "#ffffff"
   :surface "#00000007"
   :primary "#6200ee"
   :secondary "#03dac5"
   :error "#b00020"
   :on-background "#000000"
   :on-surface "#000000"
   :on-primary "#ffffff"
   :on-secondary "#000000"
   :on-error "#ffffff"})


(def navigation-bar-style
  {:background-color (:surface dark-mode)
   :height 75
   :width "100%"
   :align-items "center"
   :justify-content "center"
   :flex-direction "row"})


(def float-button-style
  {:z-index          2
   :margin           3
   :padding          6
   :align-items      :center
   :justify-content  :center
   :border-width     1
   :border-radius    4
   :border-color     (:medium-emphasis dark-mode)
   :background-color (:surface dark-mode)})

(def float-button-text-style
  {:padding-left  12
   :padding-right 12
   :font-weight   :bold
   :font-size     18
   :color         (:medium-emphasis dark-mode)})

(def active-float-button-style
  (merge float-button-style {:background-color (:primary dark-mode)}))

(def active-float-button-text-style
  (merge float-button-text-style {:color (:on-surface dark-mode)}))

(def disabled-float-button-style
  (merge float-button-style {:background-color (:low-emphasis dark-mode)}))

(def disabled-float-button-text-style
  (merge float-button-text-style {:color {:medium-emphasis dark-mode}}))



(def flat-button-style
  {:margin           3
   :padding          0
   :align-items      :center
   :justify-content  :center
   :background-color (:surface dark-mode)})

(def flat-button-text-style
  {:margin-left  5
   :margin-right 5
   :font-weight  :bold
   :font-size    18
   :margin       10
   :color        (:medium-emphasis dark-mode)})

(def active-flat-button-style
  (merge flat-button-style {:background-color (:primary dark-mode)}))

(def active-flat-button-text-style
  (merge flat-button-text-style {:color (:on-surface dark-mode)}))

(def disabled-flat-button-style
  (merge flat-button-style {:background-color (:low-emphasis dark-mode)}))

(def disabled-flat-button-text-style
  (merge flat-button-text-style {:color (:low-emphasis dark-mode)}))