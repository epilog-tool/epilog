package org.epilogtool.core.cellDynamics;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.antlr.runtime.RecognitionException;
import org.colomoto.logicalmodel.LogicalModel;

public class ModelEventManager {

	private Map<LogicalModel, Map<CellularEvent, ModelEventExpression>> model2EventsMap;

	public ModelEventManager() {
		this.model2EventsMap = new HashMap<LogicalModel, Map<CellularEvent, ModelEventExpression>>();
	}

	public ModelEventManager(Set<LogicalModel> modelSet) {
		this.model2EventsMap = new HashMap<LogicalModel, Map<CellularEvent, ModelEventExpression>>();
		for (LogicalModel m : modelSet) {
			this.addModel(m);
		}
	}

	private ModelEventManager(Map<LogicalModel, Map<CellularEvent, ModelEventExpression>> model2ManagerMap) {
		this.model2EventsMap = model2ManagerMap;
	}

	public ModelEventManager clone() {
		Map<LogicalModel, Map<CellularEvent, ModelEventExpression>> tmpMap = new HashMap<LogicalModel, Map<CellularEvent, ModelEventExpression>>();
		for (LogicalModel m : this.model2EventsMap.keySet()) {
			tmpMap.put(m, new HashMap<CellularEvent, ModelEventExpression>(this.model2EventsMap.get(m)));
		}
		return new ModelEventManager(tmpMap);
	}

	public boolean equals(Object o) {
		ModelEventManager other = (ModelEventManager) o;
		return this.model2EventsMap.equals(other.model2EventsMap);
	}

	public void addModel(LogicalModel m) {
		this.model2EventsMap.put(m, new HashMap<CellularEvent, ModelEventExpression>());
	}

	public void removeModel(LogicalModel m) {
		this.model2EventsMap.remove(m);
	}

	public boolean hasModel(LogicalModel m) {
		return this.model2EventsMap.keySet().contains(m);
	}

	public boolean containsModel(LogicalModel m) {
		return this.model2EventsMap.containsKey(m);
	}

	public Set<CellularEvent> getModelEvents(LogicalModel m) {
		return this.model2EventsMap.get(m).keySet();
	}

	public void setModelEventExpression(LogicalModel m, CellularEvent event, ModelEventExpression meExpr)
			throws RecognitionException, RuntimeException {
		this.model2EventsMap.get(m).put(event, meExpr);
		meExpr.setComputedExpression();
	}

	public ModelEventExpression getModelEventExpression(LogicalModel m, CellularEvent event) {
		if (!this.model2EventsMap.get(m).containsKey(event)) {
			return null;
		}
		return this.model2EventsMap.get(m).get(event);
	}

	public void removeCellularEvent(LogicalModel m, CellularEvent event) {
		this.model2EventsMap.get(m).remove(event);
	}
}
