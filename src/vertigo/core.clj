(ns vertigo.core
  (:require [vertigo.views :as views]
            [appengine-magic.core :as ae])
  (:use     [compojure.core]
            [clj-time.core :only (now)]))

(defn create-show [id time title]
  {:id id :time time :title title})

(def movies-by-date 
  (let [today (now)
        tomorrow (.plusDays today 1)]
    [[today {:comedy [(create-show 1 today "lil fuckers")]
             :action [(create-show 2 (.minusHours today 6) "inception")
                      (create-show 3 today "turisti")]
             :drama  [(create-show 4 today "king's speech")]
             :others []}]]))

(defroutes handler
  (GET "/helsinki" [] 
    (views/movie-page movies-by-date))
  (GET "/movies" []
    (views/top-movies top-movies))
  (GET "/movies/:id" [id]
    (views/selected-movie selected-movie)))

(ae/def-appengine-app vertigo-app #'handler)

