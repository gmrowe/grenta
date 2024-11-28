(ns grenta.nature-of-code.paint-splatter
  (:require [quil.core :as q]))

(defn setup
  []
  nil)

(defn draw
  []
  (let [color-palette [(q/color 60 85 45)
                       (q/color 202 115 115)
                       (q/color 215 178 109)
                       (q/color 238 226 181)]
        r-min 2
        r-max 12
        r (q/random 2 12)
        center-x (- (/ (q/width) 2) r)
        center-y (- (/ (q/height) 2) r)
        std-dev (/ (q/width) 5)
        x (+ (* std-dev (q/random-gaussian)) center-x)
        y (+ (* std-dev (q/random-gaussian)) center-y)
        color (rand-nth color-palette)]
    (q/no-stroke)
    (q/fill color 100)
    (q/ellipse x y (* r 2) (* r 2))))


(comment
  (q/defsketch example
    :title "Paint splatter!"
    :settings #(q/smooth 2)
    :setup setup
    :draw draw
    :size [400 400])
)

