(ns examples.odoyle
  (:require [odoyle.rules :as o]
            [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as st]))

(st/instrument)

;(s/def ::health string?)

(def rules
  (o/ruleset
    {::player-died
     [:what
      [::player ::health 0]
      :then
      (println "Hello, world")]

     ::player
     [:what
      [::player ::x x]
      [::player ::y y]
      [::player ::health health]]}))

(def *session
  (-> (reduce o/add-rule (o/->session) rules)
      (o/insert ::player ::health 0)
      (o/insert ::player ::y 1)
      (o/insert ::player ::x 1)
      ;(o/insert ::player {::x 1 ::y 1 ::health 10})
      ;(o/insert ::enemy {::x 1 ::y 1 ::health 10})
      ;(o/insert ::enemy {::x 1 ::y 1 ::health 10})
      ;(o/insert ::enemy {::x 2 ::y 2 ::health 10})
      ;(o/insert ::enemy {::x 2 ::y 2 ::health 10})
      atom))

(defn print-node [node level]
  (dotimes [_ level] (print "  "))
  (println (type node) (dissoc node :path :children :successors :child :condition))
  (doseq [child (:children node)]
    (print-node child (inc level)))
  (when-let [child (:child node)]
    (print-node child (inc level))))

(println "ALPHA")
(print-node (:alpha-node @*session) 0)
(println "BETA")
(doseq [node (vals (:beta-nodes @*session))]
  (print-node node 0))

#_
(println (o/query @*session ::get-player))
