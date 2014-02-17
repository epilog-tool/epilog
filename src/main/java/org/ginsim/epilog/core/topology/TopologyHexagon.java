package org.ginsim.epilog.core.topology;

import java.util.ArrayList;
import java.util.List;

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
	public List<Tuple2D> getNeighbours(int x, int y, int minDist, int maxDist) {
		List<Tuple2D> l = new ArrayList<Tuple2D>();

		for (int k = 0; k < neighboursY.length; k++) {
			int i = x + neighboursX[y % 2][k];
			int j = y + neighboursY[k];
			l.add(new Tuple2D(i, j));
		}
		// TODO: distance > 1
		// TODO: getNeighboursAtDistance1
		return l;
	}
}
