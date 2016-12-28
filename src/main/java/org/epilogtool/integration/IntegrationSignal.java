package org.epilogtool.integration;

public class IntegrationSignal implements IntegrationSignalExpression {
	private String componentName;
	private byte minThreshold;
	private IntegrationDistance distance;

	public IntegrationSignal(String name, String minThreshold, IntegrationDistance distance) {
		this.componentName = name;
		this.minThreshold = (byte) Integer.parseInt(minThreshold);
		this.distance = distance;
	}

	public IntegrationDistance getDistance() {
		return distance;
	}

	public String getComponentName() {
		return componentName;
	}

	public byte getMinThreshold() {
		return minThreshold;
	}
}
