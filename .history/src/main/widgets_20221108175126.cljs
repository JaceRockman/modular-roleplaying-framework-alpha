(ns main.widgets
  (:require
   ["react-native" :as rn]
   [reagent.core :as r]
   [main.styles :as styles]
   [main.helpers :as helpers :refer [>evt <sub]]))

(def VectorIcons (js/require "@expo/vector-icons"))
(def FontAwesome (.-FontAwesome VectorIcons))
(def fa (r/adapt-react-class FontAwesome))
(def FontAwesome5 (.-FontAwesome5 VectorIcons))
(def fa5 (r/adapt-react-class FontAwesome5))
(def MaterialCommunityIcons (.-MaterialCommunityIcons VectorIcons))
(def mci (r/adapt-react-class MaterialCommunityIcons))

(defn navigation-icons
  []
  {:prev [fa {:name "chevron-right" :style {:color (:on-surface (styles/active-mode)) :font-size 30}}]
   :next [fa {:name "chevron-left" :style {:color (:on-surface (styles/active-mode)) :font-size 30}}]})

(defn section-icons
  []
  {:rules [fa5 {:name "scroll" :style {:color (:on-surface (styles/active-mode)) :font-size 28}}]
   :play [fa {:name "user" :style {:color (:on-surface (styles/active-mode)) :font-size 36}}]
   :world [fa5 {:name "globe-europe" :style {:color (:on-surface (styles/active-mode)) :font-size 36}}]})

(defn tab-icons
  []
  {:flow [fa {:name "align-left" :style {:font-size 30}}]
   :stats [fa5 {:name "running" :style {:font-size 30}}]
   :checks [fa5 {:name "dice" :style {:font-size 30}}]
   :encounters [fa5 {:name "dragon" :style {:font-size 30}}]
   :damage [fa5 {:name "skull" :style {:font-size 30}}]
   :conditions [fa5 {:name "tint" :style {:font-size 30}}]
   :characters [fa {:name "users" :style {:font-size 30}}]
   :resources [fa5 {:name "angle-double-up" :style {:font-size 30}}]
   :overview [fa5 {:name "book-open" :style {:font-size 30}}]
   :territories [mci {:name "earth-box" :style {:font-size 30}}]
   :civilizations [fa5 {:name "place-of-worship" :style {:font-size 30}}]})

(def domain-icons
  {:physical [fa5 {:name "fist-raised" :style {:flex 0 :padding-left 16 :padding-right 16 :max-width 100 :color :white :font-size 50}}]
   :spiritual [fa5 {:name "eye" :style {:flex 0 :padding-left 4 :padding-right 9 :max-width 100 :color :white :font-size 50}}]
   :mental [mci {:name "brain" :style {:flex 0 :padding-left 8 :padding-right 11 :max-width 100 :color :white :font-size 50}}]
   :social [fa5 {:name "users" :style {:flex 0 :padding-left 0 :padding-right 6 :max-width 100 :color :white :font-size 50}}]})

(def common-icons
  {:create [fa5 {:name "plus" :style {:color :white}}]
   :duplicate [fa5 {:name "copy" :style {:color :white}}]
   :view [fa5 {:name "eye" :style {:color :white}}]
   :edit [fa5 {:name "pen" :style {:color :white}}]
   :delete [fa5 {:name "trash" :style {:color :white}}]
   :save [fa5 {:name "save" :style {:color :white}}]
   :up-sm [fa {:name "chevron-up" :style {:color (:on-surface (styles/active-mode)) :font-size 10}}]
   :up-med [fa {:name "chevron-up" :style {:color (:on-surface (styles/active-mode)) :font-size 18}}]
   :up-lrg [fa {:name "chevron-up" :style {:color (:on-surface (styles/active-mode)) :font-size 26}}]
   :dbl-up-sm [fa {:name "angle-double-up" :style {:color (:on-surface (styles/active-mode)) :font-size 20}}]
   :down-sm [fa {:name "chevron-down" :style {:color (:on-surface (styles/active-mode)) :font-size 10}}]
   :down-med [fa {:name "chevron-down" :style {:color (:on-surface (styles/active-mode)) :font-size 18}}]
   :down-lrg [fa {:name "chevron-down" :style {:color (:on-surface (styles/active-mode)) :font-size 26}}]
   :dbl-down-sm [fa {:name "angle-double-down" :style {:color (:on-surface (styles/active-mode)) :font-size 20}}]
   :left-sm [fa {:name "chevron-left" :style {:color (:on-surface (styles/active-mode)) :font-size 10}}]
   :left-med [fa {:name "chevron-left" :style {:color (:on-surface (styles/active-mode)) :font-size 18}}]
   :left-lrg [fa {:name "chevron-left" :style {:color (:on-surface (styles/active-mode)) :font-size 26}}]
   :right-sm [fa {:name "chevron-right" :style {:color (:on-surface (styles/active-mode)) :font-size 10}}]
   :right-med [fa {:name "chevron-right" :style {:color (:on-surface (styles/active-mode)) :font-size 18}}]
   :right-lrg [fa {:name "chevron-right" :style {:color (:on-surface (styles/active-mode)) :font-size 26}}]})

