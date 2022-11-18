(ns main.play-section.views 
  (:require
   [clojure.edn :as edn :refer [read-string]]
   [clojure.core.async :as Async]
   [clojure.string :as string]
   [reagent.core :as r]
   ["react-native" :as rn]
   [main.helpers :as helpers :refer [<sub >evt]]
   [main.widgets :as widgets :refer [domain-icons]]))

(<sub [:full-db])

;;;;;;;;;;;;;;;;;;;
;; Resources Tab ;;
;;;;;;;;;;;;;;;;;;;

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
                                        (do (>evt [:delete-character-resource char-id resource-id])
                                            (>evt [:remove-action-resource char-id 100 resource-id]))
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

;;;;;;;;;;;;;;;;;;;
;; Character Tab ;;
;;;;;;;;;;;;;;;;;;;

(def character-details-toggle
  (r/atom true))

(def character-stats-toggle
  (r/atom true))

(def character-resources-toggle
  (r/atom true))

(def character-actions-toggle
  (r/atom true))

(def character-notes-toggle
  (r/atom true))

(defn reset-cards-state
  []
  (doall (map #(reset! % true) [character-details-toggle character-stats-toggle character-resources-toggle character-actions-toggle character-notes-toggle])))

(defn character-select-button
  [character-id]
  (widgets/flat-button {:text-style {:font-size 24}
                        :active-text-style {:font-size 24}
                        :on-press #(do (reset-cards-state)
                                       (>evt [:set-active-character character-id]))
                        :active? (= (<sub [:active-character]) character-id)
                        :text (<sub [:character-name character-id])}))

(def character-add-button
  (widgets/float-button {:style {:position :absolute :right 15 :top 15 :width 50 :height 50 :border-radius 100}
                         :on-press #(>evt [:new-character {} true])
                         :text (:create widgets/common-icons)}))

(def confirm-delete (r/atom false))

(def character-init-delete-button
  (widgets/float-button {:style {:position :absolute :right 15 :top 15 :width 50 :height 50 :border-radius 100}
                         :on-press #(do (Async/go
                                          (reset! confirm-delete true)
                                          (Async/<! (Async/timeout 3000))
                                          (reset! confirm-delete false)))
                         :text (:delete widgets/common-icons)}))

(defn character-delete-confirm-button
  []
  (widgets/float-button {:style {:background-color :red :position :absolute :right 15 :top 15 :width 50 :height 50 :border-radius 100 :z-index 2}
                         :on-press #(do (>evt [:delete-character (<sub [:active-character])])
                                        (>evt [:set-active-character nil])
                                        (reset! confirm-delete false)
                                        (>evt [:set-edit-mode false]))
                         :text (:delete widgets/common-icons)}))

(defn character-edit-button
  []
  (widgets/float-button {:style {:position :absolute :right 80 :top 15 :width 50 :height 50 :border-radius 100 :padding 0}
                         :on-press #(>evt [:toggle-edit-mode (<sub [:active-character])])
                         :text (if (<sub [:edit-mode])
                                 (:view widgets/common-icons)
                                 (:edit widgets/common-icons))}))

(defn character-buttons
  []
  [:> rn/View {:style {:flex 1 :flex-direction "row" :flex-wrap :wrap :width "100%" :margin-top 15 :margin-bottom 15 :justify-content :center}}
   (if (nil? (<sub [:active-character]))
     (doall (map character-select-button (<sub [:characters])))
     (character-select-button (<sub [:active-character])))])



(defn character-portrait
  [char-id]
  (js/require (<sub [:character-portrait char-id])))

