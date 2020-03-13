(defproject amazestats "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://amazestats-api.herokuapp.com"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :min-lein-version "2.9.1"
  :dependencies [[clojure.jdbc/clojure.jdbc-c3p0 "0.3.1"]
                 [compojure "1.6.1"]
                 [environ "1.1.0"]
                 [org.clojure/clojure "1.10.0"]
                 [org.clojure/java.jdbc "0.7.9"]
                 [org.clojure/tools.logging "0.4.1"]
                 [org.postgresql/postgresql "42.2.5"]
                 [ring/ring-core "1.7.1"]
                 [ring-cors "0.1.13"]
                 [ring/ring-defaults "0.3.2"]
                 [ring/ring-json "0.4.0"]]
  :plugins [[lein-ring "0.12.5"]
            [lein-environ "1.1.0"]]
  :ring {:handler amazestats.main/handler
         :port 8000
         :open-browser? false}
  :profiles {:uberjar {:aot :all
                       :uberjar-name "amazestats.jar"}})
