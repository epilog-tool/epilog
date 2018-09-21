package org.epilogtool.core;

import java.awt.Component;

import javax.swing.JLabel;

import org.colomoto.biolqm.LogicalModel;
import org.epilogtool.common.Txt;
import org.epilogtool.gui.widgets.SliderPanel;

//
public class ModelCellularEvent {
	
	private SliderPanel spDeath;
	private SliderPanel spDivision;
	
	private String triggerDeath;
	private String triggerDivision;
	
	private String patternDeath;
	private String patternDivision;
	
	private String  newCellSate;
	private byte[] newCellState;
	
	private LogicalModel model;

	public ModelCellularEvent(LogicalModel model, SliderPanel spDeath, SliderPanel spDivision) {

		this.spDeath  = spDeath;
		this.spDivision = spDivision;
		
		this.triggerDeath = Txt.get("s_TAB_EVE_TRIGGER_NONE");
		this.triggerDivision = Txt.get("s_TAB_EVE_TRIGGER_NONE");
		
		this.patternDeath = "";
		this.patternDivision = "";
		
		this.newCellSate = "";
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
		return this.spDivision.getValue();
	}
	
	public void setDivisionValue(int value){
		this.spDivision.setValue(value);
	}
	
	public void setDeathValue(int f){
		this.spDeath.setValue(f);
	}
	
	public float getDeathValue(){
		return this.spDeath.getValue();
	}


	public JLabel getDivisionLabel() {
		return spDivision.getLabel();
	}

	public int getDivisionMin() {
		return spDivision.getMin();
	}
	
	public int getDivisionMax() {
		return spDivision.getMax();
	}
	
	public JLabel getDeathLabel() {
		return spDivision.getLabel();
	}

	public int getDeathMin() {
		return spDivision.getMin();
	}
	
	public int getDeathMax() {
		return spDivision.getMax();
	}


	public SliderPanel getDivisionSlider() {
		return this.spDivision;
	}
	
	public SliderPanel getDeathSlider() {
		return this.spDeath;
	}


	public void setDivisionText(String string) {
		this.spDivision.setText(string);
	}
	
	public void setDeathText(String string) {
		this.spDeath.setText(string);
	}


	public String getDeathPattern() {
		return this.patternDeath;
	}
	
	public String getDivisionPattern() {
		return this.patternDivision;
	}


	public void setDeathPattern(String pattern) {
		this.patternDeath = pattern;
	}
	
	public void setDivisionPattern(String pattern) {
		this.patternDivision = pattern;
	}


	public String getDeathTrigger() {
		return this.triggerDeath;
	}
	
	public String getDivisionTrigger() {
		return this.triggerDivision;
	}
	
	public void setDeathTrigger(String trigger) {
		this.triggerDeath = trigger;
	}
	
	public void setDivisionTrigger(String trigger) {
		this.triggerDivision= trigger;
	}
	
}
