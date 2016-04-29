package org.epilogtool.core.cellDynamics;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CellTriggerManager {
	
	private Map<CellTrigger, Set<String[]>> status2patternMap;
	
	public CellTriggerManager() {
		this.buildStatusMap();
	}
	
	private CellTriggerManager(Map<CellTrigger, Set<String[]>> class2Pattern) {
		this.status2patternMap = class2Pattern;
	}
	
	public CellTriggerManager clone() {
		Map<CellTrigger, Set<String[]>> class2Pattern = 
				new HashMap<CellTrigger,Set<String[]>>(this.status2patternMap);
		return new CellTriggerManager(class2Pattern);
	}
	
	public boolean equals(Object o) {
		CellTriggerManager other = (CellTriggerManager) o;
		return this.status2patternMap.equals(other.status2patternMap);
	}
	
	public Map<CellTrigger, Set<String[]>> getTrigger2PatternMap() {
		return this.status2patternMap;
	}
	
	private void buildStatusMap() {
		this.status2patternMap = new HashMap<CellTrigger, Set<String[]>>();
		this.status2patternMap.put(CellTrigger.PROLIFERATION, new HashSet<String[]>());
		this.status2patternMap.put(CellTrigger.APOPTOSIS, new HashSet<String[]>());
		this.status2patternMap.put(CellTrigger.ONCOSIS, new HashSet<String[]>());
	}
	
	public void addPattern(String patternString, CellTrigger classification) {
		if (classification==CellTrigger.DEFAULT) {
			return;
		}
		patternString = patternString.replace(" ", "");
		String[] patternArray = patternString.substring(1, patternString.length()-1).split(",");
		this.addPattern(patternArray, classification);
	}
	
	private void addPattern(String[] patternArray, CellTrigger classification) {
		this.status2patternMap.get(classification).add(patternArray);
	}
	
	public void removePattern(String[] pattern, CellTrigger classification) {
		this.status2patternMap.get(classification).remove(pattern);
	}
	
	public CellTrigger getCellStatus(byte[] stateArray) {
		String tmpString = Arrays.toString(stateArray).replace(" ", "");
		String[] stateString = tmpString.substring(1, tmpString.length()-1).split(",");
		for (CellTrigger classification : this.status2patternMap.keySet()) {
			for (String[] pattern : this.status2patternMap.get(classification)) {
				String[] tmpState = stateString.clone();
				for (int index = 0; index < pattern.length; index ++) {
					if (pattern[index]=="_") {
						tmpState[index] = "_";
					}
				}
				if (Arrays.equals(tmpState, pattern)) {
					return classification;
				}
			}
		}
		return CellTrigger.DEFAULT;
	}
}
