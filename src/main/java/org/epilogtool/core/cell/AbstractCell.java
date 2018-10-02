package org.epilogtool.core.cell;

import org.epilogtool.common.Tuple2D;
import org.epilogtool.common.Txt;

public abstract class AbstractCell{
	
	protected String name;
	protected Tuple2D<Integer> tuple;
	
	public String getName() {
		return this.name;
	}
	
	public boolean isLivingCell() {
		return this.getName().equals(Txt.get("s_LIVING_CELL"));
	}

	public boolean isEmptyCell() {
		return this.getName().equals(Txt.get("s_EMPTY_CELL"));
	}
	
	public boolean isInvalidCell() {
		return this.getName().equals(Txt.get("s_INVALID_CELL"));
	}
	
	public boolean isDeadCell() {
		return this.getName().equals(Txt.get("s_DEAD_CELL"));
	}
	
	public Tuple2D<Integer> getTuple() {
		return this.tuple;
	}
	
	public void setTuple(Tuple2D<Integer> tuple) {
		this.tuple = tuple;
	}
	
	public abstract AbstractCell clone();
	
	public String refString() {
		return this.name + "[" + Integer.toHexString(System.identityHashCode(this)) + "]";
	}

}
