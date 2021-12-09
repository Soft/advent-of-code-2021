(ns advent-of-code-2021.day-9
  (:require [clojure.string :as str]))

(defn parse-map [input]
  (mapv (partial mapv #(Integer/parseInt (str %)))
        (str/split-lines input)))

(defn adjacent [[y x]]
  (map (fn [[y-offset x-offset]]
         [(+ y y-offset) (+ x x-offset)])
       [[-1 0] [0 1] [1 0] [0 -1]]))

(defn lowest? [m coords]
  (let [v (get-in m coords)]
    (every? #(< v %)
            (filter (complement nil?)
                    (map #(get-in m %) (adjacent coords))))))

(defn lowest-points [m]
  (for [[y row] (map-indexed vector m)
        [x cell] (map-indexed vector row)
        :when (lowest? m [y x])]
    [[y x] cell]))

(defn part-1 [input]
  (->> (parse-map input)
       lowest-points
       (map (comp inc second))
       (apply +)))

(defn basin [m coord]
  (let [visited (atom #{})]
    (letfn [(fill [coord]
              (swap! visited conj coord)
              (let [neighbors (adjacent coord)]
                (doseq [coord neighbors]
                  (when-not (or (contains? @visited coord)
                                (== (get-in m coord 9) 9))
                    (fill coord)))))]
      (fill coord))
    @visited))

(defn part-2 [input]
  (let [m (parse-map input)]
    (->> (lowest-points m)
         (map (comp count (partial basin m) first))
         (sort >)
         (take 3)
         (apply *))))
