package org.ginsim.epilog.core.topology;

import java.util.List;

public abstract class Topology {
	protected int maxX;
	protected int maxY;
	protected RollOver rollover;

	public abstract List<Tuple2D> getNeighbours(int x, int y, int minDist,
			int maxDist);
}
