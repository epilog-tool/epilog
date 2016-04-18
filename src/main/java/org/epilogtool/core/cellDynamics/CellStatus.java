package org.epilogtool.core.cellDynamics;

public enum CellStatus {
	DEFAULT("Default"), 
	PROLIFERATION("Proliferation"), 
	APOPTOSIS("Apoptosis"), 
	ONCOSIS("Oncosis"),
	MIGRATION("Migration");
	
	private String stateType;
	
	private CellStatus(String stateType) {
		this.stateType = stateType;
	}
	
	public static CellStatus string2CellState(String stateType) {
		if (stateType.equals(DEFAULT.toString())) {
			return DEFAULT;
		}
		if (stateType.equals(PROLIFERATION.toString())) {
			return PROLIFERATION;
		}
		if (stateType.equals(APOPTOSIS.toString())) {
			return APOPTOSIS;
		}
		if (stateType.equals(ONCOSIS.toString())) {
			return ONCOSIS;
		}
		if (stateType.equals(MIGRATION.toString())) {
			return MIGRATION;
		}
		return null;
	}
	
	public String toString() {
		return this.stateType;
	}
}
