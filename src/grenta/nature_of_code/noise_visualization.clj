(ns grenta.nature-of-code.noise-visualization
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [grenta.nature-of-code.slider :as slider]))

(defn setup
  []
  (let [slider-len 200]
    {:time 0.0
     :step-size 0.005
     :min-step 0.000
     :max-step 0.2
     :slider (slider/new-slider (/ (- (q/width) slider-len) 2)
                                (- (q/height) 20)
                                slider-len
                                5)}))

(defn update-state
  [{:keys [min-step max-step step-size] :as state}]
  (let [slider-value (get-in state [:slider :value])]
    (-> state
        (update :time + step-size)
        (assoc :step-size (q/map-range slider-value 0.0 1.0 min-step max-step)))))

(defn draw
  [{:keys [time step-size] :as state}]
  ;; Clear the background
  (q/background 255)
  ;; Draw curve
  (let [curve-color (q/color 100)
        stroke-weight 2]
    (q/fill nil)
    (q/stroke curve-color)
    (q/stroke-weight stroke-weight)
    (q/begin-shape)
    (doseq [x (range (q/width))
            :let [y (q/map-range (q/noise (+ time (* step-size x))) 0 1.0 0 (q/height))
                 ]]
      (q/vertex x y))
    (q/end-shape))

  ;; Draw a point at the y-value in the middle of the screen
  (let [diameter 8
        color (q/color 200 0 0)
        text-color (q/color 100 0 0)
        x (/ (q/width) 2.0)
        y (q/map-range (q/noise (+ time (* step-size x)))
                       0.0 1.0
                       0 (q/height))
        text-x (+ x 3)
        text-y 10]
    (q/fill color)
    (q/stroke nil)
    (q/ellipse x y diameter diameter)
    ;; Draw some text describing the point
    (q/fill text-color)
    (q/text (format "y-value: %.0f" y) text-x text-y))

  ;; Draw text
  (q/fill 0)
  (q/text (format "Step-size = %.3f" step-size) 20 230)

  ;; Draw the slider
  (slider/draw (:slider state)))

(defn mouse-dragged
  [state drag-event]
  (update state :slider slider/mouse-dragged drag-event))

(defn mouse-clicked
  [state click-event]
  (update state :slider slider/mouse-clicked click-event))

(defn mouse-released
  [state click-event]
  (let [slider (:slider state)]
    (assoc state :slider (slider/mouse-released slider click-event))))

;; Information about the function signitures required for the
;; callback functions in fun-mode can be found here:
;; https://github.com/quil/quil/wiki/Functional-mode-%28fun-mode%29
(q/defsketch example           ;; Define a new sketch named example
  :title "Noise visualization" ;; Set the title of the sketch
  :settings #(q/smooth 2)      ;; Turn on anti-aliasing
  :setup setup                 ;; Specify the setup fn
  :draw draw                   ;; Specify the draw fn
  :update update-state
  :mouse-dragged mouse-dragged
  :mouse-released mouse-released
  :mouse-clicked mouse-clicked
  :size [640 240]
  :middleware [m/fun-mode]
  :features [])

