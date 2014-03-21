package org.ginsim.epilog.core.topology;

import java.util.HashSet;
import java.util.Set;

import org.ginsim.epilog.Tuple2D;

public class TopologyHexagon extends Topology {
	private int[][] neighboursX = { { -1, 1, 0, 0, -1, -1 },
			{ -1, 1, 0, 0, 1, 1 } };
	private int[] neighboursY = { 0, 0, -1, 1, -1, 1 };

	public TopologyHexagon(int maxX, int maxY, RollOver rollover) {
		this.maxX = maxX;
		this.maxY = maxY;
		this.rollover = rollover;
	}

	@Override
	public Set<Tuple2D> getNeighbours(int x, int y, int minDist, int maxDist) {
		Set<Tuple2D> setComplete = new HashSet<Tuple2D>();
		setComplete.add(new Tuple2D(x, y));
		Set<Tuple2D> setN = new HashSet<Tuple2D>(setComplete);
		Set<Tuple2D> setMin = new HashSet<Tuple2D>(setComplete);

		for (int i = 1; i <= maxDist; i++) {
			for (Tuple2D tuple : setN) {
				setComplete.addAll(this.getNeighbours(tuple, setComplete));
			}
			if (i == (minDist-1)) {
				setMin = new HashSet<Tuple2D>(setComplete);
			}
		}
		setComplete.removeAll(setMin);
		return setComplete;
	}

	private Set<Tuple2D> getNeighbours(Tuple2D elem, Set<Tuple2D> setComplete) {
		Set<Tuple2D> setN = new HashSet<Tuple2D>();

		for (int k = 0; k < neighboursY.length; k++) {
			int i = elem.getX() + neighboursX[elem.getY() % 2][k];
			int j = elem.getY() + neighboursY[k];
			if (this.rollover != RollOver.VERTICAL && (j < 0 || j > this.maxY))
				continue;
			if (this.rollover != RollOver.HORIZONTAL
					&& (i < 0 || i > this.maxX))
				continue;
			if (this.rollover == RollOver.VERTICAL) {
				if (j < 0)
					j = this.maxY;
				else if (j > this.maxY)
					j = 0;
			} else if (this.rollover == RollOver.HORIZONTAL) {
				if (i < 0)
					i = this.maxX;
				else if (i > this.maxX)
					i = 0;
			}
			Tuple2D temp = new Tuple2D(i, j);
			if (!setComplete.contains(temp))
				setN.add(temp);
		}
		return setN;
	}
}
