(ns main.play-section.subs
  (:require [main.helpers :as helpers]
            [re-frame.core :as rf :refer [reg-sub]]))

(reg-sub
 :characters
 (fn [db _]
   (keys (:characters db))))

(reg-sub
 :active-character
 (fn [db _]
   (get db :active-character)))

(reg-sub
 :character-profile
 (fn [db [_ char-id]]
   (get-in db [:characters char-id :profile])))

(reg-sub
 :character-profile-data
 (fn [db [_ char-id attribute]]
   (get-in db [:characters char-id :profile attribute])))

(reg-sub
 :character-name
 (fn [db [_ char-id]]
   (get-in db [:characters char-id :profile :name])))

(reg-sub
 :character-portrait
 (fn [db [_ char-id]]
   (get-in db [:characters char-id :profile :portrait])))

(reg-sub
 :character-stats
 (fn [db [_ char-id]]
   (get-in db [:characters char-id :stats])))

(reg-sub
 :character-active-skill
 (fn [db [_ char-id]]
   (get-in db [:characters char-id :active-stats :skill])))

(reg-sub
 :character-active-ability
 (fn [db [_ char-id]]
   (get-in db [:characters char-id :active-stats :ability])))

(reg-sub
 :character-stat
 (fn [db [_ char-id stat-path]]
   (get-in db (into [:characters char-id :stats] stat-path))))

