(ns advent-of-code-2021.day-10
  (:require [clojure.string :as str]))

(defn parse-line [line]
  (reduce
   (fn [stack c]
     (case c
       \( (conj stack \))
       \[ (conj stack \])
       \{ (conj stack \})
       \< (conj stack \>)
       (let [expected (peek stack)]
         (if (= c expected)
           (pop stack)
           (reduced [:unexpected c])))))
   []
   line))

(def scores-1 {\) 3 \] 57 \} 1197 \> 25137})

(defn part-1 [input]
  (->> (str/split-lines input)
       (map parse-line)
       (filter (comp (partial = :unexpected)
                     first))
       (map #(get scores-1 (second %)))
       (apply +)))

(def scores-2 {\) 1 \] 2 \} 3 \> 4})

(defn completion-score [stack]
  (reduce
   (fn [score c]
     (+ (* score 5) (get scores-2 c)))
   0
   (reverse stack)))

(defn part-2 [input]
  (let [scores
        (->> (str/split-lines input)
             (map parse-line)
             (filter (comp (partial not= :unexpected)
                           first))
             (map completion-score)
             sort)]
    (nth scores
         (quot (count scores) 2))))
