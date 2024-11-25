(ns grenta.nature-of-code.perlin-noise-walker
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(defn setup
  []
  (let [x-seed (q/random 1000)
        y-seed (q/random 1000)
        x-step 0.01
        y-step 0.01]
    {:x (q/map-range (q/noise x-seed) 0.0 1.0 0.0 (q/width))
     :y (q/map-range (q/noise y-seed) 0.0 1.0 0.0 (q/height))
     :x-state (+ x-seed x-step)
     :y-state (+ y-seed y-step)
     :x-step x-step
     :y-step y-step
     :radius 50
     :fill-color (q/color 150)
     :stroke-color (q/color 0)}))

(defn tweak-options
  [state]
  (let [my-options {:fill-color (q/color 60 85 45)}]
    (merge state my-options)))

(defn update-state
  [state]
  (let [{:keys [x-state x-step y-state y-step]} state]
    (-> state
        (tweak-options)
        (update :x-state + x-step)
        (assoc :x (q/map-range (q/noise x-state) 0.0 1.0 0.0 (q/width)))
        (update :y-state + y-step)
        (assoc :y (q/map-range (q/noise y-state) 0.0 1.0 0.0 (q/height))))))

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
