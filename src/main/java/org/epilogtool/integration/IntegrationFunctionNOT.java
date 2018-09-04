package org.epilogtool.integration;

import java.util.Map;
import java.util.Set;

import org.epilogtool.common.Tuple2D;
import org.epilogtool.core.Epithelium;
import org.epilogtool.core.EpitheliumGrid;

public class IntegrationFunctionNOT implements IntegrationFunctionExpression {
	private IntegrationFunctionExpression expr = null;

	public IntegrationFunctionNOT(IntegrationFunctionExpression expr) {
		this.expr = expr;
	}

	public IntegrationFunctionExpression getNegatedExpression() {
		return this.expr;
	}
	
	@Override
	public boolean evaluate(int x, int y, Epithelium epi, EpitheliumGrid epiGrid,
			Map<Tuple2D<Integer>, Map<Boolean, Set<Tuple2D<Integer>>>> relativeNeighboursCache) {
		return !this.expr.evaluate(x, y, epi, epiGrid, relativeNeighboursCache);
	}

	@Override
	public Set<String> getRegulators() {
		return this.expr.getRegulators();
	}
}