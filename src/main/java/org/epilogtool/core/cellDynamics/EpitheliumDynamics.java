package org.epilogtool.core.cellDynamics;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.colomoto.logicalmodel.LogicalModel;

public class EpitheliumDynamics {
	
	private Map<LogicalModel, ModelDynamics> model2DynamicsMap;

	public EpitheliumDynamics() {
		this.model2DynamicsMap = new HashMap<LogicalModel, ModelDynamics>();
	}
	
	public EpitheliumDynamics(Set<LogicalModel> modelSet) {
		this.model2DynamicsMap = new HashMap<LogicalModel, ModelDynamics>();
		for (LogicalModel m  : modelSet) {
			this.addModel(m);
		}
	}
	
	private EpitheliumDynamics(Map<LogicalModel, ModelDynamics> model2DynamicsMap) {
		this.model2DynamicsMap = model2DynamicsMap;
	}
	
	public EpitheliumDynamics clone() {
		Map<LogicalModel, ModelDynamics> tmpMap = new HashMap<LogicalModel, ModelDynamics>();
		for (LogicalModel m : this.model2DynamicsMap.keySet()) {
			tmpMap.put(m, this.model2DynamicsMap.get(m).clone());
		}
		return new EpitheliumDynamics(tmpMap);
	}
	
	public boolean equals(Object o) {
		EpitheliumDynamics other = (EpitheliumDynamics) o;
		return this.model2DynamicsMap.equals(other.model2DynamicsMap);
	}
	
	public void addModel(LogicalModel m) {
		this.model2DynamicsMap.put(m, new ModelDynamics());
	}
	
	public void removeModel(LogicalModel m) {
		this.model2DynamicsMap.remove(m);
	}
	
	public boolean hasModel(LogicalModel m) {
		return this.model2DynamicsMap.keySet().contains(m);
	}
	
	public Set<LogicalModel> getModelSet() {
		return this.model2DynamicsMap.keySet();
	}
	
	public void addTriggerManager(LogicalModel m, ModelDynamics modelDynamics) {
		this.model2DynamicsMap.put(m, modelDynamics);
	}
	
	public ModelDynamics getTriggerManager(LogicalModel m) {
		return this.model2DynamicsMap.get(m);
	}
	
	public CellTrigger getCellTrigger(LogicalModel m, byte[] state) {
		return this.model2DynamicsMap.get(m).getCellTrigger(state);
	}
	
	public void setCellTriggerPattern(TriggerPattern pattern, LogicalModel m, CellTrigger trigger) {
		this.model2DynamicsMap.get(m).addPattern(pattern, trigger);
	}
	
	public void removeCellTriggerPattern(LogicalModel m, CellTrigger trigger, TriggerPattern pattern) {
		this.model2DynamicsMap.get(m).removePattern(pattern, trigger);
	}
	
	public void removeCellTriggerPattern(LogicalModel m, CellTrigger trigger, int patternIndex) {
		this.model2DynamicsMap.get(m).removePattern(patternIndex, trigger);
	}
}
