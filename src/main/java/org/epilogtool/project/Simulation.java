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
 * Initializes and implements the simulation on epilog.
 * 
 * @author Pedro Monteiro and Pedro Varela
 * 
 */
public class Simulation {
	private Epithelium epithelium;
	private EpitheliumGrid neighbouringEpi;  //Epithelium Grid used to see neighbouring relationship (imp for sigma)
	private List<EpitheliumGrid> gridHistory;
	private List<String> gridHashHistory;
	
	private boolean allCellsCalledToUpdate;
	
	public  Map<Tuple2D<Integer>, byte[]> schuffledInstances;
	public  Map<Tuple2D<Integer>, byte[]> runningSchuffledInstances;
	public Map<Tuple2D, Double> exponentialInstances;

	
	private boolean stable;
	private boolean hasCycle;
	// Perturbed models cache - avoids repeatedly computing perturbations at
	// each step
	private PriorityUpdater[][] updaterCache;

	/**
	 * Initializes the simulation. It is called after creating and epithelium.
	 * Creates a list of EpitheliumGrids, to allow the user to travel to a
	 * previously calculated (and saved) epitheliumGrid. This list is
	 * initialized with the current EpitheliumGrid (the one defined in the
	 * initialConditions).
	 * 
	 * @param e
	 *            the epithelium the user is working with.
	 * 
	 */
	public Simulation(Epithelium e) {
		this.epithelium = e;
		this.neighbouringEpi = e.getEpitheliumGrid().clone();
		this.gridHistory = new ArrayList<EpitheliumGrid>();
		EpitheliumGrid firstGrid = this.epithelium.getEpitheliumGrid().clone();
		this.gridHistory.add(this.restrictGridWithPerturbations(firstGrid));
		this.gridHashHistory = new ArrayList<String>();
		this.gridHashHistory.add(firstGrid.hashGrid());
		this.stable = false;
		this.hasCycle = false;
		this.buildPriorityUpdaterCache();
		
		this.schuffledInstances = schuffledInstances;
		this.runningSchuffledInstances = runningSchuffledInstances;
		this.exponentialInstances = null;
		
		this.allCellsCalledToUpdate = true;
	}

	private EpitheliumGrid restrictGridWithPerturbations(EpitheliumGrid grid) {
		for (int y = 0; y < grid.getY(); y++) {
			for (int x = 0; x < grid.getX(); x++) {
				grid.restrictCellWithPerturbation(x, y);
			}
		}
		return grid;
	}

	private void buildPriorityUpdaterCache() {
		this.updaterCache = new PriorityUpdater[this.getCurrentGrid().getX()][this
				.getCurrentGrid().getY()];
		Map<LogicalModel, Map<AbstractPerturbation, PriorityUpdater>> tmpMap = new HashMap<LogicalModel, Map<AbstractPerturbation, PriorityUpdater>>();
		for (int y = 0; y < this.getCurrentGrid().getY(); y++) {
			for (int x = 0; x < this.getCurrentGrid().getX(); x++) {
				if (this.getCurrentGrid().hasEmptyModel(x, y)) {
					continue;
				}
				LogicalModel m = this.getCurrentGrid().getModel(x, y);
				AbstractPerturbation ap = this.getCurrentGrid()
						.getPerturbation(x, y);
				if (!tmpMap.containsKey(m))
					tmpMap.put(
							m,
							new HashMap<AbstractPerturbation, PriorityUpdater>());
				if (!tmpMap.get(m).containsKey(ap)) {
					// Apply model perturbation
					LogicalModel perturb = (ap == null) ? m : ap.apply(m);
					// Get Priority classes
					PriorityClasses pcs = this.epithelium.getPriorityClasses(m)
							.getPriorities();
					PriorityUpdater updater = new PriorityUpdater(perturb, pcs);
					tmpMap.get(m).put(ap, updater);
				}
				this.updaterCache[x][y] = tmpMap.get(m).get(ap);
			}
		}
	}

