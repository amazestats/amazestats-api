(defproject amazestats "0.3.0"
  :description "FIXME: write description"
  :url "http://amazestats-api.herokuapp.com"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :min-lein-version "2.9.1"
  :dependencies [[buddy/buddy-auth "2.1.0"]
                 [buddy/buddy-hashers "1.3.0"]
                 [buddy/buddy-sign "2.2.0"]
                 [clojure.jdbc/clojure.jdbc-c3p0 "0.3.1"]
                 [compojure "1.6.1"]
                 [environ "1.1.0"]
                 [migratus "1.2.8"]
                 [org.clojure/clojure "1.10.0"]
                 [org.clojure/java.jdbc "0.7.9"]
                 [org.clojure/tools.logging "0.4.1"]
                 [org.postgresql/postgresql "42.2.5"]
                 [org.slf4j/slf4j-log4j12 "1.7.25"]
                 [ring/ring-core "1.7.1"]
                 [ring/ring-defaults "0.3.2"]
                 [ring/ring-json "0.4.0"]
                 [ring-cors "0.1.13"]]
  :plugins [[lein-cloverage "1.1.2"]
            [lein-ring "0.12.5"]
            [lein-environ "1.1.0"]]
  :ring {:handler amazestats.main/handler
         :host "0.0.0.0"
         :init amazestats.main/init
         :port 8000
         :open-browser? false}
  :profiles {:uberjar {:aot :all
                       :uberjar-name "amazestats.jar"}})
