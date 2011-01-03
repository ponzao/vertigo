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
  [:div
    [:span (link-to (str "/movies/" (:id show)) (:title show))]])

(defn- shows-table [shows]
  [:table
    [:tr
      [:th "Date"]
      [:th "Comedy"]
      [:th "Action/Adventure/Sci-fi/Fantasy"]
      [:th "Drama/Romance"]
      [:th "Others"]]
    (for [date (take 4 (iterate #(.plusDays % 1) (now)))]
      (let [genres (group-by
                     (fn [{genres :genres}]
                       (cond (and (some #{"Komedia"} genres)
                                  (some #{"Draama"} genres)) :drama
                             (some #{"Komedia"} genres) :comedy
                             (some #{"Toiminta"} genres) :action
                             (some #{"Draama" "Romantiikka"} genres) :drama
                             :otherwise :other))
                     (filter (fn [show] (= (common/format-date date)
                                           "03.01.2011"))
                             shows))]
        [:tr
          [:td (common/format-date date)]
          [:td (map render-show (:comedy genres))]
          [:td (map render-show (:action genres))]
          [:td (map render-show (:drama genres))]
          [:td (map render-show (:other genres))]]))])

(defn shows [shows]
  (layout (shows-table shows)))

; TODO: Implement!
(defn movie [id]
)
