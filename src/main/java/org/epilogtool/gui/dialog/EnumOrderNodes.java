package org.epilogtool.gui.dialog;

public enum EnumOrderNodes {
	ORIGINAL("Model original"), ALPHA("Alphabetical");
	private String desc;

	private EnumOrderNodes(String desc) {
		this.desc = desc;
	}

	public static String title() {
		return "Components list order";
	}

	public String toString() {
		return this.desc;
	}

	public static EnumOrderNodes fromString(String str) {
		if (str.equals(ORIGINAL.toString()))
			return ORIGINAL;
		else if (str.equals(ALPHA.toString()))
			return ALPHA;
		return null;
	}
}
