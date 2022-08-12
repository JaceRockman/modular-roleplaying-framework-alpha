(ns main.rules-section.subs
  (:require [re-frame.core :as rf :refer [reg-sub]]))

(reg-sub
 :stat-info
 (fn [db _]
   (get-in db [:rules :stats])))

(reg-sub
 :checks
 (fn [db _]
   (get-in db [:rules :checks])))

(reg-sub
 :encounters
 (fn [db _]
   (get-in db [:rules :encounters])))

(reg-sub
 :damage
 (fn [db _]
   (get-in db [:rules :damage])))

(reg-sub
 :conditions
 (fn [db _]
   (get-in db [:rules :conditions])))