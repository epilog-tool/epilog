package org.ginsim.epilog.core.topology;

import java.awt.Polygon;
import java.util.Set;

import org.ginsim.epilog.Tuple2D;

public class TopologyHexagonOddR extends TopologyHexagon {

	private int[] neighboursX = { 0, 1, 1, 1, 0, -1 };
	private int[][] neighboursY = { { 1, -1, 0, 1, 1, 0 },
			{ -1, 1, 0, 1, 1, 0 } };

	public TopologyHexagonOddR(int maxX, int maxY, RollOver rollover) {
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
		return new TopologyHexagonOddR(this.maxX, this.maxY, this.rollover);
	}
	
	@Override
	public Polygon createNewPolygon(double radius, int gridX, int gridY) {
		Polygon hexagon = new Polygon();
		// TODO: simplify...
		double incX = radius * SQRT3_2;
		double incY = radius;
		double x, y = incY + gridY * (3 * radius / 2);
		if (gridY % 2 == 0)
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
		double radiusX = dimX / (SQRT3 * (gridX + 0.5));
		double radiuxY = dimY / (gridY * 1.5 + 0.5);
		return Math.min(radiusX, radiuxY);
	}
	
	@Override
	public Tuple2D getSelectedCell(double radius, int mouseX, int mouseY) {
		double sqH = radius * 1.5;
		double sqW_2 = radius * SQRT3_2;
		double sqW = 2 * sqW_2;
		// Y
		double y = mouseY;
		int yDiv = (int) (y / sqH);
		// X
		double x = mouseX - ((yDiv % 2 == 0) ? 0 : sqW_2);
		int xDiv = (int) (x / sqW);

		double xRest = x % sqW;
		double yRest = y % sqH;
		if (yRest < radius / 2) {
			// y = ax + b
			double ax = yRest * sqW / radius;
			
			if (yDiv % 2 == 0) {
				if (xRest < sqW_2) {
					if (xRest < (-ax + sqW_2)) {
						xDiv--;
						yDiv--;
					}
				} else {
					if (xRest > (ax + sqW_2)) {
						yDiv--;
					}
				}
			} else { // yDiv % 2 == 1
				if (xRest < 0) {
					if (xRest < (ax-sqW_2)) {
						xDiv--;
					} else {
						yDiv--;
					}
				} else if (xRest < sqW_2) {
					if (xRest < (-ax + sqW_2)) {
						yDiv--;
					}
				} else {
					if (xRest > (ax + sqW_2)) {
						yDiv--;
						xDiv++;
					}
				}
			}
		} else {
			if (yDiv % 2 == 1) {
				if (mouseX < sqW_2) {
					xDiv--;
				}
			}
		}
		
		return new Tuple2D(xDiv, yDiv);
	}
}
