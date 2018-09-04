package org.epilogtool.integration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class IntegrationFunctionBinOp implements IntegrationFunctionExpression {
	private IntegrationFunctionExpression expr1;
	private IntegrationFunctionExpression expr2;

	public IntegrationFunctionBinOp(IntegrationFunctionExpression expr1, IntegrationFunctionExpression expr2) {
		this.expr1 = expr1;
		this.expr2 = expr2;
	}

	public List<IntegrationFunctionExpression> getOperands() {
		List<IntegrationFunctionExpression> listExpression = new ArrayList<IntegrationFunctionExpression>();
		listExpression.add(this.expr1);
		listExpression.add(this.expr2);

		return listExpression;
	}

	public Set<String> getRegulators() {
		Set<String> sNodeIDs = new HashSet<String>();
		if (this.expr1 != null) {
			sNodeIDs.addAll(this.expr1.getRegulators());
		}
		if (this.expr2 != null) {
			sNodeIDs.addAll(this.expr2.getRegulators());
		}
		return sNodeIDs;
	}
}
