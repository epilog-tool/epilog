package org.ginsim.epilog.core.topology;

import java.awt.Polygon;
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

	@Override
	public Polygon createNewPolygon(double radius, int gridX, int gridY) {
		Polygon hexagon = new Polygon();
		// TODO: simplify...
		double incX = radius;
		double incY = radius * Math.sqrt(3) / 2;
		double y, x = incX + gridX * (3 * radius / 2);
		if (gridX % 2 == 0)
			y = incY + gridY * 2 * incY;
		else
			y = 2 * incY + gridY * 2 * incY;

		for (int i = 0; i < 6; i++) {
			double angle = 2 * Math.PI / 6 * i;
			double x_i = x + radius * Math.cos(angle);
			double y_i = y + radius * Math.sin(angle);
			hexagon.addPoint((int) x_i, (int) y_i);
		}
		return hexagon;
	}
	
	@Override
	public double computeBestRadius(int gridX, int gridY, double dimX,
			double dimY) {
		double radiusX = dimX / (gridX * 1.5 + 2);
		double radiuxY = dimY / (Math.sqrt(3) * (gridY + 0.5));
		return Math.min(radiusX, radiuxY);
	}
}
