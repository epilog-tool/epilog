package org.epilogtool.cellularevent;

import java.util.List;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;

public class CEEvaluation {

	public CEEvaluation() {
	}

	public boolean evaluate(LogicalModel m, byte[] state, CellularEventExpression expression) {
		return traverseTreeCEEvaluate(m, state, expression);
	}

	private boolean traverseTreeCEEvaluate(LogicalModel m, byte[] state, CellularEventExpression expression) {
		// TODO: get rid of instanceof and call a polimorfic evaluate() on
		// subclasses

		if (expression instanceof CellularEventOperation) {
			List<CellularEventExpression> listOperands = ((CellularEventOperation) expression)
					.getOperands();

			LogicalOperator operator = ((CellularEventOperation) expression).getOperation();

			switch (operator) {
			case AND:
				for (CellularEventExpression operand : listOperands) {
					if (operand == null)
						continue;
					else if (!traverseTreeCEEvaluate(m, state, operand)) {
						return false;
					}
				}
				return true;
			case OR:
				for (CellularEventExpression operand : listOperands) {
					if (operand == null)
						continue;
					else if (traverseTreeCEEvaluate(m, state, operand)) {
						return true;
					}
				}
				return false;
			}

		} else if (expression instanceof CellularEventNOT) {
			return !traverseTreeCEEvaluate(m, state, ((CellularEventNOT) expression).getNegatedExpression());

		} else if (expression instanceof CellularEventNode) {
			CellularEventNode node = (CellularEventNode) expression;
			String nodeID = node.getComponentName();
			List<NodeInfo> nodeOrder = m.getNodeOrder();
			for (int i = 0; i < nodeOrder.size(); i++) {
				if (nodeOrder.get(i).getNodeID().equals(nodeID)) {
					if (state[i] >= node.getValue()) {
						return true;
					}
					break;
				}
			}
			return false;
		}
		// TODO: this last return should not be here.
		return false;
	}
}
