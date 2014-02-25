package org.ginsim.epilog.core.topology;

public enum RollOver {
	HORIZONTAL("Horizontal"), VERTICAL("Vertical"), NOROLLOVER("No Rollover");

	private String description;

	private RollOver(String description) {
		this.description = description;
	}
	
	public String toString() {
		return this.description;
	}
}
