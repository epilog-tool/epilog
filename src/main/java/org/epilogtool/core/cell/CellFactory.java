package org.epilogtool.core.cell;

import org.colomoto.biolqm.LogicalModel;

public class CellFactory{

	public static DeadCell newDeadCell() {
		return new DeadCell();
	}
	public static LivingCell newLivingCell(LogicalModel m) {
		return new LivingCell(m);
	}
	public static InvalidCell newInvalidCell() {
		return new InvalidCell();
	}
	public static EmptyCell newEmptyCell() {
		return new EmptyCell();
	}

}
