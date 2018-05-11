package org.epilogtool.core;

public enum UpdateCells {
	ALLCELLS("All Cells"), UPDATABLECELLS("Only Updatable Cells");
	private String description;

	private UpdateCells(String desc) {
		this.description = desc;
	}

	public static String title() {
		return "Cells to update per iteration";
	}

	public String toString() {
		return this.description;
	}

	public static UpdateCells fromString(String str) {
		if (str.equals(ALLCELLS.toString()))
			return ALLCELLS;
		else if (str.equals(UPDATABLECELLS.toString()))
			return UPDATABLECELLS;
		return null;
	}
}
