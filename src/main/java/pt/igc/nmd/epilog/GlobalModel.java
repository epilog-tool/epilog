package pt.igc.nmd.epilog;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;
import org.colomoto.logicalmodel.perturbation.AbstractPerturbation;
import org.colomoto.logicalmodel.perturbation.FixedValuePerturbation;
import org.colomoto.logicalmodel.perturbation.MultiplePerturbation;
import org.colomoto.logicalmodel.perturbation.RangePerturbation;

import pt.igc.nmd.epilog.gui.MainFrame;
import pt.igc.nmd.epilog.integrationgrammar.CompositionContext;
import pt.igc.nmd.epilog.integrationgrammar.CompositionContextImpl;
import pt.igc.nmd.epilog.integrationgrammar.IntegrationFunctionEvaluation;
import pt.igc.nmd.epilog.integrationgrammar.IntegrationFunctionSpecification.IntegrationExpression;

public class GlobalModel {

	private MainFrame mainFrame = null;

	/**
	 * Creates the global state of the epithelium, either from the composed
	 * model or the CA.
	 * 
	 * @see mainFrame
	 * @param mainFrame
	 */

	public GlobalModel(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		// System.out.println("needsComposedModel: " +
		// this.mainFrame.needsComposedModel);
		// System.out.println("getComposedModel(): " +
		// this.mainFrame.epithelium.getComposedModel());
		// System.out.println("resetComposedModel: " +
		// this.mainFrame.resetComposedModel);

		if (this.mainFrame.needsComposedModel
				&& (this.mainFrame.epithelium.getComposedModel() == null || this.mainFrame.resetComposedModel))
			mainFrame.getLogicalModelComposition().createComposedModel();
		this.mainFrame.resetComposedModel = false;
	}

	/**
	 * Receives the current state of the world and returns the first next state
	 * that is different from the current state. The next state is chosen
	 * according to the priority chosen.
	 * 
	 * @see #getNextStatePriorities
	 * @param currentState
	 *            grid with the current state
	 * @return nextState grid with the next state
	 */
	public Grid getNextState(Grid currentState) {

		String key = this.mainFrame.epithelium.getSelectedPriority();

		List<List<String>> priorities = this.mainFrame.epithelium
				.getPrioritiesSet().get(key);

		if (priorities == null) {
			priorities = new ArrayList<List<String>>();
			List<String> p_aux = new ArrayList<String>();
			for (NodeInfo node : this.mainFrame.epithelium.getUnitaryModel()
					.getNodeOrder())
				p_aux.add(node.getNodeID());
			priorities.add(p_aux);
		}

		Grid nextState = currentState;

		for (List<String> nodes : priorities) {
			nextState = getNextStatePriorities(currentState, nodes);

			if (!mainFrame.simulation.stateComparative(currentState, nextState)) {
				break;
			}
		}
		return nextState;
	}

	/**
	 * Receives the current state of the world and the list of nodes that have
	 * priority and updates the current state using the composed model or the
	 * CA, according to the user's choice.
	 * 
	 * @see getNextStateFromComposedModel
	 * @see getNextStateUsingCellularAutomata
	 * @param currentState
	 *            grid with the current state
	 * @param nodes
	 *            list of nodes that have priority (are updated first)
	 * @return nextState grid with the next state
	 */
	private Grid getNextStatePriorities(Grid currentState, List<String> nodes) {

		if (this.mainFrame.needsComposedModel)
			return getNextStateFromComposedModel(currentState, nodes);
		else
			return getNextStateUsingCellularAutomata(currentState, nodes);
	}

