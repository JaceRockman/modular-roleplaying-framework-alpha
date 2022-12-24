(ns main.events
  (:require
   [re-frame.core :as rf :refer [reg-event-db reg-event-fx]]
   [main.db :as db :refer [app-db]]
   [main.play-section.events]
   [main.play-section.data :as data]
   [main.rules-section.events]
   [main.world-section.events]))

(defn deep-merge
  "Recursively merges maps."
  [& maps]
  (letfn [(m [& xs]
            (if (some #(and (map? %) (not (record? %))) xs)
              (apply merge-with m xs)
              (last xs)))]
    (reduce m maps)))

(reg-event-db
 :initialize-db
 (fn [_ _]
   (-> app-db 
       (deep-merge data/rogue-data)
       (deep-merge data/soldier-data)
       (deep-merge data/paladin-data)
       (deep-merge data/arcanist-data)
       (deep-merge data/druid-data)
       (deep-merge data/warlock-data))))

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