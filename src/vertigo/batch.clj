(ns vertigo.batch
  (:require [vertigo.repository :as repository]
            [vertigo.common :as common]
            [vertigo.finnkino :as finnkino]
            [vertigo.parser :as parser])
  (:use     [clj-time.core :only (now)]))

(defn update-shows []
  (repository/save-shows
    (parser/parse-shows (finnkino/retrieve-shows (now)))))

