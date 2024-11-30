(ns grenta.nature-of-code.mover
  (:require [grenta.nature-of-code.vec2 :as vec2]
            [quil.core :as q]
            [quil.middleware :as m]))

(defn init-mover
  "position and velocity should both be 2 element vectors"
  [[x y :as position] [dx dx :as velocity]]
  {:pos position
   :vel velocity
   :acc [0 0]})

(defn accelerate
  [mover acceleration-change]
  (update mover :acc vec2/add acceleration-change))

(defn move-one-tick
  [mover]
  (-> mover
      (update :vel vec2/add (:acc mover))
      (update :pos vec2/add (:vel mover))))

(defn check-collision
  [mover]
  (let [[x y] (:pos mover)]
    ;; Wrap at edges
    (cond-> mover
      (< x 0)
        (update :pos (fn [[_posx posy]] [(q/width) posy]))

      (< (q/width) x)
        (update :pos (fn [[_posx posy]] [0 posy]))

      (< y 0)
        (update :pos (fn [[posx _posy]] [posx (q/height)]))

      (< (q/height) y)
        (update :pos (fn [[posx _posy]] [posx 0])))))

(defn update-state
  [mover]
  (-> mover
      move-one-tick
      check-collision))

(defn draw
  [mover]
  (let [[x y] (:pos mover)]
    (q/background 255)
    (q/stroke 0)
    (q/fill 175)
    (q/ellipse x y 25 25)))


(comment
  (q/defsketch example
    :title "Normalize"
    :settings #(q/smooth 2)
    :setup #(init-mover [(/ (q/width) 2) (/ (q/height) 2)] [0 0])
    :update update-state
    :draw draw
    :size [640 400]
    :middleware [m/fun-mode]
    :features [])
)
