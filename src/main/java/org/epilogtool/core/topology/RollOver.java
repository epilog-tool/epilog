package org.epilogtool.core.topology;

public enum RollOver {
	NONE("NoRollover", false, false), HORIZ("Horizontal", true, false), VERT(
			"Vertical", false, true), HORIZ_VERT("Horizontal&Vertical", true,
			true);

	private String description;
	private boolean isHorizontal;
	private boolean isVertical;

	private RollOver(String description, boolean isH, boolean isV) {
		this.description = description;
		this.isHorizontal = isH;
		this.isVertical = isV;
	}

	public String toString() {
		return this.description;
	}

	public boolean isVertical() {
		return this.isVertical;
	}

	public boolean isHorizontal() {
		return this.isHorizontal;
	}

	public static RollOver string2RollOver(String str) {
		if (str.equals(HORIZ.toString()))
			return HORIZ;
		else if (str.equals(VERT.toString()))
			return VERT;
		else if (str.equals(NONE.toString()))
			return NONE;
		else if (str.equals(HORIZ_VERT.toString()))
			return HORIZ_VERT;
		return null;
	}
}
