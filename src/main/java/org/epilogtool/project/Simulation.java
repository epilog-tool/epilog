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

import org.colomoto.biolqm.LogicalModel;
import org.colomoto.biolqm.NodeInfo;
import org.colomoto.biolqm.modifier.perturbation.LogicalModelPerturbation;
import org.colomoto.biolqm.tool.simulation.grouping.ModelGrouping;
import org.colomoto.biolqm.tool.simulation.multiplesuccessor.PriorityUpdater;
import org.epilogtool.common.EnumRandomSeed;
import org.epilogtool.common.RandCentral;
import org.epilogtool.common.Tuple2D;
import org.epilogtool.core.Epithelium;
import org.epilogtool.core.EpitheliumGrid;
import org.epilogtool.core.UpdateCells;
import org.epilogtool.integration.IFEvaluation;
import org.epilogtool.integration.IntegrationFunctionExpression;

/**
 * Initializes and implements the simulation on epilog.
 * 
 * @author Pedro T. Monteiro
 * @author Pedro L. Varela
 * @author Camila V. Ramos
 */
public class Simulation {
	private Epithelium epithelium;
	private List<EpitheliumGrid> gridHistory;
	private List<String> gridHashHistory;
	private Random random;

	private boolean stable;
	private boolean hasCycle;
	// Perturbed models cache - avoids repeatedly computing perturbations at
	// each step
	private Map<LogicalModel, Map<LogicalModelPerturbation, PriorityUpdater>> updaterCache;

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
		if (this.epithelium.getUpdateSchemeInter().getRandomSeedType().equals(EnumRandomSeed.RANDOM)) {
			this.random = RandCentral.getInstance().getNewGenerator();
		} else {
			this.random = RandCentral.getInstance()
					.getNewGenerator(this.epithelium.getUpdateSchemeInter().getRandomSeed());
		}
		// Grid History
		this.gridHistory = new ArrayList<EpitheliumGrid>();
		EpitheliumGrid firstGrid = this.epithelium.getEpitheliumGrid().clone();
		firstGrid.updateNodeValueCounts();
		firstGrid.restrictGridWithPerturbations();
		this.gridHistory.add(firstGrid);
		// Grid Hash History
		this.gridHashHistory = new ArrayList<String>();
		this.gridHashHistory.add(firstGrid.hashGrid());
		this.stable = false;
		this.hasCycle = false;
		this.buildPriorityUpdaterCache();
	}

	private void buildPriorityUpdaterCache() {
		// updaterCache stores the PriorityUpdater to avoid unnecessary
		// computing
		this.updaterCache = new HashMap<LogicalModel, Map<LogicalModelPerturbation, PriorityUpdater>>();
		for (int y = 0; y < this.getCurrentGrid().getY(); y++) {
			for (int x = 0; x < this.getCurrentGrid().getX(); x++) {
				if (this.getCurrentGrid().isEmptyCell(x, y)) {
					continue;
				}
				LogicalModel m = this.getCurrentGrid().getModel(x, y);
				LogicalModelPerturbation ap = this.epithelium.getEpitheliumGrid().getPerturbation(x, y);
				if (!this.updaterCache.containsKey(m)) {
					this.updaterCache.put(m, new HashMap<LogicalModelPerturbation, PriorityUpdater>());
				}
				if (!this.updaterCache.get(m).containsKey(ap)) {
					// Apply model perturbation
					LogicalModel perturb = (ap == null) ? m : ap.apply(m);
					// Get Priority classes
					ModelGrouping mpc = this.epithelium.getPriorityClasses(m);
					// FIXME PriorityUpdater does not receive the "modified" model anymore
					mpc = mpc.cloneWithModel(perturb);
					PriorityUpdater updater = new PriorityUpdater(mpc);
					this.updaterCache.get(m).put(ap, updater);
				}
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

		EpitheliumGrid nextGrid = currGrid.clone();
		Set<NodeInfo> sNodes = this.epithelium.getIntegrationNodes();
		IFEvaluation evaluator = new IFEvaluation(nextGrid,this.epithelium);

		// Gets the set of cells that can be updated
		// And builds the default next grid (= current grid)
		HashMap<Tuple2D<Integer>, byte[]> cells2update = new HashMap<Tuple2D<Integer>, byte[]>();
		List<Tuple2D<Integer>> keys = new ArrayList<Tuple2D<Integer>>();
		List<Tuple2D<Integer>> changedKeys = new ArrayList<Tuple2D<Integer>>();

		for (int y = 0; y < currGrid.getY(); y++) {
			for (int x = 0; x < currGrid.getX(); x++) {
				if (currGrid.isEmptyCell(x, y)) {
					continue;
				}
				byte[] currState = currGrid.getCellState(x, y);

				// Compute next state
				byte[] nextState = this.nextCellValue(x, y, currGrid, evaluator, sNodes);

				// If the cell state changed then add it to the pool
				Tuple2D<Integer> key = new Tuple2D<Integer>(x, y);
				cells2update.put(key, nextState);
				keys.add(key);
				if (!Arrays.equals(currState, nextState)) {
					changedKeys.add(key);
				}
			}
		}

		if (this.epithelium.getUpdateSchemeInter().getUpdateCells().equals(UpdateCells.UPDATABLECELLS)) {
			keys = changedKeys;
		}

		// Internal updates
		if (changedKeys.size() > 0) {
			// Inter-cellular alpha-asynchronism
			float alphaProb = this.epithelium.getUpdateSchemeInter().getAlpha();
			int nToChange = (int) Math.floor(alphaProb * keys.size());
			if (nToChange == 0) {
				nToChange = 1;
			}
			// Create the initial shuffled array of cells
			Collections.shuffle(keys, this.random);

			for (int i = 0; i < nToChange; i++) {
				// Update cell state
				nextGrid.setCellState(keys.get(i).getX(), keys.get(i).getY(), cells2update.get(keys.get(i)));
			}
		}

		if (changedKeys.isEmpty()) {
			this.stable = true;
			return currGrid;
		}

		this.gridHistory.add(nextGrid);
		if (this.gridHashHistory != null) {
			this.gridHashHistory.add(nextGrid.hashGrid());
		}
		return nextGrid;
	}

	public boolean hasCycle() {
		if (!this.hasCycle) {
			Set<String> sStateHistory = new HashSet<String>(this.gridHashHistory);
			this.hasCycle = (sStateHistory.size() < this.gridHashHistory.size());
		}
		return this.hasCycle;
	}

	private byte[] nextCellValue(int x, int y, EpitheliumGrid currGrid, IFEvaluation evaluator,
			Set<NodeInfo> sNodeInfos) {
		byte[] currState = currGrid.getCellState(x, y).clone();
		LogicalModel m = currGrid.getModel(x, y);
		LogicalModelPerturbation ap = currGrid.getPerturbation(x, y);
		PriorityUpdater updater = this.updaterCache.get(m).get(ap);

		// 2. Update integration components
		for (NodeInfo node :this.epithelium.getIntegrationNodes()) {
			if (m.getComponents().contains(node)) {
			if (node.isInput() && sNodeInfos.contains(node)) {
				List<IntegrationFunctionExpression> lExpressions = this.epithelium
						.getIntegrationFunctionsForComponent(node).getComputedExpressions();
				byte target = 0;
				for (int i = 0; i < lExpressions.size(); i++) {
					if (evaluator.evaluate(x, y, lExpressions.get(i))) {
						target = (byte) (i + 1);
						break; // The lowest value being satisfied
					}
				}
				currState[m.getComponents().indexOf(node)] = target;
			}
		}}
		List<byte[]> succ = updater.getSuccessors(currState);
		if (succ == null || succ.size() == 0) {
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

	private boolean isSynchronous() {
		return this.epithelium.getUpdateSchemeInter().getAlpha() == 1.0;
	}

	public int getTerminalCycleLen() {
		if (this.isSynchronous()) {
			String sGrid = this.gridHashHistory.get(this.gridHashHistory.size() - 1);
			// Tmp
			List<String> lTmp = new ArrayList<String>(this.gridHashHistory);
			lTmp.remove(this.gridHashHistory.size()-1);
			int posBeforeLast = lTmp.lastIndexOf(sGrid);
			
			if (posBeforeLast > 0) {
				return (this.gridHashHistory.size() - 1) - posBeforeLast;
			}
		}
		return -1;
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

	public List<Map<String, Float>> getCell2Percentage() {
		List<Map<String, Float>> percentageHistory = new ArrayList<Map<String, Float>>();
		int index = 0;
		for (EpitheliumGrid grid : this.gridHistory) {
			percentageHistory.add(new HashMap<String, Float>());
			for (LogicalModel model : grid.getModelSet()) {
				for (NodeInfo node : model.getComponents()) {
					for (byte i = 1; i <= node.getMax(); i++) {
						String nodeID = node.getNodeID() + " " + i;
						percentageHistory.get(index).put(nodeID, grid.getPercentage(node.getNodeID(), i));
					}
				}
			}
			index = index + 1;
		}
		return percentageHistory;
	}
}
