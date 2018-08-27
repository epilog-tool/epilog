package org.epilogtool.core;


public class EpitheliumEvents {
	
	public static String DEFAULT_ORDER = "Random";
	public static String DEFAULT_NEWCELL = "Naive";
	public static String DEFAULT_DEATHOPTION = "Empty position";
	
	private String eventOrder;
	private String deathOption;
	private String strNewCellState;
	private byte[] newCellState;
	

	public EpitheliumEvents(String eventOrder, String strNewCellState, String deathOption, byte[] newCellState) {
		this.eventOrder = eventOrder;
		this.deathOption = deathOption;
		this.strNewCellState = strNewCellState;
		this.newCellState = newCellState;
	}

	
	public void setEventOrder(String str) {
		this.eventOrder = str;
	}
	
	public String getEventOrder() {
		return this.eventOrder;
	}
	
	public String getDeathOption() {
		return this.deathOption;
	}
	
	public void setDeathOption(String str) {
		this.deathOption = str;
	}

	
	public String getNewCellState() {
		return this.strNewCellState;
	}
	
	public void setNewCellState(String str) {
		this.strNewCellState = str;
	}
	
	public byte[] getCellState() {
		return this.newCellState;
	}
	
	public void setCellState(byte[] cellState) {
		this.newCellState = cellState;
	}
	
	public EpitheliumEvents clone() {
		return new EpitheliumEvents(this.eventOrder, this.deathOption, this.strNewCellState, this.newCellState);
	}

	public boolean equals(Object o) {
		return false;

	}
}
