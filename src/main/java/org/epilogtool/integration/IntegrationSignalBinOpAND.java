package org.epilogtool.integration;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.epilogtool.common.Tuple2D;
import org.epilogtool.core.Epithelium;
import org.epilogtool.core.EpitheliumGrid;

public class IntegrationSignalBinOpAND extends IntegrationSignalBinOp {
	public IntegrationSignalBinOpAND(IntegrationSignalExpression expr1, IntegrationSignalExpression expr2) {
		super(expr1, expr2);
	}

	@Override
	public Set<Tuple2D<Integer>> evaluate(int x, int y, Epithelium epi, EpitheliumGrid epiGrid,
			Map<Tuple2D<Integer>, Map<Boolean, Set<Tuple2D<Integer>>>> relativeNeighboursCache) {
		Set<Tuple2D<Integer>> result = new HashSet<Tuple2D<Integer>>();
		if (this.expr1 != null) {
			result.addAll(this.expr1.evaluate(x, y, epi, epiGrid, relativeNeighboursCache));
		}
		if (this.expr2 != null) {
			result.addAll(this.expr2.evaluate(x, y, epi, epiGrid, relativeNeighboursCache));
		}
		return result;
	}
}