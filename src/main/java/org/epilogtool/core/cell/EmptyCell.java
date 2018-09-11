package org.epilogtool.core.cell;

import org.epilogtool.common.Txt;

public class EmptyCell extends AbstractCell {
	
	public EmptyCell() {
		this.name = Txt.get("s_EMPTY_CELL");
	}

	public EmptyCell clone() {
		return CellFactory.newEmptyCell();
	}
	

	public boolean equals(Object o) {
		AbstractCell ac = (AbstractCell) o;
		return ac.isEmptyCell();
	}
}