(reg-sub
 :character-simple-stat
 (fn [db [_ char-id domain]]
   (let [{:keys [quality power]} (get-in db [:characters char-id :stats domain])]
     {:name (domain {:physical "Athleticism" :spiritual "Willpower" :mental "Intellect" :social "Charisma"})
      :skill (quot (apply + (map #(get % :value) (vals quality))) 3)
      :ability (* 2 (quot (apply + (map #(quot % 2) (map #(get % :value) (vals power)))) 3))})))

(reg-sub
 :character-moderate-stats
 (fn [db [_ char-id domain]]
   (let [{:keys [quality power]} (get-in db [:characters char-id :stats domain])
         {:keys [initiation reaction continuation]} quality
         {:keys [dominance competence resilience]} power]
     {:initiation {:name (:name initiation)
                   :skill (:value initiation)
                   :ability (:value dominance)}
      :reaction {:name (:name reaction)
                 :skill (:value reaction)
                 :ability (:value competence)}
      :continuation {:name (:name continuation)
                     :skill (:value continuation)
                     :ability (:value resilience)}})))

(reg-sub
 :character-complex-stats
 (fn [db [_ char-id domain]]
   (let [{:keys [quality power]} (get-in db [:characters char-id :stats domain])
         {:keys [initiation reaction continuation]} quality
         {:keys [dominance competence resilience]} power]
     [{:name (:name initiation)
       :skill (:value initiation)}
      {:name (:name reaction)
       :skill (:value reaction)}
      {:name (:name continuation)
       :skill (:value continuation)}
      {:name (:name dominance)
       :ability (:value dominance)}
      {:name (:name competence)
       :ability (:value competence)}
      {:name (:name resilience)
       :ability (:value resilience)}])))



(reg-sub
 :simple-stats
 (fn [db [_ char-id]]
   (let [stats (get-in db [:characters char-id :stats])]
     (defn quality-average [domain] (quot (reduce + (map #(last (vals %)) (vals (get-in stats [domain :quality])))) 3))
     (defn power-average [domain] (* 2 (quot (reduce + (map #(quot (last (vals %)) 2) (vals (get-in stats [domain :power])))) 3)))
     {:physical [{:name "Athleticism"
                  :quality (quality-average :physical)
                  :power (power-average :physical)}]
      :spiritual [{:name "Willpower"
                   :quality (quality-average :spiritual)
                   :power (power-average :spiritual)}]
      :mental [{:name "Acuity"
                :quality (quality-average :mental)
                :power (power-average :mental)}]
      :social [{:name "Charisma"
                :quality (quality-average :social)
                :power (power-average :social)}]})))

(reg-sub
 :skill-paths
 (fn [db [_ char-id]]
   (reduce (fn [path-map skill-path]
             (assoc path-map (get-in db (into [:characters char-id :stats] skill-path)) skill-path))
           {}
           (helpers/combinations3
            [:physical :spiritual :mental :social]
            [:quality]
            [:initiation :reaction :continuation]))))

(reg-sub
 :ability-paths
 (fn [db [_ char-id]]
   (reduce (fn [path-map skill-path]
             (assoc path-map (get-in db (into [:characters char-id :stats] skill-path)) skill-path))
           {}
           (helpers/combinations3
            [:physical :spiritual :mental :social]
            [:power]
            [:dominance :competence :resilience]))))

(reg-sub
 :moderate-stats
 (fn [db [_ char-id]]
   (let [stats (get-in db [:characters char-id :stats])]
     (defn initiation [domain] (get-in stats [domain :quality :initiation :value]))
     (defn dominance [domain] (get-in stats [domain :power :dominance :value]))
     (defn reaction [domain] (get-in stats [domain :quality :reaction :value]))
     (defn competence [domain] (get-in stats [domain :power :competence :value]))
     (defn continuation [domain] (get-in stats [domain :quality :continuation :value]))
     (defn resilience [domain] (get-in stats [domain :power :resilience :value]))
     {:physical [{:name "Coordination" :quality (initiation :physical) :power (dominance :physical)}
                 {:name "Reflexes" :quality (reaction :physical) :power (competence :physical)}
                 {:name "Endurance" :quality (continuation :physical) :power (resilience :physical)}]
      :spiritual [{:name "Exertion" :quality (initiation :spiritual) :power (dominance :spiritual)}
                  {:name "Instinct" :quality (reaction :spiritual) :power (competence :spiritual)}
                  {:name "Perseverance" :quality (continuation :spiritual) :power (resilience :spiritual)}]
      :mental [{:name "Concentration" :quality (initiation :mental) :power (dominance :mental)}
               {:name "Recognition" :quality (reaction :mental) :power (competence :mental)}
               {:name "Comprehension" :quality (continuation :mental) :power (resilience :mental)}]
      :social [{:name "Persuasion" :quality (initiation :social) :power (dominance :social)}
               {:name "Insight" :quality (reaction :social) :power (competence :social)}
               {:name "Connections" :quality (continuation :social) :power (resilience :social)}]})))

(reg-sub
 :complex-stats
 (fn [db [_ char-id]]
   (let [stats (get-in db [:characters char-id :stats])]
     (defn calc-stats [domain] (into [] (concat (vals (get-in stats [domain :quality])) (vals (get-in stats [domain :power])))))
     {:physical (calc-stats :physical)
      :spiritual (calc-stats :spiritual)
      :mental (calc-stats :mental)
      :social (calc-stats :social)})))

(reg-sub
 :char-minor-wounds
 (fn [db [_ char-id domain]]
   (get-in db [:characters char-id :damage domain :minor])))

(reg-sub
 :char-major-wounds
 (fn [db [_ char-id domain]]
   (get-in db [:characters char-id :damage domain :major])))

(reg-sub
 :char-total-damage
 (fn [[_ char-id domain]]
   [(rf/subscribe [:char-minor-wounds char-id domain])
    (rf/subscribe [:char-major-wounds char-id domain])])
 (fn [[minor-wounds major-wounds] _]
   (+ minor-wounds (* 2 major-wounds))))

(reg-sub
 :wounded?
 (fn [[_ char-id domain]]
   [(rf/subscribe [:char-total-damage char-id domain])
    (rf/subscribe [:action-roll-value char-id (domain {:physical 0 :spiritual 1 :mental 2 :social 3}) 1])])
 (fn [[damage health-check] _]
   (let [threshold (Math/ceil (divide (helpers/passive-result health-check) 2))]
     (> damage threshold))))

(defn find-nested
  [m k]
  (->> (tree-seq map? vals m)
       (filter map?)
       (filter (fn [node] (some #(= k %) (keys node))))))


'({:initiation {:name "Coordination", :value 1}, :reaction {:name "Reflexes", :value 1}, :continuation {:name "Endurance", :value 1}} {:initiation {:name "Exertion", :value 1}, :reaction {:name "Instinct", :value 1}, :continuation {:name "Perseverance", :value 1}} {:initiation {:name "Concentration", :value 1}, :reaction {:name "Recognition", :value 1}, :continuation {:name "Comprehension", :value 1}} {:initiation {:name "Persuasion", :value 1}, :reaction {:name "Insight", :value 1}, :continuation {:name "Connections", :value 1}})

(reg-sub
 :experience
 (fn [db [_ char-id]]
   (let [character-stats (get-in db [:characters char-id :stats])
         character-skills (map #(:quality %) (vals character-stats))
         character-abilities (map #(:power %) (vals character-stats))
         skill-cost (map #(find-nested % :value) character-skills)
         ability-cost (divide (apply + (map #(:value %) (map #(find-nested % :value) character-abilities))) 2)
         stat-cost (+ skill-cost ability-cost)] 
     (println skill-cost)
     (get-in db [:characters char-id :experience]))))

(apply vals (vals {:a {:b {:c "c" :d "d"}}}))

(reg-sub
 :equipment
 (fn [db _]
   (map first (filter #(= :equipment (:type (last %))) (get db :resources)))))

(reg-sub
 :traits
 (fn [db _]
   (map first (filter #(= :traits (:type (last %))) (get db :resources)))))

(reg-sub
 :expertise
 (fn [db _]
   (map first (filter #(= :expertise (:type (last %))) (get db :resources)))))

(reg-sub
 :affiliations
 (fn [db _]
   (map first (filter #(= :affiliations (:type (last %))) (get db :resources)))))

(reg-sub
 :items
 (fn [db _]
   (map first (filter #(= :items (:type (last %))) (get db :resources)))))

(reg-sub
 :character-resources
 (fn [db [_ char-id]]
   (keys (get-in db [:characters char-id :resources]))))

(reg-sub
 :active-resources
 (fn [db [_ char-id]]
   (get-in db [:characters char-id :active-resources])))

(reg-sub
 :resource-in-edit-mode
 (fn [db _]
   (get db :resource-in-edit-mode)))

(reg-sub
 :resource-notes
 (fn [db [_ char-id resource-id]]
   (get-in db [:characters char-id :resources resource-id :notes])))

(reg-sub
 :character-equipment
 (fn [db [_ char-id]]
   (filter #(= :equipment (get-in db [:resources % :type])) (keys (get-in db [:characters char-id :resources])))))

(reg-sub
 :character-traits
 (fn [db [_ char-id]]
   (filter #(= :traits (get-in db [:resources % :type])) (keys (get-in db [:characters char-id :resources])))))

(reg-sub
 :character-expertise
 (fn [db [_ char-id]]
   (filter #(= :expertise (get-in db [:resources % :type])) (keys (get-in db [:characters char-id :resources])))))

(reg-sub
 :character-affiliations
 (fn [db [_ char-id]]
   (filter #(= :affiliations (get-in db [:resources % :type])) (keys (get-in db [:characters char-id :resources])))))

(reg-sub
 :character-items
 (fn [db [_ char-id]]
   (filter #(= :items (get-in db [:resources % :type])) (keys (get-in db [:characters char-id :resources])))))




(reg-sub
 :global-active-resources
 (fn [db [_ resource-type]]
   (get-in db [:active-resources resource-type])))

(reg-sub
 :resources
 (fn [db _]
   (get db :resources)))

(reg-sub
 :new-resource-id
 (fn [db _]
   (->> (get db :resources)
        (keys)
        (apply max)
        inc)))

(reg-sub
 :resource
 (fn [db [_ resource-id]]
   (get-in db [:resources resource-id])))

(reg-sub
 :resource-type
 (fn [db [_ resource-id]]
   (get-in db [:resources resource-id :type])))

(reg-sub
 :resource-name
 (fn [db [_ resource-id]]
   (get-in db [:resources resource-id :name])))

(reg-sub
 :resource-description
 (fn [db [_ resource-id]]
   (get-in db [:resources resource-id :description])))

(reg-sub
 :resource-properties
 (fn [db [_ resource-id]]
   (get-in db [:resources resource-id :properties])))

(reg-sub
 :resource-quality
 (fn [db [_ resource-id]]
   (get-in db [:resources resource-id :benefits :quality])))

(reg-sub
 :resource-power
 (fn [db [_ resource-id]]
   (get-in db [:resources resource-id :benefits :power])))

(reg-sub
 :resource-details
 (fn [db [_ resource-id]]
   (get-in db [:resources resource-id :details])))

(reg-sub
 :resource-features
 (fn [db [_ resource-id]]
   (get-in db [:resources resource-id :features])))





(reg-sub
 :character-actions
 (fn [db [_ char-id]]
   (get-in db [:characters char-id :actions])))

(reg-sub
 :character-notes
 (fn [db [_ char-id]]
   (get-in db [:characters char-id :notes])))

(reg-sub
 :actions
 (fn [db _]
   (get db :actions)))

(reg-sub
 :active-action
 (fn [db [_ char-id]]
   (get-in db [:characters char-id :active-action])))

(reg-sub
 :action-roll-value
(fn [db [_ char-id action-id active-tab]]
  (let [action (get-in db [:characters char-id :actions action-id])
        action-skill-value (get-in db (concat [:characters char-id :stats] (:skill-path action) [:value]))
        action-ability-value (get-in db (concat [:characters char-id :stats] (:ability-path action) [:value]))
        action-resources (:resources action)
        action-resources-quality (map #(get-in db [:resources % :benefits :quality :value]) action-resources)
        action-resources-power (map #(get-in db [:resources % :benefits :power :value]) action-resources)
        action-dice-mod (:dice-mod action)
        action-flat-mod (:flat-mod action)]
   (case active-tab
     0 (helpers/formatted-roll
        action-skill-value
        action-ability-value
        0)
     1 (helpers/formatted-roll
        (reduce + (+ action-skill-value action-dice-mod) action-resources-quality)
        action-ability-value
        (reduce + action-flat-mod action-resources-power))
     2 (str "INCOMPLETE")
     3 (str "INCOMPLETE")
     :else (str "INCOMPLETE")))))

(reg-sub
 :action-roll-value-buttons
 (fn [db [_ char-id action-id active-tab]]
   (let [action (get-in db [:characters char-id :actions action-id])
         action-skill-value (get-in db (concat [:characters char-id :stats] (:skill-path action) [:value]))
         action-ability-value (get-in db (concat [:characters char-id :stats] (:ability-path action) [:value]))
         action-resources (:resources action)
         action-resources-quality (map #(get-in db [:resources % :benefits :quality :value]) action-resources)
         action-resources-power (map #(get-in db [:resources % :benefits :power :value]) action-resources)
         action-dice-mod (:dice-mod action)
         action-flat-mod (:flat-mod action)
         full-dice-pool [(reduce + (+ action-skill-value action-dice-mod) action-resources-quality)
                         action-ability-value
                         (reduce + action-flat-mod action-resources-power)]
         action-splinters (:splinters action)
         splintered-pools (helpers/splinter action-splinters full-dice-pool)
         action-combinations (:combinations action)
         combination-pools (map #(conj %2 (get %3 %1)) (range 0 (count splintered-pools)) splintered-pools (repeat action-combinations))]
     (case active-tab
       0 [[action-id [[action-skill-value action-ability-value 0]]]]
       1 [[action-id [[(reduce + (+ action-skill-value action-dice-mod) action-resources-quality)
                       action-ability-value
                       (reduce + action-flat-mod action-resources-power)]]]]
       2 (map #(vector action-id [%]) splintered-pools)
       3 (map #(vector action-id %) (map helpers/apply-combination combination-pools))
       :else [[action-id [[action-skill-value action-ability-value 0]]]]))))


(reg-sub
 :action-title
 (fn [db [_ char-id action-id]]
   (get-in db [:characters char-id :actions action-id :title])))

(reg-sub
 :action-skill-path
 (fn [db [_ char-id action-id]]
   (get-in db [:characters char-id :actions action-id :skill-path])))

(reg-sub
 :action-ability-path
 (fn [db [_ char-id action-id]]
   (get-in db [:characters char-id :actions action-id :ability-path])))

(reg-sub
 :action-resources
 (fn [db [_ char-id action-id]]
   (get-in db [:characters char-id :actions action-id :resources])))

(reg-sub
 :action-dice-mod
 (fn [db [_ char-id action-id]]
   (get-in db [:characters char-id :actions action-id :dice-mod])))

(reg-sub
 :action-flat-mod
 (fn [db [_ char-id action-id]]
   (get-in db [:characters char-id :actions action-id :flat-mod])))

(reg-sub
 :action-min-pool-size
 (fn [db [_ char-id action-id]]
   (get-in db [:characters char-id :actions action-id :min-pool-size])))

(reg-sub
 :action-splinters
 (fn [db [_ char-id action-id]]
   (get-in db [:characters char-id :actions action-id :splinters])))

(reg-sub
 :action-combinations
 (fn [db [_ char-id action-id]]
   (get-in db [:characters char-id :actions action-id :combinations])))



(reg-sub
 :new-dice-id
 (fn [db _]
   (let [existing-roll-ids (keys (get-in db [:rolls]))]
     (if (empty? existing-roll-ids)
       1
       (inc (apply max existing-roll-ids))))))

(reg-sub
 :char-roll-results
 (fn [db [_ char-id]]
   (get-in db [:characters char-id :rolls])))

(reg-sub
 :roll-results-by-action
 (fn [db [_ char-id action-id]]
   (get-in db [:characters char-id :rolls action-id])))