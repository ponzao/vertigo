(ns vertigo.core
  (:require [vertigo.views :as views]
            [appengine-magic.core :as ae])
  (:use     [compojure.core]))

(defroutes handler
  (GET "/helsinki" [] 
    (views/movie-page {}))) ; RETRIEVE THE MOVIES!

(ae/def-appengine-app vertigo-app #'handler)

