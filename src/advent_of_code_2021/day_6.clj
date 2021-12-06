(ns advent-of-code-2021.day-6
  (:require [clojure.string :as str]))

(defn parse-input [input]
  (map #(Integer/parseInt %)
       (str/split (str/trim input) #",")))

(defn step [fish]
  (reduce (fn [acc fish]
            (if (zero? fish)
              (conj acc 6 8)
              (conj acc (dec fish))))
          []
          fish))

(def simulate (partial iterate step))

(defn part-1 [input]
  (count (nth (simulate (parse-input input)) 80)))

