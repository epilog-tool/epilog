package org.epilogtool.core.cell;

import org.epilogtool.common.Tuple2D;
import org.epilogtool.common.Txt;

public class DeadCell extends AbstractCell {

	public DeadCell(Tuple2D<Integer> tuple) {
		this.name = Txt.get("s_DEAD_CELL");
		this.tuple = tuple;
	}

	public boolean equals(Object o) {
		
		AbstractCell ac = (AbstractCell) o;
	
		if (!ac.isDeadCell())
			return false;

		if (!tuple.equals(ac.getTuple())) {
			return false;
		}
		return true;
	}
	
	public DeadCell clone() {
		return CellFactory.newDeadCell(this.getTuple().clone());
	}

}
