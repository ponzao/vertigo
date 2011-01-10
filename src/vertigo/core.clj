(ns vertigo.core
  (:require [vertigo.views :as views]
            [vertigo.common :as common]
            [vertigo.repository :as repository]
            [vertigo.batch :as batch]
            [appengine-magic.core :as ae])
  (:use     [compojure.core]))

(defroutes handler
  (GET "/movies" [] 
    (views/shows (repository/retrieve-dates-and-shows)))
  (GET "/movies/:id" [id]
    (views/movie (repository/retrieve-movie id)))
  (GET "/batch/shows" []
    (batch/update-shows))
  (GET "/batch/movies" []
    (batch/update-movies)))

(ae/def-appengine-app vertigo-app #'handler)

