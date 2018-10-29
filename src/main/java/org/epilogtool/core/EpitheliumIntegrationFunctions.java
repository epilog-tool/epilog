package org.epilogtool.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.antlr.v4.runtime.RecognitionException;
import org.colomoto.biolqm.NodeInfo;

public class EpitheliumIntegrationFunctions {
	private Map<NodeInfo, ComponentIntegrationFunctions> functions;

	public EpitheliumIntegrationFunctions() {
		this.functions = new HashMap<NodeInfo, ComponentIntegrationFunctions>();
	}

	public EpitheliumIntegrationFunctions clone() {
		EpitheliumIntegrationFunctions newEIF = new EpitheliumIntegrationFunctions();
		
		Map<NodeInfo, ComponentIntegrationFunctions> newFuncs = new HashMap<NodeInfo, ComponentIntegrationFunctions>();
		for (NodeInfo node : this.functions.keySet()) {
			newFuncs.put(node, this.functions.get(node).clone());
		}
		newEIF.functions = newFuncs;
		return newEIF;
	}

	public void setFunctionAtLevel(NodeInfo node, byte value, String function)
			throws RuntimeException {
		this.functions.get(node).setFunctionAtLevel(value, function);
	}

	public void addComponent(NodeInfo node) {
		this.addComponentFunctions(node, new ComponentIntegrationFunctions(node.getMax()));
	}

	public void addComponentFunctions(NodeInfo node, ComponentIntegrationFunctions funcs) {
		this.functions.put(node, funcs);
	}

	public void removeComponent(NodeInfo node) {
		this.functions.remove(node);
	}

	public boolean containsNode(NodeInfo node) {
		return this.functions.containsKey(node);
	}

	public Set<NodeInfo> getNodes() {
		return Collections.unmodifiableSet(this.functions.keySet());
	}

	public ComponentIntegrationFunctions getComponentIntegrationFunctions(NodeInfo node) {
		return this.functions.get(node);
	}

	public Map<NodeInfo, ComponentIntegrationFunctions> getAllIntegrationFunctions() {
		return this.functions;
	}

	public boolean equals(Object o) {
		EpitheliumIntegrationFunctions eifOut = (EpitheliumIntegrationFunctions) o;
		Set<NodeInfo> sAllNodes = new HashSet<NodeInfo>();
		sAllNodes.addAll(this.functions.keySet());
		sAllNodes.addAll(eifOut.functions.keySet());
		for (NodeInfo node: sAllNodes) {
			if (!this.functions.containsKey(node) || !eifOut.functions.containsKey(node))
				return false;
			if (!this.functions.get(node).equals(eifOut.functions.get(node)))
				return false;
		}
		return true;
	}
}