(def resource-icons
  {:weapon [fa5 {:name "sword" :style {:color :white}}]
   :shield [fa5 {:name "shield-halved" :style {:color :white}}]
   :armor [mci {:name "tshirt-v-outline" :style {:color :white}}]})



(defn button
  [{:keys [style text-style on-press
           active? active-style active-text-style
           disabled? disabled-style disabled-text-style
           text]
    :or {on-press #()}}]
  [:> rn/TouchableOpacity
   {:style (-> style
               (merge (if active? active-style))
               (merge (if disabled? disabled-style)))
    :on-press on-press
    :disabled disabled?
    :key (.toString (random-uuid))}
   [:> rn/Text
    {:style (-> text-style
                (merge (if active? active-text-style))
                (merge (if disabled? disabled-text-style)))}
    text]])

(defn float-button 
  [props]
  (button (merge-with
           #(if (map? %2) (merge %1 %2) %2)
           {:key (helpers/new-key)
            :style styles/float-button-style
            :text-style styles/float-button-text-style
            :active? nil
            :active-style styles/active-float-button-style
            :active-text-style styles/active-float-button-text-style
            :disabled? nil
            :disabled-style styles/disabled-float-button-style
            :disabled-text-style styles/disabled-float-button-text-style
            :on-press #()
            :text ""}
           props)))

(defn flat-button 
  [props]
  (button (merge-with
           #(if (map? %2) (merge %1 %2) %2)
           {:key (helpers/new-key)
            :style styles/flat-button-style
            :text-style styles/flat-button-text-style
            :active? nil
            :active-style styles/active-flat-button-style
            :active-text-style styles/active-flat-button-text-style
            :disabled? nil
            :disabled-style styles/disabled-flat-button-style
            :disabled-text-style styles/disabled-flat-button-text-style
            :on-press #()
            :text ""}
           props)))

(defn dice-pool-button
  [char-id action-id dice-pool]
  (button {:key (helpers/new-key)
           :style styles/flat-button-style
           :text-style styles/flat-button-text-style
           :active? nil
           :active-style styles/active-flat-button-style
           :active-text-style styles/active-flat-button-text-style
           :disabled? nil
           :disabled-style styles/disabled-flat-button-style
           :disabled-text-style styles/disabled-flat-button-text-style
           :on-press #(>evt [:log-dice-roll char-id action-id dice-pool])
           :text (str "Roll: " (apply str (interpose " + " (map :result (map #(apply helpers/formatted-roll %) dice-pool)))))}))



(defn incrementor
  [inc-fn inc-disabled? dec-fn dec-disabled? & inner-content]
  [:> rn/View {:key (helpers/new-key)
               :style {:flex-direction :column :align-items :center :justify-content :center}}
   (flat-button {:style {:width 30 :height 30 :padding 0}
                 :on-press inc-fn
                 :text-style {:font-size 10}
                 :text (:up-sm common-icons)
                 :disabled? inc-disabled?})
   (if (some? inner-content) inner-content)
   (flat-button {:style {:width 30 :height 30 :padding 0}
                 :on-press dec-fn
                 :text-style {:font-size 10}
                 :text (:down-sm common-icons)
                 :disabled? dec-disabled?})])

(defn incrementor-alt
  [inc-fn inc-disabled? dec-fn dec-disabled? & inner-content]
  [:> rn/View {:key (helpers/new-key)
               :style {:flex-direction :column :align-items :center :justify-content :center}}
   (flat-button {:style {:width 30 :height 30 :padding 0}
                 :on-press inc-fn
                 :text-style {:font-size 10 :height 20}
                 :text (:dbl-up-sm common-icons)
                 :disabled? inc-disabled?})
   (if (some? inner-content) inner-content)
   (flat-button {:style {:width 30 :height 30 :padding 0}
                 :on-press dec-fn
                 :text-style {:font-size 10 :height 20}
                 :text (:dbl-down-sm common-icons)
                 :disabled? dec-disabled?})])

(defn h-incrementor
  [dec-fn dec-disabled? inc-fn inc-disabled? & inner-content]
  [:> rn/View {:key (helpers/new-key)
               :style {:flex-direction :row :align-items :center :justify-content :center :bottom 5}}
   (flat-button {:style {:width 30 :height 30 :padding 0}
                 :on-press dec-fn
                 :text-style {:font-size 10}
                 :text (:left-sm common-icons)
                 :disabled? dec-disabled?})
   (if (some? inner-content) inner-content)
   (flat-button {:style {:width 30 :height 30 :padding 0}
                 :on-press inc-fn
                 :text-style {:font-size 10}
                 :text (:right-sm common-icons)
                 :disabled? inc-disabled?})])

(merge {:flex-direction :column :align-items :center :justify-content :center} {:flex 2})

(defn tab-navigation
  [tab-props tab-titles content tracker]
  [:> rn/View {:key (helpers/new-key)}
   [:> rn/View {:style {:align-items :center :justify-content :center :flex-direction :row :padding-bottom 20}}
    (doall (map-indexed (fn [i title]
                          (flat-button (merge tab-props {:text title
                                                         :on-press #(reset! tracker i)
                                                         :active? (= (.indexOf tab-titles title) (deref tracker))})))
                        tab-titles))]
   (get content (deref tracker))])



(defn header-format
  [{:keys [font-size color padding]} text]
  [:> rn/Text {:key (helpers/new-key)
               :style {:font-size font-size :color color :padding padding}}
   (helpers/title-case text)])


(defn text-section
  ([headers paragraphs]
   (into [] (reduce (fn [view [header paragraph]]
                      (-> view
                          (concat [(header-format {:font-size 24 :color :white} header)]
                                  [[:> rn/Text {:style {:color :white :padding-bottom 20}} paragraph]])))
                    [:> rn/View]
                    (map vector headers paragraphs))))
  ([headers paragraphs examples]
   (into [] (reduce (fn [view [header paragraph example]]
                      (-> view
                          (concat [(header-format {:font-size 24 :color :white} header)]
                                  [[:> rn/Text {:style {:color :white}} paragraph]]
                                  (let [display-example? (r/atom true)
                                        get-display @display-example?]
                                    (if (some? example)
                                      [[:> rn/View {:style {:padding-bottom 20}}
                                        [:> rn/View {:flex-direction :row :align-items :center}
                                         [:> rn/Text {:style {:color :white :font-size 18}} "Examples: "]]
                                        [:> rn/Text {:style {:color :white :display (if get-display :flex :none)}}
                                         (str example)]]]
                                      [[:> rn/View {:style {:padding-bottom 20}}]])))))
                    [:> rn/View]
                    (map vector headers paragraphs examples)))))

(defn text-area
  [evt sub label & wrap?]
  [:> rn/View {:key (helpers/new-key) 
               :style {:flex-direction :row}}
   [:> rn/Text {:style {:color :white :font-size 18}} (str label ": ")]
   [:> rn/TextInput {:style {:background-color :white :padding-left 10 :padding-right 10 :margin-bottom 3 :margin-top 3 :bottom 5}
                     :multiline true
                     :on-change-text #(evt %)
                     :default-value sub}
    ""]])

(defn text-submission
  [evt sub label]
  [:> rn/View {:flex-direction :row}
   [:> rn/Text {:style {:color :white :font-size 18}} (str label ": ")]
   [:> rn/TextInput {:style {:background-color :white :padding-left 10 :padding-right 10 :margin-bottom 3 :margin-top 3 :bottom 5}
                     :on-change-text #(evt %)
                     :default-value sub}
    ""]])

(defn card
  [content header state]
  (if (some? content)
    [:> rn/View {:key (helpers/new-key)
                 :style {:width "100%" :background-color "#222222" :padding 20 :margin 10}}
     (flat-button {:style {:background-color nil :margin-right 5 :position :absolute :align-self :flex-end}
                           :text-style {:color :black}
                           :text (if @state (:up-lrg common-icons) (:down-lrg common-icons))
                           :on-press (fn [] (swap! state not))})
     (if @state
       content
       (header-format {:font-size 28 :color :white :padding 10} header))]))