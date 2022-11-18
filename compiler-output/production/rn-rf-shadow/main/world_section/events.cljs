(ns main.world-section.events
  (:require
   [re-frame.core :as rf :refer [reg-event-db reg-event-fx]]))

(reg-event-db
 :set-active-territory
 (fn [db [_ territory-id]]
   (assoc-in db [:worlds (get db :active-world) :active-territory] territory-id)))

(reg-event-db
 :set-active-civilization
 (fn [db [_ civilization-id]]
   (assoc-in db [:worlds (get db :active-world) :active-civilization] civilization-id)))