(ns vertigo.batch
  (:require [vertigo.repository :as repository]
            [vertigo.common :as common]
            [vertigo.finnkino :as finnkino]))

(defn update-movies []
  (repository/save-movies
    (common/map-on-n-days
      #(parser/movies-by-genre (finnkino/retrieve-movies %))
      8)))

