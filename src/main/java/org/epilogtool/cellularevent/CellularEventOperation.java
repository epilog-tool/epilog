package org.epilogtool.cellularevent;

import java.util.ArrayList;
import java.util.List;

public abstract class CellularEventOperation implements CellularEventExpression {
	private LogicalOperator operator;
	private CellularEventExpression expr1;
	private CellularEventExpression expr2;

	public CellularEventOperation(CellularEventExpression expr1, CellularEventExpression expr2,
			LogicalOperator operator) {
		this.expr1 = expr1;
		this.expr2 = expr2;
		this.operator = operator;
	}

	public LogicalOperator getOperation() {
		return this.operator;
	}

	public List<CellularEventExpression> getOperands() {
		List<CellularEventExpression> listExpression = new ArrayList<CellularEventExpression>();
		listExpression.add(this.expr1);
		listExpression.add(this.expr2);

		return listExpression;
	}
}
