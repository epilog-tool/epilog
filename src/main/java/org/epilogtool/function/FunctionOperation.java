package org.epilogtool.function;

import java.util.ArrayList;
import java.util.List;

public abstract class FunctionOperation implements FunctionExpression {
	private FunctionExpression expr1;
	private FunctionExpression expr2;

	public FunctionOperation(FunctionExpression expr1, FunctionExpression expr2) {
		this.expr1 = expr1;
		this.expr2 = expr2;
	}

	public List<FunctionExpression> getOperands() {
		List<FunctionExpression> listExpression = new ArrayList<FunctionExpression>();
		listExpression.add(this.expr1);
		listExpression.add(this.expr2);

		return listExpression;
	}
}
