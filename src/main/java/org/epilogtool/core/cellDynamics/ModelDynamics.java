package org.epilogtool.core.cellDynamics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.colomoto.logicalmodel.LogicalModel;

public class ModelDynamics {
	
	private Map<CellTrigger, List<TriggerPattern>> trigger2patternMap;
	private float async;
	
	public ModelDynamics() {
		this.async = (float) 1.0;
		this.buildTrigger2PatternMap();
	}

	private void buildTrigger2PatternMap() {
		this.trigger2patternMap = new HashMap<CellTrigger, List<TriggerPattern>>();
		this.trigger2patternMap.put(CellTrigger.PROLIFERATION, new ArrayList<TriggerPattern>());
		this.trigger2patternMap.put(CellTrigger.APOPTOSIS, new ArrayList<TriggerPattern>());
	}
	
	private ModelDynamics(Float async, Map<CellTrigger, List<TriggerPattern>> trigger2Pattern) {
		this.async = async;
		this.trigger2patternMap = trigger2Pattern;
	}
	
	public ModelDynamics clone() {
		Map<CellTrigger, List<TriggerPattern>> trigger2Pattern = 
				new HashMap<CellTrigger, List<TriggerPattern>>();
		for (CellTrigger trigger : this.trigger2patternMap.keySet()) {
			trigger2Pattern
			.put(trigger, new ArrayList<TriggerPattern>(this.trigger2patternMap.get(trigger)));
		}
		return new ModelDynamics(this.async, trigger2Pattern);
	}
	
	public boolean equals(Object o) {
		ModelDynamics other = (ModelDynamics) o;
		return this.trigger2patternMap.equals(other.trigger2patternMap) 
				&& this.async==other.async;
	}
	
	public Map<CellTrigger, List<TriggerPattern>> getDynamicsMap() {
		return this.trigger2patternMap;
	}
	
	public Set<CellTrigger> getCellTriggerSet() {
		return this.trigger2patternMap.keySet();
	}
	
	public List<TriggerPattern> getTriggerPatterns(CellTrigger trigger) {
		return this.trigger2patternMap.get(trigger);
	}
	
	public float getAsync() {
		return this.async;
	}
	
	public void setAsync(float async) {
		this.async = async;
	}
	
	public void addPattern(TriggerPattern pattern, CellTrigger trigger) {
		if (trigger.equals(CellTrigger.DEFAULT)) {
			return;
		}
		this.trigger2patternMap.get(trigger).add(pattern);
	}
	
	public void removePattern(int patternIndex, CellTrigger trigger) {
		this.trigger2patternMap.get(trigger).remove(patternIndex);
	}
	
	public void removePattern(TriggerPattern pattern, CellTrigger trigger) {
		this.trigger2patternMap.get(trigger).remove(pattern);
	}
	
	public CellTrigger getCellTrigger(byte[] state) {
		for (CellTrigger trigger : this.trigger2patternMap.keySet()) {
			for (TriggerPattern pattern : this.trigger2patternMap.get(trigger)) {
				if (pattern.containsState(state)) {
					return trigger;
				}
			}
		}
		return CellTrigger.DEFAULT;
	}
}
