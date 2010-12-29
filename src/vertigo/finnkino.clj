(ns vertigo.finnkino
  (:require [vertigo.common :as common]))

(defn retrieve-movies [date]
  (slurp (str
            "http://finnkino.fi/xml/Schedule/?area=1002&dt="
            (common/format-date date))))

