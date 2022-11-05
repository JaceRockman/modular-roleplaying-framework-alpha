(ns main.rules-section.views
  (:require
   ["react-native" :as rn]
   [main.helpers :as helpers :refer [<sub >evt title-case]]
   [main.widgets :as widgets :refer [button domain-icons text-section]]
   [main.db :as db :refer [app-db]]))

(defn flow-tab
  []
  [:> rn/View {:style {:align-items "center" :justify-content "center"}}
   [:> rn/Text {:style {:color :white :font-size 30}} "Flow"]
   [:> rn/Text {:style {:color :white}}
    "1. The GM describes a scene or situation.\n\n2. The players state how their characters react by describing their intent and approach.\n\n3. The GM may call for a check to be made in order to resolve the characters’ actions. This usually will happen if the following are true: there is a reasonable chance of those actions failing and failure has a cost.\n\n4. Based on the characters’ actions, the GM and players collaboratively describe how the scene or situation develops and we begin again at step 1."]])



(defn stat-info
  [name]
  [:> rn/View {:key (.toString (random-uuid)) :style {:height 55 :width 100 :flex-direction :column :justify-content "center" :align-items "center" :padding 5 :border-width 1 :border-color :white}}
   [:> rn/Text {:style {:font-size 12 :color :white}} name]])

(defn header
  [text]
  [:> rn/Text {:style {:font-size 24 :color :white :padding-top 10}} text])

(defn body
  [text]
  [:> rn/Text {:style {:color :white}} text])

(defn stats-tab
  []
  [:> rn/View {:style {:align-items :center :justify-content :center}}
   [:> rn/Text {:style {:color :white :font-size 30}} "Stats"]
   (let [stats (<sub [:moderate-stats 0])]
     [:> rn/View {:style {:align-items "center" :justify-content "center"}}
      [:> rn/View {:style {:flex-direction :row :align-items "center" :justify-content "center"}}
       (:physical domain-icons) (map #(apply stat-info %) (map vals (:physical stats))) [:> rn/View {:style {:width 50}}]]
      [:> rn/View {:style {:flex-direction :row :align-items "center" :justify-content "center"}}
       (:spiritual domain-icons) (map #(apply stat-info %) (map vals (:spiritual stats))) [:> rn/View {:style {:width 50}}]]
      [:> rn/View {:style {:flex-direction :row :align-items "center" :justify-content "center"}}
       (:mental domain-icons) (map #(apply stat-info %) (map vals (:mental stats))) [:> rn/View {:style {:width 50}}]]
      [:> rn/View {:style {:flex-direction :row :align-items "center" :justify-content "center"}}
       (:social domain-icons) (map #(apply stat-info %) (map vals (:social stats))) [:> rn/View {:style {:width 50}}]]
      (let [{:keys [coordination reflexes endurance
                    exertion instinct perseverance
                    concentration recognition comprehension
                    persuasion insight connections]} (<sub [:stat-info])]
        [:> rn/View {:style {:justify-content :center}}
         (header "Coordination") (body coordination)
         (header "Reflexes") (body reflexes)
         (header "Endurance") (body endurance)
         (header "Exertion") (body exertion)
         (header "Instinct") (body instinct)
         (header "Perseverance") (body perseverance)
         (header "Concentration") (body concentration)
         (header "Recognition") (body recognition)
         (header "Comprehension") (body comprehension)
         (header "Persuasion") (body persuasion)
         (header "Insight") (body insight)
         (header "Connections") (body connections)])])])

(defn checks-tab
  []
  [:> rn/View {:style {:flex-direction :column :align-items :center}}
   [:> rn/Text {:style {:color :white :font-size 30}} "Checks"]
   (text-section (map #(get % :header) (vals (<sub [:checks])))
                 (map #(get % :body) (vals (<sub [:checks])))
                 (map #(get % :example) (vals (<sub [:checks]))))])

(defn encounters-tab
  []
  [:> rn/View {:style {:flex-direction :column :align-items :center}}
   [:> rn/Text {:style {:color :white :font-size 30}} "Encounters"]
   (text-section (map #(get % :header) (vals (<sub [:encounters]))) 
                 (map #(get % :body) (vals (<sub [:encounters])))
                 (map #(get % :example) (vals (<sub [:encounters]))))])

(defn damage-tab
  []
  [:> rn/View {:style {:flex-direction :column :align-items :center}}
   [:> rn/Text {:style {:color :white :font-size 30}} "Damage"]
   (text-section (map #(get % :header) (vals (<sub [:damage])))
                 (map #(get % :body) (vals (<sub [:damage])))
                 (map #(get % :example) (vals (<sub [:damage]))))])

(defn conditions-tab
  []
  [:> rn/View {:style {:align-items :center}}
   [:> rn/Text {:style {:color :white :font-size 30}} "Conditions"]
   (into [] (reduce (fn [view [header {:keys [effect duration]}]]
                      (-> view
                          (concat [[:> rn/Text {:style {:color :white :font-size 24 :padding-top 20}} (title-case (clojure.string/replace header #"-" " "))]]
                                  [[:> rn/Text {:style {:color :white :font-size 16}} "Effect"]]
                                  [[:> rn/Text {:style {:color :white :padding-left 10}} effect]]
                                  [[:> rn/Text {:style {:color :white :font-size 16}} "Duration"]]
                                  [[:> rn/Text {:style {:color :white :padding-left 10}} duration]])))
                    [:> rn/View]
                    (map vector (map #(get % :header) (vals (<sub [:conditions]))) (map #(get % :body) (vals (<sub [:conditions]))))))])




(defn rules-section
  []
  (case (<sub [:active-tab 0])
    :flow (flow-tab)
    :stats (stats-tab)
    :checks (checks-tab)
    :encounters (encounters-tab)
    :damage (damage-tab)
    :conditions (conditions-tab)))