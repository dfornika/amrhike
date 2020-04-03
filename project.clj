(defproject amrhike "0.1.0-SNAPSHOT"
  :description "Proof of concept for storage and query of harmonized AMR results"
  :url ""
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [io.replikativ/datahike "0.2.1"]
                 [cheshire "5.10.0"]]
  :main ^:skip-aot amrhike.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
