(ns vertigo.batch
  (:require [vertigo.repository :as repository]
            [vertigo.finnkino :as finnkino]
            [vertigo.parser :as parser])
  (:use [clj-time.core :only (now)]))

(defn- update-events []
  (for [date (take 8 (iterate #(.plusDays % 1) (now)))]
    (repository/save-events
      (parser/parse-events (finnkino/retrieve-events date)))))

(defn- update-movies []
  (repository/save-movies
      (parser/parse-movies (finnkino/retrieve-movies))))

(defn update []
  (update-movies)
  (update-events))

