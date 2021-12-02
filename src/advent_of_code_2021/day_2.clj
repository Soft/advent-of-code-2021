(ns advent-of-code-2021.day-2
  (:require [clojure.string :as str]))

(defn parse-commands [input]
  (->> input
       (str/split-lines)
       (map (fn [line]
              (let [[command n] (str/split line #" " 2)]
                [(keyword command) (Integer/parseInt n)])))))

(defn navigate-1 [commands]
  (reduce
   (fn [[x y] [command n]]
     (case command
       :up [x (- y n)]
       :down [x (+ y n)]
       :forward [(+ x n) y]))
   [0 0]
   commands))

(defn part-1 [input]
  (->> input
       parse-commands
       navigate-1
       (apply *)))

(defn navigate-2 [commands]
  (reduce
   (fn [[aim x y] [command n]]
     (case command
       :up [(- aim n) x y]
       :down [(+ aim n) x y]
       :forward [aim (+ x n) (+ y (* aim n))]))
   [0 0 0]
   commands))

(defn part-2 [input]
  (let [[_ x y]
        (->> input
             parse-commands
             navigate-2)]
    (* x y)))
