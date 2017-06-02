package org.epilogtool.common;

public enum RandomSeedType {
	FIXED("Fixed"), RANDOM("Random");

	private String description;

	private RandomSeedType(String desc) {
		this.description = desc;
	}

	public String toString() {
		return this.description;
	}

	public static String title() {
		return "Simulation number generator seed";
	}

	public static RandomSeedType string2RandomSeed(String str) {
		if (str.equals(FIXED.toString()))
			return FIXED;
		else if (str.equals(RANDOM.toString()))
			return RANDOM;
		return null;
	}
}
