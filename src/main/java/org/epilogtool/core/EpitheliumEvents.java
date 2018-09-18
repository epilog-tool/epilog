package org.epilogtool.core;

import java.util.List;
import java.util.Map;

import org.epilogtool.core.cell.DeadCell;
import org.epilogtool.core.cell.EmptyCell;
import org.epilogtool.core.cell.InvalidCell;
import org.epilogtool.core.cell.LivingCell;

public class EpitheliumEvents {
	
	public static String DEFAULT_ORDER = "Random";
	public static String DEFAULT_NEWCELL = "Naive";
	public static String DEFAULT_DEATHOPTION = "Empty position";
	public static float DEFAULT_DIVISIONPROBABILITY = (float) 1.0;
	public static float DEFAULT_DEATHPROBABILITY = (float) 1.0;
	
//	private List<LivingCell> lstLivingCells;
//	private List<DeadCell> lstDeadCells;
//	private List<InvalidCell> lstInvalidPositions;
//	private List<EmptyCell> lstEmptyPositions;
	
	
	private String eventOrder;
	private String deathOption;
	private String strNewCellState;
	private byte[] newCellState;
	private float  divisionProbability;
	private float  deathProbability;

	public EpitheliumEvents(float divisionProbability, float deathProbability, String eventOrder, String strNewCellState, String deathOption, byte[] newCellState) {
		
		this.divisionProbability = divisionProbability;
		this.deathProbability = deathProbability;
		this.eventOrder = eventOrder;
		this.deathOption = deathOption;
		this.strNewCellState = strNewCellState;
		this.newCellState = newCellState;

	}


	public float getDivisionProbability() {
		return this.divisionProbability;
	}
	
	public float getDeathProbability() {
		return this.deathProbability;
	}
	
	public float setDivisionProbability() {
		return this.divisionProbability;
	}
	
	public float setDeathProbability() {
		return this.deathProbability;
	}
	
	public String getEventOrder() {
		return this.eventOrder;
	}
	
	public void setEventOrder(String str) {
		this.eventOrder = str;
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
		return new EpitheliumEvents(this.divisionProbability, this.deathProbability, this.eventOrder, this.strNewCellState, this.deathOption, this.newCellState);
	}

	public boolean equals(Object o) {
		
		EpitheliumEvents newEpiEvents = (EpitheliumEvents) o ;
		
		
		if (this.divisionProbability != newEpiEvents.getDivisionProbability())
			return false;
		
		if (this.deathProbability != newEpiEvents.getDeathProbability())
			return false;
		
		if (!this.eventOrder.equals(newEpiEvents.getEventOrder()))
			return false;
		
		if (!this.strNewCellState.equals(newEpiEvents.getNewCellState()))
			return false;
		
		if (!this.deathOption.equals(newEpiEvents.getDeathOption()))
			return false;
		
		if (this.newCellState!=null && newEpiEvents.getCellState()!= null)
		if (!this.newCellState.equals(newEpiEvents.getCellState()))
			return false;
		
		
		return true;

	}
}
