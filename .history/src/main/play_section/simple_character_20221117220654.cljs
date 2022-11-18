(ns main.play-section.simple-character
  (:require
   [main.play-section.views :as main-play-section]
   [clojure.edn :as edn :refer [read-string]]
   [clojure.core.async :as Async]
   [clojure.string :as string]
   [reagent.core :as r]
   ["react-native" :as rn]
   [main.helpers :as helpers :refer [<sub >evt]]
   [main.widgets :as widgets :refer [domain-icons]]))


(defn character-info
  [char-id]
  (if (<sub [:edit-mode])
    (main-play-section/character-profile-edit char-id)
    (if (< 0 (apply + (map count (vals (dissoc (dissoc (<sub [:character-profile char-id]) :name) :portrait)))))
      (main-play-section/character-profile char-id))))


(defn resource
  [char-id resource-id]
  (let [resource-name      (<sub [:resource-name resource-id])
        resource-editable? (<sub [:resource-editable resource-id])
        resource-quality   (:value (<sub [:resource-quality resource-id]))
        resource-power     (:value (<sub [:resource-power resource-id]))
        resource-limited?  {:inc-quality (<= 3 resource-quality)
                            :dec-quality (>= -3 resource-quality)
                            :inc-power (<= 3 resource-power)
                            :dec-power (>= -3 resource-power)}]
    [:> rn/View {:key (helpers/new-key)
                 :style {:align-self :flex-start}}
     (widgets/flat-button {:style {:height 90 :width 175 :flex-direction :column :border-width 1 :border-color :white}
                           :on-press #(doall [(>evt [:set-active-resource char-id resource-id])
                                              (>evt [:toggle-action-resource char-id 100 resource-id])
                                              (>evt [:reset-splinters char-id 100])])
                           :active? (some #(= resource-id %) (<sub [:active-resources char-id]))
                           :text [:> rn/View {:style {:flex-direction :row}}
                                  [:> rn/View {:style {:flex-direction :column :padding 0 :display (if resource-editable? "flex" "none")}}
                                   (widgets/flat-button {:style {:display (if (<sub [:edit-mode]) "flex" "none") :width 25 :height 25 :padding 0 :justify-content :center}
                                                         :on-press #(do
                                                                      (>evt [:update-resource-quality-val resource-id inc])
                                                                      (>evt [:update-exp char-id dec]))
                                                         :text-style {:font-size 10 :height 25 :top 7}
                                                         :disabled? (:inc-quality resource-limited?)
                                                         :text (:up-sm widgets/common-icons)})
                                   (widgets/flat-button {:style {:display (if (<sub [:edit-mode]) "flex" "none") :width 25 :height 25 :padding 0}
                                                         :on-press #(do
                                                                      (>evt [:update-resource-quality-val resource-id dec])
                                                                      (>evt [:update-exp char-id inc]))
                                                         :text-style {:font-size 10 :height 25 :top 7}
                                                         :disabled? (:dec-quality resource-limited?)
                                                         :text (:down-sm widgets/common-icons)})]
                                  [:> rn/View {:style {:align-self :center :justify-content :center :width 100 :flex-direction :column}}
                                   [:> rn/Text {:style {:font-size 12 :color :white :text-align :center}} resource-name]
                                   [:> rn/Text {:style {:font-size 18 :color :white :text-align :center}} (helpers/formatted-skill-bonus resource-quality resource-power)]]
                                  [:> rn/View {:style {:flex-direction :column :display (if resource-editable? "flex" "none")}}
                                   (widgets/flat-button {:style {:display (if (<sub [:edit-mode]) "flex" "none") :width 25 :height 25 :padding 0}
                                                         :on-press #(do
                                                                      (>evt [:update-resource-power-val resource-id inc])
                                                                      (>evt [:update-exp char-id dec]))
                                                         :disabled? (:inc-power resource-limited?)
                                                         :text-style {:font-size 10 :height 25 :top 7}
                                                         :text (:up-sm widgets/common-icons)})
                                   (widgets/flat-button {:style {:display (if (<sub [:edit-mode]) "flex" "none") :width 25 :height 25 :padding 0}
                                                         :on-press #(do
                                                                      (>evt [:update-resource-power-val resource-id dec])
                                                                      (>evt [:update-exp char-id inc]))
                                                         :disabled? (:dec-power resource-limited?)
                                                         :text-style {:font-size 10 :height 25 :top 7}
                                                         :text (:down-sm widgets/common-icons)})]]})]))

