package org.epilogtool.core.topology;

import java.awt.Polygon;
import java.util.HashSet;
import java.util.Set;

import org.epilogtool.common.Tuple2D;

public class TopologyHexagonEvenR extends TopologyHexagon {

	public TopologyHexagonEvenR(int maxX, int maxY, RollOver rollover) {
		this.maxX = maxX;
		this.maxY = maxY;
		this.rollover = rollover;
	}

	public String getDescription() {
		return "Hexagon-Even-PointyTopped";
	}

	@Override
	public Set<Tuple2D<Integer>> getNeighbours(int x, int y,
			int distance) {

		if (y % 2 == 0){
			return this.evenNeighboursAt(distance);
		} else {
			return this.oddNeighboursAt(distance);
		}
	}
	
	public Set<Tuple2D<Integer>> evenNeighboursAt(int distance) {
		
		Set<Tuple2D<Integer>> neighbours = new HashSet<Tuple2D<Integer>>();

		int aeXCoordinate = distance;
		int bdXCoordinate = distance;
		int cXCoordinate = (int) Math.ceil((float) distance/2);
		int fXCoordinate = (int) Math.ceil((float) -distance/2);
		
		for (int y = 1; y <= distance; y++){
			
			if (y % 2 == 0){
				aeXCoordinate = aeXCoordinate - 1;
			}

			//'a' region neighbours
			neighbours.add(new Tuple2D<Integer>(aeXCoordinate, -y));
			//'b' region neighbours
			neighbours.add(new Tuple2D<Integer>(bdXCoordinate, y-1));
			//'c' region neighbours
			neighbours.add(new Tuple2D<Integer>(cXCoordinate, distance));
			//'d' region neighbours
			neighbours.add(new Tuple2D<Integer>(-bdXCoordinate + 1, y));
			//'e' region neighbours
			neighbours.add(new Tuple2D<Integer>(-aeXCoordinate, -y+1));
			//'f' region neighbours
			neighbours.add(new Tuple2D<Integer>(fXCoordinate, -distance));
			
			cXCoordinate = cXCoordinate - 1;
			fXCoordinate = fXCoordinate + 1;
			
			if (y % 2 ==0) {
				bdXCoordinate = bdXCoordinate -1;
			}
		}
		return neighbours;
	}
	
	public Set<Tuple2D<Integer>> oddNeighboursAt(int distance){
		Set<Tuple2D<Integer>> neighbours = new HashSet<Tuple2D<Integer>>();
		
		int aeXCoordinate = distance;
		int bdXCoordinate = distance;
		int cXCoordinate = (int) Math.floor((float) distance/2);
		int fXCoordinate = (int) Math.floor((float) -distance/2);
		
		for (int y = 1; y <= distance; y++){
			
			if (y%2==0) {
				bdXCoordinate = bdXCoordinate - 1;
			}
			
			//'a' region neighbours
			neighbours.add(new Tuple2D<Integer>(aeXCoordinate-1, -y));
			//'b' region neighbours
			neighbours.add(new Tuple2D<Integer>(bdXCoordinate, y-1));
			//'c' region neighbours
			neighbours.add(new Tuple2D<Integer>(cXCoordinate, distance));
			//'d' region neighbours
			neighbours.add(new Tuple2D<Integer>(-bdXCoordinate, y));
			//'e' region neighbours
			neighbours.add(new Tuple2D<Integer>(-aeXCoordinate, -y+1));
			//'f' region neighbours
			neighbours.add(new Tuple2D<Integer>(fXCoordinate, -distance));
			
			cXCoordinate = cXCoordinate - 1;
			fXCoordinate = fXCoordinate + 1;
			
			if (y%2==0) {
				aeXCoordinate = aeXCoordinate - 1;
			}
		}
		return neighbours;
	}

	@Override
	public Topology clone() {
		return new TopologyHexagonEvenR(this.maxX, this.maxY, this.rollover);
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
		if (gridY % 2 == 1)
			x = incX + gridX * 2 * incX;
		else
			x = 2 * incX + gridX * 2 * incX;

		return new Tuple2D<Double>(x, y);
	}

	@Override
	public double computeBestRadius(int gridX, int gridY, double dimX,
			double dimY) {
		double radiusX = this.dimXFix(dimX) / (SQRT3 * (gridX + 0.5));
		double radiuxY = this.dimYFix(dimY) / (gridY * 1.5 + 0.5);
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
		double x = mouseX - ((yDiv % 2 == 1) ? 0 : sqW_2);
		int xDiv = (int) (x / sqW);

		double xRest = x % sqW;
		double yRest = y % sqH;
		if (yRest < radius / 2) {
			// y = ax + b
			double ax = yRest * sqH / radius;

			if (yDiv % 2 == 1) {
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
			} else { // yDiv % 2 == 0
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
			if (yDiv % 2 == 0) {
				if (mouseX < sqW_2) {
					xDiv--;
				}
			}
		}

		return new Tuple2D<Integer>(xDiv, yDiv);
	}
}
