(ns grenta.nature-of-code-subtration
  (:require [grenta.nature-of-code.vec2 :as vec2]
            [quil.core :as q]
            [quil.middleware :as m]))

(defn draw-example
  [_state]
  (let [mouse-x (q/mouse-x)
        mouse-y (q/mouse-y)
        center-x (/ (q/width) 2)
        center-y (/ (q/height) 2)
        [diff-x diff-y] (vec2/subtract [mouse-x mouse-y] [center-x center-y])]
    (q/background 255)
    (q/stroke 200)
    (q/stroke-weight 4)
    (q/line 0 0 mouse-x mouse-y)
    (q/line 0 0 center-x center-y)
    (q/stroke 0)
    (q/with-translation [(/ (q/width) 2) (/ (q/height) 2)]
                        (q/line 0 0 diff-x diff-y))))
(comment
  (q/defsketch example      ;; Define a new sketch named example
    :title "Subtraction"    ;; Set the title of the sketch
    :settings #(q/smooth 2) ;; Turn on anti-aliasing
    :draw draw-example      ;; Specify the draw fn
    :size [400 400]
    :middleware [m/fun-mode]
    :features [:no-safe-fns])
)
