(ns vertigo.views-test
  (:use clojure.test)
  (:require [vertigo.views :as views]))

(def events
  [{:id 1 :genres ["Fantasy"]}
   {:id 2 :genres ["Comedy" "Drama"]}
   {:id 3 :genres ["Drama"]}
   {:id 4 :genres ["Comedy"]}
   {:id 5 :genres ["Action"]}
   {:id 6 :genres ["Sci-fi"]}
   {:id 7 :genres ["Drama"]}
   {:id 8 :genres ["Horror"]}
   {:id 9 :genres ["Romance" "Horror"]}])

(deftest Events-Grouped-By-Genre
  (is (= {:drama  [{:id 2 :genres ["Comedy" "Drama"]}
                   {:id 3 :genres ["Drama"]}
                   {:id 7 :genres ["Drama"]}
                   {:id 9 :genres ["Romance" "Horror"]}]
          :comedy [{:id 4 :genres ["Comedy"]}]
          :action [{:id 1 :genres ["Fantasy"]}
                   {:id 5 :genres ["Action"]}
                   {:id 6 :genres ["Sci-fi"]}]
          :other  [{:id 8 :genres ["Horror"]}]}
         (views/group-by-genre events))))


