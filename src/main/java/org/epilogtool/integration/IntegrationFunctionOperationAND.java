package org.epilogtool.integration;

public class IntegrationFunctionOperationAND extends IntegrationFunctionOperation {
	public IntegrationFunctionOperationAND(IntegrationFunctionExpression expr1, IntegrationFunctionExpression expr2) {
		super(expr1, expr2, LogicalOperator.AND);
	}
}