	/**
	 * This function retrieves the next step in the simulation. The first step
	 * in this
	 */
	public EpitheliumGrid nextStepGrid() {
		EpitheliumGrid currGrid = this.getCurrentGrid();
		if (this.stable) {
			return currGrid;
		}

		this.generateNeighboursEpithelium(); // updates the neighbouringEpi
		EpitheliumGrid currNeighboursGrid = this.neighbouringEpi;

		EpitheliumGrid nextGrid = currGrid.clone();

		Set<ComponentPair> sIntegComponentPairs = this.epithelium
				.getIntegrationComponentPairs();

		IntegrationFunctionEvaluation evaluator = new IntegrationFunctionEvaluation(
				currNeighboursGrid, this.epithelium.getProjectFeatures());

		// Gets the set of cells that can be updated
		// And builds the default next grid (= current grid)
		HashMap<Tuple2D<Integer>, byte[]> cells2update = new HashMap<Tuple2D<Integer>, byte[]>();
		Stack<Tuple2D<Integer>> keys = new Stack<Tuple2D<Integer>>();
		Stack<Tuple2D<Integer>> changedKeys = new Stack<Tuple2D<Integer>>();

		for (int y = 0; y < currGrid.getY(); y++) {
			for (int x = 0; x < currGrid.getX(); x++) {
				if (currGrid.hasEmptyModel(x, y)) {
					continue;
				}
				byte[] currState = currGrid.getCellState(x, y);
				// Default cell next state is same as current
				nextGrid.setCellState(x, y, currState.clone());
				// Compute next state
				byte[] nextState = this.nextCellValue(x, y, currGrid,
						evaluator, sIntegComponentPairs);
				// If the cell state changed then add it to the pool
				Tuple2D<Integer> key = new Tuple2D<Integer>(x, y);
				cells2update.put(key, nextState);
				keys.add(key);
				if (nextState == null || (!Arrays.equals(currState, nextState))) 
				changedKeys.add(key);
			}
		}
		
		if (changedKeys.size()==0){
			this.stable = true;
			return currGrid;
		}
		if (!allCellsCalledToUpdate)
			keys = changedKeys;
		
		if (keys.size() <= 0) {
			this.stable = true;
			return currGrid;
		}
		else{

			nextGrid = updateGrid(this.epithelium.getUpdateSchemeInter().getUpdateMode(),keys,nextGrid,cells2update);
		}

//		System.out.println(nextGrid);
		this.gridHistory.add(nextGrid);
		
		this.gridHashHistory.add(nextGrid.hashGrid());
		return nextGrid;
	}

	private EpitheliumGrid updateGrid(String updateMode, Stack<Tuple2D<Integer>> keys, EpitheliumGrid nextGrid, HashMap<Tuple2D<Integer>, byte[]> cells2update) {
		// TODO Auto-generated method stub
	

		if (updateMode.equals("Synchronous"))
			nextGrid = updateModeSynchronous(keys, nextGrid, cells2update);
		else if (updateMode.equals("Asynchronous: Random independent"))
			nextGrid= updateModeRandomIndependent(keys, nextGrid, cells2update);
		else if (updateMode.equals("Asynchronous: Random Order"))
			nextGrid = updateModeRandomOrder(keys, nextGrid, cells2update);
		else if (updateMode.equals("Asynchronous: Cyclic Order"))
			nextGrid= updateModeCyclicOrder(keys, nextGrid, cells2update);
		else if (updateMode.equals("Asynchronous: Exponential clocked")){
			nextGrid= updateModeExponentialClocked(keys, nextGrid, cells2update);
		}
		return nextGrid;
		
	}

