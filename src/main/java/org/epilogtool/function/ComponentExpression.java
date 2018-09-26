package org.epilogtool.function;

public class ComponentExpression implements FunctionExpression {
	private String componentName;
	private byte minThreshold;

	public ComponentExpression(String name, byte minThreshold) {
		this.componentName = name;
		this.minThreshold = minThreshold;
	}

	public String getComponentName() {
		return componentName;
	}

	public byte getMinThreshold() {
		return minThreshold;
	}
}
