(ns grenta.nature-of-code.random-number-distribution
  (:require [quil.core :as q]
            [clojure.math :as math]
            [clojure.data.generators :as gen]))

(def num-buckets 20)
(def state (atom nil))
(defn init-state
  []
  (reset! state
    (vec (repeat num-buckets 0))))

(defn setup
  []
  (init-state))

(defn update-counts
  ([counts]
   (update-counts counts #(q/random (count counts))))

  ([counts gen]
   (let [n (q/floor (gen))]
     (update counts n inc))))

(defn render-state
  [counts]
  (let [bar-width (/ (q/width) num-buckets)
        outline-color [255 255 0]
        fill-color [11 122 255]]
    (doseq [bar-n (range num-buckets)
            :let [bar-height (nth counts bar-n)
                  x (* bar-width bar-n)
                  y (- (q/height) bar-height)]]
      (apply q/stroke outline-color)
      (apply q/fill fill-color)
      (q/rect x y bar-width bar-height)
      (when (= bar-n 5)
        (printf "pos:(%s, %s)%n" (* bar-width bar-n) bar-height)))))

(defn draw
  []
  (swap! state update-counts)
  (render-state @state))

(q/defsketch example
  :title "Random number distribution"
  :settings #(q/smooth 2)
  :setup setup
  :draw draw
  :size [640 240]
  :features [:resizable])

