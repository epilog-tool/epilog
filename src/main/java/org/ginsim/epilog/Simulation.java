package org.ginsim.epilog;

import java.util.ArrayList;
import java.util.List;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;
import org.colomoto.logicalmodel.perturbation.AbstractPerturbation;
import org.ginsim.epilog.core.ComponentIntegrationFunctions;
import org.ginsim.epilog.core.Epithelium;
import org.ginsim.epilog.core.EpitheliumGrid;
import org.ginsim.epilog.core.EpitheliumIntegrationFunctions;
import org.ginsim.epilog.core.ModelPriorityClasses;

import pt.igc.nmd.epilog.integrationgrammar.CompositionContext;
import pt.igc.nmd.epilog.integrationgrammar.CompositionContextImpl;
import pt.igc.nmd.epilog.integrationgrammar.IntegrationFunctionEvaluation;

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
		// CompositionContext context = new CompositionContextImpl(topology,
		// nodeOrder, translator);
		EpitheliumGrid currGrid = this.stateHistory.get(this.stateHistory
				.size() - 1);
		EpitheliumGrid nextGrid = currGrid.clone();

		for (int x = 0; x < currGrid.getX(); x++) {
			for (int y = 0; y < currGrid.getY(); y++) {
				byte[] currState = currGrid.getCellState(x, y);
				byte[] nextState = currState.clone();

				// NextStepCell
				LogicalModel m = currGrid.getModel(x, y);
				AbstractPerturbation ap = currGrid.getPerturbation(x, y);
				LogicalModel perturbedModel = ap.apply(m);

				// 1. Update state of integration components
				for (NodeInfo node : m.getNodeOrder()) {
					ComponentIntegrationFunctions cif = this.epithelium.getIntegrationFunctionsForComponent(node.getNodeID());
					String comp = node.getNodeID();
					if (node.isInput() && cif != null) {
						// NextStepComponent (integration)
						IntegrationFunctionEvaluation evaluator[] = cif
								.getEvaluator(context);

						byte target = 0;
						for (int pos = evaluator.length - 1; pos >= 0; pos--) {
							if (evaluator[pos].evaluate(x, y, currGrid)) {
								target = (byte) (pos + 1);
								break;
							}
						}
						nextState[m.getNodeOrder().indexOf(node)] = target;
					}
				}

				// 2. Priorities...
				ModelPriorityClasses mpc = this.epithelium
						.getPriorityClasses(m);
				boolean hasChanged = false;
				for (int p = 0; p < mpc.size(); p++) {
					for (String nodeID : mpc.getVarsAtIndex(p)) {
						// TODO: currGrid.nodeID2Node...
						String id = (nodeID.endsWith("+") || nodeID
								.endsWith("-")) ? nodeID.substring(-1) : nodeID;
						NodeInfo node = currGrid.nodeID2Node(m, id);
						int index = m.getNodeOrder().indexOf(node);
						int target = perturbedModel.getTargetValue(index,
								currState);

						if (currState[index] > target) {
							if (nodeID.endsWith("-"))
								break;
							if (currState[index] < node.getMax()) {
								nextState[index] = (byte) (currState[index] + 1);
								hasChanged = true;
							}
						} else if (currState[index] < target) {
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
}
