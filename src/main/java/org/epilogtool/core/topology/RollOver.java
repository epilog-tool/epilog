package org.epilogtool.core.topology;

public enum RollOver {
	HORIZONTAL("Horizontal"), VERTICAL("Vertical"), NOROLLOVER("NoRollover");

	private String description;

	private RollOver(String description) {
		this.description = description;
	}

	public String toString() {
		return this.description;
	}

	public static RollOver string2RollOver(String str) {
		if (str.equals(HORIZONTAL.toString()))
			return HORIZONTAL;
		else if (str.equals(VERTICAL.toString()))
			return VERTICAL;
		else if (str.equals(NOROLLOVER.toString()))
			return NOROLLOVER;
		return null;
	}
}
