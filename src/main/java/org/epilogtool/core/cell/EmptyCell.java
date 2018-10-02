package org.epilogtool.core.cell;

import org.epilogtool.common.Tuple2D;
import org.epilogtool.common.Txt;

public class EmptyCell extends AbstractCell {
	
	public EmptyCell(Tuple2D<Integer> tuple) {
		this.name = Txt.get("s_EMPTY_CELL");
		this.tuple = tuple;
	}

	public EmptyCell clone() {
		return CellFactory.newEmptyCell(this.getTuple().clone());
	}
	

	public boolean equals(Object o) {
		
		AbstractCell ac = (AbstractCell) o;
	
		if (!ac.isEmptyCell())
			return false;

		if (!tuple.equals(ac.getTuple())) {
			return false;
		}
		return true;
	}

}
