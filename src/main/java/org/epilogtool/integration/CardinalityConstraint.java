package org.epilogtool.integration;

import org.antlr.runtime.RecognitionException;

public class CardinalityConstraint implements IntegrationFunctionExpression {
	private IntegrationSignalExpression expr;
	private int minCells;
	private int maxCells;

	public CardinalityConstraint(IntegrationSignalExpression expr, String minCells, String maxCells) throws RecognitionException {
		this.expr = expr;
		this.minCells = Integer.parseInt(minCells);
		this.maxCells = Integer.parseInt(maxCells);
		if (this.maxCells > -1 && this.minCells > this.maxCells) {
			throw new RecognitionException();
		}
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
