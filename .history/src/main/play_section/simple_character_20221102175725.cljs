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

(def editing-resource?
  (r/atom false))

(def property-index
  (r/atom 0))

(def temp-resource
  (r/atom {:id 0,
           :type :items,
           :name "Resource Name",
           :properties [],
           :benefits {:quality {:name "Quality", :value 0}, :power {:name "Power", :value 0}},
           :details "Resource Details"}))

(defn resource-grid-def
  [column style-overrides]
  (merge (get [{:color :white :width "30%" :border-width 1 :border-color :white :padding 5}
               {:color :white :width "10%" :border-width 1 :border-color :white :padding 5}
               {:color :white :width "10%" :border-width 1 :border-color :white :padding 5}
               {:color :white :width "50%" :border-width 1 :border-color :white :padding 5}] column)
         style-overrides))

(defn resource-details
  [char-id resource-id action?]
  [:> rn/View {:key (helpers/new-key)
               :style {:padding 5 :background-color "#444444" :width "100%"}}
   [:> rn/Text {:key (helpers/new-key)
                :style {:color :gray}}
    (apply str (map #(clojure.string/replace (string/capitalize (name %)) #"-" " ")
                    (interpose ", " (<sub [:resource-properties resource-id]))))
    "\n"]
   [:> rn/Text {:key (helpers/new-key)
                :style {:color :white}}
    (let [quality (:value (<sub [:resource-quality resource-id]))
          power (:value (<sub [:resource-power resource-id]))]
      (str (:name (<sub [:resource-quality resource-id]))
           ": " (cond
                  (> 0 quality) (str quality "d")
                  (= 0 quality) "--"
                  (< 0 quality) (str "+" quality "d"))
           "\n"
           (:name (<sub [:resource-power resource-id]))
           ": " (cond
                  (> 0 power) power
                  (= 0 power) "--"
                  (< 0 power) (str "+" power))))]
   [:> rn/Text {:style {:color :white}}
    (str "\nDetails\n"
         (<sub [:resource-details resource-id]))]
   [:> rn/View {:style {:position :absolute :align-self :flex-end :flex-direction :row}}
    (widgets/float-button {:text-style {:margin 0 :padding 0}
                           :text (:duplicate widgets/common-icons)
                           :on-press (fn []
                                       (do
                                         (reset! temp-resource (<sub [:resource resource-id]))
                                         (reset! property-index 0)
                                         (reset! editing-resource? true)))})
    (widgets/float-button {:text-style {:margin 0 :padding 0}
                           :text (:edit widgets/common-icons)
                           :on-press #(do (let [init-vals (<sub [:resource resource-id])]
                                            (>evt [:set-resource-in-edit-mode resource-id])
                                            (reset! temp-resource init-vals)
                                            (reset! editing-resource? true)))})
    (widgets/float-button {:text-style {:margin 0 :padding 0}
                           :text (:delete widgets/common-icons)
                           :on-press (fn [] (if char-id
                                              (>evt [:delete-character-resource char-id resource-id])
                                              (>evt [:delete-resource resource-id])))})]])

