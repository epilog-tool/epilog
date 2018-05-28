EpiLog (Epithelium Logical modelling) 
=========================================================

EpiLog supports the definition, simulation and visualisation of qualitative models for epithelial pattern formation.

It relies on a modelling framework where simple (one layer) epithelia are defined as multicellular systems involving intracellular regulatory models, cell-cell communication, as well as other environmental cues. These "epithelial models" are defined as cellular automata, where the behaviour of each cell is governed by its (logical) regulatory model, subject to input signals from neighbouring cells. Neighbouring relations are defined through appropriate functions, which qualitatively describe signalling ranges and integration.

EpiLog supports the loading of cellular models as [SBML-qual](http://sbml.org/Community/Wiki/SBML_Level_3_Proposals/Qualitative_Models) files, which may be modelled using tools such as [GINsim](http://www.ginsim.org).
Internal representation of Logical Models is made with the support of [bioLQM](https://github.com/colomoto/bioLQM).


How to use it?
--------------
To compile EpiLog all you will need is java6 JDK and [maven](http://maven.apache.org/).

* grab the source from github
* run "mvn package assembly:single" to compile and package it.
* You can use the jar in the "target" subdirectory.

The command line is java -jar EpiLog-v1.0.jar


Licence
--------------
This code is available under GPL-3.0.

Authors
--------------

Pedro Monteiro  
Pedro Varela  
Camila Ramos  

The rest of the EpiLog crew provided insight:   
Claudine Chaouiya     
Adrien Faure  

