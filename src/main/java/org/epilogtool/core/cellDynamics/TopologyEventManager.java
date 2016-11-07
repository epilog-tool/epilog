package org.epilogtool.core.cellDynamics;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.colomoto.logicalmodel.LogicalModel;

public class TopologyEventManager {
	
	private Map<LogicalModel, ModelEventManager> model2ManagerMap;

	public TopologyEventManager() {
		this.model2ManagerMap = new HashMap<LogicalModel, ModelEventManager>();
	}
	
	public TopologyEventManager(Set<LogicalModel> modelSet) {
		this.model2ManagerMap = new HashMap<LogicalModel, ModelEventManager>();
		for (LogicalModel m  : modelSet) {
			this.addModel(m);
		}
	}
	
	private TopologyEventManager(Map<LogicalModel, ModelEventManager> model2ManagerMap) {
		this.model2ManagerMap = model2ManagerMap;
	}
	
	public TopologyEventManager clone() {
		Map<LogicalModel, ModelEventManager> tmpMap = new HashMap<LogicalModel, ModelEventManager>();
		for (LogicalModel m : this.model2ManagerMap.keySet()) {
			tmpMap.put(m, this.model2ManagerMap.get(m).clone());
		}
		return new TopologyEventManager(tmpMap);
	}
	
	public boolean equals(Object o) {
		TopologyEventManager other = (TopologyEventManager) o;
		return this.model2ManagerMap.equals(other.model2ManagerMap);
	}
	
	public void addModel(LogicalModel m) {
		this.model2ManagerMap.put(m, new ModelEventManager());
	}
	
	public void removeModel(LogicalModel m) {
		this.model2ManagerMap.remove(m);
	}
	
	public boolean hasModel(LogicalModel m) {
		return this.model2ManagerMap.keySet().contains(m);
	}
	
	public Set<LogicalModel> getModelSet() {
		return this.model2ManagerMap.keySet();
	}
	
	public void addModelManager(LogicalModel m, ModelEventManager modelEvents) {
		this.model2ManagerMap.put(m, modelEvents);
	}
	
	public ModelEventManager getModelManager(LogicalModel m) {
		return this.model2ManagerMap.get(m);
	}
	
	public CellularEvent getCellularEvent(LogicalModel m, byte[] state) {
		return this.model2ManagerMap.get(m).getModelEvent(state);
	}
	
	public void setCellularEventPattern(ModelPattern pattern, LogicalModel m, CellularEvent event) {
		this.model2ManagerMap.get(m).addPattern(pattern, event);
	}
	
	public void removeCellularEventPattern(LogicalModel m, CellularEvent event, ModelPattern pattern) {
		this.model2ManagerMap.get(m).removePattern(pattern, event);
	}
	
	public void removeCellularEventPattern(LogicalModel m, CellularEvent event, int patternIndex) {
		this.model2ManagerMap.get(m).removePattern(patternIndex, event);
	}
}
