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
	
	private List<LivingCell> lstLivingCells;
	private List<DeadCell> lstDeadCells;
	private List<InvalidCell> lstInvalidPositions;
	private List<EmptyCell> lstEmptyPositions;
	
	
	private String eventOrder;
	private String deathOption;
	private String strNewCellState;
	private byte[] newCellState;
	private float  divisionProbability;
	private float  deathProbability;

	public EpitheliumEvents(float divisionProbability, float deathProbability, String eventOrder, String strNewCellState, String deathOption, byte[] newCellState) {
		this.eventOrder = eventOrder;
		this.deathOption = deathOption;
		this.strNewCellState = strNewCellState;
		this.newCellState = newCellState;
		this.divisionProbability = divisionProbability;
		this.deathProbability = deathProbability;
	}


	public float getDivisionProbability() {
		return this.divisionProbability;
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
		return new EpitheliumEvents(this.divisionProbability, this.deathProbability, this.eventOrder, this.deathOption, this.strNewCellState, this.newCellState);
	}

	public boolean equals(Object o) {
		return false;

	}
}
