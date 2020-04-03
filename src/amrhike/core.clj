(ns amrhike.core
  (:require [datahike.api :as d]
            [clojure.java.io :as io]
            [clojure.edn :as edn]
            [cheshire.core :refer :all]))

(defn load-edn
  "Load edn from an io/reader source (filename or io/resource)."
  [source]
  (try
    (with-open [r (io/reader source)]
      (edn/read (java.io.PushbackReader. r)))
    (catch java.io.IOException e
      (printf "Couldn't open '%s': %s\n" source (.getMessage e)))
    (catch RuntimeException e
      (printf "Error parsing edn file '%s': %s\n" source (.getMessage e)))))

(defn remove-nils
  [record]
  (into {} (remove (comp nil? second) record)))

(defn -main
  ""
  [& args]
  (def uri "datahike:file:///tmp/amrhike")
  (def schema (load-edn (io/resource "schema.edn" )))
  (def abricate-data (vec (map remove-nils (parse-stream (io/reader (io/resource "abricate-data.json")) true))))
  (def amrfinder-data (vec (map remove-nils (parse-stream (io/reader (io/resource "amrfinder-data.json")) true))))
  (def rgi-data (vec (map remove-nils (parse-stream (io/reader (io/resource "rgi-data.json")) true))))
  
  (d/delete-database uri)
  (d/create-database uri)
  (def conn (d/connect uri))
  (d/transact conn schema)
  
  (d/transact conn abricate-data)
  (d/transact conn amrfinder-data)
  (d/transact conn rgi-data)
  
  (def catA1-hits-query '[:find
                          ?sample ?tool ?gene ?contig ?start ?stop
                          :where
                          [?e :gene_symbol "catA1"]
                          [?e :gene_symbol ?gene]
                          [?e :sample_id ?sample]
                          [?e :analysis_software_name ?tool]
                          [?e :contig_id ?contig]
                          [?e :start ?start]
                          [?e :stop ?stop]])

  (def output-keys [:sample_id :analysis_software_name :gene_symbol :contig_id :start :stop])
  (def cat1-hits (map #(zipmap output-keys %) (d/q catA1-hits-query @conn)))
  
  (print (generate-string cat1-hits {:pretty true}))
  (d/release conn)
  (d/delete-database uri)
)

(comment
  (def amr-genomic-results [{:sample_id "SAMEA6058467"
                             :input_file_name "GCA_009625195.1_PDT000607292.1_genomic.fna"
                             :contig_id "DAAGAT010000041.1"
                             :start 222
                             :stop 881
                             :strand_orientation "+"
                             :gene_symbol "catA1"
                             :coverage_percent 100.0
                             :sequence_identity 99.85
                             :reference_database_id "ncbi"
                             :reference_database_version "35f5ea86fce565dd6861f79cbc578b7cc4c3d604"                 
                             :analysis_software_name "ABRicate",
                             :analysis_software_version "0.9.8"}
                            {:sample_id "SAMEA6058467"
                             :contig_id "DAAGAT010000041.1"
                             :start 222
                             :stop 878
                             :strand_orientation "+"
                             :gene_symbol "catA1"
                             :coverage_percent 100.0
                             :sequence_identity 100.0
                             :reference_database_id "ncbi"
                             :reference_database_version "2020-01-22.1"
                             :analysis_software_name "AMRFinderPlus"
                             :analysis_software_version "3.6.10"}])
  )
