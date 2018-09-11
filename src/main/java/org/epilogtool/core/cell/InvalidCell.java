package org.epilogtool.core.cell;

import org.epilogtool.common.Txt;

public class InvalidCell extends AbstractCell {

	public InvalidCell() {
		this.name = Txt.get("s_INVALID_POSITION");

	}

	public InvalidCell clone() {
		return CellFactory.newInvalidCell();
	}

}
