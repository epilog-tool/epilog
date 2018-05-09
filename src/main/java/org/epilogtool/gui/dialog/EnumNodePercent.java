package org.epilogtool.gui.dialog;

import org.epilogtool.common.Txt;

public enum EnumNodePercent {
	YES(Txt.get("s_ENUM_NODEPERCENT_Y")), NO(Txt.get("s_ENUM_NODEPERCENT_N"));
	private String desc;

	private EnumNodePercent(String desc) {
		this.desc = desc;
	}

	public static String title() {
		return Txt.get("s_ENUM_NODEPERCENT_TITLE");
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
