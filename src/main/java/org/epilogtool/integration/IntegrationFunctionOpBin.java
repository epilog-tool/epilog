package org.epilogtool.integration;

import java.util.ArrayList;
import java.util.List;

public abstract class IntegrationFunctionOperation implements IntegrationFunctionExpression {
	private LogicalOperator operator;
	private IntegrationFunctionExpression expr1;
	private IntegrationFunctionExpression expr2;

	public IntegrationFunctionOperation(IntegrationFunctionExpression expr1, IntegrationFunctionExpression expr2,
			LogicalOperator operator) {
		this.expr1 = expr1;
		this.expr2 = expr2;
		this.operator = operator;
	}

	public LogicalOperator getOperation() {
		return this.operator;
	}

	public List<IntegrationFunctionExpression> getOperands() {
		List<IntegrationFunctionExpression> listExpression = new ArrayList<IntegrationFunctionExpression>();
		listExpression.add(this.expr1);
		listExpression.add(this.expr2);

		return listExpression;
	}
}
