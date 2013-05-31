package pt.igc.nmd.epilog;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;

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
	
	public GlobalModel(MainFrame mainFrame, Epithelium epithelium, boolean needsComposedModel) {
		this.mainFrame = mainFrame;
		this.epithelium = epithelium;
		if (needsComposedModel && epithelium.getComposedModel() == null)
			mainFrame.getLogicalModelComposition().createComposedModel();
	}

	// public GlobalModel(List<LogicalModel> modelList){
	//
	// }

	public Grid getNextState(Grid currentState) {
		if (composedModelPresent)
			return getNextStateFromComposedModel(currentState);
		else
			return getNextStateUsingCellularAutomata(currentState);
	}

	private Grid getNextStateFromComposedModel(Grid currentState) {
		LogicalModel composedModel = epithelium.getComposedModel();
		Grid nextState = new Grid(currentState.getNumberInstances(),
				currentState.getListNodes());

		byte[] composedState = new byte[composedModel.getNodeOrder().size()];

		for (NodeInfo node : composedModel.getNodeOrder())
			composedState[composedModel.getNodeOrder().indexOf(node)] = currentState
					.getValue(mainFrame.getLogicalModelComposition()
							.getOriginalInstance(node), mainFrame
							.getLogicalModelComposition().getOriginalNode(node));

		for (NodeInfo node : composedModel.getNodeOrder()) {

			int index = composedModel.getNodeOrder().indexOf(node);
			byte next = 0;
			byte target;
			byte current;

			current = composedState[index];
			target = composedModel.getTargetValue(index, composedState);

			if (current != target)
				next = (byte) (current + ((target - current) / Math.abs(target
						- current)));
			else
				next = target;

			nextState.setGrid(mainFrame.getLogicalModelComposition()
					.getOriginalInstance(node).intValue(), mainFrame
					.getLogicalModelComposition().getOriginalNode(node), next);

		}

		for (NodeInfo node : composedModel.getExtraComponents()) {

			byte current = 0;
			byte next = 0;
			int index = composedModel.getExtraComponents().indexOf(node);
			byte target = composedModel.getExtraValue(index, composedState);
			if (current != target)
				next = (byte) (current + ((target - current) / Math.abs(target
						- current)));
			else
				next = target;

			nextState.setGrid(mainFrame.getLogicalModelComposition()
					.getOriginalInstance(node).intValue(), mainFrame
					.getLogicalModelComposition().getOriginalNode(node), next);

		}

		return nextState;

	}

	private Grid getNextStateUsingCellularAutomata(Grid currentState) {
	
		Grid nextState = new Grid(currentState.getNumberInstances(),currentState.getListNodes());
		
		List<NodeInfo> integrationInputs = new ArrayList<NodeInfo>();

		
		for (NodeInfo node : epithelium.getUnitaryModel().getNodeOrder())
			if (epithelium.isIntegrationComponent(node))
				integrationInputs.add(node);

		
		
		// update integration inputs
		
		Map<Map.Entry<String, Integer>, NodeInfo> translator = new HashMap<Map.Entry<String, Integer>, NodeInfo>();
		for (int instance = 0; instance < mainFrame.topology.getNumberInstances(); instance++){
			for (NodeInfo node : epithelium.getUnitaryModel().getNodeOrder())
				translator.put(new AbstractMap.SimpleEntry<String,Integer>(node.getNodeID(),new Integer(instance)), node);
		}
		
		
		CompositionContext context = new CompositionContextImpl(mainFrame.topology, epithelium.getUnitaryModel().getNodeOrder(), translator);
		
		for (NodeInfo integrationInput : integrationInputs){
			IntegrationExpression expressions[] = epithelium.getIntegrationExpressionsForInput(integrationInput);
			IntegrationFunctionEvaluation evaluator[] = new IntegrationFunctionEvaluation[expressions.length]; 
			
			for (int index = 0; index < expressions.length; index++)
				evaluator[index] = new IntegrationFunctionEvaluation(expressions[index],context);
				
			for (int instance = 1; instance < mainFrame.topology.getNumberInstances(); instance++){
				
				byte target = 0;
				for (byte value = 1; value <= expressions.length; value++){
					
					if (evaluator[value-1].evaluate(instance, currentState.asByteMatrix())){
						target = value;
					}
				}	
				
				currentState.setGrid(instance, integrationInput, target);
			}
					
		}
			
		
		
		// update proper components
		
		for (int instance = 0; instance < mainFrame.topology.getNumberInstances(); instance++){
			for (NodeInfo node : epithelium.getUnitaryModel().getNodeOrder()){
		
				int index = epithelium.getUnitaryModel().getNodeOrder().indexOf(node);
				byte next = 0;
				byte target;
				byte current;
				
				byte[] cellState = currentState.asByteArray(instance);

				current = cellState[index];
				target = epithelium.getUnitaryModel().getTargetValue(index, cellState);

				if (current != target)
					next = (byte) (current + ((target - current) / Math.abs(target
							- current)));
				else
					next = target;

				nextState.setGrid(instance,node,next);
				
			}
		}
		
	
		
		
		
		
		
		return nextState;
	}
}