(defn damage-tracker
  [char-id domain]
  [:> rn/View {:style {:flex-direction :row :width 150 :height 90 :align-items :center :justify-content :center :border-width 1 :border-color :white :padding-top 10 :margin-left 1}}
   (widgets/incrementor
    #(>evt [:inflict-minor-wound char-id domain])
    false
    #(>evt [:heal-minor-wound char-id domain])
    (>= 0 (<sub [:char-minor-wounds char-id domain]))
    [:> rn/Text {:style {:color :white}}
     (<sub [:char-minor-wounds char-id domain])])
   (widgets/incrementor-alt
    #(>evt [:inflict-major-wound char-id domain])
    false
    #(>evt [:heal-major-wound char-id domain])
    (>= 0 (<sub [:char-major-wounds char-id domain]))
    [:> rn/Text {:style {:color :white}}
     (<sub [:char-major-wounds char-id domain])])
   [:> rn/Text {:style {:color (if (<sub [:wounded? char-id domain]) :red :white) :text-align :center :left 5}} (str "Total:\n" (<sub [:char-total-damage char-id domain]))]])

(defn simple-stat
  [char-id domain]
  (let [stat-name (string/capitalize (name domain))
        skill-paths [[domain :quality :initiation]
                     [domain :quality :reaction]
                     [domain :quality :continuation]]
        ability-paths [[domain :power :dominance]
                       [domain :power :competence]
                       [domain :power :resilience]]
        skill-value (int (/ (apply + (map #(:value (<sub [:character-stat char-id %])) skill-paths)) 3))
        ability-value (int (/ (apply + (map #(:value (<sub [:character-stat char-id %])) ability-paths)) 3))]
    (widgets/flat-button {:style {:height 90 :width 175 :flex-direction :column :padding 5 :border-width 1 :border-color :white}
                          :on-press #(doall [(>evt [:set-simple-action-stat-paths char-id 100 domain])
                                             (>evt [:set-active-skill char-id domain])
                                             (>evt [:set-active-ability char-id domain])])
                          :active? (and (= domain (<sub [:character-active-skill char-id]))
                                        (= domain (<sub [:character-active-ability char-id])))
                          :text [:> rn/View {:style {:flex-direction :row}}
                                 [:> rn/View {:style {:flex-direction :column :padding 0}}
                                  (widgets/flat-button {:style {:display (if (<sub [:edit-mode]) "flex" "none") :width 25 :height 25 :padding 0 :justify-content :center}
                                                        :on-press (fn []
                                                                    (doall (map #(do (>evt [:increase-skill char-id %]) (>evt [:update-exp char-id dec])) skill-paths)))
                                                        :text-style {:font-size 10 :height 25 :top 7}
                                                        :disabled? (> 1 (<sub [:experience char-id]))
                                                        :text (:up-sm widgets/common-icons)})
                                  (widgets/flat-button {:style {:display (if (<sub [:edit-mode]) "flex" "none") :width 25 :height 25 :padding 0}
                                                        :on-press (fn []
                                                                    (doall (map #(do (>evt [:decrease-skill char-id %]) (>evt [:update-exp char-id inc])) skill-paths)))
                                                        :text-style {:font-size 10 :height 25 :top 7}
                                                        :disabled? (> 2 skill-value)
                                                        :text (:down-sm widgets/common-icons)})]
                                 [:> rn/View {:style {:flex-direction :column :margin-left 5 :margin-right 5}}
                                  [:> rn/Text {:style {:font-size 12 :color :white :text-align :center}} stat-name]
                                  [:> rn/Text {:style {:font-size 18 :color :white :text-align :center}} (str skill-value "d" ability-value)]]
                                 [:> rn/View {:style {:flex-direction :column}}
                                  (widgets/flat-button {:style {:display (if (<sub [:edit-mode]) "flex" "none") :width 25 :height 25 :padding 0}
                                                        :on-press (fn []
                                                                    (doall
                                                                     (map #(do
                                                                             (>evt [:increase-ability char-id %])
                                                                             (>evt [:update-exp char-id dec])) ability-paths)))
                                                        :disabled? (or (< 11 ability-value) (> 1 (<sub [:experience char-id])))
                                                        :text-style {:font-size 10 :height 25 :top 7}
                                                        :text (:up-sm widgets/common-icons)})
                                  (widgets/flat-button {:style {:display (if (<sub [:edit-mode]) "flex" "none") :width 25 :height 25 :padding 0}
                                                        :on-press (fn []
                                                                    (doall (map #(do
                                                                                   (>evt [:decrease-ability char-id %])
                                                                                   (>evt [:update-exp char-id inc]))
                                                                                ability-paths)))
                                                        :disabled? (> 5 ability-value)
                                                        :text-style {:font-size 10 :height 25 :top 7}
                                                        :text (:down-sm widgets/common-icons)})]]})))

(defn resource-subs
  [domain]
  (case domain
    :physical :character-equipment
    :spiritual :character-traits
    :mental :character-expertise
    :social :character-affiliations
    :else :character-items))

(defn simple-stat-row
  [char-id domain stat-fn damage?]
  [:> rn/View {:key (helpers/new-key)}
   [:> rn/View {:style {:flex-direction :row :align-items :center}}
    (domain domain-icons)
    (stat-fn char-id domain)
    (if damage?
      (damage-tracker char-id domain)
      [:> rn/View {:width 100}])
    [:> rn/View {:style {:overflow :hidden :width 360 :height 95 :margin-top -1 :flex-direction :row}}
     (doall (map resource (repeat char-id) (<sub [(resource-subs domain) char-id])))]]])

(defn circumstantial-bonuses
  [char-id]
  [:> rn/View {:style {:border-color :white :border-width 1 :width 200 :align-self :center}}
   [:> rn/Text {:style {:color :white :font-size 16 :text-align :center}} "Circumstantial Bonuses"]
   [:> rn/View {:style {:width 175 :align-self :center :flex-direction :row :align-items :center :justify-content :center :padding 10}}
    (widgets/incrementor
     #(doall [(>evt [:update-action-dice-mod char-id 100 inc])
              (>evt [:reset-splinters char-id 100])])
     false
     #(doall [(>evt [:update-action-dice-mod char-id 100 dec])
              (>evt [:reset-splinters char-id 100])])
     false)
    [:> rn/Text {:style {:text-align :center :flex 3 :font-size 24 :color :white}}
     (let [formatted-roll (helpers/formatted-skill-bonus (<sub [:action-dice-mod char-id 100])
                                                         (<sub [:action-flat-mod char-id 100]))]
       (if (empty? formatted-roll)
         "--"
         formatted-roll))]
    (widgets/incrementor
     #(>evt [:update-action-flat-mod char-id 100 inc])
     false
     #(>evt [:update-action-flat-mod char-id 100 dec])
     false)]])

(<sub [:active-resources 1])

(defn character-stats-and-resources
  [char-id]
  [:> rn/View {:style {:width "100%" :padding-bottom 15}}
   [:> rn/Text {:style {:color :white :align-self :center :font-size 24}} "Stats & Resources"]
   (if (some? (<sub [:character-active-skill char-id]))
     (main-play-section/dice-rolling-section char-id 100))
   (if (some? (<sub [:character-active-skill char-id]))
     (circumstantial-bonuses char-id))
   [:> rn/View {:style {:flex-direction :row}}
    (main-play-section/character-experience char-id)
    [:> rn/Text {:style {:align-self :flex-start :color :white :text-align :center :width 175 :margin-right 5}} "Stats"]
    [:> rn/Text {:style {:align-self :flex-start :color :white :text-align :center :width 150 :margin-right 5}} "Damage"]
    [:> rn/Text {:style {:align-self :flex-start :color :white :text-align :center :width 175}} "Resources"]]
   (doall (map simple-stat-row
               (repeat char-id)
               [:physical :spiritual :mental :social]
               (repeat simple-stat)
               (repeat true)))
   (if (not-empty (<sub [:character-items char-id])) 
     [:> rn/Text {:style {:color :white :width "100%" :font-size 24 :padding-top 30 :padding-left 20}} "Items"])
   [:> rn/View {:style {:align-self :center :width "100%" :flex-wrap :wrap :margin-top -1 :flex-direction :row}}
    (doall (map resource (repeat char-id) (<sub [:character-items char-id])))]])

(defn character-data
  [char-id]
  [:> rn/View
   (character-stats-and-resources char-id)])

(defn character-details
  [char-id]
  (into [:> rn/View {:style {:width "95%"}}]
        (doall (map widgets/card
             [(character-info char-id)
              (character-data char-id)
              (if (<sub [:edit-mode]) (main-play-section/resources char-id main-play-section/resource-button true))
              (main-play-section/character-notes char-id)]
             ["Details"
              "Stats & Resources"
              (if (<sub [:edit-mode]) "Resources Edit")
              "Notes"]
             [main-play-section/character-details-toggle
              main-play-section/character-stats-toggle
              (if (<sub [:edit-mode]) main-play-section/character-resources-toggle)
              main-play-section/character-notes-toggle]))))

(defn characters-tab
  []
  (let [char-id (<sub [:active-character])]
    [:> rn/View {:style {:width "100%" :align-items :center}} 
     [:> rn/Text {:style {:color :white :font-size 30}} "Characters"]
     (widgets/float-button {:text "Database Download/Upload"
                            :text-style {:color :white}
                            :active? @main-play-section/show-db-diff
                            :on-press #(swap! main-play-section/show-db-diff not)})
     (if @main-play-section/show-db-diff
       (widgets/text-area
        (fn [i] (do (>evt [:manual-initialize-db (edn/read-string (str i))])
                    (swap! main-play-section/show-db-diff not)))
        ""
        "Upload Data"))
     (if @main-play-section/show-db-diff
       [:> rn/Text {:style {:color :white :text-align :center :margin-left "10%" :margin-right "10%"}
                    :selectable true}
        (str "To save data for future use, copy all of the data below and save it to some external location. To reload the data, copy all of that data back into the input field and submit it.\n\n"
             (<sub [:db-diff]))])

     [:> rn/View {:style {:position :absolute :right 0 :top 0 :width 50 :height 50 :border-radius 100}}
      (if (nil? char-id)
        main-play-section/character-add-button
        [:> rn/View
         (main-play-section/character-edit-button)
         main-play-section/character-init-delete-button
         (if @main-play-section/confirm-delete (main-play-section/character-delete-confirm-button))])]
     (if (<sub [:edit-mode])
       (main-play-section/name-edit char-id)
       (main-play-section/character-buttons))
     (if (some? char-id) (character-details char-id))]))

(defn resources-tab
  []
  (main-play-section/resources nil main-play-section/resource-button nil))

(defn play-section
  []
  (case (<sub [:active-tab 1])
    :characters (characters-tab)
    :resources (resources-tab)
    :else [:> rn/Text {:style {:color :white}} "Invalid Tab"]))





(>evt [:manual-initialize-db {:resources {316 {:benefits {:power {:value 1}, :quality {:value 1}}}, 156 {:benefits {:power {:value 1}, :quality {:value 1}}}, 147 {:benefits {:power {:value 3}}}, 300 {:benefits {:power {:value 1}, :quality {:value 2}}}, 5512 {:id 202, :editable? true, :type :traits, :name "Create a Thieves' Guild", :properties [:goal], :benefits {:quality {:name "Clarity", :value 2}, :power {:name "Dedication", :value 0}}, :details "Grants +Clarity +Dedication to checks that move you towards accomplishing your goal Checks that set you back with respect to your goal gain penalties equal to the Quality and Power of your goal. Circumstances may cause multiple traits to apply bonuses or penalties to a check. Ultimately, it is up to the DM's discretion whether a trait grants a bonus or penalty. Some examples of goals are to discover forgotten magics, wealth, or to be granted knighthood."}, 5513 {:id 200, :editable? true, :type :traits, :name "Wealth (Ideal)", :properties [:ideal], :benefits {:quality {:name "Clarity", :value 0}, :power {:name "Dedication", :value 2}}, :details "Grants +Clarity +Dedication to checks that promote or exemplify your ideal. Checks that run counter to your ideal gain penalties equal to the Quality and Power of your ideal. Circumstances may cause multiple traits to apply bonuses or penalties to a check. Ultimately, it is up to the DM's discretion whether a trait grants a bonus or penalty. Some examples of ideals are courage, life, and freedom."}, 5514 {:id 401, :editable? true, :type :affiliations, :name "Heist Crew", :properties [:individual], :benefits {:quality {:name "Reach", :value 0}, :power {:name "Influence", :value 2}}, :details "Gain +Reach +Influence on Social checks while interacting with this group. The Game Master may give partial bonuses on social checks where this group is a relevant. Examples of groups that you may be affiliated with are guilds, religious orders, schools, or communities."}, 5515 {:id 400, :editable? true, :type :affiliations, :name "Thievery Master", :properties [:individual], :benefits {:quality {:name "Reach", :value 2}, :power {:name "Influence", :value 0}}, :details "Gain +Reach +Influence on Social checks while interacting with this individual. The Game Master may give partial bonuses on social checks where this individual is a mutual acquaintance. Examples of individuals that you may be affiliated with are allies, friends, mentors, or family."}}, :characters {5 {:experience -30, :actions {0 {:description "", :combinations {}, :flat-mod 0, :title "Physical Health Check", :min-pool-size 2, :ability-path [:physical :power :resilience], :target-number nil, :splinters 1, :resources [], :skill-path [:physical :quality :continuation], :dice-mod 0}, 1 {:description "", :combinations {}, :flat-mod 0, :title "Spiritual Health Check", :min-pool-size 2, :ability-path [:spiritual :power :resilience], :target-number nil, :splinters 1, :resources [], :skill-path [:spiritual :quality :continuation], :dice-mod 0}, 2 {:description "", :combinations {}, :flat-mod 0, :title "Mental Health Check", :min-pool-size 2, :ability-path [:mental :power :resilience], :target-number nil, :splinters 1, :resources [], :skill-path [:mental :quality :continuation], :dice-mod 0}, 3 {:description "", :combinations {}, :flat-mod 0, :title "Social Health Check", :min-pool-size 2, :ability-path [:social :power :resilience], :target-number nil, :splinters 1, :resources [], :skill-path [:social :quality :continuation], :dice-mod 0}, 100 {:resources (), :splinters 1, :combinations {}, :stat nil}}, :damage {:physical {:minor 0, :major 0}, :spiritual {:minor 0, :major 0}, :mental {:minor 0, :major 0}, :social {:minor 0, :major 0}}, :id 1, :active-stats {:skill nil, :ability nil}, :notes "", :rolls {100 [{:roll ((4 3 4 4 1 2)), :bonus 0, :roll-result 4}]}, :active-resources (), :active-action nil, :resources {5512 {:notes ""}, 5513 {:notes ""}, 5514 {:notes ""}, 5515 {:notes ""}, 300 {:notes ""}, 147 {:notes ""}, 503 {:notes ""}, 504 {:notes ""}, 156 {:notes ""}, 316 {:notes ""}, 509 {:notes ""}, 511 {:notes ""}}, :stats {:physical {:quality {:initiation {:name "Coordination", :value 4}, :reaction {:name "Reflexes", :value 4}, :continuation {:name "Endurance", :value 4}}, :power {:dominance {:name "Might", :value 6}, :competence {:name "Finesse", :value 6}, :resilience {:name "Fortitude", :value 6}}}, :spiritual {:quality {:initiation {:name "Exertion", :value 1}, :reaction {:name "Instinct", :value 1}, :continuation {:name "Perseverance", :value 1}}, :power {:dominance {:name "Willpower", :value 6}, :competence {:name "Discipline", :value 6}, :resilience {:name "Tenacity", :value 6}}}, :mental {:quality {:initiation {:name "Concentration", :value 1}, :reaction {:name "Recognition", :value 1}, :continuation {:name "Comprehension", :value 1}}, :power {:dominance {:name "Intellect", :value 6}, :competence {:name "Focus", :value 6}, :resilience {:name "Stability", :value 6}}}, :social {:quality {:initiation {:name "Persuasion", :value 2}, :reaction {:name "Insight", :value 2}, :continuation {:name "Connections", :value 2}}, :power {:dominance {:name "Presence", :value 6}, :competence {:name "Wit", :value 6}, :resilience {:name "Poise", :value 6}}}}, :profile {:portrait "../assets/Avis.png", :name "Rogue Template", :gender nil, :race nil, :titles nil, :description nil}}}, :resource-in-edit-mode nil}




])
