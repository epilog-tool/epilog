package org.epilogtool.integration;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.epilogtool.common.Tuple2D;
import org.epilogtool.core.Epithelium;
import org.epilogtool.core.EpitheliumGrid;

/**
 * @author Pedro T. Monteiro
 */
public class IFEvaluation {

	private EpitheliumGrid neighboursGrid;
	private Map<Tuple2D<Integer>, Map<Boolean, Set<Tuple2D<Integer>>>> relativeNeighboursCache;
	private Epithelium epithelium;

	public IFEvaluation(EpitheliumGrid neighboursGrid, Epithelium epithelium) {
		this.neighboursGrid = neighboursGrid;
		this.epithelium = epithelium;
		this.relativeNeighboursCache = new HashMap<Tuple2D<Integer>, Map<Boolean, Set<Tuple2D<Integer>>>>();
	}

	public boolean evaluate(int x, int y, IntegrationFunctionExpression expr) {
		return expr.evaluate(x, y, this.epithelium, this.neighboursGrid, this.relativeNeighboursCache);
	}

}
