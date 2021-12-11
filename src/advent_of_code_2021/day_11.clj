(ns advent-of-code-2021.day-11
  (:require [clojure.string :as str]))

(defn parse-map [input]
  (mapv (partial mapv #(Integer/parseInt (str %)))
        (str/split-lines input)))

(defn update-all [fun m]
  (mapv (partial mapv fun) m))

(defn fully-charged? [m coord]
  (> (get-in m coord) 9))

(defn matching-cells [fun m]
  (for [[y row] (map-indexed vector m)
        [x cell] (map-indexed vector row)
        :when (fun m [y x])]
    [y x]))

(defn dimensions [m]
  {:pre [(not-empty m)]}
  [(count (first m)) (count m)])

(defn adjacent [m [y x]]
  (let [[w h] (dimensions m)]
    (for [y-offset (range -1 2)
          x-offset (range -1 2)
          :let [y-val (+ y y-offset)
                x-val (+ x x-offset)]
          :when (and
                 (not (and (zero? y-offset)
                           (zero? x-offset)))
                 (<= 0 y-val (dec h))
                 (<= 0 x-val (dec w)))]
      [y-val x-val])))

(defn flash [flashed m coord]
  (if (and (fully-charged? m coord)
           (not (contains? @flashed coord)))
    (let [neighbors (adjacent m coord)]
      (swap! flashed conj coord)
      (as-> (assoc-in m coord 0) m
        (reduce (fn [m coord]
                  (if-not (contains? @flashed coord)
                    (update-in m coord inc)
                    m)) m neighbors)
        (reduce (partial flash flashed) m neighbors)))
    m))

(defn step [m]
  (let [flashed (atom #{})
        m (as-> (update-all inc m) m
            (reduce (partial flash flashed)
                    m
                    (matching-cells fully-charged? m)))]
    [m (count @flashed)]))

(defn simulate [m]
  (iterate (fn [[m total]]
             (let [[m flashes] (step m)]
               [m (+ total flashes)]))
           [m 0]))

(defn part-1 [input]
  (-> (parse-map input)
      simulate
      (nth 100)
      second))
