package org.ginsim.servicegui.tool.composition.integrationgrammar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.colomoto.logicalmodel.NodeInfo;

import composition.CompositionSpecificationDialog;
import composition.integrationgrammar.IntegrationLogicalOperator;


public class IntegrationFunctionSpecification {

	public static abstract interface IntegrationExpression {
	}

	public static abstract class IntegrationOperation implements
			IntegrationExpression {
		private IntegrationLogicalOperator operator = null;
		private IntegrationExpression expression1 = null;
		private IntegrationExpression expression2 = null;

		public IntegrationOperation(IntegrationExpression exp1,
				IntegrationExpression exp2, IntegrationLogicalOperator operator) {
			this.expression1 = exp1;
			this.expression2 = exp2;
			this.operator = operator;
		}

		public IntegrationLogicalOperator getOperation() {
			return this.operator;
		}

		public List<IntegrationExpression> getOperands() {
			List<IntegrationExpression> listExpression = new ArrayList<IntegrationExpression>();
			listExpression.add(this.expression1);
			listExpression.add(this.expression2);

			return listExpression;
		}

	}

	public static class IntegrationDisjunction extends IntegrationOperation {

		public IntegrationDisjunction(IntegrationExpression exp1,
				IntegrationExpression exp2) {
			super(exp1, exp2, IntegrationLogicalOperator.OR);
		}

	}

	public static class IntegrationConjunction extends IntegrationOperation {

		public IntegrationConjunction(IntegrationExpression exp1,
				IntegrationExpression exp2) {
			super(exp1, exp2, IntegrationLogicalOperator.AND);
		}

	}

	public static class IntegrationAtom implements IntegrationExpression {
		private String componentName = null;
		private byte threshold = 0;
		private int minNeighbours = 0;
		private int maxNeighbours = 0;
		private int distance = 1;

		public IntegrationAtom(String componentName, byte threshold,
				int minNeighbours, int maxNeighbours) {
			this.componentName = componentName;
			this.threshold = threshold;
			this.minNeighbours = minNeighbours;
			this.maxNeighbours = maxNeighbours;
			this.distance = 1;
		}

		public IntegrationAtom(String componentName, byte threshold,
				int minNeighbours, int maxNeighbours, int distance) {
			this.componentName = componentName;
			this.threshold = threshold;
			this.minNeighbours = minNeighbours;
			this.maxNeighbours = maxNeighbours;
			this.distance = distance;

		}

		public String getComponentName() {
			return this.componentName;
		}

		public byte getThreshold() {
			return this.threshold;
		}

		public int getMinNeighbours() {
			return this.minNeighbours;
		}

		public int getMaxNeighbours() {
			return this.maxNeighbours;
		}

		public int getDistance() {
			return this.distance;
		}

	}

	public static IntegrationExpression createAtom(String componentName,
			String thresholdString, String minString, String maxString) {

		byte threshold;
		if (thresholdString.equals("_"))
			threshold = -1;
		else
			threshold = (byte) Integer.parseInt(thresholdString);
		int min;
		if (minString.equals("_"))
			min = -1;
		else
			min = Integer.parseInt(minString);
		int max;
		if (maxString.equals("_"))
			max = -1;
		else
			max = Integer.parseInt(maxString);
		return new IntegrationAtom(componentName, threshold, min, max);
	}

	public static IntegrationExpression createAtom(String componentName,
			String thresholdString, String minString, String maxString,
			String distString) {
		byte threshold;
		if (thresholdString.equals("_"))
			threshold = -1;
		else
			threshold = (byte) Integer.parseInt(thresholdString);
		int min;
		if (minString.equals("_"))
			min = -1;
		else
			min = Integer.parseInt(minString);
		int max;
		if (maxString.equals("_"))
			max = -1;
		else
			max = Integer.parseInt(maxString);
		int distance;
		if (distString.equals("_"))
			distance = 1;
		else
			distance = Integer.parseInt(distString);

		return new IntegrationAtom(componentName, threshold, min, max, distance);
	}

	public static IntegrationExpression createAtom(
			IntegrationExpression expression) {
		return expression;
	}

	public static IntegrationExpression createConjunction(
			IntegrationExpression e1, IntegrationExpression e2) {
		return new IntegrationConjunction(e1, e2);
	}

	public static IntegrationExpression createDisjunction(
			IntegrationExpression e1, IntegrationExpression e2) {
		return new IntegrationDisjunction(e1, e2);
	}

	public static Set<String> getInvalidComponentSpecification(
			CompositionSpecificationDialog dialog,
			IntegrationExpression expression) {
		List<NodeInfo> listNodes = dialog.getNodeOrder();

		Map<String, NodeInfo> mapNameNode = new HashMap<String, NodeInfo>();
		for (NodeInfo node : listNodes)
			mapNameNode.put(node.getNodeID(), node);

		Set<String> invalidComponentNames = new HashSet<String>();
		traverseTreeSemanticVerification(invalidComponentNames, mapNameNode,
				expression);

		return invalidComponentNames;
	}

	private static void traverseTreeSemanticVerification(Set<String> invalid,
			Map<String, NodeInfo> mapNameNode,
			IntegrationExpression expression) {

		if (expression instanceof IntegrationOperation) {
			List<IntegrationExpression> listOperands = ((IntegrationOperation) expression)
					.getOperands();
			for (IntegrationExpression operand : listOperands)
				traverseTreeSemanticVerification(invalid, mapNameNode, operand);

		} else if (expression instanceof IntegrationAtom) {

			IntegrationAtom atom = (IntegrationAtom) expression;
			String componentName = atom.getComponentName();
			byte threshold = atom.getThreshold();
			int min = atom.getMinNeighbours();
			int max = atom.getMaxNeighbours();
			// TODO: deal with distance

			if (!mapNameNode.containsKey(componentName))
				invalid.add(componentName);
			else if (mapNameNode.get(componentName).isInput())
				invalid.add(componentName);
			else if (threshold > mapNameNode.get(componentName).getMax())
				invalid.add(componentName);
			else if (min > max && max >= 0)
				invalid.add(componentName);

		}
	}

	public static boolean evaluate(
			Map<NodeInfo, List<Integer>> argumentValues,
			IntegrationExpression expression) {
		Map<String, NodeInfo> mapNameNode = new HashMap<String, NodeInfo>();
		for (NodeInfo node : argumentValues.keySet())
			mapNameNode.put(node.getNodeID(), node);

		return traverseTreeEvaluate(argumentValues, mapNameNode, expression);

	}

	private static boolean traverseTreeEvaluate(
			Map<NodeInfo, List<Integer>> argumentValues,
			Map<String, NodeInfo> mapNameNode,
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
