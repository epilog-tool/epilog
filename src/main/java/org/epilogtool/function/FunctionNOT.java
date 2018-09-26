package org.epilogtool.function;

public class FunctionNOT implements FunctionExpression {
	private FunctionExpression expr = null;

	public FunctionNOT(FunctionExpression expr) {
		this.expr = expr;
	}

	public FunctionExpression getNegatedExpression() {
		return this.expr;
	}
}