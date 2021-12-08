(ns advent-of-code-2021.day-8
  (:require [clojure.string :as str]))

(defn parse-pattern [input]
  (into #{} (map (comp keyword str) input)))

(defn parse-patterns [input]
  (map parse-pattern (str/split (str/trim input) #"\s+")))

(defn parse-line [line]
  (let [[patterns digits] (str/split line #"\|" 2)]
    [(into #{} (parse-patterns patterns))
     (into [] (parse-patterns digits))]))

(defn parse-input [input]
  (map parse-line (str/split-lines input)))

(defn part-1 [input]
  (->> input
       parse-input
       (map
        (fn [[_ digits]]
          (count
           (filter #(contains? #{2 3 4 7} (count %)) digits))))
       (apply +)))

