package org.epilogtool.core.cell;

import org.colomoto.biolqm.LogicalModel;

public class CellFactory{



	public CellFactory() {
	}
	
	public AbstractCell newDeadCell() {
		return new DeadCell();
	}
	public AbstractCell newLivingCell(LogicalModel m) {
		return new LivingCell(m);
	}
	public AbstractCell newInvalidCell() {
		return new InvalidCell();
	}
	public AbstractCell newEmptyCell() {
		return new EmptyCell();
	}

}
