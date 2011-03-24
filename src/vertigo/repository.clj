(ns vertigo.repository
  (:use clojure.contrib.logging
        [clj-time.core :only (date-time)]
        [clj-time.coerce :only (to-long from-long)])
  (:import java.util.UUID)
  (:require [appengine-magic.services.datastore :as ds]))

(org.apache.log4j.BasicConfigurator/configure) 

(ds/defentity Movie [])

(defn save-movie [movie]
  (ds/save! (merge (Movie.)
                   (update-in movie [:release-date] to-long))))

(defn save-movies [movies]
  (map save-movie movies))

(defn get-movie [id]
  (-> (ds/retrieve Movie id)
      (update-in [:release-date] from-long)))

(defn get-movies-sorted-by-release []
  (ds/query :kind Movie
            :sort :release-date))

(ds/defentity Event [])

(defn save-event [event]
  (ds/save! (merge (Event.)
                   (update-in event [:date] to-long))))

; FIXME Not tested
(defn save-events [events]
  (map save-event events))

(defn retrieve-events-between-dates [start-date end-date]
  (->> (ds/query :kind Event
                 :filter [(>= :date (to-long start-date))
                          (<  :date (to-long end-date))])
       (map #(update-in % [:date] from-long))
       ; FIXME toDateTimeMidnight...
       (group-by (fn [{date :date}]
                   (date-time
                     (.getYear date)
                     (.getMonthOfYear date)
                     (.getDayOfMonth date))))
       (sort)))

