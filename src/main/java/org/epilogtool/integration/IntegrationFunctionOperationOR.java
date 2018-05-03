package org.epilogtool.integration;

public class IntegrationFunctionOperationOR extends IntegrationFunctionOperation {
	public IntegrationFunctionOperationOR(IntegrationFunctionExpression expr1, IntegrationFunctionExpression expr2) {
		super(expr1, expr2, LogicalOperator.OR);
	}
}