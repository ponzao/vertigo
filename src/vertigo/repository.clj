(ns vertigo.repository
  (:require [vertigo.common :as common]
            [appengine-magic.services.datastore :as ds]))

(ds/defentity Show [^:key id])

(defn save-shows [shows]
  (map
    (fn [show]
      (let [id (str (:event-id show) (:time show))]
        (ds/save! (merge (Show. id) show))))
    shows))

(defn retrieve-shows []
  (ds/query :kind Show))

