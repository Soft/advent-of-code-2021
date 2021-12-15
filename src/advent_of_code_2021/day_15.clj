(ns advent-of-code-2021.day-15
  (:require [clojure.string :as str]
            [clojure.data.priority-map :refer [priority-map]]))

(defn parse-map [input]
  (mapv (partial mapv #(Integer/parseInt (str %)))
        (str/split-lines input)))

(defn adjacent [[y x]]
  (map (fn [[y-offset x-offset]]
         [(+ y y-offset) (+ x x-offset)])
       [[-1 0] [0 1] [1 0] [0 -1]]))

(defn dimensions [m]
  {:pre [(not-empty m)]}
  [(count (first m)) (count m)])

(defn in-bounds? [m [y x]]
  (let [[w h] (dimensions m)]
    (and (<= 0 y (dec h))
         (<= 0 x (dec w)))))

(defn neighbors [m pos]
  (filter (partial in-bounds? m)
          (adjacent pos)))

(defn shortest-path [m source target]
  (loop [queue (priority-map source 0)
         distances {source 0}]
    (when-not (empty? queue)
      (let [[u w] (peek queue)]
        (if (= u target)
          (get distances target)
          (let [[queue distances]
                (reduce
                 (fn [[queue distances] v]
                   (let [alt (+ (get distances u) (get-in m v))]
                     (if (< alt (get distances v Long/MAX_VALUE))
                       [(assoc queue v alt)
                        (assoc distances v alt)]
                       [queue distances])))
                 [(pop queue) distances]
                 (neighbors m u))]
            (recur queue distances)))))))

(defn part-1 [input]
  (let [m (parse-map input)
        [x y] (map dec (dimensions m))]
    (shortest-path m [0 0] [y x])))
