package org.epilogtool.project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
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
import org.epilogtool.core.Epithelium;
import org.epilogtool.core.EpitheliumGrid;
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
	private List<EpitheliumGrid> stateHistory;
	private boolean stable;
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
		this.stateHistory = new ArrayList<EpitheliumGrid>();
		this.stateHistory.add(this.epithelium.getEpitheliumGrid());
		this.stable = false;
		this.buildPriorityUpdaterCache();
	}

	private void buildPriorityUpdaterCache() {
		this.updaterCache = new PriorityUpdater[this.getCurrentGrid().getX()][this
				.getCurrentGrid().getY()];
		Map<LogicalModel, Map<AbstractPerturbation, PriorityUpdater>> tmpMap = new HashMap<LogicalModel, Map<AbstractPerturbation, PriorityUpdater>>();
		for (int y = 0; y < this.getCurrentGrid().getY(); y++) {
			for (int x = 0; x < this.getCurrentGrid().getX(); x++) {
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

		EpitheliumGrid nextGrid = currGrid.clone();

		Set<NodeInfo> sIntegComponents = this.epithelium
				.getIntegrationFunctionsComponents();

		IntegrationFunctionEvaluation evaluator = new IntegrationFunctionEvaluation(
				currGrid, this.epithelium.getComponentFeatures());

		// Gets the set of cells that can be updated
		// And builds the default next grid (= current grid)
		HashMap<Tuple2D<Integer>, byte[]> cells2update = new HashMap<Tuple2D<Integer>, byte[]>();
		Stack<Tuple2D<Integer>> keys = new Stack<Tuple2D<Integer>>();

		for (int y = 0; y < currGrid.getY(); y++) {
			for (int x = 0; x < currGrid.getX(); x++) {
				byte[] currState = currGrid.getCellState(x, y);
				// Default cell next state is same as current
				nextGrid.setCellState(x, y, currState.clone());
				// Compute next state
				byte[] nextState = this.nextCellValue(x, y, currGrid,
						evaluator, sIntegComponents);
				// If the cell state changed then add it to the pool
				if (nextState == null || !Arrays.equals(currState, nextState)) {
					Tuple2D<Integer> key = new Tuple2D<Integer>(x, y);
					cells2update.put(key, nextState);
					keys.add(key);
				}
			}
		}

		if (keys.size() > 0) {
			// Randomize the order of cells to update
			Collections.shuffle(keys,
					new Random(Double.doubleToLongBits(Math.random())));

			// Updates at least one (asynchronous case - alpha=0)
			Tuple2D<Integer> key = keys.pop();
			nextGrid.setCellState(key.getX(), key.getY(), cells2update.get(key));

			// Inter-cellular alpha-asynchronism
			float alphaProb = epithelium.getUpdateSchemeInter().getAlpha();

			// Updates the rest of them if alphaProb permits
			for (int i = 0; i < (alphaProb * keys.size()); i++) {
				key = keys.get(i);
				nextGrid.setCellState(key.getX(), key.getY(),
						cells2update.get(key));
			}
		} else {
			this.stable = true;
			return currGrid;
		}

		this.stateHistory.add(nextGrid);
		return nextGrid;
	}

	private byte[] nextCellValue(int x, int y, EpitheliumGrid currGrid,
			IntegrationFunctionEvaluation evaluator,
			Set<NodeInfo> sIntegComponents) {
		byte[] currState = currGrid.getCellState(x, y);

		PriorityUpdater updater = this.updaterCache[x][y];
		LogicalModel m = updater.getModel();

		// 2. Update integration components
		for (NodeInfo node : m.getNodeOrder()) {
			if (node.isInput() && sIntegComponents.contains(node)) {
				List<IntegrationExpression> lExpressions = this.epithelium
						.getIntegrationFunctionsForComponent(node)
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
		return (i >= this.stateHistory.size() && this.stable);
	}

	public EpitheliumGrid getGridAt(int i) {
		if (i < this.stateHistory.size()) {
			return this.stateHistory.get(i);
		}
		return this.nextStepGrid();
	}

	public EpitheliumGrid getCurrentGrid() {
		return stateHistory.get(stateHistory.size() - 1);
	}

	public Epithelium getEpithelium() {
		return this.epithelium;
	}
}
