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
  (let [resource-name    (<sub [:resource-name resource-id])
        resource-quality (:value (<sub [:resource-quality resource-id]))
        resource-power   (:value (<sub [:resource-power resource-id]))]
    [:> rn/View
     (widgets/flat-button {:key helpers/new-key
                           :style {:height 90 :width 175 :flex-direction :column :padding 5 :border-width 1 :border-color :white}
                           :on-press #(>evt [:set-active-resource char-id resource-id])
                           :active? (some #(= resource-id %) (<sub [:active-resources char-id]))
                           :text [:> rn/View {:style {:flex-direction :row}}
                                  [:> rn/View {:style {:flex-direction :column :padding 0}}
                                   (widgets/flat-button {:style {:display (if (<sub [:edit-mode]) "flex" "none") :width 25 :height 25 :padding 0 :justify-content :center}
                                                         :on-press #(do
                                                                      (>evt [:update-resource-quality-val resource-id +])
                                                                      (>evt [:update-exp char-id dec]))
                                                         :text-style {:font-size 10 :height 25 :top 7}
                                                         :disabled? (> 1 (<sub [:experience char-id]))
                                                         :text (:up-sm widgets/common-icons)})
                                   (widgets/flat-button {:style {:display (if (<sub [:edit-mode]) "flex" "none") :width 25 :height 25 :padding 0}
                                                         :on-press #(do
                                                                      (>evt [:update-resource-quality-val resource-id inc])
                                                                      (>evt [:update-exp char-id inc]))
                                                         :text-style {:font-size 10 :height 25 :top 7}
                                                         :disabled? (> 2 resource-quality)
                                                         :text (:down-sm widgets/common-icons)})]
                                  [:> rn/View {:style {:max-width 100 :flex-direction :column :margin-left 5 :margin-right 5}}
                                   [:> rn/Text {:style {:font-size 12 :color :white :text-align :center :allow-font-scaling true}} resource-name]
                                   [:> rn/Text {:style {:font-size 18 :color :white :text-align :center}} (helpers/formatted-skill-bonus resource-quality resource-power)]]
                                  [:> rn/View {:style {:flex-direction :column}}
                                   (widgets/flat-button {:style {:display (if (<sub [:edit-mode]) "flex" "none") :width 25 :height 25 :padding 0}
                                                         :on-press #(do
                                                                      (>evt [:update-resource-power-val resource-id +])
                                                                      (>evt [:update-exp char-id dec]))
                                                         :disabled? (> 1 (<sub [:experience char-id]))
                                                         :text-style {:font-size 10 :height 25 :top 7}
                                                         :text (:up-sm widgets/common-icons)})
                                   (widgets/flat-button {:style {:display (if (<sub [:edit-mode]) "flex" "none") :width 25 :height 25 :padding 0}
                                                         :on-press #(do
                                                                      (>evt [:update-resource-power-val resource-id +])
                                                                      (>evt [:update-exp char-id inc]))
                                                         :disabled? (> 2 resource-power)
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
    [:> rn/Text {:key (helpers/new-key)
                 :style {:color :white}}
     (<sub [:char-minor-wounds char-id domain])])
   (widgets/incrementor-alt
    #(>evt [:inflict-major-wound char-id domain])
    false
    #(>evt [:heal-major-wound char-id domain])
    (>= 0 (<sub [:char-major-wounds char-id domain]))
    [:> rn/Text {:key (helpers/new-key)
                 :style {:color :white}}
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
    (widgets/flat-button {:key helpers/new-key
                          :style {:height 90 :width 175 :flex-direction :column :padding 5 :border-width 1 :border-color :white}
                          :on-press #(doall (>evt [:set-active-skill char-id domain])
                                            (>evt [:set-active-ability char-id domain]))
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
                                  [:> rn/Text {:style {:font-size 12 :color :white :text-align :center}}stat-name]
                                  [:> rn/Text {:style {:font-size 18 :color :white :text-align :center}} (str skill-value "d" ability-value)]]
                                 [:> rn/View {:style {:flex-direction :column}}
                                  (widgets/flat-button {:style {:display (if (<sub [:edit-mode]) "flex" "none") :width 25 :height 25 :padding 0}
                                                        :on-press (fn []
                                                                    (doall
                                                                     (map #(do (>evt [:increase-ability char-id %]) (>evt [:update-exp char-id dec])) ability-paths)))
                                                        :disabled? (> 1 (<sub [:experience char-id]))
                                                        :text-style {:font-size 10 :height 25 :top 7}
                                                        :text (:up-sm widgets/common-icons)})
                                  (widgets/flat-button {:style {:display (if (<sub [:edit-mode]) "flex" "none") :width 25 :height 25 :padding 0}
                                                        :on-press (fn []
                                                                    (doall (map #(do (>evt [:decrease-ability char-id %]) (>evt [:update-exp char-id inc]))
                                                                                ability-paths)))
                                                        :disabled? (> 3 ability-value)
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
  [:> rn/View {:key (helpers/new-key)
               :style {:flex-direction :row :align-items :center}}
   (domain domain-icons)
   (stat-fn char-id domain)
   (if damage?
     (damage-tracker char-id domain)
     [:> rn/View {:width 100}])
   (doall (map resource (repeat char-id) (<sub [(resource-subs domain) char-id])))])

(defn character-stats
  [char-id]
  [:> rn/View {:style {:width "100%" :padding-bottom 15}}
   [:> rn/Text {:style {:color :white :align-self :center :font-size 24}} "Stats"]
   [:> rn/View {:style {:flex-direction :row}}
    [:> rn/Text {:style {:align-self :flex-end :right 225 :color :white :padding-right 8}} "Damage"]]
   (doall (map simple-stat-row
               (repeat char-id)
               [:physical :spiritual :mental :social]
               (repeat simple-stat)
               (repeat true)))])

(defn character-data
  [char-id]
  [:> rn/View
   (character-stats char-id)])

(defn character-details
  [char-id]
  (into [:> rn/View {:style {:width "95%"}}]
        (map widgets/card
             [(character-info char-id)
              (character-data char-id)
              (if (<sub [:edit-mode]) (main-play-section/resources char-id main-play-section/resource-button true))]
             ["Details" "Stats & Resources" (if (<sub [:edit-mode]) "Resources Edit")]
             [main-play-section/character-details-toggle main-play-section/character-stats-toggle (if (<sub [:edit-mode]) main-play-section/character-resources-toggle)])))

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
       (widgets/text-submission
        (fn [i] (>evt [:manual-initialize-db (edn/read-string (str i))]))
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