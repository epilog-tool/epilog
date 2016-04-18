package org.epilogtool.core.cellDynamics;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.colomoto.logicalmodel.LogicalModel;

public class EpitheliumCellStatusManager {
	
	private Map<LogicalModel, CellStatusManager> model2StatusManagerMap;

	public EpitheliumCellStatusManager() {
		this.model2StatusManagerMap = new HashMap<LogicalModel, CellStatusManager>();
	}
	
	public EpitheliumCellStatusManager(Set<LogicalModel> modelSet) {
		this.model2StatusManagerMap = new HashMap<LogicalModel, CellStatusManager>();
		for (LogicalModel m  : modelSet) {
			this.addModel(m);
		}
	}
	
	public EpitheliumCellStatusManager clone() {
		return new EpitheliumCellStatusManager(new HashMap<LogicalModel, 
				CellStatusManager>(this.model2StatusManagerMap));
	}
	
	public boolean equals(Object o) {
		EpitheliumCellStatusManager other = (EpitheliumCellStatusManager) o;
		return this.model2StatusManagerMap.equals(other.model2StatusManagerMap);
	}
	
	private EpitheliumCellStatusManager(Map<LogicalModel, CellStatusManager> model2ClassManagerMap) {
		this.model2StatusManagerMap = model2ClassManagerMap;
	}
	
	public void addModel(LogicalModel m) {
		this.model2StatusManagerMap.put(m, new CellStatusManager());
	}
	
	public void removeModel(LogicalModel m) {
		this.model2StatusManagerMap.remove(m);
	}
	
	public boolean hasModel(LogicalModel m) {
		return this.model2StatusManagerMap.keySet().contains(m);
	}
	
	public Set<LogicalModel> getModelSet() {
		return this.model2StatusManagerMap.keySet();
	}
	
	public CellStatusManager getCellStatusManager(LogicalModel m) {
		return this.model2StatusManagerMap.get(m);
	}
	
	public CellStatus getCellStatus(LogicalModel m, byte[] state) {
		return this.model2StatusManagerMap.get(m).getCellStatus(state);
	}
	
	public void setPattern(LogicalModel m, String pattern, CellStatus classification) {
		this.model2StatusManagerMap.get(m).addPattern(pattern, classification);
	}
	
	public void removePattern(LogicalModel m, String[] pattern, CellStatus classification) {
		this.model2StatusManagerMap.get(m).removePattern(pattern, classification);
	}
}
