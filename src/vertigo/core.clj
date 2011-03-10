(ns vertigo.core
  (:require [vertigo.views :as views]
            [appengine-magic.core :as ae])
  (:use     [compojure.core]
            [clj-time.core :only (now)]))
(defroutes handler
  (GET "/helsinki" [] 
    (views/movie-page movies-by-date))
  (GET "/movies" []
    (views/top-movies top-movies))
  (GET "/movies/:id" [id]
    (views/selected-movie selected-movie)))

(ae/def-appengine-app vertigo-app #'handler)

