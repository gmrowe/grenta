(ns grenta.nature-of-code.perlin-noise-walker
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(defn setup
  []
  (let [x-seed (q/random 10000)
        y-seed (q/random 10000)
        x-off 0.05
        y-off 0.05]
    {:x (/ (q/width) 2.0)
     :y (/ (q/height) 2.0)
     :x-state (+ x-seed x-off)
     :y-state (+ y-seed y-off)
     :x-step 0.0
     :y-step 0.0
     :x-off x-off
     :y-off y-off
     :radius 50
     :fill-color (q/color 150)
     :stroke-color (q/color 0)}))

(defn tweak-options
  [state]
  (let [my-options {:x-off 0.1
                    :y-off 0.1}]
    (merge state my-options)))

(defn update-state
  [state]
  (let [{:keys [x-state x-off y-state y-off x-step y-step]} state]
    (-> state
        tweak-options
        (update :x + x-step)
        (update :y + y-step)
        (assoc :x-step (q/map-range (q/noise x-state) 0.0 1.0 -2.0 2.0))
        (assoc :y-step (q/map-range (q/noise y-state) 0.0 1.0 -2.0 2.0))
        (update :x-state + x-off)
        (update :y-state + y-off))))

(defn draw
  [{:keys [x y radius fill-color stroke-color]}]
  (let [stroke-weight 1]
    (q/stroke stroke-color)
    (q/stroke-weight stroke-weight)
    (q/fill fill-color)
    (q/ellipse x y radius radius)))

(q/defsketch perlin-noise-walker
  :size [600 400]
  :title "Perlin noise walker"
  :settings #(q/smooth 2)
  :features [:no-safe-fns]
  :setup setup
  :update update-state
  :draw draw
  :middleware [m/fun-mode])
