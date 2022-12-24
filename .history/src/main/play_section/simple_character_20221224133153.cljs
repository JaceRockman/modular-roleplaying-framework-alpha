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
  [:> rn/ScrollView
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

     (widgets/float-button {:text "Toggle Example Characters"
                            :text-style {:color :white}
                            :on-press #(>evt [:toggle-character-templates])})

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

(defn toggle-character-templates
  []
  (widgets/float-button {:text "Toggle Example Characters"
                         :text-style {:color :white}
                         :on-press #(>evt [:toggle-character-templates])}))

;; (>evt [:manual-initialize-db {:resources {316 {:benefits {:power {:value 1}, :quality {:value 1}}}, 156 {:benefits {:power {:value 1}, :quality {:value 1}}}, 147 {:benefits {:power {:value 3}}}, 300 {:benefits {:power {:value 1}, :quality {:value 2}}}, 5512 {:id 202, :editable? true, :type :traits, :name "Create a Thieves' Guild", :properties [:goal], :benefits {:quality {:name "Clarity", :value 2}, :power {:name "Dedication", :value 0}}, :details "Grants +Clarity +Dedication to checks that move you towards accomplishing your goal Checks that set you back with respect to your goal gain penalties equal to the Quality and Power of your goal. Circumstances may cause multiple traits to apply bonuses or penalties to a check. Ultimately, it is up to the DM's discretion whether a trait grants a bonus or penalty. Some examples of goals are to discover forgotten magics, wealth, or to be granted knighthood."}, 5513 {:id 200, :editable? true, :type :traits, :name "Wealth (Ideal)", :properties [:ideal], :benefits {:quality {:name "Clarity", :value 0}, :power {:name "Dedication", :value 2}}, :details "Grants +Clarity +Dedication to checks that promote or exemplify your ideal. Checks that run counter to your ideal gain penalties equal to the Quality and Power of your ideal. Circumstances may cause multiple traits to apply bonuses or penalties to a check. Ultimately, it is up to the DM's discretion whether a trait grants a bonus or penalty. Some examples of ideals are courage, life, and freedom."}, 5514 {:id 401, :editable? true, :type :affiliations, :name "Heist Crew", :properties [:individual], :benefits {:quality {:name "Reach", :value 0}, :power {:name "Influence", :value 2}}, :details "Gain +Reach +Influence on Social checks while interacting with this group. The Game Master may give partial bonuses on social checks where this group is a relevant. Examples of groups that you may be affiliated with are guilds, religious orders, schools, or communities."}, 5515 {:id 400, :editable? true, :type :affiliations, :name "Thievery Master", :properties [:individual], :benefits {:quality {:name "Reach", :value 2}, :power {:name "Influence", :value 0}}, :details "Gain +Reach +Influence on Social checks while interacting with this individual. The Game Master may give partial bonuses on social checks where this individual is a mutual acquaintance. Examples of individuals that you may be affiliated with are allies, friends, mentors, or family."}}, :characters {5 {:experience -30, :actions {0 {:description "", :combinations {}, :flat-mod 0, :title "Physical Health Check", :min-pool-size 2, :ability-path [:physical :power :resilience], :target-number nil, :splinters 1, :resources [], :skill-path [:physical :quality :continuation], :dice-mod 0}, 1 {:description "", :combinations {}, :flat-mod 0, :title "Spiritual Health Check", :min-pool-size 2, :ability-path [:spiritual :power :resilience], :target-number nil, :splinters 1, :resources [], :skill-path [:spiritual :quality :continuation], :dice-mod 0}, 2 {:description "", :combinations {}, :flat-mod 0, :title "Mental Health Check", :min-pool-size 2, :ability-path [:mental :power :resilience], :target-number nil, :splinters 1, :resources [], :skill-path [:mental :quality :continuation], :dice-mod 0}, 3 {:description "", :combinations {}, :flat-mod 0, :title "Social Health Check", :min-pool-size 2, :ability-path [:social :power :resilience], :target-number nil, :splinters 1, :resources [], :skill-path [:social :quality :continuation], :dice-mod 0}, 100 {:resources (), :splinters 1, :combinations {}, :stat nil}}, :damage {:physical {:minor 0, :major 0}, :spiritual {:minor 0, :major 0}, :mental {:minor 0, :major 0}, :social {:minor 0, :major 0}}, :id 1, :active-stats {:skill nil, :ability nil}, :notes "", :active-resources (), :active-action nil, :resources {5512 {:notes ""}, 5513 {:notes ""}, 5514 {:notes ""}, 5515 {:notes ""}, 300 {:notes ""}, 147 {:notes ""}, 503 {:notes ""}, 504 {:notes ""}, 156 {:notes ""}, 316 {:notes ""}, 509 {:notes ""}, 511 {:notes ""}}, :stats {:physical {:quality {:initiation {:name "Coordination", :value 4}, :reaction {:name "Reflexes", :value 4}, :continuation {:name "Endurance", :value 4}}, :power {:dominance {:name "Might", :value 6}, :competence {:name "Finesse", :value 6}, :resilience {:name "Fortitude", :value 6}}}, :spiritual {:quality {:initiation {:name "Exertion", :value 1}, :reaction {:name "Instinct", :value 1}, :continuation {:name "Perseverance", :value 1}}, :power {:dominance {:name "Willpower", :value 6}, :competence {:name "Discipline", :value 6}, :resilience {:name "Tenacity", :value 6}}}, :mental {:quality {:initiation {:name "Concentration", :value 1}, :reaction {:name "Recognition", :value 1}, :continuation {:name "Comprehension", :value 1}}, :power {:dominance {:name "Intellect", :value 6}, :competence {:name "Focus", :value 6}, :resilience {:name "Stability", :value 6}}}, :social {:quality {:initiation {:name "Persuasion", :value 2}, :reaction {:name "Insight", :value 2}, :continuation {:name "Connections", :value 2}}, :power {:dominance {:name "Presence", :value 6}, :competence {:name "Wit", :value 6}, :resilience {:name "Poise", :value 6}}}}, :profile {:portrait "../assets/Avis.png", :name "Rogue", :gender nil, :race nil, :titles nil, :description nil}}}, :resource-in-edit-mode nil}])

;; (>evt [:manual-initialize-db {:resources {316 {:benefits {:power {:value 1}, :quality {:value 1}}}, 149 {:benefits {:quality {:value 2}}}, 145 {:benefits {:power {:value 1}, :quality {:value 1}}}, 301 {:benefits {:power {:value 1}, :quality {:value 2}}}, 3512 {:id 400, :editable? true, :type :affiliations, :name "Arms Instructor", :properties [:individual], :benefits {:quality {:name "Reach", :value 0}, :power {:name "Influence", :value 2}}, :details "Gain +Reach +Influence on Social checks while interacting with this individual. The Game Master may give partial bonuses on social checks where this individual is a mutual acquaintance. Examples of individuals that you may be affiliated with are allies, friends, mentors, or family."}, 3513 {:id 401, :editable? true, :type :affiliations, :name "Battalion", :properties [:individual], :benefits {:quality {:name "Reach", :value 2}, :power {:name "Influence", :value 0}}, :details "Gain +Reach +Influence on Social checks while interacting with this group. The Game Master may give partial bonuses on social checks where this group is a relevant. Examples of groups that you may be affiliated with are guilds, religious orders, schools, or communities."}, 3514 {:id 200, :editable? true, :type :traits, :name "Bravery (Ideal)", :properties [:ideal], :benefits {:quality {:name "Clarity", :value 0}, :power {:name "Dedication", :value 2}}, :details "Grants +Clarity +Dedication to checks that promote or exemplify your ideal. Checks that run counter to your ideal gain penalties equal to the Quality and Power of your ideal. Circumstances may cause multiple traits to apply bonuses or penalties to a check. Ultimately, it is up to the DM's discretion whether a trait grants a bonus or penalty. Some examples of ideals are courage, life, and freedom."}, 3515 {:id 202, :editable? true, :type :traits, :name "Achieve the Rank of Master of Arms", :properties [:goal], :benefits {:quality {:name "Clarity", :value 1}, :power {:name "Dedication", :value 0}}, :details "Grants +Clarity +Dedication to checks that move you towards accomplishing your goal Checks that set you back with respect to your goal gain penalties equal to the Quality and Power of your goal. Circumstances may cause multiple traits to apply bonuses or penalties to a check. Ultimately, it is up to the DM's discretion whether a trait grants a bonus or penalty. Some examples of goals are to discover forgotten magics, wealth, or to be granted knighthood."}}, :characters {3 {:experience -28, :actions {0 {:description "", :combinations {}, :flat-mod 0, :title "Physical Health Check", :min-pool-size 2, :ability-path [:physical :power :resilience], :target-number nil, :splinters 1, :resources [], :skill-path [:physical :quality :continuation], :dice-mod 0}, 1 {:description "", :combinations {}, :flat-mod 0, :title "Spiritual Health Check", :min-pool-size 2, :ability-path [:spiritual :power :resilience], :target-number nil, :splinters 1, :resources [], :skill-path [:spiritual :quality :continuation], :dice-mod 0}, 2 {:description "", :combinations {}, :flat-mod 0, :title "Mental Health Check", :min-pool-size 2, :ability-path [:mental :power :resilience], :target-number nil, :splinters 1, :resources [], :skill-path [:mental :quality :continuation], :dice-mod 0}, 3 {:description "", :combinations {}, :flat-mod 0, :title "Social Health Check", :min-pool-size 2, :ability-path [:social :power :resilience], :target-number nil, :splinters 1, :resources [], :skill-path [:social :quality :continuation], :dice-mod 0}, 100 {:stat nil, :resources (), :splinters 1, :combinations {}}}, :damage {:physical {:minor 0, :major 0}, :spiritual {:minor 0, :major 0}, :mental {:minor 0, :major 0}, :social {:minor 0, :major 0}}, :id 1, :active-stats {:skill nil, :ability nil}, :notes "", :active-resources (), :active-action nil, :resources {3512 {:notes ""}, 3513 {:notes ""}, 3514 {:notes ""}, 3515 {:notes ""}, 301 {:notes ""}, 145 {:notes ""}, 149 {:notes ""}, 504 {:notes ""}, 316 {:notes ""}, 510 {:notes ""}, 511 {:notes ""}}, :stats {:physical {:quality {:initiation {:name "Coordination", :value 3}, :reaction {:name "Reflexes", :value 3}, :continuation {:name "Endurance", :value 3}}, :power {:dominance {:name "Might", :value 8}, :competence {:name "Finesse", :value 8}, :resilience {:name "Fortitude", :value 8}}}, :spiritual {:quality {:initiation {:name "Exertion", :value 1}, :reaction {:name "Instinct", :value 1}, :continuation {:name "Perseverance", :value 1}}, :power {:dominance {:name "Willpower", :value 6}, :competence {:name "Discipline", :value 6}, :resilience {:name "Tenacity", :value 6}}}, :mental {:quality {:initiation {:name "Concentration", :value 2}, :reaction {:name "Recognition", :value 2}, :continuation {:name "Comprehension", :value 2}}, :power {:dominance {:name "Intellect", :value 6}, :competence {:name "Focus", :value 6}, :resilience {:name "Stability", :value 6}}}, :social {:quality {:initiation {:name "Persuasion", :value 1}, :reaction {:name "Insight", :value 1}, :continuation {:name "Connections", :value 1}}, :power {:dominance {:name "Presence", :value 6}, :competence {:name "Wit", :value 6}, :resilience {:name "Poise", :value 6}}}}, :profile {:portrait "../assets/Avis.png", :name "Fighter", :gender nil, :race nil, :titles nil, :description nil}}}, :resource-in-edit-mode nil}])

;; (>evt [:manual-initialize-db {:resources {155 {:benefits {:power {:value 1}, :quality {:value 1}}}, 311 {:benefits {:power {:value 1}, :quality {:value 2}}}, 302 {:benefits {:quality {:value 1}}}, 4512 {:id 148, :editable? true, :type :equipment, :name "Religious History Book", :properties [:tool], :benefits {:quality {:name "Quality", :value 1}, :power {:name "Versatility", :value 0}}, :details "Gain +Quality +Versatility on checks that use these documents to recall information related to the topic of these documents."}, 4513 {:id 402, :editable? true, :type :affiliations, :name "Sentinel Rank", :properties [:individual], :benefits {:quality {:name "Reach", :value 0}, :power {:name "Influence", :value 2}}, :details "Gain +Reach +Influence on Social checks while interacting with individuals who are familiar with your acknowledgements. The Game Master may give partial bonuses based on how familiar the individual is with your acknowledgements. Examples of acknowledgements that may be affiliated with you are reputations, titles, degrees or certifications, and rumors."}, 4514 {:id 401, :editable? true, :type :affiliations, :name "Paladin's of Heilm", :properties [:individual], :benefits {:quality {:name "Reach", :value 1}, :power {:name "Influence", :value 1}}, :details "Gain +Reach +Influence on Social checks while interacting with this group. The Game Master may give partial bonuses on social checks where this group is a relevant. Examples of groups that you may be affiliated with are guilds, religious orders, schools, or communities."}, 4515 {:id 200, :editable? true, :type :traits, :name "Protect Life (Ideal)", :properties [:ideal], :benefits {:quality {:name "Clarity", :value 2}, :power {:name "Dedication", :value 0}}, :details "Grants +Clarity +Dedication to checks that promote or exemplify your ideal. Checks that run counter to your ideal gain penalties equal to the Quality and Power of your ideal. Circumstances may cause multiple traits to apply bonuses or penalties to a check. Ultimately, it is up to the DM's discretion whether a trait grants a bonus or penalty. Some examples of ideals are courage, life, and freedom."}, 4516 {:id 203, :editable? true, :type :traits, :name "Uphold the Edicts of Heilm (Oath)", :properties [:oath], :benefits {:quality {:name "Clarity", :value 0}, :power {:name "Dedication", :value 1}}, :details "Grants +Clarity +Dedication to checks that are aligned with your oath. Checks that are misaligned with your oath gain penalties equal to the Quality and Power of your oath. Circumstances may cause multiple traits to apply bonuses or penalties to a check. Ultimately, it is up to the DM's discretion whether a trait grants a bonus or penalty. Some examples of oaths are to uphold the virtues of Heilm, an oath of revenge, or an oath to serve the crown."}}, :worlds {1 {:active-civilization 401}}, :sections {2 {:active-tab :civilizations}}, :characters {4 {:experience -26, :actions {0 {:description "", :combinations {}, :flat-mod 0, :title "Physical Health Check", :min-pool-size 2, :ability-path [:physical :power :resilience], :target-number nil, :splinters 1, :resources [], :skill-path [:physical :quality :continuation], :dice-mod 0}, 1 {:description "", :combinations {}, :flat-mod 0, :title "Spiritual Health Check", :min-pool-size 2, :ability-path [:spiritual :power :resilience], :target-number nil, :splinters 1, :resources [], :skill-path [:spiritual :quality :continuation], :dice-mod 0}, 2 {:description "", :combinations {}, :flat-mod 0, :title "Mental Health Check", :min-pool-size 2, :ability-path [:mental :power :resilience], :target-number nil, :splinters 1, :resources [], :skill-path [:mental :quality :continuation], :dice-mod 0}, 3 {:description "", :combinations {}, :flat-mod 0, :title "Social Health Check", :min-pool-size 2, :ability-path [:social :power :resilience], :target-number nil, :splinters 1, :resources [], :skill-path [:social :quality :continuation], :dice-mod 0}, 100 {:resources ()}}, :damage {:physical {:minor 0, :major 0}, :spiritual {:minor 0, :major 0}, :mental {:minor 0, :major 0}, :social {:minor 0, :major 0}}, :id 1, :notes "", :active-resources (), :active-action nil, :resources {4512 {:notes ""}, 4513 {:notes ""}, 4514 {:notes ""}, 4515 {:notes ""}, 4516 {:notes ""}, 302 {:notes ""}, 502 {:notes ""}, 311 {:notes ""}, 504 {:notes ""}, 155 {:notes ""}, 510 {:notes ""}, 511 {:notes ""}}, :stats {:physical {:quality {:initiation {:name "Coordination", :value 2}, :reaction {:name "Reflexes", :value 2}, :continuation {:name "Endurance", :value 2}}, :power {:dominance {:name "Might", :value 10}, :competence {:name "Finesse", :value 10}, :resilience {:name "Fortitude", :value 10}}}, :spiritual {:quality {:initiation {:name "Exertion", :value 1}, :reaction {:name "Instinct", :value 1}, :continuation {:name "Perseverance", :value 1}}, :power {:dominance {:name "Willpower", :value 8}, :competence {:name "Discipline", :value 8}, :resilience {:name "Tenacity", :value 8}}}, :mental {:quality {:initiation {:name "Concentration", :value 1}, :reaction {:name "Recognition", :value 1}, :continuation {:name "Comprehension", :value 1}}, :power {:dominance {:name "Intellect", :value 6}, :competence {:name "Focus", :value 6}, :resilience {:name "Stability", :value 6}}}, :social {:quality {:initiation {:name "Persuasion", :value 1}, :reaction {:name "Insight", :value 1}, :continuation {:name "Connections", :value 1}}, :power {:dominance {:name "Presence", :value 6}, :competence {:name "Wit", :value 6}, :resilience {:name "Poise", :value 6}}}}, :profile {:portrait "../assets/Avis.png", :name "Paladin", :gender nil, :race nil, :titles nil, :description nil}}}, :active-resource nil, :resource-in-edit-mode nil}])



;; (>evt [:manual-initialize-db {:resources {151 {:benefits {:quality {:value 1}}}, 150 {:benefits {:power {:value 1}, :quality {:value 1}}}, 303 {:benefits {:power {:value 2}}}, 1512 {:id 1313, :editable? true, :type :expertise, :name "Researcher of Aethrology", :properties [:experience], :benefits {:quality {:name "Skill", :value 1}, :power {:name "Equipment", :value 0}}, :details "Gain +Skill +Equipment on checks to gather information about (Topic). This can be through multiple means such as experimentation, locating sources of information, or interpreting those sources of information. If a check to gain information fails, you will at least gain some idea of where you need to go in order to gain the information."}, 1513 {:id 1402, :editable? true, :type :affiliations, :name "Kairinith University Graduate", :properties [:individual], :benefits {:quality {:name "Reach", :value 1}, :power {:name "Influence", :value 1}}, :details "Gain +Reach +Influence on Social checks while interacting with individuals who are familiar with your acknowledgements. The Game Master may give partial bonuses based on how familiar the individual is with your acknowledgements. Examples of acknowledgements that may be affiliated with you are reputations, titles, degrees or certifications, and rumors."}, 1514 {:id 401, :editable? true, :type :affiliations, :name "Aethrologist's Guild", :properties [:individual], :benefits {:quality {:name "Reach", :value 0}, :power {:name "Influence", :value 2}}, :details "Gain +Reach +Influence on Social checks while interacting with this group. The Game Master may give partial bonuses on social checks where this group is a relevant. Examples of groups that you may be affiliated with are guilds, religious orders, schools, or communities."}, 1515 {:id 200, :editable? true, :type :traits, :name "Progress of Civilization", :properties [:ideal], :benefits {:quality {:name "Clarity", :value 1}, :power {:name "Dedication", :value 1}}, :details "Grants +Clarity +Dedication to checks that promote or exemplify your ideal. Checks that run counter to your ideal gain penalties equal to the Quality and Power of your ideal. Circumstances may cause multiple traits to apply bonuses or penalties to a check. Ultimately, it is up to the DM's discretion whether a trait grants a bonus or penalty. Some examples of ideals are courage, life, and freedom."}, 1516 {:id 202, :editable? true, :type :traits, :name "Discover Lost Magics", :properties [:goal], :benefits {:quality {:name "Clarity", :value 1}, :power {:name "Dedication", :value 0}}, :details "Grants +Clarity +Dedication to checks that move you towards accomplishing your goal Checks that set you back with respect to your goal gain penalties equal to the Quality and Power of your goal. Circumstances may cause multiple traits to apply bonuses or penalties to a check. Ultimately, it is up to the DM's discretion whether a trait grants a bonus or penalty. Some examples of goals are to discover forgotten magics, wealth, or to be granted knighthood."}}, :characters {1 {:experience -28, :actions {0 {:description "", :combinations {}, :flat-mod 0, :title "Physical Health Check", :min-pool-size 2, :ability-path [:physical :power :resilience], :target-number nil, :splinters 1, :resources [], :skill-path [:physical :quality :continuation], :dice-mod 0}, 1 {:description "", :combinations {}, :flat-mod 0, :title "Spiritual Health Check", :min-pool-size 2, :ability-path [:spiritual :power :resilience], :target-number nil, :splinters 1, :resources [], :skill-path [:spiritual :quality :continuation], :dice-mod 0}, 2 {:description "", :combinations {}, :flat-mod 0, :title "Mental Health Check", :min-pool-size 2, :ability-path [:mental :power :resilience], :target-number nil, :splinters 1, :resources [], :skill-path [:mental :quality :continuation], :dice-mod 0}, 3 {:description "", :combinations {}, :flat-mod 0, :title "Social Health Check", :min-pool-size 2, :ability-path [:social :power :resilience], :target-number nil, :splinters 1, :resources [], :skill-path [:social :quality :continuation], :dice-mod 0}, 100 {:resources ()}}, :damage {:physical {:minor 0, :major 0}, :spiritual {:minor 0, :major 0}, :mental {:minor 0, :major 0}, :social {:minor 0, :major 0}}, :id 1, :notes "", :active-resources (), :active-action nil, :resources {1512 {:notes ""}, 1513 {:notes ""}, 1514 {:notes ""}, 1515 {:notes ""}, 1516 {:notes ""}, 303 {:notes ""}, 501 {:notes ""}, 150 {:notes ""}, 151 {:notes ""}, 510 {:notes ""}}, :stats {:physical {:quality {:initiation {:name "Coordination", :value 1}, :reaction {:name "Reflexes", :value 1}, :continuation {:name "Endurance", :value 1}}, :power {:dominance {:name "Might", :value 6}, :competence {:name "Finesse", :value 6}, :resilience {:name "Fortitude", :value 6}}}, :spiritual {:quality {:initiation {:name "Exertion", :value 4}, :reaction {:name "Instinct", :value 4}, :continuation {:name "Perseverance", :value 4}}, :power {:dominance {:name "Willpower", :value 6}, :competence {:name "Discipline", :value 6}, :resilience {:name "Tenacity", :value 6}}}, :mental {:quality {:initiation {:name "Concentration", :value 2}, :reaction {:name "Recognition", :value 2}, :continuation {:name "Comprehension", :value 2}}, :power {:dominance {:name "Intellect", :value 8}, :competence {:name "Focus", :value 8}, :resilience {:name "Stability", :value 8}}}, :social {:quality {:initiation {:name "Persuasion", :value 1}, :reaction {:name "Insight", :value 1}, :continuation {:name "Connections", :value 1}}, :power {:dominance {:name "Presence", :value 6}, :competence {:name "Wit", :value 6}, :resilience {:name "Poise", :value 6}}}}, :profile {:portrait "../assets/Avis.png", :name "Arcanist", :gender nil, :race nil, :titles nil, :description nil}}}, :resource-in-edit-mode nil}])

;; (>evt [:manual-initialize-db {:resources {155 {:benefits {:power {:value 1}, :quality {:value 1}}}, 153 {:benefits {:quality {:value 1}}}, 310 {:benefits {:power {:value 1}, :quality {:value 2}}}, 2512 {:id 200, :editable? true, :type :traits, :name "Kindness (Ideal)", :properties [:ideal], :benefits {:quality {:name "Clarity", :value 1}, :power {:name "Dedication", :value 0}}, :details "Grants +Clarity +Dedication to checks that promote or exemplify your ideal. Checks that run counter to your ideal gain penalties equal to the Quality and Power of your ideal. Circumstances may cause multiple traits to apply bonuses or penalties to a check. Ultimately, it is up to the DM's discretion whether a trait grants a bonus or penalty. Some examples of ideals are courage, life, and freedom."}, 2513 {:id 201, :editable? true, :type :traits, :name "Preserve Nature (Purpose)", :properties [:purpose], :benefits {:quality {:name "Clarity", :value 0}, :power {:name "Dedication", :value 2}}, :details "Grants +Clarity +Dedication to checks that fulfill your purpose. Checks that contradict your purpose gain penalties equal to the Quality and Power of your purpose. Circumstances may cause multiple traits to apply bonuses or penalties to a check. Ultimately, it is up to the DM's discretion whether a trait grants a bonus or penalty. Some examples of purposes would be to protect something important to you or to serve a deity or power."}, 2514 {:id 312, :editable? true, :type :expertise, :name "Healing Expertise", :properties [:education], :benefits {:quality {:name "Skill", :value 1}, :power {:name "Equipment", :value 1}}, :details "Gain +Skill +Equipment on checks to recall information related to (Topic)."}, 2515 {:id 401, :editable? true, :type :affiliations, :name "The Eldari Druids", :properties [:individual], :benefits {:quality {:name "Reach", :value 1}, :power {:name "Influence", :value 0}}, :details "Gain +Reach +Influence on Social checks while interacting with this group. The Game Master may give partial bonuses on social checks where this group is a relevant. Examples of groups that you may be affiliated with are guilds, religious orders, schools, or communities."}, 2516 {:id 400, :editable? true, :type :affiliations, :name "Family Member in the Elven Government", :properties [:individual], :benefits {:quality {:name "Reach", :value 0}, :power {:name "Influence", :value 2}}, :details "Gain +Reach +Influence on Social checks while interacting with this individual. The Game Master may give partial bonuses on social checks where this individual is a mutual acquaintance. Examples of individuals that you may be affiliated with are allies, friends, mentors, or family."}}, :worlds {1 {:active-civilization 400}}, :sections {2 {:active-tab :civilizations}}, :characters {2 {:experience -25, :actions {0 {:description "", :combinations {}, :flat-mod 0, :title "Physical Health Check", :min-pool-size 2, :ability-path [:physical :power :resilience], :target-number nil, :splinters 1, :resources [], :skill-path [:physical :quality :continuation], :dice-mod 0}, 1 {:description "", :combinations {}, :flat-mod 0, :title "Spiritual Health Check", :min-pool-size 2, :ability-path [:spiritual :power :resilience], :target-number nil, :splinters 1, :resources [], :skill-path [:spiritual :quality :continuation], :dice-mod 0}, 2 {:description "", :combinations {}, :flat-mod 0, :title "Mental Health Check", :min-pool-size 2, :ability-path [:mental :power :resilience], :target-number nil, :splinters 1, :resources [], :skill-path [:mental :quality :continuation], :dice-mod 0}, 3 {:description "", :combinations {}, :flat-mod 0, :title "Social Health Check", :min-pool-size 2, :ability-path [:social :power :resilience], :target-number nil, :splinters 1, :resources [], :skill-path [:social :quality :continuation], :dice-mod 0}, 100 {:resources (), :splinters 1, :combinations {}}}, :damage {:physical {:minor 0, :major 0}, :spiritual {:minor 0, :major 0}, :mental {:minor 0, :major 0}, :social {:minor 0, :major 0}}, :id 1, :notes "", :active-resources (), :active-action nil, :resources {2512 {:notes ""}, 2513 {:notes ""}, 2514 {:notes ""}, 2515 {:notes ""}, 2516 {:notes ""}, 310 {:notes ""}, 502 {:notes ""}, 504 {:notes ""}, 153 {:notes ""}, 155 {:notes ""}, 510 {:notes ""}, 511 {:notes ""}}, :stats {:physical {:quality {:initiation {:name "Coordination", :value 2}, :reaction {:name "Reflexes", :value 2}, :continuation {:name "Endurance", :value 2}}, :power {:dominance {:name "Might", :value 8}, :competence {:name "Finesse", :value 8}, :resilience {:name "Fortitude", :value 8}}}, :spiritual {:quality {:initiation {:name "Exertion", :value 3}, :reaction {:name "Instinct", :value 3}, :continuation {:name "Perseverance", :value 3}}, :power {:dominance {:name "Willpower", :value 8}, :competence {:name "Discipline", :value 8}, :resilience {:name "Tenacity", :value 8}}}, :mental {:quality {:initiation {:name "Concentration", :value 1}, :reaction {:name "Recognition", :value 1}, :continuation {:name "Comprehension", :value 1}}, :power {:dominance {:name "Intellect", :value 6}, :competence {:name "Focus", :value 6}, :resilience {:name "Stability", :value 6}}}, :social {:quality {:initiation {:name "Persuasion", :value 1}, :reaction {:name "Insight", :value 1}, :continuation {:name "Connections", :value 1}}, :power {:dominance {:name "Presence", :value 4}, :competence {:name "Wit", :value 4}, :resilience {:name "Poise", :value 4}}}}, :profile {:portrait "../assets/Avis.png", :name "Druid", :gender nil, :race nil, :titles nil, :description nil}}}, :active-resource nil, :resource-in-edit-mode nil}])

;; (>evt [:manual-initialize-db {:resources {156 {:benefits {:power {:value 1} :quality {:value 1}}}, 311 {:benefits {:power {:value 1}, :quality {:value 2}}}, 146 {:benefits {:quality {:value 1}}}, 6512 {:id 202, :editable? true, :type :traits, :name "Awaken the Ancient Ones", :properties [:goal], :benefits {:quality {:name "Clarity", :value 0}, :power {:name "Dedication", :value 2}}, :details "Grants +Clarity +Dedication to checks that move you towards accomplishing your goal Checks that set you back with respect to your goal gain penalties equal to the Quality and Power of your goal. Circumstances may cause multiple traits to apply bonuses or penalties to a check. Ultimately, it is up to the DM's discretion whether a trait grants a bonus or penalty. Some examples of goals are to discover forgotten magics, wealth, or to be granted knighthood."}, 6513 {:id 203, :editable? true, :type :traits, :name "Serve the Hidden God", :properties [:oath], :benefits {:quality {:name "Clarity", :value 1}, :power {:name "Dedication", :value 0}}, :details "Grants +Clarity +Dedication to checks that are aligned with your oath. Checks that are misaligned with your oath gain penalties equal to the Quality and Power of your oath. Circumstances may cause multiple traits to apply bonuses or penalties to a check. Ultimately, it is up to the DM's discretion whether a trait grants a bonus or penalty. Some examples of oaths are to uphold the virtues of Heilm, an oath of revenge, or an oath to serve the crown."}, 6514 {:id 401, :editable? true, :type :affiliations, :name "The Cult of the Hidden God", :properties [:individual], :benefits {:quality {:name "Reach", :value 0}, :power {:name "Influence", :value 1}}, :details "Gain +Reach +Influence on Social checks while interacting with this group. The Game Master may give partial bonuses on social checks where this group is a relevant. Examples of groups that you may be affiliated with are guilds, religious orders, schools, or communities."}, 6515 {:id 400, :editable? true, :type :affiliations, :name "Close Friend", :properties [:individual], :benefits {:quality {:name "Reach", :value 1}, :power {:name "Influence", :value 0}}, :details "Gain +Reach +Influence on Social checks while interacting with this individual. The Game Master may give partial bonuses on social checks where this individual is a mutual acquaintance. Examples of individuals that you may be affiliated with are allies, friends, mentors, or family."}, 6516 {:id 300 :editable? true :type :expertise :name "Thief" :properties [:training :fighting-style] :benefits {:quality {:name "Skill" :value 1} :power {:name "Equipment" :value 1}} :details "The Thief Expertise includes a set of light armor and two daggers. Gain +Skill +Equipment on checks that use these items. Additionally, gain +Skill +Equipment on checks to move stealthily and to cover your tracks and when contesting another creature's check to moving stealthily or cover their tracks."}}, :characters {6 {:experience -28, :actions {0 {:description "", :combinations {}, :flat-mod 0, :title "Physical Health Check", :min-pool-size 2, :ability-path [:physical :power :resilience], :target-number nil, :splinters 1, :resources [], :skill-path [:physical :quality :continuation], :dice-mod 0}, 1 {:description "", :combinations {}, :flat-mod 0, :title "Spiritual Health Check", :min-pool-size 2, :ability-path [:spiritual :power :resilience], :target-number nil, :splinters 1, :resources [], :skill-path [:spiritual :quality :continuation], :dice-mod 0}, 2 {:description "", :combinations {}, :flat-mod 0, :title "Mental Health Check", :min-pool-size 2, :ability-path [:mental :power :resilience], :target-number nil, :splinters 1, :resources [], :skill-path [:mental :quality :continuation], :dice-mod 0}, 3 {:description "", :combinations {}, :flat-mod 0, :title "Social Health Check", :min-pool-size 2, :ability-path [:social :power :resilience], :target-number nil, :splinters 1, :resources [], :skill-path [:social :quality :continuation], :dice-mod 0}, 100 {:resources ()}}, :damage {:physical {:minor 0, :major 0}, :spiritual {:minor 0, :major 0}, :mental {:minor 0, :major 0}, :social {:minor 0, :major 0}}, :id 1, :notes "", :active-resources (), :active-action nil, :resources {6512 {:notes ""}, 6513 {:notes ""}, 6514 {:notes ""}, 6515 {:notes ""}, 6516 {:notes ""}, 146 {:notes ""}, 500 {:notes ""}, 311 {:notes ""}, 503 {:notes ""}, 156 {:notes ""}, 509 {:notes ""}, 511 {:notes ""}}, :stats {:physical {:quality {:initiation {:name "Coordination", :value 1}, :reaction {:name "Reflexes", :value 1}, :continuation {:name "Endurance", :value 1}}, :power {:dominance {:name "Might", :value 6}, :competence {:name "Finesse", :value 6}, :resilience {:name "Fortitude", :value 6}}}, :spiritual {:quality {:initiation {:name "Exertion", :value 2}, :reaction {:name "Instinct", :value 2}, :continuation {:name "Perseverance", :value 2}}, :power {:dominance {:name "Willpower", :value 10}, :competence {:name "Discipline", :value 10}, :resilience {:name "Tenacity", :value 10}}}, :mental {:quality {:initiation {:name "Concentration", :value 1}, :reaction {:name "Recognition", :value 1}, :continuation {:name "Comprehension", :value 1}}, :power {:dominance {:name "Intellect", :value 6}, :competence {:name "Focus", :value 6}, :resilience {:name "Stability", :value 6}}}, :social {:quality {:initiation {:name "Persuasion", :value 2}, :reaction {:name "Insight", :value 2}, :continuation {:name "Connections", :value 2}}, :power {:dominance {:name "Presence", :value 8}, :competence {:name "Wit", :value 8}, :resilience {:name "Poise", :value 8}}}}, :profile {:portrait "../assets/Avis.png", :name "Warlock", :gender nil, :race nil, :titles nil, :description nil}}}, :resource-in-edit-mode nil}])
