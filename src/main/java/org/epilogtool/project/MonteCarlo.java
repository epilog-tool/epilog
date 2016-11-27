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
	private boolean randomIniC;
	
	private int stableStatesFound;
	private List<EpitheliumGrid> stableStates;
	private Map<EpitheliumGrid,Integer> stablestate2iteration;
	
	
	public MonteCarlo(Epithelium e){
	
		
	this.epithelium = e;
	this.numberRuns = numberRuns;
	this.maxNumberIterations = maxNumberIterations;
	this.randomIniC  = randomIniC;
	this.stablestate2iteration = new HashMap<EpitheliumGrid,Integer>();
	this.stableStates = new ArrayList<EpitheliumGrid>();
	
	}
	
	public Map<EpitheliumGrid,Integer> getStableState2Iteration(){
		return this.stablestate2iteration;
	}
	
	public List<EpitheliumGrid> getStableStates(){
		return this.stableStates;
	}
	
	public boolean getMonteCarloInitialConditions(){
		return this.randomIniC;
	}
	
	public void setMonteCarloInitialConditions(boolean mcInic){
		this.randomIniC = mcInic;
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

	public void run(Epithelium epi ) {

		this.epithelium = epi;
//		System.out.println("The montecarlo simulation is running with the paramenters: ");
//		System.out.println("MAxRuns: " + this.numberRuns);
//		System.out.println("MaxIter: " + this.maxNumberIterations);
		for (int i = 0; i<this.numberRuns; i++){
			Epithelium clonedEpi = this.epithelium.clone();
			if (randomIniC)
				clonedEpi.setRandomInitialConditions();
			Simulation sim =new Simulation(clonedEpi);
			
			boolean flag = false;
//			System.out.println("Running Simulation: "+i);
			for (int j=0; j<this.maxNumberIterations;j++){
				EpitheliumGrid nextGrid = sim.getGridAt(j + 1);
				if (sim.isStableAt(j+1)){
					stableStates.add(nextGrid);
					stablestate2iteration.put(nextGrid, j);
					flag = true;
					break;	

				}
			}
			if (flag)
				System.out.println("Found Stable State");
			else{
				System.out.println("Missed a  Stable State");
			}
			
		}
		System.out.println("MonteCarlo OVER");
	}
	
	
}
