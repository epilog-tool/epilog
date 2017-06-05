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

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;
import org.colomoto.logicalmodel.perturbation.AbstractPerturbation;
import org.colomoto.logicalmodel.tool.simulation.updater.PriorityClasses;
import org.colomoto.logicalmodel.tool.simulation.updater.PriorityUpdater;
import org.epilogtool.OptionStore;
import org.epilogtool.cellularevent.CEEvaluation;
import org.epilogtool.cellularevent.CellularEventExpression;
import org.epilogtool.common.RandCentral;
import org.epilogtool.common.RandomSeedType;
import org.epilogtool.common.Tuple2D;
import org.epilogtool.core.Epithelium;
import org.epilogtool.core.EpitheliumGrid;
import org.epilogtool.core.EpitheliumUpdateSchemeInter;
import org.epilogtool.core.UpdateCells;
import org.epilogtool.core.cellDynamics.CellularEvent;
import org.epilogtool.gui.dialog.GridNodePercent;
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
	private CEEvaluation ceEvaluator;
	// Perturbed models cache - avoids repeatedly computing perturbations at
	// each step
	private Map<LogicalModel, Map<AbstractPerturbation, PriorityUpdater>> updaterCache;

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
		if (this.epithelium.getUpdateSchemeInter().getRandomSeedType().equals(RandomSeedType.RANDOM)) {
			this.random = RandCentral.getInstance().getNewGenerator();
		} else {
			this.random = RandCentral.getInstance()
					.getNewGenerator(this.epithelium.getUpdateSchemeInter().getRandomSeed());
		}
		this.gridHistory = new ArrayList<EpitheliumGrid>();
		EpitheliumGrid firstGrid = this.epithelium.getEpitheliumGrid();
		firstGrid.updateNodeValueCounts();
		this.gridHistory.add(this.restrictGridWithPerturbations(firstGrid));
		this.gridHashHistory = new ArrayList<String>();
		this.gridHashHistory.add(firstGrid.hashGrid());
		this.stable = false;
		this.hasCycle = false;
		this.ceEvaluator = new CEEvaluation();
		this.buildPriorityUpdaterCache();
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
		// updaterCache stores the PriorityUpdater to avoid unnecessary
		// computing
		this.updaterCache = new HashMap<LogicalModel, Map<AbstractPerturbation, PriorityUpdater>>();
		for (int y = 0; y < this.getCurrentGrid().getY(); y++) {
			for (int x = 0; x < this.getCurrentGrid().getX(); x++) {
				if (this.getCurrentGrid().isEmptyCell(x, y)) {
					continue;
				}
				LogicalModel m = this.getCurrentGrid().getModel(x, y);
				AbstractPerturbation ap = this.epithelium.getEpitheliumGrid().getPerturbation(x, y);
				if (!this.updaterCache.containsKey(m)) {
					this.updaterCache.put(m, new HashMap<AbstractPerturbation, PriorityUpdater>());
				}
				if (!this.updaterCache.get(m).containsKey(ap)) {
					// Apply model perturbation
					LogicalModel perturb = (ap == null) ? m : ap.apply(m);
					// Get Priority classes
					PriorityClasses pcs = this.epithelium.getPriorityClasses(m).getPriorities();
					PriorityUpdater updater = new PriorityUpdater(perturb, pcs);
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
		boolean popUpdates = false;

		EpitheliumGrid neighboursGrid = this.getNeighboursGrid();
		EpitheliumGrid nextGrid = currGrid.clone();

		Set<ComponentPair> sIntegComponentPairs = this.epithelium.getIntegrationComponentPairs();

		IFEvaluation evaluator = new IFEvaluation(neighboursGrid);

		// Gets the set of cells that can be updated
		// And builds the default next grid (= current grid)
		HashMap<Tuple2D<Integer>, byte[]> cells2update = new HashMap<Tuple2D<Integer>, byte[]>();
		List<Tuple2D<Integer>> keys = new ArrayList<Tuple2D<Integer>>();
		List<Tuple2D<Integer>> changedKeys = new ArrayList<Tuple2D<Integer>>();
		HashMap<Tuple2D<Integer>, CellularEvent> cells2event = new HashMap<Tuple2D<Integer>, CellularEvent>();

		for (int y = 0; y < currGrid.getY(); y++) {
			for (int x = 0; x < currGrid.getX(); x++) {
				if (currGrid.isEmptyCell(x, y)) {
					continue;
				}
				byte[] currState = currGrid.getCellState(x, y);

				// Compute next state
				byte[] nextState = this.nextCellValue(x, y, currGrid, evaluator, sIntegComponentPairs);

				// If the cell state changed then add it to the pool
				Tuple2D<Integer> key = new Tuple2D<Integer>(x, y);
				cells2update.put(key, nextState);
				keys.add(key);
				if (!Arrays.equals(currState, nextState)) {
					changedKeys.add(key);
				}

				// Cellular events
				LogicalModel m = currGrid.getModel(x, y);
				CellularEvent prevCellEvent = currGrid.getCellEvent(x, y);
				CellularEvent nextCellEvent = this.nextCellEvent(m, nextState);
				cells2event.put(key, nextCellEvent);
				if (!prevCellEvent.equals(CellularEvent.DEFAULT) && prevCellEvent.equals(nextCellEvent)) {
					popUpdates = true;
				}
			}
		}

		if (this.epithelium.getUpdateSchemeInter().getUpdateCells().equals(UpdateCells.UPDATABLECELLS)) {
			keys = changedKeys;
		}
		List<Tuple2D<Integer>> divisionUpdates = new ArrayList<Tuple2D<Integer>>();
		List<Tuple2D<Integer>> deathUpdates = new ArrayList<Tuple2D<Integer>>();

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
				// Update cell event
				nextGrid.setCellEvent(keys.get(i).getX(), keys.get(i).getY(), cells2event.get(keys.get(i)));
				CellularEvent currCellEvent = currGrid.getCellEvent(keys.get(i).getX(), keys.get(i).getY());
				CellularEvent nextCellEvent = cells2event.get(keys.get(i));
				if (!currCellEvent.equals(CellularEvent.DEFAULT) && currCellEvent.equals(nextCellEvent)) {
					if (currCellEvent.equals(CellularEvent.PROLIFERATION)) {
						divisionUpdates.add(keys.get(i));
					} else {
						deathUpdates.add(keys.get(i));
					}
				}
			}
		}

		// There is no more space & no one is dying today :(
		int emptyModelNumber = nextGrid.countEmptyCells();
		popUpdates = deathUpdates.size() > 0 && emptyModelNumber > 0;
		if (changedKeys.isEmpty() && !popUpdates) {
			this.stable = true;
			return currGrid;
		}

		// Proliferation and Death updates
		if (popUpdates) {
			Collections.shuffle(divisionUpdates, this.random);
			Collections.shuffle(deathUpdates, this.random);
			for (Tuple2D<Integer> cell : deathUpdates) {
				nextGrid.removeCell(cell.getX(), cell.getY());
				emptyModelNumber += 1;
			}
			while (emptyModelNumber > 0 && divisionUpdates.size() > 0) {
				this.divideCell(nextGrid, divisionUpdates);
				emptyModelNumber -= 1;
			}
			nextGrid.updateNodeValueCounts();
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

	private CellularEvent nextCellEvent(LogicalModel m, byte[] nextState) {
		for (CellularEvent cellEvent : this.epithelium.getModelEventManager().getModelEvents(m)) {
			CellularEventExpression exp = this.epithelium.getModelEventManager().getModelEventExpression(m, cellEvent)
					.getcomputedExpression();
			if (ceEvaluator.evaluate(m, nextState, exp) == true) {
				return cellEvent;
			}
		}
		return CellularEvent.DEFAULT;
	}

	private byte[] nextCellValue(int x, int y, EpitheliumGrid currGrid, IFEvaluation evaluator,
			Set<ComponentPair> sIntegComponentPairs) {
		byte[] currState = currGrid.getCellState(x, y).clone();
		LogicalModel m = currGrid.getModel(x, y);
		AbstractPerturbation ap = currGrid.getPerturbation(x, y);
		PriorityUpdater updater = this.updaterCache.get(m).get(ap);

		// 2. Update integration components
		for (NodeInfo node : m.getNodeOrder()) {
			ComponentPair nodeCP = new ComponentPair(m, node);
			if (node.isInput() && sIntegComponentPairs.contains(nodeCP)) {
				List<IntegrationFunctionExpression> lExpressions = this.epithelium
						.getIntegrationFunctionsForComponent(nodeCP).getComputedExpressions();
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

	private void divideCell(EpitheliumGrid nextGrid, List<Tuple2D<Integer>> cells2Divide) {
		Tuple2D<Integer> cell2Divide = cells2Divide.get(0);
		cells2Divide.remove(0);
		LogicalModel m = nextGrid.getModel(cell2Divide.getX(), cell2Divide.getY());
		byte[] motherState = nextGrid.getCellState(cell2Divide.getX(), cell2Divide.getY()).clone();
		List<Tuple2D<Integer>> path = nextGrid.divisionPath(this.random, cell2Divide);
		for (int index = 1; index < path.size(); index++) {
			if (cells2Divide.contains(path.get(index))) {
				cells2Divide.remove(path.get(index).clone());
				cells2Divide.add(path.get(index - 1).clone());
			}
		}
		Tuple2D<Integer> motherPosition = path.get(path.size() - 1).clone();
		Tuple2D<Integer> daughterPosition = path.get(path.size() - 2).clone();
		nextGrid.shiftCells(path);
		nextGrid.divideCell(motherPosition.getX(), motherPosition.getY(), daughterPosition.getX(),
				daughterPosition.getY());

		if (this.epithelium.getModelHeritableNodes().hasHeritableNodes(m)) {
			for (String node : this.epithelium.getModelHeritableNodes().getModelHeritableNodes(m)) {
				int nodeIndex = nextGrid.getNodeIndex(motherPosition.getX(), motherPosition.getY(), node);
				nextGrid.setCellComponentValue(motherPosition.getX(), motherPosition.getY(), node,
						motherState[nodeIndex]);
				nextGrid.setCellComponentValue(daughterPosition.getX(), daughterPosition.getY(), node,
						motherState[nodeIndex]);
			}
		}
		nextGrid.updateEpiCellConnections(path);
	}

	public void removeCell(EpitheliumGrid nextGrid, int x, int y) {
		nextGrid.removeCell(x, y);
	}

	public boolean isStableAt(int i) {
		return (i >= this.gridHistory.size() && this.stable);
	}

	private boolean isSynchronous() {
		if (this.epithelium.getUpdateSchemeInter().getAlpha() < 1)
			return false;
		Map<ComponentPair, Float> mSigmas = this.epithelium.getUpdateSchemeInter().getCPSigmas();
		for (ComponentPair cp : mSigmas.keySet()) {
			if (mSigmas.get(cp) < 1)
				return false;
		}
		return true;
	}

	public int getTerminalCycleLen() {
		if (this.isSynchronous()) {
			String sGrid = this.gridHashHistory.get(this.gridHashHistory.size()-1);
			int pos = this.gridHashHistory.indexOf(sGrid);
			if (pos < this.gridHashHistory.size()-1) {
				return (this.gridHashHistory.size()-1) - pos;
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

	private EpitheliumGrid getNeighboursGrid() {
		// Creates an epithelium which is only visited to 'see' neighbours and
		// their states
		EpitheliumGrid neighbourEpi = this.getCurrentGrid().clone();

		Map<ComponentPair, Float> mSigmaAsync = this.epithelium.getUpdateSchemeInter().getCPSigmas();

		Map<LogicalModel, Set<Tuple2D<Integer>>> mapModelPositions = neighbourEpi.getModelPositions();
		if (mSigmaAsync.size() > 0) {
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
					List<Tuple2D<Integer>> modelPositions = new ArrayList<Tuple2D<Integer>>(mapModelPositions.get(m));
					int selectedCells = (int) Math.ceil((1 - sigma) * modelPositions.size());
					Collections.shuffle(modelPositions, this.random);
					List<Tuple2D<Integer>> selectedModelPositions = modelPositions.subList(0, selectedCells);
					for (Tuple2D<Integer> tuple : selectedModelPositions) {
						if (!(delayGrid.getModel(tuple.getX(), tuple.getY()).equals(m))) {
							neighbourEpi.setCellComponentValue(tuple.getX(), tuple.getY(), nodeID, (byte) 0);
						} else {
							byte[] delayState = delayGrid.getCellState(tuple.getX(), tuple.getY());
							neighbourEpi.setCellComponentValue(tuple.getX(), tuple.getY(), nodeID,
									(byte) delayState[nodePosition]);
						}
					}
				}
			}
		}
		return neighbourEpi;
	}

	private void printConnections(EpitheliumGrid grid) {
		String result = "";
		for (int x = 0; x < grid.getX(); x++) {
			for (int y = 0; y < grid.getY(); y++) {
				if (grid.isEmptyCell(x, y)) {
					result += " \t";
				}
				result += grid.getEpiCellConnections().size(grid.getCellID(x, y)) + "\t";
			}
			result += "\n";
		}
		System.out.println(result);
	}

	private void printLineage(EpitheliumGrid grid) {
		String result = "";
		for (int x = 0; x < grid.getX(); x++) {
			for (int y = 0; y < grid.getY(); y++) {
				if (grid.isEmptyCell(x, y)) {
					result += " \t";
				}
				result += grid.getCellID(x, y).getDepth() + "\t";
			}
			result += "\n";
		}
		System.out.println(result);
	}

	public List<Map<String, Float>> getCell2Percentage() {
		List<Map<String, Float>> percentageHistory = new ArrayList<Map<String, Float>>();
		int index = 0;
		for (EpitheliumGrid grid : this.gridHistory) {
			percentageHistory.add(new HashMap<String, Float>());
			for (LogicalModel model : grid.getModelSet()) {
				for (NodeInfo node : model.getNodeOrder()) {
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
