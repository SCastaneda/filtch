(defproject filtch "0.1.0-SNAPSHOT"
  :description "Search for twitch"
  :url "http://filtch.sam-the-man.com"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [compojure "1.6.1"]
                 [ring/ring-defaults "0.3.2"]
                 [selmer "1.12.31"]
                 [http-kit "2.4.0"]
                 [org.clojure/data.json "1.0.0"]
                 [aero "1.1.6"]]
  :plugins [[lein-ring "0.12.5"]]
  :ring {
         :handler filtch.handler/app
         :port 3000
         }
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.2"]]}})
