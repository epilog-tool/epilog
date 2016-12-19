package org.epilogtool.integration;

public class IntegrationFunctionNOT implements IntegrationFunctionExpression {
	private IntegrationFunctionExpression expr = null;

	public IntegrationFunctionNOT(IntegrationFunctionExpression expr) {
		this.expr = expr;
	}

	public IntegrationFunctionExpression getNegatedExpression() {
		return this.expr;
	}
}