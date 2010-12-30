(ns vertigo.app_servlet
  (:gen-class :extends javax.servlet.http.HttpServlet)
  (:use vertigo.core)
  (:use [appengine-magic.servlet :only [make-servlet-service-method]]))


(defn -service [this request response]
  ((make-servlet-service-method vertigo-app) this request response))
