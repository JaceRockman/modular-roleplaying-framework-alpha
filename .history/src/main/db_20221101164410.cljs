(ns main.db
  (:require
   [main.styles :as styles]
   [main.rules-section.data :as rules-data]
   [main.world-section.data :as world-data]))

;; initial state of app-db
(defonce app-db {:style-mode styles/dark-mode
                 :edit-mode false
                 :active-section 1
                 :sections {0 {:id 0
                               :name :rules
                               :active-tab :flow
                               :tabs [:flow :checks :encounters :damage :conditions]}
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
                                           :name "Blank Character"
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
                                 :resources {100 {:notes ""}}
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
                 :resources {100 {:id 100
                                  :type :equipment
                                  :name "Dagger"
                                  :properties [:light :sword :slashing :piercing]
                                  :benefits {:quality {:name "Balance"
                                                       :value 4}
                                             :power {:name "Lethality"
                                                     :value -1}}
                                  :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be in the same zone as you. As a light weapon, it may be wielded in either hand. This weapon may deal piercing or slashing damage."}
                             101 {:id 101
                                  :type :equipment
                                  :name "Short Sword"
                                  :properties [:light :sword :slashing :piercing]
                                  :benefits {:quality {:name "Balance"
                                                       :value 3}
                                             :power {:name "Lethality"
                                                     :value 0}}
                                  :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be in the same zone as you. As a light weapon, it may be wielded in either hand. This weapon may deal piercing or slashing damage."}
                             102 {:id 102
                                  :type :equipment
                                  :name "Rapier"
                                  :properties [:light :sword :piercing]
                                  :benefits {:quality {:name "Balance"
                                                       :value 3}
                                             :power {:name "Lethality"
                                                     :value 1}}
                                  :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be in the same zone as you. As a light weapon, it may be wielded in either hand. This weapon deals piercing damage."}
                             103 {:id 103
                                  :type :equipment
                                  :name "Longsword"
                                  :properties [:medium :sword :slashing :piercing]
                                  :benefits {:quality {:name "Balance"
                                                       :value 2}
                                             :power {:name "Lethality"
                                                     :value 2}}
                                  :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be in the same zone as you. As a medium weapon, it must be wielded in the dominant hand. This weapon may deal piercing or slashing damage."}
                             104 {:id 104
                                  :type :equipment
                                  :name "Greatsword"
                                  :properties [:heavy :sword :slashing :piercing]
                                  :benefits {:quality {:name "Balance"
                                                       :value 1}
                                             :power {:name "Lethality"
                                                     :value 4}}
                                  :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be in the same zone as you. As a heavy weapon, it must be wielded in both hands. This weapon may deal piercing or slashing damage."}
                             105 {:id 105
                                  :type :equipment
                                  :name "Handaxe"
                                  :properties [:light :axe :slashing :bludgeoning]
                                  :benefits {:quality {:name "Balance"
                                                       :value 2}
                                             :power {:name "Lethality"
                                                     :value 1}}
                                  :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be in the same zone as you. As a light weapon, it may be wielded in either hand. This weapon may deal slashing or bludgeoning damage."}
                             106 {:id 106
                                  :type :equipment
                                  :name "Battle Axe"
                                  :properties [:medium :axe :slashing :bludgeoning]
                                  :benefits {:quality {:name "Balance"
                                                       :value 1}
                                             :power {:name "Lethality"
                                                     :value 3}}
                                  :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be in the same zone as you. As a medium weapon, it must be wielded in the dominant hand. This weapon may deal slashing or bludgeoning damage."}
                             107 {:id 107
                                  :type :equipment
                                  :name "Greataxe"
                                  :properties [:heavy :axe :slashing :bludgeoning]
                                  :benefits {:quality {:name "Balance"
                                                       :value 1}
                                             :power {:name "Lethality"
                                                     :value 4}}
                                  :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be in the same zone as you. As a heavy weapon, it must be wielded in both hands. This weapon may deal slashing or bludgeoning damage."}
                             108 {:id 108
                                  :type :equipment
                                  :name "Javelin"
                                  :properties [:light :spear :piercing]
                                  :benefits {:quality {:name "Balance"
                                                       :value 1}
                                             :power {:name "Lethality"
                                                     :value 3}}
                                  :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be in the same zone as you. As a light weapon, it may be wielded in either hand. This weapon deals piercing damage."}
                             109 {:id 109
                                  :type :equipment
                                  :name "Spear"
                                  :properties [:medium :spear :piercing]
                                  :benefits {:quality {:name "Balance"
                                                       :value 2}
                                             :power {:name "Lethality"
                                                     :value 3}}
                                  :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be in the same zone as you. As a medium weapon, it must be wielded in the dominant hand. This weapon deals piercing damage."}
                             110 {:id 110
                                  :type :equipment
                                  :name "Lance"
                                  :properties [:heavy :spear :piercing]
                                  :benefits {:quality {:name "Balance"
                                                       :value 3}
                                             :power {:name "Lethality"
                                                     :value 3}}
                                  :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be in the same zone as you. As a heavy weapon, it must be wielded in both hands. This weapon deals piercing damage."}
                             111 {:id 111
                                  :type :equipment
                                  :name "Warhammer"
                                  :properties [:medium :hammer :bludgeoning]
                                  :benefits {:quality {:name "Balance"
                                                       :value 1}
                                             :power {:name "Lethality"
                                                     :value 4}}
                                  :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be in the same zone as you. As a medium weapon, it must be wielded in the dominant hand. This weapon deals bludgeoning damage."}
                             112 {:id 112
                                  :type :equipment
                                  :name "Greathammer"
                                  :properties [:heavy :heavy :bludgeoning]
                                  :benefits {:quality {:name "Balance"
                                                       :value 2}
                                             :power {:name "Lethality"
                                                     :value 4}}
                                  :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be in the same zone as you. As a heavy weapon, it must be wielded in both hands. This weapon deals bludgeoning damage."}
                             113 {:id 113
                                  :type :equipment
                                  :name "Hand Crossbow"
                                  :properties [:light :crossbow :piercing]
                                  :benefits {:quality {:name "Balance"
                                                       :value 1}
                                             :power {:name "Lethality"
                                                     :value 2}}
                                  :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be within 3 zones or 30 feet. As a light weapon, it may be wielded in either hand. This weapon deals piercing damage."}
                             114 {:id 114
                                  :type :equipment
                                  :name "Crossbow"
                                  :properties [:medium :crossbow :piercing]
                                  :benefits {:quality {:name "Balance"
                                                       :value 1}
                                             :power {:name "Lethality"
                                                     :value 3}}
                                  :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be within 8 zones or 80 feet. As a medium crossbow, it must be wielded in both hands. This weapon deals piercing damage."}
                             115 {:id 115
                                  :type :equipment
                                  :name "Shortbow"
                                  :properties [:light :bow :piercing]
                                  :benefits {:quality {:name "Balance"
                                                       :value 2}
                                             :power {:name "Lethality"
                                                     :value 1}}
                                  :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be within 5 zones or 50 feet. As a bow, it must be wielded in both hands. This weapon deals piercing damage."}
                             116 {:id 116
                                  :type :equipment
                                  :name "Longbow"
                                  :properties [:medium :bow :piercing]
                                  :benefits {:quality {:name "Balance"
                                                       :value 3}
                                             :power {:name "Lethality"
                                                     :value 1}}
                                  :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be with 10 zones or 100 feet. As a bow it must be wielded in both hands. This weapon deals piercing damage."}
                             117 {:id 117
                                  :type :equipment
                                  :name "Greatbow"
                                  :properties [:heavy :bow :piercing]
                                  :benefits {:quality {:name "Balance"
                                                       :value 2}
                                             :power {:name "Lethality"
                                                     :value 3}}
                                  :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be with 15 zones or 150 feet. As a bow, it must be wielded in both hands. This weapon deals piercing damage."}
                             118 {:id 118
                                  :type :equipment
                                  :name "Buckler"
                                  :properties [:light :shield]
                                  :benefits {:quality {:name "Balance"
                                                       :value 3}
                                             :power {:name "Coverage"
                                                     :value 0}}
                                  :details "You may spend a main action to make a Reflexes +Balance +Coverage check to defend yourself against a creature's attack that targets you. As a shield, it can be wielded in either hand."}
                             119 {:id 119
                                  :type :equipment
                                  :name "Kite Shield"
                                  :properties [:medium :shield]
                                  :benefits {:quality {:name "Balance"
                                                       :value 2}
                                             :power {:name "Coverage"
                                                     :value 2}}
                                  :details "You may spend a main action to make a Reflexes +Balance +Coverage check to defend yourself against a creature's attack that targets you. As a shield, it can be wielded in either hand."}
                             120 {:id 120
                                  :type :equipment
                                  :name "Tower Shield"
                                  :properties [:heavy :shield]
                                  :benefits {:quality {:name "Balance"
                                                       :value 2}
                                             :power {:name "Coverage"
                                                     :value 3}}
                                  :details "You may spend a main action to make a Reflexes +Balance +Coverage check to defend yourself against a creature's attack that targets you. As a shield, it can be wielded in either hand."}
                             121 {:id 121
                                  :type :equipment
                                  :name "Cloth Armor"
                                  :properties [:light :padding]
                                  :benefits {:quality {:name "Flexibility"
                                                       :value 3}
                                             :power {:name "Durability"
                                                     :value 0}}
                                  :details "You may spend a main action to make a Reflexes +Quality +Power check to defend yourself against a creature's attack that targets you."}
                             122 {:id 122
                                  :type :equipment
                                  :name "Hide Armor"
                                  :properties [:medium :padding :bludgeoning]
                                  :benefits {:quality {:name "Flexibility"
                                                       :value 3}
                                             :power {:name "Durability"
                                                     :value 0}}
                                  :details "You may spend a main action to make a Reflexes +Quality +Power check to defend yourself against a creature's attack that targets you. This armor reduces the severity of injuries inflicted by bludgeoning damage."}
                             123 {:id 123
                                  :type :equipment
                                  :name "Leather Armor"
                                  :properties [:heavy :padding :bludgeoning :slashing]
                                  :benefits {:quality {:name "Flexibility"
                                                       :value 2}
                                             :power {:name "Durability"
                                                     :value 1}}
                                  :details "You may spend a main action to make a Reflexes +Quality +Power check to defend yourself against a creature's attack that targets you. This armor reduces the severity of injuries inflicted by bludgeoning and slashing damage."}
                             124 {:id 124
                                  :type :equipment
                                  :name "Chain Maille Vest"
                                  :properties [:light :maille :slashing]
                                  :benefits {:quality {:name "Flexibility"
                                                       :value 1}
                                             :power {:name "Durability"
                                                     :value 1}}
                                  :details "You may spend a main action to make a Reflexes +Quality +Power check to defend yourself against a creature's attack that targets you. This armor reduces the severity of injuries inflicted by slashing damage."}
                             125 {:id 125
                                  :type :equipment
                                  :name "Chain Maille Shirt"
                                  :properties [:medium :maille :slashing]
                                  :benefits {:quality {:name "Flexibility"
                                                       :value 1}
                                             :power {:name "Durability"
                                                     :value 2}}
                                  :details "You may spend a main action to make a Reflexes +Quality +Power check to defend yourself against a creature's attack that targets you. This armor reduces the severity of injuries inflicted by slashing damage."}
                             126 {:id 126
                                  :type :equipment
                                  :name "Riveted Chain Maille"
                                  :properties [:heavy :maille :slashing :piercing]
                                  :benefits {:quality {:name "Flexibility"
                                                       :value 1}
                                             :power {:name "Durability"
                                                     :value 2}}
                                  :details "You may spend a main action to make a Reflexes +Quality +Power check to defend yourself against a creature's attack that targets you. This armor reduces the severity of injuries inflicted by slashing and piercing damage."}
                             127 {:id 127
                                  :type :equipment
                                  :name "Leather Plate"
                                  :properties [:light :plate :slashing :piercing]
                                  :benefits {:quality {:name "Flexibility"
                                                       :value -1}
                                             :power {:name "Durability"
                                                     :value 2}}
                                  :details "You may spend a main action to make a Reflexes +Quality +Power check to defend yourself against a creature's attack that targets you. This armor reduces the severity of injuries inflicted by slashing and piercing damage."}
                             128 {:id 128
                                  :type :equipment
                                  :name "Steel Half-Plate"
                                  :properties [:medium :plate :slashing :piercing :bludgeoning]
                                  :benefits {:quality {:name "Flexibility"
                                                       :value -2}
                                             :power {:name "Durability"
                                                     :value 3}}
                                  :details "You may spend a main action to make a Reflexes +Quality +Power check to defend yourself against a creature's attack that targets you. This armor reduces the severity of injuries inflicted by slashing, piercing, and bludgeoning damage."}
                             129 {:id 129
                                  :type :equipment
                                  :name "Steel Full-Plate"
                                  :properties [:heavy :plate :slashing :piercing :bludgeoning]
                                  :benefits {:quality {:name "Flexibility"
                                                       :value -2}
                                             :power {:name "Durability"
                                                     :value 4}}
                                  :details "You may spend a main action to make a Reflexes +Quality +Power check to defend yourself against a creature's attack that targets you. This armor reduces the severity of injuries inflicted by slashing, piercing, and bludgeoning damage."}
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
                                  :type :equipment
                                  :name "Gauntlet of Heilm"
                                  :properties [:relic]
                                  :benefits {:quality {:name "Quality"
                                                       :value 2}
                                             :power {:name "Power"
                                                     :value 2}}
                                  :details "A pair of gauntlets said to be molded upon the Fist of Heilm. While worn, gain +Quality +Power on checks to maintain a firm grasp. Great for climbing, grappling, and building, among other things."}
                             132 {:id 132
                                  :type :equipment
                                  :name "Fragrance of Ijarda"
                                  :properties [:relic]
                                  :benefits {:quality {:name "Quality"
                                                       :value 3}
                                             :power {:name "Power"
                                                     :value 1}}
                                  :details "A warm fragrance said to be the remnants of the Breath of Ijarda that was once held in this very bottle. When a creature smells the contents of this bottle, they gain +Quality +Power on their next Physical, Spiritual, or Mental Health Check."}
                             133 {:id 133
                                  :type :equipment
                                  :name "Coin of Aldiff"
                                  :properties [:artifact]
                                  :benefits {:quality {:name "Quality"
                                                       :value 1}
                                             :power {:name "Power"
                                                     :value 3}}
                                  :details "Target a creature within line of sight and flip this coin into the air to change places with that creature. If the creature is unwilling, then gain +Quality +Power on an Exertion check contested by their Instinct check. The coin falls at your feet whether or not the switch was successful."}
                             134 {:id 134
                                  :type :equipment
                                  :name "Arcanist's Quill"
                                  :properties [:artifact]
                                  :benefits {:quality {:name "Quality"
                                                       :value 2}
                                             :power {:name "Power"
                                                     :value 2}}
                                  :details "Gain +Quality +Power when inscribing glyphs with the Arcanist's Quill."}
                             135 {:id 135
                                  :type :equipment
                                  :name "Gravitas Staff"
                                  :properties [:artifact]
                                  :benefits {:quality {:name "Quality"
                                                       :value 1}
                                             :power {:name "Power"
                                                     :value 3}}
                                  :details "The Gravitas Staff is able to move objects telekinetically. Gain +Quality +Power on these Exertion checks. The target number on these checks increases as the weight and distance increase."}
                             136 {:id 136
                                  :type :equipment
                                  :name "Spores of Myconis"
                                  :properties [:totem]
                                  :benefits {:quality {:name "Quality"
                                                       :value 2}
                                             :power {:name "Power"
                                                     :value 2}}
                                  :details "Once per day, you can inhale the spores contained in this terrarium to gain the ability to communicate with plant life for 10 minutes. Gain +Quality +Power on checks related to handling plants while you are in this state."}
                             137 {:id 137
                                  :type :equipment
                                  :name "Tooth of Draconis"
                                  :properties [:totem :draconic]
                                  :benefits {:quality {:name "Quality"
                                                       :value 0}
                                             :power {:name "Power"
                                                     :value 4}}
                                  :details "Once per day, gain +Quality +Power on an Exertion check targeted on an adjacent zone to breath fire. All creatures in the zone are hit by the attack and must make defense checks to avoid taking damage."}
                             138 {:id 138
                                   :type :equipment
                                   :name "Shard of Primordial Flame"
                                   :properties [:totem :elemental]
                                   :benefits {:quality {:name "Quality"
                                                        :value 2}
                                              :power {:name "Power"
                                                      :value 2}}
                                   :details "A wand-shaped fragment of the primordial manifestation of fire that is warm to the touch. Known equally for its life-giving and destructive properties, this shard has two uses. Gain +Quality +Power on an Exertion check to throw a fireball at a targeted creature or object. Alternatively, if this shard is used to spark the flame of a campfire or fireplace, then make a Perseverance check and over the lifetime of that fire, it can heal a number of physical minor wounds equal to the result of your check."}
                             139 {:id 139
                                   :type :equipment
                                   :name "Shard of Primordial Air"
                                   :properties [:totem :elemental]
                                   :benefits {:quality {:name "Quality"
                                                        :value 2}
                                              :power {:name "Power"
                                                      :value 2}}
                                   :details "An orb-shaped fragment of the primordial manifestation of air. Grants +Quality +Power on an Instinct check to increase jump height/distance by double or decrease fall speed by half. The target number for this check is equal to 4 + the number of creatures that you are attempting to affect."}
                             140 {:id 140
                                   :type :equipment
                                   :name "Shard of Primordial Water"
                                   :properties [:totem :elemental]
                                   :benefits {:quality {:name "Quality"
                                                        :value 2}
                                              :power {:name "Power"
                                                      :value 2}}
                                   :details "A bowl-shaped fragment of the primordial element of water that is cool to the touch. Once per day, fill the fragment with water and gain +Quality +Power on an Instinct check while thinking of an objective where the target number is determined by the complexity of that objective. If successful, the poured water will flow to point in the direction of whatever would be most helpful in accomplishing your objective."}
                             141 {:id 141
                                   :type :equipment
                                   :name "Shard of Primordial Stone"
                                   :properties [:totem :elemental]
                                   :benefits {:quality {:name "Quality"
                                                        :value 0}
                                              :power {:name "Power"
                                                      :value 0}}
                                   :details "A shield-shaped fragment of the primordial element of stone. Once per day, gain +Quality +Power on a Perseverance check on a target creature in line of sight as a reaction to that creature being targeted by an attack. The result of the Perseverance check is granted as a flat bonus on the target creature's defense check."}
                             142 {:id 142
                                  :type :equipment
                                  :name "Tattoo of (Damage Type) Protection"
                                  :properties [:talisman]
                                  :benefits {:quality {:name "Quality"
                                                       :value 1}
                                             :power {:name "Power"
                                                     :value 1}}
                                  :details "Once per day, gain +Quality +Power on a defense check against an attack that deals (Damage Type) damage."}
                             143 {:id 143
                                  :type :equipment
                                  :name "Ring of Truth"
                                  :properties [:talisman]
                                  :benefits {:quality {:name "Quality"
                                                       :value 0}
                                             :power {:name "Power"
                                                     :value 0}}
                                  :details "Once per day, you may gain +Quality +Power on any Insight check."}
                             144 {:id 144
                                  :type :equipment
                                  :name "Amulet of (Domain)"
                                  :properties [:talisman]
                                  :benefits {:quality {:name "Quality"
                                                       :value 2}
                                             :power {:name "Power"
                                                     :value 2}}
                                  :details "Once per day, gain +Quality +Power on any (Domain) health check."}

                             145 {:id 145
                                  :type :equipment
                                  :name "Climber’s Tools"
                                  :properties [:tool]
                                  :benefits {:quality {:name "Quality"
                                                       :value 0}
                                             :power {:name "Power"
                                                     :value 0}}
                                  :details "Gain +Quality +Power on checks that use these tools to climb a surface."}
                             146 {:id 146
                                  :type :equipment
                                  :name "Forgery Kit"
                                  :properties [:tool]
                                  :benefits {:quality {:name "Quality"
                                                       :value 0}
                                             :power {:name "Power"
                                                     :value 0}}
                                  :details "Gain +Quality +Power on checks that use these tools to duplicate text."}
                             147 {:id 147
                                  :type :equipment
                                  :name "Lockpick Set"
                                  :properties [:tool]
                                  :benefits {:quality {:name "Quality"
                                                       :value 0}
                                             :power {:name "Power"
                                                     :value 0}}
                                  :details "Gain +Quality +Power on checks that use these tools to open locks or disable traps."}
                             148 {:id 148
                                  :type :equipment
                                  :name "Reference Document"
                                  :properties [:tool]
                                  :benefits {:quality {:name "Quality"
                                                       :value 0}
                                             :power {:name "Power"
                                                     :value 0}}
                                  :details "Gain +Quality +Power on checks that use these documents to recall information related to the topic of these documents."}
                             149 {:id 149
                                  :type :equipment
                                  :name "Navigator's Tools"
                                  :properties [:tool]
                                  :benefits {:quality {:name "Quality"
                                                       :value 0}
                                             :power {:name "Power"
                                                     :value 0}}
                                  :details "Gain +Quality +Power on checks that use these tools to determine your current location and orientation."}
                             150 {:id 150
                                  :type :equipment
                                  :name "Tinkerer’s Tools"
                                  :properties [:tool]
                                  :benefits {:quality {:name "Quality"
                                                       :value 0}
                                             :power {:name "Power"
                                                     :value 0}}
                                  :details "Gain +Quality +Power on checks that use these tools to create small mechanisms and trinkets"}
                             151 {:id 151
                                  :type :equipment
                                  :name "Alchemist’s Tools"
                                  :properties [:tool]
                                  :benefits {:quality {:name "Quality"
                                                       :value 0}
                                             :power {:name "Power"
                                                     :value 0}}
                                  :details "Gain +Quality +Power on checks that use these tools to brew alchemical mixtures."}
                             152 {:id 152
                                  :type :equipment
                                  :name "Musical Instrument"
                                  :properties [:tool]
                                  :benefits {:quality {:name "Quality"
                                                       :value 0}
                                             :power {:name "Power"
                                                     :value 0}}
                                  :details "Gain +Quality +Power on checks that use this instrument in a musical performance."}
                             153 {:id 153
                                  :type :equipment
                                  :name "Cartographer’s Tools"
                                  :properties [:tool]
                                  :benefits {:quality {:name "Quality"
                                                       :value 0}
                                             :power {:name "Power"
                                                     :value 0}}
                                  :details "Gain +Quality +Power on checks that use these tools to measure and draw maps."}
                             154 {:id 154
                                  :type :equipment
                                  :name "Painter’s Supplies"
                                  :properties [:tool]
                                  :benefits {:quality {:name "Quality"
                                                       :value 0}
                                             :power {:name "Power"
                                                     :value 0}}
                                  :details "Gain +Quality +Power on checks that use these tools to create visual art."}
                             155 {:id 155
                                  :type :equipment
                                  :name "Herbalism Kit"
                                  :properties [:tool]
                                  :benefits {:quality {:name "Quality"
                                                       :value 0}
                                             :power {:name "Power"
                                                     :value 0}}
                                  :details "Gain +Quality +Power on checks that use these tools to create medicines."}
                             156 {:id 156
                                  :type :equipment
                                  :name "Disguise Kit"
                                  :properties [:tool]
                                  :benefits {:quality {:name "Quality"
                                                       :value 0}
                                             :power {:name "Power"
                                                     :value 0}}
                                  :details "Gain +Quality +Power on checks that use these tools to disguise your appearance"}

                             200 {:id 200
                                  :type :traits
                                  :name "Ideal Template"
                                  :properties [:ideal]
                                  :benefits {:quality {:name "Clarity"
                                                       :value 0}
                                             :power {:name "Dedication"
                                                     :value 0}}
                                  :details "Grants +Quality +Power to checks that promote or exemplify your ideal. Checks that run counter to your ideal gain penalties equal to the Quality and Power of your ideal. Circumstances may cause multiple traits to apply bonuses or penalties to a check. Ultimately, it is up to the DM's discretion whether a trait grants a bonus or penalty. Some examples of ideals are courage, life, and freedom."}
                             201 {:id 201
                                  :type :traits
                                  :name "Purpose Template"
                                  :properties [:purpose]
                                  :benefits {:quality {:name "Clarity"
                                                       :value 0}
                                             :power {:name "Dedication"
                                                     :value 0}}
                                  :details "Grants +Quality +Power to checks that fulfill your purpose. Checks that contradict your purpose gain penalties equal to the Quality and Power of your purpose. Circumstances may cause multiple traits to apply bonuses or penalties to a check. Ultimately, it is up to the DM's discretion whether a trait grants a bonus or penalty. Some examples of purposes would be to protect something important to you or to serve a deity or power."}
                             202 {:id 202
                                  :type :traits
                                  :name "Goal Template"
                                  :properties [:goal]
                                  :benefits {:quality {:name "Clarity"
                                                       :value 0}
                                             :power {:name "Dedication"
                                                     :value 0}}
                                  :details "Grants +Quality +Power to checks that move you towards accomplishing your goal Checks that set you back with respect to your goal gain penalties equal to the Quality and Power of your goal. Circumstances may cause multiple traits to apply bonuses or penalties to a check. Ultimately, it is up to the DM's discretion whether a trait grants a bonus or penalty. Some examples of goals are to discover forgotten magics, wealth, or to be granted knighthood."}
                             203 {:id 203
                                  :type :traits
                                  :name "Oath Template"
                                  :properties [:oath]
                                  :benefits {:quality {:name "Clarity"
                                                       :value 0}
                                             :power {:name "Dedication"
                                                     :value 0}}
                                  :details "Grants +Quality +Power to checks that are aligned with your oath. Checks that are misaligned with your oath gain penalties equal to the Quality and Power of your oath. Circumstances may cause multiple traits to apply bonuses or penalties to a check. Ultimately, it is up to the DM's discretion whether a trait grants a bonus or penalty. Some examples of oaths are to uphold the virtues of Heilm, an oath of revenge, or an oath to serve the crown."}

                             300 {:id 300
                                  :type :expertise
                                  :name "Scout"
                                  :properties [:training :fighting-style]
                                  :benefits {:quality {:name "Knowledge"
                                                       :value 3}
                                             :power {:name "Practice"
                                                     :value 1}}
                                  :details "Gain +Quality +Power on checks that use a light weapon, shield, or armor. Gain +Quality +Power on checks to move stealthily and to cover your tracks and on checks to contest another creature moving stealthily or covering their tracks."}
                             301 {:id 301
                                  :type :expertise
                                  :name "Soldier"
                                  :properties [:training :fighting-style]
                                  :benefits {:quality {:name "Knowledge"
                                                       :value 2}
                                             :power {:name "Practice"
                                                     :value 2}}
                                  :details "Gain +Quality +Power on checks that use a medium weapon, shield, or armor. Additionally, when you make a melee attack against a targeted creature, you can choose to make one of the following combat maneuvers instead: trip, shove, or disarm. If your trip attack is successful, the target falls prone. If the shove is successful, the target is moved to an adjacent zone. If the disarm is successful, the target's weapon falls to the ground in the zone that they occupy."}
                             302 {:id 302
                                  :type :expertise
                                  :name "Sentinel"
                                  :properties [:training :fighting-style]
                                  :benefits {:quality {:name "Knowledge"
                                                       :value 1}
                                             :power {:name "Practice"
                                                     :value 3}}
                                  :details "Gain +Quality +Power on checks that use a heavy weapon, shield, or armor. Additionally, if a target creature within the same zone as you is attacked and you are wielding a heavy shield, you can make a defense check against that attack. The result of your defense check is inflicted as a flat penalty on the attack roll. The target of the attack may still make their own defense check if needed."}
                             303 {:id 303
                                  :type :expertise
                                  :name "Arcanist"
                                  :properties [:education :aether-manipulation]
                                  :benefits {:quality {:name "Knowledge"
                                                       :value 3}
                                             :power {:name "Practice"
                                                     :value 1}}
                                  :details "Gain +Quality +Power on checks that use Artifacts. Additionally, you have been taught the three fundamental glyph patterns: Connection, Imbuement, and Transmutation. These are the underlying patterns to most glyphs used by Arcanists. This allows you to acquire glyphs as additional resources."}
                             304 {:id 304
                                  :type :expertise
                                  :name "Glyph of Alarm"
                                  :properties [:glyph :connection]
                                  :benefits {:quality {:name "Knowledge"
                                                       :value 0}
                                             :power {:name "Practice"
                                                     :value 0}}
                                  :details "Inscribing this Glyph causes an alarm to be raised when certain creatures enter its given proximity. During the inscription process, any creature that touches the Glyph will be marked and the Glyph can be set to be triggered only by marked creatures or to be triggered only by unmarked creatures. For every 10 feet of proximity covered by this Glyph, the target number on the Exertion check for correctly inscribing it increases by 1. For example, a 30 foot radius alarm would have a target number of 3. This check should be done by the GM in secret. Once triggered, the Glyph will need to be recharged with and Exertion check in order to be reused and the target number for charging it is equal to the target number required to initially inscribe it."}
                             305 {:id 305
                                  :type :expertise
                                  :name "Glyph of Communication"
                                  :properties [:glyph :connection]
                                  :benefits {:quality {:name "Knowledge"
                                                       :value 0}
                                             :power {:name "Practice"
                                                     :value 0}}
                                  :details "Inscribing this Glyph on a pair of similar objects allows any creature that speaks the command word to transmit sound through these objects regardless of the objects' distance from one another. The target number on the Exertion check to correctly inscribe this Glyph is equal the desired duration of transmission in seconds. This check should be done by the GM in secret. Once the Glyph has been used, it can be recharged with another Exertion check with a target number equal to the target number required to initially inscribe it."}
                             306 {:id 306
                                  :type :expertise
                                  :name "Glyph of (Stat) Enhancement"
                                  :properties [:glyph :imbuement]
                                  :benefits {:quality {:name "Knowledge"
                                                       :value 0}
                                             :power {:name "Practice"
                                                     :value 0}}
                                  :details "(Select a stat when gaining this Glyph) Inscribing this Glyph on a willing creature gives that creature the ability to expend the Glyph to gain a flat bonus on a (Stat) check. The target number for the Exertion check to correctly inscribe this Glyph is equal to two times the intended flat bonus. This check should be done by the GM in secret. If the inscription process is unsuccessful, then a flat penalty is instead inflicted on the (Stat) check. This Glyph cannot be recharged."}
                             307 {:id 307
                                  :type :expertise
                                  :name "Glyph of Dark Vision"
                                  :properties [:glyph :imbuement]
                                  :benefits {:quality {:name "Knowledge"
                                                       :value 0}
                                             :power {:name "Practice"
                                                     :value 0}}
                                  :details "Inscribing this Glyph on a willing creature gives that creature the ability to see in the dark as if it were dim light. The target number on the Exertion check to correctly inscribe this Glyph increases by one for every ten minutes that it is intended to last for. If the inscription process is unsuccessful, then the creature is instead blinded for the inteded duration of the Glyph's effects."}
                             308 {:id 308
                                  :type :expertise
                                  :name "Glyph of Pure Water"
                                  :properties [:glyph :transmutation]
                                  :benefits {:quality {:name "Knowledge"
                                                       :value 0}
                                             :power {:name "Practice"
                                                     :value 0}}
                                  :details "Inscribing this Glyph on a gallon-sized container of liquid will transmute that liquid into pure drinkable water. This Glyph does not require an Exertion check to inscribe."}
                             309 {:id 309
                                  :type :expertise
                                  :name "Glyph of Stoneskin"
                                  :properties [:glyph :imbuement]
                                  :benefits {:quality {:name "Knowledge"
                                                       :value 0}
                                             :power {:name "Practice"
                                                     :value 0}}
                                  :details "Inscribing this Glyph on a willing creature transmutes that creature's skin to be as tough as stone. The target number on the Exertion check to correctly inscribe this Glyph increases by one for every 10 seconds that it is intended to last for. If the inscription process is unsuccessful, then the creature is instead inflicted with a minor physical wound."}
                             310 {:id 310
                                  :type :expertise
                                  :name "Druid"
                                  :properties [:experience :aether-manipulation]
                                  :benefits {:quality {:name "Knowledge"
                                                       :value 2}
                                             :power {:name "Practice"
                                                     :value 2}}
                                  :details "Gain +Quality +Power on checks that use Totems. Additionally, you can make an Instinct +Quality +Power check to communicate roughly with plants and animals. The more distant the relation between the plant or animal and your race, the higher the target number becomes."}
                             311 {:id 311
                                  :type :expertise
                                  :name "Invoker"
                                  :properties [:training :aether-manipulation]
                                  :benefits {:quality {:name "Knowledge"
                                                       :value 1}
                                             :power {:name "Practice"
                                                     :value 3}}
                                  :details "Gain +Quality +Power on checks that use Relics. Additionally, you have a pledge or contract with some deity or power that you channel. Once per day, you may make an Exertion +Quality +Power check to commune with whatever deity or power you are bound to and request access to an invocation. The invocations available to you will depend on the deity or power you are bound to."}
                             312 {:id 312
                                  :type :expertise
                                  :name "Education in (Topic)"
                                  :properties [:education]
                                  :benefits {:quality {:name "Knowledge"
                                                       :value 3}
                                             :power {:name "Practice"
                                                     :value 1}}
                                  :details "Gain +Quality +Power on checks to recall information related to (Topic). Education in a given topic is a common prerequisite for other resources."}
                             313 {:id 313
                                  :type :expertise
                                  :name "Researcher of (Topic)"
                                  :properties [:experience]
                                  :benefits {:quality {:name "Knowledge"
                                                       :value 1}
                                             :power {:name "Practice"
                                                     :value 3}}
                                  :details "Gain +Quality +Power on checks to gather information about (Topic). This can be through multiple means such as experimentation, locating sources of information, or interpreting those sources of information. If a check to gain information fails, you will at least gain some idea of where you need to go in order to gain the information."}
                             314 {:id 314
                                  :type :expertise
                                  :name "Noble"
                                  :properties [:training]
                                  :benefits {:quality {:name "Knowledge"
                                                       :value 3}
                                             :power {:name "Practice"
                                                     :value 1}}
                                  :details "Gain +Quality +Power on checks related to passing as well-mannered nobility and to identify the social faux paus of others trying to come across as well-mannered nobility. This includes knowledge of social decorum, family ties, and any relevant political tensions."}
                             315 {:id 315
                                  :type :expertise
                                  :name "Storyteller"
                                  :properties [:experience]
                                  :benefits {:quality {:name "Knowledge"
                                                       :value 2}
                                             :power {:name "Practice"
                                                     :value 2}}
                                  :details "Gain +Quality +Power on checks to convince others that you know what you're talking about as well as on checks to identify when someone is trying to deceive you. This could be because you have actually come across this knowledge in your time collecting interesting stories and rumors, or this could be because you have extensive practice weaving the threads of a story in an enthralling and believable way."}
                             316 {:id 316
                                  :type :expertise
                                  :name "Leader"
                                  :properties [:experience]
                                  :benefits {:quality {:name "Knowledge"
                                                       :value 1}
                                             :power {:name "Practice"
                                                     :value 3}}
                                  :details "Gain +Quality +Power on checks to convince or inspire others to do what you want them to do as well as on checks to determine if others are being truthful with you."}

                             400 {:id 400
                                  :type :affiliations
                                  :name "Individual Template"
                                  :properties [:individual]
                                  :benefits {:quality {:name "Reach"
                                                       :value 0}
                                             :power {:name "Influence"
                                                     :value 0}}
                                  :details "Gain +Quality +Power on social checks while interacting with this individual. The Game Master may give partial bonuses on social checks where this individual is a mutual acquaintance. Examples of individuals that you may be affiliated with are allies, friends, mentors, or family."}
                             401 {:id 401
                                  :type :affiliations
                                  :name "Group Template"
                                  :properties [:individual]
                                  :benefits {:quality {:name "Reach"
                                                       :value 0}
                                             :power {:name "Influence"
                                                     :value 0}}
                                  :details "Gain +Quality +Power on social checks while interacting with this group. The Game Master may give partial bonuses on social checks where this group is a relevant. Examples of groups that you may be affiliated with are guilds, religious orders, schools, or communities."}
                             402 {:id 402
                                  :type :affiliations
                                  :name "Acknowledgements Template"
                                  :properties [:individual]
                                  :benefits {:quality {:name "Reach"
                                                       :value 0}
                                             :power {:name "Influence"
                                                     :value 0}}
                                  :details "Gain +Quality +Power on social checks while interacting with individuals who are familiar with your acknowledgements. The Game Master may give partial bonuses based on how familiar the individual is with your acknowledgements. Examples of acknowledgements that may be affiliated with you are reputations, titles, degrees or certifications, and rumors."}

                             500 {:id 500
                                  :type :items
                                  :name "Acid"
                                  :properties [:item]
                                  :benefits {:quality {:name "Quality"
                                                       :value 1}
                                             :power {:name "Power"
                                                     :value 1}}
                                  :details "Gain +Quality +Power on a Coordination check to throw this bottle at a target creature. If the attack is successful, then inflict a physical minor wound with acid damage."}
                             501 {:id 501
                                  :type :items
                                  :name "Alchemist's Fire"
                                  :properties [:item]
                                  :benefits {:quality {:name "Quality"
                                                       :value 1}
                                             :power {:name "Power"
                                                     :value 2}}
                                  :details "Gain +Quality +Power on a Coordination check to throw this bottle at a target creature. If the attack is successful, then inflict a physical minor wound with fire damage."}
                             502 {:id 502
                                  :type :items
                                  :name "Antitoxin"
                                  :properties [:item]
                                  :benefits {:quality {:name "Quality"
                                                       :value 1}
                                             :power {:name "Power"
                                                     :value 2}}
                                  :details "Gain +Quality +Power on your next health checks to remove the poisoned condition."}
                             503 {:id 503
                                  :type :items
                                  :name "Lantern"
                                  :properties [:item]
                                  :benefits {:quality {:name "Quality"
                                                       :value 0}
                                             :power {:name "Power"
                                                     :value 0}}
                                  :details "Casts light within a 20 foot radius. Nullify any penalties from low light in that radius. A flask of oil keeps the lantern running for 6 hours."}
                             504 {:id 504
                                  :type :items
                                  :name "Torch"
                                  :properties [:item]
                                  :benefits {:quality {:name "Quality"
                                                       :value 0}
                                             :power {:name "Power"
                                                     :value 0}}
                                  :details "Casts light within a 30 foot radius for the next hour. Nullify any penalties from low light in that radius."}
                             505 {:id 505
                                  :type :items
                                  :name "Lock"
                                  :properties [:item]
                                  :benefits {:quality {:name "Quality"
                                                       :value 0}
                                             :power {:name "Power"
                                                     :value 0}}
                                  :details "A metal lock that can be broken with a physical attack result of 10 or can be picked with a Coordination check using theives' tools and a target number of 7."}
                             507 {:id 507
                                  :type :items
                                  :name "Manacles"
                                  :properties [:item]
                                  :benefits {:quality {:name "Quality"
                                                       :value 0}
                                             :power {:name "Power"
                                                     :value 0}}
                                  :details "Metal manacles that can be be placed on a creature to give it the restrained condition that inflicts a -3d penalty on checks that require the use of your hands or feet, whichever is restrained. These can be broken with a physical attack result of 10 or can be picked with a Coordination check using theives' tools and a target number of 7."}
                             509 {:id 509
                                  :type :items
                                  :name "Poison"
                                  :properties [:item]
                                  :benefits {:quality {:name "Quality"
                                                       :value 0}
                                             :power {:name "Power"
                                                     :value 0}}
                                  :details "When ingested, the creature must make an Endurance check with a target number of 8. If they are unsuccessful, they gain the poisoned condition for the next 24 hours."}
                             510 {:id 510
                                  :type :items
                                  :name "Healing Potion"
                                  :properties [:item]
                                  :benefits {:quality {:name "Quality"
                                                       :value 2}
                                             :power {:name "Power"
                                                     :value 2}}
                                  :details "When ingested, gain +Quality +Power on physical health checks for the next hour."}
                             511 {:id 511
                                  :type :items
                                  :name "Rope"
                                  :properties [:item]
                                  :benefits {:quality {:name "Quality"
                                                       :value 1}
                                             :power {:name "Power"
                                                     :value 2}}
                                  :details "Gain +Quality +Power on a Coordination check to restrain a creature. The creature must break free with a Coordination check -Quality -Power where the target number is equal to the initial Coordination check you made."}}
                 :rules {:stats  rules-data/stats
                         :checks rules-data/skill-checks-details
                         :encounters rules-data/encounters
                         :damage rules-data/damage
                         :conditions rules-data/conditions}
                 :rolls {}})



(def fillias-chest
  {:resources {11101 {:id 11101, :type :equipment, :name "Cloak of Protection from Evil", :properties [], :benefits {:quality {:name "Quality", :value 1}, :power {:name "Power", :value 3}}, :details "Grants +Quality +Power on rolls to protect yourself from actions made out of malicious intent."} 11102 {:id 11102 :type :item :name "Healing Stone" :properties [] :benefits {:quality {:name "Quality", :value 0}, :power {:name "Power", :value 0}} :details "Heals a minor wound once per day."} 11103 {:id 11103, :type :equipment, :name "Shortsword of Teleportation", :properties [], :benefits {:quality {:name "Balance", :value 3}, :power {:name "Lethality", :value 2}}, :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be in the same zone as you. As a light weapon, it may be wielded in either hand. When partitioning the base dice pool, the resulting dice pools must have at least two dice in them. This weapon may deal piercing or slashing damage. Once per day, on a successful hit, you may teleport yourself and the affected creature up to 3 zones away."}}})

(def glendas-chest
  {:resources {11101 {:id 11101, :type :equipment, :name "Cloak of Protection from Evil", :properties [], :benefits {:quality {:name "Quality", :value 1}, :power {:name "Power", :value 3}}, :details "Grants +Quality +Power on rolls to protect yourself from actions made out of malicious intent."}}})

(def reginalds-drawer
  {:resources {22201 {:id 22201, :type :equipment, :name "Aether-reaching Dagger", :properties [], :benefits {:quality {:name "Balance", :value 4}, :power {:name "Lethality", :value 3}}, :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be in the same zone as you. As a light weapon, it may be wielded in either hand. When partitioning the base dice pool, the resulting dice pools must have at least two dice in them. This weapon deals Spiritual injuries instead of Physical injuries."} 22202 {:id 22202, :type :equipment, :name "Ring of Isolation", :properties [], :benefits {:quality {:name "Quality", :value 2}, :power {:name "Power", :value 2}}, :details "Grants +Quality +Power on rolls to protect yourself from binding magics."}}})

(def chess-puzzle
  {:resources {33301 {:id 33301, :type :equipment, :name "", :properties [], :benefits {:quality {:name "Quality", :value 0}, :power {:name "Power", :value 0}}, :details ""}}})


;; resources that mention complex and reckless/careful actions
;; {100 {:id 100
;;       :type :equipment
;;       :name "Dagger"
;;       :properties [:light :sword :slashing :piercing]
;;       :benefits {:quality {:name "Balance"
;;                            :value 4}
;;                  :power {:name "Lethality"
;;                          :value -1}}
;;       :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be in the same zone as you. As a light weapon, it may be wielded in either hand. When partitioning the base dice pool, the resulting dice pools must have at least two dice in them. This weapon may deal piercing or slashing damage."}
;;  101 {:id 101
;;       :type :equipment
;;       :name "Short Sword"
;;       :properties [:light :sword :slashing :piercing]
;;       :benefits {:quality {:name "Balance"
;;                            :value 3}
;;                  :power {:name "Lethality"
;;                          :value 0}}
;;       :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be in the same zone as you. As a light weapon, it may be wielded in either hand. When partitioning the base dice pool, the resulting dice pools must have at least two dice in them. This weapon may deal piercing or slashing damage."}
;;  102 {:id 102
;;       :type :equipment
;;       :name "Rapier"
;;       :properties [:light :sword :piercing]
;;       :benefits {:quality {:name "Balance"
;;                            :value 3}
;;                  :power {:name "Lethality"
;;                          :value 1}}
;;       :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be in the same zone as you. As a light weapon, it may be wielded in either hand. When partitioning the base dice pool, the resulting dice pools must have at least two dice in them. This weapon deals piercing damage."}
;;  103 {:id 103
;;       :type :equipment
;;       :name "Longsword"
;;       :properties [:medium :sword :slashing :piercing]
;;       :benefits {:quality {:name "Balance"
;;                            :value 2}
;;                  :power {:name "Lethality"
;;                          :value 2}}
;;       :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be in the same zone as you. As a medium weapon, it must be wielded in the dominant hand. When partitioning the base dice pool, the resulting dice pools must have at least three dice in them. This weapon may deal piercing or slashing damage."}
;;  104 {:id 104
;;       :type :equipment
;;       :name "Greatsword"
;;       :properties [:heavy :sword :slashing :piercing]
;;       :benefits {:quality {:name "Balance"
;;                            :value 1}
;;                  :power {:name "Lethality"
;;                          :value 4}}
;;       :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be in the same zone as you. As a heavy weapon, it must be wielded in both hands. When partitioning the base dice pool, the resulting dice pools must have at least four dice in them. This weapon may deal piercing or slashing damage."}
;;  105 {:id 105
;;       :type :equipment
;;       :name "Handaxe"
;;       :properties [:light :axe :slashing :bludgeoning]
;;       :benefits {:quality {:name "Balance"
;;                            :value 2}
;;                  :power {:name "Lethality"
;;                          :value 1}}
;;       :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be in the same zone as you. As a light weapon, it may be wielded in either hand. When partitioning the base dice pool, the resulting dice pools must have at least two dice in them. This weapon may deal slashing or bludgeoning damage."}
;;  106 {:id 106
;;       :type :equipment
;;       :name "Battle Axe"
;;       :properties [:medium :axe :slashing :bludgeoning]
;;       :benefits {:quality {:name "Balance"
;;                            :value 1}
;;                  :power {:name "Lethality"
;;                          :value 3}}
;;       :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be in the same zone as you. As a medium weapon, it must be wielded in the dominant hand. When partitioning the base dice pool, the resulting dice pools must have at least three dice in them. This weapon may deal slashing or bludgeoning damage."}
;;  107 {:id 107
;;       :type :equipment
;;       :name "Greataxe"
;;       :properties [:heavy :axe :slashing :bludgeoning]
;;       :benefits {:quality {:name "Balance"
;;                            :value 1}
;;                  :power {:name "Lethality"
;;                          :value 4}}
;;       :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be in the same zone as you. As a heavy weapon, it must be wielded in both hands. When partitioning the base dice pool, the resulting dice pools must have at least four dice in them. This weapon may deal slashing or bludgeoning damage."}
;;  108 {:id 108
;;       :type :equipment
;;       :name "Javelin"
;;       :properties [:light :spear :piercing]
;;       :benefits {:quality {:name "Balance"
;;                            :value 1}
;;                  :power {:name "Lethality"
;;                          :value 3}}
;;       :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be in the same zone as you. As a light weapon, it may be wielded in either hand. When partitioning the base dice pool, the resulting dice pools must have at least two dice in them. This weapon deals piercing damage."}
;;  109 {:id 109
;;       :type :equipment
;;       :name "Spear"
;;       :properties [:medium :spear :piercing]
;;       :benefits {:quality {:name "Balance"
;;                            :value 2}
;;                  :power {:name "Lethality"
;;                          :value 3}}
;;       :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be in the same zone as you. As a medium weapon, it must be wielded in the dominant hand. When partitioning the base dice pool, the resulting dice pools must have at least three dice in them. This weapon deals piercing damage."}
;;  110 {:id 110
;;       :type :equipment
;;       :name "Lance"
;;       :properties [:heavy :spear :piercing]
;;       :benefits {:quality {:name "Balance"
;;                            :value 3}
;;                  :power {:name "Lethality"
;;                          :value 3}}
;;       :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be in the same zone as you. As a heavy weapon, it must be wielded in both hands. When partitioning the base dice pool, the resulting dice pools must have at least four dice in them. This weapon deals piercing damage."}
;;  111 {:id 111
;;       :type :equipment
;;       :name "Warhammer"
;;       :properties [:medium :hammer :bludgeoning]
;;       :benefits {:quality {:name "Balance"
;;                            :value 1}
;;                  :power {:name "Lethality"
;;                          :value 4}}
;;       :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be in the same zone as you. As a medium weapon, it must be wielded in the dominant hand. When partitioning the base dice pool, the resulting dice pools must have at least three dice in them. This weapon deals bludgeoning damage."}
;;  112 {:id 112
;;       :type :equipment
;;       :name "Greathammer"
;;       :properties [:heavy :heavy :bludgeoning]
;;       :benefits {:quality {:name "Balance"
;;                            :value 2}
;;                  :power {:name "Lethality"
;;                          :value 4}}
;;       :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be in the same zone as you. As a heavy weapon, it must be wielded in both hands. When partitioning the base dice pool, the resulting dice pools must have at least four dice in them. This weapon deals bludgeoning damage."}
;;  113 {:id 113
;;       :type :equipment
;;       :name "Hand Crossbow"
;;       :properties [:light :crossbow :piercing]
;;       :benefits {:quality {:name "Balance"
;;                            :value 1}
;;                  :power {:name "Lethality"
;;                          :value 2}}
;;       :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be within 3 zones or 30 feet. As a light weapon, it may be wielded in either hand. When partitioning the base dice pool, the resulting dice pools must have at least two dice in them. This weapon deals piercing damage."}
;;  114 {:id 114
;;       :type :equipment
;;       :name "Crossbow"
;;       :properties [:medium :crossbow :piercing]
;;       :benefits {:quality {:name "Balance"
;;                            :value 1}
;;                  :power {:name "Lethality"
;;                          :value 3}}
;;       :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be within 8 zones or 80 feet. As a medium crossbow, it must be wielded in both hands. When partitioning the base dice pool, the resulting dice pools must have at least three dice in them. This weapon deals piercing damage."}
;;  115 {:id 115
;;       :type :equipment
;;       :name "Shortbow"
;;       :properties [:light :bow :piercing]
;;       :benefits {:quality {:name "Balance"
;;                            :value 2}
;;                  :power {:name "Lethality"
;;                          :value 1}}
;;       :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be within 5 zones or 50 feet. As a bow, it must be wielded in both hands. When partitioning the base dice pool, the resulting dice pools must have at least two dice in them. This weapon deals piercing damage."}
;;  116 {:id 116
;;       :type :equipment
;;       :name "Longbow"
;;       :properties [:medium :bow :piercing]
;;       :benefits {:quality {:name "Balance"
;;                            :value 3}
;;                  :power {:name "Lethality"
;;                          :value 1}}
;;       :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be with 10 zones or 100 feet. As a bow it must be wielded in both hands. When partitioning the base dice pool, the resulting dice pools must have at least three dice in them. This weapon deals piercing damage."}
;;  117 {:id 117
;;       :type :equipment
;;       :name "Greatbow"
;;       :properties [:heavy :bow :piercing]
;;       :benefits {:quality {:name "Balance"
;;                            :value 2}
;;                  :power {:name "Lethality"
;;                          :value 3}}
;;       :details "You may spend a main action to make a Coordination +Balance +Lethality check against a creature. You must have the creature targeted and the creature must be with 15 zones or 150 feet. As a bow, it must be wielded in both hands. When partitioning the base dice pool, the resulting dice pools must have at least four dice in them. This weapon deals piercing damage."}
;;  118 {:id 118
;;       :type :equipment
;;       :name "Buckler"
;;       :properties [:light :shield]
;;       :benefits {:quality {:name "Balance"
;;                            :value 3}
;;                  :power {:name "Coverage"
;;                          :value 0}}
;;       :details "You may spend a main action to make a Reflexes +Balance +Coverage check to defend yourself against a creature's attack that targets you. When partitioning the base dice pool while defending yourself against physical attacks, the resulting dice pools must have at least two dice in them. When multiple items contribute to a defense check, the partitioned dice pools must follow the size limitations of every item involved. As a shield, it can be wielded in either hand."}
;;  119 {:id 119
;;       :type :equipment
;;       :name "Kite Shield"
;;       :properties [:medium :shield]
;;       :benefits {:quality {:name "Balance"
;;                            :value 2}
;;                  :power {:name "Coverage"
;;                          :value 2}}
;;       :details "You may spend a main action to make a Reflexes +Balance +Coverage check to defend yourself against a creature's attack that targets you. When partitioning the base dice pool while defending yourself against physical attacks, the resulting dice pools must have at least three dice in them. When multiple items contribute to a defense check, the partitioned dice pools must follow the size limitations of every item involved. As a shield, it can be wielded in either hand."}
;;  120 {:id 120
;;       :type :equipment
;;       :name "Tower Shield"
;;       :properties [:heavy :shield]
;;       :benefits {:quality {:name "Balance"
;;                            :value 2}
;;                  :power {:name "Coverage"
;;                          :value 3}}
;;       :details "You may spend a main action to make a Reflexes +Balance +Coverage check to defend yourself against a creature's attack that targets you. When partitioning the base dice pool while defending yourself against physical attacks, the resulting dice pools must have at least four dice in them. When multiple items contribute to a defense check, the partitioned dice pools must follow the size limitations of every item involved. As a shield, it can be wielded in either hand."}
;;  121 {:id 121
;;       :type :equipment
;;       :name "Cloth Armor"
;;       :properties [:light :padding]
;;       :benefits {:quality {:name "Flexibility"
;;                            :value 3}
;;                  :power {:name "Durability"
;;                          :value 0}}
;;       :details "You may spend a main action to make a Reflexes +Quality +Power check to defend yourself against a creature's attack that targets you. When partitioning the base dice pool while defending yourself against physical attacks, the resulting dice pools must have at least two dice in them. When multiple items contribute to a defense check, the partitioned dice pools must follow the size limitations of every item involved."}
;;  122 {:id 122
;;       :type :equipment
;;       :name "Hide Armor"
;;       :properties [:medium :padding :bludgeoning]
;;       :benefits {:quality {:name "Flexibility"
;;                            :value 3}
;;                  :power {:name "Durability"
;;                          :value 0}}
;;       :details "You may spend a main action to make a Reflexes +Quality +Power check to defend yourself against a creature's attack that targets you. When partitioning the base dice pool while defending yourself against physical attacks, the resulting dice pools must have at least three dice in them. When multiple items contribute to a defense check, the partitioned dice pools must follow the size limitations of every item involved. This armor reduces the severity of injuries inflicted by bludgeoning damage."}
;;  123 {:id 123
;;       :type :equipment
;;       :name "Leather Armor"
;;       :properties [:heavy :padding :bludgeoning :slashing]
;;       :benefits {:quality {:name "Flexibility"
;;                            :value 2}
;;                  :power {:name "Durability"
;;                          :value 1}}
;;       :details "You may spend a main action to make a Reflexes +Quality +Power check to defend yourself against a creature's attack that targets you. When partitioning the base dice pool while defending yourself against physical attacks, the resulting dice pools must have at least four dice in them. When multiple items contribute to a defense check, the partitioned dice pools must follow the size limitations of every item involved. This armor reduces the severity of injuries inflicted by bludgeoning and slashing damage."}
;;  124 {:id 124
;;       :type :equipment
;;       :name "Chain Maille Vest"
;;       :properties [:light :maille :slashing]
;;       :benefits {:quality {:name "Flexibility"
;;                            :value 1}
;;                  :power {:name "Durability"
;;                          :value 1}}
;;       :details "You may spend a main action to make a Reflexes +Quality +Power check to defend yourself against a creature's attack that targets you. When partitioning the base dice pool while defending yourself against physical attacks, the resulting dice pools must have at least two dice in them. When multiple items contribute to a defense check, the partitioned dice pools must follow the size limitations of every item involved. This armor reduces the severity of injuries inflicted by slashing damage."}
;;  125 {:id 125
;;       :type :equipment
;;       :name "Chain Maille Shirt"
;;       :properties [:medium :maille :slashing]
;;       :benefits {:quality {:name "Flexibility"
;;                            :value 1}
;;                  :power {:name "Durability"
;;                          :value 2}}
;;       :details "You may spend a main action to make a Reflexes +Quality +Power check to defend yourself against a creature's attack that targets you. When partitioning the base dice pool while defending yourself against physical attacks, the resulting dice pools must have at least three dice in them. When multiple items contribute to a defense check, the partitioned dice pools must follow the size limitations of every item involved. This armor reduces the severity of injuries inflicted by slashing damage."}
;;  126 {:id 126
;;       :type :equipment
;;       :name "Riveted Chain Maille"
;;       :properties [:heavy :maille :slashing :piercing]
;;       :benefits {:quality {:name "Flexibility"
;;                            :value 1}
;;                  :power {:name "Durability"
;;                          :value 2}}
;;       :details "You may spend a main action to make a Reflexes +Quality +Power check to defend yourself against a creature's attack that targets you. When partitioning the base dice pool while defending yourself against physical attacks, the resulting dice pools must have at least four dice in them. When multiple items contribute to a defense check, the partitioned dice pools must follow the size limitations of every item involved. This armor reduces the severity of injuries inflicted by slashing and piercing damage."}
;;  127 {:id 127
;;       :type :equipment
;;       :name "Leather Plate"
;;       :properties [:light :plate :slashing :piercing]
;;       :benefits {:quality {:name "Flexibility"
;;                            :value -1}
;;                  :power {:name "Durability"
;;                          :value 2}}
;;       :details "You may spend a main action to make a Reflexes +Quality +Power check to defend yourself against a creature's attack that targets you. When partitioning the base dice pool while defending yourself against physical attacks, the resulting dice pools must have at least two dice in them. When multiple items contribute to a defense check, the partitioned dice pools must follow the size limitations of every item involved. This armor reduces the severity of injuries inflicted by slashing and piercing damage."}
;;  128 {:id 128
;;       :type :equipment
;;       :name "Steel Half-Plate"
;;       :properties [:medium :plate :slashing :piercing :bludgeoning]
;;       :benefits {:quality {:name "Flexibility"
;;                            :value -2}
;;                  :power {:name "Durability"
;;                          :value 3}}
;;       :details "You may spend a main action to make a Reflexes +Quality +Power check to defend yourself against a creature's attack that targets you. When partitioning the base dice pool while defending yourself against physical attacks, the resulting dice pools must have at least three dice in them. When multiple items contribute to a defense check, the partitioned dice pools must follow the size limitations of every item involved. This armor reduces the severity of injuries inflicted by slashing, piercing, and bludgeoning damage."}
;;  129 {:id 129
;;       :type :equipment
;;       :name "Steel Full-Plate"
;;       :properties [:heavy :plate :slashing :piercing :bludgeoning]
;;       :benefits {:quality {:name "Flexibility"
;;                            :value -2}
;;                  :power {:name "Durability"
;;                          :value 4}}
;;       :details "You may spend a main action to make a Reflexes +Quality +Power check to defend yourself against a creature's attack that targets you. When partitioning the base dice pool while defending yourself against physical attacks, the resulting dice pools must have at least four dice in them. When multiple items contribute to a defense check, the partitioned dice pools must follow the size limitations of every item involved. This armor reduces the severity of injuries inflicted by slashing, piercing, and bludgeoning damage."}
;;  130 {:id 130
;;       :type :equipment
;;       :name "Tear of Ishiq"
;;       :properties [:relic]
;;       :benefits {:quality {:name "Quality"
;;                            :value 1}
;;                  :power {:name "Power"
;;                          :value 3}}
;;       :details "A fog-colored stone said to have come from the Eye of Ishiq. Add water to the surface of the stone to produce bright light in a 50 foot radius. Once a day, you can speak a chosen word or phrase to create a flash of brilliant light. Make an Exertion +Quality +Power check. Any creature within 50 feet that has line of sight must make a Reflex check or be blinded for a moment."}
;;  131 {:id 131
;;       :type :equipment
;;       :name "Gauntlet of Heilm"
;;       :properties [:relic]
;;       :benefits {:quality {:name "Quality"
;;                            :value 2}
;;                  :power {:name "Power"
;;                          :value 2}}
;;       :details "A pair of gauntlets said to be molded upon the Fist of Heilm. While worn, gain +Quality +Power on checks to maintain a firm grasp. Great for climbing, grappling, and building, among other things."}
;;  132 {:id 132
;;       :type :equipment
;;       :name "Fragrance of Ijarda"
;;       :properties [:relic]
;;       :benefits {:quality {:name "Quality"
;;                            :value 3}
;;                  :power {:name "Power"
;;                          :value 1}}
;;       :details "A warm fragrance said to be the remnants of the Breath of Ijarda that was once held in this very bottle. When a creature smells the contents of this bottle, they gain +Quality +Power on their next Physical, Spiritual, or Mental Health Check."}
;;  133 {:id 133
;;       :type :equipment
;;       :name "Coin of Aldiff"
;;       :properties [:artifact]
;;       :benefits {:quality {:name "Quality"
;;                            :value 1}
;;                  :power {:name "Power"
;;                          :value 3}}
;;       :details "Target a creature within line of sight and flip this coin into the air to change places with that creature. If the creature is unwilling, then gain +Quality +Power on an Exertion check contested by their Instinct check. The coin falls at your feet whether or not the switch was successful."}
;;  134 {:id 134
;;       :type :equipment
;;       :name "Arcanist's Quill"
;;       :properties [:artifact]
;;       :benefits {:quality {:name "Quality"
;;                            :value 2}
;;                  :power {:name "Power"
;;                          :value 2}}
;;       :details "Gain +Quality +Power when inscribing glyphs with the Arcanist's Quill."}
;;  135 {:id 135
;;       :type :equipment
;;       :name "Gravitas Staff"
;;       :properties [:artifact]
;;       :benefits {:quality {:name "Quality"
;;                            :value 1}
;;                  :power {:name "Power"
;;                          :value 3}}
;;       :details "The Gravitas Staff is able to move objects telekinetically. Gain +Quality +Power on these Exertion checks. The target number on these checks increases as the weight and distance increase."}
;;  136 {:id 136
;;       :type :equipment
;;       :name "Spores of Myconis"
;;       :properties [:totem]
;;       :benefits {:quality {:name "Quality"
;;                            :value 2}
;;                  :power {:name "Power"
;;                          :value 2}}
;;       :details "Once per day, you can inhale the spores contained in this terrarium to gain the ability to communicate with plant life for 10 minutes. Gain +Quality +Power on checks related to handling plants while you are in this state."}
;;  137 {:id 137
;;       :type :equipment
;;       :name "Tooth of Draconis"
;;       :properties [:totem :draconic]
;;       :benefits {:quality {:name "Quality"
;;                            :value 0}
;;                  :power {:name "Power"
;;                          :value 4}}
;;       :details "Once per day, gain +Quality +Power on an Exertion check targeted on an adjacent zone to breath fire. All creatures in the zone are hit by the attack and must make defense checks to avoid taking damage."}
;;  138 {:id 138
;;       :type :equipment
;;       :name "Shard of Primordial Flame"
;;       :properties [:totem :elemental]
;;       :benefits {:quality {:name "Quality"
;;                            :value 2}
;;                  :power {:name "Power"
;;                          :value 2}}
;;       :details "A wand-shaped fragment of the primordial manifestation of fire that is warm to the touch. Known equally for its life-giving and destructive properties, this shard has two uses. Gain +Quality +Power on an Exertion check to throw a fireball at a targeted creature or object. Alternatively, if this shard is used to spark the flame of a campfire or fireplace, then make a Perseverance check and over the lifetime of that fire, it can heal a number of physical minor wounds equal to the result of your check."}
;;  139 {:id 139
;;       :type :equipment
;;       :name "Shard of Primordial Air"
;;       :properties [:totem :elemental]
;;       :benefits {:quality {:name "Quality"
;;                            :value 2}
;;                  :power {:name "Power"
;;                          :value 2}}
;;       :details "An orb-shaped fragment of the primordial manifestation of air. Grants +Quality +Power on an Instinct check to increase jump height/distance by double or decrease fall speed by half. The target number for this check is equal to 4 + the number of creatures that you are attempting to affect."}
;;  140 {:id 140
;;       :type :equipment
;;       :name "Shard of Primordial Water"
;;       :properties [:totem :elemental]
;;       :benefits {:quality {:name "Quality"
;;                            :value 2}
;;                  :power {:name "Power"
;;                          :value 2}}
;;       :details "A bowl-shaped fragment of the primordial element of water that is cool to the touch. Once per day, fill the fragment with water and gain +Quality +Power on an Instinct check while thinking of an objective where the target number is determined by the complexity of that objective. If successful, the poured water will flow to point in the direction of whatever would be most helpful in accomplishing your objective."}
;;  141 {:id 141
;;       :type :equipment
;;       :name "Shard of Primordial Stone"
;;       :properties [:totem :elemental]
;;       :benefits {:quality {:name "Quality"
;;                            :value 0}
;;                  :power {:name "Power"
;;                          :value 0}}
;;       :details "A shield-shaped fragment of the primordial element of stone. Once per day, gain +Quality +Power on a Perseverance check on a target creature in line of sight as a reaction to that creature being targeted by an attack. The result of the Perseverance check is granted as a flat bonus on the target creature's defense check."}
;;  142 {:id 142
;;       :type :equipment
;;       :name "Tattoo of (Damage Type) Protection"
;;       :properties [:talisman]
;;       :benefits {:quality {:name "Quality"
;;                            :value 1}
;;                  :power {:name "Power"
;;                          :value 1}}
;;       :details "Once per day, gain +Quality +Power on a defense check against an attack that deals (Damage Type) damage."}
;;  143 {:id 143
;;       :type :equipment
;;       :name "Ring of Truth"
;;       :properties [:talisman]
;;       :benefits {:quality {:name "Quality"
;;                            :value 0}
;;                  :power {:name "Power"
;;                          :value 0}}
;;       :details "Once per day, you may gain +Quality +Power on any Insight check."}
;;  144 {:id 144
;;       :type :equipment
;;       :name "Amulet of (Domain)"
;;       :properties [:talisman]
;;       :benefits {:quality {:name "Quality"
;;                            :value 2}
;;                  :power {:name "Power"
;;                          :value 2}}
;;       :details "Once per day, gain +Quality +Power on any (Domain) health check."}

;;  145 {:id 145
;;       :type :equipment
;;       :name "Climber’s Tools"
;;       :properties [:tool]
;;       :benefits {:quality {:name "Quality"
;;                            :value 0}
;;                  :power {:name "Power"
;;                          :value 0}}
;;       :details "Gain +Quality +Power on checks that use these tools to climb a surface."}
;;  146 {:id 146
;;       :type :equipment
;;       :name "Forgery Kit"
;;       :properties [:tool]
;;       :benefits {:quality {:name "Quality"
;;                            :value 0}
;;                  :power {:name "Power"
;;                          :value 0}}
;;       :details "Gain +Quality +Power on checks that use these tools to duplicate text."}
;;  147 {:id 147
;;       :type :equipment
;;       :name "Lockpick Set"
;;       :properties [:tool]
;;       :benefits {:quality {:name "Quality"
;;                            :value 0}
;;                  :power {:name "Power"
;;                          :value 0}}
;;       :details "Gain +Quality +Power on checks that use these tools to open locks or disable traps."}
;;  148 {:id 148
;;       :type :equipment
;;       :name "Reference Document"
;;       :properties [:tool]
;;       :benefits {:quality {:name "Quality"
;;                            :value 0}
;;                  :power {:name "Power"
;;                          :value 0}}
;;       :details "Gain +Quality +Power on checks that use these documents to recall information related to the topic of these documents."}
;;  149 {:id 149
;;       :type :equipment
;;       :name "Navigator's Tools"
;;       :properties [:tool]
;;       :benefits {:quality {:name "Quality"
;;                            :value 0}
;;                  :power {:name "Power"
;;                          :value 0}}
;;       :details "Gain +Quality +Power on checks that use these tools to determine your current location and orientation."}
;;  150 {:id 150
;;       :type :equipment
;;       :name "Tinkerer’s Tools"
;;       :properties [:tool]
;;       :benefits {:quality {:name "Quality"
;;                            :value 0}
;;                  :power {:name "Power"
;;                          :value 0}}
;;       :details "Gain +Quality +Power on checks that use these tools to create small mechanisms and trinkets"}
;;  151 {:id 151
;;       :type :equipment
;;       :name "Alchemist’s Tools"
;;       :properties [:tool]
;;       :benefits {:quality {:name "Quality"
;;                            :value 0}
;;                  :power {:name "Power"
;;                          :value 0}}
;;       :details "Gain +Quality +Power on checks that use these tools to brew alchemical mixtures."}
;;  152 {:id 152
;;       :type :equipment
;;       :name "Musical Instrument"
;;       :properties [:tool]
;;       :benefits {:quality {:name "Quality"
;;                            :value 0}
;;                  :power {:name "Power"
;;                          :value 0}}
;;       :details "Gain +Quality +Power on checks that use this instrument in a musical performance."}
;;  153 {:id 153
;;       :type :equipment
;;       :name "Cartographer’s Tools"
;;       :properties [:tool]
;;       :benefits {:quality {:name "Quality"
;;                            :value 0}
;;                  :power {:name "Power"
;;                          :value 0}}
;;       :details "Gain +Quality +Power on checks that use these tools to measure and draw maps."}
;;  154 {:id 154
;;       :type :equipment
;;       :name "Painter’s Supplies"
;;       :properties [:tool]
;;       :benefits {:quality {:name "Quality"
;;                            :value 0}
;;                  :power {:name "Power"
;;                          :value 0}}
;;       :details "Gain +Quality +Power on checks that use these tools to create visual art."}
;;  155 {:id 155
;;       :type :equipment
;;       :name "Herbalism Kit"
;;       :properties [:tool]
;;       :benefits {:quality {:name "Quality"
;;                            :value 0}
;;                  :power {:name "Power"
;;                          :value 0}}
;;       :details "Gain +Quality +Power on checks that use these tools to create medicines."}
;;  156 {:id 156
;;       :type :equipment
;;       :name "Disguise Kit"
;;       :properties [:tool]
;;       :benefits {:quality {:name "Quality"
;;                            :value 0}
;;                  :power {:name "Power"
;;                          :value 0}}
;;       :details "Gain +Quality +Power on checks that use these tools to disguise your appearance"}

;;  200 {:id 200
;;       :type :traits
;;       :name "Ideal Template"
;;       :properties [:ideal]
;;       :benefits {:quality {:name "Clarity"
;;                            :value 0}
;;                  :power {:name "Dedication"
;;                          :value 0}}
;;       :details "Grants +Quality +Power to checks that promote or exemplify your ideal. Checks that run counter to your ideal gain penalties equal to the Quality and Power of your ideal. Circumstances may cause multiple traits to apply bonuses or penalties to a check. Ultimately, it is up to the DM's discretion whether a trait grants a bonus or penalty. Some examples of ideals are courage, life, and freedom."}
;;  201 {:id 201
;;       :type :traits
;;       :name "Purpose Template"
;;       :properties [:purpose]
;;       :benefits {:quality {:name "Clarity"
;;                            :value 0}
;;                  :power {:name "Dedication"
;;                          :value 0}}
;;       :details "Grants +Quality +Power to checks that fulfill your purpose. Checks that contradict your purpose gain penalties equal to the Quality and Power of your purpose. Circumstances may cause multiple traits to apply bonuses or penalties to a check. Ultimately, it is up to the DM's discretion whether a trait grants a bonus or penalty. Some examples of purposes would be to protect something important to you or to serve a deity or power."}
;;  202 {:id 202
;;       :type :traits
;;       :name "Goal Template"
;;       :properties [:goal]
;;       :benefits {:quality {:name "Clarity"
;;                            :value 0}
;;                  :power {:name "Dedication"
;;                          :value 0}}
;;       :details "Grants +Quality +Power to checks that move you towards accomplishing your goal Checks that set you back with respect to your goal gain penalties equal to the Quality and Power of your goal. Circumstances may cause multiple traits to apply bonuses or penalties to a check. Ultimately, it is up to the DM's discretion whether a trait grants a bonus or penalty. Some examples of goals are to discover forgotten magics, wealth, or to be granted knighthood."}
;;  203 {:id 203
;;       :type :traits
;;       :name "Oath Template"
;;       :properties [:oath]
;;       :benefits {:quality {:name "Clarity"
;;                            :value 0}
;;                  :power {:name "Dedication"
;;                          :value 0}}
;;       :details "Grants +Quality +Power to checks that are aligned with your oath. Checks that are misaligned with your oath gain penalties equal to the Quality and Power of your oath. Circumstances may cause multiple traits to apply bonuses or penalties to a check. Ultimately, it is up to the DM's discretion whether a trait grants a bonus or penalty. Some examples of oaths are to uphold the virtues of Heilm, an oath of revenge, or an oath to serve the crown."}

;;  300 {:id 300
;;       :type :expertise
;;       :name "Scout"
;;       :properties [:training :fighting-style]
;;       :benefits {:quality {:name "Knowledge"
;;                            :value 3}
;;                  :power {:name "Practice"
;;                          :value 1}}
;;       :details "Gain +Quality +Power on checks that use a light weapon, shield, or armor. Gain +Quality +Power on checks to move stealthily and to cover your tracks and on checks to contest another creature moving stealthily or covering their tracks."}
;;  301 {:id 301
;;       :type :expertise
;;       :name "Soldier"
;;       :properties [:training :fighting-style]
;;       :benefits {:quality {:name "Knowledge"
;;                            :value 2}
;;                  :power {:name "Practice"
;;                          :value 2}}
;;       :details "Gain +Quality +Power on checks that use a medium weapon, shield, or armor. Additionally, when you make a melee attack against a targeted creature, you can choose to make one of the following combat maneuvers instead: trip, shove, or disarm. If your trip attack is successful, the target falls prone. If the shove is successful, the target is moved to an adjacent zone. If the disarm is successful, the target's weapon falls to the ground in the zone that they occupy."}
;;  302 {:id 302
;;       :type :expertise
;;       :name "Sentinel"
;;       :properties [:training :fighting-style]
;;       :benefits {:quality {:name "Knowledge"
;;                            :value 1}
;;                  :power {:name "Practice"
;;                          :value 3}}
;;       :details "Gain +Quality +Power on checks that use a heavy weapon, shield, or armor. Additionally, if a target creature within the same zone as you is attacked and you are wielding a heavy shield, you can make a defense check against that attack. The result of your defense check is inflicted as a flat penalty on the attack roll. The target of the attack may still make their own defense check if needed."}
;;  303 {:id 303
;;       :type :expertise
;;       :name "Arcanist"
;;       :properties [:education :aether-manipulation]
;;       :benefits {:quality {:name "Knowledge"
;;                            :value 3}
;;                  :power {:name "Practice"
;;                          :value 1}}
;;       :details "Gain +Quality +Power on checks that use Artifacts. Additionally, you have been taught the three fundamental glyph patterns: Connection, Imbuement, and Transmutation. These are the underlying patterns to most glyphs used by Arcanists. This allows you to acquire glyphs as additional resources."}
;;  304 {:id 304
;;       :type :expertise
;;       :name "Glyph of Alarm"
;;       :properties [:glyph :connection]
;;       :benefits {:quality {:name "Knowledge"
;;                            :value 0}
;;                  :power {:name "Practice"
;;                          :value 0}}
;;       :details "Inscribing this Glyph causes an alarm to be raised when certain creatures enter its given proximity. During the inscription process, any creature that touches the Glyph will be marked and the Glyph can be set to be triggered only by marked creatures or to be triggered only by unmarked creatures. For every 10 feet of proximity covered by this Glyph, the target number on the Exertion check for correctly inscribing it increases by 1. For example, a 30 foot radius alarm would have a target number of 3. This check should be done by the GM in secret. Once triggered, the Glyph will need to be recharged with and Exertion check in order to be reused and the target number for charging it is equal to the target number required to initially inscribe it."}
;;  305 {:id 305
;;       :type :expertise
;;       :name "Glyph of Communication"
;;       :properties [:glyph :connection]
;;       :benefits {:quality {:name "Knowledge"
;;                            :value 0}
;;                  :power {:name "Practice"
;;                          :value 0}}
;;       :details "Inscribing this Glyph on a pair of similar objects allows any creature that speaks the command word to transmit sound through these objects regardless of the objects' distance from one another. The target number on the Exertion check to correctly inscribe this Glyph is equal the desired duration of transmission in seconds. This check should be done by the GM in secret. Once the Glyph has been used, it can be recharged with another Exertion check with a target number equal to the target number required to initially inscribe it."}
;;  306 {:id 306
;;       :type :expertise
;;       :name "Glyph of (Stat) Enhancement"
;;       :properties [:glyph :imbuement]
;;       :benefits {:quality {:name "Knowledge"
;;                            :value 0}
;;                  :power {:name "Practice"
;;                          :value 0}}
;;       :details "(Select a stat when gaining this Glyph) Inscribing this Glyph on a willing creature gives that creature the ability to expend the Glyph to gain a flat bonus on a (Stat) check. The target number for the Exertion check to correctly inscribe this Glyph is equal to two times the intended flat bonus. This check should be done by the GM in secret. If the inscription process is unsuccessful, then a flat penalty is instead inflicted on the (Stat) check. This Glyph cannot be recharged."}
;;  307 {:id 307
;;       :type :expertise
;;       :name "Glyph of Dark Vision"
;;       :properties [:glyph :imbuement]
;;       :benefits {:quality {:name "Knowledge"
;;                            :value 0}
;;                  :power {:name "Practice"
;;                          :value 0}}
;;       :details "Inscribing this Glyph on a willing creature gives that creature the ability to see in the dark as if it were dim light. The target number on the Exertion check to correctly inscribe this Glyph increases by one for every ten minutes that it is intended to last for. If the inscription process is unsuccessful, then the creature is instead blinded for the inteded duration of the Glyph's effects."}
;;  308 {:id 308
;;       :type :expertise
;;       :name "Glyph of Pure Water"
;;       :properties [:glyph :transmutation]
;;       :benefits {:quality {:name "Knowledge"
;;                            :value 0}
;;                  :power {:name "Practice"
;;                          :value 0}}
;;       :details "Inscribing this Glyph on a gallon-sized container of liquid will transmute that liquid into pure drinkable water. This Glyph does not require an Exertion check to inscribe."}
;;  309 {:id 309
;;       :type :expertise
;;       :name "Glyph of Stoneskin"
;;       :properties [:glyph :imbuement]
;;       :benefits {:quality {:name "Knowledge"
;;                            :value 0}
;;                  :power {:name "Practice"
;;                          :value 0}}
;;       :details "Inscribing this Glyph on a willing creature transmutes that creature's skin to be as tough as stone. The target number on the Exertion check to correctly inscribe this Glyph increases by one for every 10 seconds that it is intended to last for. If the inscription process is unsuccessful, then the creature is instead inflicted with a minor physical wound."}
;;  310 {:id 310
;;       :type :expertise
;;       :name "Druid"
;;       :properties [:experience :aether-manipulation]
;;       :benefits {:quality {:name "Knowledge"
;;                            :value 2}
;;                  :power {:name "Practice"
;;                          :value 2}}
;;       :details "Gain +Quality +Power on checks that use Totems. Additionally, you can make an Instinct +Quality +Power check to communicate roughly with plants and animals. The more distant the relation between the plant or animal and your race, the higher the target number becomes."}
;;  311 {:id 311
;;       :type :expertise
;;       :name "Invoker"
;;       :properties [:training :aether-manipulation]
;;       :benefits {:quality {:name "Knowledge"
;;                            :value 1}
;;                  :power {:name "Practice"
;;                          :value 3}}
;;       :details "Gain +Quality +Power on checks that use Relics. Additionally, you have a pledge or contract with some deity or power that you channel. Once per day, you may make an Exertion +Quality +Power check to commune with whatever deity or power you are bound to and request access to an invocation. The invocations available to you will depend on the deity or power you are bound to."}
;;  312 {:id 312
;;       :type :expertise
;;       :name "Education in (Topic)"
;;       :properties [:education]
;;       :benefits {:quality {:name "Knowledge"
;;                            :value 3}
;;                  :power {:name "Practice"
;;                          :value 1}}
;;       :details "Gain +Quality +Power on checks to recall information related to (Topic). Education in a given topic is a common prerequisite for other resources."}
;;  313 {:id 313
;;       :type :expertise
;;       :name "Researcher of (Topic)"
;;       :properties [:experience]
;;       :benefits {:quality {:name "Knowledge"
;;                            :value 1}
;;                  :power {:name "Practice"
;;                          :value 3}}
;;       :details "Gain +Quality +Power on checks to gather information about (Topic). This can be through multiple means such as experimentation, locating sources of information, or interpreting those sources of information. If a check to gain information fails, you will at least gain some idea of where you need to go in order to gain the information."}
;;  314 {:id 314
;;       :type :expertise
;;       :name "Noble"
;;       :properties [:training]
;;       :benefits {:quality {:name "Knowledge"
;;                            :value 3}
;;                  :power {:name "Practice"
;;                          :value 1}}
;;       :details "Gain +Quality +Power on checks related to passing as well-mannered nobility and to identify the social faux paus of others trying to come across as well-mannered nobility. This includes knowledge of social decorum, family ties, and any relevant political tensions."}
;;  315 {:id 315
;;       :type :expertise
;;       :name "Storyteller"
;;       :properties [:experience]
;;       :benefits {:quality {:name "Knowledge"
;;                            :value 2}
;;                  :power {:name "Practice"
;;                          :value 2}}
;;       :details "Gain +Quality +Power on checks to convince others that you know what you're talking about as well as on checks to identify when someone is trying to deceive you. This could be because you have actually come across this knowledge in your time collecting interesting stories and rumors, or this could be because you have extensive practice weaving the threads of a story in an enthralling and believable way."}
;;  316 {:id 316
;;       :type :expertise
;;       :name "Leader"
;;       :properties [:experience]
;;       :benefits {:quality {:name "Knowledge"
;;                            :value 1}
;;                  :power {:name "Practice"
;;                          :value 3}}
;;       :details "Gain +Quality +Power on checks to convince or inspire others to do what you want them to do as well as on checks to determine if others are being truthful with you."}

;;  400 {:id 400
;;       :type :affiliations
;;       :name "Individual Template"
;;       :properties [:individual]
;;       :benefits {:quality {:name "Reach"
;;                            :value 0}
;;                  :power {:name "Influence"
;;                          :value 0}}
;;       :details "Gain +Quality +Power on social checks while interacting with this individual. The Game Master may give partial bonuses on social checks where this individual is a mutual acquaintance. Examples of individuals that you may be affiliated with are allies, friends, mentors, or family."}
;;  401 {:id 401
;;       :type :affiliations
;;       :name "Group Template"
;;       :properties [:individual]
;;       :benefits {:quality {:name "Reach"
;;                            :value 0}
;;                  :power {:name "Influence"
;;                          :value 0}}
;;       :details "Gain +Quality +Power on social checks while interacting with this group. The Game Master may give partial bonuses on social checks where this group is a relevant. Examples of groups that you may be affiliated with are guilds, religious orders, schools, or communities."}
;;  402 {:id 402
;;       :type :affiliations
;;       :name "Acknowledgements Template"
;;       :properties [:individual]
;;       :benefits {:quality {:name "Reach"
;;                            :value 0}
;;                  :power {:name "Influence"
;;                          :value 0}}
;;       :details "Gain +Quality +Power on social checks while interacting with individuals who are familiar with your acknowledgements. The Game Master may give partial bonuses based on how familiar the individual is with your acknowledgements. Examples of acknowledgements that may be affiliated with you are reputations, titles, degrees or certifications, and rumors."}

;;  500 {:id 500
;;       :type :items
;;       :name "Acid"
;;       :properties [:item]
;;       :benefits {:quality {:name "Quality"
;;                            :value 1}
;;                  :power {:name "Power"
;;                          :value 1}}
;;       :details "Gain +Quality +Power on a Coordination check to throw this bottle at a target creature. If the attack is successful, then inflict a physical minor wound with acid damage."}
;;  501 {:id 501
;;       :type :items
;;       :name "Alchemist's Fire"
;;       :properties [:item]
;;       :benefits {:quality {:name "Quality"
;;                            :value 1}
;;                  :power {:name "Power"
;;                          :value 2}}
;;       :details "Gain +Quality +Power on a Coordination check to throw this bottle at a target creature. If the attack is successful, then inflict a physical minor wound with fire damage."}
;;  502 {:id 502
;;       :type :items
;;       :name "Antitoxin"
;;       :properties [:item]
;;       :benefits {:quality {:name "Quality"
;;                            :value 1}
;;                  :power {:name "Power"
;;                          :value 2}}
;;       :details "Gain +Quality +Power on your next health checks to remove the poisoned condition."}
;;  503 {:id 503
;;       :type :items
;;       :name "Lantern"
;;       :properties [:item]
;;       :benefits {:quality {:name "Quality"
;;                            :value 0}
;;                  :power {:name "Power"
;;                          :value 0}}
;;       :details "Casts light within a 20 foot radius. Nullify any penalties from low light in that radius. A flask of oil keeps the lantern running for 6 hours."}
;;  504 {:id 504
;;       :type :items
;;       :name "Torch"
;;       :properties [:item]
;;       :benefits {:quality {:name "Quality"
;;                            :value 0}
;;                  :power {:name "Power"
;;                          :value 0}}
;;       :details "Casts light within a 30 foot radius for the next hour. Nullify any penalties from low light in that radius."}
;;  505 {:id 505
;;       :type :items
;;       :name "Lock"
;;       :properties [:item]
;;       :benefits {:quality {:name "Quality"
;;                            :value 0}
;;                  :power {:name "Power"
;;                          :value 0}}
;;       :details "A metal lock that can be broken with a physical attack result of 10 or can be picked with a Coordination check using theives' tools and a target number of 7."}
;;  507 {:id 507
;;       :type :items
;;       :name "Manacles"
;;       :properties [:item]
;;       :benefits {:quality {:name "Quality"
;;                            :value 0}
;;                  :power {:name "Power"
;;                          :value 0}}
;;       :details "Metal manacles that can be be placed on a creature to give it the restrained condition that inflicts a -3d penalty on checks that require the use of your hands or feet, whichever is restrained. These can be broken with a physical attack result of 10 or can be picked with a Coordination check using theives' tools and a target number of 7."}
;;  509 {:id 509
;;       :type :items
;;       :name "Poison"
;;       :properties [:item]
;;       :benefits {:quality {:name "Quality"
;;                            :value 0}
;;                  :power {:name "Power"
;;                          :value 0}}
;;       :details "When ingested, the creature must make an Endurance check with a target number of 8. If they are unsuccessful, they gain the poisoned condition for the next 24 hours."}
;;  510 {:id 510
;;       :type :items
;;       :name "Healing Potion"
;;       :properties [:item]
;;       :benefits {:quality {:name "Quality"
;;                            :value 2}
;;                  :power {:name "Power"
;;                          :value 2}}
;;       :details "When ingested, gain +Quality +Power on physical health checks for the next hour."}
;;  511 {:id 511
;;       :type :items
;;       :name "Rope"
;;       :properties [:item]
;;       :benefits {:quality {:name "Quality"
;;                            :value 1}
;;                  :power {:name "Power"
;;                          :value 2}}
;;       :details "Gain +Quality +Power on a Coordination check to restrain a creature. The creature must break free with a Coordination check -Quality -Power where the target number is equal to the initial Coordination check you made."}}