(defn edit-resource-section
  [char-id resource-id]
  (let [quality-name-path [:benefits :quality :name]
        power-name-path [:benefits :power :name]]
    [:> rn/View {:border-color :white :border-width 2 :margin-top 10 :margin-bottom 10 :padding 10}
     [:> rn/View {:flex-direction :row}
      (doall (map (fn [type-val] (widgets/flat-button {:text (helpers/title-case (clojure.core/name type-val))
                                                       :active? (= type-val (:type @temp-resource))
                                                       :on-press (fn [] (swap! temp-resource #(assoc % :type type-val)))}))
                  [:equipment :traits :expertise :affiliations :items]))]
     (widgets/text-area (fn [i] (swap! temp-resource #(assoc % :name i)))
                        (:name @temp-resource)
                        "Title")
     [:> rn/View {:style {:flex-direction :row}}
      (widgets/text-area (fn [i] (swap! temp-resource #(assoc-in % quality-name-path i)))
                         (get-in @temp-resource quality-name-path)
                         "Quality Alias")
      (widgets/h-incrementor (fn [] (swap! temp-resource #(update-in % [:benefits :quality :value] dec)))
                             (or (>= -3 (get-in @temp-resource [:benefits :quality :value]))
                                 (= 0 (+ (get-in @temp-resource [:benefits :quality :value])
                                         (get-in @temp-resource [:benefits :power :value]))))
                             (fn [] (swap! temp-resource #(update-in % [:benefits :quality :value] inc)))
                             (<= 3 (get-in @temp-resource [:benefits :quality :value]))
                             [:> rn/Text {:key (.toString (random-uuid))
                                          :style {:color :white :margin 5}}
                              (let [val (get-in @temp-resource [:benefits :quality :value])]
                                (if (= val 0) "--" (str val "d")))])]
     [:> rn/View {:style {:flex-direction :row}}
      (widgets/text-area (fn [i] (swap! temp-resource #(assoc-in % power-name-path i)))
                         (get-in @temp-resource power-name-path)
                         "Power Alias")
      (widgets/h-incrementor (fn [] (swap! temp-resource #(update-in % [:benefits :power :value] dec)))
                             (or (>= -3 (get-in @temp-resource [:benefits :power :value]))
                                 (= 0 (+ (get-in @temp-resource [:benefits :quality :value])
                                         (get-in @temp-resource [:benefits :power :value]))))
                             (fn [] (swap! temp-resource #(update-in % [:benefits :power :value] inc)))
                             (<= 3 (get-in @temp-resource [:benefits :power :value]))
                             [:> rn/Text {:key (.toString (random-uuid))
                                          :style {:color :white :margin 5}}
                              (let [val (get-in @temp-resource [:benefits :power :value])]
                                (if (= val 0) "--" (str val)))])]
     (widgets/text-area (fn [i] (swap! temp-resource #(assoc % :details i)))
                        (:details @temp-resource)
                        "Details")
     [:> rn/View {:style {:flex-direction :row :align-self :flex-end}}
      (widgets/float-button {:disabled? (not (:type @temp-resource))
                             :text "Save"
                             :on-press #(let [id (if (nil? resource-id) (<sub [:new-resource-id]) resource-id)]
                                          (if (and char-id (not resource-id))
                                            (>evt [:add-character-resource char-id id]))
                                          (>evt [:set-all-resource-vals id @temp-resource])
                                          (reset! temp-resource {:id 0,
                                                                 :type :items,
                                                                 :name "Resource Name",
                                                                 :properties [],
                                                                 :benefits {:quality {:name "Quality", :value 0}, :power {:name "Power", :value 0}},
                                                                 :details "Resource Details"})
                                          (reset! editing-resource? false)
                                          (>evt [:set-resource-in-edit-mode nil]))})
      (widgets/float-button {:text "Cancel"
                             :on-press #(do (reset! temp-resource {:id 0,
                                                                   :type :items,
                                                                   :name "Resource Name",
                                                                   :properties [],
                                                                   :benefits {:quality {:name "Quality", :value 0}, :power {:name "Power", :value 0}},
                                                                   :details "Resource Details"})
                                            (reset! editing-resource? false)
                                            (>evt [:set-resource-in-edit-mode nil]))})]]))

(def create-resource-button
  (widgets/float-button {:text "+"
                         :on-press #(swap! editing-resource? not)}))

(defn resource-button
  ([resource-id]
   (let [resource-type (<sub [:resource-type resource-id])
         active? (some #(= resource-id %) (<sub [:global-active-resources resource-type]))]
     (widgets/flat-button {:style (resource-grid-def 0 {:padding 0 :margin 0})
                           :active? active?
                           :active-style {:padding 0 :margin 0}
                           :on-press #(>evt [:set-global-active-resource resource-type resource-id])
                           :text (<sub [:resource-name resource-id])})))
  ([resource-id char-id]
   (let [active? (some #(= resource-id %) (<sub [:active-resources char-id]))]
     (widgets/flat-button {:style (resource-grid-def 0 {:padding 0 :margin 0})
                           :active? active?
                           :active-style {:padding 0 :margin 0}
                           :on-press #(>evt [:set-active-resource char-id resource-id])
                           :text (<sub [:resource-name resource-id])})))
  ([resource-id char-id edit?]
   (let [active? (some #(= resource-id %) (<sub [:character-resources char-id]))]
     (widgets/flat-button {:style (resource-grid-def 0 {:padding 0 :margin 0})
                           :active? active?
                           :active-style {:padding 0 :margin 0}
                           :on-press #(if active?
                                        (>evt [:delete-character-resource char-id resource-id])
                                        (>evt [:add-character-resource char-id resource-id]))
                           :text (<sub [:resource-name resource-id])}))))

(defn active-row?
  [resource-id char-id edit?]
  (condp = (count (filter some? [resource-id char-id edit?]))
    1 (some #(= resource-id %) (<sub [:global-active-resources (<sub [:resource-type resource-id])]))
    2 (some #(= resource-id %) (<sub [:active-resources char-id]))
    3 (some #(= resource-id %) (<sub [:character-resources char-id]))))

(defn resource-row
  [resource-id resource-button-fn char-id edit?]
  (let [action-context? (not (= resource-button-fn resource-button))
        action-resource? (some #(= resource-id %) (<sub [:action-resources char-id (<sub [:active-action char-id])]))]
    [:> rn/View {:key (helpers/new-key)
                 :style {:display (if (and action-context? action-resource?) :none :flex)}}
     [:> rn/View {:style {:flex-direction :row :align-items :center :justify-content :center}}
      (apply resource-button-fn (filter some? [resource-id char-id edit?]))
      [:> rn/Text {:style (resource-grid-def 1 {:font-size 20 :text-align :center :height "100%"})}
       (helpers/formatted-dice-mod (:value (<sub [:resource-quality resource-id])))]
      [:> rn/Text {:style (resource-grid-def 2 {:font-size 20 :text-align :center :align-self :center :height "100%"})}
       (helpers/formatted-flat-mod (:value (<sub [:resource-power resource-id])))]
      [:> rn/Text {:style (resource-grid-def 3 {:text-align :center :align-self :center :height "100%"})}
       (apply str (interpose " | " (map #(helpers/title-case (name %)) (<sub [:resource-properties resource-id]))))]]
     (if (and (active-row? resource-id char-id edit?) (not action-context?))
       (resource-details char-id resource-id action-context?))]))

(defn resource-section
  [{:keys [section-title resource-button-fn char-id edit?]}]
  (let [resources (sort-by #(<sub [:resource-name %])
                           (if (or (not (or char-id edit?)) (and char-id edit?))
                             (<sub [(keyword section-title)])
                             (<sub [(keyword (str "character-" section-title)) char-id])))
        resources? (not-empty resources)]
    [:> rn/View {:style {:width "100%" :border-width 1 :border-color :white :display (if resources? :flex :none)}}
     [:> rn/View {:style {:flex-direction :row :background-color "#333333"}}
      [:> rn/Text {:style {:font-size 24 :color :white :padding 10}} (helpers/title-case section-title)]]
     (doall (map resource-row resources (repeat resource-button-fn) (repeat char-id) (repeat edit?)))]))

(defn character-experience
  [char-id]
  [:> rn/Text {:style {:left 300 :text-align :left :flex 5 :color :white}}
   "Experience: " (<sub [:experience char-id])])

(defn resources
  [char-id resource-button-fn edit?]
  [:> rn/View {:style {:padding 15 :width "100%"}}
   [:> rn/View {:style {:flex-direction :row}}
    [:> rn/Text {:style {:color :white :font-size 28 :padding-right 30}} "Resources"]
    (if (and (not @editing-resource?) (= resource-button resource-button-fn))
      create-resource-button)
    helpers/spacer
    (if char-id
      [:> rn/View {:top 15 :right 300} (character-experience char-id)])]
   (if @editing-resource?
     (edit-resource-section char-id (<sub [:resource-in-edit-mode])))
   [:> rn/View {:style {:flex-direction :row}}
    (let [header-fn (fn [column text]
                      [:> rn/Text {:key (helpers/new-key)
                                   :style (resource-grid-def column {:font-size 16 :text-align :center})} text])]
      (doall (map #(apply header-fn %) [[0 "Title"] [1 "Quality"] [2 "Power"] [3 "Properties"]])))]
   (resource-section {:char-id char-id :section-title "equipment" :resource-button-fn resource-button-fn :edit? edit?})
   (resource-section {:char-id char-id :section-title "traits" :resource-button-fn resource-button-fn :edit? edit?})
   (resource-section {:char-id char-id :section-title "expertise" :resource-button-fn resource-button-fn :edit? edit?})
   (resource-section {:char-id char-id :section-title "affiliations" :resource-button-fn resource-button-fn :edit? edit?})
   (resource-section {:char-id char-id :section-title "items" :resource-button-fn resource-button-fn :edit? edit?})])

(defn resource
  [char-id domain resource-id]
  (let [resource-name     (<sub [:resource-name resource-id])
        resource-quality  (:value (<sub [:resource-quality resource-id]))
        resource-power    (:value (<sub [:resource-power resource-id]))
        resource-limited? {:inc-quality (<= 3 resource-quality)
                           :dec-quality (>= -3 resource-quality)
                           :inc-power (<= 3 resource-power)
                           :dec-power (>= -3 resource-power)}]
    [:> rn/View
     (widgets/flat-button {:key helpers/new-key
                           :style {:height 90 :width 175 :flex-direction :column :border-width 1 :border-color :white}
                           :on-press #(>evt [:set-active-resource-map char-id domain resource-id])
                           :active? (= resource-id (<sub [:active-resource char-id domain]))
                           :text [:> rn/View {:style {:flex-direction :row}}
                                  [:> rn/View {:style {:flex-direction :column :padding 0}}
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
                                  [:> rn/View {:style {:justify-content :center :max-width 100 :flex-direction :column :margin-left 5 :margin-right 5}}
                                   [:> rn/Text {:style {:font-size 12 :color :white :text-align :center}} resource-name]
                                   [:> rn/Text {:style {:font-size 18 :color :white :text-align :center}} (helpers/formatted-skill-bonus resource-quality resource-power)]]
                                  [:> rn/View {:style {:flex-direction :column}}
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
   [:> rn/ScrollView {:style {:horizontal true  :overflow :scroll}
                      :content-container-style {:horizontal true :height 95 :width 360 :margin-top -1 :overflow :scroll}} 
    (doall (map resource (repeat char-id) (repeat domain) (<sub [(resource-subs domain) char-id])))]])

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
              (if (<sub [:edit-mode]) (resources char-id resource-button true))
              (main-play-section/character-notes char-id)]
             ["Details" 
              "Stats & Resources" 
              (if (<sub [:edit-mode]) "Resources Edit")
              "Notes"]
             [main-play-section/character-details-toggle 
              main-play-section/character-stats-toggle 
              (if (<sub [:edit-mode]) main-play-section/character-resources-toggle)
              main-play-section/character-notes-toggle])))

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
  (resources nil resource-button nil))

(defn play-section
  []
  (case (<sub [:active-tab 1])
    :characters (characters-tab)
    :resources (resources-tab)
    :else [:> rn/Text {:style {:color :white}} "Invalid Tab"]))