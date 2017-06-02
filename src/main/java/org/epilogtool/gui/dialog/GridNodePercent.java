package org.epilogtool.gui.dialog;

public enum GridNodePercent {
	YES("Yes"), NO("No");
	private String desc;

	private GridNodePercent(String desc) {
		this.desc = desc;
	}

	public static String title() {
		return "Show grid node percentage";
	}

	public String toString() {
		return this.desc;
	}

	public static GridNodePercent fromString(String str) {
		if (str.equals(YES.toString()))
			return YES;
		else if (str.equals(NO.toString()))
			return NO;
		return null;
	}
}
