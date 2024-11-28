(ns grenta.nature-of-code.random-walk-fun-mode
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [clojure.data.generators :as gen]
            [clojure.math :as math]))

(defn update-position-cardinal
  [{:keys [old new]}]
  (let [deltap (condp = (rand-int 4)
                 0 [0 1]
                 1 [0 -1]
                 2 [1 0]
                 3 [-1 0])]
    {:old new
     :new (mapv + deltap new)}))

(defn update-position-universal
  ([state] (update-position-universal state #(rand-int 9)))
  ([{:keys [old new]} gen-fn]
   (let [deltap (condp = (gen-fn)
                  0 [0 0]
                  1 [0 -1]
                  2 [-1 1]
                  3 [-1 0]
                  4 [-1 -1]
                  5 [1 -1]
                  6 [1 0]
                  7 [0 1]
                  8 [1 1])]
     {:old new
      :new (mapv + deltap new)})))

(defn update-position-skew-southeast
  [state]
  (update-position-universal
    state
    #(gen/weighted
       {(gen/uniform 0 6) 11
        (gen/uniform 6 9) 9})))

(defn update-position-gaussian
  [{:keys [new]}]
  (let [direction (condp = (q/floor (q/random 0 9))
                    0 [0 -1]
                    1 [0 0]
                    2 [0 1]
                    3 [-1 -1]
                    4 [-1 0]
                    5 [-1 1]
                    6 [1 -1]
                    7 [1 0]
                    8 [1 1])
        distance-factor (* 3.0 (q/random-gaussian))
        deltap (mapv (partial * distance-factor) direction)]
    {:old new
     :new (mapv + deltap new)}))

(defn accept-reject-generator
  [lo hi]
  (let [n (rand)]
    (if (< n (rand))
      (let [factor (math/signum (- (rand) 0.5))
            half-range (/ (- hi lo) 2)
            mid (+ lo half-range)]
        (long (math/floor (+ mid (* n factor half-range)))))
      (recur lo hi))))

(defn update-position-custom-distribution
  [state]
  (let [dx (accept-reject-generator -10 10)
        dy (accept-reject-generator -10 10)]
    {:old (:new state)
     :new (mapv + [dx dy] (:new state))}))

(defn setup
  []
  (q/background 200)
  {:old nil
   :new [(/ (q/width) 2) (/ (q/height) 2)]})

(defn hexstr->qcolor
  [hex]
  (let [hexn (if (= \# (first hex)) (subs hex 1) hex)
        rgb (mapv #(Integer/parseInt % 16)
              [(subs hexn 0 2) (subs hexn 2 4) (subs hexn 4 6)])]
    (apply q/color rgb)))

(defn draw
  [{:keys [old new]}]
  (let [start (if old old [(/ (q/width) 2) (/ (q/height) 2)])]
    (q/stroke (hexstr->qcolor "#4e19F6"))
    (q/line start new)))

(comment
  (q/defsketch example      ;; Define a new sketch named example
    :title "Random walker"  ;; Set the title of the sketch
    :settings #(q/smooth 2) ;; Turn on anti-aliasing
    :setup setup            ;; Specify the setup fn
    :draw draw              ;; Specify the draw fn
    :update update-position-gaussian
    :size [640 240]
    :middleware [m/fun-mode]
    :features [])
)
