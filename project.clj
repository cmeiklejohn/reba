(defproject reba "1.0.0-SNAPSHOT"
  :description "FIXME: write description"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [org.clojure/clojurescript "0.0-1424"]
                 [hiccups "0.1.1"]]
  :plugins [[lein-cljsbuild "0.2.7"]]
  :source-path "src/clj"
  :cljsbuild {
    :builds [{
      :source-path "src/cljs"
      :compiler {
        :output-to "resources/public/application.js"
        :optimizations :whitespace
        :pretty-print true}}]})
