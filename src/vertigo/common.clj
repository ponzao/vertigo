(ns vertigo.common
  (:use [clj-time.format]))

(defn format-date [date]
  (unparse (formatter "dd.MM.yyyy") date))

