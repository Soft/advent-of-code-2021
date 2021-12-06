(ns advent-of-code-2021.day-5
  (:require [clojure.string :as str]))

(defn parse-input [input]
  (map (fn [line]
         (let [[_ x1 y1 x2 y2]
               (re-matches #"(\d+),(\d+) -> (\d+),(\d+)" line)]
           [[(Integer/parseInt x1)
             (Integer/parseInt y1)]
            [(Integer/parseInt x2)
             (Integer/parseInt y2)]]))
       (str/split-lines input)))

(defn inclusive-range [start end]
  (if (>= end start)
    (range start (inc end))
    (range start (dec end) -1)))

(defn line-coords [[[x1 y1] [x2 y2]]]
  (cond
    (= x1 x2)
    (map #(vector x1 %) (inclusive-range y1 y2))
    (= y1 y2)
    (map #(vector % y1) (inclusive-range x1 x2))
    :else
    (map vector
         (inclusive-range x1 x2)
         (inclusive-range y1 y2))))

(defn place-hydrothermal-vents [coords]
  (->> coords
       (map line-coords)
       (apply concat)
       (reduce #(update %1 %2 (fnil inc 0)) {})))

(defn solve [coords]
  (->> coords
       place-hydrothermal-vents
       vals
       (filter #(> % 1))
       count))

(defn part-1 [input]
  (let [coords (parse-input input)
        filtered (filter (fn [[[x1 y1] [x2 y2]]]
                           (or (= x1 x2) (= y1 y2)))
                         coords)]
    (solve filtered)))

(defn part-2 [input]
  (solve (parse-input input)))

