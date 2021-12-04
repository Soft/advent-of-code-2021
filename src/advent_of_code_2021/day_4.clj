(ns advent-of-code-2021.day-4
  (:require [clojure.string :as str]))

(defn parse-board [input]
  (loop [input (rest input)
         board []]
    (let [current (first input)]
      (if (or (not current)
              (str/blank? current))
        [board input]
        (recur (rest input)
               (conj board
                     (mapv #(Integer/parseInt %)
                           (filter (complement str/blank?)
                                   (str/split current #"\s+")))))))))

(defn parse-boards [input]
  (loop [boards []
         input input]
    (if (empty? input)
      boards
      (let [[board left] (parse-board input)]
        (recur (conj boards board)
               left)))))

(defn board->positions [board]
  (let [positions (atom {})]
    (doseq [[y row] (map-indexed vector board)]
      (doseq [[x cell] (map-indexed vector row)]
        (swap! positions assoc cell [x y])))
    @positions))

(defn make-board [w h v]
  (into [] (repeat h (into [] (repeat w v)))))

(defn parse-input [input]
  (let [lines (str/split-lines input)
        numbers (mapv #(Integer/parseInt %)
                      (str/split (first lines) #","))
        boards (mapv (fn [board]
                       (let [h (count board)
                             w (count (first board))]
                         [(board->positions board) (make-board w h false)]))
                     (parse-boards (rest lines)))]
    [numbers boards]))

(defn transpose [m]
  (apply mapv vector m))

(defn full-row? [board]
  (some identity
        (map (partial every? identity) board)))

(defn bingo? [board]
  (or (full-row? board)
      (full-row? (transpose board))))

(defn record-number [positions board number]
  (when-let [[x y] (get positions number)]
    (assoc-in board [y x] true)))

(defn play-number [number boards] ;; Returns ([[positions board] won]...)
  (map (fn [[positions board]]
         (if-let [new-board (record-number positions board number)]
           [[positions new-board] (bingo? new-board)]
           [[positions board] false]))
       boards))

(defn play-1 [numbers boards] ;; Returns winning [[positions board] number]
  (loop [numbers numbers
         boards boards]
    (when-let [number (first numbers)]
      (let [new-state (play-number number boards)] ;; Play a number on all boards
        (if-let [[[positions board] _] ;; Was there a winner?
                 (first (filter second new-state))]
          [[positions board] number] ;; Winner found
          (recur (rest numbers) (map first new-state)))))))

(defn positions-state [positions board]
  (map (fn [[number [x y]]]
         [number (get-in board [y x])])
       positions))

(defn unmarked-numbers [positions board]
  (->> (positions-state positions board)
       (filter (complement second))
       (map first)))

(defn solve [play-fn input]
  (let [[numbers boards] (parse-input input)
        [[positions board] final-number] (play-fn numbers boards)
        unmarked-numbers (unmarked-numbers positions
                                           board)]
    (* (apply + unmarked-numbers) final-number)))

(defn part-1 [input]
  (solve play-1 input))

(defn play-2 [numbers boards]
  (loop [numbers numbers
         boards boards]
    (when-let [number (first numbers)]
      (let [new-state (play-number number boards)]
        (if (and (not (second new-state)) ;; This was the final board
                 (second (first new-state))) ;; ...and now it won
          (let [[[positions board] _] (first new-state)]
            [[positions board] number])
          (recur (rest numbers)
                 (map first (filter (complement second) new-state))))))))

(defn part-2 [input]
  (solve play-2 input))
