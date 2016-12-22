package org.epilogtool.cellularevent;

public class CellularEventNOT implements CellularEventExpression {
	private CellularEventExpression expr = null;

	public CellularEventNOT(CellularEventExpression expr) {
		this.expr = expr;
	}

	public CellularEventExpression getNegatedExpression() {
		return this.expr;
	}
}