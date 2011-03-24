(ns vertigo.finnkino
  (:use [clj-time.format :only (formatters
                                formatter
                                unparse)]))

(defn retrieve-events [date]
  (slurp (str
            "http://www.finnkino.fi/xml/Schedule/?area=1002&dt="
            (unparse (formatter "d.M.yyyy") date))))

(defn retrieve-movies []
  (slurp (str
            "http://www.finnkino.fi/xml/Events/")))

