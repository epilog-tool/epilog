package org.epilogtool.core.cellDynamics;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.epilogtool.common.Tuple2D;

public class CellLineage {
	
	private Map<Tuple2D<Integer>, Tuple2D<Integer>> daughterMap;
	private Map<Tuple2D<Integer>, Set<Tuple2D<Integer>>> motherMap;

	public CellLineage() {
		this.daughterMap = new HashMap<Tuple2D<Integer>, Tuple2D<Integer>>();
		this.motherMap = new HashMap<Tuple2D<Integer>, Set<Tuple2D<Integer>>>();
	}
	
	private CellLineage(Map<Tuple2D<Integer>, Tuple2D<Integer>> daughterMap, Map<Tuple2D<Integer>, Set<Tuple2D<Integer>>> motherMap) {
		this.daughterMap = daughterMap;
		this.motherMap = motherMap;
	}
	
	public void addCellConnection(Tuple2D<Integer> motherCell, Tuple2D<Integer> daughterCell) {
		this.daughterMap.put(daughterCell, motherCell);
		if (!this.motherMap.containsKey(motherCell)) {
			this.motherMap.put(motherCell, new HashSet<Tuple2D<Integer>>());
		}
		this.motherMap.get(motherCell).add(daughterCell);
	}
	
	public void removeCellConnection(Tuple2D<Integer> cellPos1, Tuple2D<Integer> cellPos2) {
		if (cellPos1.equals(this.getMotherOf(cellPos2))) {
			this.daughterMap.remove(cellPos2);
			this.motherMap.get(cellPos1).remove(cellPos2);
		} else if (cellPos2.equals(this.getMotherOf(cellPos1))) {
			this.daughterMap.remove(cellPos1);
			this.motherMap.get(cellPos2).remove(cellPos1);
		}
	}
	
	public void removeCell(Tuple2D<Integer> cell) {
		Tuple2D<Integer> motherCell = this.daughterMap.get(cell);
		this.daughterMap.remove(cell);
		this.motherMap.remove(cell);
		this.motherMap.get(motherCell).remove(cell);
	}
	
	public Tuple2D<Integer> getMotherOf(Tuple2D<Integer> daughterCell) {
		return this.daughterMap.get(daughterCell);
	}
	
	public Set<Tuple2D<Integer>> getDaughtersOf(Tuple2D<Integer> motherCell) {
		return new HashSet<Tuple2D<Integer>>(this.motherMap.get(motherCell));
	}
	
	public void changeIndexOf(Tuple2D<Integer> originalPos, Tuple2D<Integer> finalPos) {
		Tuple2D<Integer> motherCell = this.daughterMap.get(originalPos);
		this.daughterMap.remove(originalPos);
		this.daughterMap.put(finalPos, motherCell);
		this.motherMap.get(motherCell).remove(originalPos);
		this.motherMap.get(motherCell).add(finalPos);
	}
	
	public CellLineage clone() {
		return new CellLineage(new HashMap<Tuple2D<Integer>, Tuple2D<Integer>>(this.daughterMap),
				new HashMap<Tuple2D<Integer>, Set<Tuple2D<Integer>>>(this.motherMap));
	}
	
}
