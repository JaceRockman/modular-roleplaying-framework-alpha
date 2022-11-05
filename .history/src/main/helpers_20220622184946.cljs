(ns main.helpers
  (:require [clojure.string :as str]
            [re-frame.core :as rf]
            ["react-native" :as rn]))


(def <sub (comp deref rf/subscribe))
(def >evt rf/dispatch)

(defn title-case
  [string]
  (as-> string s
    (str/capitalize s)
    (str/split s #" ")
    (map #(if (< 3 (count %)) (str/capitalize %) %) s)
    (str/join " " s)))

(defn new-key
  []
  (.toString (random-uuid)))

(defn get-next-highest-key
  [m]
  (inc (apply max (keys m))))

(defn exsert
  [coll val]
  (filter (fn [coll-val] (not (= val coll-val))) coll))

(defn dissoc-in
  [coll keys dissoc-key]
  (assoc-in coll keys 
            (dissoc (get-in coll keys) dissoc-key)))

(defn combinations3
  "Get all possible combinations of the values of 3 collections"
  [x y z]
  (into [] (for [x x y y z z] [x y z])))

(defn partition-min
  "Partition a collection into subcollections each the size of min. If there are any remaining values, they are added to the last subcollection.
   
   (partition-min 2 [1 2 3 4 5]) -> [(1 2) (3 4 5)]"
  [min coll]
  (loop [rem-coll coll
         partd-coll []]
    (if (> min (count rem-coll))
      (conj (vec (drop-last partd-coll)) (concat (last partd-coll) rem-coll))
      (recur (drop min rem-coll) (conj partd-coll (take min rem-coll))))))

(def spacer
  [:> rn/View {:style {:flex 10}}])

(def skill-paths
  (combinations3
   [:physical :spiritual :mental :social]
   [:quality]
   [:initiation :reaction :continuation]))

(def ability-paths
  (combinations3
   [:physical :spiritual :mental :social]
   [:power]
   [:dominance :competence :resilience]))

(def all-paths
  (into [] (concat skill-paths ability-paths)))

(def stat-paths
  (apply merge (map hash-map
                    [:coordination :reflexes :endurance
                     :exertion :instinct :perseverance
                     :concentration :recognition :comprehension
                     :persuasion :insight :connections
                     :might :finesse :fortitude
                     :willpower :discipline :tenacity
                     :intellect :focus :stability
                     :presence :wit :poise]
                    all-paths)))


(defn path-to-stat
  [path]
  (get
   (->> stat-paths
        (map reverse)
        (map #(apply hash-map %))
        (apply merge))
   path))

(defn formatted-dice-mod
  [dice-mod]
  (cond
    (> 0 dice-mod) (str dice-mod "d")
    (= 0 dice-mod) "--"
    (< 0 dice-mod) (str "+" dice-mod "d")))

(defn formatted-flat-mod
  [flat-mod]
  (cond
    (> 0 flat-mod) (str flat-mod)
    (= 0 flat-mod) "--"
    (< 0 flat-mod) (str "+" flat-mod)))

(defn formatted-skill-bonus
  [dice-bonus flat-bonus]
  (str (cond
         (< 0 dice-bonus) (str "+" dice-bonus "d ")
         (= 0 dice-bonus) nil
         (> 0 dice-bonus) (str dice-bonus "d "))
       (cond
         (< 0 flat-bonus) (str "+" flat-bonus)
         (= 0 flat-bonus) nil
         (> 0 flat-bonus) (str flat-bonus))))

(defn formatted-roll
  [quality power flat-bonus]
  (let [adjusted-quality (if (> 1 quality)
                           1
                           quality)
        adjusted-power (if (> 1 quality)
                         (- power (* 2 (- 1 quality)))
                         power)
        auto-fail? (or (> 1 adjusted-power)
                       (case adjusted-quality
                         1 (> 1 (+ adjusted-power flat-bonus))
                         (> 1 (+ 2 adjusted-power flat-bonus))))]
    {:dice-number adjusted-quality
     :dice-size adjusted-power
     :dice-bonus flat-bonus
     :result (if auto-fail?
               "Failure"
               (str
                adjusted-quality "d" adjusted-power
                (cond
                  (< 0 flat-bonus) (str " +" flat-bonus)
                  (= 0 flat-bonus) nil
                  (> 0 flat-bonus) (str " " flat-bonus))))}))

(defn abs
  [n]
  (if (< 0 n)
    n
    (* -1 n)))

(defn formatted-roll-with-combs
  [quality power flat-bonus combs]
  (cond
    (or (= 0 combs)
        (nil? combs)) [(formatted-roll quality power flat-bonus)]
    (< 0 combs) (if (= quality (* 2 combs))
                  [(formatted-roll combs (+ power 2) flat-bonus)]
                  [(formatted-roll combs (+ power 2) 0)
                   (formatted-roll (- quality (* 2 combs))
                                   power
                                   flat-bonus)])
    (> 0 combs) (if (= quality (abs combs))
                  [(formatted-roll (* 2 (abs combs)) (- power 2) flat-bonus)]
                  [(formatted-roll (* 2 (abs combs)) (- power 2) 0)
                   (formatted-roll (- quality (abs combs))
                                   power
                                   flat-bonus)])))

(defn splinter-math
  [n splinters]
  (let [base (quot n splinters)
        splinters (repeat splinters base)
        remainder (- n (apply + splinters))]
    (map-indexed #(if (< %1 remainder) (inc %2) %2) splinters)))

(defn splinter
  [splinters [quantity size bonus]]
  (let [quantities (splinter-math quantity splinters)
        bonuses (splinter-math bonus splinters)]
    (map vector quantities (repeat size) bonuses)))

(defn apply-combination
  [[quantity size bonus combs]]
  (if (or (nil? combs)
          (= 0 combs))
    [[quantity size bonus]]
    (let [applied-combs (formatted-roll-with-combs quantity size bonus combs)]
      (map #(mapv (fn [applied-comb k] 
                    (get applied-comb k)) 
                  (repeat %)
                  [:dice-number :dice-size :dice-bonus]) 
           applied-combs))))

(defn roll-dice
  [quantity size]
  (take quantity (repeatedly #(inc (rand-int size)))))

(defn passive-result
  [{:keys [dice-number dice-size dice-bonus]}]
  (let [result (+ dice-bonus (min dice-size (+ dice-number (quot dice-size 2))))]
    (if (< 0 result)
      result
      "Failure")))

