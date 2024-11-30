(ns grenta.nature-of-code.magnitude
  (:require [grenta.nature-of-code.vec2 :as vec2]
            [quil.core :as q]
            [quil.middleware :as m]))


(defn draw-example
  [_state]
  (let [mouse [(q/mouse-x) (q/mouse-y)]
        center [(/ (q/width) 2) (/ (q/height) 2)]
        diff (vec2/subtract mouse center)
        magnitude (vec2/magnitude diff)]
    (q/background 250)
    (q/fill 20)
    (q/rect 0 5 magnitude 10)
    (q/with-translation [(/ (q/width) 2) (/ (q/height) 2)]
      (apply q/line 0 0 diff))))


(comment
  (q/defsketch example ;; Define a new sketch named example
    :title "Magnitude" ;; Set the title of the sketch
    :settings #(q/smooth 2) ;; Turn on anti-aliasing
    :draw draw-example      ;; Specify the draw fn
    :size [400 400]
    :middleware [m/fun-mode]
    :features [:no-safe-fns])
)

