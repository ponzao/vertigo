(ns vertigo.repository
  (:require [vertigo.common :as common]
            [appengine-magic.services.datastore :as ds]))

(ds/defentity Show [])

(defn save-shows [shows]
  (map
    (fn [show]
      (ds/save! (merge (Show.) show)))
    shows))

(defn retrieve-shows []
  (ds/query :kind Show))

