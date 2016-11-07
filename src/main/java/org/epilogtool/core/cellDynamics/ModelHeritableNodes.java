package org.epilogtool.core.cellDynamics;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;

public class ModelHeritableNodes {
	
	private Map<LogicalModel, Set<String>> heritableNodesMap;
	
	public ModelHeritableNodes() {
		this.heritableNodesMap = new HashMap<LogicalModel, Set<String>>();
	}
	
	private ModelHeritableNodes(Map<LogicalModel, Set<String>> heritableNodesMap) {
		this.heritableNodesMap = new HashMap<LogicalModel, Set<String>>(heritableNodesMap);
	}
	
	public void addModel(LogicalModel m) {
		this.heritableNodesMap.put(m, new HashSet<String>());
	}
	
	public void removeModel(LogicalModel m) {
		this.heritableNodesMap.remove(m);
	}
	
	public boolean hasModel(LogicalModel m) {
		return this.heritableNodesMap.containsKey(m);
	}
	
	public void addNode(LogicalModel m, String node) {
		if (this.heritableNodesMap.containsKey(m)) {
			this.heritableNodesMap.get(m).add(node);
		}
		this.addModel(m);
		this.heritableNodesMap.get(m).add(node);
	}
	
	public void removeNode(LogicalModel m, String node) {
		this.heritableNodesMap.get(m).remove(node);
	}
	
	public boolean isHeritableNode(LogicalModel m, String node) {
		return this.heritableNodesMap.get(m).contains(node);
	}
	
	public Set<String> getModelHeritableNodes(LogicalModel m) {
		return this.heritableNodesMap.get(m);
	}
	
	public void clearModelHeritableNodes(LogicalModel m) {
		this.heritableNodesMap.get(m).clear();
	}
	
	public void addAllModelNodes(LogicalModel m) {
		if (!this.hasModel(m)) {
			this.addModel(m);
		}
		for (NodeInfo node : m.getNodeOrder()) {
			this.addNode(m, node.getNodeID());
		}
	}
	
	public ModelHeritableNodes clone() {
		return new ModelHeritableNodes(this.heritableNodesMap);
	}
	
	public boolean equals(ModelHeritableNodes otherMHN){
		return this.heritableNodesMap.equals(otherMHN.heritableNodesMap);
	}
}
