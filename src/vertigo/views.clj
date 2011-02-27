(ns vertigo.views
  (:use [hiccup.core]
        [hiccup.page-helpers]))

(defn layout [& content]
  (html (list [:!DOCTYPE {:html ""}] ; FIXME This is broken!
              [:html {:lang "fi"}
                [:head
                  [:meta {:charset "utf-8"}]
                  (include-css "/style.css")]
                [:body
                  [:div#content content]]])))

(defn- between? [obj start end]
  (and (<= start obj)
       (<= obj   end)))

(defn- render-in-time-range [movies start end]
  [:ul
    (for [movie movies
           :when (-> (:time movie)
                     (.getHourOfDay)
                     (between? start end))]
      [:li (:time movie) (:title movie)])])

(defn- render-all-in-time-range [genres start end]
  (list
    [:td (render-in-time-range (:comedy genres) start end)]
    [:td (render-in-time-range (:action genres) start end)]
    [:td (render-in-time-range (:drama genres) start end)]
    [:td (render-in-time-range (:others genres) start end)]))


(defn movie-calendar [movies-by-date]
  [:table
    [:tr
      [:th]
      [:th "komedia"]
      [:th "toiminta"]
      [:th "draama"]
      [:th "muut"]]
    (for [[day genres] movies-by-date]
      (list [:tr
              [:td (str day)]]
            [:tr
              [:td]
              [:td {:colspan 4} "10.00"]]
            [:tr
              [:td]
              (render-all-in-time-range genres 10 14)]
            [:tr
              [:td]
              [:td {:colspan 4} "15.00"]]
            [:tr
              [:td]
              (render-all-in-time-range genres 15 18)]
            [:tr
              [:td]
              [:td {:colspan 4} "19.00"]]
            [:tr
              [:td]
              (render-all-in-time-range genres 19 22)]
            [:tr
              [:td]
              [:td {:colspan 4} "23.00"]]
            [:tr
              [:td]
              (render-all-in-time-range genres 23 23)]))])

(defn top-movies []
  [:table
    [:tr
      [:td [:article "IMAGE" [:h1 "black swan"]]]
      [:td "IMAGE" [:h1 "inception"]]
      [:td "IMAGE" [:h1 "meet the fuckers"]]]
    [:tr
      [:td "IMAGE" [:h1 "rare exports"]]
      [:td "IMAGE" [:h1 "harry potter"]]
      [:td "IMAGE" [:h1 "the tourist"]]]
    [:tr
      [:td "IMAGE" [:h1 "king's speech"]]
      [:td "IMAGE" [:h1 "los ojos de julia"]]
      [:td "IMAGE" [:h1 "tron"]]]])

(defn viewed-movie []
  [:table
    [:tr
      [:td "IMAGE"]]
    [:tr
      [:td [:h1 "black swan"]
           [:p  "Black Swan on elokuva..."]]]])

(defn movie-page [movies-by-date]
  (layout (list [:table {:style "width: 100%;"}
                  [:tr
                    [:td [:h1 "KINOT.FI"]]
                    [:td]]
                  [:tr
                    [:td (viewed-movie)]
                    [:td (top-movies)]]
                  [:tr
                    [:td "TILAA BLAA BLAA BLAA"]
                    [:td "DIGGAA MEITÄ FB:ssä"]]]
                (movie-calendar movies-by-date))))

