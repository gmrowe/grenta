(ns grenta.main
  (:require [mikera.image.core :as image]
            [mikera.image.colours :as colors]))


(def aspect-ratio (/ 4 3))
(def width 400)
(def height (/ width aspect-ratio))

;; A var holding the image that we are going to display
(def image (image/new-image width height))

(defn update-image!
  [f]
  (let [pixels (for [y (range height)
                     x (range width)
                     :let [ny (- (* 2.0 y (/ 1.0 height)) 1.0)
                           nx (- (* 2.0 x (/ 1.0 width)) 1.0)
                           [r g b] (f nx ny)]]
                 (colors/rgb r g b))]
    (image/set-pixels image (int-array pixels))))


(defn show-image!
  []
  (image/show image :zoom 1.0 :title "Isn't it beautiful?"))

;; f must be a function that takes two pararamters representing
;; an x y coordinate. x coords are in the domain [-1,1) , y coord
;; are in the range [-1, 1). The function should return an triple
;; in the form [r g b] representing r g b values from [0,1).
(defn update-and-show-image!
  [f]
  (update-image! f)
  (show-image!))

;; A function that computes a uv pattern
(defn uv-pattern
  [x y]
  [1.0 (/ (+ x 1.0) 2.0) (/ (+ y 1.0) 2.0)])

(defn greyscale
  [x _y]
  [(/ (+ x 1.0) 2.0)
   (/ (+ x 1.0) 2.0)
   (/ (+ x 1.0) 2.0)])

;; Returns an function that returns the input color for any input
(defn fill
  ([color] (apply fill (colors/values-rgb color)))
  ([r g b] (constantly [r g b])))

(comment
  (update-and-show-image! (fill 0.0 1.0 0.0))
  (update-and-show-image! uv-pattern)
  (update-and-show-image! greyscale))

(defn -main
  [& args]
  (println "Hello World!"))
