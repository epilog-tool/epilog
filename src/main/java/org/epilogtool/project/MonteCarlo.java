package org.epilogtool.project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;
import org.colomoto.logicalmodel.perturbation.AbstractPerturbation;
import org.colomoto.logicalmodel.tool.simulation.updater.PriorityClasses;
import org.colomoto.logicalmodel.tool.simulation.updater.PriorityUpdater;
import org.epilogtool.common.RandomFactory;
import org.epilogtool.common.Tuple2D;
import org.epilogtool.core.Epithelium;
import org.epilogtool.core.EpitheliumGrid;
import org.epilogtool.core.EpitheliumUpdateSchemeInter;
import org.epilogtool.integration.IntegrationFunctionEvaluation;
import org.epilogtool.integration.IntegrationFunctionSpecification.IntegrationExpression;

/**
 * MonteCarlo
 * 
 * @author Pedro Varela
 * 
 */
public class MonteCarlo {
	
	private Epithelium epithelium;
	
	private int numberRuns;
	private int maxNumberIterations;
	
	public MonteCarlo(Epithelium e){
	
	this.epithelium = e;
	this.numberRuns = numberRuns;
	this.maxNumberIterations = maxNumberIterations;
	}
	
	public int getNumberRuns(){
		return this.numberRuns;
	}
	
	public void setNumberRuns(int nRuns){
		this.numberRuns = nRuns;
	}
	
	public int getMaxIter(){
		return this.maxNumberIterations;
	}
	
	public void setMaxIter(int maxIter){
		this.maxNumberIterations = maxIter;
	}

	public void run() {
		// TODO Auto-generated method stub
		for (int i = 0; i<this.numberRuns; i++){
			Simulation sim =new Simulation(this.epithelium);
			boolean flag = false;
			System.out.println("Running Simulation: "+i);
			for (int j=0; j<this.maxNumberIterations;j++){
				EpitheliumGrid nextGrid = sim.getGridAt(j + 1);
				if (sim.isStableAt(j+1)){
					flag = true;
					break;	
					
				}
			}
			if (flag)
				System.out.println("Found Stable State");
			
		}
	}
	
}
