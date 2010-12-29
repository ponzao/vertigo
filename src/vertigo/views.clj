(ns views
  (:use [hiccup.core]
        [hiccup.page-helpers]))

(defn layout [content]
  (html [:html
          [:head]
          [:body
            [:div#content
              content]]]))

(defn- render-movie [movie]
  [:div (link-to (str "/movies/" (:id movie)) (:title movie))])

(defn- movies-table [movies]
  [:table
    [:tr
      [:th "Date"]
      [:th "Comedy"]
      [:th "Action/Adventure/Sci-fi/Fantasy"]
      [:th "Drama/Romance"]
      [:th "Thriller/Horror"]
      [:th "Others"]]
    (for [{date      :date
           by-genres :by-genre} movies]
      [:tr
        [:td date]
        [:td (map render-movie (:comedy by-genre))]
        [:td (map render-movie (:action-adventure-scifi-fantasy by-genre))]
        [:td (map render-movie (:drama-romance by-genre))]
        [:td (map render-movie (:thriller-horror by-genre))]
        [:td (map render-movie (:other by-genre))]])])

(defn movies [movies]
  (layout (movies-table movies)))

