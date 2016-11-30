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

import javax.swing.JLabel;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;
import org.colomoto.logicalmodel.perturbation.AbstractPerturbation;
import org.colomoto.logicalmodel.tool.simulation.updater.PriorityClasses;
import org.colomoto.logicalmodel.tool.simulation.updater.PriorityUpdater;
import org.epilogtool.common.RandomFactory;
import org.epilogtool.common.Tuple2D;
import org.epilogtool.common.Tuple3D;
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
	private Map<EpitheliumGrid,Integer> repeatedStableState;
	
	
	
	public MonteCarlo(Epithelium e){
	
		
	this.epithelium = e;
	this.numberRuns = numberRuns;
	this.maxNumberIterations = maxNumberIterations;
	this.randomIniC  = randomIniC;

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
		
		this.stablestate2iteration = new HashMap<EpitheliumGrid,Integer>();
		this.repeatedStableState = new HashMap<EpitheliumGrid,Integer>();
		this.stableStates = new ArrayList<EpitheliumGrid>();
		
		this.stableStatesFound = 0;

		
		for (int i = 0; i<this.numberRuns; i++){
			Epithelium clonedEpi = this.epithelium.clone();
			
			if (randomIniC)
				clonedEpi.setRandomInitialConditions();
		
			Simulation sim =new Simulation(clonedEpi);
			

			for (int indexIteration=0; indexIteration<this.maxNumberIterations;indexIteration++){
				
				EpitheliumGrid nextGrid = sim.getGridAt(indexIteration + 1);
				
//				System.out.println("  ");
//				System.out.println("Run: " + i);
//				System.out.println(sim.getGridAt(indexIteration));
//				System.out.println(nextGrid);
				
				if (sim.isStableAt(indexIteration+1)){
					if (compareStableStates(nextGrid)){
						stableStates.add(nextGrid);
						stablestate2iteration.put(nextGrid, indexIteration);
					}
					break;	

				}
			}
			
		}
		
		System.out.println("MonteCarlo OVER");
	}


	private boolean compareStableStates(EpitheliumGrid stableState){
		
		if (this.stableStates.size()==0)
			return true;
		else{
			for (EpitheliumGrid sState: this.stableStates){
				if (sState.equals(stableState)){
					System.out.println("found the same state");
					if (this.repeatedStableState.containsKey(sState))
						this.repeatedStableState.put(sState,this.repeatedStableState.get(sState)+1);
					return false;
				}
				
				
				
//				for (int x = 0; x < sState.getX(); x++) {
//					for (int y = 0; y < sState.getY(); y++) {
//						for (NodeInfo node: sState.getModel(x, y).getNodeOrder()){
//							if (sState.getCellValue(x, y, node.getNodeID())!=stableState.getCellValue(x, y, node.getNodeID())){
//								System.out.println(x + " "+ y);
//								return true;
//						}}}}
			}
			return true;
		}
	}

	
	public void createCumulative() {
		// TODO Auto-generated method stub
		
		Map<Tuple3D,Integer> cellNode2Count = new HashMap<Tuple3D,Integer>();
		
		if (this.stableStates.size()>0)
			for (EpitheliumGrid stableState: this.stableStates){
				for (int x = 0; x < stableState.getX(); x++) {
					for (int y = 0; y < stableState.getY(); y++) {
						for (NodeInfo node: stableState.getModel(x,y).getNodeOrder()){
							Tuple3D cellNode = new Tuple3D(x,y,node.getNodeID());
							if (!cellNode2Count.keySet().contains(cellNode)){
								cellNode2Count.put(cellNode, 1);
							}
							else{
							int count = stableState.getCellValue(x, y, node.getNodeID());
							cellNode2Count.put(cellNode, cellNode2Count.get(cellNode)+count);
							}
							
						}
					}
					
					}
				
			}
		System.out.println(cellNode2Count);
			
	}

	
}
