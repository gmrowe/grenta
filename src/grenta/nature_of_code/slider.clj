(ns grenta.nature-of-code.slider
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [clojure.pprint :as pp]))

(defn clamp
  [n lo hi]
  (cond
    (< n lo) lo
    (< hi n) hi
    :else n))

(defn new-slider
  [x y width height]
  {:slider-length width
   :slider-height height
   :slider-inactive-color (q/color 190)
   :slider-active-color (q/color 50)
   :slider-outline (q/color 10)
   :knob-diameter 15
   :knob-color 10
   :slider-x (/ (- (q/width) width) 2.0)
   :slider-y (/ (- (q/height) height) 2.0)
   :slider-corner-radius 4
   :knob-x (/ (- (q/width) width) 2.0)
   :knob-y (+ (/ height 2.0) (/ (- (q/height) height) 2.0))})

(defn setup
  []
  (let [length 180
        height 5]
    (new-slider (/ (- (q/width) length) 2.0)
                (/ (- (q/height) height) 2.0)
                length
                height)))

(defn update-state
  [state]
  state)

(defn draw
  [state]
  ;; Draw horizontal rectangle
  (q/background 250)
  (let [{:keys
           [slider-length
            slider-height
            slider-inactive-color
            slider-active-color
            slider-outline
            slider-corner-radius
            knob-diameter
            knob-color
            slider-x
            slider-y
            knob-x
            knob-y]}
          state]
    (q/fill slider-inactive-color)
    (q/stroke slider-outline)
    (q/rect slider-x slider-y slider-length slider-height slider-corner-radius)
    (q/fill slider-active-color)
    (q/rect slider-x slider-y (- knob-x slider-x) slider-height slider-corner-radius)
    ;; Draw circular knob
    (q/fill knob-color)
    (q/ellipse knob-x knob-y knob-diameter knob-diameter)

    ;; Make knob slidable across slider
    ;; -- done in mouse-dragged functon

    ;; Output slider state
    (q/text (format "%.0f%%" (* 100 (/ (- knob-x slider-x) slider-length)))
            slider-x
            (- slider-y 20))))

(defn slider-clicked?
  [state click-event]
  (let [{:keys [button x y]} click-event
        {:keys [slider-x slider-y slider-length slider-height]} state
        buffer-y 8]
    (and (= button :left)
         (<= slider-x x (+ slider-x slider-length))
         (<= (- slider-y buffer-y) y (+ slider-y slider-height buffer-y)))))

(defn mouse-clicked
  [state click-event]
  (if (slider-clicked? state click-event)
    (assoc state :knob-x (:x click-event))
    state))

(defn knob-dragged?
  [state drag-event]
  (let [{:keys [button p-x p-y]} drag-event
        {:keys [knob-x knob-y knob-diameter]} state
        knob-radius (/ knob-diameter 2.0)]
    (and (= button :left)
         (<= (- knob-x knob-radius) p-x (+ knob-x knob-radius))
         (<= (- knob-y knob-radius) p-y (+ knob-y knob-radius)))))

(defn mouse-dragged
  [{:keys [knob-x knob-y slider-x slider-length] :as state}
   {:keys [p-x p-y x y button] :as drag-event}]
  (cond
    (nil? (:dragging-knob? state))
      (assoc state :dragging-knob? (knob-dragged? state drag-event))
    (:dragging-knob? state)
      (assoc state :knob-x (clamp x slider-x (+ slider-x slider-length)))
    :else
      state))

(q/defsketch slider
  :size [300 200]
  :title "Slider"
  :settings #(q/smooth 2)
  :features [:no-safe-fns]
  :setup setup
  :update update-state
  :draw draw
  :mouse-clicked mouse-clicked
  :mouse-dragged mouse-dragged
  :mouse-released (fn [state _] (dissoc state :dragging-knob?))
  :middleware [m/fun-mode])
