package org.epilogtool.core.cellDynamics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.epilogtool.core.EpitheliumCellIdentifier;

public class EpitheliumCellConnections {
	
	private Map<EpitheliumCellIdentifier, Set<EpitheliumCellIdentifier>> connectionsMap;

	public EpitheliumCellConnections() {
		this.connectionsMap = new HashMap<EpitheliumCellIdentifier, Set<EpitheliumCellIdentifier>>();
	}
	 
	private EpitheliumCellConnections( Map<EpitheliumCellIdentifier, Set<EpitheliumCellIdentifier>> connectionsMap) {
		this.connectionsMap = connectionsMap;
	}
	
	public void addCellIdentifier(EpitheliumCellIdentifier cellID) {
		this.connectionsMap.put(cellID, new HashSet<EpitheliumCellIdentifier>());
	}
	
	public Set<EpitheliumCellIdentifier> get(EpitheliumCellIdentifier cellID) {
		return this.connectionsMap.get(cellID);
	}

	public void put(EpitheliumCellIdentifier cellID, Set<EpitheliumCellIdentifier> connectionSet) {
		this.connectionsMap.put(cellID, connectionSet);
		for (EpitheliumCellIdentifier connectedID : connectionSet) {
			this.connectionsMap.get(connectedID).add(cellID);
		}
	}
	
	public void append(EpitheliumCellIdentifier cellID1, EpitheliumCellIdentifier cellID2) {
		this.connectionsMap.get(cellID1).add(cellID2);
		this.connectionsMap.get(cellID2).add(cellID1);
	}
	
	public void append(EpitheliumCellIdentifier cellID, Set<EpitheliumCellIdentifier> connectionSet){
		for (EpitheliumCellIdentifier connectedID : connectionSet) {
			this.append(cellID, connectedID);
		}
	}
	
	public void delete(EpitheliumCellIdentifier cellID) {
		for (EpitheliumCellIdentifier connectedID : this.connectionsMap.get(cellID)) {
			this.connectionsMap.get(connectedID).remove(cellID);
		}
		this.connectionsMap.remove(cellID);
	}
	
	public void remove(EpitheliumCellIdentifier cellID1, EpitheliumCellIdentifier cellID2) {
		this.connectionsMap.get(cellID1).remove(cellID2);
		this.connectionsMap.get(cellID2).remove(cellID1);
	}
	
	public void retain(EpitheliumCellIdentifier cellID, 
			Set<EpitheliumCellIdentifier> currentNeighbours) {
		Set<EpitheliumCellIdentifier> cloneSet = new HashSet<EpitheliumCellIdentifier>(this.connectionsMap.get(cellID));
		for (EpitheliumCellIdentifier connectedID : cloneSet) {
			if (!currentNeighbours.contains(connectedID)) {
				this.remove(connectedID, cellID);
			}
		}
	}
	
	public void replaceMother(Map<EpitheliumCellIdentifier, Set<EpitheliumCellIdentifier>> daughters2neighboursMap) {
		
		List<EpitheliumCellIdentifier> daughterIDs = new ArrayList<EpitheliumCellIdentifier>(daughters2neighboursMap.keySet());
		EpitheliumCellIdentifier motherID =daughterIDs.get(0).getParent();
		assert this.connectionsMap.containsKey(motherID);
		for (EpitheliumCellIdentifier daughterID : daughterIDs) {
			this.put(daughterID, new HashSet<EpitheliumCellIdentifier>());
		}
		for (EpitheliumCellIdentifier daughterID : daughterIDs) {
			this.append(daughterID, daughters2neighboursMap.get(daughterID));
			this.retain(daughterID, this.connectionsMap.get(motherID));
		}
		this.delete(motherID);
		this.append(daughterIDs.get(0), daughterIDs.get(1));
	}

	public int size(EpitheliumCellIdentifier cellID) {
		return this.connectionsMap.get(cellID).size();
	}
	
	public int size() {
		return this.connectionsMap.keySet().size();
	}
	
	public boolean equals(Object o) {
		EpitheliumCellConnections oECC = (EpitheliumCellConnections) o;
		return this.connectionsMap.equals(oECC.connectionsMap);
	}
	
	public EpitheliumCellConnections clone() {
		return new EpitheliumCellConnections(new HashMap<EpitheliumCellIdentifier, Set<EpitheliumCellIdentifier>>(this.connectionsMap));
	}
	
	public String toString() {
		Map<Integer, Integer> tmpMap = new HashMap<Integer, Integer>();
		for (EpitheliumCellIdentifier cellID : this.connectionsMap.keySet()) {
			int connectionNumber = this.connectionsMap.get(cellID).size();
			assert connectionNumber < 7;
			int cells4ConnectionNumber;
			if (tmpMap.containsKey(connectionNumber)) {
				cells4ConnectionNumber = tmpMap.get(connectionNumber);
			} else {
				cells4ConnectionNumber = 0;
			}
			cells4ConnectionNumber = cells4ConnectionNumber+1;
			tmpMap.put(connectionNumber, cells4ConnectionNumber);
		}
		String stg = "";
		for (int i = 0; i <= 6; i++) {
			stg +=(tmpMap.get(i)==null ? "0" : tmpMap.get(i)) + "\t";
		}
		return stg;
	}
	
}
