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
       [(- (* 2 x-fold) x) y]
       [x y]))
   sheet))

(defn fold-up [sheet y-fold]
  (map
   (fn [[x y]]
     (if (> y y-fold)
       [x (- (* 2 y-fold) y)]
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

(defn sheet->matrix [sheet]
  (let [max-x (first (apply (partial max-key first) sheet))
        max-y (second (apply (partial max-key second) sheet))]
    (reduce
     (fn [matrix [x y]]
       (assoc-in matrix [y x] true))
     (->> (repeat (inc max-x) false)
          (into [])
          (repeat (inc max-y))
          (into []))
     sheet)))

(defn matrix->str [matrix]
  (str/join
   \newline
   (map
    (fn [row]
      (str/join
       (map #(if % \u2588 \space) row)))
    matrix)))

(defn part-2 [input]
  (let [[sheet instructions] (parse-input input)]
    (-> (fold-sheet sheet instructions)
        sheet->matrix
        matrix->str)))
