(ns grenta.nature-of-code.vec2
  (:require [clojure.math :as math]))

(defn add
  [[a b] [x y]]
  [(+ a x) (+ b y)])

(defn negate
  [[a b]]
  [(- a) (- b)])

(defn subtract
  [[a b] [x y]]
  [(- a x) (- b y)])

(defn mul-scalar
  [[a b] scalar]
  [(* a scalar) (* b scalar)])

(defn div-scalar
  [[a b] scalar]
  [(/ a scalar) (/ b scalar)])

(defn magnitude
  [[a b]]
  (math/sqrt (+ (* a a) (* b b))))

(defn normalize
  [vec2]
  (let [mag (magnitude vec2)]
    (when (not (zero? mag))
      (div-scalar vec2 mag))))

(defn as-magnitude
  [vec2 mag]
  (-> vec2
      normalize
      (mul-scalar mag)))

(defn limit
  [vec2 max]
  (if (< (magnitude vec2) max)
    vec2
    (as-magnitude vec2 max)))

(defn rand-vec2
  []
  (normalize [(- (rand) 0.5) (- (rand) 0.5)]))
