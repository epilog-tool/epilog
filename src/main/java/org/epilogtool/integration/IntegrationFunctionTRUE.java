package org.epilogtool.integration;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.epilogtool.common.Tuple2D;
import org.epilogtool.core.Epithelium;
import org.epilogtool.core.EpitheliumGrid;

public class IntegrationFunctionTRUE implements IntegrationFunctionExpression {
	public IntegrationFunctionTRUE() {
	}
	
	@Override
	public boolean evaluate(int x, int y, Epithelium epi, EpitheliumGrid epiGrid,
			Map<Tuple2D<Integer>, Map<Boolean, Set<Tuple2D<Integer>>>> relativeNeighboursCache) {
		return true;
	}
	
	@Override
	public Set<String> getRegulators() {
		return new HashSet<String>();
	}
}