	private EpitheliumGrid updateModeExponentialClocked(Stack<Tuple2D<Integer>> keys, EpitheliumGrid nextGrid,
			HashMap<Tuple2D<Integer>, byte[]> cells2update) {
		// TODO Auto-generated method stub
		
		int lambda = 1;
		Random randomno = new Random();

		int maxNumberCells = this.getCurrentGrid().getX()*this.getCurrentGrid().getY();
		//Initializes the exponential array for all cells of the grid. The array of dimension maxNumberCells,
		// has a random exponential value for each cell
		if (this.exponentialInstances == null){
			this.exponentialInstances = new HashMap<Tuple2D,Double>();
			for (int x = 0; x<this.getCurrentGrid().getX();x++){
				for (int y=0; y<this.getCurrentGrid().getY();y++){
				Tuple2D<Integer> n= new Tuple2D(x,y);
				double aux = Math.log(1-randomno.nextDouble())/(-lambda);
				this.exponentialInstances.put(n, aux);
			}}
		}
		
		
//		System.out.println("The system is performing an evaluation over an intial number of : "+ keys.size()+ " cells");
		//for each cell in keys; how many are we going to call to update?
		
		float alphaProb = epithelium.getUpdateSchemeInter().getAlpha();
				
		int numberCellsCalledToUpdate = (int) Math.floor(alphaProb * keys.size());

		
		if (numberCellsCalledToUpdate ==0)
			numberCellsCalledToUpdate =1;
		
//		System.out.println("of which only : "+ numberCellsCalledToUpdate+ " are going to be updated!");

		Map<Tuple2D, Double> updatableCellsList = UpdateMode.findMinIdx(this.exponentialInstances, numberCellsCalledToUpdate,keys) ;

		for (Tuple2D<Integer> key :updatableCellsList.keySet()) {
		
			nextGrid.setCellState(key.getX(), key.getY(),
					cells2update.get(key));
			double aux = Math.log(1-randomno.nextDouble())/(-lambda);
			double value = this.exponentialInstances.get(key)+aux;
			this.exponentialInstances.put(key, value);
		}
		return nextGrid;
	}

	private EpitheliumGrid updateModeCyclicOrder(Stack<Tuple2D<Integer>> keys, EpitheliumGrid nextGrid,
			HashMap<Tuple2D<Integer>, byte[]> cells2update) {
		// TODO Auto-generated method stub
		return nextGrid;
	}

	private EpitheliumGrid updateModeRandomOrder(Stack<Tuple2D<Integer>> keys, EpitheliumGrid nextGrid,
			HashMap<Tuple2D<Integer>, byte[]> cells2update) {
		// TODO Auto-generated method stub
		
		float alphaProb = epithelium.getUpdateSchemeInter().getAlpha();
		int numberCellsCalledToUpdate = (int) Math.floor(alphaProb * keys.size());
		if (numberCellsCalledToUpdate ==0)
			numberCellsCalledToUpdate =1;
		
		if (this.schuffledInstances == null){//Create the initial shuffled array of cells
			this.schuffledInstances = UpdateMode.shuffleAndSelect(cells2update
					, cells2update.size());
//			System.out.println("Number of cells to be updated: "+this.schuffledInstances.size());
		}
		
		List<Tuple2D> finalListOfCells = new ArrayList<Tuple2D>();
		int index = 0;
		for (Tuple2D tuple: this.schuffledInstances.keySet()){
			if (index >=numberCellsCalledToUpdate) break;
			if (keys.contains(tuple)){
				finalListOfCells.add(tuple);
				index = index +1;
				keys.remove(tuple);
			}
		}
		
//		System.out.println
		
//			for (int n = 0; n<numberCells; n++)
//				if (updatableCells.contains(this.runningSchuffledInstances.get(0))){
//					updatableCellsList.add(this.runningSchuffledInstances.get(0));
//					this.runningSchuffledInstances.remove(0);
//					if (this.runningSchuffledInstances.size()==0){
//						this.schuffledInstances = utils.shuffleAndSelect(currentGlobalState
//								.getNumberInstances(), maxNumberCells);
//						for (int k =0; k<this.schuffledInstances.size();k++)
//							this.runningSchuffledInstances.add(k, this.schuffledInstances.get(k));
//					}
//
//				}	
//				else{
//					this.runningSchuffledInstances.remove(0);
//					if (this.runningSchuffledInstances.size()==0){
//						this.schuffledInstances = utils.shuffleAndSelect(currentGlobalState
//								.getNumberInstances(), maxNumberCells);
//						for (int k =0; k<this.schuffledInstances.size();k++)
//							this.runningSchuffledInstances.add(k, this.schuffledInstances.get(k));
//					}
//
//					n = n-1;
//
//
//				}}
//		else{
//			numberCells = (int) Math.ceil(this.alpha * mainFrame.simulation.currentGlobalState.getNumberInstances()/100);
//			if (numberCells ==0)
//				numberCells = 1;
//
//			updatableCellsList = new ArrayList<Integer>();
//			for (int n = 0; n<numberCells; n++){
//				updatableCellsList.add(this.runningSchuffledInstances.get(0));
//				this.runningSchuffledInstances.remove(0);
//
//				if (this.runningSchuffledInstances.size()==0){
//					this.schuffledInstances = utils.shuffleAndSelect(currentGlobalState
//							.getNumberInstances(), maxNumberCells);
//					for (int k =0; k<this.schuffledInstances.size();k++)
//						this.runningSchuffledInstances.add(k, this.schuffledInstances.get(k));
//				}
//			}}
//
//		nextGlobalState = globalModel.getNextState(currentGlobalState,updatableCellsList);
//		this.cellIteration = this.cellIteration + numberCells;
//
//	}
		
		
		
		return nextGrid;
	}

