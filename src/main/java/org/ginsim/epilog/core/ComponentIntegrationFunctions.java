package org.ginsim.epilog.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.antlr.runtime.RecognitionException;

import org.ginsim.epilog.integration.IntegrationFunctionSpecification;
import org.ginsim.epilog.integration.IntegrationFunctionSpecification.IntegrationExpression;

public class ComponentIntegrationFunctions {
	private List<String> stringExpr;
	private List<IntegrationExpression> computedExpr;

	public ComponentIntegrationFunctions(int maxValue) {
		this.stringExpr = new ArrayList<String>();
		for (int i = 0; i < maxValue; i++) {
			this.stringExpr.add("");
		}
		this.parseExpressions();
	}

	private void parseExpressions() {
		for (int i = 0; i < this.stringExpr.size(); i++) {
			this.computedExpr
					.add(this.string2Expression(this.stringExpr.get(i)));
		}
	}
	
	public List<IntegrationExpression> getComputedExpressions() {
		return this.computedExpr;
	}

	private IntegrationExpression string2Expression(String expr) {
		IntegrationFunctionSpecification spec = new IntegrationFunctionSpecification();
		IntegrationExpression expression = null;
		try {
			expression = spec.parse(expr);
		} catch (RecognitionException e) {
			e.printStackTrace();
		}
		return expression;
	}

	public void setFunctionAtLevel(byte value, String function) {
		this.stringExpr.set(value - 1, function);
		this.computedExpr.set(value - 1, this.string2Expression(function));
	}

	public List<String> getFunctions() {
		return Collections.unmodifiableList(this.stringExpr);
	}
}
