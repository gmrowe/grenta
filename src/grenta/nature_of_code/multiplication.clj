(ns grenta.nature-of-code.multiplication
  (:require [grenta.nature-of-code.vec2 :as vec2]
            [quil.core :as q]
            [quil.middleware :as m]))


(defn draw-example
  [_state]
  (let [mouse [(q/mouse-x) (q/mouse-y)]
        center [(/ (q/width) 2) (/ (q/height) 2)]
        scaled (vec2/mul-scalar mouse 0.5)]
    (q/with-translation center
      (q/background 255)
      (q/stroke-weight 2)
      (q/stroke 200)
      (apply q/line 0 0 (vec2/subtract mouse center))
      (q/stroke-weight 4)
      (q/stroke 0)
      (apply q/line
        0
        0
        (-> mouse
            (vec2/subtract center)
            (vec2/mul-scalar 0.5))))))


(comment
  (q/defsketch example      ;; Define a new sketch named example
    :title "Multiplication" ;; Set the title of the sketch
    :settings #(q/smooth 2) ;; Turn on anti-aliasing
    :draw draw-example      ;; Specify the draw fn
    :size [400 400]
    :middleware [m/fun-mode]
    :features [:no-safe-fns])
)

