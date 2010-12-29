(ns vertigo.core
  (:require [vertigo.views :as views])
  (:use     [compojure.core]
            [ring.adapter.jetty]
            [clj-time.core :only (now)]))

(defrecord Movie [id
                 title
                 theatre
                 genres
                 image])

(defn apply-on-whole-week [f]
  (let [dates (take 7 (iterate #(.plusDays % 1) (now)))]
    (map f dates)))

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
    (views/movies (apply-on-whole-week (repository/retrieve-movies))))
  (GET "/movies/:id" [id]
    (views/movie (retrieve-movie id)))
  (GET "/execute-batch" []
    (if (and
          (apply-on-whole-week retrieve-and-store-shows-from-finnkino)
          (retrieve-all-events-for-stored-shows))
      "success"
      "failure")))

(run-jetty handler {:port 8080 :join? false})

