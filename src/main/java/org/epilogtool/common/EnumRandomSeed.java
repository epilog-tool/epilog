package org.epilogtool.common;

public enum EnumRandomSeed {
	FIXED("Fixed"), RANDOM("Random");

	private String description;

	private EnumRandomSeed(String desc) {
		this.description = desc;
	}

	public String toString() {
		return this.description;
	}

	public static String title() {
		return "Simulation number generator seed";
	}

	public static EnumRandomSeed string2RandomSeed(String str) {
		if (str.equals(FIXED.toString()))
			return FIXED;
		else if (str.equals(RANDOM.toString()))
			return RANDOM;
		return null;
	}
}
