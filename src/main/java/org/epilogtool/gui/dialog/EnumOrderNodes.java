package org.epilogtool.gui.dialog;

import org.epilogtool.common.Txt;

public enum EnumOrderNodes {
	ORIGINAL(Txt.get("s_ENUM_ORDERNODES_ORIG")), ALPHA(Txt.get("s_ENUM_ORDERNODES_ALPHA"));
	private String desc;

	private EnumOrderNodes(String desc) {
		this.desc = desc;
	}

	public static String title() {
		return Txt.get("s_ENUM_ORDERNODES_TITLE");
	}

	public String toString() {
		return this.desc;
	}

//	public static EnumOrderNodes fromString(String str) {
//		if (str.equals(ORIGINAL.toString()))
//			return ORIGINAL;
//		else if (str.equals(ALPHA.toString()))
//			return ALPHA;
//		return null;
//	}
}
