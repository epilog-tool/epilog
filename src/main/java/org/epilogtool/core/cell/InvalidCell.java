package org.epilogtool.core.cell;

import org.epilogtool.common.Tuple2D;
import org.epilogtool.common.Txt;

public class InvalidCell extends AbstractCell {

	public InvalidCell(Tuple2D<Integer> tuple) {
		this.name = Txt.get("s_INVALID_CELL");
		this.tuple = tuple;

	}

	
	public boolean equals(Object o) {
		
		AbstractCell ac = (AbstractCell) o;
	
		if (!ac.isInvalidCell())
			return false;

		if (!tuple.equals(ac.getTuple())) {
			return false;
		}
		return true;
	}
	
	public InvalidCell clone() {
		return CellFactory.newInvalidCell(this.getTuple().clone());
	}

}
