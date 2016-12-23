package org.epilogtool.cellularevent;

public class CellularEventNode implements CellularEventExpression {
	private String componentName;
	private byte value;

	public CellularEventNode(String name, String value) {
		this.componentName = name;
		this.value = (byte) Integer.parseInt(value);
	}

	public String getComponentName() {
		return this.componentName;
	}

	public byte getValue() {
		return this.value;
	}
}
