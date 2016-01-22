package org.epilogtool.core.topology;

import java.util.HashSet;
import java.util.Set;

import org.epilogtool.common.Tuple2D;

public abstract class TopologyHexagon extends Topology {

	protected final double SQRT3 = Math.sqrt(3);
	protected final double SQRT3_2 = SQRT3 / 2;

	@Override
	public Set<Tuple2D<Integer>> getNeighbours(int x, int y, int minDist, int maxDist) {
		Set<Tuple2D<Integer>> setCompleteN = new HashSet<Tuple2D<Integer>>();
		setCompleteN.add(new Tuple2D<Integer>(x, y));
		Set<Tuple2D<Integer>> setRingN = new HashSet<Tuple2D<Integer>>(setCompleteN);
		Set<Tuple2D<Integer>> setMin = new HashSet<Tuple2D<Integer>>(setCompleteN);

		for (int i = 1; i <= maxDist; i++) {
			Set<Tuple2D<Integer>> tmpSetN = new HashSet<Tuple2D<Integer>>();
			Set<Tuple2D<Integer>> oldSetCompleteN = new HashSet<Tuple2D<Integer>>(setCompleteN);
			for (Tuple2D<Integer> tuple : setRingN) {
				Set<Tuple2D<Integer>> setNewN = this.getNeighbours(tuple, setRingN);
				tmpSetN.addAll(setNewN);
				setCompleteN.addAll(setNewN);
			}
			tmpSetN.removeAll(oldSetCompleteN);
			setRingN = new HashSet<Tuple2D<Integer>>(tmpSetN);
			if (i == (minDist - 1)) {
				setMin = new HashSet<Tuple2D<Integer>>(setCompleteN);
			}
		}
		setCompleteN.removeAll(setMin);
		return setCompleteN;
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
