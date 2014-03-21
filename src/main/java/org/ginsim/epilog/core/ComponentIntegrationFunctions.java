package org.ginsim.epilog.core;

import java.util.Arrays;
import java.util.List;

import org.antlr.runtime.RecognitionException;
import org.ginsim.epilog.integration.IntegrationFunctionSpecification;
import org.ginsim.epilog.integration.IntegrationFunctionSpecification.IntegrationExpression;

public class ComponentIntegrationFunctions {
	private String[] stringExpr;
	private IntegrationExpression[] computedExpr;

	public ComponentIntegrationFunctions(int maxValue) {
		this.stringExpr = new String[maxValue];
		for (int i = 0; i < maxValue; i++) {
			this.stringExpr[i] = "";
		}
		this.computedExpr = new IntegrationExpression[maxValue];
	}

	public List<IntegrationExpression> getComputedExpressions() {
		return Arrays.asList(this.computedExpr);
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
		this.stringExpr[value - 1] = function;
		this.computedExpr[value - 1] = this.string2Expression(function);
	}

	public List<String> getFunctions() {
		return Arrays.asList(this.stringExpr);
	}
}
