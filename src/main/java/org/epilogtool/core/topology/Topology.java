package org.epilogtool.core.topology;

import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.epilogtool.common.Tuple2D;

public abstract class Topology {
	protected int maxX;
	protected int maxY;
	protected RollOver rollover;

	public abstract String getDescription();

	public abstract Set<Tuple2D<Integer>> getPositionNeighbours(int x, int y,
			Set<Tuple2D<Integer>> setRelativeNeighbours);

	public int getX() {
		return this.maxX;
	}

	public void setX(int x) {
		this.maxX = x;
	}

	public int getY() {
		return this.maxY;
	}

	public void setY(int y) {
		this.maxY = y;
	}

	public RollOver getRollOver() {
		return this.rollover;
	}

	public void setRollOver(RollOver rollover) {
		this.rollover = rollover;
	}

	public List<Tuple2D<Integer>> instances2Tuples2D(String[] instances) {
		List<Tuple2D<Integer>> lTuples = new ArrayList<Tuple2D<Integer>>();
		for (String tmp : instances) {
			int i, j;
			if (tmp.contains("-")) {
				String[] saTmp = tmp.split("-");
				i = Integer.parseInt(saTmp[0]);
				j = Integer.parseInt(saTmp[1]);
			} else {
				i = j = Integer.parseInt(tmp);
			}
			lTuples.addAll(this.instance2Tuple2D(i, j));
		}
		return lTuples;
	}

	private List<Tuple2D<Integer>> instance2Tuple2D(int iStart, int iEnd) {
		List<Tuple2D<Integer>> list = new ArrayList<Tuple2D<Integer>>();
		for (int i = iStart; i <= iEnd; i++) {
			int x = i % this.maxX;
			int y = i / this.maxX;
			list.add(new Tuple2D<Integer>(x, y));
		}
		return list;
	}

	public abstract Topology clone();

	public abstract Tuple2D<Double> getPolygonCenter(double cellSize, int gridX, int gridY);

	public abstract Polygon createNewPolygon(double radius, Tuple2D<Double> center);

	public abstract double computeBestRadius(int gridX, int gridY, double dimX, double dimY);

	protected double dimXFix(double dimX) {
		return dimX - 3;
	}

	protected double dimYFix(double dimY) {
		return dimY - 3;
	}

	public abstract Tuple2D<Integer> getSelectedCell(double radius, int mouseX, int mouseY);

	public boolean equals(Object o) {
		Topology tOut = (Topology) o;
		return (this.getDescription().equals(tOut.getDescription()) && this.getX() == tOut.getX()
				&& this.getY() == tOut.getY());
	}

	public abstract boolean isEven(int x, int y);

	public abstract Set<Tuple2D<Integer>> getPositionNeighbours(int x, int y, int minDist, int maxDist);

	public Set<Tuple2D<Integer>> getPositionNeighbours(int x, int y, int exactDist) {
		return this.getPositionNeighbours(x, y, exactDist, exactDist);
	}

	public abstract Set<Tuple2D<Integer>> getRelativeNeighbours(boolean even, int minDist, int maxDist);
}
