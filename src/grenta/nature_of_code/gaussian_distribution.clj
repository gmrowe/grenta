(ns grenta.nature-of-code.gaussian-distribution
  (:require [quil.core :as q]))

(defn setup
  []
  nil)

(defn draw
  []
  (let [r 10
        center-x (- (/ (q/width) 2) r)
        center-y (- (/ (q/height) 2) r)
        std-dev (* r 5)
        x (+ (* std-dev (q/random-gaussian)) center-x)]
    (q/no-stroke)
    (q/fill 0 10)
    (q/ellipse x center-y (* r 2) (* r 2))))


(comment
  (q/defsketch example
    :title "Random number distribution"
    :settings #(q/smooth 2)
    :setup setup
    :draw draw
    :size [640 240])
)

