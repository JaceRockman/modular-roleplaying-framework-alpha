(ns main.play-section.events
  (:require 
   [main.helpers :as helpers]
   [re-frame.core :as rf :refer [reg-event-db]]))

(reg-event-db
 :set-active-character
 (fn [db [_ char-id]]
   (assoc db :active-character (if (= char-id (:active-character db)) nil char-id))))

(reg-event-db
 :new-character
 (fn [db [_ character-map set-active?]]
   (let [next-id (if (nil? (keys (get db :characters)))
                   0
                   (inc (apply max (keys (get db :characters)))))]
     (as-> db new-db
       (assoc-in new-db [:characters next-id] (merge
                                               {:id next-id
                                                :profile {:portrait "../assets/Avis.png"
                                                          :name "New Character"
                                                          :gender nil
                                                          :race nil
                                                          :titles nil
                                                          :description nil}
                                                :experience 0
                                                :stats {:physical {:quality {:initiation   {:name "Coordination"
                                                                                            :value 1}
                                                                             :reaction     {:name "Reflexes"
                                                                                            :value 1}
                                                                             :continuation {:name "Endurance"
                                                                                            :value 1}}
                                                                   :power {:dominance  {:name "Might"
                                                                                        :value 6}
                                                                           :competence {:name "Finesse"
                                                                                        :value 6}
                                                                           :resilience {:name "Fortitude"
                                                                                        :value 6}}}
                                                        :spiritual {:quality {:initiation   {:name "Exertion"
                                                                                             :value 1}
                                                                              :reaction     {:name "Instinct"
                                                                                             :value 1}
                                                                              :continuation {:name "Perseverance"
                                                                                             :value 1}}
                                                                    :power {:dominance  {:name "Willpower"
                                                                                         :value 6}
                                                                            :competence {:name "Discipline"
                                                                                         :value 6}
                                                                            :resilience {:name "Tenacity"
                                                                                         :value 6}}}
                                                        :mental {:quality {:initiation   {:name "Concentration"
                                                                                          :value 1}
                                                                           :reaction     {:name "Recognition"
                                                                                          :value 1}
                                                                           :continuation {:name "Comprehension"
                                                                                          :value 1}}
                                                                 :power {:dominance  {:name "Intellect"
                                                                                      :value 6}
                                                                         :competence {:name "Focus"
                                                                                      :value 6}
                                                                         :resilience {:name "Stability"
                                                                                      :value 6}}}
                                                        :social {:quality {:initiation   {:name "Persuasion"
                                                                                          :value 1}
                                                                           :reaction     {:name "Insight"
                                                                                          :value 1}
                                                                           :continuation {:name "Connections"
                                                                                          :value 1}}
                                                                 :power {:dominance  {:name "Presence"
                                                                                      :value 6}
                                                                         :competence {:name "Wit"
                                                                                      :value 6}
                                                                         :resilience {:name "Poise"
                                                                                      :value 6}}}}
                                                :damage {:physical {:minor 0
                                                                    :major 0}
                                                         :spiritual {:minor 0
                                                                     :major 0}
                                                         :mental {:minor 0
                                                                  :major 0}
                                                         :social {:minor 0
                                                                  :major 0}}
                                                :resources '()
                                                :active-action nil
                                                :actions {0 {:title "Physical Health Check"
                                                             :description ""
                                                             :skill-path [:physical :quality :continuation]
                                                             :ability-path [:physical :power :resilience]
                                                             :resources []
                                                             :dice-mod 0
                                                             :flat-mod 0
                                                             :min-pool-size 2
                                                             :splinters 1
                                                             :combinations {}
                                                             :target-number nil}
                                                          1 {:title "Spiritual Health Check"
                                                             :description ""
                                                             :skill-path [:spiritual :quality :continuation]
                                                             :ability-path [:spiritual :power :resilience]
                                                             :resources []
                                                             :dice-mod 0
                                                             :flat-mod 0
                                                             :min-pool-size 2
                                                             :splinters 1
                                                             :combinations {}
                                                             :target-number nil}
                                                          2 {:title "Mental Health Check"
                                                             :description ""
                                                             :skill-path [:mental :quality :continuation]
                                                             :ability-path [:mental :power :resilience]
                                                             :resources []
                                                             :dice-mod 0
                                                             :flat-mod 0
                                                             :min-pool-size 2
                                                             :splinters 1
                                                             :combinations {}
                                                             :target-number nil}
                                                          3 {:title "Social Health Check"
                                                             :description ""
                                                             :skill-path [:social :quality :continuation]
                                                             :ability-path [:social :power :resilience]
                                                             :resources []
                                                             :dice-mod 0
                                                             :flat-mod 0
                                                             :min-pool-size 2
                                                             :splinters 1
                                                             :combinations {}
                                                             :target-number nil}}
                                                :notes ""}
                                               character-map))
       (if set-active? (assoc new-db :active-character next-id))))))

