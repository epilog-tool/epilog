package org.ginsim.epilog.core.topology;

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
		if (str.equals(HORIZONTAL))
			return HORIZONTAL;
		else if (str.equals(VERTICAL)) 
			return VERTICAL;
		else if (str.equals(NOROLLOVER))
			return NOROLLOVER;
		return null;
	}
}
