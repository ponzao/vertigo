(ns vertigo.batch
  (:require [vertigo.repository :as repository]
            [vertigo.common :as common]
            [vertigo.finnkino :as finnkino]
            [vertigo.parser :as parser])
  (:use     [clj-time.core :only (now)]))

(defn update-shows []
  (for [date (take 8 (iterate #(.plusDays % 1) (now)))]
    (repository/save-shows
      (parser/parse-shows (finnkino/retrieve-shows date)))))

(defn update-movies []
  (repository/save-movies 
      (parser/parse-movies (finnkino/retrieve-movies))))

