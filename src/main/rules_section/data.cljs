(ns main.rules-section.data)

(def skill-checks-details
  {:overview
   {:header "Overview"
    :body "Skill checks should be made when a character’s actions have a reasonable chance of both failure and success and when failure would come at a cost. If it is impossible for a skill check to fail or to succeed, then there is nothing to check. If a skill check has a reasonable chance of both success and failure but there is no cost for the actions either way, then the character could just continue to try again until they succeed. Ultimately, it is up to the GM’s discretion when a skill check should be called for. When a skill check is called for, you will use your character’s stats to determine the check’s base dice pool. The base dice pool may then be modified in three ways which must occur in the following order: bonuses and penalties, splintering the dice pool, and combining or splitting dice. Once the base dice pool has been modified, then the dice are rolled and the results are compared to a target number which represents the difficulty of the actions you are taking."
    :example "For example, say the dice pool you roll for a skill check is 3d6 and you roll a 1, 3, and 4. The result of the check would be the 4 and the other two numbers are ignored."}

   :base-dice-pool
   {:header "Base Dice Pool"
    :body "When a check is called for, the GM will determine which of your stats is most relevant to the task at hand and the dice pool provided under that stat will become your base dice pool. For example, if you are trying to climb a cliffside, the GM may ask for a check using your coordination skill. If your coordination skill is 2d8, then your base dice pool would be two 8-sided dice."
    :example ""}

   :benefits-and-detriments
   {:header "Benefits and Detriments"
    :body "Circumstances and resources can affect skill checks beneficially or detrimentally. Beneficial effects grant dice bonuses and flat bonuses while detrimental effects inflict dice penalties and flat penalties. Dice bonuses and dice penalties alter your chances of success by increasing or decreasing the number of dice you roll. They are represented in the format of \"+1d\" for bonuses and \"-1d\" for penalties. On the other hand, flat penalties alter the limits of what you can accomplish by increasing or decreasing the result of the roll. They are represented in the format of \"+1\" or \"-1\". When bonuses and penalties of the same type are applied to a check, they cancel each other out and the final number is then applied to the dice pool. If a dice penalty would cause you to roll zero dice, instead, roll one die of the next size down.\n\nSometimes bonuses and penalties will be applied to a check after you have rolled the dice but before the check is resolved. If a dice bonus is applied after you roll your dice but before the roll is resolved, then roll the bonus dice and compare the result of the dice pool in question to your bonus dice result and take the higher of the two. If a dice penalty is inflicted after you roll all of your dice but before the roll is resolved, then roll the penalty dice one at a time and compare the result to the previous result. The die that is numerically closest to the penalty die you rolled is removed from the roll, rounding unfavorably, and then repeat this process with any remaining penalty dice. If you have penalty dice remaining when there is only one die left in the dice pool in question, then roll the next penalty die and take the lower of the two results. Continue this process until there are no penalty dice left."
    :example ""}

   :complex-actions
   {:header "Complex Actions"
    :body "After bonuses and penalties are applied but before the roll is made, you can choose how complex you would like your action to be. Actions are made complex by splintering the base dice pool into multiple fragmentary pools. Each of these fragmentary pools are counted as separate results for the purposes of resolving the check. This allows you to attempt multiple endeavors where you would usually only be able to attempt one. A common example of this is making multiple attacks against an enemy or attacking different enemies simultaneously. If you choose to splinter your base dice pool, each of the fragmentary dice pools must contain at least two dice. Some resources place higher limitations on the size of the fragmentary dice pools you can create. Flat bonuses and penalties must be distributed as evenly as possible amongst the fragmentary dice pools."
    :example ""}

   :careful-and-reckless-actions
   {:header "Careful and Reckless Actions"
    :body "The final modification that can be made to a dice pool before rolling is choosing to be careful or reckless. If you choose to act recklessly, then two dice of the same size in the same dice pool may be combined into a single die of the next highest size. On the other hand, if you choose to act carefully, then a single die may be split into two dice of the next smallest size. You can choose your degree of carefulness or recklessness by splitting or combining only some of the dice in the dice pool. In this case, you will end up with a dice pool containing dice of different sizes. This makes no difference in the roll. You will still simply roll all of the dice in the pool and take the highest result."
    :example ""}

   :passive-checks
   {:header "Passive Checks"
    :body "Passive checks may be used by the GM to resolve situations where your character’s stats are relevant but your character doesn't need to do anything to resolve the situation. Checks to determine your character's level of knowledge on a topic or checks to notice something in your surroundings when you aren’t searching will commonly be made as passive checks. The GM will still determine a target number and choose which stats are most relevant, however, instead of modifying that dice pool and rolling those dice, you will add together the following three values to determine the result: half of the size of the dice that make up the dice pool, the number of dice in the dice pool, and the net value of all of the bonuses and penalties. This result cannot exceed the cumulative total of the size of dice that make up the dice pool and the net value of your flat bonuses and penalties. Once the result is determined, it is compared to the target number determined by the GM as usual. Passive checks cannot be made into complex actions, nor can they be made careful or reckless."
    :example ""}})


