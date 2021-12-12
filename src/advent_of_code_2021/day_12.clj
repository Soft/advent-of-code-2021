(ns advent-of-code-2021.day-12
  (:require [clojure.string :as str]))

(defn parse-graph [input]
  (reduce
   (fn [graph line]
     (let [[start end] (str/split line #"-" 2)
           start (keyword start)
           end (keyword end)]
       (-> graph
           (update start (fnil conj #{}) end)
           (update end (fnil conj #{}) start))))
   {}
   (str/split-lines input)))

(def large-cave?
  (memoize
   (fn [cave]
     (let [name (name cave)]
       (= name (str/upper-case name))))))

(defn traverse-helper [visited graph current end]
  (do
    (when-not (large-cave? current)
      (swap! visited conj current))
    (if-not (= current end)
      (let [nodes (current graph)]
        (apply concat
               (for [node nodes
                     :when (or (large-cave? node)
                               (not (contains? @visited node)))
                     :let [paths (traverse-helper (atom @visited) graph node end)]]
                 (map #(concat [current] %) paths))))
      [[current]])))

(defn traverse [graph]
  (traverse-helper (atom #{}) graph :start :end))

(defn part-1 [input]
  (-> (parse-graph input)
      traverse
      count))
