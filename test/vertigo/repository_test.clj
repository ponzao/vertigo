(ns vertigo.repository-test
  (:use clojure.test
        [clj-time.core :only (date-time)])
  (:require [vertigo.repository :as repository]
            [appengine-magic.testing :as ae-testing]))

(def movies
  [{:id          1
    :title       "Inception"
    :large-image "http://finnkino.fi/inception_large.jpg"
    :small-image "http://finnkino.fi/inception_small.jpg"
    :synopsis    "Inception is a great movie..."
    :released    (date-time 2010 1 1)}
   {:id          2
    :title       "The Tourist"
    :large-image "http://finnkino.fi/the_tourist_large.jpg"
    :small-image "http://finnkino.fi/the_tourist_small.jpg"
    :synopsis    "The Tourist sucks..."
    :released    (date-time 2011 1 1)}
   {:id          3
    :title       "The Black Swan"
    :large-image "http://finnkino.fi/the_black_swan_large.jpg"
    :small-image "http://finnkino.fi/the_black_swan_small.jpg"
    :synopsis    "The Black Swan is a film..."
    :released    (date-time 2009 1 1)}])

(defn database-fixture [f]
  (doseq [movie movies]
    (repository/save-movie movie))
  (f))

(use-fixtures :once (ae-testing/local-services :all)
                    database-fixture)

(deftest Retrieved-Movie-Is-Equal-To-Stored
  (is (= (first movies)
         (repository/get-movie 1))))

(deftest Movies-Are-Sorted-By-Release-Date
  (is (= (list 3 1 2)
         (map :id (repository/get-movies-sorted-by-release)))))
