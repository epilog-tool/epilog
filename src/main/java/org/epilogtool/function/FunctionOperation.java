package org.epilogtool.function;

public abstract class FunctionOperation implements FunctionExpression {
	private FunctionExpression expr1;
	private FunctionExpression expr2;

	public FunctionOperation(FunctionExpression expr1, FunctionExpression expr2) {
		this.expr1 = expr1;
		this.expr2 = expr2;
	}

	public FunctionExpression getExprLeft() {
		return this.expr1;
	}
	public FunctionExpression getExprRight() {
		return this.expr2;
	}
}
