package pt.igc.nmd.composition;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;

import pt.igc.nmd.epilog.integrationgrammar.IntegrationFunctionSpecification;
import pt.igc.nmd.epilog.integrationgrammar.IntegrationFunctionSpecification.IntegrationAtom;
import pt.igc.nmd.epilog.integrationgrammar.IntegrationFunctionSpecification.IntegrationExpression;
import pt.igc.nmd.epilog.integrationgrammar.IntegrationFunctionSpecification.IntegrationOperation;

/**
 * The integration function and proper components associated to an input
 * components
 * 
 * @author Nuno D. Mendes
 */
public class RegulatoryIntegration {

	private IntegrationFunctionSpecification.IntegrationExpression expression = null;
	private Map<String, NodeInfo> mapNameNode = new HashMap<String, NodeInfo>();
	private Set<String> properComponentNames = new HashSet<String>();
	private List<NodeInfo> properComponents = new ArrayList<NodeInfo>();

	private LogicalModel model = null;

	public RegulatoryIntegration(IntegrationExpression expression,
			LogicalModel model) {

		this.model = model;
		this.expression = expression;
		traverseTree(expression);

		for (NodeInfo node : model.getNodeOrder())
			mapNameNode.put(node.getNodeID(), node);

		for (String name : properComponentNames)
			properComponents.add(mapNameNode.get(name));

	}

	public List<NodeInfo> getProperComponents() {
		return this.properComponents;
	}

	private void traverseTree(IntegrationExpression expression) {
		if (expression instanceof IntegrationFunctionSpecification.IntegrationOperation) {
			IntegrationOperation operation = (IntegrationOperation) expression;
			for (IntegrationExpression operand : operation.getOperands())
				traverseTree(operand);
		} else if (expression instanceof IntegrationFunctionSpecification.IntegrationAtom) {
			IntegrationAtom atom = (IntegrationAtom) expression;
			this.properComponentNames.add(atom.getComponentName());
		}
	}

}
