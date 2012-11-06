(defproject reba "0.0.1-SNAPSHOT"
  :description "Prototype of a binding library authored in ClojureScript."
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [org.clojure/clojurescript "0.0-1424"]
                 [hiccups "0.1.1"]]
  :plugins [[lein-cljsbuild "0.2.7"]]
  :source-path "src/clj"
  :hooks [leiningen.cljsbuild]
  :cljsbuild {
    :builds [{
      :source-path "src/cljs"
      :compiler {
        :output-to "resources/public/examples.js"
        :optimizations :whitespace
        :pretty-print true}}]})
