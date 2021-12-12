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

(defn traverse-helper [state visit allowed? graph current end]
  (when-not (large-cave? current)
    (swap! state visit current))
  (if-not (= current end)
    (apply concat
           (for [node (current graph)
                 :when (or (large-cave? node)
                           (allowed? @state node))]
             (map (partial concat [current])
                  (traverse-helper
                   (atom @state) visit allowed? graph node end))))
    [[current]]))

(defn traverse [graph]
  (traverse-helper (atom #{}) conj (comp not contains?) graph :start :end))

(defn part-1 [input]
  (-> (parse-graph input)
      traverse
      count))

(defn part-2 [input]
  (let [graph (parse-graph input)]
    (->> (filter #(and (not (large-cave? %))
                       (not= :start %)
                       (not= :end %))
                 (keys graph))
         (reduce
          (fn [paths special]
            (apply (partial conj paths)
                   (traverse-helper
                    (atom {})
                    #(update %1 %2 (fnil inc 0))
                    (fn [state node]
                      (let [visits (node state 0)]
                        (if (= node special)
                          (< visits 2)
                          (zero? visits))))
                    graph
                    :start
                    :end)))
          #{})
         count)))

