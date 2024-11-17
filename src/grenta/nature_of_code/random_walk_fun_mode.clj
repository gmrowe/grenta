(ns grenta.nature-of-code.random-walk-fun-mode
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [clojure.data.generators :as gen]))

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
  (let [direction (condp = (rand-int 9)
                    0 [0 0]
                    1 [0 -1]
                    2 [-1 1]
                    3 [-1 0]
                    4 [-1 -1]
                    5 [1 -1]
                    6 [1 0]
                    7 [0 1]
                    8 [1 1])
        distance-factor (* 3.0 (q/random-gaussian))
        deltap (mapv (partial * distance-factor) direction)]
    {:old new
     :new (mapv + deltap new)}))

(defn setup
  []
  (q/background 200)
  {:old nil
   :new [(/ (q/width) 2) (/ (q/height) 2)]})

(defn draw
  [{:keys [old new]}]
  (let [black (q/color 0)
        red (q/color 255 0 0)
        start (if old old [(/ (q/width) 2) (/ (q/height) 2)])]
    (q/stroke black)
    (q/line start new)))

(q/defsketch example      ;; Define a new sketch named example
  :title "Random walker"  ;; Set the title of the sketch
  :settings #(q/smooth 2) ;; Turn on anti-aliasing
  :setup setup            ;; Specify the setup fn
  :draw draw              ;; Specify the draw fn
  :update update-position-gaussian
  :size [640 240]
  :middleware [m/fun-mode]
  :features [])