	private EpitheliumGrid updateModeRandomIndependent(Stack<Tuple2D<Integer>> keys, EpitheliumGrid nextGrid,
			HashMap<Tuple2D<Integer>, byte[]> cells2update) {
		// TODO IMPROVE
		
		// Randomize the order of cells to update
		Collections.shuffle(keys, RandomFactory.getInstance()
				.getGenerator());
		
		// Inter-cellular alpha-asynchronism
		float alphaProb = epithelium.getUpdateSchemeInter().getAlpha();
		
		boolean atleastone = false;
		// Creates the set of cells to update. If alpha is zero, then the atleast remains false, and only onde cell is called to update
		for (int i = 0; i < Math.floor(alphaProb * keys.size()); i++) {
			Tuple2D<Integer> key = keys.get(i);
			nextGrid.setCellState(key.getX(), key.getY(),
					cells2update.get(key));
			System.out.println("" + cells2update.get(key));
			atleastone = true;
		}
		if (!atleastone && !keys.isEmpty()) {
			// Updates at least one (asynchronous case: alpha=0.0)
			Tuple2D<Integer> key = keys.pop();
			nextGrid.setCellState(key.getX(), key.getY(),
					cells2update.get(key));
		}
		return nextGrid;
	}

	private EpitheliumGrid updateModeSynchronous(Stack<Tuple2D<Integer>> keys, EpitheliumGrid nextGrid,
			HashMap<Tuple2D<Integer>, byte[]> cells2update) {
				
		// TODO Auto-generated method stub
				for (int i = 0; i < keys.size(); i++) {
					Tuple2D<Integer> key = keys.get(i);
					nextGrid.setCellState(key.getX(), key.getY(),
							cells2update.get(key));
				}
				return nextGrid;
		
	}

	public boolean hasCycle() {
		if (!this.hasCycle) {
			Set<String> sStateHistory = new HashSet<String>(
					this.gridHashHistory);
			this.hasCycle = (sStateHistory.size() < this.gridHashHistory.size());
		}
		return this.hasCycle;
	}

