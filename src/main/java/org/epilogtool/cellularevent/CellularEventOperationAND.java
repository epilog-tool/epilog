package org.epilogtool.cellularevent;

public class CellularEventOperationAND extends CellularEventOperation {
	public CellularEventOperationAND(CellularEventExpression expr1, CellularEventExpression expr2) {
		super(expr1, expr2, LogicalOperator.AND);
	}
}