(ns vertigo.batch
  (:require [vertigo.repository :as repository]
            [vertigo.common :as common]
            [vertigo.finnkino :as finnkino]
            [vertigo.parser :as parser]))

(defn update-movies []
  (common/map-on-n-days
    #(repository/save-movies % (parser/movies-by-genre (finnkino/retrieve-movies %)))
    8))

