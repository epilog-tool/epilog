package org.cellularasynchronyIntegration;


import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.colomoto.logicalmodel.NodeInfo;

import org.cellularasynchronyIntegration.IntegrationFunctionSpecification.IntegrationAtom;
import org.cellularasynchronyIntegration.IntegrationFunctionSpecification.IntegrationExpression;
import org.cellularasynchronyIntegration.IntegrationFunctionSpecification.IntegrationOperation;

public class IntegrationFunctionVerification {

	private IntegrationExpression expression = null;
	private CompositionContext context = null;
	private Set<String> invalidSpecifications = new HashSet<String>();

	public IntegrationFunctionVerification(IntegrationExpression expression,
			CompositionContext context) {
		this.expression = expression;
		this.context = context;
		init();
	}

	private void init() {
		List<NodeInfo> listNodes = context.getLowLevelComponents();
		Map<String, NodeInfo> mapNameNode = new HashMap<String, NodeInfo>();
		for (NodeInfo node : listNodes)
			mapNameNode.put(node.getNodeID(), node);

		traverseTreeSemanticVerification(this.invalidSpecifications,
				mapNameNode, expression);
	}

	public Set<String> getInvalidComponentSpecification() {
		return this.invalidSpecifications;
	}

	private void traverseTreeSemanticVerification(Set<String> invalid,
			Map<String, NodeInfo> mapNameNode, IntegrationExpression expression) {

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

}
