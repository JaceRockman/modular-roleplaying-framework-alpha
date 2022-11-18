(ns main.events
  (:require
   [main.utils :include-macros true :refer [spit contents contents-contents update-contents]]
   [re-frame.core :as rf :refer [reg-event-db reg-event-fx]]
   [main.db :as db :refer [app-db]]
   [main.initialdb :as init-db :refer [init-app-db]]
   [main.play-section.events]
   [main.rules-section.events]
   [main.world-section.events]))

(def testing {:hello "hello"})

(defn project-clj
  [new-contents]
  (do
    (update-contents new-contents)
    (spit "./initialdb.cljs")))

(update-contents "a")
(contents-contents)

(project-clj "b")

;; (reg-event-db
;;  :save-db
;;  (fn [db [_ data]]
;;    (spit )))

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