(defn profile-line-edit
  [char-id label]
  [:> rn/View {:key (.toString (random-uuid)) :style {:flex-direction :row}}
   [:> rn/Text {:key (.toString (random-uuid)) :style {:color :white :font-size 18}} (str label ": ")]
   [:> rn/TextInput {:key (.toString (random-uuid)) 
                     :style {:background-color :white :padding-left 10 :padding-right 10 :margin-top 3 :margin-botton 3}
                     :on-change-text #(>evt [:update-character-profile char-id (keyword (string/lower-case label)) (str %)])}
    ((keyword (string/lower-case label)) (<sub [:character-profile char-id]))]])

(defn character-profile-edit
  [char-id]
  [:> rn/View {:style {:margin-bottom 15}}
   (doall (map (fn [label] (widgets/text-area #(>evt [:update-character-profile char-id (keyword (string/lower-case label)) (str %)])
                                              ((keyword (string/lower-case label)) (<sub [:character-profile char-id]))
                                              label))
               ["Gender" "Race" "Titles" "Description"]))])

(defn name-edit
  [char-id]
  [:> rn/TextInput {:style {:font-size 24 :background-color :white :padding 6 :margin-top 30 :margin-bottom 30}
                    :on-change-text #(>evt [:update-character-profile char-id :name (str %)])}
   (:name (<sub [:character-profile char-id]))])

(defn character-profile
  [char-id]
  [:> rn/View {:style {:justify-content :center :flex-direction :row :margin-bottom 15}}
  ;;  [:> rn/Image {:source (character-portrait char-id) :style {:width 100 :height 100}}]
   (let [{:keys [gender race titles description]} (<sub [:character-profile char-id])]
     [:> rn/Text {:style {:color :white}}
      (str
       (if (and (not-empty gender) (not-empty race)) (str "The " gender " "))
       (if (and (not-empty gender) (empty? race)) gender)
       (if (not-empty race) (str race "\n"))
       (if (not-empty titles) (str titles "\n"))
       (if (not-empty description) description))])])




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
                          :style {:height 100 :width 175 :flex-direction :column :justify-content "center"
                                  :align-items "center" :padding 5 :border-width 1 :border-color :white}
                          :on-press #(doall (>evt [:set-active-skill char-id domain])
                                            (>evt [:set-active-ability char-id domain]))
                          :active? (and (= domain (<sub [:character-active-skill char-id]))
                                        (= domain (<sub [:character-active-ability char-id])))
                          :text [:> rn/View {:style {:flex-direction :row}}
                                 [:> rn/View {:style {:flex-direction :column :padding 0}}
                                  (widgets/flat-button {:style {:display (if (<sub [:edit-mode]) "flex" "none") :width 25 :height 25 :padding 0}
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
                                 [:> rn/View {:style {:flex-direction :column :justify-content "center" :align-items "center" :margin-left 5 :margin-right 5}}
                                  [:> rn/Text {:style {:font-size 12 :color :white}} stat-name]
                                  [:> rn/Text {:style {:font-size 18 :color :white}} (str skill-value "d" ability-value)]]
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

(defn moderate-stat
  [char-id skill-path ability-path]
  (let [skill-name (:name (<sub [:character-stat char-id skill-path]))
        skill-value (:value (<sub [:character-stat char-id skill-path]))
        ability-value (:value (<sub [:character-stat char-id ability-path]))]
    (widgets/flat-button {:on-press #(doall (>evt [:set-active-skill char-id skill-path])
                                            (>evt [:set-active-ability char-id ability-path]))
                          :style {:height 100 :width 175 :flex-direction :column :justify-content "center" :align-items "center"
                                  :padding 5 :border-width 1 :border-color :white}
                          :active? (and (= skill-path (<sub [:character-active-skill char-id]))
                                        (= ability-path (<sub [:character-active-ability char-id])))
                          :text [:> rn/View {:style {:flex-direction :row}}
                                 [:> rn/View {:style {:flex-direction :column :padding 0}}
                                  (widgets/flat-button {:style {:display (if (<sub [:edit-mode]) "flex" "none") :width 25 :height 25 :padding 0}
                                                        :on-press #(do (>evt [:increase-skill char-id skill-path])
                                                                       (>evt [:update-exp char-id dec]))
                                                        :text-style {:font-size 10 :height 25 :top 7}
                                                        :disabled? (> 1 (<sub [:experience char-id]))
                                                        :text (:up-sm widgets/common-icons)})
                                  (widgets/flat-button {:style {:display (if (<sub [:edit-mode]) "flex" "none") :width 25 :height 25 :padding 0}
                                                        :on-press #(do (>evt [:decrease-skill char-id skill-path])
                                                                       (>evt [:update-exp char-id inc]))
                                                        :text-style {:font-size 10 :height 25 :top 7}
                                                        :disabled? (> 2 (:value (<sub [:character-stat char-id skill-path])))
                                                        :text (:down-sm widgets/common-icons)})]
                                 [:> rn/View {:style {:flex-direction :column :justify-content "center" :align-items "center" :margin-left 5 :margin-right 5}}
                                  [:> rn/Text {:style {:font-size 12 :color :white}} skill-name]
                                  [:> rn/Text {:style {:font-size 18 :color :white}} (str skill-value "d" ability-value)]]
                                 [:> rn/View {:style {:flex-direction :column}}
                                  (widgets/flat-button {:style {:display (if (<sub [:edit-mode]) "flex" "none") :width 25 :height 25 :padding 0}
                                                        :on-press #(do (>evt [:increase-ability char-id ability-path])
                                                                       (>evt [:update-exp char-id dec]))
                                                        :disabled? (> 1 (<sub [:experience char-id]))
                                                        :text-style {:font-size 10 :height 25 :top 7}
                                                        :text (:up-sm widgets/common-icons)})
                                  (widgets/flat-button {:style {:display (if (<sub [:edit-mode]) "flex" "none") :width 25 :height 25 :padding 0}
                                                        :on-press #(do (>evt [:decrease-ability char-id ability-path])
                                                                       (>evt [:update-exp char-id inc]))
                                                        :disabled? (> 3 (:value (<sub [:character-stat char-id ability-path])))
                                                        :text-style {:font-size 10 :height 25 :top 7}
                                                        :text (:down-sm widgets/common-icons)})]]})))

