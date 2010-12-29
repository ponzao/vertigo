(ns vertigo.parser
  (:require [clojure.xml :as xml]
            [clojure.zip :as zip]
  (:use     [clojure.contrib.zip-filter.xml]
            [clojure.string :only (split)])))

(defn movies-by-genre [xz]
  (group-by
    (fn [{genres :genres}]
      (condp some genres
        #{"Komedia"} :comedy
        #{"Toiminta" "Seikkailu" "Sci-fi" "Fantasia"}
          :action-adventure-scifi-fantasy
        #{"Draama" "Romantiikka"} :drama-romance
        #{"Jännitys" "Kauhu"} :thriller-horror
        :other))
    (for [movie (xml-> xz :Shows :Show)]
          (Movie.
            (xml1-> movie :EventID text)
            (xml1-> movie :OriginalTitle text)
            (xml1-> movie :TheatreAndAuditorium text)
            (into #{} (split (xml1-> movie :Genres text) #", "))
            (xml1-> movie :Images :EventLargeImagePortrait text)))))

