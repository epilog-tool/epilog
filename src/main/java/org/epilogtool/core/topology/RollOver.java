package org.epilogtool.core.topology;

public enum RollOver {
	NONE("Rectangular", false, false), HORIZ("Cylinder (horizontal wrap)", true,
			false), VERT("Cylinder (vertical wrap)", false, true), HORIZ_VERT("Torus", true, true);

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
		if (str.equals(HORIZ.toString()) || str.equals("Horizontal"))
			return HORIZ;
		else if (str.equals(VERT.toString()) || str.equals("Vertical"))
			return VERT;
		else if (str.equals(NONE.toString()) || str.equals("NoRollover"))
			return NONE;
		else if (str.equals(HORIZ_VERT.toString()) || str.equals("Horizontal&Vertical"))
			return HORIZ_VERT;
		return null;
	}
}
