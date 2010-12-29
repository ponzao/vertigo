(ns vertigo.repository
  (:require [vertigo.common :as common]))

(defn save-movie [movie])

(defn retrieve-movie [id])

(def db (atom {}))

(defn save-movies [date movies]
  (swap! db assoc (common/format-date date) movies))

(defn retrieve-movies [date]
  {:date     (common/format-date date)
   :by-genre (get @db (common/format-date date))})