(reg-event-db
 :delete-character
 (fn [db [_ character-id]]
   (update db :characters dissoc character-id)))



(reg-event-db
 :update-character-profile
 (fn [db [_ char-id key val]]
   (assoc-in db [:characters char-id :profile key] val)))



(reg-event-db
 :set-active-skill
 (fn [db [_ char-id skill-path]]
   (let [current-val (get-in db [:characters char-id :active-stats :skill])]
     (assoc-in db [:characters char-id :active-stats :skill] (if (= current-val skill-path) nil skill-path)))))

(reg-event-db
 :set-active-ability
 (fn [db [_ char-id ability-path]]
   (let [current-val (get-in db [:characters char-id :active-stats :ability])]
     (assoc-in db [:characters char-id :active-stats :ability] (if (= current-val ability-path) nil ability-path)))))

(reg-event-db
 :increase-skill
 (fn [db [_ char-id skill-path]]
   (update-in db (into (into [:characters char-id :stats] skill-path) [:value]) #(+ % 1))))

(reg-event-db
 :decrease-skill
 (fn [db [_ char-id skill-path]]
   (update-in db (into (into [:characters char-id :stats] skill-path) [:value]) #(- % 1))))

(reg-event-db
 :increase-ability
 (fn [db [_ char-id ability-path]]
   (update-in db (into (into [:characters char-id :stats] ability-path) [:value]) #(+ % 2))))

(reg-event-db
 :decrease-ability
 (fn [db [_ char-id ability-path]]
   (update-in db (into (into [:characters char-id :stats] ability-path) [:value]) #(- % 2))))

(reg-event-db
 :update-exp
 (fn [db [_ char-id update-fn]]
   (update-in db [:characters char-id :experience] update-fn)))


(reg-event-db
 :inflict-minor-wound
 (fn [db [_ char-id domain]]
   (update-in db [:characters char-id :damage domain :minor] inc)))

(reg-event-db
 :heal-minor-wound
 (fn [db [_ char-id domain]]
   (update-in db [:characters char-id :damage domain :minor] dec)))

(reg-event-db
 :inflict-major-wound
 (fn [db [_ char-id domain]]
   (update-in db [:characters char-id :damage domain :major] inc)))

(reg-event-db
 :heal-major-wound
 (fn [db [_ char-id domain]]
   (update-in db [:characters char-id :damage domain :major] dec)))

(reg-event-db
 :copy-resource
 (fn [db [_ source-id target-id]]
   (let [resource-details (get-in db [:resources source-id])]
     (assoc-in db [:resources target-id] resource-details))))

(reg-event-db
 :create-shadow-resource
 (fn [db [_ resource-id]]
   (let [resource-details (get-in db [:resources resource-id])
         shadow-id (if resource-id (- 0 resource-id) 0)
         shadow-exists? (contains? (get db :resources) shadow-id)]
     (if (not shadow-exists?)
       (assoc-in db [:resources shadow-id] resource-details)))))

(reg-event-db
 :reset-temp-resource
 (fn [db _]
   (assoc-in db [:resources 0] {:id 0
                                :type :items
                                :name "New Resource"
                                :properties []
                                :benefits {:quality {:name "Quality"
                                                     :value 0}
                                           :power {:name "Power"
                                                   :value 0}}
                                :details "Resource Details"})))

(reg-event-db
 :create-new-resource
 (fn [db _]
   (let [new-id (inc (apply max (keys (get db :resources))))
         resource-details (get-in db [:resources 0])]
     (-> db 
         (assoc-in [:resources new-id] (assoc resource-details :id new-id))
         (assoc-in [:resources 0] {:id 0
                                   :type :items
                                   :name "New Resource"
                                   :properties []
                                   :benefits {:quality {:name "Quality"
                                                        :value 0}
                                              :power {:name "Power"
                                                      :value 0}}
                                   :details "Resource Details"})))))

(reg-event-db
 :create-new-resource-2
 (fn [db [_ resource-details]]
   (let [new-id (inc (apply max (keys (get db :resources))))]
     (assoc-in db [:resources new-id] (assoc resource-details :id new-id)))))

(reg-event-db
 :create-new-character-resource-2
 (fn [db [_ char-id resource-details]]
   (let [new-id (inc (apply max (keys (get db :resources))))
         resource-quality (get-in resource-details [:benefits :quality :value])
         resource-power (get-in resource-details [:benefits :power :value])
         resource-cost (+ resource-quality resource-power)]
     (-> db
         (assoc-in [:resources new-id] (assoc resource-details :id new-id))
         (assoc-in [:characters char-id :resources new-id] {:notes "Notes"})
         (update-in [:characters char-id :experience] #(- % resource-cost))))))

(reg-event-db
 :create-new-character-resource
 (fn [db [_ char-id]]
   (let [new-id (inc (apply max (keys (get db :resources))))
         resource-details (get-in db [:resources 0])
         resource-quality (get-in db [:resources 0 :benefits :quality :value])
         resource-power (get-in db [:resources 0 :benefits :power :value])
         resource-cost (+ resource-quality resource-power)]
     (-> db
         (assoc-in [:resources new-id] (assoc resource-details :id new-id))
         (assoc-in [:characters char-id :resources new-id] {:notes "Notes"})
         (assoc-in [:resources 0] {:id 0
                                    :type nil
                                    :name "New Resource"
                                    :properties []
                                    :benefits {:quality {:name "Quality"
                                                         :value 0}
                                               :power {:name "Power"
                                                       :value 0}}
                                    :details "Resource Details"})
         (update-in [:characters char-id :experience] #(- % resource-cost))))))

(reg-event-db
 :update-resource-type
 (fn [db [_ resource-id type]]
   (assoc-in db [:resources resource-id :type] type)))

(reg-event-db
 :update-resource-title
 (fn [db [_ resource-id text]]
   (assoc-in db [:resources resource-id :name] text)))

(reg-event-db
 :update-resource-property
 (fn [db [_ resource-id property-index text]]
   (assoc-in db [:resources resource-id :properties property-index] text)))

(reg-event-db
 :update-resource-quality-alias
 (fn [db [_ resource-id text]]
   (assoc-in db [:resources resource-id :benefits :quality :name] text)))

(reg-event-db
 :update-resource-quality-val
 (fn [db [_ resource-id update-fn]]
   (update-in db [:resources resource-id :benefits :quality :value] #(update-fn %))))

(reg-event-db
:update-resource-power-alias
  (fn [db [_ resource-id text]]
  (assoc-in db [:resources resource-id :benefits :power :name] text)))

(reg-event-db
 :update-resource-power-val
 (fn [db [_ resource-id update-fn]]
   (update-in db [:resources resource-id :benefits :power :value] #(update-fn %))))

(reg-event-db
 :update-resource-details
  (fn [db [_ resource-id text]]
  (assoc-in db [:resources resource-id :details] text)) )

(reg-event-db
 :set-global-active-resource
 (fn [db [_ resource-type resource-id]]
   (let [current-vals (get-in db [:active-resources resource-type])]
     (assoc-in db [:active-resources resource-type] 
               (if (= -1 (.indexOf current-vals resource-id))
                 (conj current-vals resource-id)
                 (remove #(= resource-id %) current-vals))))))

(reg-event-db
 :set-active-resource
 (fn [db [_ char-id resource-id]]
   (update-in db [:characters char-id :active-resources]
              (fn [curr-active-resources] (if (some #(= resource-id %) curr-active-resources)
                                            (remove #(= resource-id %) curr-active-resources)
                                            (conj curr-active-resources resource-id))))))



(reg-event-db
 :add-character-resource
 (fn [db [_ char-id resource-id]]
   (let [current-val (keys (get-in db [:characters char-id :resources]))
         remove? (some #(= resource-id %) current-val)
         resource-quality (get-in db [:resources resource-id :benefits :quality :value])
         resource-power (get-in db [:resources resource-id :benefits :power :value])
         resource-cost (+ resource-quality resource-power)]
     (-> db
         (update-in [:characters char-id :resources] #(if remove?
                                                       (dissoc % resource-id)
                                                       (assoc % resource-id {:notes "Notes"})))
         (update-in [:characters char-id :experience] #(if remove?
                                                         (+ % resource-cost)
                                                         (- % resource-cost)))))))

(reg-event-db
 :update-resource-notes
 (fn [db [_ char-id resource-id text]]
   (assoc-in db [:characters char-id :resources resource-id :notes] text)))

(reg-event-db
 :resource-edit-mode-toggle
 (fn [db [_ resource-id]]
   (update-in db [:resources resource-id :edit-mode?] not)))

(reg-event-db
 :delete-character-resource
 (fn [db [_ char-id resource-id]]
   (let [resource-quality (get-in db [:resources resource-id :benefits :quality :value])
         resource-power (get-in db [:resources resource-id :benefits :power :value])
         resource-cost (+ resource-quality resource-power)] 
     (-> db
         (helpers/dissoc-in [:characters char-id :resources] resource-id)
         (update-in [:characters char-id :experience] #(+ % resource-cost))))))

(reg-event-db
 :delete-resource
 (fn [db [_ resource-id]]
   (assoc-in db [:resources resource-id :type] nil)))

(reg-event-db
 :new-character-action
 (fn [db [_ char-id]]
   (let [new-action-id (if (empty? (get-in db [:characters char-id :actions]))
                         0
                         (->> (get-in db [:characters char-id :actions])
                              (keys)
                              (apply max)
                              (inc)))]
     (assoc-in db [:characters char-id :actions new-action-id]
               {:title "New Action"
                :description ""
                :skill-path []
                :ability-path []
                :resources []
                :dice-mod 0
                :flat-mod 0
                :min-pool-size 2
                :splinters 1
                :combinations {}
                :active-tab 0}))))

(reg-event-db
 :delete-character-action
 (fn [db [_ char-id action-id]]
   (-> db 
       (helpers/dissoc-in [:characters char-id :actions] action-id)
       (helpers/dissoc-in [:characters char-id :rolls] action-id))))

(reg-event-db
 :set-active-action
 (fn [db [_ char-id action-id]]
   (let [current-val (get-in db [:characters char-id :active-action])]
     (assoc-in db [:characters char-id :active-action] (if (= current-val action-id) nil action-id)))))

(reg-event-db
 :edit-action-title
 (fn [db [_ char-id action-id new-title]]
   (assoc-in db [:characters char-id :actions action-id :title] new-title)))

(reg-event-db
 :set-active-action-tab
 (fn [db [_ char-id action-id action-tab]]
   (assoc-in db [:characters char-id :actions action-id :active-tab] action-tab)))

(reg-event-db
 :set-action-stat-paths
 (fn [db [_ char-id action-id skill-path ability-path]]
   (let [current-skill-val (get-in db [:characters char-id :actions action-id :skill-path])
         current-ability-val (get-in db [:characters char-id :actions action-id :ability-path])
         deselect? (and (= current-skill-val skill-path) (= current-ability-val ability-path))]
     (-> db
         (assoc-in [:characters char-id :actions action-id :skill-path] (if deselect? nil skill-path))
         (assoc-in [:characters char-id :actions action-id :ability-path] (if deselect? nil ability-path))))))

(reg-event-db
 :set-action-skill-path
 (fn [db [_ char-id action-id skill-path]]
   (let [current-val (get-in db [:characters char-id :actions action-id :skill-path])]
     (assoc-in db [:characters char-id :actions action-id :skill-path] (if (= skill-path current-val) nil skill-path)))))

(reg-event-db
 :set-action-ability-path
 (fn [db [_ char-id action-id ability-path]]
   (let [current-val (get-in db [:characters char-id :actions action-id :ability-path])]
     (assoc-in db [:characters char-id :actions action-id :ability-path] (if (= ability-path current-val) nil ability-path)))))

(reg-event-db
 :add-action-resource
 (fn [db [_ char-id action-id resource-id]]
   (update-in db [:characters char-id :actions action-id :resources] #(conj % resource-id))))

(reg-event-db
 :remove-action-resource
 (fn [db [_ char-id action-id resource-id]]
   (update-in db [:characters char-id :actions action-id :resources] #(remove (fn [v] (= resource-id v)) %))))

(reg-event-db
 :update-action-dice-mod
 (fn [db [_ char-id action-id update-fn]]
   (update-in db [:characters char-id :actions action-id :dice-mod] update-fn)))

(reg-event-db
 :update-action-flat-mod
 (fn [db [_ char-id action-id update-fn]]
   (update-in db [:characters char-id :actions action-id :flat-mod] update-fn)))

(reg-event-db
 :reset-dice-and-flat-mods
 (fn [db [_ char-id action-id]]
   (-> db
       (assoc-in [:characters char-id :actions action-id :dice-mod] 0)
       (assoc-in [:characters char-id :actions action-id :flat-mod] 0)
       (assoc-in [:characters char-id :actions action-id :splinters] 1)
       (assoc-in [:characters char-id :actions action-id :combinations] {}))))

(reg-event-db
 :update-splinters
 (fn [db [_ char-id action-id update-fn]]
   (update-in db [:characters char-id :actions action-id :splinters] update-fn)))

(reg-event-db
 :reset-splinters
 (fn [db [_ char-id action-id]]
   (-> db
       (assoc-in [:characters char-id :actions action-id :splinters] 1)
       (assoc-in [:characters char-id :actions action-id :combinations] {}))))

(reg-event-db
 :update-action-combinations
 (fn [db [_ char-id action-id index update-fn]]
   (update-in db [:characters char-id :actions action-id :combinations index] update-fn)))

(reg-event-db
 :reset-combinations
 (fn [db [_ char-id action-id]]
   (assoc-in db [:characters char-id :actions action-id :combinations] {})))





(reg-event-db
 :log-dice-roll
 (fn [db [_ char-id action-id dice-pool]]
   (let [roll (map #(apply helpers/roll-dice (take 2 %)) dice-pool)
         bonus (apply + (map last dice-pool))
         roll-result (+ bonus (apply max (flatten roll)))
         final-result (if (> 1 roll-result)
                        "Failure"
                        roll-result)]
     (update-in db [:characters char-id :rolls action-id]
                #(if (nil? %)
                   [{:roll roll :bonus bonus :roll-result final-result}]
                   (conj % {:roll roll :bonus bonus :roll-result final-result}))))))


(reg-event-db
 :update-character-notes
 (fn [db [_ char-id text]]
   (assoc-in db [:characters char-id :notes] text)))
