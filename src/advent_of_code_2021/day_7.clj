(ns advent-of-code-2021.day-7
  (:require [clojure.string :as str]))

(defn parse-input [input]
  (map #(Integer/parseInt %)
       (str/split (str/trim input) #",")))

(defn abs [n] (max n (- n)))

(defn cost [pos positions]
  (apply + (map #(abs (- pos %)) positions)))

(defn solve [positions]
  (let [min-pos (apply min positions)
        max-pos (apply max positions)
        costs (map #(cost % positions)
                   (range min-pos (inc max-pos)))]
    (apply min costs)))

(defn part-1 [input]
  (solve (parse-input input)))