(defn complex-stat
  [char-id stat-path])

(defn simple-action-stat
  [char-id domain]
  (let [action-id (<sub [:active-action char-id])
        stat-name (string/capitalize (name domain))
        skill-paths [[domain :quality :initiation]
                     [domain :quality :reaction]
                     [domain :quality :continuation]]
        ability-paths [[domain :power :dominance]
                       [domain :power :competence]
                       [domain :power :resilience]]
        skill-value (int (/ (apply + (map #(:value (<sub [:character-stat char-id %])) skill-paths)) 3))
        ability-value (int (/ (apply + (map #(:value (<sub [:character-stat char-id %])) ability-paths)) 3))]
    (widgets/flat-button {:on-press #(>evt [:set-simple-action-stat-paths char-id action-id domain])
                          :style {:height 75 :width 150 :flex-direction :column :justify-content "center" :align-items "center"
                                  :padding 5 :border-width 1 :border-color :white}
                          :active? (= domain (<sub [:simple-action-stat-path char-id action-id]))
                          :text [:> rn/View {:style {:flex-direction :row}}
                                 [:> rn/View {:style {:flex-direction :column :justify-content "center" :align-items "center" :margin-left 5 :margin-right 5}}
                                  [:> rn/Text {:style {:font-size 14 :color :white}} stat-name]
                                  [:> rn/Text {:style {:font-size 18 :color :white}} (str skill-value "d" ability-value)]]]})))

(defn moderate-action-stat
  [char-id skill-path ability-path]
  (let [action-id (<sub [:active-action char-id])
        skill-name (:name (<sub [:character-stat char-id skill-path]))
        skill-value (:value (<sub [:character-stat char-id skill-path]))
        ability-value (:value (<sub [:character-stat char-id ability-path]))]
    (widgets/flat-button {:on-press #(>evt [:set-action-stat-paths char-id action-id skill-path ability-path])
                          :style {:height 75 :width 150 :flex-direction :column :justify-content "center" :align-items "center"
                                  :padding 5 :border-width 1 :border-color :white}
                          :active? (and (= skill-path (<sub [:action-skill-path char-id action-id]))
                                        (= ability-path (<sub [:action-ability-path char-id action-id])))
                          :text [:> rn/View {:style {:flex-direction :row}}
                                 [:> rn/View {:style {:flex-direction :column :justify-content "center" :align-items "center" :margin-left 5 :margin-right 5}}
                                  [:> rn/Text {:style {:font-size 14 :color :white}} skill-name]
                                  [:> rn/Text {:style {:font-size 18 :color :white}} (str skill-value "d" ability-value)]]]})))

(defn damage-tracker
  [char-id domain]
  [:> rn/View {:style {:flex-direction :row :width 150 :height 100 :align-items :center :justify-content :center :border-width 1 :border-color :white :padding-top 10 :margin-left 1}}
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

(defn stat-row
  [char-id domain stat-fn damage?]
  [:> rn/View {:key (helpers/new-key)
               :style {:flex-direction :row :align-items "center" :justify-content "center"}}
   (domain domain-icons)
   (doall (map stat-fn
               (repeat char-id)
               [[domain :quality :initiation]
                [domain :quality :reaction]
                [domain :quality :continuation]]
               [[domain :power :dominance]
                [domain :power :competence]
                [domain :power :resilience]]))
   (if damage?
     (damage-tracker char-id domain)
     [:> rn/View {:width 100}])])

(defn simple-stat-row
  [char-id domain stat-fn damage?]
  [:> rn/View {:key (helpers/new-key)
               :style {:flex-direction :row :align-items "center" :justify-content "center"}}
   (domain domain-icons)
   (stat-fn char-id domain)
   (if damage?
     (damage-tracker char-id domain)
     [:> rn/View {:width 100}])])

(defn character-stats
  [char-id stat-complexity]
  [:> rn/View {:style {:width "100%" :align-items "center" :justify-content "center" :padding-bottom 15}}
   [:> rn/Text {:style {:color :white :font-size 24}} "Stats"]
   [:> rn/View {:style {:flex-direction :row}}
    (character-experience char-id)
    [:> rn/Text {:style {:align-self :flex-end :right 225 :color :white :padding-right 8}} "Damage"]]
   (doall (map simple-stat-row
               (repeat char-id)
               [:physical :spiritual :mental :social]
               (repeat simple-stat)
               (repeat true)))])



(defn action-base-dice-pool-tab
  [char-id]
  [:> rn/View {:style {:left "4%" :align-items :center :justify-content :center :padding-bottom 15}}
   (doall
    (map simple-stat-row
         (repeat char-id)
         [:physical :spiritual :mental :social]
         (repeat simple-action-stat)
         (repeat false)))])

(defn action-resource-details
  [resource-id char-id]
  (let [action-id (<sub [:active-action char-id])
        resource-quality (:value (<sub [:resource-quality resource-id]))
        resource-power (:value (<sub [:resource-power resource-id]))
        resource-data (helpers/formatted-skill-bonus resource-quality resource-power)]
    [:> rn/View {:key (helpers/new-key)
                 :style {:align-items :center :flex-direction :row}}
     (widgets/flat-button {:style {:align-items :center :justify-content :center :flex-shrink 1}
                           :active? true
                           :active-text-style {:font-size 14}
                           :on-press #(doall [(>evt [:remove-action-resource char-id action-id resource-id])
                                              (>evt [:reset-splinters char-id action-id])])
                           :text (<sub [:resource-name resource-id])})
     [:> rn/Text {:style {:color :white :font-size 18 :padding-left 10 :flex-shrink 1}} resource-data]]))

(defn resource-action-button
  [resource-id char-id]
  (let [action-id (<sub [:active-action char-id])
        active? (some #(= resource-id %) (<sub [:action-resources char-id action-id]))]
    (widgets/flat-button {:style (resource-grid-def 0 {:padding 0 :margin 0})
                          :on-press #(doall [(>evt [:add-action-resource char-id action-id resource-id])
                                             (>evt [:reset-splinters char-id action-id])])
                          :text (<sub [:resource-name resource-id])})))

(defn action-mods-tab
  [char-id action-id]
  (let [action-resources (<sub [:action-resources char-id action-id])]
    [:> rn/View {:style {:flex-direction :column}}
     [:> rn/View {:style {:flex-direction :row}}
      [:> rn/View {:style {:flex 1 :flex-direction :column :border-width 1 :border-color :white}}
       [:> rn/Text {:style {:padding 10 :color :white :font-size 18 :justify-content :center :text-align :center}} "Resource Modifiers"]
       (doall (map action-resource-details action-resources (repeat char-id)))]
      [:> rn/View {:style {:flex-direction :column :align-items :center :border-width 1 :border-color :white}}
       [:> rn/Text {:style {:padding 10 :color :white :font-size 18 :justify-content :center :text-align :center}} "Circumstantial Modifiers"]
       [:> rn/View {:style {:flex-direction :row :align-items :center :justify-content :center :padding 10}}
        (widgets/incrementor
         #(doall [(>evt [:update-action-dice-mod char-id action-id inc])
                  (>evt [:reset-splinters char-id action-id])])
         false
         #(doall [(>evt [:update-action-dice-mod char-id action-id dec])
                  (>evt [:reset-splinters char-id action-id])])
         false)

        [:> rn/Text {:style {:text-align :center :flex 3 :font-size 24 :color :white}}
         (let [formatted-roll (helpers/formatted-skill-bonus (<sub [:action-dice-mod char-id action-id])
                                                             (<sub [:action-flat-mod char-id action-id]))]
           (if (empty? formatted-roll)
             "--"
             formatted-roll))]

        (widgets/incrementor
         #(>evt [:update-action-flat-mod char-id action-id inc])
         false
         #(>evt [:update-action-flat-mod char-id action-id dec])
         false)]
       (widgets/flat-button {:on-press #(>evt [:reset-dice-and-flat-mods char-id action-id])
                             :text-style {:font-size 14}
                             :text "Reset"})]
      [:> rn/View {:style {:flex 1 :flex-direction :column :align-items :center :border-width 1 :border-color :white}}
       [:> rn/Text {:style {:padding 10 :color :white :font-size 18}}
        "Total Modifiers"]
       [:> rn/Text {:style {:color :white :font-size 24}}
        (if (or (not-empty action-resources)
                (not (= 0 (<sub [:action-dice-mod char-id action-id])))
                (not (= 0 (<sub [:action-flat-mod char-id action-id]))))
          (helpers/formatted-skill-bonus
           (apply + (conj (map #(:value (<sub [:resource-quality %])) action-resources)
                          (<sub [:action-dice-mod char-id action-id])))
           (apply + (conj (map #(:value (<sub [:resource-power %])) action-resources)
                          (<sub [:action-flat-mod char-id action-id])))))]]]
     [:> rn/Text {:style {:padding-top 20 :align-self :center :color :white :font-size 24}}
      "Select resources to add to your roll"]
     (resources char-id resource-action-button nil)]))

(defn pool-display
  [splinter]
  [:> rn/View {:key (helpers/new-key)
               :style {:border-width 1 :border-color :white :margin 5}}
   [:> rn/Text {:style {:color :white :text-align :center :margin 10}} (:result (apply helpers/formatted-roll splinter))]])

(defn action-splintering-tab
  [char-id action-id]
  (let [main-dice-pool (<sub [:simple-action-roll-value char-id action-id 1 {}])
        splinters (helpers/splinter
                   (<sub [:action-splinters char-id action-id])
                   [(:dice-number main-dice-pool) (:dice-size main-dice-pool) (:dice-bonus main-dice-pool)])]
    [:> rn/View {:style {:align-items :center :justify-content :center :padding-top 20}}
     [:> rn/Text {:style {:color :white :font-size 24}} (str "Full Dice Pool: " (:result main-dice-pool))]
     [:> rn/View {:style {:flex-direction :row :align-items :center :justify-content :center :margin 10}}
      (widgets/flat-button {:style {:height 40 :width 40}
                            :text-style {:bottom 3}
                            :text "-"
                            :on-press #(doall [(>evt [:update-splinters char-id action-id dec])
                                               (>evt [:reset-combinations char-id action-id])])
                            :disabled? (= 1 (<sub [:action-splinters char-id action-id]))})
      [:> rn/View {:style {:align-items :center :justify-content :center :max-width "85%" :flex-direction :row :flex-wrap :wrap}}
       (map pool-display splinters)]
      (widgets/flat-button {:style {:height 40 :width 40}
                            :text-style {:bottom 3}
                            :text "+"
                            :on-press #(doall [(>evt [:update-splinters char-id action-id inc])
                                               (>evt [:reset-combinations char-id action-id])])
                            :disabled? (> (<sub [:action-min-pool-size char-id action-id])
                                          (apply min (helpers/splinter-math (:dice-number main-dice-pool) (inc (<sub [:action-splinters char-id action-id])))))})]
     (widgets/flat-button {:on-press #(>evt [:reset-splinters char-id action-id])
                           :text-style {:font-size 14}
                           :text "Reset"})]))

(defn action-combining-tab
  [char-id action-id]
  (let [main-dice-pool (<sub [:simple-action-roll-value char-id action-id 1 {}])
        splinters (helpers/splinter
                   (<sub [:action-splinters char-id action-id])
                   [(:dice-number main-dice-pool) (:dice-size main-dice-pool) (:dice-bonus main-dice-pool)])
        dice-pool (fn [i [dice-number dice-size dice-bonus]]
                    (let [combs (get (<sub [:action-combinations char-id action-id]) i)]
                      (widgets/incrementor
                       #(>evt [:update-action-combinations char-id action-id i inc])
                       (> (* 2 (inc combs))
                          dice-number)
                       #(>evt [:update-action-combinations char-id action-id i dec])
                       (or (= 2 dice-size)
                           (< dice-number (inc (helpers/absolute-value combs))))
                       [:> rn/View {:key (helpers/new-key)
                                    :style {:border-width 1 :border-color :white :margin 5}}
                        [:> rn/Text {:style {:color :white :text-align :center :margin 10}} (interpose " + " (map :result (helpers/formatted-roll-with-combs dice-number dice-size dice-bonus combs)))]])))]
    [:> rn/View {:key (helpers/new-key)
                 :style {:align-items :center :justify-content :center}}
     [:> rn/View {:style {:align-items :center :justify-content :center :padding-top 20}}
      [:> rn/Text {:style {:color :white :font-size 24}} (str "Full Dice Pool: " (:result main-dice-pool))]
      [:> rn/View {:style {:padding 10 :flex-direction :row :align-items :center :justify-content :center}}
       (doall (map-indexed dice-pool splinters))]
      (widgets/flat-button {:on-press #(>evt [:reset-combinations char-id action-id])
                            :text-style {:font-size 14}
                            :text "Reset"})]]))

(def active-action-tab (r/atom 0))

(def show-more-results? (r/atom false))

(defn dice-roll-display
  [{:keys [roll bonus roll-result]}]
  [:> rn/View {:key (helpers/new-key) :flex-direction :row}
   [:> rn/Text {:style {:color :white}} "Active Result: "]
   (doall (map #(conj [:> rn/Text {:key (helpers/new-key)
                                   :style {:color :white}}] (str % " ")) roll))
   [:> rn/Text {:style {:color :white}} (cond
                                          (> 0 bonus) (str roll-result " " bonus " ")
                                          (= 0 bonus) roll-result
                                          (< 0 bonus) (str roll-result " +" bonus " "))]
   [:> rn/Text {:style {:color :white}} (str "-> " roll-result)]])

