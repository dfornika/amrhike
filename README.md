# amrhike

A proof-of-concept for storage and querying of [harmonized Antimicrobial Resistance Genomic Analysis Results](https://github.com/pha4ge/harmonized-amr-parsers)

## Installation

Follow installation instructions for [Leiningen](https://leiningen.org/) for your system

## Usage

```bash
lein run
```

Currently, the program is designed to load a small set of harmonized AMR Genomic Analysis Result
files into a [datahike](https://github.com/replikativ/datahike) database.

It then runs the following query:

```edn
[:find
 ?sample ?tool ?gene ?contig ?start ?stop
 :where [?e :gene_symbol "catA1"]
        [?e :gene_symbol ?gene]
        [?e :sample_id ?sample]
        [?e :analysis_software_name ?tool]
        [?e :contig_id ?contig]
        [?e :start ?start]
        [?e :stop ?stop]]
```

...which essentially means "find all results where the `catA1` gene was found, and display a subset of fields associated with those results"

The result is printed in JSON format to standard output, and should look like:

```json
[ {
  "sample_id" : "SAMEA6058467",
  "analysis_software_name" : "AMRFinderPlus",
  "gene_symbol" : "catA1",
  "contig_id" : "DAAGAT010000085.1",
  "start" : 43,
  "stop" : 699
}, {
  "sample_id" : "SAMEA6058467",
  "analysis_software_name" : "AMRFinderPlus",
  "gene_symbol" : "catA1",
  "contig_id" : "DAAGAT010000041.1",
  "start" : 222,
  "stop" : 878
}, {
  "sample_id" : "SAMEA6058467",
  "analysis_software_name" : "ABRicate",
  "gene_symbol" : "catA1",
  "contig_id" : "DAAGAT010000041.1",
  "start" : 222,
  "stop" : 881
}, {
  "sample_id" : "SAMEA6058467",
  "analysis_software_name" : "ABRicate",
  "gene_symbol" : "catA1",
  "contig_id" : "DAAGAT010000085.1",
  "start" : 43,
  "stop" : 702
} ]
```

## License

Copyright © 2020 Dan Fornika

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
