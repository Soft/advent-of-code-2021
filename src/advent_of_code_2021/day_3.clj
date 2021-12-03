(ns advent-of-code-2021.day-3
  (:require [clojure.string :as str]))

(defn str->bitvec [s]
  (mapv #(case % \1 true \0 false) s))

(defn parse-input [input]
  (->> input
       str/split-lines
       (mapv str->bitvec)))

(defn transpose [m]
  (apply mapv vector m))

(defn bitvec->number [v]
  (->> (reverse v)
       (map-indexed #(if %2 (bit-shift-left 1 %1) 0))
       (apply bit-or)))

(defn part-1 [input]
  (let [bitvec (->> input
                    parse-input
                    transpose
                    (map frequencies)
                    (map #(< (get % false) (get % true))))
        gamma (bitvec->number bitvec)
        epsilon (bitvec->number (map not bitvec))]
    (* gamma epsilon)))

(defn prune-once [value-fn bitvecs pos]
  (let [{bits-set true
         bits-unset false
         :or {bits-set 0
              bits-unset 0}}
        (frequencies (get (transpose bitvecs) pos))
        value (value-fn bits-set bits-unset)]
    (filter #(= (get % pos) value)
            bitvecs)))

(defn prune [value-fn bitvecs]
  {:pre [(not-empty bitvecs)]}
  (let [current (atom bitvecs)]
    (dorun
     (for [pos (range (count (first bitvecs)))
           :while (> (count @current) 1)]
       (swap! current #(prune-once value-fn % pos))))
    (first @current)))

(defn oxygen-generator-rating [bitvecs]
  (bitvec->number
   (prune (fn [bits-set bits-unset]
            (if (= bits-set bits-unset)
              true
              (> bits-set bits-unset)))
          bitvecs)))

(defn CO2-scrubber-rating [bitvecs]
  (bitvec->number
   (prune (fn [bits-set bits-unset]
            (if (= bits-set bits-unset)
              false
              (< bits-set bits-unset)))
          bitvecs)))

(defn part-2 [input]
  (let [bitvecs (parse-input input)
        oxygen-rating (oxygen-generator-rating bitvecs)
        CO2-rating (CO2-scrubber-rating bitvecs)]
    (* oxygen-rating CO2-rating)))
