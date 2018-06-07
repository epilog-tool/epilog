package org.epilogtool.core;

import org.epilogtool.common.Txt;

public enum UpdateCells {
	ALLCELLS(Txt.get("s_TAB_EPIUPDATE_ALL_CELLS")), UPDATABLECELLS(Txt.get("s_TAB_EPIUPDATE_UPDATABLE"));
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
