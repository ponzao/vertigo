(ns vertigo.repository-test
  (:use [clojure.test]
        [clj-time.core :only (now)])
  (:require [vertigo.repository :as repository]
            [appengine-magic.testing :as ae-testing]))

(use-fixtures :each (ae-testing/local-services :all))

(def movies-by-genre
  {:comedy [{:title "Ferris"}
            {:title "Tropic Thunder"}]
   :action [{:title "Die Hard"}]})

(def date (now))

(deftest save-movies-and-retrieve-them
  (do (repository/save-movies date movies-by-genre)
      (is (= (-> (repository/retrieve-movies date)
                 :by-genre
                 keys)
             (list :comedy :action)))))

