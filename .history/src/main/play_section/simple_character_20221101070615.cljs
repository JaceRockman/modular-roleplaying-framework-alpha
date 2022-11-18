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

(defn character-data
  [char-id]
  [:> rn/View
   (main-play-section/character-stats char-id "simple")
   (widgets/button {})])

(defn character-details
  [char-id]
  (into [:> rn/View {:style {:width "95%" :align-items :center}}]
        (map widgets/card
             [(character-info char-id)
              (character-data char-id)]
             ["Details" "Stats & Resources"]
             [main-play-section/character-details-toggle main-play-section/character-stats-toggle])))

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