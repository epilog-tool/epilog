package org.epilogtool.core.topology;

import java.util.HashSet;
import java.util.Set;

import org.epilogtool.common.Tuple2D;

public abstract class TopologyHexagon extends Topology {

	protected final double SQRT3 = Math.sqrt(3);
	protected final double SQRT3_2 = SQRT3 / 2;

	@Override
	public Set<Tuple2D<Integer>> getPositionNeighbours(int x, int y,
			Set<Tuple2D<Integer>> setRelativeNeighbours) {

		Set<Tuple2D<Integer>> setNeighbours = new HashSet<Tuple2D<Integer>>();

		for (Tuple2D<Integer> tuple : setRelativeNeighbours) {
//			System.out.println("TopologyHexagon: " + setRelativeNeighbours);
			Tuple2D<Integer> posTupleCandidate = this.relativeToAbsolutePosition(tuple,x, y);
//			System.out.println("topologyHexagon: getPositionNeighbours " + posTuple);
			
			//posTuple are the set of neighbours, taking into consideration the relative position
			if (this.isValidPosTuple(posTupleCandidate)) {
				this.correctPosTuple(posTupleCandidate);
				setNeighbours.add(posTupleCandidate);
			}
		}
		return setNeighbours;
	}
//
//	public Set<Tuple2D<Integer>> getPositionNeighbours(int x, int y,
//			int minDist, int maxDist) {
//		boolean even = this.isEven(x, y);
//		Set<Tuple2D<Integer>> setRelativeNeighbours = this
//				.getRelativeNeighbours(even, minDist, maxDist);
//		return this.getPositionNeighbours(x, y, setRelativeNeighbours);
//	}

	protected Tuple2D<Integer> relativeToAbsolutePosition(
			Tuple2D<Integer> tuple, int x, int y) {
		int newX = tuple.getX() + x;
		int newY = tuple.getY() + y;
		return new Tuple2D<Integer>(newX, newY);
	}

	protected boolean isValidPosTuple(Tuple2D<Integer> posTuple) {
		int x = posTuple.getX();
		int y = posTuple.getY();

		if (!this.rollover.isVertical() && (y < 0 || y >= this.maxY))
			return false;
		if (!this.rollover.isHorizontal() && (x < 0 || x >= this.maxX))
			return false;
		return true;
	}

	protected void correctPosTuple(Tuple2D<Integer> posTuple) {
		int x = posTuple.getX();
		int y = posTuple.getY();
		
//		if (x==7 & y==6) {
//			System.out.println("TopologyHexagon-> value of y: " + y);
//			System.out.println("TopologyHexagon-> value of this.maxY: " + this.maxY);}

		if (this.rollover.isVertical()) {
			if (y < 0) {
				y = y%this.maxY + this.maxY; 
			} else if (y >= this.maxY) {
				y = y%this.maxY;
			}
		}
		if (this.rollover.isHorizontal()) {
			if (x < 0) {
				x = x%this.maxX + this.maxX; 
			} else if (x >= this.maxX) {
				x = x%this.maxX;
			}
		}

		posTuple.setX(x);
		posTuple.setY(y);
	}

	public Set<Tuple2D<Integer>> getRelativeNeighbours(boolean even,
			int minDist, int maxDist) {

		Set<Tuple2D<Integer>> setRelativeNeighbours = new HashSet<Tuple2D<Integer>>();

		if (minDist == 0) {
			setRelativeNeighbours.add(new Tuple2D<Integer>(0, 0));
			minDist++;
		}
		if (even) {
			for (int i = minDist; i <= maxDist; i++) {
				setRelativeNeighbours.addAll(this.evenRelativeNeighboursAt(i));
			}
		} else {
			for (int i = minDist; i <= maxDist; i++) {
				setRelativeNeighbours.addAll(this.oddRelativeNeighboursAt(i));
			}
		}
		return setRelativeNeighbours;
	}

	public abstract Set<Tuple2D<Integer>> evenRelativeNeighboursAt(int distance);

	public abstract Set<Tuple2D<Integer>> oddRelativeNeighboursAt(int distance);

	public abstract boolean isEven(int x, int y);

}
