package org.epilogtool.core.cellDynamics;


import org.antlr.runtime.RecognitionException;
import org.epilogtool.cellularevent.CellularEventExpression;
import org.epilogtool.cellularevent.*;

public class ModelEventExpression {
	
	private String expression;
	private CellularEventExpression computedExpression;

	public ModelEventExpression(String expression) {
		this.expression = expression;
		this.computedExpression = this.setComputedExpression();
	}
	
	private CellularEventExpression setComputedExpression() {
		CESpecification ceSpec = new CESpecification();
		CellularEventExpression ceExp = null;
		try {
			ceExp = ceSpec.parse(this.expression);
		} catch (RecognitionException e) {
			e.printStackTrace();
		}
		return ceExp;
	}
	
	public CellularEventExpression getcomputedExpression() {
		return this.computedExpression;
	}
	
	public String getExpression() {
		return this.expression;
	}
	
	public void setExpression(String expression) {
		this.expression = expression;
		this.setComputedExpression();
	}
	
	
	public boolean equals(Object o) {
		ModelEventExpression other = (ModelEventExpression) o;
		return this.expression.equals(other.expression);
	}
	
	public ModelEventExpression clone() {
		return new ModelEventExpression(new String(this.expression));
	}
}
