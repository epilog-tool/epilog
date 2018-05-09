package org.epilogtool.core.topology;

import java.awt.Polygon;
import java.util.HashSet;
import java.util.Set;

import org.epilogtool.common.Tuple2D;

public class TopologyHexagonEvenQ extends TopologyHexagon {

	public TopologyHexagonEvenQ(int maxX, int maxY, RollOver rollover) {
		this.maxX = maxX;
		this.maxY = maxY;
		this.rollover = rollover;
	}

	public String getDescription() {
		return "Hexagon-Even-FlatTopped";
	}
	
	public Set<Tuple2D<Integer>> evenRelativeNeighboursAt(int distance){
		Set<Tuple2D<Integer>> neighbours = new HashSet<Tuple2D<Integer>>();
	
		int bfYCoordinate = distance;
		int ceYCoordinate = distance;
		int aYCoordinate = (int) Math.ceil((float) -distance/2);
		int dYCoordinate = (int) Math.ceil((float) distance/2);
	
		for (int x = 1; x <= distance; x++){
		
			if (x%2==0) {
				bfYCoordinate = bfYCoordinate - 1;
			}
		
			//'a' region neighbours
			neighbours.add(new Tuple2D<Integer>(distance, aYCoordinate));
			//'b' region neighbours
			neighbours.add(new Tuple2D<Integer>(x, bfYCoordinate));
			//'c' region neighbours
			neighbours.add(new Tuple2D<Integer>(-(x-1), ceYCoordinate));
			//'d' region neighbours
			neighbours.add(new Tuple2D<Integer>(-distance, dYCoordinate));
			//'e' region neighbours
			neighbours.add(new Tuple2D<Integer>(-x, -(ceYCoordinate-1)));
			//'f' region neighbours
			neighbours.add(new Tuple2D<Integer>(x-1, -bfYCoordinate));
		
			aYCoordinate = aYCoordinate + 1;
			dYCoordinate = dYCoordinate - 1;
		
			if (x%2==0) {
				ceYCoordinate = ceYCoordinate - 1;
			}
		}
//		System.out.println("TopologyHexagonEvenQ: " + neighbours);
		return neighbours;
	}
	
	public Set<Tuple2D<Integer>> oddRelativeNeighboursAt(int distance){
		Set<Tuple2D<Integer>> neighbours = new HashSet<Tuple2D<Integer>>();
	
		int bfYCoordinate = distance;
		int ceYCoordinate = distance;
		int aYCoordinate = (int) Math.floor((float) -distance/2);
		int dYCoordinate = (int) Math.floor((float) distance/2);
	
		for (int x = 1; x <= distance; x++){
		
			if (x%2==0) {
				ceYCoordinate = ceYCoordinate - 1;
			}
		
			//'a' region neighbours
			neighbours.add(new Tuple2D<Integer>(distance, aYCoordinate));
			//'b' region neighbours
			neighbours.add(new Tuple2D<Integer>(x, bfYCoordinate-1));
			//'c' region neighbours
			neighbours.add(new Tuple2D<Integer>(-(x-1), ceYCoordinate));
			//'d' region neighbours
			neighbours.add(new Tuple2D<Integer>(-distance, dYCoordinate));
			//'e' region neighbours
			neighbours.add(new Tuple2D<Integer>(-x, -ceYCoordinate));
			//'f' region neighbours
			neighbours.add(new Tuple2D<Integer>(x-1, -bfYCoordinate));
		
			aYCoordinate = aYCoordinate + 1;
			dYCoordinate = dYCoordinate - 1;
		
			if (x%2==0) {
				bfYCoordinate = bfYCoordinate - 1;
			}
		}
		return neighbours;
	}


	@Override
	public Topology clone() {
		return new TopologyHexagonEvenQ(this.maxX, this.maxY, this.rollover);
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
		if (gridX % 2 == 1)
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
			if (xDiv % 2 == 0) {
				if (mouseY < sqH_2) {
					yDiv--;
				}
			}
		}

		return new Tuple2D<Integer>(xDiv, yDiv);
	}

	@Override
	public boolean isEven(int x, int y) {
		return x%2==0? true:false;
	}
}
