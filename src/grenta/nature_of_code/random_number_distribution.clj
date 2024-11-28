(ns grenta.nature-of-code.random-number-distribution
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [clojure.math :as math]
            [clojure.data.generators :as gen]))

(def num-buckets 30)

(defn setup
  []
  (vec (repeat num-buckets 0)))

(defn accept-reject-generator
  []
  (let [n (rand)]
    (if (< (rand) n)
      (long (math/floor (* n num-buckets)))
      (recur))))

(defn update-counts
  ([counts]
   (update-counts counts #(accept-reject-generator)))

  ([counts gen]
   (let [n (q/floor (gen))]
     (update counts n inc))))

(defn draw
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

(comment
  (q/defsketch example
    :title "Random number distribution"
    :settings #(q/smooth 2)
    :setup setup
    :draw draw
    :update update-counts
    :size [640 240]
    :features [:resizable]
    :middleware [m/fun-mode])
)

