package org.epilogtool.integration;

import java.util.ArrayList;
import java.util.List;

public abstract class IntegrationSignalOperation implements IntegrationSignalExpression {
	private LogicalOperator operator;
	private IntegrationSignalExpression expr1;
	private IntegrationSignalExpression expr2;

	public IntegrationSignalOperation(IntegrationSignalExpression expr1, IntegrationSignalExpression expr2,
			LogicalOperator operator) {
		this.expr1 = expr1;
		this.expr2 = expr2;
		this.operator = operator;
	}

	public LogicalOperator getOperation() {
		return this.operator;
	}

	public List<IntegrationSignalExpression> getOperands() {
		List<IntegrationSignalExpression> listExpression = new ArrayList<IntegrationSignalExpression>();
		listExpression.add(this.expr1);
		listExpression.add(this.expr2);

		return listExpression;
	}
}
