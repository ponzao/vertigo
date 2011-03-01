(ns vertigo.views
  (:use [hiccup.core]
        [hiccup.page-helpers]
        [clj-time.core]
        [clj-time.format]))

(defn layout [title & page]
  (html (list [:!DOCTYPE {:html "HTML5"}] ; FIXME This is broken!
              [:html {:lang "fi"}
                [:head
                  [:meta {:charset "utf-8"}]
                  (include-css "/style.css")
                  [:title title]]
                [:body page]]))) 

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
  [:table {:border 1}
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
  (let [partitioned-movies (partition-all 3 movies)]
    [:table
      (for [three-movies partitioned-movies]
        [:tr
          (for [movie three-movies]
            [:td [:article [:img {:src (:small-image movie) :alt "Movie poster."}]
                           [:header [:h1 (:title movie)]]]])])]))

(defn viewed-movie [movie]
  [:article
    [:img {:src (:large-image movie) :alt "Big movie poster."}]
    [:header [:h1 (:title movie)]]
    [:p (:synopsis movie)]])
    
(defn movie-page [movies-by-date]
  (layout
    "kinos"
    [:header [:h1 "kinos"]]
    [:content (viewed-movie {:title       "The Tourist"
                             :large-image "http://media.finnkino.fi/1012/Event_7531/landscape_large/The_Tourist_670.jpg"
                             :synopsis    "Johnny Depp nähdään elokuvan The Tourist pääosassa amerikkalaisena turistina,
                                           jonka harmiton suhde ventovieraan naisen kanssa alkaa punoa romantiikan ja
                                           juonittelun aineksista hengenvaarallista verkkoa.
    
                                           Frank (Depp) matkustaa hetken mielijohteesta Eurooppaan hoitamaan särkynyttä
                                           sydäntään, mutta löytää itsensä varsin pian flirttailemasta hurmaavan Elisen
                                           (Angelina Jolie) kanssa, jonka tapaaminen ei kuitenkaan ole silkkaa sattumaa.
                                           Heidän salamaromanssinsa etenee pikavauhtia Pariisin ja Venetsian upeissa
                                           maisemissa, mutta aivan yhtä nopeasti he saavat huomata tempautuneensa mukaan
                                           vaaralliseen kissa ja hiiri -leikkiin."})
              (top-movies [{:title "Black Swan" :small-image "http://media.finnkino.fi/1012/Event_7560/portrait_large/Black_Swan_99a.jpg"}
                           {:title "Black Swan" :small-image "http://media.finnkino.fi/1012/Event_7560/portrait_large/Black_Swan_99a.jpg"}
                           {:title "Black Swan" :small-image "http://media.finnkino.fi/1012/Event_7560/portrait_large/Black_Swan_99a.jpg"}
                           {:title "Black Swan" :small-image "http://media.finnkino.fi/1012/Event_7560/portrait_large/Black_Swan_99a.jpg"}
                           {:title "Black Swan" :small-image "http://media.finnkino.fi/1012/Event_7560/portrait_large/Black_Swan_99a.jpg"}
                           {:title "Black Swan" :small-image "http://media.finnkino.fi/1012/Event_7560/portrait_large/Black_Swan_99a.jpg"}
                           {:title "Black Swan" :small-image "http://media.finnkino.fi/1012/Event_7560/portrait_large/Black_Swan_99a.jpg"}
                           {:title "Black Swan" :small-image "http://media.finnkino.fi/1012/Event_7560/portrait_large/Black_Swan_99a.jpg"}
                           {:title "Black Swan" :small-image "http://media.finnkino.fi/1012/Event_7560/portrait_large/Black_Swan_99a.jpg"}])
              [:section "Tilaa uutiskirje!"]
              [:section "Likee FB:ssä"]
              (movie-calendar movies-by-date)]
    [:footer]))

