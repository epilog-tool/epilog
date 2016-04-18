package org.epilogtool.core.cellDynamics;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CellStatusManager {
	
	private Map<CellStatus, Set<String[]>> status2patternMap;
	
	public CellStatusManager() {
		this.buildStatusMap();
	}
	
	private CellStatusManager(Map<CellStatus, Set<String[]>> class2Pattern) {
		this.status2patternMap = class2Pattern;
	}
	
	public CellStatusManager clone() {
		Map<CellStatus, Set<String[]>> class2Pattern = 
				new HashMap<CellStatus,Set<String[]>>(this.status2patternMap);
		return new CellStatusManager(class2Pattern);
	}
	
	public boolean equals(Object o) {
		CellStatusManager other = (CellStatusManager) o;
		return this.status2patternMap.equals(other.status2patternMap);
	}
	
	public Map<CellStatus, Set<String[]>> getStatus2PatternMap() {
		return this.status2patternMap;
	}
	
	private void buildStatusMap() {
		this.status2patternMap = new HashMap<CellStatus, Set<String[]>>();
		this.status2patternMap.put(CellStatus.PROLIFERATION, new HashSet<String[]>());
		this.status2patternMap.put(CellStatus.APOPTOSIS, new HashSet<String[]>());
		this.status2patternMap.put(CellStatus.ONCOSIS, new HashSet<String[]>());
	}
	
	public void addPattern(String patternString, CellStatus classification) {
		if (classification==CellStatus.DEFAULT) {
			return;
		}
		patternString = patternString.replace(" ", "");
		String[] patternArray = patternString.substring(1, patternString.length()-1).split(",");
		this.addPattern(patternArray, classification);
	}
	
	private void addPattern(String[] patternArray, CellStatus classification) {
		this.status2patternMap.get(classification).add(patternArray);
	}
	
	public void removePattern(String[] pattern, CellStatus classification) {
		this.status2patternMap.get(classification).remove(pattern);
	}
	
	public CellStatus getCellStatus(byte[] stateArray) {
		String tmpString = Arrays.toString(stateArray).replace(" ", "");
		String[] stateString = tmpString.substring(1, tmpString.length()-1).split(",");
		for (CellStatus classification : this.status2patternMap.keySet()) {
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
		return CellStatus.DEFAULT;
	}
}
