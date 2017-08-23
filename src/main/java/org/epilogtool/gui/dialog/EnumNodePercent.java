package org.epilogtool.gui.dialog;

public enum EnumNodePercent {
	YES("Yes"), NO("No");
	private String desc;

	private EnumNodePercent(String desc) {
		this.desc = desc;
	}

	public static String title() {
		return "Show grid node percentage";
	}

	public String toString() {
		return this.desc;
	}

	public static EnumNodePercent fromString(String str) {
		if (str.equals(YES.toString()))
			return YES;
		else if (str.equals(NO.toString()))
			return NO;
		return null;
	}
}