(def encounters
  {:overview
   {:header "Overview"
    :body "An encounter is a scene that is centered around a goal and obstacle where many of your actions require skill checks. One of the clearest examples of an encounter is combat. During combat, the most basic goal is survival, the obstacle to that goal is the creatures or characters that are trying to kill you, and most of your actions will relate to attacking, defending, or positioning which all frequently require skill checks. Other examples of scenes that may be counted as encounters are chase scenes, social functions, survivalist challenges, heists, and investigations. Encounters are the most structured portions of an adventure. Your GM may decide to run some encounters more casually just like they would any other part of the adventure, however, if they want to track the order of actions more carefully, then they may use the following structure."
    :example nil}

   :rounds
   {:header "Rounds"
    :body "The in-game duration of a round depends on the encounter you are involved in. In the case of small scale combat, a round will usually take about five to ten seconds whereas in something like a social encounter, a round may take twenty minutes or an hour or even longer. Rounds are made up of two phases: the setup phase and the engagement phase. During the setup phase, creatures will have two setup actions that they can use to prepare themselves for the engagements that are to come. This phase will begin with everyone being given the opportunity to take the target action since many engagement actions can only be used on targeted creatures or areas. Then everyone is given the opportunity to use their remaining setup actions. During the engagement phase, creatures will have two engagement actions which they can use to attempt to overcome the obstacles they have targeted. This phase will usually involve traversing significant obstacles by climbing, swimming, jumping, etc., using items, attacking and defending, or various forms of persuasion. The round ends once all of the engagement actions have been resolved or when a moment passes where no dice were rolled for engagement actions."
    :example ""}

   :moments
   {:header "Moments"
    :body "After the setup phase of a round is complete and bonuses and penalties have been added to everyone’s base dice pools, the first moment of the engagement phase begins. When a moment begins, anyone who would like to act in that moment may commit dice to the moment by setting them aside. You can choose to commit only a portion of your dice pool to the moment by splintering them from their current dice pool and you can also commit multiple fragmentary dice pools to a single moment. Once everyone has committed their dice, they may modify them by combining or splitting them and then everyone rolls their dice at the same time. The dice that have been rolled are resolved in the following manner: the dice pools with the fewest dice are resolved first, if two dice pools have the same number of dice, then the dice pool with the larger sized dice goes first. If that does not resolve the tie, then the GM simply decides which action is resolved first. This means that modifications to your dice pool may cause your actions to be resolved earlier or later than they otherwise would be. Reckless actions will have fewer dice and so will be resolved earlier while careful actions will have more dice and so be resolved later. Once all of the rolls committed to a moment have been resolved, the next moment of the round begins and everyone who has dice remaining in their dice pool may choose to commit dice to the next moment following the same rules as in the prior moment."
    :example ""}

   :actions
   {:header "Actions"
    :body "There are two types of actions: setup actions and engagement actions. Setup actions are small adjustments that don’t take much focus, time, or effort. Engagement actions are where the bulk of the encounter takes place and they will usually require much more from you and have a risk of failure. Generally speaking, if an action requires a check, then it is likely an engagement action and if an action only requires a passive check or no check at all, then it is likely a setup action. Each combatant can take two setup actions during the setup phase and two engagement actions during the engagement phase. Combatants may take additional setup actions during the engagement phase as part of their engagement actions, however, each one of these setup actions inflicts a -1d penalty on both of their engagement actions. This penalty is applied even if you did not use either of your two setup actions in the setup phase. You may sacrifice one of your engagement actions to prevent these penalties from being applied to your other engagement action. You cannot take more than six setup actions in a round and you can’t take more than two setup actions in a moment. Engagement actions are further divided into initiations and reactions. Initiations require you to have focus on a target while reactions can only be used when you are the target of another creature's initiation. Listed below are some examples of the two kinds of actions."
    :example ""}})


