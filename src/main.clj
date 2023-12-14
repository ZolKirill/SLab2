(ns main
  (:require [clojure.core.async :as async :refer [<! >! <!! >!! timeout chan onto-chan alts! alts!! go-loop go close!]]))

(defn chtovec
  [chn]
  (go-loop [res []]
           (let [x (<!! chn)]
             (if x
               (do
                 (println x)
                 (recur (conj res x)))
               res)
             )
           )
  )

(defn foo [x, y]

  (go-loop []
           (let [message (alts! [ x   (timeout 5) ]) ]
             (if  (nil? (first message))

               (if (identical? x (last message))
                 nil
                 (do (>! y :timeout) (recur))
                 )
               (do (>! y (first message)) (recur))

               )
             )
           )


  )



(def x (chan 10))
(def y (chan 10))

(foo x y)
(>!! x 5)
(Thread/sleep 10)
(>!! x 6)
(>!! x 1)
(Thread/sleep 10)
(>!! x 9)
(close! x)
(chtovec y)

(Thread/sleep 100)