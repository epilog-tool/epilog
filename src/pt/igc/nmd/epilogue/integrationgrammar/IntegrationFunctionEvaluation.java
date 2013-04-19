package pt.igc.nmd.epilogue.integrationgrammar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.colomoto.logicalmodel.NodeInfo;

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

	// Add more convenient expression evaluation procedure
	public boolean evaluate(int instance,
			Map<NodeInfo, List<Integer>> argumentValues) {
		Map<String, NodeInfo> mapNameNode = new HashMap<String, NodeInfo>();
		for (NodeInfo node : argumentValues.keySet())
			mapNameNode.put(node.getNodeID(), node);

		return traverseTreeEvaluate(instance, argumentValues, mapNameNode,
				expression);
	}

	private boolean traverseTreeEvaluate(int instance,
			Map<NodeInfo, List<Integer>> argumentValues,
			Map<String, NodeInfo> mapNameNode, IntegrationExpression expression) {

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
					if (operand == null)
						continue;
					else if (!traverseTreeEvaluate(instance, argumentValues,
							mapNameNode, operand)) {
						return false;
					}
				break;
			case OR:
				result = false;
				for (IntegrationExpression operand : listOperands)
					if (operand == null)
						continue;
					else if (traverseTreeEvaluate(instance, argumentValues,
							mapNameNode, operand)) {
						return true;
					}
				break;
			}
		} else if (expression instanceof IntegrationNegation) {
			return !traverseTreeEvaluate(instance, argumentValues, mapNameNode,
					((IntegrationNegation) expression).getNegatedExpression());

		} else if (expression instanceof IntegrationAtom) {
			IntegrationAtom atom = (IntegrationAtom) expression;
			String componentName = atom.getComponentName();

			NodeInfo node = mapNameNode.get(componentName);
			List<Integer> listValues = argumentValues.get(node);

			byte threshold = atom.getThreshold();
			if (threshold < 0)
				threshold = node.getMax();

			int min = atom.getMinNeighbours();
			if (min < 0)
				min = listValues.size();

			int max = atom.getMaxNeighbours();
			if (max < 0)
				max = listValues.size();

			int habilitations = 0;

			Set<Integer> neighbours = context.getNeighbourIndices(instance,
					atom.getMinDistance(), atom.getMaxDistance());

			if (min > neighbours.size() || min > max) {
				// condition is trivially impossible to satisfy
				return false;
			} else if (threshold == 0 && max < neighbours.size()) {
				// condition is trivially impossible to satisfy
				return false;
			} else if (min == 0 && max == neighbours.size()) {
				// condition is trivially tautological
				return true;
			} else if (threshold == 0 && max == neighbours.size()) {
				// condition is trivially tautological
				return true;
			}

			for (Integer value : listValues)
				if (value.intValue() >= threshold)
					habilitations++;

			if (habilitations >= min && habilitations <= max)
				return true;
			else
				return false;
		}
		return result;
	}
}