(def damage
  {:overview
   {:header "Overview"
    :body "Both in and out of encounters, your character will have to react to events around them in an attempt to prevent harm to themself. Often times, when these reactions fail, your character will take damage. This damage is tracked separately for each of the four domains: physical, spiritual, mental, and social. As damage accumulates in each of the domains, you will be inflicted with more and more serious conditions."
    :example nil}

   :injuries
   {:header "Injuries"
    :body "If an attack targets you and the result is equal to or greater than your reaction, then you take a minor injury. If the attack roll is equal to or greater than the reaction roll + 3, then you take a major injury. When you are inflicted with an injury, record the severity of the injury and the domain in which it was inflicted. It may also help to keep a running total of the points of damage you have in each domain. Minor injuries inflict one point of damage while major injuries inflict two points of damage."
    :example ""}

   :conditions
   {:header "Conditions"
    :body "There are three conditions that can apply to each domain that are inflicted on your character as they take increasing numbers of injuries: Wounded, Incapacitated, and Dead. You will make health checks to determine when these conditions are applied and the health check for each domain is that domain's continuation skill. If you receive an injury in a domain and the new total number of injuries exceeds half of the passive result of your health check rounded favorably, then you become inflicted with the Wounded condition in that domain. If you are inflicted with the Wounded condition and you take further damage in that domain, you must make a health check for that domain where the target number is equal to the new damage total. If your health check fails, then you become inflicted with the Incapacitated condition in that domain. If at any point, the total damage in a domain exceeds your maximum possible roll, then you are inflicted with the final condition in that domain - Dead. Whenever your wounds heal, look at your most severe condition in that domain. If you rolled to receive that condition, then roll a health check now where the target number is equal to your new damage total. If you succeed, then remove the condition. If the condition was received as a result of passing a threshold, then compare your new damage total to the threshold. If it is equal to or lesser than the damage threshold, then remove the condition. If inflicted with the Death condition in the Physical, Spiritual, or Mental domain, your character is no longer playable unless there is some process or item that specifically reverses the condition. The Death condition in the Social domain is relative to the circumstances in which it was acquired and so your character is still playable, however, the individuals that you are “dead to” so to speak may never want to interact with you again or they may become hostile to you."
    :example ""}

   :recovery
   {:header "Recovery"
    :body "Minor injuries will heal naturally after 2 days while major injuries will heal naturally after 1 week. You may come across items or techniques that can be used to heal injuries quicker than through natural processes."
    :example nil}})

