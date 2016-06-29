# SHACL runner

Prototype of validation checks for OBO ontologies using the [Shapes Constraint Language (SHACL)](https://www.w3.org/TR/shacl/#constraints-inverse-property). This test runner is only a shell for the [SHACL implementation by TopQuadrant](https://github.com/TopQuadrant/shacl).

## Test shapes
An initial set of implemented constraints can be found in [shapes.ttl](https://github.com/balhoff/shacl-runner/blob/master/shapes.ttl).

## Building

First, install the TopBraid SHACL library since it is not yet in Maven central:

```bash
git clone git@github.com:TopQuadrant/shacl.git
cd shacl
mvn install
```

Install `sbt` on your system. For Mac OS X, it is easily done using [Homebrew](http://brew.sh): `brew install sbt`

`sbt compile`

## Run checks on an ontology
To run from sbt:

Download an ontology to the project directory (e.g. `pato.owl`).

`sbt -mem 8000 "run pato.owl shapes.ttl"`
