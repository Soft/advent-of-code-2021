(ns advent-of-code-2021.day-13
  (:require [clojure.string :as str]))

(defn parse-sheet [lines]
  (loop [lines lines
         sheet []]
    (let [line (first lines)]
      (if (str/blank? line)
        [sheet (rest lines)]
        (let [[x y] (str/split line #"," 2)
              x (Integer/parseInt x)
              y (Integer/parseInt y)]
          (recur (rest lines)
                 (conj sheet [x y])))))))

(defn parse-instructions [lines]
  (reduce
   (fn [instructions line]
     (let [[_ kind n] (re-matches #"fold along ([xy])=(\d+)" line)
           kind (keyword kind)
           n (Integer/parseInt n)]
       (conj instructions [kind n])))
   []
   lines))

(defn parse-input [input]
  (let [lines (seq (str/split-lines input))
        [sheet lines] (parse-sheet lines)
        instructions (parse-instructions lines)]
    [sheet instructions]))

(defn fold-left [sheet x-fold]
  (map
   (fn [[x y]]
     (if (> x x-fold)
       [(- x-fold (- x x-fold)) y] ; a*x-fold - x
       [x y]))
   sheet))

(defn fold-up [sheet y-fold]
  (map
   (fn [[x y]]
     (if (> y y-fold)
       [x (- y-fold (- y y-fold))] ; a*y-fold - y
       [x y]))
   sheet))

(defn fold-sheet [sheet instructions]
  (reduce
   (fn [sheet [fold pos]]
     (case fold
       :x (fold-left sheet pos) 
       :y (fold-up sheet pos)))
   sheet
   instructions))

(defn part-1 [input]
  (let [[sheet instructions] (parse-input input)]
    (->> (take 1 instructions)
         (fold-sheet sheet)
         (into #{})
         count)))
