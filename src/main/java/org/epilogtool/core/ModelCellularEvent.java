package org.epilogtool.core;



//
public class ModelCellularEvent {
	
	private float deathValue;
	private float divisionValue;
	
	private String deathTrigger;
	private String divisionTrigger;
	
	private String deathPattern;
	private String divisionPattern;
	
	private byte[] newCellState;
	

	public ModelCellularEvent(float deathValue, float divisionValue, String deathTrigger, String divisionTrigger, String deathPattern, String divisionPattern, byte[] newCellState) {

		this.deathValue  = deathValue;
		this.divisionValue = divisionValue;
		
		this.deathTrigger = deathTrigger;
		this.divisionTrigger = divisionTrigger;
		
		this.deathPattern = deathPattern;
		this.divisionPattern = divisionPattern;
		
//		this.newCellSate = "";
		this.newCellState = newCellState;
		
	}


	public ModelCellularEvent clone() {
		return new ModelCellularEvent(this.getDeathValue(), this.getDivisionValue(), this.getDeathTrigger(), this.getDivisionTrigger(), this.getDeathPattern(), this.getDivisionPattern(), this.getNewCellState());
	}
	public byte[] getNewCellState(){
		return this.newCellState;
	}
	public void setNewCellState(byte[] state){
		this.newCellState = state;
	}
	
	public float getDivisionValue(){
		return this.divisionValue;
	}
	
	public void setDivisionValue(float value){
		this.divisionValue = value;
	}
	
	public void setDeathValue(float value){
		this.deathValue = value;
	}
	
	public float getDeathValue(){
		return this.deathValue;
	}


	public String getDeathPattern() {
		return this.deathPattern;}
	public void setDeathPattern(String pattern) {
		this.deathPattern = pattern;}
	
	public String getDivisionPattern() {
		return this.divisionPattern;}
	public void setDivisionPattern(String pattern) {
		this.divisionPattern = pattern;}


	public String getDeathTrigger() {
		return this.deathTrigger;
	}
	
	public String getDivisionTrigger() {
		return this.divisionTrigger;
	}
	
	public void setDeathTrigger(String trigger) {
		this.deathTrigger = trigger;
	}
	
	public void setDivisionTrigger(String trigger) {
		this.divisionTrigger= trigger;
	}
	
	public boolean equals(Object o) {
		
		ModelCellularEvent mce = (ModelCellularEvent) o ;
		
		if (this.deathValue !=mce.getDeathValue()) {
			return false;		}
		if (this.divisionValue !=mce.getDivisionValue()) {
			return false;		}
		if (!this.deathTrigger.equals(mce.getDeathTrigger())) {
			return false;		}
		if (!this.divisionTrigger.equals(mce.getDivisionTrigger())) {
			return false;		}
		if (!this.deathPattern.equals(mce.getDeathPattern())) {
			return false;		}
		if (!this.divisionPattern.equals(mce.getDivisionPattern())) {
			return false;		}
		if (!this.newCellState.equals(mce.getNewCellState())) {
			return false;		}
		
		
		return true;
	}
	
}
