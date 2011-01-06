(ns vertigo.views
  (:require [vertigo.common :as common])
  (:use [hiccup.core]
        [hiccup.page-helpers]
        [clj-time.core :only (now)]
        [clj-time.format]))

(defn layout [content]
  (html [:html
          [:head
            (include-css "style.css")]
          [:body
            [:div#content
              content]]]))

(defn- render-show [show]
  [:li (unparse (:hour-minute formatters) (parse (:time show)))
       (link-to
         (str "/movies/" (:event-id show)) 
         (:title show))])

(defn- render-shows [shows]
  [:ul (map render-show shows)])

(defn- shows-table [shows]
  [:table
    [:tr
      [:th "Date"]
      [:th "Comedy"]
      [:th "Action/Adventure/Sci-fi/Fantasy"]
      [:th "Drama/Romance"]
      [:th "Other"]]
    (for [[day genres] shows]
      [:tr
        [:td day]
        [:td (render-shows (:comedy genres))]
        [:td (render-shows (:action genres))]
        [:td (render-shows (:drama genres))]
        [:td (render-shows (:other genres))]])])

(defn shows [shows]
  (layout (shows-table shows)))

; TODO: Implement!
(defn movie [id]
)
