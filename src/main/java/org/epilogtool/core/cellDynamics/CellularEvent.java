package org.epilogtool.core.cellDynamics;

public enum CellularEvent {
	DEFAULT("Default"), 
	PROLIFERATION("Proliferation"), 
	APOPTOSIS("Apoptosis"), 
	ONCOSIS("Oncosis"),
	MIGRATION("Migration");
	
	private String eventType;
	
	private CellularEvent(String eventType) {
		this.eventType = eventType;
	}
	
	public static CellularEvent string2Event(String eventType) {
		if (eventType.equals(DEFAULT.toString())) {
			return DEFAULT;
		}
		if (eventType.equals(PROLIFERATION.toString())) {
			return PROLIFERATION;
		}
		if (eventType.equals(APOPTOSIS.toString())) {
			return APOPTOSIS;
		}
		if (eventType.equals(ONCOSIS.toString())) {
			return ONCOSIS;
		}
		if (eventType.equals(MIGRATION.toString())) {
			return MIGRATION;
		}
		return null;
	}
	
	public String toString() {
		return this.eventType;
	}
}
