package org.epilogtool.integration;

public class IntegrationDistance {
	private int min;
	private int max;

	public IntegrationDistance(String min, String max) throws RuntimeException {
		this.min = Integer.parseInt(min);
		this.max = Integer.parseInt(max);
		if (this.max > -1 && this.min > this.max) {
			throw new RuntimeException("Invalid integration distance");
		}
	}

	public int getMin() {
		return min;
	}

	public int getMax() {
		return max;
	}
}
