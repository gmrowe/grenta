(ns grenta.main
  (:require [mikera.image.core :as image]
            [mikera.image.colours :as colors]
            [clojure.math :as math]
            [clojure.data.generators :as gen])
  (:import (java.security SecureRandom)
           (java.nio ByteBuffer)))

(def default-seed 42)

(defn new-image
  "Creates a new image using the supplied render-fn. The
  render-fn should be a function of 2 parameters x and y
  representing the x and y values of the image as a
  double in the range (-1.0 to 1.0). The render-fn
  should return a three-element sequence [r g b] representing the
  rgb components of the color of the pixel at the x y
  location. Each color component should be in the range
  0.0 - 1.0. The width and height (default 400 x 400) of the image
  in pixels can optionally be provided."
  ([render-fn] (new-image render-fn 400 400))
  ([render-fn width height] (new-image render-fn width height default-seed))
  ([render-fn width height seed]
   {:height height :width height :render-fn render-fn :seed seed}))

(defn show-image
  [{:keys [height width render-fn] :as image}]
  (let [pixels (for [y (range height)
                     x (range width)
                     :let [;; ny and nx are normalized coordinates.
                           ;; They take the x y in screen coords
                           ;; and translate them to -1.0-1.0
                           ;; world coordinates.
                           ny (- 1.0 (* 2.0 y (/ 1.0 height)))
                           nx (- (* 2.0 x (/ 1.0 width)) 1.0)
                           [r g b] (render-fn nx ny)]]
                 (colors/rgb r g b))]
    (prn image)
    (doto (image/new-image width height)
      (image/set-pixels (int-array pixels))
      (image/show :zoom 1.0 :title "grenda"))))

;; A function that computes a uv pattern
(defn uv-pattern
  [x y]
  [(/ (+ x 1.0) 2.0) 0.0 (/ (+ y 1.0) 2.0)])

(defn greyscale
  [x _y]
  [(/ (+ x 1.0) 2.0)
   (/ (+ x 1.0) 2.0)
   (/ (+ x 1.0) 2.0)])

;; Returns an function that returns the input color for any input
(defn fill
  ([color] (apply fill (colors/values-rgb color)))
  ([r g b] (constantly [r g b])))

(defn dimple
  [x y]
  (let [fuzz (rand (* y y))
        ;; The origin here is the location of the `dimple`
        [origin-x origin-y] [0.0 0.4]
        ;; Create a vector between the origin and  the point: Vx => x component of
        ;; the vector vy => y component of the vector.
        [vx vy] [(- x origin-x) (- y origin-y)]
        ;; the magnitude of the above vector
        magnitude (math/sqrt (+ (* vx vx) (* vy vy)))
        ;; the vector representing the x axis.
        [avx avy] [0 1]
        ;; The dot product bewteen the x-axis and the vector from the origin
        ;; to our point
        dot-product (+ (* vx avx) (* vy avy))
        theta (math/acos (/ dot-product magnitude))]
    [(abs (math/cos theta)) (/ theta 2.0 math/PI) fuzz]))

(defn distance-squared
  [x1 y1 x2 y2]
  (let [dx (- x2 x1)
        dy (- y2 y1)]
    (+ (* dx dx) (* dy dy))))

;; Uses java.security.SecureRandom to generate a random seed
;; for use in creating a seeded random generator to send to
;; other functions
(defn random-long-seed
  []
  (let [sec-rng (SecureRandom.)
        bytes (doto (byte-array Long/BYTES) (#(.nextBytes sec-rng %)))
        byte-buffer (ByteBuffer/wrap bytes)]
    (.getLong byte-buffer)))

(defn circle
  [center radius fg-color bg-color]
  (fn [x y]
    (let [[cx cy] center]
      (if (< (apply distance-squared x y center) (* radius radius))
        (colors/values-rgb fg-color)
        (colors/values-rgb bg-color)))))

(defn in-circle?
  [{:keys [center radius]} x y]
  (< (apply distance-squared x y center) (* radius radius)))

(defn random-circle
  [min-radius max-radius color]
  {:center [(- (* (gen/double) 2.0) 1.0) (- (* (gen/double) 2.0) 1.0)]
   :radius (+ min-radius (* (gen/double) (- max-radius min-radius)))
   :color color})

(defn circles
  [num min-radius max-radius bg-color color-palette]
  (let [circles
          (take num
                (repeatedly
                  #(random-circle min-radius max-radius (gen/rand-nth color-palette))))]
    (fn [x y]
      (if-let [circle (first (filter #(in-circle? % x y) circles))]
        (colors/values-rgb (:color circle))
        (colors/values-rgb bg-color)))))

(comment
  (show-image (new-image uv-pattern))
  (show-image (new-image (circle [0.2 0.03] 0.03 colors/magenta colors/cyan)))
  (show-image
    (new-image
      (circles 5 0.01 0.3 colors/darkGray [0xFFFAF6E3 0xFFD8DBBD 0xFFB59F78 0xFF2A3663])))
  (show-image (new-image dimple))
  (show-image (new-image (fill colors/magenta)))
  (show-image (new-image uv-pattern))
  (show-image (new-image greyscale)))

(defn -main
  [& args]
  (println "Hello World!"))
