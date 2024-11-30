(ns grenta.nature-of-code-accelerator
  (:require [grenta.nature-of-code.vec2 :as vec2]
            [grenta.nature-of-code.mover :as mover]
            [quil.core :as q]
            [quil.middleware :as m]))

(defn init
  []
  {:mover (mover/init-mover [(/ (q/width) 2) (/ (q/height) 2)] [0 0])})

(defn update-state
  [state]
  (let [mouse [(q/mouse-x) (q/mouse-y)]
        obj (get-in state [:mover :pos])
        direction (vec2/normalize (vec2/subtract mouse obj))
        distance (vec2/distance mouse obj)
        accel-factor (* 0.8 (/ 1.0 distance))]
    (-> state
        (update :mover mover/update-state)
        (assoc-in [:mover :acc] (vec2/mul-scalar direction accel-factor))
        (update-in [:mover :vel] vec2/limit 10))))

(defn draw-example
  [state]
  (let [triangle-offsets 40
        [ddx _] (get-in state [:mover :acc])
        [dx _] (get-in state [:mover :vel])
        [x y] (get-in state [:mover :pos])]
    (q/background 200)
    (q/fill (q/color 255 0 0))
    (q/triangle x (- y triangle-offsets) x y (+ x triangle-offsets) y)
    (q/fill (q/color 10))
    (q/text (format "Velocity: %.3f" (float dx)) 10 10)
    (q/text (format "Acceleration: %.3f" (float ddx)) 10 25)))

(comment
  (q/defsketch example
    :title "Accelerator"
    :settings #(q/smooth 2)
    :setup init
    :update update-state
    :draw draw-example
    :size [400 400]
    :middleware [m/fun-mode]
    :features [])
)
