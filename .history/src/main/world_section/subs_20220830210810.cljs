(ns main.world-section.subs
  (:require 
   [re-frame.core :as rf :refer [reg-sub]]))

(reg-sub
 :active-world
 (fn [db _]
   (get-in db [:worlds (get db :active-world)])))

(reg-sub
 :active-world-name
 (fn [db _]
   (get-in db [:worlds (get db :active-world) :name])))

(reg-sub
 :world-territories
 (fn [db _]
   (get-in db [:worlds (get db :active-world) :territories])))

(reg-sub
 :active-territory
 (fn [db _]
   (get-in db [:worlds (get db :active-world) :active-territory])))

(reg-sub
 :territory-name
 (fn [db [_ territory-id]]
   (get-in db [:worlds (get db :active-world) :territories territory-id :name])))

(reg-sub
 :territory-details
 (fn [db [_ territory-id]]
   (get-in db [:worlds (get db :active-world) :territories territory-id :details])))

(reg-sub
 :territory-civilizations
 (fn [db [_ territory-id]]
   (get-in db [:worlds (get db :active-world) :territories territory-id :civilizations])))

(reg-sub
 :active-civilization
 (fn [db _]
   (get-in db [:worlds (get db :active-world) :active-civilization])))

(reg-sub
 :civilizations
 (fn [db _]
   (mapv first (get-in db [:worlds (get db :active-world) :civilizations]))))

(reg-sub
 :civilization-name
 (fn [db [_ civilization-id]]
   (get-in db [:worlds (get db :active-world) :civilizations civilization-id :name])))

(reg-sub
 :civilization-details
 (fn [db [_ civilization-id]]
   (get-in db [:worlds (get db :active-world) :civilizations civilization-id :details])))