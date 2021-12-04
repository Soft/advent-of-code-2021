(ns advent-of-code-2021.core
  (:require
   [advent-of-code-2021.day-1 :as day-1]
   [advent-of-code-2021.day-2 :as day-2]
   [advent-of-code-2021.day-3 :as day-3]
   [advent-of-code-2021.day-4 :as day-4])
  (:gen-class))

(def solutions
  {1 [day-1/part-1 day-1/part-2]
   2 [day-2/part-1 day-2/part-2]
   3 [day-3/part-1 day-3/part-2]
   4 [day-4/part-1 day-4/part-2]})

(defn -main [day part path]
  (let [day (Integer/parseInt day)
        part (Integer/parseInt part)
        input (slurp path)]
    (if-let [solution (get-in solutions [day (dec part)])]
      (println (solution input))
      (binding [*out* *err*]
        (println "Part is not available")
        (System/exit 1)))))
