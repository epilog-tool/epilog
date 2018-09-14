package org.epilogtool.core.cell;

import org.epilogtool.common.Txt;

public class InvalidCell extends AbstractCell {

	public InvalidCell() {
		this.name = Txt.get("s_INVALID_CELL");

	}

	public InvalidCell clone() {
		return CellFactory.newInvalidCell();
	}

}
