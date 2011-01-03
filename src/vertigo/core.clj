(ns vertigo.core
  (:require [vertigo.views :as views]
            [vertigo.common :as common]
            [vertigo.repository :as repository]
            [vertigo.batch :as batch]
            [appengine-magic.core :as ae])
  (:use     [compojure.core]))

(defroutes handler
  (GET "/movies" [] 
    (views/shows (repository/retrieve-shows)))
  (GET "/batch/shows" []
    (batch/update-shows)))

(ae/def-appengine-app vertigo-app #'handler)

