(ns vertigo.finnkino
  (:use [clj-time.format :only (formatters
                                formatter
                                unparse)]))

(defn retrieve-events [date]
  (slurp (str
            "http://www.finnkino.fi/xml/Schedule/?area=1002&dt="
            (unparse (formatter "dd.MM.yyyy") date))))

(defn retrieve-movies []
  (slurp "http://www.finnkino.fi/xml/Events/"))

