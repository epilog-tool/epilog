package org.ginsim.epilog.core.topology;

import java.util.Set;

import org.ginsim.epilog.Tuple2D;

public class TopologyHexagonOddQ extends TopologyHexagon {

	private int[] neighboursX = { -1, -1, 0, 1, 1, 0 };
	private int[][] neighboursY = { { -1, 0, 1, 0, -1, -1 },
			{ 0, 1, 1, 1, 0, -1 } };

	public TopologyHexagonOddQ(int maxX, int maxY, RollOver rollover) {
		this.maxX = maxX;
		this.maxY = maxY;
		this.rollover = rollover;
	}

	public Set<Tuple2D> getNeighbours(Tuple2D elem, Set<Tuple2D> setComplete) {
		return getNeighbours(this.neighboursX, this.neighboursY, elem,
				setComplete);
	}

	@Override
	public Topology clone() {
		return new TopologyHexagonOddQ(this.maxX, this.maxY, this.rollover);
	}
}
