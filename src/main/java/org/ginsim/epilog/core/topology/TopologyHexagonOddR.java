package org.ginsim.epilog.core.topology;

import java.awt.Polygon;
import java.util.HashSet;
import java.util.Set;

import org.ginsim.epilog.common.Tuple2D;

public class TopologyHexagonOddR extends TopologyHexagon {

	private int[] neighboursY = { -1, -1, 0, 1, 1, 0 };
	private int[][] neighboursX = { { 1, -1, 0, -1, 0, -1 }, // even Y
			{ 1, -1, 0, 1, 0, 1 } }; // odd Y

	public TopologyHexagonOddR(int maxX, int maxY, RollOver rollover) {
		this.maxX = maxX;
		this.maxY = maxY;
		this.rollover = rollover;
	}

	public String getDescription() {
		return "Hexagon-Odd-PointyTopped";
	}

	@Override
	public Set<Tuple2D<Integer>> getNeighbours(Tuple2D<Integer> elem,
			Set<Tuple2D<Integer>> setComplete) {
		Set<Tuple2D<Integer>> setN = new HashSet<Tuple2D<Integer>>();

		for (int k = 0; k < neighboursY.length; k++) {
			int i = elem.getX() + neighboursX[elem.getY() % 2][k];
			int j = elem.getY() + neighboursY[k];
			this.includeNeighbour(i, j, setN, setComplete);
		}
		return setN;
	}
	
	@Override
	public Topology clone() {
		return new TopologyHexagonOddR(this.maxX, this.maxY, this.rollover);
	}

	@Override
	public Polygon createNewPolygon(double radius, Tuple2D<Double> center) {
		Polygon hexagon = new Polygon();
		for (int i = 0; i < 6; i++) {
			double angle = 2 * Math.PI / 6 * (i + 0.5);
			double x_i = center.getX() + radius * Math.cos(angle);
			double y_i = center.getY() + radius * Math.sin(angle);
			hexagon.addPoint((int) x_i, (int) y_i);
		}
		return hexagon;
	}

	@Override
	public Tuple2D<Double> getPolygonCenter(double cellSize, int gridX,
			int gridY) {
		double incX = cellSize * SQRT3_2;
		double incY = cellSize;
		double x, y = incY + gridY * (3 * cellSize / 2);
		if (gridY % 2 == 0)
			x = incX + gridX * 2 * incX;
		else
			x = 2 * incX + gridX * 2 * incX;

		return new Tuple2D<Double>(x, y);
	}

	@Override
	public double computeBestRadius(int gridX, int gridY, double dimX,
			double dimY) {
		double radiusX = dimX / (SQRT3 * (gridX + 0.5));
		double radiuxY = dimY / (gridY * 1.5 + 0.5);
		return Math.min(radiusX, radiuxY);
	}

	@Override
	public Tuple2D<Integer> getSelectedCell(double radius, int mouseX,
			int mouseY) {
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
					if (xRest < (ax - sqW_2)) {
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

		return new Tuple2D<Integer>(xDiv, yDiv);
	}

}
