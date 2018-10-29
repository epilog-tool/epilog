package org.epilogtool.integration;

import java.util.Map;
import java.util.Set;

import org.epilogtool.common.Tuple2D;
import org.epilogtool.core.Epithelium;
import org.epilogtool.core.EpitheliumGrid;

public class CardinalityConstraint implements IntegrationFunctionExpression {
	private IntegrationSignalExpression expr;
	private int minCells;
	private int maxCells;

	public CardinalityConstraint(IntegrationSignalExpression expr, String minCells, String maxCells)
			throws RuntimeException {
		this.expr = expr;
		this.minCells = Integer.parseInt(minCells);
		this.maxCells = Integer.parseInt(maxCells);
		if (this.maxCells > -1 && this.minCells > this.maxCells) {
			throw new RuntimeException("Invalid number of cells");
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

	@Override
	public boolean evaluate(int x, int y, Epithelium epi, EpitheliumGrid epiGrid,
			Map<Tuple2D<Integer>, Map<Boolean, Set<Tuple2D<Integer>>>> relativeNeighboursCache) {
		if (this.minCells == 0 && this.maxCells == -1) {
			return true;
		}
		// Check SignalExpression
		Set<Tuple2D<Integer>> neighbours = this.expr.evaluate(x, y, epi, epiGrid, relativeNeighboursCache);

		if (this.minCells != -1 && neighbours.size() < this.minCells
				|| this.maxCells != -1 && neighbours.size() > this.maxCells) {
			return false;
		}
		return true;
	}

	@Override
	public Set<String> getRegulators() {
		return this.expr.getRegulators();
	}
}
