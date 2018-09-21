package org.epilogtool.core;


import org.colomoto.biolqm.LogicalModel;
import org.epilogtool.common.Txt;

//
public class ModelCellularEvent {
	
	private float deathValue;
	private float divisionValue;
	
	private String deathTrigger;
	private String divisionTrigger;
	
	private String deathPattern;
	private String divisionPattern;
	
//	private String  newCellSate;
	private byte[] newCellState;
	
	private LogicalModel model;

	public ModelCellularEvent(LogicalModel model, float deathValue, float divisionValue) {

		this.deathValue  = deathValue;
		this.divisionValue = divisionValue;
		
		this.deathTrigger = Txt.get("s_TAB_EVE_TRIGGER_NONE");
		this.divisionTrigger = Txt.get("s_TAB_EVE_TRIGGER_NONE");
		
		this.deathPattern = "";
		this.divisionPattern = "";
		
//		this.newCellSate = "";
		this.newCellState = null;
		
		this.model = model;		
	}


	public byte[] getNewCellState(){
		return this.newCellState;
	}
	public void setNewCellState(byte[] state){
		this.newCellState = state;
	}
	
	public LogicalModel getModel() {
		return this.model;
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


//	public JLabel getDivisionLabel() {
//		return spDivision.getLabel();
//	}
//
//	public int getDivisionMin() {
//		return spDivision.getMin();
//	}
	
//	public int getDivisionMax() {
//		return spDivision.getMax();
//	}
//	
//	public JLabel getDeathLabel() {
//		return spDivision.getLabel();
//	}

//	public int getDeathMin() {
//		return spDivision.getMin();
//	}
//	
//	public int getDeathMax() {
//		return spDivision.getMax();
//	}


//	public SliderPanel getDivisionSlider() {
//		return this.spDivision;
//	}
//	
//	public SliderPanel getDeathSlider() {
//		return this.spDeath;
//	}


//	public void setDivisionText(String string) {
//		this.spDivision.setText(string);
//	}
//	
//	public void setDeathText(String string) {
//		this.spDeath.setText(string);
//	}


	public String getDeathPattern() {
		return this.deathPattern;
	}
	
	public String getDivisionPattern() {
		return this.divisionPattern;
	}


	public void setDeathPattern(String pattern) {
		this.deathPattern = pattern;
	}
	
	public void setDivisionPattern(String pattern) {
		this.divisionPattern = pattern;
	}


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
	
}
