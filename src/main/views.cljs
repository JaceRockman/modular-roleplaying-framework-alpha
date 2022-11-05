(ns main.views
  (:require
   ["react-native" :as rn]
   [main.helpers :as helpers :refer [>evt <sub]]
   [main.events]
   [main.subs]
   [main.styles :as styles]
   [main.widgets :as widgets]))

(defn tab
  [tab-name]
  (widgets/flat-button {:style {:background-color nil :width 80 :height 75}
                        :active-style {:background-color (:background (<sub [:style-mode]))}
                        :on-press #(>evt [:set-active-tab tab-name])
                        :active? (= tab-name (<sub [:active-tab]))
                        :text (get (widgets/tab-icons) tab-name)}))

(defn prev-navigation
  []
  (widgets/flat-button {:style {:background-color nil}
                        :on-press #(>evt [:prev-section])
                        :text [:> rn/Text {:style {:flex-direction "row" :align-items "center" :justify-content "center"}}
                               (get (widgets/section-icons) (<sub [:section-name (case (<sub [:active-section]) 0 2 1 0 2 1)]))
                               (:prev (widgets/navigation-icons))]}))

(defn tab-navigation
  []
  (doall (map tab (<sub [:section-tabs (<sub [:active-section])]))))

(defn next-navigation
  []
  (widgets/flat-button {:style {:background-color nil}
                        :on-press #(>evt [:next-section])
                        :text [:> rn/Text {:style {:flex-direction "row" :align-items "center" :justify-content "center"}}
                               (:next (widgets/navigation-icons))
                               (get (widgets/section-icons) (<sub [:section-name (case (<sub [:active-section]) 0 1 1 2 2 0)]))]}))




(defn navigation-view
  []
  [:> rn/View {:style styles/navigation-bar-style}
   (prev-navigation)
   helpers/spacer
   (tab-navigation)
   helpers/spacer
   (next-navigation)])