	private byte[] nextCellValue(int x, int y, EpitheliumGrid currGrid,
			IntegrationFunctionEvaluation evaluator,
			Set<ComponentPair> sIntegComponentPairs) {
		byte[] currState = currGrid.getCellState(x, y).clone();

		PriorityUpdater updater = this.updaterCache[x][y];
		LogicalModel m = this.epithelium.getEpitheliumGrid().getModel(x, y);

		// 2. Update integration components
		for (NodeInfo node : m.getNodeOrder()) {
			ComponentPair nodeCP = new ComponentPair(m, node);
			if (node.isInput() && sIntegComponentPairs.contains(nodeCP)) {
				List<IntegrationExpression> lExpressions = this.epithelium
						.getIntegrationFunctionsForComponent(nodeCP)
						.getComputedExpressions();
				byte target = 0;
				for (int i = 0; i < lExpressions.size(); i++) {
					if (evaluator.evaluate(x, y, lExpressions.get(i))) {
						target = (byte) (i + 1);
						break; // The lowest value being satisfied
					}
				}
				currState[m.getNodeOrder().indexOf(node)] = target;
			}
		}

		List<byte[]> succ = updater.getSuccessors(currState);
		if (succ == null) {
			return currState;
		} else if (succ.size() > 1) {
			// FIXME
			// throw new Exception("Argh");
		}
		return succ.get(0);
	}

	public boolean isStableAt(int i) {
		return (i >= this.gridHistory.size() && this.stable);
	}

	public boolean hasCycleAt(int i) {
		if (!(this.epithelium.getUpdateSchemeInter().getAlpha() == 1)) {
			return false;
		}
		List<String> tmpList = new ArrayList<String>(
				this.gridHashHistory.subList(0, i));
		Set<String> tmpSet = new HashSet<String>(tmpList);
		return !(tmpSet.size() == tmpList.size());
	}

	public EpitheliumGrid getGridAt(int i) {
		if (i < this.gridHistory.size()) {
			return this.gridHistory.get(i);
		}
		return this.nextStepGrid();
	}

	public EpitheliumGrid getCurrentGrid() {
		return gridHistory.get(gridHistory.size() - 1);
	}

	public Epithelium getEpithelium() {
		return this.epithelium;
	}

	private void generateNeighboursEpithelium() {
		// Creates an epithelium which is only visited to 'see' neighbours and
		// their states, especially when using the sigma, where the previous grids values are seen.

		EpitheliumGrid tmpNeighbourEpi = this.getGridAt(
				this.gridHistory.size() - 1).clone();

		Map<ComponentPair, Float> mSigmaAsync = this.epithelium
				.getUpdateSchemeInter().getCPSigmas();

		Map<LogicalModel, List<Tuple2D<Integer>>> mapModelPositions = tmpNeighbourEpi
				.getModelPositions();
		if (mSigmaAsync.size() == 0) {
			this.neighbouringEpi = tmpNeighbourEpi;
		} else {
			EpitheliumGrid delayGrid = this.gridHistory.get(0);
			if (this.gridHistory.size() >= 2) {
				delayGrid = this.gridHistory.get(this.gridHistory.size() - 2);
			}
			for (ComponentPair cp : mSigmaAsync.keySet()) {
				float sigma = mSigmaAsync.get(cp);
				if (sigma != EpitheliumUpdateSchemeInter.DEFAULT_SIGMA) {
					LogicalModel m = cp.getModel();
					String nodeID = cp.getNodeInfo().getNodeID();
					List<NodeInfo> modelNodes = m.getNodeOrder();
					int nodePosition = modelNodes.indexOf(cp.getNodeInfo());
					List<Tuple2D<Integer>> modelPositions = mapModelPositions
							.get(m);
					int selectedCells = (int) Math.ceil((1 - sigma)
							* modelPositions.size());
					Collections.shuffle(modelPositions, RandomFactory
							.getInstance().getGenerator());
					List<Tuple2D<Integer>> selectedModelPositions = modelPositions
							.subList(0, selectedCells);
					for (Tuple2D<Integer> tuple : selectedModelPositions) {
						byte[] delayState = delayGrid.getCellState(
								tuple.getX(), tuple.getY());
						tmpNeighbourEpi.setCellComponentValue(tuple.getX(),
								tuple.getY(), nodeID,
								(byte) delayState[nodePosition]);
					}
				}
			}
			this.neighbouringEpi = tmpNeighbourEpi;
		}
	}
}
