package org.epilogtool.core.cellDynamics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ModelEventManager {
	
	private Map<CellularEvent, List<ModelPattern>> event2patternMap;
	
	public ModelEventManager() {
		this.buildEvent2PatternMap();
	}

	private void buildEvent2PatternMap() {
		this.event2patternMap = new HashMap<CellularEvent, List<ModelPattern>>();
		this.event2patternMap.put(CellularEvent.PROLIFERATION, new ArrayList<ModelPattern>());
		this.event2patternMap.put(CellularEvent.APOPTOSIS, new ArrayList<ModelPattern>());
	}
	
	private ModelEventManager(Map<CellularEvent, List<ModelPattern>> event2Pattern) {
		this.event2patternMap = event2Pattern;
	}
	
	public ModelEventManager clone() {
		Map<CellularEvent, List<ModelPattern>> event2Pattern = 
				new HashMap<CellularEvent, List<ModelPattern>>();
		for (CellularEvent event : this.event2patternMap.keySet()) {
			event2Pattern
			.put(event, new ArrayList<ModelPattern>(this.event2patternMap.get(event)));
		}
		return new ModelEventManager(event2Pattern);
	}
	
	public boolean equals(Object o) {
		ModelEventManager other = (ModelEventManager) o;
		return this.event2patternMap.equals(other.event2patternMap);
	}
	
	public Map<CellularEvent, List<ModelPattern>> getEvent2PatternMap() {
		return this.event2patternMap;
	}
	
	public Set<CellularEvent> getCellularEventSet() {
		return this.event2patternMap.keySet();
	}
	
	public List<ModelPattern> getModelEventPatterns(CellularEvent event) {
		return this.event2patternMap.get(event);
	}
	
	public void addPattern(ModelPattern pattern, CellularEvent event) {
		if (event.equals(CellularEvent.DEFAULT)) {
			return;
		}
		this.event2patternMap.get(event).add(pattern);
	}
	
	public void removePattern(int patternIndex, CellularEvent event) {
		this.event2patternMap.get(event).remove(patternIndex);
	}
	
	public void removePattern(ModelPattern pattern, CellularEvent event) {
		this.event2patternMap.get(event).remove(pattern);
	}
	
	public CellularEvent getModelEvent(byte[] state) {
		for (CellularEvent event : this.event2patternMap.keySet()) {
			for (ModelPattern pattern : this.event2patternMap.get(event)) {
				if (pattern.containsState(state)) {
					return event;
				}
			}
		}
		return CellularEvent.DEFAULT;
	}
}
