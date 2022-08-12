(ns main.app
  (:require [clojure.core.async :refer [go chan >!]]
            [main.expo.root :as expo-root]
            [re-frame.core :as rf]
            ["react-native" :as rn]
            [reagent.core :as r]
            [main.helpers :as helpers :refer [>evt <sub]]
            [main.events]
            [main.subs]
            [main.styles :as styles]
            [main.widgets :as widgets]
            [main.views :as views]
            [main.play-section.views :as play-views]
            [main.rules-section.views :as rules-views]
            [main.world-section.views :as world-views]))

;; (def shadow-splash (js/require "../assets/shadow-cljs.png"))
;; (def cljs-splash (js/require "../assets/cljs.png"))
;; (def character-icon (js/require "../assets/user.svg"))


;; (defn get-string-channel-from-file-path [path]
;;   (let [fs (js/require "fs") c (chan)]
;;     (.readFileSync fs path "utf8" (fn [err data] (go (>! c data))))
;;     c))


(defn root []
  [:> rn/ScrollView {:style {:background-color (get styles/dark-mode :background)}}
   [:> rn/StatusBar {:hidden true}]
   (views/navigation-view)
   [:> rn/ScrollView {:style {:background-color (:background (<sub [:style-mode]))}
                      :content-container-style {:align-items "center"
                                                :justify-content "center"}}
    (case (<sub [:active-section])
      0 (rules-views/rules-section)
      1 (play-views/play-section)
      2 (world-views/world-section)
      :else (rules-views/rules-section))]])

(defn start
  {:dev/after-load true}
  []
  (expo-root/render-root (r/as-element [root])))

(defn init []
  (rf/dispatch-sync [:initialize-db])
  (start))