(defn roll-results
  [char-id action-id]
  [:> rn/View {:key (helpers/new-key)
               :style {:padding 20 :align-items :center :justify-content :center}}
   (widgets/header-format {:font-size 24 :color :white} "Roll Results")
   (if (> 2 @active-action-tab)
     [:> rn/Text {:style {:color :white}}
      "Passive Result: " (helpers/passive-result (<sub [:simple-action-roll-value char-id action-id @active-action-tab {}]))])
   [:> rn/View {:style {:flex-direction :row :display (if (empty? (<sub [:roll-results-by-action char-id action-id])) :none :flex)}}
    (widgets/button {:style {:position :absolute :align-items :center :top 4 :left -20 :width 20 :height 20}
                     :text-style {:color :white}
                     :on-press #(swap! show-more-results? not)
                     :text (if @show-more-results?
                             (:up-sm widgets/common-icons)
                             (:down-sm widgets/common-icons))})
    [:> rn/View {:key (helpers/new-key)
                 :style {:flex-direction :column}}
     (reverse (take-last (if @show-more-results? 10 1) (doall (map dice-roll-display (<sub [:roll-results-by-action char-id action-id])))))]]])

(defn dice-rolling-section
  [char-id action-id]
  [:> rn/View
   [:> rn/View {:style {:flex-wrap :wrap :justify-content :center :flex-direction :row}}
    (doall (map #(apply widgets/dice-pool-button (concat [char-id] %))
                (<sub [:simple-action-roll-value-buttons char-id action-id 1 {}])))]
   (roll-results char-id action-id)])

