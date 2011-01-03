(ns vertigo.parser
  (:require [clojure.xml :as xml]
            [clojure.zip :as zip])
  (:use     [clojure.contrib.zip-filter.xml]
            [clojure.string :only (split)]))

(defn parse-shows [s]
  (let [xz (zip/xml-zip (xml/parse (java.io.ByteArrayInputStream. (.getBytes s))))]
    (for [show (xml-> xz :Shows :Show)]
      {:event-id (xml1-> show :EventID text)
       :title    (xml1-> show :OriginalTitle text)
       :theatre  (xml1-> show :TheatreAndAuditorium text)
       :time     (xml1-> show :dttmShowStart text)
       :genres   (into #{} (split (xml1-> show :Genres text) #", "))
       :image    (xml1-> show :Images :EventLargeImagePortrait text)})))

