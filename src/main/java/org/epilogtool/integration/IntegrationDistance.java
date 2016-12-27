package org.epilogtool.integration;



import org.antlr.runtime.RecognitionException;

public class IntegrationDistance {
	private int min;
	private int max;
	
	public IntegrationDistance(String min, String max) throws RecognitionException {
		this.min = Integer.parseInt(min);
		this.max = Integer.parseInt(max);
		if (this.max > -1 && this.min > this.max) {
			throw new RecognitionException();
		}
	}

	public int getMin() {
		return min;
	}

	public int getMax() {
		return max;
	}
}
