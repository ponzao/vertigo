(ns vertigo.common
  (:use [clj-time.format]
        [clj-time.core :only (now)]))

(defn format-date [date]
  (unparse (formatter "dd.MM.yyyy") date))

(defn map-on-n-days [f n]
  (let [dates (take n (iterate #(.plusDays % 1) (now)))]
    (map f dates)))