	/**
	 * Receives the current state of the world and the list of nodes that have
	 * priority and updates the current state using the composed model. When
	 * composing the model a reduction is performed and the pseudo-outputs
	 * (proper components that do no regulate other components) are not updated.
	 * These components are summoned as extra components and are the last
	 * components to be updated (from the set of nodes to be updated).
	 * 
	 * @see computeNextValue
	 * @param currentState
	 *            grid with the current state
	 * @param nodes
	 *            list of nodes that have priority (are updated first)
	 * @return nextState grid with the next state
	 */
	private Grid getNextStateFromComposedModel(Grid currentState,
			List<String> nodes) {

		List<NodeInfo> priorityNodesCleanOfSignal = new ArrayList<NodeInfo>();

		for (String node : nodes) {
			priorityNodesCleanOfSignal
					.add(mainFrame.prioritiesPanel.string2Node.get(node));
		}

		System.out
				.println("I am using the composed Model and the nodes of this priority list are: "
						+ nodes);

		LogicalModel composedModel = this.mainFrame.epithelium
				.getComposedModel();
		Grid nextState = new Grid(currentState.getNumberInstances(),
				currentState.getListNodes());

		byte[] composedState = new byte[composedModel.getNodeOrder().size()];

		for (NodeInfo node : composedModel.getNodeOrder())

			composedState[composedModel.getNodeOrder().indexOf(node)] = currentState
					.getValue(this.mainFrame.getLogicalModelComposition()
							.getOriginalInstance(node), this.mainFrame
							.getLogicalModelComposition().getOriginalNode(node));

		for (NodeInfo node : composedModel.getNodeOrder()) {

			if (!priorityNodesCleanOfSignal.contains(this.mainFrame
					.getLogicalModelComposition().new2Old.get(node).getKey())) {

				int index = composedModel.getNodeOrder().indexOf(node);
				byte next = composedState[index];

				nextState.setGrid(mainFrame.getLogicalModelComposition()
						.getOriginalInstance(node).intValue(), mainFrame
						.getLogicalModelComposition().getOriginalNode(node),
						next);
				// System.out.println(mainFrame.getLogicalModelComposition()
				// .getOriginalInstance(node).intValue() + " " +mainFrame
				// .getLogicalModelComposition().getOriginalNode(node) + " "
				// +next);
				continue;
			}

			int index = composedModel.getNodeOrder().indexOf(node);

			byte next = 0;
			byte target;
			byte current;

			current = composedState[index];
			target = composedModel.getTargetValue(index, composedState);

			if (current != target) {

				next = computeNextValue(current, target, mainFrame
						.getLogicalModelComposition().getOriginalNode(node),
						mainFrame.getLogicalModelComposition()
								.getOriginalInstance(node).intValue());

				// System.out.println("Main Node " +
				// mainFrame.getLogicalModelComposition()
				// .getOriginalInstance(node).intValue() + " " +mainFrame
				// .getLogicalModelComposition().getOriginalNode(node) + " "
				// +next);
			}

			else
				next = target;

			nextState.setGrid(mainFrame.getLogicalModelComposition()
					.getOriginalInstance(node).intValue(), mainFrame
					.getLogicalModelComposition().getOriginalNode(node), next);

		}

		for (NodeInfo node : composedModel.getExtraComponents()) {

			
			
			if (!priorityNodesCleanOfSignal.contains(this.mainFrame
					.getLogicalModelComposition().new2Old.get(node).getKey())) {

				int index = composedModel.getExtraComponents().indexOf(node);

				byte next = currentState.getValue(this.mainFrame
						.getLogicalModelComposition().getOriginalInstance(node),
						this.mainFrame.getLogicalModelComposition()
								.getOriginalNode(node));

				nextState.setGrid(mainFrame.getLogicalModelComposition()
						.getOriginalInstance(node).intValue(), mainFrame
						.getLogicalModelComposition().getOriginalNode(node),
						next);

//				System.out.println("Not updating this Node "
//						+ mainFrame.getLogicalModelComposition()
//								.getOriginalInstance(node).intValue()
//						+ " "
//						+ mainFrame.getLogicalModelComposition()
//								.getOriginalNode(node) + " " + next);
				continue;
			}

			byte current = currentState.getValue(this.mainFrame
					.getLogicalModelComposition().getOriginalInstance(node),
					this.mainFrame.getLogicalModelComposition()
							.getOriginalNode(node));
			;
			byte next = 0;

			int index = composedModel.getExtraComponents().indexOf(node);
			byte target = composedModel.getExtraValue(index, composedState);

			if (current != target) {
				next = computeNextValue(current, target, mainFrame
						.getLogicalModelComposition().getOriginalNode(node),
						mainFrame.getLogicalModelComposition()
								.getOriginalInstance(node).intValue());

//				System.out.println("extra Node "
//						+ mainFrame.getLogicalModelComposition()
//								.getOriginalInstance(node).intValue()
//						+ " "
//						+ mainFrame.getLogicalModelComposition()
//								.getOriginalNode(node) + " " + next);
			} else
				next = target;

			nextState.setGrid(mainFrame.getLogicalModelComposition()
					.getOriginalInstance(node).intValue(), mainFrame
					.getLogicalModelComposition().getOriginalNode(node), next);
		}

		
		return nextState;
	}

//	private void printComposedState(byte[] composedState){
//		
//			for (byte value: composedState)
//				System.out.print(value);
//			System.out.println(" ");
//		}
//	
//	
//	
//	private void printNextState(Grid nextState) {
//		String aux = mainFrame.simulation.getIterationNumber() + " : ";
//		for (int instance = 0; instance < nextState.getNumberInstances(); instance++) {
//			for (NodeInfo node : nextState.getListNodes())
//				aux = aux + (nextState.getValue(instance, node));
//		}
//		System.out.println(aux);
//	}
	/**
	 * Receives the current state of the world and the list of nodes that have
	 * priority and updates the current state using the CA. The first nodes to
	 * be updated are the integration inputs must be updated first. Then the
	 * nodes that have priority are updated and the next state is returned.
	 * 
	 * @see computeNextValue
	 * @param currentState
	 *            grid with the current state
	 * @param nodes
	 *            list of nodes that have priority (are updated first)
	 * @return nextState grid with the next state
	 */
	private Grid getNextStateUsingCellularAutomata(Grid currentState,
			List<String> nodes) {

		List<NodeInfo> priorityNodesCleanOfSignal = new ArrayList<NodeInfo>();

		for (String nodeString : nodes) {
			priorityNodesCleanOfSignal
					.add(mainFrame.prioritiesPanel.string2Node.get(nodeString));
			System.out.println(nodeString);
		}

		System.out
				.println("I am using the cellular Automata and the nodes of this priority list are: "
						+ nodes);

		Grid nextState = new Grid(currentState.getNumberInstances(),
				currentState.getListNodes());

		List<NodeInfo> integrationInputs = new ArrayList<NodeInfo>();

		for (NodeInfo node : this.mainFrame.epithelium.getUnitaryModel()
				.getNodeOrder())
			if (this.mainFrame.epithelium.isIntegrationComponent(node))
				integrationInputs.add(node);

		// update integration inputs

		Map<Map.Entry<String, Integer>, NodeInfo> translator = new HashMap<Map.Entry<String, Integer>, NodeInfo>();
		for (int instance = 0; instance < mainFrame.topology
				.getNumberInstances(); instance++) {
			for (NodeInfo node : this.mainFrame.epithelium.getUnitaryModel()
					.getNodeOrder())
				translator.put(new AbstractMap.SimpleEntry<String, Integer>(
						node.getNodeID(), new Integer(instance)), node);
		}

		CompositionContext context = new CompositionContextImpl(
				mainFrame.topology, this.mainFrame.epithelium.getUnitaryModel()
						.getNodeOrder(), translator);

		for (NodeInfo integrationInput : integrationInputs) {
			IntegrationExpression expressions[] = this.mainFrame.epithelium
					.getIntegrationExpressionsForInput(integrationInput);
			IntegrationFunctionEvaluation evaluator[] = new IntegrationFunctionEvaluation[expressions.length];

			for (int index = 0; index < expressions.length; index++)
				evaluator[index] = new IntegrationFunctionEvaluation(
						expressions[index], context);

			for (int instance = 0; instance < mainFrame.topology
					.getNumberInstances(); instance++) {

				byte target = 0;
				for (byte value = 1; value <= expressions.length; value++) {

					if (evaluator[value - 1].evaluate(instance,
							currentState.asByteMatrix())) {
						target = value;
					}
				}
				currentState.setGrid(instance, integrationInput, target);
			}
		}

		// update proper components

		for (int instance = 0; instance < mainFrame.topology
				.getNumberInstances(); instance++) {
			for (NodeInfo node : this.mainFrame.epithelium.getUnitaryModel()
					.getNodeOrder()) {

				if (!priorityNodesCleanOfSignal.contains(node)) {
					byte next = currentState.getValue(instance, node);
					nextState.setGrid(instance, node, next);

					continue;
				}

				int index = this.mainFrame.epithelium.getUnitaryModel()
						.getNodeOrder().indexOf(node);

				byte next = 0;
				byte target;
				byte current;

				byte[] cellState = currentState.asByteArray(instance);

				current = cellState[index];
				target = this.mainFrame.epithelium.getUnitaryModel()
						.getTargetValue(index, cellState);

				if (current != target) {
					if ((current > target && nodes.contains(node.getNodeID()
							+ "-"))
							|| (current < target && nodes.contains(node
									.getNodeID() + "+"))
							|| (nodes.contains(node.getNodeID())))
						next = computeNextValue(current, target, node, instance);
					else
						next = currentState.getValue(instance, node);
				} else {
					next = target;
				}
				nextState.setGrid(instance, node, next);
			}
		}
		return nextState;
	}

