
(defproject advent-of-code-2021 "0.1.0-SNAPSHOT"
  :description "Advent of Code 2021 Solutions"
  :url "https://github.com/Soft/advent-of-code-2021"
  :license {:name "MIT"}
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [org.clojure/data.priority-map "1.1.0"]]
  :profiles {:dev {:resource-paths ["test/resources"]}}
  :main advent-of-code-2021.core
  :aot [advent-of-code-2021.core])
