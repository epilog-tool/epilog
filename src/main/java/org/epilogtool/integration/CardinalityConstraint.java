package org.epilogtool.integration;

public class CardinalityConstraint implements IntegrationFunctionExpression {
	private IntegrationSignalExpression expr;
	private int minCells;
	private int maxCells;

	public CardinalityConstraint(IntegrationSignalExpression expr, String minCells, String maxCells) {
		this.expr = expr;
		this.minCells = Integer.parseInt(minCells);
		this.maxCells = Integer.parseInt(maxCells);
	}

	public IntegrationSignalExpression getSignalExpr() {
		return expr;
	}

	public int getMinCells() {
		return minCells;
	}

	public int getMaxCells() {
		return maxCells;
	}
}
