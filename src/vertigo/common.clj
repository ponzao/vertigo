(ns vertigo.common
  (:use [clj-time.format]
        [clj-time.core :only (now)]))

(defn format-date [date]
  (unparse (formatter "dd.MM.yyyy") date))

