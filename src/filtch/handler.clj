(ns filtch.handler
  (:require
   [aero.core :as ae]
   [clojure.java.io :as io]
   [compojure.core :refer [defroutes GET]]
   [compojure.route :as route]
   [filtch.twitter :as twitter]
   [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
   [selmer.parser :as selmer])
  (:gen-class))

(defonce cfg (ae/read-config (io/resource "config.edn")))

(defroutes app-routes
  (GET "/" [:as params]
    (let [q (:query (:params params))
          query (if (nil? q) "" q)
          streams (twitter/get-streams (:twitter cfg) query)]
      (selmer/render-file
       "templates/main.html"
       {:query query
        :streams streams})))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))

(defn -main [& args])
