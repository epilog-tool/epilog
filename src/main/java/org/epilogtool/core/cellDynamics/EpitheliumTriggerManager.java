package org.epilogtool.core.cellDynamics;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.colomoto.logicalmodel.LogicalModel;

public class EpitheliumTriggerManager {
	
	private Map<LogicalModel, CellTriggerManager> model2TriggerManagerMap;

	public EpitheliumTriggerManager() {
		this.model2TriggerManagerMap = new HashMap<LogicalModel, CellTriggerManager>();
	}
	
	public EpitheliumTriggerManager(Set<LogicalModel> modelSet) {
		this.model2TriggerManagerMap = new HashMap<LogicalModel, CellTriggerManager>();
		for (LogicalModel m  : modelSet) {
			this.addModel(m);
		}
	}
	
	public EpitheliumTriggerManager clone() {
		return new EpitheliumTriggerManager(new HashMap<LogicalModel, 
				CellTriggerManager>(this.model2TriggerManagerMap));
	}
	
	public boolean equals(Object o) {
		EpitheliumTriggerManager other = (EpitheliumTriggerManager) o;
		return this.model2TriggerManagerMap.equals(other.model2TriggerManagerMap);
	}
	
	private EpitheliumTriggerManager(Map<LogicalModel, CellTriggerManager> model2ClassManagerMap) {
		this.model2TriggerManagerMap = model2ClassManagerMap;
	}
	
	public void addModel(LogicalModel m) {
		this.model2TriggerManagerMap.put(m, new CellTriggerManager());
	}
	
	public void removeModel(LogicalModel m) {
		this.model2TriggerManagerMap.remove(m);
	}
	
	public boolean hasModel(LogicalModel m) {
		return this.model2TriggerManagerMap.keySet().contains(m);
	}
	
	public Set<LogicalModel> getModelSet() {
		return this.model2TriggerManagerMap.keySet();
	}
	
	public CellTriggerManager getCellStatusManager(LogicalModel m) {
		return this.model2TriggerManagerMap.get(m);
	}
	
	public CellTrigger getCellStatus(LogicalModel m, byte[] state) {
		return this.model2TriggerManagerMap.get(m).getCellStatus(state);
	}
	
	public void setPattern(LogicalModel m, String pattern, CellTrigger classification) {
		this.model2TriggerManagerMap.get(m).addPattern(pattern, classification);
	}
	
	public void removePattern(LogicalModel m, String[] pattern, CellTrigger classification) {
		this.model2TriggerManagerMap.get(m).removePattern(pattern, classification);
	}
}
