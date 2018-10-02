package org.epilogtool.core.cell;

import org.colomoto.biolqm.LogicalModel;
import org.epilogtool.common.Tuple2D;

public class CellFactory{

	public static DeadCell newDeadCell(Tuple2D<Integer> tuple) {
		return new DeadCell(tuple);
	}
	public static LivingCell newLivingCell(Tuple2D<Integer> tuple, LogicalModel m) {
		return new LivingCell(tuple,m);
	}
	public static InvalidCell newInvalidCell(Tuple2D<Integer> tuple) {
		return new InvalidCell(tuple);
	}
	public static EmptyCell newEmptyCell(Tuple2D<Integer> tuple) {
		return new EmptyCell(tuple);
	}

}
