package org.ginsim.epilog.core.topology;

import java.awt.Polygon;
import java.util.HashSet;
import java.util.Set;

import org.ginsim.epilog.common.Tuple2D;

public class TopologyHexagonOddQ extends TopologyHexagon {

	private int[] neighboursX = { -1, 0, 1, 1, 0, -1 };
	private int[][] neighboursY = { { -1, -1, -1, 0, 1, 0 },
			{ 0, -1, 0, 1, 1, 1 } };

	public TopologyHexagonOddQ(int maxX, int maxY, RollOver rollover) {
		this.maxX = maxX;
		this.maxY = maxY;
		this.rollover = rollover;
	}

	public String getDescription() {
		return "Hexagon-Odd-FlatTopped";
	}

	public Set<Tuple2D<Integer>> getNeighbours(Tuple2D<Integer> elem,
			Set<Tuple2D<Integer>> setComplete) {
		Set<Tuple2D<Integer>> setN = new HashSet<Tuple2D<Integer>>();

		for (int k = 0; k < neighboursX.length; k++) {
			int i = elem.getX() + neighboursX[k];
			int j = elem.getY() + neighboursY[elem.getX() % 2][k];
			this.includeNeighbour(i, j, setN, setComplete);
		}
		return setN;
	}

	@Override
	public Topology clone() {
		return new TopologyHexagonOddQ(this.maxX, this.maxY, this.rollover);
	}

	@Override
	public Polygon createNewPolygon(double radius, Tuple2D<Double> center) {
		Polygon hexagon = new Polygon();
		for (int i = 0; i < 6; i++) {
			double angle = 2 * Math.PI / 6 * i;
			double x_i = center.getX() + radius * Math.cos(angle);
			double y_i = center.getY() + radius * Math.sin(angle);
			hexagon.addPoint((int) x_i, (int) y_i);
		}
		return hexagon;
	}

	@Override
	public Tuple2D<Double> getPolygonCenter(double cellSize, int gridX,
			int gridY) {
		double incX = cellSize;
		double incY = cellSize * SQRT3_2;
		double y, x = incX + gridX * (3 * cellSize / 2);
		if (gridX % 2 == 0)
			y = incY + gridY * 2 * incY;
		else
			y = 2 * incY + gridY * 2 * incY;

		return new Tuple2D<Double>(x, y);
	}

	@Override
	public double computeBestRadius(int gridX, int gridY, double dimX,
			double dimY) {
		double radiusX = this.dimXFix(dimX) / (gridX * 1.5 + 0.5);
		double radiuxY = this.dimYFix(dimY) / (SQRT3 * (gridY + 0.5));
		return Math.min(radiusX, radiuxY);
	}

	@Override
	public Tuple2D<Integer> getSelectedCell(double radius, int mouseX,
			int mouseY) {
		double sqW = radius * 1.5;
		double sqH_2 = radius * SQRT3_2;
		double sqH = 2 * sqH_2;
		// X
		double x = mouseX;
		int xDiv = (int) (x / sqW);
		// Y
		double y = mouseY - ((xDiv % 2 == 0) ? 0 : sqH_2);
		int yDiv = (int) (y / sqH);

		double xRest = x % sqW;
		double yRest = y % sqH;
		if (xRest < radius / 2) {
			// y = ax + b
			double ax = xRest * sqH / radius;

			if (xDiv % 2 == 0) {
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
			} else { // xDiv % 2 == 1
				if (yRest < 0) {
					if (yRest < (ax - sqH_2)) {
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
			if (xDiv % 2 == 1) {
				if (mouseY < sqH_2) {
					yDiv--;
				}
			}
		}

		return new Tuple2D<Integer>(xDiv, yDiv);
	}
}
