package org.ginsim.epilog.core.topology;

import java.awt.Polygon;
import java.util.Set;

import org.ginsim.epilog.common.Tuple2D;

public class TopologyHexagonEvenQ extends TopologyHexagon {

	private int[] neighboursX = { 0, 1, 1, 0, -1, -1 };
	private int[][] neighboursY = { { -1, 0, 1, 1, 1, 0 },
			{ -1, -1, 0, 1, 0, -1 } };

	public TopologyHexagonEvenQ(int maxX, int maxY, RollOver rollover) {
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
		return new TopologyHexagonEvenQ(this.maxX, this.maxY, this.rollover);
	}

	@Override
	public Polygon createNewPolygon(double radius, int gridX, int gridY) {
		Polygon hexagon = new Polygon();
		// TODO: simplify...
		double incX = radius;
		double incY = radius * SQRT3_2;
		double y, x = incX + gridX * (3 * radius / 2);
		if (gridX % 2 == 1)
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
		double radiusX = dimX / (gridX * 1.5 + 0.5);
		double radiuxY = dimY / (SQRT3 * (gridY + 0.5));
		return Math.min(radiusX, radiuxY);
	}
	
	@Override
	public Tuple2D getSelectedCell(double radius, int mouseX, int mouseY) {
		double sqW = radius * 1.5;
		double sqH_2 = radius * SQRT3_2;
		double sqH = 2 * sqH_2;
		// X
		double x = mouseX;
		int xDiv = (int) (x / sqW);
		// Y
		double y = mouseY - ((xDiv % 2 == 1) ? 0 : sqH_2);
		int yDiv = (int) (y / sqH);

		double xRest = x % sqW;
		double yRest = y % sqH;
		if (xRest < radius / 2) {
			// y = ax + b
			double ax = xRest * sqH / radius;
			
			if (xDiv % 2 == 1) {
				if (yRest < sqH_2) {
					if (yRest < (-ax + sqH_2)) {
						xDiv--;
						yDiv--;
					}
				} else {
					if (yRest > (ax + sqH_2)) {
						xDiv--;
					}
				}
			} else { // xDiv % 2 == 0
				if (yRest < 0) {
					if (yRest < (ax-sqH_2)) {
						yDiv--;
					} else {
						xDiv--;
					}
				} else if (yRest < sqH_2) {
					if (yRest < (-ax + sqH_2)) {
						xDiv--;
					}
				} else {
					if (yRest > (ax + sqH_2)) {
						xDiv--;
						yDiv++;
					}
				}
			}
		} else {
			if (xDiv % 2 == 0) {
				if (mouseY < sqH_2) {
					yDiv--;
				}
			}
		}
		
		return new Tuple2D(xDiv, yDiv);
	}
}
