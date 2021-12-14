(ns advent-of-code-2021.day-14
  (:require [clojure.string :as str]))

(defn parse-rules [lines]
  (reduce
   (fn [rules line]
     (let [[_ a b c] (re-matches #"(\w)(\w) -> (\w)" line)]
       (assoc rules [(first a) (first b)] (first c))))
   {}
   lines))

(defn parse-input [input]
  (let [lines (seq (str/split-lines input))
        template (mapv identity (first lines))
        rules (parse-rules (rest (rest lines)))]
    [template rules]))

(defn expand-once [rules template]
  (concat
   (take 1 template)
   (reduce
    (fn [acc [a b]]
      (if-let [c (get rules [a b])]
        (conj acc c b)
        (conj acc b)))
    []
    (partition 2 1 template))))

(defn expand [rules template]
  (iterate (partial expand-once rules) template))

(defn part-1 [input]
  (let [[template rules] (parse-input input)
        freqs (-> (expand rules template)
                  (nth 10)
                  frequencies)
        least (apply min (vals freqs))
        most (apply max (vals freqs))]
    (- most least)))

