package org.ginsim.epilog.core.topology;

import java.util.Set;

public abstract class Topology {
	protected int maxX;
	protected int maxY;
	protected RollOver rollover;

	public abstract Set<Tuple2D> getNeighbours(int x, int y, int minDist,
			int maxDist);
	
	public RollOver getRollOver() {
		return this.rollover;
	}
}
