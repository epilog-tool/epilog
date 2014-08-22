package org.ginsim.epilog;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;
import org.colomoto.logicalmodel.perturbation.AbstractPerturbation;
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
	}

	/**
	 * This function retrieves the next step in the simulation. The first step
	 * in this
	 */
	public EpitheliumGrid nextStepGrid() {
		EpitheliumGrid currGrid = this.stateHistory.get(this.stateHistory
				.size() - 1);
		EpitheliumGrid nextGrid = currGrid.clone();

		Set<String> sIntegComponents = this.epithelium
				.getIntegrationFunctionsComponents();

		IntegrationFunctionEvaluation evaluator = new IntegrationFunctionEvaluation(
				currGrid, this.epithelium.getComponentFeatures());

		for (int y = 0; y < currGrid.getY(); y++) {
			for (int x = 0; x < currGrid.getX(); x++) {
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
				ModelPriorityClasses mpc = this.epithelium
						.getPriorityClasses(m);
				boolean hasChanged = false;
				for (int p = 0; p < mpc.size(); p++) {
					// At least one variable has been updated in the current PC
					if (hasChanged)
						break;
					for (String varID : mpc.getVarsAtIndex(p)) {
						String nodeID = (varID
								.endsWith(ModelPriorityClasses.INC) || varID
								.endsWith(ModelPriorityClasses.DEC)) ? varID
								.substring(0, varID.length()
										- ModelPriorityClasses.INC.length())
								: varID;

						NodeInfo node = this.epithelium.getComponentFeatures()
								.getNodeInfo(nodeID);
						int index = perturbedModel.getNodeOrder().indexOf(node);
						int target = perturbedModel.getTargetValue(index,
								currState);

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
				nextGrid.setCellState(x, y, nextState);
			}
		}
		this.stateHistory.add(nextGrid);
		return nextGrid;
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
}
