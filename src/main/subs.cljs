(ns main.subs
  (:require [re-frame.core :as rf :refer [reg-sub]]
            [main.play-section.subs]
            [main.rules-section.subs]
            [main.world-section.subs]))

(reg-sub
 :style-mode
 (fn [db _]
   (get db :style-mode)))

(reg-sub
 :edit-mode
 (fn [db _]
   (:edit-mode db)))

(reg-sub
 :active-section
 (fn [db _]
   (get db :active-section)))

(reg-sub
 :section
 (fn [db [_ section-id]]
   (get-in db [:sections section-id])))

(reg-sub
 :section-name
 (fn [db [_ section-id]]
   (get-in db [:sections section-id :name])))

(reg-sub
 :active-tab
 (fn [db _]
   (get-in db [:sections (get db :active-section) :active-tab])))

(reg-sub
 :section-tabs
 (fn [db [_ section-id]]
   (get-in db [:sections section-id :tabs])))
