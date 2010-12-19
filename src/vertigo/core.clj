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
    (map 
      (fn [{title   :title
            theatre :theatre
            time    :time
            image   :image}]
        [:tr
          [:td [:img {:src image}]]
          [:td title]
          [:td theatre]])
      shows)])

(defn shows [shows]
  (layout (list-shows shows)))

(ns vertigo.core
  (:require [clojure.xml :as xml]
            [clojure.zip :as zip])
  (:use     [clojure.contrib.zip-filter.xml]
            [compojure.core]
            [ring.adapter.jetty]
            [clojure.string :only (split)]
            [clj-time.format]))

(def url "http://finnkino.fi/xml/Schedule/?area=1002&dt=16.12.2010")

(def movies (zip/xml-zip (xml/parse url)))

(defn get-shows [xz] 
  (map
    (fn [show]
      {:title   (xml1-> show :OriginalTitle text)
       :theatre (xml1-> show :TheatreAndAuditorium text)
       :time    (xml1-> show :dttmShowStart text)
       :genres  (into #{} (split (xml1-> show :Genres text) #", "))
       :image   (xml1-> show :Images :EventLargeImagePortrait text)})
    (xml-> xz :Shows :Show)))

; To parse the starting times...
; (map (fn [{time :time}] (unparse (formatters :hour-minute) (parse time)))   (get-shows movies))
(defroutes handler
  (GET "/" []
    (views/shows (get-shows movies))))

(run-jetty handler {:port 8080 :join? false})

