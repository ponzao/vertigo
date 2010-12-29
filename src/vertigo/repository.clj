(ns vertigo.repository)

(defn save-movie [movie])

(defn retrieve-movie [id])

(def db (atom {}))

(defn save-movies [movies]
  (swap! db assoc (:date     movies)
                  (:by-genre movies)))

(defn retrieve-movies [date]
  {:date     (unparse (formatter "dd.MM.yyyy") date)
   :by-genre (get @db :date)})

