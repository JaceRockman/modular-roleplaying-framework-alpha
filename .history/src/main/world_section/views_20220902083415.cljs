(ns main.world-section.views
  (:require
   ["react-native" :as rn]
   [main.helpers :as helpers :refer [<sub >evt]]
   [main.widgets :as widgets :refer [button text-section]]))

(defn overview-tab
  []
  [:> rn/View {:style {:height "100%" :align-items "center" :justify-content "center"}}
   [:> rn/Text {:style {:color :white :font-size 24}} (<sub [:active-world-name])]
   [:> rn/Text {:style {:color :white}} "In a time twice forgotten, the conspirations of Flame and Rain did yield the enshadowed form of Kalashar. In its depths did sleep the nascent souls of our most holy Lords. First did emerge Ishiq, the Lady of Light, who glimpsed the once-dappled canopy, and thus her tears of solitude did fill the sky with its ceaseless flame. Then came Heilm, the Lord of Preservation, who's mighty hands grasped the great stones which had fallen to the depths and held them together as one. Then Ijarda, The Mother of All, came forth and whispered the passion of embers and ash into the dust of the land. Last, Kef, The Child of Whimsy and Mischief, escaped and stole the eye of Ishiq, the fist of Heilm, and the breath of Ijarda, bestowing them as gifts upon the lands and creatures of Kalashar. All was as all was meant to be until ages passed and the first cosmic dusk befell these lands and the Gods retreated into slumber once again. A stagnance blankets Kalashar and the land itself yearns for a second kindling."]])


(defn territory-select-button
  [territory-id]
  (button {:style {:height "100%" :align-items "center" :justify-content "center" :background-color (if (= (:active-territory (<sub [:active-world])) territory-id) :gray "#333333") :padding 5 :margin 5 :width 200}
           :on-press #(>evt [:set-active-territory territory-id])
           :text-style {:color :white}
           :text (<sub [:territory-name territory-id])}))

(defn territories-buttons
  []
  [:> rn/View {:style {:flex 1 :flex-direction "row" :width "100%" :margin-top 15 :margin-bottom 15}}
   (doall (map territory-select-button (map #(:id (last %)) (<sub [:world-territories]))))])

(defn territories-details
  []
  [:> rn/Text {:style {:color :white}}
   (<sub [:territory-details (:active-territory (<sub [:active-world]))])])

(defn civilization-button
  [civilization-id]
  (button {:style {:background-color (if (and (= civilization-id (<sub [:active-civilization])) (= (<sub [:active-tab]) :civilizations)) :gray "#333333") :padding 5 :margin 5 :width 100 :align-items :center}
           :on-press #(do (>evt [:set-active-civilization civilization-id])
                          (>evt [:set-active-tab :civilizations]))
           :text-style {:color :white}
           :text (<sub [:civilization-name civilization-id])}))

(defn territory-civilizations-buttons
  []
  [:> rn/View {:style {:flex-direction :row :align-items "center" :justify-content "center"}}
   (doall (map civilization-button (<sub [:territory-civilizations (<sub [:active-territory])])))])

(defn territories-tab
  []
  [:> rn/View {:style {:height "100%" :align-items "center"}}
   [:> rn/Text {:style {:color :white :font-size 24}} "Territories"]
   (territories-buttons)
   (territory-civilizations-buttons)
   (territories-details)])

(defn civilizations-buttons
  []
  [:> rn/View {:style {:flex-direction :row :align-items "center" :justify-content "center"}}
   (doall (map civilization-button (<sub [:civilizations])))])



(defn civilization-details
  [civilization-id]
  (println civilization-id)
  (text-section (map #(name (first %)) (<sub [:civilization-details civilization-id])) (map last (<sub [:civilization-details civilization-id]))))

(defn civilizations-tab
  []
  [:> rn/View {:align-items "center"}
   [:> rn/Text {:style {:color :white :font-size 24}} "Civilizations"]
   [:> rn/Text {:style {:color :white :font-size 16}} (<sub [:territory-name (<sub [:active-territory])])]
   (civilizations-buttons)
   (civilization-details (<sub [:active-civilization]))])

(defn world-section
  []
  (case (<sub [:active-tab 2])
    :overview (overview-tab)
    :territories (territories-tab)
    :civilizations (civilizations-tab)))