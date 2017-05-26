package org.epilogtool.core;

public enum UpdateOrder {
	RANDOM_INDEPENDENT("Random Independent"), RANDOM_ORDER("Random Order"), CYCLIC_ORDER("Cyclic Order");

	private String description;

	public static String title() {
		return "Cells Updating Order";
	}

	private UpdateOrder(String description) {
		this.description = description;
	}

	public String toString() {
		return this.description;
	}

	public static UpdateOrder fromString(String str) {
		if (str.equals(RANDOM_INDEPENDENT.toString()))
			return RANDOM_INDEPENDENT;
		else if (str.equals(RANDOM_ORDER.toString()))
			return RANDOM_ORDER;
		else if (str.equals(CYCLIC_ORDER.toString()))
			return CYCLIC_ORDER;
		return null;
	}
}
