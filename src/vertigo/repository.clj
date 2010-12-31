(ns vertigo.repository
  (:require [vertigo.common :as common]
            [appengine-magic.services.datastore :as ds]))

(defn save-movie [movie])

(defn retrieve-movie [id])

(def db (atom {}))

(ds/defentity Movies [^:key date by-genre])

(defn save-movies [date movies]
;  (swap! db assoc (common/format-date date) movies))
  (ds/save! (Movies. (common/format-date date) movies)))

(defn retrieve-movies [date]
;  {:date     (common/format-date date)
;   :by-genre (get @db (common/format-date date))})
  {:date     (common/format-date date)
   :by-genre (ds/retrieve Movies (common/format-date date))})


