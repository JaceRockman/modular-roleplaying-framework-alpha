(ns main.initialdb
  (:require [main.styles :as styles]
            [main.data :as data]
            [main.play-section.data :as play-data]
            [main.rules-section.data :as rules-data]
            [main.world-section.data :as world-data]))

;; initial state of app-db
(defonce app-db {:style-mode styles/dark-mode
                 :edit-mode false
                 :active-section 1
                 :sections {0 {:id 0
                               :name :rules
                               :active-tab :flow
                               :tabs [:flow :stats :checks :encounters :damage :conditions]}
                            1 {:id 1
                               :name :play
                               :active-tab :characters
                               :tabs [:characters :resources]}
                            2 {:id 2
                               :name :world
                               :active-tab :overview
                               :tabs [:overview :territories :civilizations]}}
                 :active-world 1
                 :worlds {1 {:id 1
                             :name "Kalashar"
                             :active-territory 300
                             :territories  world-data/territories
                             :active-civilization nil
                             :civilizations world-data/civilizations}}
                 :active-character nil
                 :characters {0 {:id 0
                                 :profile {:portrait ""
                                           :name "Example Character"
                                           :gender ""
                                           :race ""
                                           :titles ""
                                           :description ""}
                                 :experience 0
                                 :active-stats {:skill nil
                                                :ability nil}
                                 :stats {:physical {:quality {:initiation   {:name "Coordination"
                                                                             :value 1}
                                                              :reaction     {:name "Reflexes"
                                                                             :value 1}
                                                              :continuation {:name "Endurance"
                                                                             :value 1}}
                                                    :power {:dominance  {:name "Might"
                                                                         :value 6}
                                                            :competence {:name "Finesse"
                                                                         :value 6}
                                                            :resilience {:name "Fortitude"
                                                                         :value 6}}}
                                         :spiritual {:quality {:initiation   {:name "Exertion"
                                                                              :value 1}
                                                               :reaction     {:name "Instinct"
                                                                              :value 1}
                                                               :continuation {:name "Perseverance"
                                                                              :value 1}}
                                                     :power {:dominance  {:name "Willpower"
                                                                          :value 6}
                                                             :competence {:name "Discipline"
                                                                          :value 6}
                                                             :resilience {:name "Tenacity"
                                                                          :value 6}}}
                                         :mental {:quality {:initiation   {:name "Concentration"
                                                                           :value 1}
                                                            :reaction     {:name "Recognition"
                                                                           :value 1}
                                                            :continuation {:name "Comprehension"
                                                                           :value 1}}
                                                  :power {:dominance  {:name "Intellect"
                                                                       :value 6}
                                                          :competence {:name "Focus"
                                                                       :value 6}
                                                          :resilience {:name "Stability"
                                                                       :value 6}}}
                                         :social {:quality {:initiation   {:name "Persuasion"
                                                                           :value 1}
                                                            :reaction     {:name "Insight"
                                                                           :value 1}
                                                            :continuation {:name "Connections"
                                                                           :value 1}}
                                                  :power {:dominance  {:name "Presence"
                                                                       :value 6}
                                                          :competence {:name "Wit"
                                                                       :value 6}
                                                          :resilience {:name "Poise"
                                                                       :value 6}}}}
                                 :damage {:physical {:minor 0
                                                     :major 0}
                                          :spiritual {:minor 0
                                                      :major 0}
                                          :mental {:minor 0
                                                   :major 0}
                                          :social {:minor 0
                                                   :major 0}}
                                 :active-resources '()
                                 :resources {1 {:notes "Notes"}
                                             2 {:notes "Notes"}
                                             3 {:notes "Notes"}
                                             4 {:notes "Notes"}
                                             5 {:notes "Notes"}}
                                 :active-action nil
                                 :actions {0 {:title "Physical Health Check"
                                              :description ""
                                              :skill-path [:physical :quality :continuation]
                                              :ability-path [:physical :power :resilience]
                                              :resources []
                                              :dice-mod 0
                                              :flat-mod 0
                                              :min-pool-size 2
                                              :splinters 1
                                              :combinations {}
                                              :target-number nil}
                                           1 {:title "Spiritual Health Check"
                                              :description ""
                                              :skill-path [:spiritual :quality :continuation]
                                              :ability-path [:spiritual :power :resilience]
                                              :resources []
                                              :dice-mod 0
                                              :flat-mod 0
                                              :min-pool-size 2
                                              :splinters 1
                                              :combinations {}
                                              :target-number nil}
                                           2 {:title "Mental Health Check"
                                              :description ""
                                              :skill-path [:mental :quality :continuation]
                                              :ability-path [:mental :power :resilience]
                                              :resources []
                                              :dice-mod 0
                                              :flat-mod 0
                                              :min-pool-size 2
                                              :splinters 1
                                              :combinations {}
                                              :target-number nil}
                                           3 {:title "Social Health Check"
                                              :description ""
                                              :skill-path [:social :quality :continuation]
                                              :ability-path [:social :power :resilience]
                                              :resources []
                                              :dice-mod 0
                                              :flat-mod 0
                                              :min-pool-size 2
                                              :splinters 1
                                              :combinations {}
                                              :target-number nil}}
                                 :notes "Notes"
                                 :rolls {0 []}}}
                 :active-resources {:equipment '()
                                    :traits '()
                                    :expertise '()
                                    :affiliations '()
                                    :items '()}
                 :resources {0 {:id 0
                                :type :items
                                :name "Resource Name"
                                :properties []
                                :benefits {:quality {:name "Quality"
                                                     :value 0}
                                           :power {:name "Power"
                                                   :value 0}}
                                :details "Resource Details"}
                         ;;     Weapons
                             100 {:id 100
                                  :type :equipment
                                  :name "Dagger"
                                  :properties [:light :sword :slashing :piercing]
                                  :benefits {:quality {:name "Balance"
                                                       :value 4}
                                             :power {:name "Lethality"
                                                     :value -1}}
                                  :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be in the same zone as you. As a light weapon, it may be wielded in either hand. When partitioning the base dice pool, the resulting dice pools must have at least two dice in them. This weapon may deal piercing or slashing damage."}
                             101 {:id 101
                                  :type :equipment
                                  :name "Short Sword"
                                  :properties [:light :sword :slashing :piercing]
                                  :benefits {:quality {:name "Balance"
                                                       :value 3}
                                             :power {:name "Lethality"
                                                     :value 0}}
                                  :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be in the same zone as you. As a light weapon, it may be wielded in either hand. When partitioning the base dice pool, the resulting dice pools must have at least two dice in them. This weapon may deal piercing or slashing damage."}
                             102 {:id 102
                                  :type :equipment
                                  :name "Rapier"
                                  :properties [:light :sword :piercing]
                                  :benefits {:quality {:name "Balance"
                                                       :value 3}
                                             :power {:name "Lethality"
                                                     :value 1}}
                                  :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be in the same zone as you. As a light weapon, it may be wielded in either hand. When partitioning the base dice pool, the resulting dice pools must have at least two dice in them. This weapon deals piercing damage."}
                             103 {:id 103
                                  :type :equipment
                                  :name "Longsword"
                                  :properties [:medium :sword :slashing :piercing]
                                  :benefits {:quality {:name "Balance"
                                                       :value 2}
                                             :power {:name "Lethality"
                                                     :value 2}}
                                  :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be in the same zone as you. As a medium weapon, it must be wielded in the dominant hand. When partitioning the base dice pool, the resulting dice pools must have at least three dice in them. This weapon may deal piercing or slashing damage."}
                             104 {:id 104
                                  :type :equipment
                                  :name "Greatsword"
                                  :properties [:heavy :sword :slashing :piercing]
                                  :benefits {:quality {:name "Balance"
                                                       :value 1}
                                             :power {:name "Lethality"
                                                     :value 4}}
                                  :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be in the same zone as you. As a heavy weapon, it must be wielded in both hands. When partitioning the base dice pool, the resulting dice pools must have at least four dice in them. This weapon may deal piercing or slashing damage."}
                             105 {:id 105
                                  :type :equipment
                                  :name "Handaxe"
                                  :properties [:light :axe :slashing :bludgeoning]
                                  :benefits {:quality {:name "Balance"
                                                       :value 2}
                                             :power {:name "Lethality"
                                                     :value 1}}
                                  :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be in the same zone as you. As a light weapon, it may be wielded in either hand. When partitioning the base dice pool, the resulting dice pools must have at least two dice in them. This weapon may deal slashing or bludgeoning damage."}
                             106 {:id 106
                                  :type :equipment
                                  :name "Battle Axe"
                                  :properties [:medium :axe :slashing :bludgeoning]
                                  :benefits {:quality {:name "Balance"
                                                       :value 1}
                                             :power {:name "Lethality"
                                                     :value 3}}
                                  :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be in the same zone as you. As a medium weapon, it must be wielded in the dominant hand. When partitioning the base dice pool, the resulting dice pools must have at least three dice in them. This weapon may deal slashing or bludgeoning damage."}
                             107 {:id 107
                                  :type :equipment
                                  :name "Greataxe"
                                  :properties [:heavy :axe :slashing :bludgeoning]
                                  :benefits {:quality {:name "Balance"
                                                       :value 0}
                                             :power {:name "Lethality"
                                                     :value 5}}
                                  :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be in the same zone as you. As a heavy weapon, it must be wielded in both hands. When partitioning the base dice pool, the resulting dice pools must have at least four dice in them. This weapon may deal slashing or bludgeoning damage."}
                             108 {:id 108
                                  :type :equipment
                                  :name "Javelin"
                                  :properties [:light :spear :piercing]
                                  :benefits {:quality {:name "Balance"
                                                       :value 1}
                                             :power {:name "Lethality"
                                                     :value 3}}
                                  :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be in the same zone as you. As a light weapon, it may be wielded in either hand. When partitioning the base dice pool, the resulting dice pools must have at least two dice in them. This weapon deals piercing damage."}
                             109 {:id 109
                                  :type :equipment
                                  :name "Spear"
                                  :properties [:medium :spear :piercing]
                                  :benefits {:quality {:name "Balance"
                                                       :value 2}
                                             :power {:name "Lethality"
                                                     :value 3}}
                                  :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be in the same zone as you. As a medium weapon, it must be wielded in the dominant hand. When partitioning the base dice pool, the resulting dice pools must have at least three dice in them. This weapon deals piercing damage."}
                             110 {:id 110
                                  :type :equipment
                                  :name "Lance"
                                  :properties [:heavy :spear :piercing]
                                  :benefits {:quality {:name "Balance"
                                                       :value 3}
                                             :power {:name "Lethality"
                                                     :value 3}}
                                  :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be in the same zone as you. As a heavy weapon, it must be wielded in both hands. When partitioning the base dice pool, the resulting dice pools must have at least four dice in them. This weapon deals piercing damage."}
                             111 {:id 111
                                  :type :equipment
                                  :name "Warhammer"
                                  :properties [:medium :hammer :bludgeoning]
                                  :benefits {:quality {:name "Balance"
                                                       :value 0}
                                             :power {:name "Lethality"
                                                     :value 5}}
                                  :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be in the same zone as you. As a medium weapon, it must be wielded in the dominant hand. When partitioning the base dice pool, the resulting dice pools must have at least three dice in them. This weapon deals bludgeoning damage."}
                             112 {:id 112
                                  :type :equipment
                                  :name "Greathammer"
                                  :properties [:heavy :heavy :bludgeoning]
                                  :benefits {:quality {:name "Balance"
                                                       :value 0}
                                             :power {:name "Lethality"
                                                     :value 6}}
                                  :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be in the same zone as you. As a heavy weapon, it must be wielded in both hands. When partitioning the base dice pool, the resulting dice pools must have at least four dice in them. This weapon deals bludgeoning damage."}
                             113 {:id 113
                                  :type :equipment
                                  :name "Hand Crossbow"
                                  :properties [:light :crossbow :piercing]
                                  :benefits {:quality {:name "Balance"
                                                       :value 1}
                                             :power {:name "Lethality"
                                                     :value 2}}
                                  :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be within 3 zones or 30 feet. As a light weapon, it may be wielded in either hand. When partitioning the base dice pool, the resulting dice pools must have at least two dice in them. This weapon deals piercing damage."}
                             114 {:id 114
                                  :type :equipment
                                  :name "Crossbow"
                                  :properties [:medium :crossbow :piercing]
                                  :benefits {:quality {:name "Balance"
                                                       :value 1}
                                             :power {:name "Lethality"
                                                     :value 3}}
                                  :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be within 8 zones or 80 feet. As a medium crossbow, it must be wielded in both hands. When partitioning the base dice pool, the resulting dice pools must have at least three dice in them. This weapon deals piercing damage."}
                             115 {:id 115
                                  :type :equipment
                                  :name "Shortbow"
                                  :properties [:light :bow :piercing]
                                  :benefits {:quality {:name "Balance"
                                                       :value 2}
                                             :power {:name "Lethality"
                                                     :value 1}}
                                  :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be within 5 zones or 50 feet. As a bow, it must be wielded in both hands. When partitioning the base dice pool, the resulting dice pools must have at least two dice in them. This weapon deals piercing damage."}
                             116 {:id 116
                                  :type :equipment
                                  :name "Longbow"
                                  :properties [:medium :bow :piercing]
                                  :benefits {:quality {:name "Balance"
                                                       :value 3}
                                             :power {:name "Lethality"
                                                     :value 1}}
                                  :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be with 10 zones or 100 feet. As a bow it must be wielded in both hands. When partitioning the base dice pool, the resulting dice pools must have at least three dice in them. This weapon deals piercing damage."}
                             117 {:id 117
                                  :type :equipment
                                  :name "Greatbow"
                                  :properties [:heavy :bow :piercing]
                                  :benefits {:quality {:name "Balance"
                                                       :value 2}
                                             :power {:name "Lethality"
                                                     :value 3}}
                                  :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be with 15 zones or 150 feet. As a bow, it must be wielded in both hands. When partitioning the base dice pool, the resulting dice pools must have at least four dice in them. This weapon deals piercing damage."}
                         ;;     Shields
                             118 {:id 118
                                  :type :equipment
                                  :name "Buckler"
                                  :properties [:light :shield]
                                  :benefits {:quality {:name "Balance"
                                                       :value 3}
                                             :power {:name "Coverage"
                                                     :value 0}}
                                  :details "You may spend a main action to make a Reflexes +Balance +Coverage check to defend yourself against a creature's attack that targets you. When partitioning the base dice pool while defending yourself against physical attacks, the resulting dice pools must have at least two dice in them. When multiple items contribute to a defense check, the partitioned dice pools must follow the size limitations of every item involved. As a shield, it can be wielded in either hand."}
                             119 {:id 119
                                  :type :equipment
                                  :name "Kite Shield"
                                  :properties [:medium :shield]
                                  :benefits {:quality {:name "Balance"
                                                       :value 2}
                                             :power {:name "Coverage"
                                                     :value 2}}
                                  :details "You may spend a main action to make a Reflexes +Balance +Coverage check to defend yourself against a creature's attack that targets you. When partitioning the base dice pool while defending yourself against physical attacks, the resulting dice pools must have at least three dice in them. When multiple items contribute to a defense check, the partitioned dice pools must follow the size limitations of every item involved. As a shield, it can be wielded in either hand."}
                             120 {:id 120
                                  :type :equipment
                                  :name "Tower Shield"
                                  :properties [:heavy :shield]
                                  :benefits {:quality {:name "Balance"
                                                       :value 0}
                                             :power {:name "Coverage"
                                                     :value 5}}
                                  :details "You may spend a main action to make a Reflexes +Balance +Coverage check to defend yourself against a creature's attack that targets you. When partitioning the base dice pool while defending yourself against physical attacks, the resulting dice pools must have at least four dice in them. When multiple items contribute to a defense check, the partitioned dice pools must follow the size limitations of every item involved. As a shield, it can be wielded in either hand."}
                         ;;     Armor
                             121 {:id 121
                                  :type :equipment
                                  :name "Cloth Armor"
                                  :properties [:light :padding]
                                  :benefits {:quality {:name "Flexibility"
                                                       :value 3}
                                             :power {:name "Durability"
                                                     :value 0}}
                                  :details "You may spend a main action to make a Reflexes +Quality +Power check to defend yourself against a creature's attack that targets you. When partitioning the base dice pool while defending yourself against physical attacks, the resulting dice pools must have at least two dice in them. When multiple items contribute to a defense check, the partitioned dice pools must follow the size limitations of every item involved."}
                             122 {:id 122
                                  :type :equipment
                                  :name "Hide Armor"
                                  :properties [:medium :padding :bludgeoning]
                                  :benefits {:quality {:name "Flexibility"
                                                       :value 3}
                                             :power {:name "Durability"
                                                     :value 0}}
                                  :details "You may spend a main action to make a Reflexes +Quality +Power check to defend yourself against a creature's attack that targets you. When partitioning the base dice pool while defending yourself against physical attacks, the resulting dice pools must have at least three dice in them. When multiple items contribute to a defense check, the partitioned dice pools must follow the size limitations of every item involved. This armor reduces the severity of injuries inflicted by bludgeoning damage."}
                             123 {:id 123
                                  :type :equipment
                                  :name "Leather Armor"
                                  :properties [:heavy :padding :bludgeoning :slashing]
                                  :benefits {:quality {:name "Flexibility"
                                                       :value 2}
                                             :power {:name "Durability"
                                                     :value 1}}
                                  :details "You may spend a main action to make a Reflexes +Quality +Power check to defend yourself against a creature's attack that targets you. When partitioning the base dice pool while defending yourself against physical attacks, the resulting dice pools must have at least four dice in them. When multiple items contribute to a defense check, the partitioned dice pools must follow the size limitations of every item involved. This armor reduces the severity of injuries inflicted by bludgeoning and slashing damage."}
                             124 {:id 124
                                  :type :equipment
                                  :name "Chain Maille Vest"
                                  :properties [:light :maille :slashing]
                                  :benefits {:quality {:name "Flexibility"
                                                       :value 1}
                                             :power {:name "Durability"
                                                     :value 1}}
                                  :details "You may spend a main action to make a Reflexes +Quality +Power check to defend yourself against a creature's attack that targets you. When partitioning the base dice pool while defending yourself against physical attacks, the resulting dice pools must have at least two dice in them. When multiple items contribute to a defense check, the partitioned dice pools must follow the size limitations of every item involved. This armor reduces the severity of injuries inflicted by slashing damage."}
                             125 {:id 125
                                  :type :equipment
                                  :name "Chain Maille Shirt"
                                  :properties [:medium :maille :slashing]
                                  :benefits {:quality {:name "Flexibility"
                                                       :value 1}
                                             :power {:name "Durability"
                                                     :value 2}}
                                  :details "You may spend a main action to make a Reflexes +Quality +Power check to defend yourself against a creature's attack that targets you. When partitioning the base dice pool while defending yourself against physical attacks, the resulting dice pools must have at least three dice in them. When multiple items contribute to a defense check, the partitioned dice pools must follow the size limitations of every item involved. This armor reduces the severity of injuries inflicted by slashing damage."}
                             126 {:id 126
                                  :type :equipment
                                  :name "Riveted Chain Maille"
                                  :properties [:heavy :maille :slashing :piercing]
                                  :benefits {:quality {:name "Flexibility"
                                                       :value 1}
                                             :power {:name "Durability"
                                                     :value 2}}
                                  :details "You may spend a main action to make a Reflexes +Quality +Power check to defend yourself against a creature's attack that targets you. When partitioning the base dice pool while defending yourself against physical attacks, the resulting dice pools must have at least four dice in them. When multiple items contribute to a defense check, the partitioned dice pools must follow the size limitations of every item involved. This armor reduces the severity of injuries inflicted by slashing and piercing damage."}
                             127 {:id 127
                                  :type :equipment
                                  :name "Leather Plate"
                                  :properties [:light :plate :slashing :piercing]
                                  :benefits {:quality {:name "Flexibility"
                                                       :value -1}
                                             :power {:name "Durability"
                                                     :value 2}}
                                  :details "You may spend a main action to make a Reflexes +Quality +Power check to defend yourself against a creature's attack that targets you. When partitioning the base dice pool while defending yourself against physical attacks, the resulting dice pools must have at least two dice in them. When multiple items contribute to a defense check, the partitioned dice pools must follow the size limitations of every item involved. This armor reduces the severity of injuries inflicted by slashing and piercing damage."}
                             128 {:id 128
                                  :type :equipment
                                  :name "Steel Half-Plate"
                                  :properties [:medium :plate :slashing :piercing :bludgeoning]
                                  :benefits {:quality {:name "Flexibility"
                                                       :value -2}
                                             :power {:name "Durability"
                                                     :value 3}}
                                  :details "You may spend a main action to make a Reflexes +Quality +Power check to defend yourself against a creature's attack that targets you. When partitioning the base dice pool while defending yourself against physical attacks, the resulting dice pools must have at least three dice in them. When multiple items contribute to a defense check, the partitioned dice pools must follow the size limitations of every item involved. This armor reduces the severity of injuries inflicted by slashing, piercing, and bludgeoning damage."}
                             129 {:id 129
                                  :type :equipment
                                  :name "Steel Full-Plate"
                                  :properties [:heavy :plate :slashing :piercing :bludgeoning]
                                  :benefits {:quality {:name "Flexibility"
                                                       :value -2}
                                             :power {:name "Durability"
                                                     :value 4}}
                                  :details "You may spend a main action to make a Reflexes +Quality +Power check to defend yourself against a creature's attack that targets you. When partitioning the base dice pool while defending yourself against physical attacks, the resulting dice pools must have at least four dice in them. When multiple items contribute to a defense check, the partitioned dice pools must follow the size limitations of every item involved. This armor reduces the severity of injuries inflicted by slashing, piercing, and bludgeoning damage."}
                             ;; Augments
                             ;; Relics
                             130 {:id 130
                                  :type :equipment
                                  :name "Tear of Ishiq"
                                  :properties [:relic]
                                  :benefits {:quality {:name "Quality"
                                                       :value 1}
                                             :power {:name "Power"
                                                     :value 3}}
                                  :details "A fog-colored stone said to have come from the Eye of Ishiq. Add water to the surface of the stone to produce bright light in a 50 foot radius. Once a day, you can speak a chosen word or phrase to create a flash of brilliant light. Make an Exertion +Quality +Power check. Any creature within 50 feet that has line of sight must make a Reflex check or be blinded for a moment."}
                             131 {:id 131
                                  :type :items
                                  :name "Gauntlet of Heilm"
                                  :properties [:relic]
                                  :benefits {:quality {:name "Quality"
                                                       :value 2}
                                             :power {:name "Power"
                                                     :value 2}}
                                  :details "A pair of gauntlets said to be molded upon the Fist of Heilm. While worn, gain +Quality +Power on checks to maintain a firm grasp. Great for climbing, grappling, and building, among other things."}
                             132 {:id 132
                                  :type :items
                                  :name "Fragrance of Ijarda"
                                  :properties [:relic]
                                  :benefits {:quality {:name "Quality"
                                                       :value 3}
                                             :power {:name "Power"
                                                     :value 1}}
                                  :details "A warm fragrance said to be the remnants of the Breath of Ijarda that was once held in this very bottle. When a creature smells the contents of this bottle, they gain +Quality +Power on their next Physical, Spiritual, or Mental Health Check."}
                             ;; Artifact
                             133 {:id 133
                                  :type :equipment
                                  :name "Prayer Beads"
                                  :properties [:relic]
                                  :benefits {:quality {:name ""
                                                       :value 0}
                                             :power {:name ""
                                                     :value 0}}
                                  :details ""}
                             134 {:id 134
                                  :type :items
                                  :name "Resource Name"
                                  :properties []
                                  :benefits {:quality {:name "Quality"
                                                       :value 0}
                                             :power {:name "Power"
                                                     :value 0}}
                                  :details "Resource Details"}
                             135 {:id 135
                                  :type :items
                                  :name "Resource Name"
                                  :properties []
                                  :benefits {:quality {:name "Quality"
                                                       :value 0}
                                             :power {:name "Power"
                                                     :value 0}}
                                  :details "Resource Details"}
                             ;; Totem
                             136 {:id 136
                                  :type :equipment
                                  :name "Prayer Beads"
                                  :properties [:relic]
                                  :benefits {:quality {:name ""
                                                       :value 0}
                                             :power {:name ""
                                                     :value 0}}
                                  :details ""}
                             137 {:id 137
                                  :type :items
                                  :name "Resource Name"
                                  :properties []
                                  :benefits {:quality {:name "Quality"
                                                       :value 0}
                                             :power {:name "Power"
                                                     :value 0}}
                                  :details "Resource Details"}
                             138 {:id 138
                                  :type :items
                                  :name "Resource Name"
                                  :properties []
                                  :benefits {:quality {:name "Quality"
                                                       :value 0}
                                             :power {:name "Power"
                                                     :value 0}}
                                  :details "Resource Details"}
                             ;; Vessel
                             139 {:id 139
                                  :type :equipment
                                  :name "Prayer Beads"
                                  :properties [:relic]
                                  :benefits {:quality {:name ""
                                                       :value 0}
                                             :power {:name ""
                                                     :value 0}}
                                  :details ""}
                             140 {:id 140
                                  :type :items
                                  :name "Resource Name"
                                  :properties []
                                  :benefits {:quality {:name "Quality"
                                                       :value 0}
                                             :power {:name "Power"
                                                     :value 0}}
                                  :details "Resource Details"}
                             141 {:id 141
                                  :type :items
                                  :name "Resource Name"
                                  :properties []
                                  :benefits {:quality {:name "Quality"
                                                       :value 0}
                                             :power {:name "Power"
                                                     :value 0}}
                                  :details "Resource Details"}

                             ;; Talismans
                             142 {:id 142
                                  :type :equipment
                                  :name "Tattoo"
                                  :properties [:relic]
                                  :benefits {:quality {:name ""
                                                       :value 0}
                                             :power {:name ""
                                                     :value 0}}
                                  :details ""}
                             143 {:id 143
                                  :type :items
                                  :name "Ring"
                                  :properties []
                                  :benefits {:quality {:name "Quality"
                                                       :value 0}
                                             :power {:name "Power"
                                                     :value 0}}
                                  :details "Resource Details"}
                             144 {:id 144
                                  :type :items
                                  :name "Amulet"
                                  :properties []
                                  :benefits {:quality {:name "Quality"
                                                       :value 0}
                                             :power {:name "Power"
                                                     :value 0}}
                                  :details "Resource Details"}

                             ;; Tools
                             145 {:id 145
                                  :type :items
                                  :name "Climber’s Tools"
                                  :properties []
                                  :benefits {:quality {:name "Quality"
                                                       :value 0}
                                             :power {:name "Power"
                                                     :value 0}}
                                  :details "Resource Details"}
                             146 {:id 146
                                  :type :items
                                  :name "Forgery Kit"
                                  :properties []
                                  :benefits {:quality {:name "Quality"
                                                       :value 0}
                                             :power {:name "Power"
                                                     :value 0}}
                                  :details "Resource Details"}
                             147 {:id 147
                                  :type :items
                                  :name "Lockpick Set"
                                  :properties []
                                  :benefits {:quality {:name "Quality"
                                                       :value 0}
                                             :power {:name "Power"
                                                     :value 0}}
                                  :details "Resource Details"}
                             148 {:id 148
                                  :type :items
                                  :name "Reference Document"
                                  :properties []
                                  :benefits {:quality {:name "Quality"
                                                       :value 0}
                                             :power {:name "Power"
                                                     :value 0}}
                                  :details "Resource Details"}
                             149 {:id 149
                                  :type :items
                                  :name "Traveller’s Tools"
                                  :properties []
                                  :benefits {:quality {:name "Quality"
                                                       :value 0}
                                             :power {:name "Power"
                                                     :value 0}}
                                  :details "Resource Details"}
                             150 {:id 150
                                  :type :items
                                  :name "Tinkerer’s Tools"
                                  :properties []
                                  :benefits {:quality {:name "Quality"
                                                       :value 0}
                                             :power {:name "Power"
                                                     :value 0}}
                                  :details "Resource Details"}
                             151 {:id 151
                                  :type :items
                                  :name "Alchemist’s Tools"
                                  :properties []
                                  :benefits {:quality {:name "Quality"
                                                       :value 0}
                                             :power {:name "Power"
                                                     :value 0}}
                                  :details "Resource Details"}
                             152 {:id 152
                                  :type :items
                                  :name "Musical Instrument"
                                  :properties []
                                  :benefits {:quality {:name "Quality"
                                                       :value 0}
                                             :power {:name "Power"
                                                     :value 0}}
                                  :details "Resource Details"}
                             153 {:id 153
                                  :type :items
                                  :name "Cartographer’s Tools"
                                  :properties []
                                  :benefits {:quality {:name "Quality"
                                                       :value 0}
                                             :power {:name "Power"
                                                     :value 0}}
                                  :details "Resource Details"}
                             154 {:id 154
                                  :type :items
                                  :name "Painter’s Supplies"
                                  :properties []
                                  :benefits {:quality {:name "Quality"
                                                       :value 0}
                                             :power {:name "Power"
                                                     :value 0}}
                                  :details "Resource Details"}
                             155 {:id 155
                                  :type :items
                                  :name "Herbalism Kit"
                                  :properties []
                                  :benefits {:quality {:name "Quality"
                                                       :value 0}
                                             :power {:name "Power"
                                                     :value 0}}
                                  :details "Resource Details"}
                             156 {:id 156
                                  :type :items
                                  :name "Disguise Kit"
                                  :properties []
                                  :benefits {:quality {:name "Quality"
                                                       :value 0}
                                             :power {:name "Power"
                                                     :value 0}}
                                  :details "Resource Details"}

                             ;; Traits
                             200 {:id 200
                                  :type :traits
                                  :name "Ideal Template"
                                  :properties [:ideal]
                                  :benefits {:quality {:name "Clarity"
                                                       :value 0}
                                             :power {:name "Dedication"
                                                     :value 0}}
                                  :details "Grants +Quality +Power to checks that promote or exemplify your ideal. Checks that run counter to your ideal gain penalties equal to the Quality and Power of your ideal. Circumstances may cause multiple traits to apply bonuses or penalties to a check. Ultimately, it is up to the DM's discretion whether a trait grants a bonus or penalty."}
                             ;; Courage
                             ;; Life
                             ;; Freedom
                             201 {:id 201
                                  :type :traits
                                  :name "Purpose Template"
                                  :properties [:purpose]
                                  :benefits {:quality {:name "Clarity"
                                                       :value 0}
                                             :power {:name "Dedication"
                                                     :value 0}}
                                  :details "Grants +Quality +Power to checks that fulfill your purpose. Checks that contradict your purpose gain penalties equal to the Quality and Power of your purpose. Circumstances may cause multiple traits to apply bonuses or penalties to a check. Ultimately, it is up to the DM's discretion whether a trait grants a bonus or penalty."}
                             ;; Serve the (Insert Noble Family)
                             ;; Protect Something Important to me
                             ;; Achieve Godhood
                             202 {:id 202
                                  :type :traits
                                  :name "Goal Template"
                                  :properties [:goal]
                                  :benefits {:quality {:name "Clarity"
                                                       :value 0}
                                             :power {:name "Dedication"
                                                     :value 0}}
                                  :details "Grants +Quality +Power to checks that move you towards accomplishing your goal Checks that set you back with respect to your goal gain penalties equal to the Quality and Power of your goal. Circumstances may cause multiple traits to apply bonuses or penalties to a check. Ultimately, it is up to the DM's discretion whether a trait grants a bonus or penalty."}
                             ;; Discover Forgotten Magics
                             ;; Money
                             ;; Be Granted Knighthood
                             203 {:id 203
                                  :type :traits
                                  :name "Oath Template"
                                  :properties [:oath]
                                  :benefits {:quality {:name "Clarity"
                                                       :value 0}
                                             :power {:name "Dedication"
                                                     :value 0}}
                                  :details "Grants +Quality +Power to checks that are aligned with your oath. Checks that are misaligned with your oath gain penalties equal to the Quality and Power of your oath. Circumstances may cause multiple traits to apply bonuses or penalties to a check. Ultimately, it is up to the DM's discretion whether a trait grants a bonus or penalty."}
                             ;; Uphold Virtues of Heilm
                             ;; Oath of Revenge
                             ;; Serve the Crown



                             ;; Expertise
                             300 {:id 300
                                  :type :expertise
                                  :name "Scout"
                                  :properties [:training :fighting-style]
                                  :benefits {:quality {:name "Knowledge"
                                                       :value 3}
                                             :power {:name "Practice"
                                                     :value 1}}
                                  :details "Gain +Quality +Power on checks that use a light weapon, shield, or armor."}
                             301 {:id 301
                                  :type :expertise
                                  :name "Soldier"
                                  :properties [:training :fighting-style]
                                  :benefits {:quality {:name "Knowledge"
                                                       :value 2}
                                             :power {:name "Practice"
                                                     :value 2}}
                                  :details "Gain +Quality +Power on checks that use a medium weapon, shield, or armor."}
                             302 {:id 302
                                  :type :expertise
                                  :name "Sentinel"
                                  :properties [:training :fighting-style]
                                  :benefits {:quality {:name "Knowledge"
                                                       :value 1}
                                             :power {:name "Practice"
                                                     :value 3}}
                                  :details "Gain +Quality +Power on checks that use a heavy weapon, shield, or armor."}
                             ;; Arcanist, Druid, Invoker
                             ;; Student, Educator, Researcher
                             ;; Noble, Storyteller, Leader

                             ;; Affiliations
                             4 {:id 4
                                :type :affiliations
                                :name "Example Affiliation ()"
                                :properties [:ally]
                                :benefits {:quality {:name "Reach"
                                                     :value 0}
                                           :power {:name "Influence"
                                                   :value 5}}
                                :details "Affiliation Details"}
                             ;; Individuals - Ally, Friend, Mentor, Family
                             ;; Groups - Guild, Order, School, Community
                             ;; Acknowledgements - Reputation, Title, Degree/Certification, Rumor

                             ;; Items
                             5 {:id 5
                                :type :items
                                :name "Example Item"
                                :properties [:ally]
                                :benefits {:quality {:name "Reach"
                                                     :value 0}
                                           :power {:name "Influence"
                                                   :value 5}}
                                :details "Item Details"}}
                 :rules {:stats  rules-data/stats
                         :checks rules-data/skill-checks-details
                         :encounters rules-data/encounters
                         :damage rules-data/damage
                         :conditions rules-data/conditions}
                 :rolls {}})






