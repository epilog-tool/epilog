package org.epilogtool.integration;

import java.util.Map;
import java.util.Set;

import org.epilogtool.common.Tuple2D;
import org.epilogtool.core.Epithelium;
import org.epilogtool.core.EpitheliumGrid;

public class IntegrationFunctionBinOpAND extends IntegrationFunctionBinOp {
	public IntegrationFunctionBinOpAND(IntegrationFunctionExpression expr1, IntegrationFunctionExpression expr2) {
		super(expr1, expr2);
	}

	@Override
	public boolean evaluate(int x, int y, Epithelium epi, EpitheliumGrid epiGrid,
			Map<Tuple2D<Integer>, Map<Boolean, Set<Tuple2D<Integer>>>> relativeNeighboursCache) {
		for (IntegrationFunctionExpression operand : this.getOperands()) {
			if (operand == null)
				continue;
			else if (!operand.evaluate(x, y, epi, epiGrid, relativeNeighboursCache)) {
				return false;
			}
		}
		return true;
	}
}