(def conditions {:wounded {:header "Wounded"
                           :body {:effect "As a result of the injuries you have accumulated, your performance in a particular domain has diminished. While wounded, a -1d penalty is applied to all checks made in the domain you are wounded in."
                           :duration "This condition lasts until the total injuries in the relevant domain are equal to or lower than the threshold determined by that domain's health check."}}
                 :incapacitated {:header "Incapacitated"
                                 :body {:effect "You have accumulated enough injuries that your character is no longer in control of their body, mind, spirit, or reputation. When this condition is inflicted in the physical, spiritual, or mental domain, your character becomes unconscious, despondent, or unstable, respectively. You have no control over your character in any of these circumstances. In the case of social incapacitation, people may avoid associating with you or may ignore you completely."
                                 :duration "Whenever your wounds heal, make a health check where the target number is your new damage total. If you succeed, then this condition is removed."}}
                 :dead {:header "Dead"
                        :body {:effect "You have accumulated more injuries than your body, mind, spirit, or reputation can handle. When this condition is inflicted in the physical, spiritual, or mental domain, the injuries accumulated in those domains no longer heal naturally over time. In the case of the social domain, your relationships with specific individuals or groups may be permanently severed and trying to interact with them may cause them to become hostile."
                        :duration "Unless a certain circumstance, process, or item says otherwise, this condition is permanent."}}
                 :exhausted {:header "Exhausted" 
                             :body{:effect "Whether through over-exertion, sickness, or some other unfortunate circumstance, you have become exhausted and your overall capabilities are diminished. This condition can be applied any number of times adding additional levels of exhaustion. Each level of exhaustion inflicts a -1d penalty on all of your checks"
                             :duration "Six hours of uninterrupted sleep removes one level of exhaustion."}}
                 :surprised {:header "Surprised"
                             :body {:effect "Events have unfolded that were so unexpected that it takes your mind a bit to catch up. You cannot take any actions while surprised."
                             :duration "The surprise fades away after a few seconds."}}
                 :blinded {:header "Blinded"
                           :body {:effect "Some damage or barrier prevents you from seeing. Actions that depend on your sense of sight will receive penalties as determined by the Game Master."
                           :duration "Your sense of sight returns once the barrier is removed or damage healed."}}
                 :deafened {:header "Deafened"
                            :body {:effect "Some damage or barrier prevents you from hearing. Actions that depend on your sense of hearing will receive penalties as determined by the Game Master."
                            :duration "Your sense of hearing returns once the barrier is removed or damage healed."}}
                 :constrained {:header "Constrained"
                               :body {:effect "You have become tangled, tied, or grappled and your movement is restricted. Actions that depend on your ability to move will receive penalties as determined by the Game Master."
                               :duration "Your freedom of movement returns when the restraints are removed."}}
                 :frightened {:header "Frightened"
                              :body {:effect "Your instinctual desire to preserve your life and well-being take over in the face of great danger. You cannot move closer to where you believe the source of your fear is."
                              :duration "This effect lasts until the source of your fear takes a major wound or until you deal any damage to it."}}})

(def stats 
  {:coordination "Coordination represents a creature's physical strength, flexibility, and control. A creature with high coordination will hit harder and faster and be able to perform impressive feats of athleticism and acrobatics."
 :reflexes "Reflexes represents the speed and accuracy of a creature's reactions. A creature with high reflexes will be able to defend themself from incoming attacks much more reliably."
 :endurance "Endurance represents how much of a beating a creature can take as well as their resistance to sickness, disease, and exhaustion. A creature with high endurance will manage to survive where most other would not."
 :exertion "Exertion represents the strength of a creature's sense of self. A creature with high exertion is capable of handling enormous amounts of aethereal energy without fear of being consumed by it."
 :instinct "Instinct represents the strength and reliability of a creature's gut reactions. A creature with high instinct is very sensitive to the constantly shifting aether and are able to block out or reinforce the magic that touches their spirit."
 :perseverance "Perseverance represents a creature's ability to maintain their sense of self in the face of doubt and temptation. A creature with high perseverance is able to absorb large amounts of foreign aethereal energy without losing themself to the chaos."
 :concentration "Concentration represents a creature's ability to focus on a single task and avoid distractions. A creature with high concentration is able to accomplish more impressive feats with greater efficiency."
 :recognition "Recognition represents a creature's attention to detail. A creature with high recognition will often be able to notice the small things that others find insignificant. They are attentive to their surroundings and usually have great memories."
 :comprehension "Comprehension represents a creature's ability to absorb and retain information. A creature with high comprehension is better able to understand the underlying principles of that which they are learning often causing them to pick up skills quickly and can make them great teachers."
 :persuasion "Persuasion represents a creature's ability to sway the dispositions of others through charm, intimidation, or deception. A creature with high persuasion is able to get what they want from people without resorting to violence."
 :insight "Insight represents how well a creature picks up on the many unspoken facets of a social environment. A creature with high insight can often discern the true motives and character of those they are interacting with."
 :connections "Connections represents how many relationships and affiliations a creature has, how deep those connections run, and how good they are at calling upon those connections for favors. A creature with high connections always knows a guy who knows a guy whether that is through a membership in a group, a trade deal they made years ago, or simply a friend they met throughout their journeys."})