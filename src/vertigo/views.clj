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

(defn movie-calendar [movies-by-date]
  [:table
    [:tr
      [:th]
      [:th "komedia"]
      [:th "toiminta"]
      [:th "draama"]
      [:th "muut"]]
    [:tr
      [:td "torstai 24.2.2011"]
      [:td "10.00"]
      [:td]
      [:td]
      [:td]
    [:tr
      [:td]
      [:td "10.45 lil fuckers"]
      [:td [:div "12.15 inception"]
           [:div "14.30 turisti"]]
      [:td "10.00 kings speech"]
      [:td]]
    [:tr
      [:td]
      [:td "15.00"]
      [:td]
      [:td]
      [:td]]
    [:tr
      [:td]
      [:td]
      [:td]
      [:td "15.30 ojos de julia"]
      [:td]]
    [:tr
      [:td]
      [:td "17.00"]
      [:td]
      [:td]
      [:td]]
    [:tr
      [:td]
      [:td]
      [:td]
      [:td "17.30 ojos de julia"]
      [:td]]
    [:tr
      [:td]
      [:td "19.00"]
      [:td]
      [:td]
      [:td]]
    [:tr
      [:td]
      [:td]
      [:td]
      [:td "19.30 ojos de julia"]
      [:td]]
    [:tr
      [:td]
      [:td "21.00"]
      [:td]
      [:td]
      [:td]]
    [:tr
      [:td]
      [:td]
      [:td]
      [:td "21.30 ojos de julia"]
      [:td]]
   [:tr
      [:td "perjantai 25.2.2011"]
      [:td "10.00"]
      [:td]
      [:td]
      [:td]
    [:tr
      [:td]
      [:td "10.45 lil fuckers"]
      [:td [:div "12.15 inception"]
           [:div "14.30 turisti"]]
      [:td "10.00 kings speech"]
      [:td]]
    [:tr
      [:td]
      [:td "15.00"]
      [:td]
      [:td]
      [:td]]
    [:tr
      [:td]
      [:td]
      [:td]
      [:td "15.30 ojos de julia"]
      [:td]]
    [:tr
      [:td]
      [:td "17.00"]
      [:td]
      [:td]
      [:td]]
    [:tr
      [:td]
      [:td]
      [:td]
      [:td "17.30 ojos de julia"]
      [:td]]
    [:tr
      [:td]
      [:td "19.00"]
      [:td]
      [:td]
      [:td]]
    [:tr
      [:td]
      [:td]
      [:td]
      [:td "19.30 ojos de julia"]
      [:td]]
    [:tr
      [:td]
      [:td "21.00"]
      [:td]
      [:td]
      [:td]]
    [:tr
      [:td]
      [:td]
      [:td]
      [:td "21.30 ojos de julia"]
      [:td]]]]])

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

