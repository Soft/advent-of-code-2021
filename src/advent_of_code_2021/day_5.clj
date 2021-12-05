(ns advent-of-code-2021.day-5
  (:require [clojure.string :as str]))

(defn parse-input [input]
  (map (fn [line]
         (let [[l x1 y1 x2 y2]
               (re-matches #"(\d+),(\d+) -> (\d+),(\d+)" line)]
           [[(Integer/parseInt x1)
             (Integer/parseInt y1)]
            [(Integer/parseInt x2)
             (Integer/parseInt y2)]]))
       (str/split-lines input)))

(defn line-coords [[[x1 y1] [x2 y2]]]
  {:pre [(or (= x1 x2)
             (= y1 y2))]}
  (if (= x1 x2)
    (let [start-y (min y1 y2)
          end-y (max y1 y2)]
      (map (fn [y] [x1 y])
           (range start-y (inc end-y))))
    (let [start-x (min x1 x2)
          end-x (max x1 x2)]
      (map (fn [x] [x y1])
           (range start-x (inc end-x))))))

(defn place-hydrothermal-vents [coords]
  (->> coords
       (map line-coords)
       (apply concat)
       (reduce #(update %1 %2 (fnil inc 0)) {})))

(defn part-1 [input]
  (let [coords (parse-input input)
        filtered (filter (fn [[[x1 y1] [x2 y2]]]
                           (or (= x1 x2) (= y1 y2)))
                         coords)
        m (place-hydrothermal-vents filtered)]
    (count (filter #(> % 1) (vals m)))))

