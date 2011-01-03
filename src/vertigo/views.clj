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
    [:span (link-to (str "/movies/" (:event-id show)) (:title show))] (:time show)])

(defn- render-genres [coll genres]
  (for [genre genres]
    [:td (map render-show (genre coll))]))

(defn- shows-table [shows]
  [:table
    [:tr
      [:th "Date"]
      [:th "Comedy"]
      [:th "Action/Adventure/Sci-fi/Fantasy"]
      [:th "Drama/Romance"]
      [:th "Others"]]
    (for [date (take 7 (iterate #(.plusDays % 1) (now)))]
      (let [genres (group-by
                     (fn [{genres :genres}]
                       (cond (and (some #{"Komedia"} genres)
                                  (some #{"Draama"} genres)) :drama
                             (some #{"Komedia"} genres) :comedy
                             (some #{"Toiminta" "Sci-fi" "Seikkailu" "Fantasia"} genres) :action
                             (some #{"Draama" "Romantiikka"} genres) :drama
                             :otherwise :other))
                     (filter (fn [show] (= (.toLocalDate date)
                                           (.toLocalDate (parse (:time show)))))
                             shows))]
        [:tr
          [:td (common/format-date date)]
          (render-genres genres [:comedy :action :drama :other])]))])


(defn shows [shows]
  (layout (shows-table shows)))

; TODO: Implement!
(defn movie [id]
)
