(ns vertigo.repository
  (:require [vertigo.common :as common]
            [appengine-magic.services.datastore :as ds])
  (:use     [clj-time.core :only (now)]
            [clj-time.format]))

(ds/defentity Show [^:key id])

(ds/defentity Movie [^:key id])

(defn save-movies [movies]
  (map
    (fn [movie]
      (ds/save!
        (merge (Movie. nil)
               (update-in movie [:synopsis] ds/as-text))))
    movies))

(defn retrieve-movie [id]
  (merge (ds/query :kind Show
                   :filter (= :event-id id)))
          (ds/retrieve Movie id))

(defn save-shows [shows]
  (map
    (fn [show]
      (let [id (str (:event-id show) (:time show))]
        (ds/save! (merge (Show. id) show))))
    shows))

(defn retrieve-shows [day]
  (ds/query :kind Show
            :filter (= :day day)
            :sort :time))

(defn retrieve-dates-and-shows []
  (for [day (take 4 (iterate #(.plusDays % 1) (.toLocalDate (now))))]
    [day
     (group-by
       (fn [{genres :genres}]
         (cond (and (some #{"Komedia"} genres)
                    (some #{"Draama"} genres)) :drama
               (some #{"Komedia"} genres) :comedy
               (some #{"Toiminta" "Sci-fi" "Seikkailu" "Fantasia"} genres) :action
               (some #{"Draama" "Romantiikka"} genres) :drama
               :otherwise :other))
       (retrieve-shows (str day)))]))

