package org.epilogtool.integration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class IntegrationSignalBinOp implements IntegrationSignalExpression {
	protected IntegrationSignalExpression expr1;
	protected IntegrationSignalExpression expr2;

	public IntegrationSignalBinOp(IntegrationSignalExpression expr1, IntegrationSignalExpression expr2) {
		this.expr1 = expr1;
		this.expr2 = expr2;
	}

	public List<IntegrationSignalExpression> getOperands() {
		List<IntegrationSignalExpression> listExpression = new ArrayList<IntegrationSignalExpression>();
		listExpression.add(this.expr1);
		listExpression.add(this.expr2);

		return listExpression;
	}
	
	@Override
	public Set<String> getRegulators() {
		Set<String> sNodeIDs = new HashSet<String>();
		if (this.expr1!=null) {
			sNodeIDs.addAll(this.expr1.getRegulators());
		}
		if (this.expr2!=null) {
			sNodeIDs.addAll(this.expr2.getRegulators());
		}
		return sNodeIDs;
	}

}
