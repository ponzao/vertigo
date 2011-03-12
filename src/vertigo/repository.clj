(ns vertigo.repository
  (:use clojure.contrib.logging)
  (:use [clj-time.coerce :only (to-long from-long)])
  (:import java.util.UUID)
  (:require [appengine-magic.services.datastore :as ds]))

(org.apache.log4j.BasicConfigurator/configure) 

(ds/defentity Movie [])

(defn save-movie [movie]
  (ds/save! (merge (Movie.)
                   (update-in movie [:released] to-long))))

(defn get-movie [id]
  (-> (ds/retrieve Movie id)
      (update-in [:released] from-long)))

(defn get-movies-sorted-by-release []
  (ds/query :kind Movie
            :sort :released))

(ds/defentity Event [])

(defn save-event [event]
  (ds/save! (merge (Event.)
                   (update-in event [:date] to-long))))

(defn retrieve-events-between-dates [start-date end-date]
  (->> (ds/query :kind Event
                 :filter [(>= :date (to-long start-date))
                          (<  :date (to-long end-date))])
       (map #(update-in % [:date] from-long))))

