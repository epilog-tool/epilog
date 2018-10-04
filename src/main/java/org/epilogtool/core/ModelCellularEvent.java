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
	
	private String deathAlgorithm;
	private String divisionAlgorithm;
	
	private int divisionRange;
	

	public ModelCellularEvent(float deathValue, float divisionValue, String deathTrigger, String divisionTrigger, String deathPattern, String divisionPattern, byte[] newCellState, String deathAlgorithm, String divisionAlgorithm, int divisionRange) {

		this.deathValue  = deathValue;
		this.divisionValue = divisionValue;
		
		this.deathTrigger = deathTrigger;
		this.divisionTrigger = divisionTrigger;
		
		this.deathPattern = deathPattern;
		this.divisionPattern = divisionPattern;
		
//		this.newCellSate = "";
		this.newCellState = newCellState;
		
		this.deathAlgorithm = deathAlgorithm;
		this.divisionAlgorithm = divisionAlgorithm;
		
		this.divisionRange = divisionRange;
		
	}


	public ModelCellularEvent clone() {
		return new ModelCellularEvent(this.getDeathValue(), this.getDivisionValue(), this.getDeathTrigger(), this.getDivisionTrigger(), this.getDeathPattern(), this.getDivisionPattern(), this.getNewCellState(), this.getDeathAlgorithm(), this.getDivisionAlgorithm(), this.getDivisionRange());
	}
	public int getDivisionRange() {
		return this.divisionRange;
	}

	public void setDivisionRange(int dRange) {
		this.divisionRange = dRange;
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
	
	public String getDeathAlgorithm() {
		return this.deathAlgorithm;}
	public void setDeathAlgorithm(String deathAlg) {
		this.deathAlgorithm = deathAlg;}
	
	public String getDivisionAlgorithm() {
		return this.divisionAlgorithm;}
	
	public void setDivisionAlgorithm(String divAlg) {

		this.divisionAlgorithm = divAlg;}
	
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
		if (!this.deathAlgorithm.equals(mce.getDeathAlgorithm())) {
			return false;		}
		if (!this.divisionAlgorithm.equals(mce.getDivisionAlgorithm())) {
			return false;		}
		if (this.divisionRange!=mce.getDivisionRange()) {
			return false;		}
		
		return true;
	}
	
}
