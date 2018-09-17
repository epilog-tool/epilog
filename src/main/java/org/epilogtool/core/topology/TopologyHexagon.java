package org.epilogtool.core.topology;

import java.util.HashSet;
import java.util.Set;

import org.epilogtool.common.Tuple2D;

public abstract class TopologyHexagon extends Topology {

	protected final double SQRT3 = Math.sqrt(3);
	protected final double SQRT3_2 = SQRT3 / 2;

	@Override
	public Set<Tuple2D<Integer>> getPositionNeighbours(int x, int y, Set<Tuple2D<Integer>> setRelativeNeighbours) {

		Set<Tuple2D<Integer>> setNeighbours = new HashSet<Tuple2D<Integer>>();

		for (Tuple2D<Integer> tuple : setRelativeNeighbours) {
			Tuple2D<Integer> posTupleCandidate = this.relativeToAbsolutePosition(tuple, x, y);

			// posTuple are the set of neighbours, taking into consideration the relative
			// position
			if (this.isValidPosTuple(posTupleCandidate)) {
				this.correctPosTuple(posTupleCandidate);
				setNeighbours.add(posTupleCandidate);
			}
		}

		return setNeighbours;
	}

	/**
	 * From the relative positions given by the neighborhood relationship, return the position on the grid. 
	 * It will depend on the size of the grid. 
	 *
	 * @param Tuple2D<Integer> (x,y), which are the relative coordinates
	 * @return    Tuple2D<Integer> (newX,newY), which are the absolute position on the grid
	 * @see         Topology
	 */
	protected Tuple2D<Integer> relativeToAbsolutePosition(Tuple2D<Integer> tuple, int x, int y) {
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

		if (this.rollover.isVertical()) {
			if (y < 0) {
				y = y % this.maxY + this.maxY;
			}
			if (y >= this.maxY) {
				y = y % this.maxY;
			}
		}
		if (this.rollover.isHorizontal()) {
			if (x < 0) {
				x = x % this.maxX + this.maxX;
			}
			if (x >= this.maxX) {
				x = x % this.maxX;
			}

		}
		posTuple.setX(x);
		posTuple.setY(y);

	}

	public Set<Tuple2D<Integer>> getRelativeNeighbours(boolean even, int minDist, int maxDist) {

		Set<Tuple2D<Integer>> setRelativeNeighbours = new HashSet<Tuple2D<Integer>>();

		if (minDist == 0) {
			setRelativeNeighbours.add(new Tuple2D<Integer>(0, 0));
			minDist++;
		}

		if (maxDist == -1) {
			maxDist = Math.max(this.getX(), this.getY());
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