(defn action
  [char-id action-id]
  [:> rn/View {:style {:align-items :center}}
   [:> rn/View {:style {:flex-wrap :wrap :justify-content :center :flex-direction :row}}
    (doall (map #(apply widgets/dice-pool-button (concat [char-id] %))
                (<sub [:simple-action-roll-value-buttons char-id action-id @active-action-tab {}])))]
   (roll-results char-id action-id)
   (widgets/tab-navigation {}
                           ["Base Dice Pool" "Benefits & Detriments" 
                            ;; "Complexity" "Careful or Reckless"
                            ]
                           [(action-base-dice-pool-tab char-id)
                            (action-mods-tab char-id action-id)
                            ;; (action-splintering-tab char-id action-id)
                            ;; (action-combining-tab char-id action-id)
                            ]
                           active-action-tab)])

(defn character-action-button
  [char-id action-id]
  (widgets/flat-button {:active? (= (<sub [:active-action char-id]) action-id)
                        :on-press #(>evt [:set-active-action char-id action-id])
                        :text (<sub [:action-title char-id action-id])}))

(defn character-action-edit-button
  [char-id action-id]
  [:> rn/TextInput {:style {:font-size 24 :background-color :white :padding 6 :margin-top 30 :margin-bottom 30}
                    :on-change-text #(>evt [:edit-action-title char-id action-id (str %)])}
   (<sub [:action-title char-id action-id])])

(defn character-action-add-button
  [char-id]
  (widgets/float-button {:style {:padding 0 :margin-left 10 :height 30 :width 30}
                         :text-style {:padding 0 :margin 0 :font-size 12 :width 30}
                         :on-press #(>evt [:new-character-action char-id])
                         :text "+"}))

(defn character-action-delete-button
  [char-id]
  (widgets/float-button {:style {:padding 0 :margin-left 10 :height 30 :width 30}
                         :text-style {:padding 0 :margin 0 :font-size 4 :left 3 :width 40}
                         :on-press #(do (>evt [:delete-character-action char-id (<sub [:active-action char-id])])
                                        (>evt [:set-active-action char-id (<sub [:active-action char-id])]))
                         :text (:delete widgets/common-icons)}))

