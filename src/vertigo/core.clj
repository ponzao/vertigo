(ns views
  (:use [hiccup.core]))

(defn- layout [content]
  (html [:html
          [:head]
          [:body
            [:div#content
              content]]]))

(defn list-shows [shows]
  [:table
    (for [day shows]
      [:tr
        [:td (:time (first (:comedy day)))]
        [:td (map :title (:comedy day))]])])

(defn shows [shows]
  (layout (list-shows shows)))

(ns vertigo.core
  (:require [clojure.xml :as xml]
            [clojure.zip :as zip])
  (:use     [clojure.contrib.zip-filter.xml]
            [compojure.core]
            [ring.adapter.jetty]
            [clojure.string :only (split)]
            [clj-time.format]
            [clj-time.core :only (now)]))

(defrecord Show [title
                 theatre
                 time
                 genres
                 image])

(defn group-by-genre [shows]
  (group-by
    (fn [{genres :genres}]
      (condp some genres
        #{"Komedia"} :comedy
        #{"Draama"} :drama
        #{"Toiminta"} :action
        :other))
    shows))

(defn parse-shows [xz] 
  (for [show (xml-> xz :Shows :Show)]
    (Show.
      (xml1-> show :OriginalTitle text)
      (xml1-> show :TheatreAndAuditorium text)
      (xml1-> show :dttmShowStart text)
      (into #{} (split (xml1-> show :Genres text) #", "))
      (xml1-> show :Images :EventLargeImagePortrait text))))

(def finnish-date-format (formatter "dd.MM.yyyy"))

(def week-of-shows
  (pmap
    parse-shows
    (map
      #(zip/xml-zip (xml/parse (str "http://finnkino.fi/xml/Schedule/?area=1002&dt=" (unparse finnish-date-format %))))
      (for [day (range 7)]
        (.plusDays (now) day)))))

(def week-of-shows-grouped-by-genre
  (map group-by-genre (filter not-empty week-of-shows)))

(defroutes handler
  (GET "/" []
    (views/shows week-of-shows-grouped-by-genre)))

(run-jetty handler {:port 8080 :join? false})

