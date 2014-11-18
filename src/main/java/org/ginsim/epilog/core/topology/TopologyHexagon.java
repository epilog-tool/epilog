package org.ginsim.epilog.core.topology;

import java.util.HashSet;
import java.util.Set;

import org.ginsim.epilog.common.Tuple2D;

public abstract class TopologyHexagon extends Topology {

	protected final double SQRT3 = Math.sqrt(3);
	protected final double SQRT3_2 = SQRT3 / 2;

	@Override
	public Set<Tuple2D<Integer>> getNeighbours(int x, int y, int minDist, int maxDist) {
		Set<Tuple2D<Integer>> setComplete = new HashSet<Tuple2D<Integer>>();
		setComplete.add(new Tuple2D<Integer>(x, y));
		Set<Tuple2D<Integer>> setN = new HashSet<Tuple2D<Integer>>(setComplete);
		Set<Tuple2D<Integer>> setMin = new HashSet<Tuple2D<Integer>>(setComplete);

		for (int i = 1; i <= maxDist; i++) {
			for (Tuple2D<Integer> tuple : setN) {
				setComplete.addAll(this.getNeighbours(tuple, setComplete));
			}
			if (i == (minDist - 1)) {
				setMin = new HashSet<Tuple2D<Integer>>(setComplete);
			}
		}
		setComplete.removeAll(setMin);

		return setComplete;
	}

	public abstract Set<Tuple2D<Integer>> getNeighbours(Tuple2D<Integer> elem,
			Set<Tuple2D<Integer>> setComplete);
	
	protected void includeNeighbour(int i, int j, Set<Tuple2D<Integer>> setN, Set<Tuple2D<Integer>> setComplete) {
		if (this.rollover != RollOver.VERTICAL && (j < 0 || j >= this.maxY))
			return;
		if (this.rollover != RollOver.HORIZONTAL
				&& (i < 0 || i >= this.maxX))
			return;
		if (this.rollover == RollOver.VERTICAL) {
			if (j < 0)
				j = (this.maxY - 1);
			else if (j >= this.maxY)
				j = 0;
		} else if (this.rollover == RollOver.HORIZONTAL) {
			if (i < 0)
				i = (this.maxX - 1);
			else if (i >= this.maxX)
				i = 0;
		}
		Tuple2D<Integer> temp = new Tuple2D<Integer>(i, j);
		if (!setComplete.contains(temp))
			setN.add(temp);
	}
}
