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

(defn safest-path [m source target]
  (loop [queue (priority-map source 0)
         total-safety {source 0}]
    (when-not (empty? queue)
      (let [[u w] (peek queue)]
        (if (= u target)
          (get total-safety target)
          (let [[queue total-safety]
                (reduce
                 (fn [[queue total-safety] v]
                   (let [alt (+ (get total-safety u) (get-in m v))]
                     (if (< alt (get total-safety v Long/MAX_VALUE))
                       [(assoc queue v alt)
                        (assoc total-safety v alt)]
                       [queue total-safety])))
                 [(pop queue) total-safety]
                 (neighbors m u))]
            (recur queue total-safety)))))))

(defn part-1 [input]
  (let [m (parse-map input)
        [x y] (map dec (dimensions m))]
    (safest-path m [0 0] [y x])))

(defn expand-row [times offset row]
  (->> (repeat times row)
       (map-indexed
        (fn [n v]
          (map #(inc (mod (dec (+ % n offset)) 9)) v)))
       (apply concat)
       (into [])))

(defn expand-chunk [times offset m]
  (map (partial expand-row times offset) m))

(defn expand-map [times m]
  (->> (repeat times m)
       (map-indexed #(expand-chunk times %1 %2))
       (apply concat)
       (into [])))

(defn part-2 [input]
  (let [m (expand-map 5 (parse-map input))
        [x y] (map dec (dimensions m))]
    (safest-path m [0 0] [y x])))
