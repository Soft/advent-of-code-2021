(ns advent-of-code-2021.day-7
  (:require [clojure.string :as str]))

(defn parse-input [input]
  (map #(Integer/parseInt %)
       (str/split (str/trim input) #",")))

(defn abs [n]
  (max n (- n)))

(defn cost-1 [start end]
  (abs (- start end)))

(defn solve [cost-fn positions]
  (let [min-pos (apply min positions)
        max-pos (apply max positions)
        total-cost #(apply + (map (partial cost-fn %) positions))
        costs (map total-cost
                   (range min-pos
                          (inc max-pos)))]
    (apply min costs)))

(defn part-1 [input]
  (solve cost-1 (parse-input input)))

(defn cost-2 [start end]
  (let [n (abs (- start end))]
    (/ (* n (inc n)) 2)))

(defn part-2 [input]
  (solve cost-2 (parse-input input)))