(defn character-actions
  [char-id]
  [:> rn/View {:style {:padding-bottom 15 :align-items :center}}
   [:> rn/View {:style {:flex-direction :row}}
    [:> rn/Text {:style {:color :white :font-size 24}} "Actions"]
    (if (nil? (<sub [:active-action char-id]))
      (character-action-add-button char-id)
      (character-action-delete-button char-id))]
   (if (nil? (<sub [:active-action char-id]))
     [:> rn/View {:style {:flex-direction :row :flex-wrap :wrap :flex-shrink 1}}
      (doall (map character-action-button (repeat char-id) (keys (<sub [:character-actions (<sub [:active-character])]))))]
     [:> rn/View {:style {:flex-direction :row}}
      (if (<sub [:edit-mode])
        (character-action-edit-button char-id (<sub [:active-action char-id]))
        (character-action-button char-id (<sub [:active-action char-id])))])
   (if (some? (<sub [:active-action char-id]))
     (action char-id (<sub [:active-action char-id])))])

(defn character-notes
  [char-id]
  [:> rn/View
   [:> rn/Text {:style {:color :white :font-size 24}} "Notes"]
   [:> rn/TextInput {:multiline true
                     :style {:color :white}
                     :on-change-text #(>evt [:update-character-notes char-id (str %)])}
    (<sub [:character-notes char-id])]])


(defn card
  [content header state]
  (if (some? content)
    [:> rn/View {:style {:width "100%" :background-color "#222222" :padding 20 :margin 10}}
     (widgets/flat-button {:style {:background-color nil :margin-right 5 :position :absolute :align-self :flex-end}
                           :text-style {:color :black}
                           :text (if @state (:up-lrg widgets/common-icons) (:down-lrg widgets/common-icons))
                           :on-press (fn [] (swap! state not))})
     (if @state
       content
       (widgets/header-format {:font-size 28 :color :white :padding 10} header))]))

(defn character-details
  [char-id]
  (into [:> rn/View {:style {:width "95%" :justify-content :center :align-items :center}}]
        (map widgets/card
             [(if (<sub [:edit-mode])
                (character-profile-edit char-id)
                (if (< 0 (apply + (map count (vals (dissoc (dissoc (<sub [:character-profile char-id]) :name) :portrait)))))
                  (character-profile char-id)))
              (character-stats char-id "moderate")
              (if (<sub [:edit-mode])
                (resources char-id resource-button true)
                (resources char-id resource-button nil))
              (character-actions char-id)
              (character-notes char-id)]
             ["Details" "Stats" "Resources" "Actions" "Notes"]
             [character-details-toggle character-stats-toggle character-resources-toggle character-actions-toggle character-notes-toggle])))

(def show-db-diff
  (r/atom false))

(defn characters-tab
  []
  (let [char-id (<sub [:active-character])]
    [:> rn/View {:style {:width "100%" :align-items :center}}
     [:> rn/Text {:style {:color :white :font-size 30}} "Characters"]
     (widgets/float-button {:text "Database Download/Upload"
                           :text-style {:color :white}
                           :active? @show-db-diff
                           :on-press #(swap! show-db-diff not)})
     (if @show-db-diff
       (widgets/text-submission
        (fn [i] (>evt [:manual-initialize-db (edn/read-string (str i))]))
        ""
        "Upload Data"))
     (if @show-db-diff
       [:> rn/Text {:style {:color :white :text-align :center :margin-left "10%" :margin-right "10%"}
                    :selectable true}
        (str "To save data for future use, copy all of the data below and save it to some external location. To reload the data, copy all of that data back into the input field and submit it.\n\n"
             (<sub [:db-diff]))])
     
     [:> rn/View {:style {:position :absolute :right 0 :top 0 :width 50 :height 50 :border-radius 100}}
      (if (nil? char-id)
        character-add-button
        [:> rn/View
         (character-edit-button)
         character-init-delete-button
         (if @confirm-delete (character-delete-confirm-button))])]
     (if (<sub [:edit-mode])
       (name-edit char-id)
       (character-buttons))
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
