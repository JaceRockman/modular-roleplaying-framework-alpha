(ns main.db
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
                                :edit-mode? false
                                :type :items
                                :name "Resource Name"
                                :properties []
                                :benefits {:quality {:name "Quality"
                                                     :value 0}
                                           :power {:name "Power"
                                                   :value 0}}
                                :details "Resource Details"}
                             1 {:id 1
                                :edit-mode? false
                                :typ :equipment
                                :name "Example Equipment"
                                :properties [:sword]
                                :benefits {:quality {:name "Balance"
                                                     :value -1}
                                           :power {:name "Lethality"
                                                   :value 2}}
                                :details "Equipment Details"}
                         ;;     Weapons
                             100 {:id 100
                                  :edit-mode? false
                                  :typ :equipment
                                  :name "Dagger"
                                  :properties [:light :sword :slashing :piercing]
                                  :benefits {:quality {:name "Balance"
                                                       :value 4}
                                             :power {:name "Lethality"
                                                     :value -1}}
                                  :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be in the same zone as you. As a light weapon, it may be wielded in either hand. When partitioning the base dice pool, the resulting dice pools must have at least two dice in them. This weapon may deal piercing or slashing damage."}
                             101 {:id 101
                                  :edit-mode? false
                                  :typ :equipment
                                  :name "Short Sword"
                                  :properties [:light :sword :slashing :piercing]
                                  :benefits {:quality {:name "Balance"
                                                       :value 3}
                                             :power {:name "Lethality"
                                                     :value 0}}
                                  :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be in the same zone as you. As a light weapon, it may be wielded in either hand. When partitioning the base dice pool, the resulting dice pools must have at least two dice in them. This weapon may deal piercing or slashing damage."}
                             102 {:id 102
                                  :edit-mode? false
                                  :typ :equipment
                                  :name "Rapier"
                                  :properties [:light :sword :piercing]
                                  :benefits {:quality {:name "Balance"
                                                       :value 3}
                                             :power {:name "Lethality"
                                                     :value 1}}
                                  :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be in the same zone as you. As a light weapon, it may be wielded in either hand. When partitioning the base dice pool, the resulting dice pools must have at least two dice in them. This weapon deals piercing damage."}
                             103 {:id 103
                                  :edit-mode? false
                                  :typ :equipment
                                  :name "Longsword"
                                  :properties [:medium :sword :slashing :piercing]
                                  :benefits {:quality {:name "Balance"
                                                       :value 2}
                                             :power {:name "Lethality"
                                                     :value 2}}
                                  :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be in the same zone as you. As a medium weapon, it must be wielded in the dominant hand. When partitioning the base dice pool, the resulting dice pools must have at least three dice in them. This weapon may deal piercing or slashing damage."}
                             104 {:id 104
                                  :edit-mode? false
                                  :typ :equipment
                                  :name "Greatsword"
                                  :properties [:heavy :sword :slashing :piercing]
                                  :benefits {:quality {:name "Balance"
                                                       :value 1}
                                             :power {:name "Lethality"
                                                     :value 4}}
                                  :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be in the same zone as you. As a heavy weapon, it must be wielded in both hands. When partitioning the base dice pool, the resulting dice pools must have at least four dice in them. This weapon may deal piercing or slashing damage."}
                             105 {:id 105
                                  :edit-mode? false
                                  :typ :equipment
                                  :name "Handaxe"
                                  :properties [:light :axe :slashing :bludgeoning]
                                  :benefits {:quality {:name "Balance"
                                                       :value 2}
                                             :power {:name "Lethality"
                                                     :value 1}}
                                  :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be in the same zone as you. As a light weapon, it may be wielded in either hand. When partitioning the base dice pool, the resulting dice pools must have at least two dice in them. This weapon may deal slashing or bludgeoning damage."}
                             106 {:id 106
                                  :edit-mode? false
                                  :typ :equipment
                                  :name "Battle Axe"
                                  :properties [:medium :axe :slashing :bludgeoning]
                                  :benefits {:quality {:name "Balance"
                                                       :value 1}
                                             :power {:name "Lethality"
                                                     :value 3}}
                                  :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be in the same zone as you. As a medium weapon, it must be wielded in the dominant hand. When partitioning the base dice pool, the resulting dice pools must have at least three dice in them. This weapon may deal slashing or bludgeoning damage."}
                             107 {:id 107
                                  :edit-mode? false
                                  :typ :equipment
                                  :name "Greataxe"
                                  :properties [:heavy :axe :slashing :bludgeoning]
                                  :benefits {:quality {:name "Balance"
                                                       :value 0}
                                             :power {:name "Lethality"
                                                     :value 5}}
                                  :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be in the same zone as you. As a heavy weapon, it must be wielded in both hands. When partitioning the base dice pool, the resulting dice pools must have at least four dice in them. This weapon may deal slashing or bludgeoning damage."}
                             108 {:id 108
                                  :edit-mode? false
                                  :typ :equipment
                                  :name "Javelin"
                                  :properties [:light :spear :piercing]
                                  :benefits {:quality {:name "Balance"
                                                       :value 1}
                                             :power {:name "Lethality"
                                                     :value 3}}
                                  :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be in the same zone as you. As a light weapon, it may be wielded in either hand. When partitioning the base dice pool, the resulting dice pools must have at least two dice in them. This weapon deals piercing damage."}
                             109 {:id 109
                                  :edit-mode? false
                                  :typ :equipment
                                  :name "Spear"
                                  :properties [:medium :spear :piercing]
                                  :benefits {:quality {:name "Balance"
                                                       :value 2}
                                             :power {:name "Lethality"
                                                     :value 3}}
                                  :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be in the same zone as you. As a medium weapon, it must be wielded in the dominant hand. When partitioning the base dice pool, the resulting dice pools must have at least three dice in them. This weapon deals piercing damage."}
                             110 {:id 110
                                  :edit-mode? false
                                  :typ :equipment
                                  :name "Lance"
                                  :properties [:heavy :spear :piercing]
                                  :benefits {:quality {:name "Balance"
                                                       :value 3}
                                             :power {:name "Lethality"
                                                     :value 3}}
                                  :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be in the same zone as you. As a heavy weapon, it must be wielded in both hands. When partitioning the base dice pool, the resulting dice pools must have at least four dice in them. This weapon deals piercing damage."}
                             111 {:id 111
                                  :edit-mode? false
                                  :typ :equipment
                                  :name "Warhammer"
                                  :properties [:medium :hammer :bludgeoning]
                                  :benefits {:quality {:name "Balance"
                                                       :value 0}
                                             :power {:name "Lethality"
                                                     :value 5}}
                                  :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be in the same zone as you. As a medium weapon, it must be wielded in the dominant hand. When partitioning the base dice pool, the resulting dice pools must have at least three dice in them. This weapon deals bludgeoning damage."}
                             112 {:id 112
                                  :edit-mode? false
                                  :typ :equipment
                                  :name "Greathammer"
                                  :properties [:heavy :heavy :bludgeoning]
                                  :benefits {:quality {:name "Balance"
                                                       :value 0}
                                             :power {:name "Lethality"
                                                     :value 6}}
                                  :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be in the same zone as you. As a heavy weapon, it must be wielded in both hands. When partitioning the base dice pool, the resulting dice pools must have at least four dice in them. This weapon deals bludgeoning damage."}
                             113 {:id 113
                                  :edit-mode? false
                                  :typ :equipment
                                  :name "Hand Crossbow"
                                  :properties [:light :crossbow :piercing]
                                  :benefits {:quality {:name "Balance"
                                                       :value 1}
                                             :power {:name "Lethality"
                                                     :value 2}}
                                  :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be within 3 zones or 30 feet. As a light weapon, it may be wielded in either hand. When partitioning the base dice pool, the resulting dice pools must have at least two dice in them. This weapon deals piercing damage."}
                             114 {:id 114
                                  :edit-mode? false
                                  :typ :equipment
                                  :name "Crossbow"
                                  :properties [:medium :crossbow :piercing]
                                  :benefits {:quality {:name "Balance"
                                                       :value 1}
                                             :power {:name "Lethality"
                                                     :value 3}}
                                  :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be within 8 zones or 80 feet. As a medium crossbow, it must be wielded in both hands. When partitioning the base dice pool, the resulting dice pools must have at least three dice in them. This weapon deals piercing damage."}
                             115 {:id 115
                                  :edit-mode? false
                                  :typ :equipment
                                  :name "Shortbow"
                                  :properties [:light :bow :piercing]
                                  :benefits {:quality {:name "Balance"
                                                       :value 2}
                                             :power {:name "Lethality"
                                                     :value 1}}
                                  :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be within 5 zones or 50 feet. As a bow, it must be wielded in both hands. When partitioning the base dice pool, the resulting dice pools must have at least two dice in them. This weapon deals piercing damage."}
                             116 {:id 116
                                  :edit-mode? false
                                  :typ :equipment
                                  :name "Longbow"
                                  :properties [:medium :bow :piercing]
                                  :benefits {:quality {:name "Balance"
                                                       :value 3}
                                             :power {:name "Lethality"
                                                     :value 1}}
                                  :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be with 10 zones or 100 feet. As a bow it must be wielded in both hands. When partitioning the base dice pool, the resulting dice pools must have at least three dice in them. This weapon deals piercing damage."}
                             117 {:id 117
                                  :edit-mode? false
                                  :typ :equipment
                                  :name "Greatbow"
                                  :properties [:heavy :bow :piercing]
                                  :benefits {:quality {:name "Balance"
                                                       :value 2}
                                             :power {:name "Lethality"
                                                     :value 3}}
                                  :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be with 15 zones or 150 feet. As a bow, it must be wielded in both hands. When partitioning the base dice pool, the resulting dice pools must have at least four dice in them. This weapon deals piercing damage."}
                         ;;     Shields
                             118 {:id 118
                                  :edit-mode? false
                                  :typ :equipment
                                  :name "Buckler"
                                  :properties [:light :shield]
                                  :benefits {:quality {:name "Balance"
                                                       :value 3}
                                             :power {:name "Coverage"
                                                     :value 0}}
                                  :details "You may spend a main action to make a Reflexes +Balance +Coverage check to defend yourself against a creature's attack that targets you. When partitioning the base dice pool while defending yourself against physical attacks, the resulting dice pools must have at least two dice in them. When multiple items contribute to a defense check, the partitioned dice pools must follow the size limitations of every item involved. As a shield, it can be wielded in either hand."}
                             119 {:id 119
                                  :edit-mode? false
                                  :typ :equipment
                                  :name "Kite Shield"
                                  :properties [:medium :shield]
                                  :benefits {:quality {:name "Balance"
                                                       :value 2}
                                             :power {:name "Coverage"
                                                     :value 2}}
                                  :details "You may spend a main action to make a Reflexes +Balance +Coverage check to defend yourself against a creature's attack that targets you. When partitioning the base dice pool while defending yourself against physical attacks, the resulting dice pools must have at least three dice in them. When multiple items contribute to a defense check, the partitioned dice pools must follow the size limitations of every item involved. As a shield, it can be wielded in either hand."}
                             120 {:id 120
                                  :edit-mode? false
                                  :typ :equipment
                                  :name "Tower Shield"
                                  :properties [:heavy :shield]
                                  :benefits {:quality {:name "Balance"
                                                       :value 0}
                                             :power {:name "Coverage"
                                                     :value 5}}
                                  :details "You may spend a main action to make a Reflexes +Balance +Coverage check to defend yourself against a creature's attack that targets you. When partitioning the base dice pool while defending yourself against physical attacks, the resulting dice pools must have at least four dice in them. When multiple items contribute to a defense check, the partitioned dice pools must follow the size limitations of every item involved. As a shield, it can be wielded in either hand."}
                         ;;     Armor
                             121 {:id 121
                                  :edit-mode? false
                                  :typ :equipment
                                  :name "Cloth Armor"
                                  :properties [:light :padding]
                                  :benefits {:quality {:name "Flexibility"
                                                       :value 3}
                                             :power {:name "Durability"
                                                     :value 0}}
                                  :details "You may spend a main action to make a Reflexes +Quality +Power check to defend yourself against a creature's attack that targets you. When partitioning the base dice pool while defending yourself against physical attacks, the resulting dice pools must have at least two dice in them. When multiple items contribute to a defense check, the partitioned dice pools must follow the size limitations of every item involved."}
                             122 {:id 122
                                  :edit-mode? false
                                  :typ :equipment
                                  :name "Hide Armor"
                                  :properties [:medium :padding :bludgeoning]
                                  :benefits {:quality {:name "Flexibility"
                                                       :value 3}
                                             :power {:name "Durability"
                                                     :value 0}}
                                  :details "You may spend a main action to make a Reflexes +Quality +Power check to defend yourself against a creature's attack that targets you. When partitioning the base dice pool while defending yourself against physical attacks, the resulting dice pools must have at least three dice in them. When multiple items contribute to a defense check, the partitioned dice pools must follow the size limitations of every item involved. This armor reduces the severity of injuries inflicted by bludgeoning damage."}
                             123 {:id 123
                                  :edit-mode? false
                                  :typ :equipment
                                  :name "Leather Armor"
                                  :properties [:heavy :padding :bludgeoning :slashing]
                                  :benefits {:quality {:name "Flexibility"
                                                       :value 2}
                                             :power {:name "Durability"
                                                     :value 1}}
                                  :details "You may spend a main action to make a Reflexes +Quality +Power check to defend yourself against a creature's attack that targets you. When partitioning the base dice pool while defending yourself against physical attacks, the resulting dice pools must have at least four dice in them. When multiple items contribute to a defense check, the partitioned dice pools must follow the size limitations of every item involved. This armor reduces the severity of injuries inflicted by bludgeoning and slashing damage."}
                             124 {:id 124
                                  :edit-mode? false
                                  :typ :equipment
                                  :name "Chain Maille Vest"
                                  :properties [:light :maille :slashing]
                                  :benefits {:quality {:name "Flexibility"
                                                       :value 1}
                                             :power {:name "Durability"
                                                     :value 1}}
                                  :details "You may spend a main action to make a Reflexes +Quality +Power check to defend yourself against a creature's attack that targets you. When partitioning the base dice pool while defending yourself against physical attacks, the resulting dice pools must have at least two dice in them. When multiple items contribute to a defense check, the partitioned dice pools must follow the size limitations of every item involved. This armor reduces the severity of injuries inflicted by slashing damage."}
                             125 {:id 125
                                  :edit-mode? false
                                  :typ :equipment
                                  :name "Chain Maille Shirt"
                                  :properties [:medium :maille :slashing]
                                  :benefits {:quality {:name "Flexibility"
                                                       :value 1}
                                             :power {:name "Durability"
                                                     :value 2}}
                                  :details "You may spend a main action to make a Reflexes +Quality +Power check to defend yourself against a creature's attack that targets you. When partitioning the base dice pool while defending yourself against physical attacks, the resulting dice pools must have at least three dice in them. When multiple items contribute to a defense check, the partitioned dice pools must follow the size limitations of every item involved. This armor reduces the severity of injuries inflicted by slashing damage."}
                             126 {:id 126
                                  :edit-mode? false
                                  :typ :equipment
                                  :name "Riveted Chain Maille"
                                  :properties [:heavy :maille :slashing :piercing]
                                  :benefits {:quality {:name "Flexibility"
                                                       :value 1}
                                             :power {:name "Durability"
                                                     :value 2}}
                                  :details "You may spend a main action to make a Reflexes +Quality +Power check to defend yourself against a creature's attack that targets you. When partitioning the base dice pool while defending yourself against physical attacks, the resulting dice pools must have at least four dice in them. When multiple items contribute to a defense check, the partitioned dice pools must follow the size limitations of every item involved. This armor reduces the severity of injuries inflicted by slashing and piercing damage."}
                             127 {:id 127
                                  :edit-mode? false
                                  :typ :equipment
                                  :name "Leather Plate"
                                  :properties [:light :plate :slashing :piercing]
                                  :benefits {:quality {:name "Flexibility"
                                                       :value -1}
                                             :power {:name "Durability"
                                                     :value 2}}
                                  :details "You may spend a main action to make a Reflexes +Quality +Power check to defend yourself against a creature's attack that targets you. When partitioning the base dice pool while defending yourself against physical attacks, the resulting dice pools must have at least two dice in them. When multiple items contribute to a defense check, the partitioned dice pools must follow the size limitations of every item involved. This armor reduces the severity of injuries inflicted by slashing and piercing damage."}
                             128 {:id 128
                                  :edit-mode? false
                                  :typ :equipment
                                  :name "Steel Half-Plate"
                                  :properties [:medium :plate :slashing :piercing :bludgeoning]
                                  :benefits {:quality {:name "Flexibility"
                                                       :value -2}
                                             :power {:name "Durability"
                                                     :value 3}}
                                  :details "You may spend a main action to make a Reflexes +Quality +Power check to defend yourself against a creature's attack that targets you. When partitioning the base dice pool while defending yourself against physical attacks, the resulting dice pools must have at least three dice in them. When multiple items contribute to a defense check, the partitioned dice pools must follow the size limitations of every item involved. This armor reduces the severity of injuries inflicted by slashing, piercing, and bludgeoning damage."}
                             129 {:id 129
                                  :edit-mode? false
                                  :typ :equipment
                                  :name "Steel Full-Plate"
                                  :properties [:heavy :plate :slashing :piercing :bludgeoning]
                                  :benefits {:quality {:name "Flexibility"
                                                       :value -2}
                                             :power {:name "Durability"
                                                     :value 4}}
                                  :details "You may spend a main action to make a Reflexes +Quality +Power check to defend yourself against a creature's attack that targets you. When partitioning the base dice pool while defending yourself against physical attacks, the resulting dice pools must have at least four dice in them. When multiple items contribute to a defense check, the partitioned dice pools must follow the size limitations of every item involved. This armor reduces the severity of injuries inflicted by slashing, piercing, and bludgeoning damage."}

                             ;;     Traits
                             200 {:id 200
                                  :edit-mode? false
                                  :typ :trait
                                  :name "Example Ideal"
                                  :properties [:ideal]
                                  :benefits {:quality {:name "Purity"
                                                       :value 4}
                                             :power {:name "Passion"
                                                     :value -3}}
                                  :details "Trait Details"}
                             201 {:id 201
                                  :edit-mode? false
                                  :typ :trait
                                  :name "Example Purpose"
                                  :properties [:ideal]
                                  :benefits {:quality {:name "Purity"
                                                       :value 4}
                                             :power {:name "Passion"
                                                     :value -3}}
                                  :details "Trait Details"}
                             202 {:id 202
                                  :edit-mode? false
                                  :typ :trait
                                  :name "Example Goal"
                                  :properties [:ideal]
                                  :benefits {:quality {:name "Purity"
                                                       :value 4}
                                             :power {:name "Passion"
                                                     :value -3}}
                                  :details "Trait Details"}
                             203 {:id 203
                                  :edit-mode? false
                                  :typ :trait
                                  :name "Example Oath"
                                  :properties [:ideal]
                                  :benefits {:quality {:name "Purity"
                                                       :value 4}
                                             :power {:name "Passion"
                                                     :value -3}}
                                  :details "Trait Details"}




                             3 {:id 3
                                :edit-mode? false
                                :typ :expertise
                                :name "Example Expertise"
                                :properties [:education :cool :extra-properties]
                                :benefits {:quality {:name "Knowledge"
                                                     :value 1}
                                           :power {:name "Practice"
                                                   :value 1}}
                                :details "Expertise Details"}


                             4 {:id 4
                                :edit-mode? false
                                :typ :affiliations
                                :name "Example Affiliation"
                                :properties [:ally]
                                :benefits {:quality {:name "Reach"
                                                     :value 0}
                                           :power {:name "Influence"
                                                   :value 5}}
                                :details "Affiliation Details"}


                             5 {:id 5
                                :edit-mode? false
                                :typ :items
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






