package org.ginsim.epilog.core.topology;

import java.awt.Polygon;
import java.util.Set;

import org.ginsim.epilog.Tuple2D;

public class TopologyHexagonEvenR extends TopologyHexagon {

	private int[] neighboursX = { -1, 0, 1, 0, -1, -1 };
	private int[][] neighboursY = { { -1, -1, 0, 1, 1, 0 },
			{ -1, 1, 0, 1, 1, 0 } };

	public TopologyHexagonEvenR(int maxX, int maxY, RollOver rollover) {
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
		return new TopologyHexagonEvenR(this.maxX, this.maxY, this.rollover);
	}
	
	@Override
	public Polygon createNewPolygon(double radius, int gridX, int gridY) {
		Polygon hexagon = new Polygon();
		// TODO: simplify...
		double incX = radius * Math.sqrt(3) / 2;
		double incY = radius;
		double x, y = incY + gridY * (3 * radius / 2);
		if (gridY % 2 == 1)
			x = incX + gridX * 2 * incX;
		else
			x = 2 * incX + gridX * 2 * incX;

		for (int i = 0; i < 6; i++) {
			double angle = 2 * Math.PI / 6 * (i+0.5);
			double x_i = x + radius * Math.cos(angle);
			double y_i = y + radius * Math.sin(angle);
			hexagon.addPoint((int) x_i, (int) y_i);
		}
		return hexagon;
	}
	
	@Override
	public double computeBestRadius(int gridX, int gridY, double dimX,
			double dimY) {
		double radiusX = dimX / (Math.sqrt(3) * (gridX + 0.5));
		double radiuxY = dimY / (gridY * 1.5 + 2);
		return Math.min(radiusX, radiuxY);
	}
}