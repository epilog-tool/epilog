package org.epilogtool.gui.dialog;

public enum CycleIdent {
	YES("Yes"), NO("No");
	private String desc;

	private CycleIdent(String desc) {
		this.desc = desc;
	}

	public static String title() {
		return "Enable Cycle identification";
	}

	public String toString() {
		return this.desc;
	}

	public static CycleIdent fromString(String str) {
		if (str.equals(YES.toString()))
			return YES;
		else if (str.equals(NO.toString()))
			return NO;
		return null;
	}
}
