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
import org.epilogtool.common.Tuple2D;
import org.epilogtool.core.EmptyModel;
import org.epilogtool.core.Epithelium;
import org.epilogtool.core.EpitheliumGrid;
import org.epilogtool.core.EpitheliumUpdateSchemeInter;
import org.epilogtool.core.cellDynamics.CellTrigger;
import org.epilogtool.core.cellDynamics.EpitheliumTopology;
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
	private EpitheliumTopology epiTopology;
	private List<EpitheliumGrid> gridHistory;
	private List<String> gridHashHistory;
	private boolean stable;
	private boolean hasCycle;
	// Perturbed models cache - avoids repeatedly computing perturbations at
	// each step
	private PriorityUpdater[][] updaterCache;
	private Random randomGenerator;

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
		this.epithelium = e.clone();
		this.epiTopology = new EpitheliumTopology(this.epithelium);
		this.gridHistory = new ArrayList<EpitheliumGrid>();
		this.gridHistory.add(this.epithelium.getEpitheliumGrid());
		this.gridHashHistory = new ArrayList<String>();
		this.gridHashHistory
				.add(this.epithelium.getEpitheliumGrid().hashGrid());
		this.stable = false;
		this.hasCycle = false;
		this.randomGenerator = new Random();
		this.buildPriorityUpdaterCache();
	}

	private void buildPriorityUpdaterCache() {
		this.updaterCache = new PriorityUpdater[this.getCurrentGrid().getX()][this
				.getCurrentGrid().getY()];
		Map<LogicalModel, Map<AbstractPerturbation, PriorityUpdater>> tmpMap = new HashMap<LogicalModel, Map<AbstractPerturbation, PriorityUpdater>>();
		for (int y = 0; y < this.getCurrentGrid().getY(); y++) {
			for (int x = 0; x < this.getCurrentGrid().getX(); x++) {
				if (this.getCurrentGrid().isEmptyCell(x, y)) {
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
	
	private void updateUpdaterCache(EpitheliumGrid workingGrid, List<Tuple2D<Integer>> path) {
		for (int index = 1; index < path.size(); index ++) {
			Tuple2D<Integer> originalPos = path.get(index);
			Tuple2D<Integer> clonedPos = path.get(index-1);
			LogicalModel m = workingGrid.getModel(originalPos.getX(), originalPos.getY());
			if (EmptyModel.getInstance().isEmptyModel(m)) continue;
			AbstractPerturbation ap = workingGrid.getPerturbation(originalPos.getX(), originalPos.getY());
			LogicalModel perturb = (ap == null) ? m : ap.apply(m);
			PriorityClasses pcs = this.epithelium.getPriorityClasses(m).getPriorities();
			PriorityUpdater updater = new PriorityUpdater(perturb, pcs);
			this.updaterCache[clonedPos.getX()][clonedPos.getY()] = updater;
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
		
		boolean updates = false;
		boolean dynamics = false;

		EpitheliumGrid neighboursGrid = this.getNeighboursGrid();
		EpitheliumGrid nextGrid = currGrid.clone();

		Set<ComponentPair> sIntegComponentPairs = this.epithelium
				.getIntegrationComponentPairs();
		IntegrationFunctionEvaluation evaluator = new IntegrationFunctionEvaluation(
				neighboursGrid, this.epithelium.getProjectFeatures());

		// Gets the set of cells that can be updated
		// And builds the default next grid (= current grid)
		HashMap<Tuple2D<Integer>, byte[]> cells2update = new HashMap<Tuple2D<Integer>, byte[]>();
		List<Tuple2D<Integer>> keys = new ArrayList<Tuple2D<Integer>>();
		List<Tuple2D<Integer>> cells2divide = new ArrayList<Tuple2D<Integer>>();
		
		for (int y = 0; y < currGrid.getY(); y++) {
			for (int x = 0; x < currGrid.getX(); x++) {
				if (currGrid.isEmptyCell(x, y)) { 
					continue;
				}
				byte[] currState = currGrid.getCellState(x, y);
				// Default cell next state is same as current
				nextGrid.setCellState(x, y, currState.clone());
				// Compute next state
				byte[] nextState = this.nextCellValue(x, y, currGrid,
						evaluator, sIntegComponentPairs);
				// If the cell state changed then add it to the pool
				if (nextState == null || !Arrays.equals(currState, nextState)) {
					Tuple2D<Integer> key = new Tuple2D<Integer>(x, y);
					cells2update.put(key, nextState);
					keys.add(key);
				}
			}
		}
		

		// Inter-cellular alpha-asynchronism
		//Update Logical Model States
		float alphaProb = epithelium.getUpdateSchemeInter().getAlpha();
		if (keys.size() > 0) {
			updates = true;
			boolean atleastone = false;
			int updateCounter = (int) Math.floor(alphaProb * keys.size());
			for (int i = 0; i < updateCounter; i++) {
				Tuple2D<Integer> key = keys.get(randomGenerator.nextInt(keys.size()));
				keys.remove(key);
				nextGrid.setCellState(key.getX(), key.getY(),
						cells2update.get(key));
				CellTrigger nextCellStatus = this.epithelium.getCellStatusManager()
						.getCellStatus(currGrid
								.getModel(key.getX(), key.getY()), cells2update.get(key));
				nextGrid.setCellTrigger(key.getX(), key.getY(), nextCellStatus);
				atleastone = true;
			}
			if (!atleastone && !keys.isEmpty()) {
				// Updates at least one (asynchronous case: alpha=0.0)
				Tuple2D<Integer> key = keys.get(randomGenerator.nextInt(keys.size()));
				keys.remove(key);
				nextGrid.setCellState(key.getX(), key.getY(), cells2update.get(key));
				CellTrigger nextCellStatus = this.epithelium.getCellStatusManager()
						.getCellStatus(currGrid
								.getModel(key.getX(), key.getY()), cells2update.get(key));
				nextGrid.setCellTrigger(key.getX(), key.getY(), nextCellStatus);
			}
		} 
		
		//update Division events
		int emptyModelNumber = nextGrid.emptyModelNumber();
		
		for (int x = 0; x < nextGrid.getX(); x ++) {
			for (int y = 0; y < nextGrid.getY(); y ++) {
				if (currGrid.isEmptyCell(x, y)) continue;
				if (nextGrid.getCellTrigger(x, y).equals(CellTrigger.PROLIFERATION)
						&& currGrid.getCellTrigger(x, y).equals(CellTrigger.PROLIFERATION)) {
					cells2divide.add(new Tuple2D<Integer>(x, y));
				}
			}
		}
		if (cells2divide.size() == 0) {
			dynamics = false;
		} 
		//if (!updates && !dynamics) {
		//	this.stable = true;
		//	return currGrid;
		//}
		if (cells2divide.size() > 0) {
			while (emptyModelNumber > 0) {
				Tuple2D<Integer> currPosition = cells2divide.get(randomGenerator.nextInt(cells2divide.size()));
				cells2divide.remove(currPosition);
				List<Tuple2D<Integer>> path = this.epiTopology.divisionPath(currPosition.getX(), currPosition.getY());
				Tuple2D<Integer> motherCell = path.get(path.size()-1).clone();
				Tuple2D<Integer> daughterCell = path.get(path.size()-2).clone();
				byte[] initialState = nextGrid.getCellInitialState(motherCell.getX(), motherCell.getY());
				Tuple2D<Integer> exteriorPos = path.get(0);
				nextGrid.shiftCells(path);
				this.epiTopology.updatePopulationTopology(exteriorPos.getX(), exteriorPos.getY(), (byte) 1);
				this.updateUpdaterCache(nextGrid, path);
				nextGrid.setCellState(motherCell.getX(), motherCell.getY(), initialState);
				nextGrid.setCellState(daughterCell.getX(), daughterCell.getY(), initialState);
				emptyModelNumber -=1;
				if (cells2divide.size()==0) break;
			}
		}
		this.gridHistory.add(nextGrid);
		this.gridHashHistory.add(nextGrid.hashGrid());
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
		byte[] currState = currGrid.getCellState(x, y);

		PriorityUpdater updater = this.updaterCache[x][y];
		LogicalModel m = currGrid.getModel(x, y);
		
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
		if (!(this.epithelium.getUpdateSchemeInter().getAlpha()==1)) {
			return false;
		}
		List<String> tmpList = new ArrayList<String>(this.gridHashHistory.subList(0, i));
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

	private EpitheliumGrid getNeighboursGrid() {
		// Creates an epithelium which is only visited to 'see' neighbours and
		// their states

		EpitheliumGrid neighbourEpi = this.getCurrentGrid().clone();

		Map<ComponentPair, Float> mSigmaAsync = this.epithelium
				.getUpdateSchemeInter().getCPSigmas();

		Map<LogicalModel, List<Tuple2D<Integer>>> mapModelPositions = neighbourEpi
				.getModelPositions();
		if (mSigmaAsync.size() == 0) {
			return neighbourEpi;
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
					Collections.shuffle(modelPositions,
							new Random(Double.doubleToLongBits(Math.random())));
					List<Tuple2D<Integer>> selectedModelPositions = modelPositions
							.subList(0, selectedCells);
					for (Tuple2D<Integer> tuple : selectedModelPositions) {
						byte[] delayState = delayGrid.getCellState(
								tuple.getX(), tuple.getY());
						neighbourEpi.setCellComponentValue(tuple.getX(),
								tuple.getY(), nodeID,
								(byte) delayState[nodePosition]);
					}
				}
			}
			return neighbourEpi;
		}
	}
}
