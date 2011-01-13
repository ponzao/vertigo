(ns vertigo.parser
  (:require [clojure.xml :as xml]
            [clojure.zip :as zip])
  (:use     [clojure.contrib.zip-filter.xml]
            [clojure.string :only (split)]
            [clj-time.format]))

(defn zip-string [s]
  (zip/xml-zip (xml/parse (java.io.ByteArrayInputStream.
                            (.getBytes s "UTF-8")))))

(defn parse-shows [s]
  (let [xz (zip-string s)]
    (for [show (xml-> xz :Shows :Show)]
      {:event-id (xml1-> show :EventID text)
       :title    (xml1-> show :Title text)
       :original-title (xml1-> show :OriginalTitle text)
       :theatre  (xml1-> show :TheatreAndAuditorium text)
       :time     (xml1-> show :dttmShowStart text)
       :day      (str (.toLocalDate (parse (xml1-> show :dttmShowStart text))))
       :genres   (into #{} (split (xml1-> show :Genres text) #", "))
       :image    (xml1-> show :Images :EventLargeImagePortrait text)})))

(defn parse-movies [s]
  (let [xz (zip-string s)]
    (for [movie (xml-> xz :Event)]
      {:id       (xml1-> movie :ID text)
       :title    (xml1-> movie :Title text)
       :original-title (xml1-> movie :OriginalTitle text)
       :synopsis (xml1-> movie :Synopsis text)
       :image    (xml1-> movie :Images :EventLargeImageLandscape text)})))

