(ns vertigo.finnkino
  (:require [vertigo.common :as common]))

(defn retrieve-shows [date]
  (slurp (str
            "http://www.finnkino.fi/xml/Schedule/?area=1002&dt="
            (common/format-date date))))

