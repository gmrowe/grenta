(ns grenta.nature-of-code.bouncing-ball
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [clojure.math :as math]
            [grenta.nature-of-code.vec2 :as vec2]))

(def millis-per-second 1000)

(defn hexstr->qcolor
  "Fragile, string based algorithm that strips the leading
   \\# if present, then using substrings, converts a string
   to a quil color"
  [hex]
  (let [hexn (if (= \# (first hex)) (subs hex 1) hex)
        rgb (mapv #(Integer/parseInt % 16)
              [(subs hexn 0 2) (subs hexn 2 4) (subs hexn 4 6)])]
    (apply q/color rgb)))

(defn setup
  []
  (q/frame-rate 60)
  {:background-color (hexstr->qcolor "#EBE4D2")
   :ball-diameter 20
   :ball-color (hexstr->qcolor "FF2929")
   :ball-speed 25 ;; units per second
   :last-update 0
   :ball-pos [(+ (/ (q/width) 2.0) 22) (- (/ (q/height) 2.0) 64)]
   :ball-vel [2 3]})

(defonce my-opts
  nil)

(defn check-collision
  [{:keys [ball-pos ball-diameter] :as state}]
  (let [[ball-x ball-y] ball-pos
        ball-radius (/ ball-diameter 2.0)
        x-collision? (not (< ball-radius ball-x (- (q/width) ball-radius)))
        y-collision? (not (< ball-radius ball-y (- (q/height) ball-radius)))]
    (cond-> state
      x-collision? (update :ball-vel (fn [[x y]] [(* -1 x) y]))
      y-collision? (update :ball-vel (fn [[x y]] [x (* -1 y)])))))

(defn move-ball
  [state]
  (-> state
      (update :ball-pos vec2/add (:ball-vel state))
      check-collision))

(defn next-frame?
  [state now]
  (let [refresh-rate (/ millis-per-second (:ball-speed state))
        elapsed-millis (- now (:last-update state))]
    (< refresh-rate elapsed-millis)))

(defn update-state
  [state]
  (let [state' (merge state my-opts)
        now (q/millis)]
    (if (next-frame? state' now)
      (-> state'
          move-ball
          (assoc :last-update now))
      state')))

(defn draw
  [state]
  (let [[x y] (:ball-pos state)]
    (q/background (:background-color state))
    (q/fill (:ball-color state))
    (q/ellipse x y (:ball-diameter state) (:ball-diameter state))))



(comment
  (q/defsketch example      ;; Define a new sketch named example
    :title "Bouncing ball"  ;; Set the title of the sketch
    :settings #(q/smooth 2) ;; Turn on anti-aliasing
    :setup setup            ;; Specify the setup fn
    :draw draw              ;; Specify the draw fn
    :update update-state
    :size [400 400]
    :middleware [m/fun-mode]
    :features [:no-safe-fns])
)

(comment

  (do (defn draw-example
        [_state]
        (let [mouse-x (q/mouse-x)
              mouse-y (q/mouse-y)
              center-x (/ (q/width) 2)
              center-y (/ (q/height) 2)
              [diff-x diff-y] (vec2/subtract [mouse-x mouse-y] [center-x center-y])]
          (q/background 255)
          (q/stroke 200)
          (q/stroke-weight 4)
          (q/line 0 0 mouse-x mouse-y)
          (q/line 0 0 center-x center-y)
          (q/stroke 0)
          (q/with-translation [(/ (q/width) 2) (/ (q/height) 2)]
                              (q/line 0 0 diff-x diff-y))
        ))

      (q/defsketch example      ;; Define a new sketch named example
        :title "Subtraction"    ;; Set the title of the sketch
        :settings #(q/smooth 2) ;; Turn on anti-aliasing
        :draw draw-example      ;; Specify the draw fn
        :size [400 400]
        :middleware [m/fun-mode]
        :features [:no-safe-fns]))

)


;; function draw() {
;;   background(255);
;;   let mouse = createVector(mouseX, mouseY);
;;   let center = createVector(width / 2, height / 2);
;;   mouse.sub(center);
;;   translate(width / 2, height / 2);
;;   strokeWeight(2);
;;   stroke(200);
;;   line(0, 0, mouse.x, mouse.y);
;;   mouse.mult(0.5);
;; Multiplying a vector! The vector is now half its original size (multiplied by 0.5).
;;   stroke(0);
;;   strokeWeight(4);
;;   line(0, 0, mouse.x, mouse.y);
;; }

(comment

  (do
    (defn draw-example-2
      [_state]
      (let [mouse [(q/mouse-x) (q/mouse-y)]
            center [(/ (q/width) 2) (/ (q/height) 2)]
            scaled (vec2/mul-scalar mouse 0.5)]
        (q/with-translation
          center
          (q/background 255)
          (q/stroke-weight 2)
          (q/stroke 200)
          (apply q/line 0 0 (vec2/subtract mouse center))
          (q/stroke-weight 4)
          (q/stroke 0)
          (apply q/line
            0
            0
            (-> mouse
                (vec2/subtract center)
                (vec2/mul-scalar 0.5))))
      ))

    (q/defsketch example ;; Define a new sketch named example
      :title "Multiplication" ;; Set the title of the sketch
      :settings #(q/smooth 2) ;; Turn on anti-aliasing
      :draw draw-example-2 ;; Specify the draw fn
      :size [400 400]
      :middleware [m/fun-mode]
      :features [:no-safe-fns]))

)

