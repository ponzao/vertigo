(ns vertigo.core
  (:require [clojure.xml :as xml]
            [clojure.zip :as zip]
            [vertigo.views :as views])
  (:use     [clojure.contrib.zip-filter.xml]
            [compojure.core]
            [ring.adapter.jetty]
            [clojure.string :only (split)]
            [clj-time.format]
            [clj-time.core :only (now)]))

(defrecord Movie [id
                 title
                 theatre
                 genres
                 image])

(defn retrieve-shows-by-date [date]
  (let [formatted-date (unparse (formatter "dd.MM.yyyy") date)
        cached-shows   (get @shows-db formatted-date)]
    {:date           formatted-date
     :shows-by-genre cached-shows}))

(defn apply-on-whole-week [f]
  (let [dates (take 7 (iterate #(.plusDays % 1) (now)))]
    (map f dates)))

(def events-db (atom {}))

(defn retrieve-event [event-id]
  (let [cached-event (get @events-db event-id)]
    (if cached-event
      {:event-id event-id
       :event    cached-event}
      (let [new-event (slurp
                        (str
                          "http://finnkino.fi/xml/Events/?eventId="
                          event-id))]
        {:event-id event-id
         :event    (get
                     (swap! events-db assoc event-id new-event)
                     event-id)}))))

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

(defn retrieve-and-store-shows-from-finnkino [date]
  (let [formatted-date (unparse (formatter "dd.MM.yyyy") date)
        new-shows
          (parse-shows-and-group-by-genre
            (zip/xml-zip
              (xml/parse
                (str
                  "http://finnkino.fi/xml/Schedule/?area=1002&dt="
                  formatted-date))))]
    (swap! shows-db assoc
                      formatted-date
                      new-shows)))

(defroutes handler
  (GET "/" []
    (views/calendar (apply-on-whole-week retrieve-shows-by-date)))
  (GET "/:id" [id]
    (views/movie (retrieve-movie id))
  (GET "/execute-batch" []
    (if (and
          (apply-on-whole-week retrieve-and-store-shows-from-finnkino)
          (retrieve-all-events-for-stored-shows))
      "success"
      "failure")))

(run-jetty handler {:port 8080 :join? false})

