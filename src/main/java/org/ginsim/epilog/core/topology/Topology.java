package org.ginsim.epilog.core.topology;

import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.ginsim.epilog.common.Tuple2D;

public abstract class Topology {
	protected int maxX;
	protected int maxY;
	protected RollOver rollover;

	public abstract String getDescription();

	public abstract Set<Tuple2D> getNeighbours(int x, int y, int minDist,
			int maxDist);

	public int getX() {
		return this.maxX;
	}

	public int getY() {
		return this.maxY;
	}

	public RollOver getRollOver() {
		return this.rollover;
	}

	public void setRollOver(RollOver rollover) {
		this.rollover = rollover;
	}

	public List<Tuple2D> instances2Tuples2D(String[] instances) {
		List<Tuple2D> lTuples = new ArrayList<Tuple2D>();
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

	private List<Tuple2D> instance2Tuple2D(int iStart, int iEnd) {
		List<Tuple2D> list = new ArrayList<Tuple2D>();
		for (int i = iStart; i <= iEnd; i++) {
			int x = i % this.maxX;
			int y = i / this.maxX;
			list.add(new Tuple2D(x, y));
		}
		return list;
	}

	public abstract Topology clone();

	public abstract Polygon createNewPolygon(double radius, int gridX, int gridY);

	public abstract double computeBestRadius(int gridX, int gridY, double dimX,
			double dimY);

	public abstract Tuple2D getSelectedCell(double radius, int mouseX,
			int mouseY);
}
