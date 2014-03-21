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
	public void nextStepGrid() {
		EpitheliumGrid currGrid = this.stateHistory.get(this.stateHistory
				.size() - 1);
		EpitheliumGrid nextGrid = currGrid.clone();

		Set<String> sIntegComponents = this.epithelium
				.getIntegrationFunctionsComponents();

		IntegrationFunctionEvaluation evaluator = new IntegrationFunctionEvaluation(
				currGrid, this.epithelium.getComponentFeatures());

		for (int x = 0; x < currGrid.getX(); x++) {
			for (int y = 0; y < currGrid.getY(); y++) {
				byte[] currState = currGrid.getCellState(x, y);
				byte[] nextState = currState.clone();

				// 1. Apply the Cell perturbation
				LogicalModel m = currGrid.getModel(x, y);
				AbstractPerturbation ap = currGrid.getPerturbation(x, y);
				LogicalModel perturbedModel = m;
				if (ap != null) {
					System.out.println(x + ","+ y + " <- " + ap);
					perturbedModel = ap.apply(m);
				}

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
						nextState[perturbedModel.getNodeOrder().indexOf(node)] = target;
					}
				}

				// 3. Apply Priorities
				ModelPriorityClasses mpc = this.epithelium
						.getPriorityClasses(m);
				boolean hasChanged = false;
				for (int p = 0; p < mpc.size(); p++) {
					for (String nodeID : mpc.getVarsAtIndex(p)) {
						String id = (nodeID.endsWith("+") || nodeID
								.endsWith("-")) ? nodeID.substring(-1) : nodeID;
						NodeInfo node = this.epithelium.getComponentFeatures()
								.getNodeInfo(nodeID);
						int index = perturbedModel.getNodeOrder().indexOf(node);
						int target = perturbedModel.getTargetValue(index,
								currState);

						if (target > currState[index]) {
							if (nodeID.endsWith("-"))
								break;
							if (currState[index] < node.getMax()) {
								nextState[index] = (byte) (currState[index] + 1);
								hasChanged = true;
							}
						} else if (target < currState[index]) {
							if (nodeID.endsWith("+"))
								break;
							if (currState[index] > 0) {
								nextState[index] = (byte) (currState[index] - 1);
								hasChanged = true;
							}
						}
					}
					// At least one variable has been updated in the current PC
					if (hasChanged)
						break;
				}
				nextGrid.setCellState(x, y, nextState);
			}
		}
		this.stateHistory.add(nextGrid);
	}
	
	public EpitheliumGrid getCurrentGrid() {
		return stateHistory.get(stateHistory.size()-1);
	}
}
