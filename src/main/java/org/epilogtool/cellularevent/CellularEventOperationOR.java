package org.epilogtool.cellularevent;

public class CellularEventOperationOR extends CellularEventOperation {
	public CellularEventOperationOR(CellularEventExpression expr1, CellularEventExpression expr2) {
		super(expr1, expr2, LogicalOperator.OR);
	}
}