(ns vertigo.views
  (:require [vertigo.common :as common])
  (:use [hiccup.core]
        [hiccup.page-helpers]
        [clj-time.core :only (now)]
        [clj-time.format]))

(defn layout [& content]
  (html [:html
          [:head
            [:meta {:http-equiv "content-type"
                    :content    "text/html; charset=utf-8"}]
            (include-css "/style.css")]
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

(defn movie [movie]
  (layout
    [:div
      [:img {:src (:image movie)}]
      [:h1 (:title movie)]
      [:p  (.getValue (:synopsis movie))]
      [:script {:src "http://widgets.twimg.com/j/2/widget.js"}]
      [:script "new TWTR.Widget({
                  version: 2,
                  type: 'search',
                  search: '" (:original-title movie) "',
                  interval: 6000,
                  title: '',
                  subject: '',
                  width: 250,
                  height: 300,
                  theme: {
                    shell: {
                      background: '#8ec1da',
                      color: '#ffffff'
                    },
                    tweets: {
                      background: '#ffffff',
                      color: '#444444',
                      links: '#1985b5'
                    }
                  },
                  features: {
                    scrollbar: false,
                    loop: true,
                    live: true,
                    hashtags: true,
                    timestamp: true,
                    avatars: true,
                    toptweets: true,
                    behavior: 'default'
                  }
                }).render().start();"]]))

