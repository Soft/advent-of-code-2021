(ns advent-of-code-2021.tests
  (:require [clojure.test :refer [deftest is]]
            [clojure.string :as str]
            [clojure.java.io :as io]
            [advent-of-code-2021.core :as core]))

(def day-13-part-2
  (str/triml
   "
████  ██  ████ █  █ █    █  █ ████ ████
█    █  █ █    █  █ █    █  █    █ █   
███  █    ███  ████ █    ████   █  ███ 
█    █    █    █  █ █    █  █  █   █   
█    █  █ █    █  █ █    █  █ █    █   
████  ██  █    █  █ ████ █  █ ████ █   "))

(def answers
  {1  [1709 1761]
   2  [2019945 1599311480]
   3  [4191876 3414905]
   4  [12796 18063]
   5  [5576 18144]
   6  [353274 1609314870967]
   7  [359648 100727924]
   8  [416]
   9  [575 1019700]
   10 [462693 3094671161]
   11 [1652 220]
   12 [3298 93572]
   13 [678 day-13-part-2]})

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

