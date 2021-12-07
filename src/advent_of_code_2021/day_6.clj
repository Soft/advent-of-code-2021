(ns advent-of-code-2021.day-6
  (:require [clojure.string :as str]))

(defn parse-input [input]
  (map #(Integer/parseInt %)
       (str/split (str/trim input) #",")))

(defn fish-counts [input]
  (reduce (fn [acc [days count]]
            (assoc acc days count))
          (into [] (repeat 9 0))
          (frequencies input)))

(defn step [fish]
  (let [new-fish (get fish 0)]
    (update
     (conj (into [] (rest fish)) new-fish)
     6
     (partial + new-fish))))

(defn simulate [input]
  (iterate step (fish-counts input)))

(defn solve [days input]
  (apply + (nth (simulate (parse-input input)) days)))

(defn part-1 [input]
  (solve 80 input))

(defn part-2 [input]
  (solve 256 input))
