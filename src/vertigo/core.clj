(ns vertigo.core
  (:require [vertigo.views :as views]
            [appengine-magic.core :as ae]
            [vertigo.repository :as repository]
            [vertigo.batch :as batch])
  (:use     [compojure.core]
            [clj-time.core :only (now)]))

(defroutes handler
  (GET "/helsinki" [] 
    (let [start-date (.plusDays (now) 1)
          end-date   (.plusDays start-date 7)]
      (views/movie-page 
        (repository/retrieve-events-between-dates start-date end-date))))
  (GET "/movies" []
    (views/newest-movies (repository/get-movies-sorted-by-release)))
  (GET "/movies/:id" [id]
    (views/selected-movie (repository/get-movie id)))
  (GET "/admin" []
    (batch/update)))

(ae/def-appengine-app vertigo-app #'handler)

