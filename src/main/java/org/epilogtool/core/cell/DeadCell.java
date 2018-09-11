package org.epilogtool.core.cell;

import org.epilogtool.common.Txt;

public class DeadCell extends AbstractCell {

	public DeadCell() {
		this.name = Txt.get("s_DEAD_CELL");
	}

	public DeadCell clone() {
		return CellFactory.newDeadCell();
	}

}
