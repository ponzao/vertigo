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

(def top-movies
  [{:title "Black Swan" :small-image "http://media.finnkino.fi/1012/Event_7560/portrait_large/Black_Swan_99a.jpg"}
   {:title "Black Swan" :small-image "http://media.finnkino.fi/1012/Event_7560/portrait_large/Black_Swan_99a.jpg"}
   {:title "Black Swan" :small-image "http://media.finnkino.fi/1012/Event_7560/portrait_large/Black_Swan_99a.jpg"}
   {:title "Black Swan" :small-image "http://media.finnkino.fi/1012/Event_7560/portrait_large/Black_Swan_99a.jpg"}
   {:title "Black Swan" :small-image "http://media.finnkino.fi/1012/Event_7560/portrait_large/Black_Swan_99a.jpg"}
   {:title "Black Swan" :small-image "http://media.finnkino.fi/1012/Event_7560/portrait_large/Black_Swan_99a.jpg"}
   {:title "Black Swan" :small-image "http://media.finnkino.fi/1012/Event_7560/portrait_large/Black_Swan_99a.jpg"}
   {:title "Black Swan" :small-image "http://media.finnkino.fi/1012/Event_7560/portrait_large/Black_Swan_99a.jpg"}
   {:title "Black Swan" :small-image "http://media.finnkino.fi/1012/Event_7560/portrait_large/Black_Swan_99a.jpg"}])

(def selected-movie {:title       "The Tourist"
                     :large-image "http://media.finnkino.fi/1012/Event_7531/landscape_large/The_Tourist_670.jpg"
                     :synopsis    "Johnny Depp nähdään elokuvan The Tourist pääosassa amerikkalaisena turistina,
                                   jonka harmiton suhde ventovieraan naisen kanssa alkaa punoa romantiikan ja
                                   juonittelun aineksista hengenvaarallista verkkoa.
    
                                   Frank (Depp) matkustaa hetken mielijohteesta Eurooppaan hoitamaan särkynyttä
                                   sydäntään, mutta löytää itsensä varsin pian flirttailemasta hurmaavan Elisen
                                   (Angelina Jolie) kanssa, jonka tapaaminen ei kuitenkaan ole silkkaa sattumaa.
                                   Heidän salamaromanssinsa etenee pikavauhtia Pariisin ja Venetsian upeissa
                                   maisemissa, mutta aivan yhtä nopeasti he saavat huomata tempautuneensa mukaan
                                   vaaralliseen kissa ja hiiri -leikkiin."})

(defroutes handler
  (GET "/helsinki" [] 
    (views/movie-page movies-by-date))
  (GET "/movies" []
    (views/top-movies top-movies))
  (GET "/movies/:id" [id]
    (views/selected-movie selected-movie)))

(ae/def-appengine-app vertigo-app #'handler)

