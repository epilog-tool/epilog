package org.epilogtool.core.cell;

import org.epilogtool.common.Txt;

public abstract class AbstractCell{
	
	protected String name;
	
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
	
	public abstract AbstractCell clone();

}
