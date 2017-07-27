package org.epilogtool.core;

import java.util.Arrays;
import java.util.List;

import org.antlr.runtime.RecognitionException;
import org.epilogtool.integration.IFSpecification;
import org.epilogtool.integration.IntegrationFunctionExpression;

public class ComponentIntegrationFunctions {
	private String[] stringExpr;
	private IntegrationFunctionExpression[] computedExpr;

	public ComponentIntegrationFunctions(int maxValue) {
		this.stringExpr = new String[maxValue];
		for (int i = 0; i < maxValue; i++) {
			this.stringExpr[i] = "";
		}
		this.computedExpr = new IntegrationFunctionExpression[maxValue];
	}

	public ComponentIntegrationFunctions clone() {
		ComponentIntegrationFunctions newcif = new ComponentIntegrationFunctions(this.stringExpr.length);
		for (byte i = 1; i <= this.stringExpr.length; i++) {
			try {
				newcif.setFunctionAtLevel(i, this.stringExpr[i - 1]);
			} catch (RecognitionException re) {
				// TODO Auto-generated catch block
			} catch (RuntimeException re) {
				// TODO Auto-generated catch block
			}
		}
		return newcif;
	}

	public List<IntegrationFunctionExpression> getComputedExpressions() {
		return Arrays.asList(this.computedExpr);
	}

	/**
	 * Translates an Integration Function (IF) from string to IntegrationFunctionExpression. Calls the parse to check if the IF is well written.
	 * 
	 * @param expr
	 * @return
	 * @throws RecognitionException
	 * @throws RuntimeException
	 */
	private IntegrationFunctionExpression string2Expression(String expr) throws RecognitionException, RuntimeException {
		IFSpecification spec = new IFSpecification();
		IntegrationFunctionExpression expression = null;
		expression = spec.parse(expr);
		return expression;
	}

	/**
	 * Sets a integration function to a level, both at string level and IntegrationFunctionExpression level.
	 * 
	 * @param value
	 * @param function
	 * @throws RecognitionException
	 * @throws RuntimeException
	 */
	public void setFunctionAtLevel(byte value, String function) throws RecognitionException, RuntimeException {
		this.stringExpr[value - 1] = function;
		this.computedExpr[value - 1] = this.string2Expression(function);
	}

	public List<String> getFunctions() {
		return Arrays.asList(this.stringExpr);
	}

	public boolean equals(Object o) {
		ComponentIntegrationFunctions cif = (ComponentIntegrationFunctions) o;
		List<String> outStrings = cif.getFunctions();
		for (int i = 0; i < this.stringExpr.length; i++) {
			if (!this.stringExpr[i].equals(outStrings.get(i)))
				return false;
		}
		return true;
	}
	
}
