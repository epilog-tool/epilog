package org.epilogtool.gui.dialog;

public enum KeepHistory {
	YES("Yes"), NO("No");
	private String desc;

	private KeepHistory(String desc) {
		this.desc = desc;
	}

	public static String title() {
		return "Keep Simulation History";
	}

	public String toString() {
		return this.desc;
	}

	public static KeepHistory fromString(String str) {
		if (str.equals(YES.toString()))
			return YES;
		else if (str.equals(NO.toString()))
			return NO;
		return null;
	}
}
