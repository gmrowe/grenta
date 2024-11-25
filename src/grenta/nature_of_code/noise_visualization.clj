(ns grenta.nature-of-code.noise-visualization
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(defn setup
  []
  {:time 0.0
   :step-size 0.005})

(defn update-state
  [{:keys [step-size] :as state}]
  (-> state
      (update :time + step-size)))

(defn draw
  [{:keys [time step-size]}]
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

  ;; Draw a thin vertical line in the middle of the screen
  (let [midpoint (/ (q/width) 2.0)]
    (q/fill 127 50)
    (q/stroke 1 50)
    (q/stroke-weight 1)
    (q/line midpoint 0 midpoint (q/height)))

  ;; Draw text
  (q/fill 0)
  (q/text "Press \"+\" or \"-\" to change step size" 10 20)
  (q/text (format "Step-size = %.3f" step-size) 10 30))

(defn key-pressed
  [state {:keys [raw-key]}]
  ;; The 0.005 step-change  is completely arbitrary, and hard coded
  (let [step-change 0.005]
    (cond
      (= raw-key \+)
        (update state :step-size + step-change)
      (or (= raw-key \-) (= raw-key \_))
        (assoc state :step-size (max 0.0 (- (:step-size state) step-change)))
      :else
        state)))

;; Information about the function signitures required for the
;; callback functions in fun-mode can be found here:
;; https://github.com/quil/quil/wiki/Functional-mode-%28fun-mode%29
;;
(q/defsketch example           ;; Define a new sketch named example
  :title "Noise visualization" ;; Set the title of the sketch
  :settings #(q/smooth 2)      ;; Turn on anti-aliasing
  :setup setup                 ;; Specify the setup fn
  :draw draw                   ;; Specify the draw fn
  :update update-state
  :key-pressed key-pressed
  :size [640 240]
  :middleware [m/fun-mode]
  :features [])

