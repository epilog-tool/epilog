package org.epilogtool.core.topology;

import org.epilogtool.common.Txt;

public enum RollOver {
	NONE(Txt.get("s_Rollover_N"), false, false), HORIZ(Txt.get("s_Rollover_H"), true,
			false), VERT(Txt.get("s_Rollover_V"), false, true), HORIZ_VERT(Txt.get("s_Rollover_B"), true, true);

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

	public static RollOver string2RollOver(String epiName, String str) {
		// Note: The equals with additional options are for backward compatibility
		if (str.equals(HORIZ.toString()) || str.equals("Horizontal") || str.equals("Cylinder_(horizontal_wrap)")) {
			return HORIZ;
		} else if (str.equals(VERT.toString()) || str.equals("Vertical") || str.equals("Cylinder_(vertical_wrap)")) {
			return VERT;
		} else if (str.equals(NONE.toString()) || str.equals("NoRollover") || str.equals("Rectangular")
				|| str.equals("Rectangular_(no_wrap)")) {
			return NONE;
		} else if (str.equals(HORIZ_VERT.toString()) || str.equals("Torus") || str.equals("Horizontal&Vertical")
				|| str.equals("Torus_(both_wraps)")) {
			return HORIZ_VERT;
		} else {
			return null;
		}
	}

}
