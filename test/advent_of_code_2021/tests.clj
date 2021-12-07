(ns advent-of-code-2021.tests
  (:require [clojure.test :refer [deftest is]]
            [clojure.java.io :as io]
            [advent-of-code-2021.core :as core]))

(def answers
  {1 [1709 1761]
   2 [2019945 1599311480]
   3 [4191876 3414905]
   4 [12796 18063]
   5 [5576 18144]
   6 [353274 1609314870967]
   7 [359648]})

(defmacro make-test [name fun answer input-path]
  `(deftest ~(symbol name)
     (is (= ~answer (~fun (slurp (io/resource ~input-path)))))))

(doseq [[day fns] answers]
  (dotimes [part (count fns)]
    (let [name (symbol (format "test-day-%d-part-%d" day (inc part)))
          fun (get-in core/solutions [day part])
          answer (get-in answers [day part])
          input-path (format "day%d.txt" day)]
      (eval `(make-test ~name ~fun ~answer ~input-path)))))

