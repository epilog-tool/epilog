package org.epilogtool.core;

import java.util.HashMap;
import java.util.Map;

import org.colomoto.biolqm.LogicalModel;
import org.epilogtool.common.Txt;

public class EpitheliumEvents {
	
	public static String DEFAULT_ORDER = Txt.get("s_TAB_EPIUPDATE_ORDER_RANDOM");
	public static String DEFAULT_DIVISIONOPTION= Txt.get("s_TAB_EPIUPDATE_NEWCELLSTATE_NAIVE");
	public static String DEFAULT_DEATHOPTION = Txt.get("s_TAB_EPIUPDATE_CELLDEATH_EMPTY");
	
	public static String DEFAULT_DEATHTRIGGER = Txt.get("s_TAB_EVE_TRIGGER_NONE");
	public static String DEFAULT_DIVISIONTRIGGER = Txt.get("s_TAB_EVE_TRIGGER_NONE");
	public static String DEFAULT_DEATHPATTERN = "";
	public static String DEFAULT_DIVISIONPATTERN = "";
	
	public static float DEFAULT_DIVISIONPROBABILITY = (float) 1.0;
	public static float DEFAULT_DEATHPROBABILITY = (float) 1.0;
	
	
	//GENERAL
	private String eventOrder;
	private String deathOption;
	private String divisionOption;
	
	private Map<LogicalModel, ModelCellularEvent> model2MCE;

public EpitheliumEvents(String eventOrder, String deathOption, String divisionOption, Map<LogicalModel, ModelCellularEvent> model2MCE) {
		
		this.eventOrder = eventOrder;
		this.deathOption = deathOption;
		this.divisionOption = divisionOption;
		this.model2MCE = new HashMap<LogicalModel, ModelCellularEvent>();
	}
		
		
	public void setModel2MCE (LogicalModel model, ModelCellularEvent mce) {
		this.model2MCE.put(model, mce);
	}

	//Pattern that triggers Death
	public String getDeathPattern(LogicalModel m) {
		return this.model2MCE.get(m).getDeathPattern();
	}
	
	public void setDeathPattern(LogicalModel m, String pattern) {
		this.model2MCE.get(m).setDeathPattern(pattern);
	}
	
	//Pattern that triggers Division
	public String getDivisionPattern(LogicalModel m) {
		return this.model2MCE.get(m).getDivisionPattern();
	}
	
	public void setDivisionPattern(LogicalModel m, String pattern) {
		this.model2MCE.get(m).setDivisionPattern(pattern);
	}
	
	//If cell Division is predefined new cells will have this state (byte array)
	public byte[] getDivisionNewState(LogicalModel m) {
		return this.model2MCE.get(m).getNewCellState();
	}
	
	public void setDivisionNewState(LogicalModel m,byte[] state) {
		this.model2MCE.get(m).setNewCellState(state);
	}
	
	//What type of trigger are we assuming here for Death
	public String getDeathTrigger(LogicalModel m) {
		return this.model2MCE.get(m).getDeathTrigger();
	}
	
	public void setDeathTrigger(LogicalModel m, String str) {
		this.model2MCE.get(m).setDeathTrigger(str);
	}
	
	public String getDivisionTrigger(LogicalModel m) {
		return this.model2MCE.get(m).getDivisionTrigger();
	}
	
	public void setDivisionTrigger(LogicalModel m, String str) {
		this.model2MCE.get(m).setDivisionTrigger(str);
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
	
	public String getDivisionOption() {
		return this.divisionOption;
	}
	
	public void setDivisionOption(String option) {
		 this.divisionOption = option;
	}
	
	
	public float getDivisionProbability(LogicalModel m) {
		return this.model2MCE.get(m).getDivisionValue();
	}
	
	public float getDeathProbability(LogicalModel m) {
		return this.model2MCE.get(m).getDeathValue();
	}
	
	public void setDivisionProbability(LogicalModel m, int value) {
		this.model2MCE.get(m).setDivisionValue(value);
	}
	
	public void setDeathProbability(LogicalModel m, int value) {
		this.model2MCE.get(m).setDeathValue(value);
	}

	
	
	public EpitheliumEvents clone() {
		EpitheliumEvents epiEv = new  EpitheliumEvents(this.eventOrder, this.deathOption, this.divisionOption, this.model2MCE);
		return epiEv;
	}

	public boolean equals(Object o) {
		
		EpitheliumEvents newEpiEvents = (EpitheliumEvents) o ;
		
		if (!this.eventOrder.equals(newEpiEvents.getEventOrder()))
			return false;
		
		if (!this.deathOption.equals(newEpiEvents.getDeathOption()))
			return false;
		
		if (!this.divisionOption.equals(newEpiEvents.getDivisionOption()))
			return false;
		
		for (LogicalModel m: this.model2MCE.keySet()) {
			if (!this.model2MCE.get(m).equals(newEpiEvents.getMCE(m)))
				return false;
		}
		
		return true;

	}


	public ModelCellularEvent getMCE(LogicalModel selModel) {
		return this.model2MCE.get(selModel);
	}
}
