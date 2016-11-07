package org.epilogtool.core.cellDynamics;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.colomoto.logicalmodel.LogicalModel;

public class ModelPattern {
	
	private String expression;
	private Map<Byte, byte[]> pattern;

	public ModelPattern() {
		this.expression = null;
		this.pattern = null;
	}
	
	public void setPatternExpression(String expression) {
		this.expression = expression;
	}
	
	public String getPatternExpression() {
		return this.expression;
	}
	
	private ModelPattern(String expression, Map<Byte, byte[]> pattern) {
		this.expression = expression;
		this.pattern = pattern;
	}
	
	public void setComputedPattern(LogicalModel m) {
		this.pattern = new HashMap<Byte, byte[]>();
		String[] tmpArray = this.expression.split(" ");
		for (String atom : tmpArray) {
			String nodeID = atom.split("\\(")[0];
			byte nodeIndex = -1;
			for (byte i = 0; i < m.getNodeOrder().size(); i ++){
				if (m.getNodeOrder().get(i).getNodeID().equals(nodeID)) {
					nodeIndex = i;
					break;
				}
			}
			byte minValue = Byte.parseByte(atom.split("\\(")[1].split(":")[0]);
			byte maxValue = Byte.parseByte(atom.split("\\(")[1].split(":")[1].split("\\)")[0]);
			this.pattern.put(nodeIndex, new byte[]{minValue, maxValue});
		}
	}
	
	public Map<Byte, byte[]> getComputedPattern() {
		return this.pattern;
	}
	
	public boolean isExpressionValid(String expression, LogicalModel m) {
		boolean isValid = true;
		String[] tmpArray = expression.split(" ");
		for (String atom : tmpArray) {
			String nodeID = atom.split("\\(")[0];
			byte nodeIndex = -1;
			for (byte i = 0; i < m.getNodeOrder().size(); i ++){
				if (m.getNodeOrder().get(i).getNodeID().equals(nodeID)) {
					nodeIndex = i;
					break;
				}
			}
			if (nodeIndex == -1) {
				isValid = false;
				break;
			}
		}
		return isValid;
	}
	
	public boolean containsState(byte[] state) {
		//evaluates if a given state is part of the pattern;
		for (Byte index : this.pattern.keySet()) {
			byte indexValue = state[index];
			if (indexValue < this.pattern.get(index)[0] 
					|| indexValue > this.pattern.get(index)[1]) {
				return false;
			}
		}
		return true;
	}
	
	public boolean overlaps(Object o) {
		//Tests if two Patterns can contain the same state, making them "overlap"
		ModelPattern other = (ModelPattern) o;
		Set<Byte> thisPositions = this.pattern.keySet();
		Set<Byte> otherPositions = other.pattern.keySet();
		Set<Byte> commonTestSet = new HashSet<Byte>(thisPositions);
		commonTestSet.retainAll(otherPositions);
		//if pattern specification of positions have nothing in common, they overlap
		if (commonTestSet.size()==0) {
			return true;
		}
		//if there are common positions, test whether all ranges overlap
		// if one does not, they cannot overlap
		for (Byte position : commonTestSet) {
			byte[] thisRange = this.pattern.get(position);
			byte[] otherRange = other.pattern.get(position);
			if (!(thisRange[0]<=otherRange[1] && thisRange[1]>=otherRange[0])) {
				return false;
			}
		}
		return true;
	}
	
	public boolean equals(Object o) {
		ModelPattern other = (ModelPattern) o;
		return this.pattern.equals(other.pattern);
	}
	
	public ModelPattern clone() {
		return new ModelPattern(new String(this.expression), 
				new HashMap<Byte, byte[]>(this.pattern));
	}
}
