(ns views
  (:use [hiccup.core]))

(defn layout [content]
  (html [:html
          [:head]
          [:body
            [:div#content
              content]]]))

(defn render-show [show]
  [:div (:title show)])

(defn show-table [shows]
  [:table
    [:tr
      [:th "Date"]
      [:th "Comedy"]
      [:th "Action"]
      [:th "Drama"]
      [:th "Others"]]
    (for [{date           :date
           shows-by-genre :shows-by-genre} shows]
      [:tr
        [:td date]
        [:td (map render-show (:comedy shows-by-genre))]
        [:td (map render-show (:action shows-by-genre))]
        [:td (map render-show (:drama shows-by-genre))]
        [:td (map render-show (:other shows-by-genre))]])])

(defn calendar [shows]
  (layout (show-table shows)))

(ns vertigo.core
  (:require [clojure.xml :as xml]
            [clojure.zip :as zip])
  (:use     [clojure.contrib.zip-filter.xml]
            [compojure.core]
            [ring.adapter.jetty]
            [clojure.string :only (split)]
            [clj-time.format]
            [clj-time.core :only (now)]))

(defrecord Show [event-id
                 title
                 theatre
                 genres
                 image])

(def shows-db (atom {}))

(defn parse-shows-and-group-by-genre [xz]
  (let [shows (for [show (xml-> xz :Shows :Show)]
          (Show.
            (xml1-> show :EventId text)
            (xml1-> show :OriginalTitle text)
            (xml1-> show :TheatreAndAuditorium text)
            (into #{} (split (xml1-> show :Genres text) #", "))
            (xml1-> show :Images :EventLargeImagePortrait text)))]
    (group-by
      (fn [{genres :genres}]
        (condp some genres
          #{"Komedia"} :comedy
          #{"Draama"} :drama
          #{"Toiminta"} :action
          :other))
      shows)))

(defn retrieve-shows-by-date [date]
  (let [formatted-date (unparse (formatter "dd.MM.yyyy") date)
        cached-shows   (get @shows-db formatted-date)]
    (if cached-shows
      [formatted-date cached-shows]
      (let [new-shows (parse-shows-and-group-by-genre
                        (zip/xml-zip
                          (xml/parse
                            (str
                              "http://finnkino.fi/xml/Schedule/?area=1002&dt="
                              formatted-date))))]
        {:date           formatted-date
         :shows-by-genre (get
                           (swap! shows-db assoc
                                             formatted-date
                                             new-shows)
                           formatted-date)}))))

(defn retrieve-shows-for-whole-week []
  (let [dates (take 7 (iterate #(.plusDays % 1) (now)))]
    (map retrieve-shows-by-date dates)))

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

(defroutes handler
  (GET "/" []
    (views/calendar (retrieve-shows-for-whole-week))))

(run-jetty handler {:port 8080 :join? false})

