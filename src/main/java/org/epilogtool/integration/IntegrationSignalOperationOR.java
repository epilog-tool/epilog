package org.epilogtool.integration;

public class IntegrationSignalOperationOR extends IntegrationSignalOperation {
	public IntegrationSignalOperationOR(IntegrationSignalExpression expr1, IntegrationSignalExpression expr2) {
		super(expr1, expr2, LogicalOperator.OR);
	}
}