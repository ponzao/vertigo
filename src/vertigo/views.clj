(ns vertigo.views
  (:use [hiccup.core]
        [hiccup.page-helpers]
        [clj-time.core :only (now)]
        [clj-time.format :only (formatters
                                formatter
                                unparse)]))

(defn layout [title & body]
  (html (list [:!DOCTYPE {:html "HTML5"}] ; FIXME This is broken!
              [:html {:lang "fi"}
                [:head
                  [:meta {:charset "utf-8"}]
                  (include-css "/style.css")
                  (include-js "/jquery.js")
                  (include-js "/main.js")
                  [:title title]]
                [:body body]]))) 

(defn- between? [obj start end]
  (and (<= start obj)
       (<= obj   end)))

(defn- parse-time [date-time]
  (unparse (formatters :hour-minute) date-time))

(defn- parse-date [date-time]
  (unparse (formatter "d.M.yyyy") date-time))

(defn- render-in-time-range [movies start end]
  [:ul
    (for [movie movies
           :when (-> (:date movie)
                     (.getHourOfDay)
                     (between? start end))]
      [:li (parse-time (:date movie)) (:title movie)])])

(defn- render-all-in-time-range [genres start end]
  (for [genre [:comedy :action :drama :others]]
    [:td (render-in-time-range (get genres genre) start end)]))

(defn group-by-genre [events]
  (group-by
    (fn [{genres :genres}]
      (cond (and (some #{"Comedy"} genres)
                 (some #{"Drama"} genres)) :drama
            (some #{"Comedy"} genres) :comedy
            (some #{"Action" "Sci-fi" "Adventure" "Fantasy"} genres) :action
            (some #{"Drama" "Romance"} genres) :drama
            :otherwise :other))
    events))

(defn event-calendar [events-by-date]
  [:table
    [:tr [:th] [:th "Komedia"] [:th "Toiminta"] [:th "Draama"] [:th "Muut"]]
    (for [[day events] events-by-date]
      (for [genres (group-by-genre events)]
        genres))])
;        (list [:tr [:td {:colspan 5} (parse-date day)]]
;              [:tr [:td] [:td {:colspan 4} "10.00"]]
;              [:tr [:td] (render-all-in-time-range genres 10 14)]
;              [:tr [:td] [:td {:colspan 4} "15.00"]]
;              [:tr [:td] (render-all-in-time-range genres 15 18)]
;              [:tr [:td] [:td {:colspan 4} "19.00"]]
;              [:tr [:td] (render-all-in-time-range genres 19 22)]
;              [:tr [:td] [:td {:colspan 4} "23.00"]]
;              [:tr [:td] (render-all-in-time-range genres 23 23)])))])

(defn newest-movies [movies]
  (html
    [:table
      (let [partitioned-movies (partition-all 3 movies)]
        (for [three-movies partitioned-movies]
          [:tr
            (for [movie three-movies]
              [:td [:img {:src (:small-image-url movie) :alt "Movie poster."}]
                   [:header [:h3 (:title movie)]]])]))]))

(defn selected-movie [movie]
  (html
    [:div
      [:img {:src (:large-image-url movie) :alt "Big movie poster."}]
      [:header [:h2 (:title movie)]]
      [:p (:synopsis movie)]]))
    
(defn movie-page [events-by-date]
  (layout
    "kinos"
    [:div#content-wrapper
      [:header [:h1 "kinos"]]
      [:content
        [:table
          [:tr [:td#selected-movie ""] [:td#newest-movies ""]]
          [:tr [:td#newsletter "Tilaa uutiskirje!"] [:td#share "Likee FB:ssä"]]
          [:tr [:td#calendar {:colspan 2} (event-calendar events-by-date)]]]]
      [:footer]]))

