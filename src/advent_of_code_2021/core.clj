(ns advent-of-code-2021.core
  (:require
   [clojure.pprint :refer [pprint]]
   [advent-of-code-2021.day-1 :as day-1]
   [advent-of-code-2021.day-2 :as day-2])
  (:gen-class))

(def solutions
  {1 [day-1/part-1 day-1/part-2]
   2 [day-2/part-1 day-2/part-2]})

(defn -main [day part path]
  (let [day (Integer/parseInt day)
        part (Integer/parseInt part)
        input (slurp path)]
    (println
     (if-let [day-solutions (get solutions day)]
       (if-let [solution (get day-solutions (dec part))]
         (solution input)
         "Part is not available")
       "Day is not available"))))
