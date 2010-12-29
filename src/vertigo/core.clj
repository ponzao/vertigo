(ns vertigo.core
  (:require [vertigo.views :as views]
            [vertigo.common :as common]
            [vertigo.repository :as repository]
            [vertigo.batch :as batch])
  (:use     [compojure.core]
            [ring.adapter.jetty]))

(defrecord Movie [id
                 title
                 theatre
                 genres
                 image])

(defn retrieve-all-distinct-event-ids-for-stored-shows []
  (distinct (map
              :event-id
              (flatten
                (for [{shows :shows-by-genre}
                        (apply-on-whole-week retrieve-shows-by-date)]
                  (vals shows))))))

(defn retrieve-all-events-for-stored-shows []
  (map
    retrieve-event
    (retrieve-all-distinct-event-ids-for-stored-shows)))

(defroutes handler
  (GET "/movies" [] 
    (views/movies (common/map-on-n-days (repository/retrieve-movies) 4)))
  (GET "/movies/:id" [id]
    (views/movie (retrieve-movie id)))
  (GET "/batch/movies" []
    (batch/update-movies)))

(run-jetty handler {:port 8080 :join? false})

