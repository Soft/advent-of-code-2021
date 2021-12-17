(ns advent-of-code-2021.day-16
  (:require [clojure.string :as str]
            [clojure.walk :as walk]))

(def hex-char->bool-vec
  (memoize
   (fn [c]
     (let [v (Character/digit c 16)]
       (mapv #(bit-test v %) (range 3 -1 -1))))))

(defn hex->bits [s]
  (mapcat hex-char->bool-vec s))

(defn bits->int [bits]
  (reduce
   (fn [v [n b]]
     (if b
       (bit-or v (bit-shift-left 1 n))
       v))
   0
   (map vector
        (range (dec (count bits)) -1 -1)
        bits)))

(defn read-bits [bits n]
  [(take n bits) (drop n bits)])

(defn read-int [bits n]
  (let [[num-bits bits] (read-bits bits n)]
    [(bits->int num-bits) bits]))

(defn read-literal-value [bits]
  (let [[nibbles bits]
        (loop [nibbles []
               bits bits]
          (let [[value bits] (read-int bits 5)
                num (bit-and 0xf value)]
            (if (bit-test value 4)
              (recur (conj nibbles num) bits)
              [(conj nibbles num) bits])))
        value (reduce
               (fn [v [n nibble]]
                 (+ v (bit-shift-left nibble (* n 4))))
               0
               (map-indexed vector (reverse nibbles)))]
    [value bits]))

(declare read-packet)

(defn read-packets-until [pred bits]
  (loop [packets []
         bits bits]
    (if (pred packets bits)
      [packets bits]
      (let [[packet bits] (read-packet bits)]
        (recur (conj packets packet) bits)))))

(defn read-count-packets [bits n]
  (read-packets-until
   (fn [packets bits]
     (= (count packets) n))
   bits))

(defn read-all-packets [bits]
  (read-packets-until
   (fn [packets bits]
     (empty? bits))
   bits))

(defn read-sub-packets [bits]
  (let [[length-type bits] (read-int bits 1)
        [length bits]
        (read-int bits
                  (case length-type
                    0 15
                    1 11))
        [packets bits]
        (case length-type
          0 (let [[packets-bits bits] (read-bits bits length)]
              [(first (read-all-packets packets-bits)) bits])
          1 (read-count-packets bits length))]
    [packets bits]))

(defn read-packet-header [bits]
  (let [[version bits] (read-int bits 3)
        [type bits] (read-int bits 3)]
    [{:version version
      :type type}
     bits]))

(defn read-operator-packet [bits]
  (let [[packets bits] (read-sub-packets bits)]
    [{:packets packets} bits]))

(defn read-literal-packet [bits]
  (let [[value bits] (read-literal-value bits)]
    [{:value value} bits]))

(defn read-packet [bits]
  (let [[{type :type :as header} bits] (read-packet-header bits)
        [packet bits] (case type
                        4 (read-literal-packet bits)
                        (read-operator-packet bits))]
    [(merge header packet) bits]))

(defn part-1 [input]
  (->>
   (str/trimr input)
   hex->bits
   read-packet
   first
   (tree-seq :packets :packets)
   (map :version)
   (apply +)))


(defn var-op [op node]
  (apply op (:packets node)))

(defn bin-op [op node]
  {:pre [(= (count (:packets node)) 2)]}
  (if (op (first (:packets node))
          (second (:packets node)))
    1 0))

(defmulti evaluate-node :type)

(defmethod evaluate-node 0 [node]
  (var-op + node))

(defmethod evaluate-node 1 [node]
  (var-op * node))

(defmethod evaluate-node 2 [node]
  (var-op min node))

(defmethod evaluate-node 3 [node]
  (var-op max node))

(defmethod evaluate-node 4 [node]
  (:value node))

(defmethod evaluate-node 5 [node]
  (bin-op > node))

(defmethod evaluate-node 6 [node]
  (bin-op < node))

(defmethod evaluate-node 7 [node]
  (bin-op = node))

(defmethod evaluate-node :default [node]
  node)

(defn evaluate [prog]
  (walk/postwalk evaluate-node prog))

(defn part-2 [input]
  (->>
   (str/trimr input)
   hex->bits
   read-packet
   first
   evaluate))
