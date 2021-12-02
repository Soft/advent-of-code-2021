(ns advent-of-code-2021.day-1
  (:require [clojure.string :as str]))

(defn to-numbers [input]
  (map #(Integer/parseInt %)
       (str/split-lines input)))

(defn increases [coll]
  (first
   (reduce (fn [[acc prev] n]
             (if (> n prev)
               [(inc acc) n]
               [acc n]))
           [0 (first coll)]
           (rest coll))))

(defn part-1 [input]
  (increases (to-numbers input)))

(defn part-2 [input]
  (->> input
       to-numbers
       (partition 3 1)
       (map #(apply + %))
       increases))

