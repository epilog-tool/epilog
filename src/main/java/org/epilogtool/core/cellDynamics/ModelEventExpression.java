package org.epilogtool.core.cellDynamics;


import org.antlr.runtime.RecognitionException;
import org.epilogtool.cellularevent.CellularEventExpression;
import org.epilogtool.cellularevent.*;

public class ModelEventExpression {
	
	private String expression;
	private CellularEventExpression computedExpression;

	public ModelEventExpression(String expression) {
		this.expression = expression;
		this.computedExpression = null;
	}
	
	public CellularEventExpression setComputedExpression() throws RecognitionException, RuntimeException {
		CESpecification ceSpec = new CESpecification();
		this.computedExpression = ceSpec.parse(this.expression);
		return this.computedExpression;
	}
	
	public CellularEventExpression getcomputedExpression() {
		return this.computedExpression;
	}
	
	public String getExpression() {
		return this.expression;
	}
	
	public boolean equals(Object o) {
		ModelEventExpression other = (ModelEventExpression) o;
		return this.expression.equals(other.expression);
	}
	
	public ModelEventExpression clone() {
		return new ModelEventExpression(new String(this.expression));
	}
}
