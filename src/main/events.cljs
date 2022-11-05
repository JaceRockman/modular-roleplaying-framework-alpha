(ns main.events
  (:require
   [re-frame.core :as rf :refer [reg-event-db]]
   [main.db :as db :refer [app-db]]
   [main.play-section.events]
   [main.rules-section.events]
   [main.world-section.events]))

(reg-event-db
 :initialize-db
 (fn [_ _]
   app-db))

(reg-event-db
 :set-style-mode
 (fn [db [_ style-mode]]
   (assoc db :style-mode style-mode)))

(reg-event-db
 :toggle-edit-mode
 (fn [db _]
   (update db :edit-mode not)))

(reg-event-db
 :set-edit-mode
 (fn [db [_ bool]]
   (assoc db :edit-mode bool)))


(reg-event-db
 :prev-section
 (fn [db _]
   (update db :active-section #(if (= % 0) (+ 2 %) (dec %)))))

(reg-event-db
 :next-section
 (fn [db _]
   (update db :active-section #(if (= % 2) (- 2 %) (inc %)))))

(reg-event-db
 :set-active-tab
 (fn [db [_ tab]]
   (-> db
       (assoc-in [:sections (get db :active-section) :active-tab] tab)
       (assoc :active-resource nil))))