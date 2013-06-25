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

	private Epithelium epithelium = null;
	private MainFrame mainFrame = null;
	private boolean composedModelPresent = false;

	public GlobalModel(MainFrame mainFrame, Epithelium epithelium) {
		this.mainFrame = mainFrame;
		this.epithelium = epithelium;
		this.composedModelPresent = (epithelium.getComposedModel() != null);
	}

	public GlobalModel(MainFrame mainFrame, Epithelium epithelium,
			boolean needsComposedModel) {
		this.mainFrame = mainFrame;
		this.epithelium = epithelium;
		if (needsComposedModel && epithelium.getComposedModel() == null)
			mainFrame.getLogicalModelComposition().createComposedModel();
		this.composedModelPresent = needsComposedModel;
	}

	public Grid getNextState(Grid currentState) {

		String key = this.mainFrame.epithelium.getSelectedPriority();

		List<List<NodeInfo>> priorities = this.mainFrame.epithelium
				.getPrioritiesSet().get(key);

		if (priorities == null) {
			priorities = new ArrayList<List<NodeInfo>>();
			priorities.add(this.mainFrame.epithelium.getUnitaryModel()
					.getNodeOrder());
		}

		Grid nextState = currentState;
		//System.out.println("Current STate:" + currentState.getValue(1, epithelium.getUnitaryModel().getNodeOrder().get(0)));
		for (List<NodeInfo> nodes : priorities) {
			nextState = getNextStatePriorities(currentState, nodes);
			//System.out.println("next State:" + nextState.getValue(1, epithelium.getUnitaryModel().getNodeOrder().get(0)));
			if (!currentState.equals(nextState)) {
				break;
			}
		}
		return nextState;
	}

	private Grid getNextStatePriorities(Grid currentState, List<NodeInfo> nodes) {

		if (composedModelPresent)
			return getNextStateFromComposedModel(currentState, nodes);
		else
			return getNextStateUsingCellularAutomata(currentState, nodes);
	}

	private Grid getNextStateFromComposedModel(Grid currentState,
			List<NodeInfo> nodes) {

		System.out.println("I am using the composed Model and the nodes of this priority list are: " + nodes);

		LogicalModel composedModel = this.epithelium.getComposedModel();
		Grid nextState = new Grid(currentState.getNumberInstances(),
				currentState.getListNodes());

		byte[] composedState = new byte[composedModel.getNodeOrder().size()];

		for (NodeInfo node : composedModel.getNodeOrder())
			
			composedState[composedModel.getNodeOrder().indexOf(node)] = currentState
					.getValue(this.mainFrame.getLogicalModelComposition()
							.getOriginalInstance(node), this.mainFrame
							.getLogicalModelComposition().getOriginalNode(node));

		for (NodeInfo node : composedModel.getNodeOrder()) {
			
			if (!nodes.contains(this.mainFrame.getLogicalModelComposition().new2Old.get(node).getKey())) {
				
				int index = composedModel.getNodeOrder().indexOf(node);
				byte next = composedState[index];
				
				nextState.setGrid(mainFrame.getLogicalModelComposition()
				.getOriginalInstance(node).intValue(), mainFrame
				.getLogicalModelComposition().getOriginalNode(node), next);
				
				continue;
			}
			
			int index = composedModel.getNodeOrder().indexOf(node);
		

			byte next = 0;
			byte target;
			byte current;

			current = composedState[index];
			target = composedModel.getTargetValue(index, composedState);

			next = computeNextValue(
					current,
					target,
					mainFrame.getLogicalModelComposition()
							.getOriginalNode(node),
					mainFrame.getLogicalModelComposition()
							.getOriginalInstance(node).intValue());

			nextState.setGrid(mainFrame.getLogicalModelComposition()
					.getOriginalInstance(node).intValue(), mainFrame
					.getLogicalModelComposition().getOriginalNode(node), next);

		}

		for (NodeInfo node : composedModel.getExtraComponents()) {

			if (!nodes.contains(this.mainFrame.getLogicalModelComposition().new2Old.get(node).getKey())) {
				
				int index = composedModel.getExtraComponents().indexOf(node);
				byte next = composedState[index];
				
				nextState.setGrid(mainFrame.getLogicalModelComposition()
				.getOriginalInstance(node).intValue(), mainFrame
				.getLogicalModelComposition().getOriginalNode(node), next);
				
				continue;
			}

			byte current = 0;
			byte next = 0;
			int index = composedModel.getExtraComponents().indexOf(node);
			byte target = composedModel.getExtraValue(index, composedState);

			next = computeNextValue(
					current,
					target,
					mainFrame.getLogicalModelComposition()
							.getOriginalNode(node),
					mainFrame.getLogicalModelComposition()
							.getOriginalInstance(node).intValue());

			nextState.setGrid(mainFrame.getLogicalModelComposition()
					.getOriginalInstance(node).intValue(), mainFrame
					.getLogicalModelComposition().getOriginalNode(node), next);
		}

		return nextState;
	}

	private Grid getNextStateUsingCellularAutomata(Grid currentState,
			List<NodeInfo> nodes) {
		
		System.out.println("I am using the cellular Automata and the nodes of this priority list are: " + nodes);
		
		
		Grid nextState = new Grid(currentState.getNumberInstances(),
				currentState.getListNodes());

		List<NodeInfo> integrationInputs = new ArrayList<NodeInfo>();

		for (NodeInfo node : epithelium.getUnitaryModel().getNodeOrder())
			if (epithelium.isIntegrationComponent(node))
				integrationInputs.add(node);

		// update integration inputs

		Map<Map.Entry<String, Integer>, NodeInfo> translator = new HashMap<Map.Entry<String, Integer>, NodeInfo>();
		for (int instance = 0; instance < mainFrame.topology
				.getNumberInstances(); instance++) {
			for (NodeInfo node : epithelium.getUnitaryModel().getNodeOrder())
				translator.put(new AbstractMap.SimpleEntry<String, Integer>(
						node.getNodeID(), new Integer(instance)), node);
		}

		CompositionContext context = new CompositionContextImpl(
				mainFrame.topology,
				epithelium.getUnitaryModel().getNodeOrder(), translator);

		for (NodeInfo integrationInput : integrationInputs) {
			IntegrationExpression expressions[] = epithelium
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
			for (NodeInfo node : epithelium.getUnitaryModel().getNodeOrder()) {

				if (!nodes.contains(node))
				{
					byte next = currentState.getValue(instance,node);
					nextState.setGrid(instance, node, next);
					continue;
				}

				int index = epithelium.getUnitaryModel().getNodeOrder()
						.indexOf(node);

				byte next = 0;
				byte target;
				byte current;

				byte[] cellState = currentState.asByteArray(instance);

				current = cellState[index];
				target = epithelium.getUnitaryModel().getTargetValue(index,
						cellState);

				next = computeNextValue(current, target, node, instance);

				nextState.setGrid(instance, node, next);
				//System.out.println(nextState.getValue(1, epithelium.getUnitaryModel().getNodeOrder().get(0)));
				
			}
		}

		return nextState;
	}

	private byte computeNextValue(byte current, byte target, NodeInfo node,
			int instance) {
		
		
		byte next = current;

		if (current != target)
			next = (byte) (current + ((target - current) / Math.abs(target
					- current)));
		else
			next = target;

		AbstractPerturbation mutant = epithelium
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