	/**
	 * Receives the node that is to be updated, current value target value and
	 * the instance. Checks if that instance is perturbed and returns the
	 * updated value for that node.
	 * 
	 * @see mainFrame.epithelium .getInstancePerturbation(instance)
	 * 
	 * @param current
	 *            current value of the node
	 * @param target
	 *            target value of the node
	 * @param node
	 *            node to be updated
	 * @param instance
	 *            instance to update
	 * @return next next value of the node to be updated
	 */
	private byte computeNextValue(byte current, byte target, NodeInfo node,
			int instance) {

		byte next = current;

		if (current != target)
			next = (byte) (current + ((target - current) / Math.abs(target
					- current)));
		else
			next = target;

		AbstractPerturbation mutant = this.mainFrame.epithelium
				.getInstancePerturbation(instance);

		List<AbstractPerturbation> perturbations = new ArrayList<AbstractPerturbation>();

		if (mutant != null) {
			if (mutant instanceof MultiplePerturbation)
				perturbations
						.addAll(((MultiplePerturbation) mutant).perturbations);
			else
				perturbations.add(mutant);
		}

		for (AbstractPerturbation perturbation : perturbations) {
			if (perturbation instanceof FixedValuePerturbation) {
				if (node.equals(((FixedValuePerturbation) perturbation).component))
					return (byte) ((FixedValuePerturbation) perturbation).value;
			} else if (perturbation instanceof RangePerturbation) {
				if (node.equals(((RangePerturbation) perturbation).component))
					if (next > ((RangePerturbation) perturbation).max) {
						return (byte) ((RangePerturbation) perturbation).max;
					} else if (next < ((RangePerturbation) perturbation).min) {
						return (byte) ((RangePerturbation) perturbation).min;
					}
			}
		}
		return next;
	}

}
