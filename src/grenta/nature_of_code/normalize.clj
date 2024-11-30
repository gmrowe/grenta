(ns grenta.nature-of-code.normalize
  (:require [grenta.nature-of-code.vec2 :as vec2]
            [quil.core :as q]
            [quil.middleware :as m]))


(defn draw-example
  [_state]
  (let [mouse [(q/mouse-x) (q/mouse-y)]
        center [(/ (q/width) 2) (/ (q/height) 2)]
        diff (vec2/subtract mouse center)]
    (q/with-translation center
      (q/background 250)
      (q/stroke-weight 2)
      (q/stroke 200)
      (apply q/line 0 0 diff)
      (q/stroke-weight 4)
      (q/stroke 10)
      (apply q/line 0 0 (vec2/mul-scalar (vec2/normalize diff) 50))
    )))

(vec2/normalize [129 15])

(comment
  (q/defsketch example      ;; Define a new sketch named example
    :title "Normalize"      ;; Set the title of the sketch
    :settings #(q/smooth 2) ;; Turn on anti-aliasing
    :draw draw-example      ;; Specify the draw fn
    :size [640 400]
    :middleware [m/fun-mode]
    :features [:no-safe-fns])
)

