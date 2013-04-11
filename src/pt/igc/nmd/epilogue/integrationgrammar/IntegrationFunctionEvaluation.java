package pt.igc.nmd.epilogue.integrationgrammar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ginsim.core.graph.regulatorygraph.RegulatoryNode;

import pt.igc.nmd.epilogue.integrationgrammar.IntegrationFunctionSpecification.IntegrationAtom;
import pt.igc.nmd.epilogue.integrationgrammar.IntegrationFunctionSpecification.IntegrationExpression;
import pt.igc.nmd.epilogue.integrationgrammar.IntegrationFunctionSpecification.IntegrationNegation;
import pt.igc.nmd.epilogue.integrationgrammar.IntegrationFunctionSpecification.IntegrationOperation;


public class IntegrationFunctionEvaluation {

	private IntegrationExpression expression = null;
	private CompositionContext context = null;

	public IntegrationFunctionEvaluation(IntegrationExpression expression,
			CompositionContext context) {
		this.expression = expression;
		this.context = context;
	}

	public boolean evaluate(Map<RegulatoryNode, List<Integer>> argumentValues) {
		Map<String, RegulatoryNode> mapNameNode = new HashMap<String, RegulatoryNode>();
		for (RegulatoryNode node : argumentValues.keySet())
			mapNameNode.put(node.getNodeInfo().getNodeID(), node);

		return traverseTreeEvaluate(argumentValues, mapNameNode, expression);

	}

	private boolean traverseTreeEvaluate(
			Map<RegulatoryNode, List<Integer>> argumentValues,
			Map<String, RegulatoryNode> mapNameNode,
			IntegrationExpression expression) {
		boolean result = false;

		if (expression instanceof IntegrationOperation) {
			List<IntegrationExpression> listOperands = ((IntegrationOperation) expression)
					.getOperands();

			IntegrationLogicalOperator operator = ((IntegrationOperation) expression)
					.getOperation();

			switch (operator) {
			case AND:
				result = true;
				for (IntegrationExpression operand : listOperands)
					if (!traverseTreeEvaluate(argumentValues, mapNameNode,
							operand)) {
						result = false;
						break;
					}
				break;
			case OR:
				result = false;
				for (IntegrationExpression operand : listOperands)
					if (traverseTreeEvaluate(argumentValues, mapNameNode,
							operand)) {
						result = true;
						break;
					}
				break;
			}
		} else if (expression instanceof IntegrationNegation) {
			return !traverseTreeEvaluate(argumentValues, mapNameNode,
					((IntegrationNegation) expression).getNegatedExpression());

		} else if (expression instanceof IntegrationAtom) {
			IntegrationAtom atom = (IntegrationAtom) expression;
			String componentName = atom.getComponentName();

			RegulatoryNode node = mapNameNode.get(componentName);
			List<Integer> listValues = argumentValues.get(node);

			byte threshold = atom.getThreshold();
			if (threshold < 0)
				threshold = node.getMaxValue();

			int min = atom.getMinNeighbours();
			if (min < 0)
				min = listValues.size();

			int max = atom.getMaxNeighbours();
			if (max < 0)
				max = listValues.size();

			// TODO: we have to deal with distance too

			int habilitations = 0;

			for (Integer value : listValues) {
				if (value.intValue() >= threshold)
					habilitations++;
			}

			if (habilitations >= min && habilitations <= max)
				return true;
			else
				return false;

		}

		return result;
	}

}
