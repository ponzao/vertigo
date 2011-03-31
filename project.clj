(defproject vertigo "0.0.1-SNAPSHOT"
  :description "TODO: add summary of your project"
  :dependencies [[clojure "1.2.0"]
                 [clojure-contrib "1.2.0"]
                 [hiccup "0.3.4"]
                 [compojure "0.6.1"]
                 [ring/ring-jetty-adapter "0.3.1"]
                 [clj-time "0.2.0-SNAPSHOT"]
                 [log4j "1.2.15" :exclusions [javax.mail/mail
                                              javax.jms/jms
                                              com.sun.jdmk/jmxtools
                                              com.sun.jmx/jmxri]]]
  :dev-dependencies [[appengine-magic "0.4.0"]])

