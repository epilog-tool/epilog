package org.epilogtool.core.cellDynamics;

public enum CellTrigger {
	DEFAULT("Default"), 
	PROLIFERATION("Proliferation"), 
	APOPTOSIS("Apoptosis"), 
	ONCOSIS("Oncosis"),
	MIGRATION("Migration");
	
	private String triggerType;
	
	private CellTrigger(String triggerType) {
		this.triggerType = triggerType;
	}
	
	public static CellTrigger string2CellTrigger(String triggerType) {
		if (triggerType.equals(DEFAULT.toString())) {
			return DEFAULT;
		}
		if (triggerType.equals(PROLIFERATION.toString())) {
			return PROLIFERATION;
		}
		if (triggerType.equals(APOPTOSIS.toString())) {
			return APOPTOSIS;
		}
		if (triggerType.equals(ONCOSIS.toString())) {
			return ONCOSIS;
		}
		if (triggerType.equals(MIGRATION.toString())) {
			return MIGRATION;
		}
		return null;
	}
	
	public String toString() {
		return this.triggerType;
	}
}
