(ns vertigo.core
  (:require [clojure.xml :as xml]
            [clojure.zip :as zip])
  (:use     [clojure.contrib.zip-filter.xml]
            [hiccup.core]
            [compojure.core]
            [ring.adapter.jetty]
            [clj-time.format]))

(def url "http://finnkino.fi/xml/Schedule/?area=1002&dt=16.12.2010")

(def movies (zip/xml-zip (xml/parse url)))

(defn get-shows [xz] 
  (map
    (fn [show]
      {:title   (xml1-> show :Title text)
       :theatre (xml1-> show :Theatre text)
       :time    (xml1-> show :dttmShowStart text)
       :image   (xml1-> show :Images :EventLargeImagePortrait text)})
    (xml-> xz :Shows :Show)))

; To parse the starting times...
; (map (fn [{time :time}] (unparse (formatters :hour-minute) (parse time)))   (get-shows movies))

(def render-shows
  [:ul
    (map 
      (fn [{title   :title
            theatre :theatre
            time    :time
            image   :image}]
        [:li title theatre time [:img {:src image}]])
      (get-shows movies))])

(defn layout [content]
  (html [:html
          [:head]
          [:body
            [:div#content
              content]]]))

(def show-page (layout render-shows))

(defroutes handler
  (GET "/" []
    show-page))

(run-jetty handler {:port 8080 :join? false})

