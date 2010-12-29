(ns vertigo.repository)

(defn save-movie [movie])

(defn retrieve-movie [id])

(defn save-movies [movies]
  (conj [] movies))

(defn retrieve-movies [date]
  {:date     (unparse (formatter "dd.MM.yyyy") date)
   :by-genre []})

