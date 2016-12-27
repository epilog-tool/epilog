package org.epilogtool.integration;

public class IntegrationDistance {
	private int min;
	private int max;
	
	public IntegrationDistance(String min, String max) {
		this.min = Integer.parseInt(min);
		this.max = Integer.parseInt(max);
	}

	public int getMin() {
		return min;
	}

	public int getMax() {
		return max;
	}
}
