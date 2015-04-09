package org.ginsim.epilog.project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;
import org.colomoto.logicalmodel.perturbation.AbstractPerturbation;
import org.ginsim.epilog.common.Tuple2D;
import org.ginsim.epilog.core.Epithelium;
import org.ginsim.epilog.core.EpitheliumGrid;
import org.ginsim.epilog.core.ModelPriorityClasses;
import org.ginsim.epilog.integration.IntegrationFunctionEvaluation;
import org.ginsim.epilog.integration.IntegrationFunctionSpecification.IntegrationExpression;

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

		Set<String> sIntegComponents = this.epithelium
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
				if (!Arrays.equals(currState, nextState)) {
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
			Set<String> sIntegComponents) {
		byte[] currState = currGrid.getCellState(x, y);

		// 1. Apply the Cell perturbation
		LogicalModel m = currGrid.getModel(x, y);
		AbstractPerturbation ap = currGrid.getPerturbation(x, y);
		LogicalModel perturbedModel = (ap != null) ? ap.apply(m) : m;

		// 2. Update integration components
		for (NodeInfo node : perturbedModel.getNodeOrder()) {
			String nodeID = node.getNodeID();
			if (node.isInput() && sIntegComponents.contains(nodeID)) {
				List<IntegrationExpression> lExpressions = this.epithelium
						.getIntegrationFunctionsForComponent(nodeID)
						.getComputedExpressions();
				byte target = 0;
				for (int i = 0; i < lExpressions.size(); i++) {
					if (evaluator.evaluate(x, y, lExpressions.get(i))) {
						target = (byte) (i + 1);
						break; // The lowest value being satisfied
					}
				}
				currState[perturbedModel.getNodeOrder().indexOf(node)] = target;
			}
		}
		byte[] nextState = currState.clone();

		// 3. Apply Priorities
		ModelPriorityClasses mpc = this.epithelium.getPriorityClasses(m);
		boolean hasChanged = false;
		for (int p = 0; p < mpc.size(); p++) {
			// At least one variable has been updated in the current
			// PC
			if (hasChanged)
				break;
			for (String varID : mpc.getClassVars(p)) {
				String nodeID = (varID.endsWith(ModelPriorityClasses.INC) || varID
						.endsWith(ModelPriorityClasses.DEC)) ? varID.substring(
						0, varID.length() - ModelPriorityClasses.INC.length())
						: varID;

				NodeInfo node = this.epithelium.getComponentFeatures()
						.getNodeInfo(nodeID);
				int index = perturbedModel.getNodeOrder().indexOf(node);
				int target = perturbedModel.getTargetValue(index, currState);

				if (target > currState[index]) {
					if (varID.endsWith(ModelPriorityClasses.DEC))
						break;
					if (currState[index] < node.getMax()) {
						nextState[index] = (byte) (currState[index] + 1);
						hasChanged = true;
					}
				} else if (target < currState[index]) {
					if (varID.endsWith(ModelPriorityClasses.INC))
						break;
					if (currState[index] > 0) {
						nextState[index] = (byte) (currState[index] - 1);
						hasChanged = true;
					}
				}
			}
		}
		return nextState;
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
