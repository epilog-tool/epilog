EpiLog (Epithelium Logical modelling) 
=========================================================


EpiLog supports the definition, simulation and visualisation of qualitative, logical models over hexagonal grids of cells.  Such models conveniently support the study of epithelial pattern formation.

EpiLog defines hexagonal cellular automata, where the behaviour of each cell is governed by the associated (logical) cellular model, subject to input signals from neighbouring cells or to other positional cues. Signalling is defined through appropriate logical functions, which qualitatively handle signal ranges and synergies.

Cellular models should be provided in the [SBML-qual](http://sbml.org/Community/Wiki/SBML_Level_3_Proposals/Qualitative_Models) format, being generated using e.g. [GINsim](http://www.ginsim.org). Internal handling of logical models is made with the support of [bioLQM](https://github.com/colomoto/bioLQM).

How to use it?
--------------
To compile EpiLog all you will need is java6 JDK and [maven](http://maven.apache.org/).

* grab the source from github
* run "mvn package assembly:single" to compile and package it.
* You can use the jar in the "target" subdirectory.

The command line is:
    java -jar EpiLog-v1.1.jar


Licence
--------------
This code is available under GPL-3.0.

Implementation
--------------

Pedro T. Monteiro  
Pedro L. Varela  
Camila Ramos  

Project supervision
--------------
Claudine Chaouiya & Pedro T. Monteiro  

Ackowledgements
--------------
A. Fauré & C. Hernandez for insightful comments

