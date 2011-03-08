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
           :when (-> (:time movie)
                     (.getHourOfDay)
                     (between? start end))]
      [:li (parse-time (:time movie)) (:title movie)])])

(defn- render-all-in-time-range [genres start end]
  (for [genre [:comedy :action :drama :others]]
    [:td (render-in-time-range (get genres genre) start end)]))

(defn movie-calendar [movies-by-date]
  [:table
    [:tr [:th] [:th "komedia"] [:th "toiminta"] [:th "draama"] [:th "muut"]]
    (for [[day genres] movies-by-date]
      (list [:tr [:td {:colspan 5} (parse-date day)]]
            [:tr [:td] [:td {:colspan 4} "10.00"]]
            [:tr [:td] (render-all-in-time-range genres 10 14)]
            [:tr [:td] [:td {:colspan 4} "15.00"]]
            [:tr [:td] (render-all-in-time-range genres 15 18)]
            [:tr [:td] [:td {:colspan 4} "19.00"]]
            [:tr [:td] (render-all-in-time-range genres 19 22)]
            [:tr [:td] [:td {:colspan 4} "23.00"]]
            [:tr [:td] (render-all-in-time-range genres 23 23)]))])

(defn top-movies [movies]
  (html
    [:table
      (let [partitioned-movies (partition-all 3 movies)]
        (for [three-movies partitioned-movies]
          [:tr
            (for [movie three-movies]
              [:td [:img {:src (:small-image movie) :alt "Movie poster."}]
                   [:header [:h3 (:title movie)]]])]))]))

(defn selected-movie [movie]
  (html
    [:div
      [:img {:src (:large-image movie) :alt "Big movie poster."}]
      [:header [:h2 (:title movie)]]
      [:p (:synopsis movie)]]))
    
(defn movie-page [movies-by-date]
  (layout
    "kinos"
    [:div#content-wrapper
      [:header [:h1 "kinos"]]
      [:content
        [:table
          [:tr [:td#selected-movie ""] [:td#top-movies ""]]
          [:tr [:td#newsletter "Tilaa uutiskirje!"] [:td#share "Likee FB:ssä"]]
          [:tr [:td#calendar {:colspan 2} (movie-calendar movies-by-date)]]]]
      [:footer]]))

