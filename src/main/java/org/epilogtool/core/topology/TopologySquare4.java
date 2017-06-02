package org.epilogtool.core.topology;

import java.awt.Polygon;
import java.util.HashSet;
import java.util.Set;

import org.epilogtool.common.Tuple2D;

public class TopologySquare4 extends TopologySquare {

	public TopologySquare4(int maxX, int maxY, RollOver rollover) {
		this.maxX = maxX;
		this.maxY = maxY;
		this.rollover = rollover;
	}

	public String getDescription() {
		return "Square-4-neighbours";
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
		return neighbours;
	}
	
	public Set<Tuple2D<Integer>> oddRelativeNeighboursAt(int distance){
		return this.evenRelativeNeighboursAt(distance);
	}


	@Override
	public Topology clone() {
		return new TopologySquare4(this.maxX, this.maxY, this.rollover);
	}

	@Override
	public Polygon createNewPolygon(double radius, Tuple2D<Double> center) {
		Polygon polygon = new Polygon();
		double angle = Math.PI / 2;
		for (int i = 0; i < 4; i++) {
			double x_i = center.getX() + radius * Math.cos(angle);
			double y_i = center.getY() + radius * Math.sin(angle);
			polygon.addPoint((int) x_i, (int) y_i);
		}
		return polygon;
	}

	@Override
	public Tuple2D<Double> getPolygonCenter(double cellSize, int gridX,
			int gridY) {
		double x = cellSize * gridX + cellSize / 2;
		double y = cellSize * gridY + cellSize / 2;
		return new Tuple2D<Double>(x, y);
	}

	@Override
	public double computeBestRadius(int gridX, int gridY, double dimX,
			double dimY) {
		double radiusX = this.dimXFix(dimX) / gridX;
		double radiuxY = this.dimYFix(dimY) / gridY;
		return Math.min(radiusX, radiuxY);
	}

	@Override
	public Tuple2D<Integer> getSelectedCell(double radius, int mouseX,
			int mouseY) {
		// X
		int xDiv = (int) (mouseX / radius);
		// Y
		int yDiv = (int) (mouseY / radius);
		return new Tuple2D<Integer>(xDiv, yDiv);
	}

	@Override
	public boolean isEven(int x, int y) {
		return true;
	}
}
