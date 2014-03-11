package org.ginsim.epilog.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.colomoto.logicalmodel.NodeInfo;

public class EpitheliumIntegrationFunctions {
	private Map<String, ComponentIntegrationFunctions> functions;

	public EpitheliumIntegrationFunctions() {
		functions = new HashMap<String, ComponentIntegrationFunctions>();
	}

	public EpitheliumIntegrationFunctions clone() {
		EpitheliumIntegrationFunctions newEIF = new EpitheliumIntegrationFunctions();
		Map<String, ComponentIntegrationFunctions> f = new HashMap<String, ComponentIntegrationFunctions>(
				this.functions);
		newEIF.setFunctions(f);
		return newEIF;
	}

	private void setFunctions(Map<String, ComponentIntegrationFunctions> f) {
		this.functions = f;
	}

	public void setFunctionAtLevel(NodeInfo nodeInfo, byte value,
			String function) {
		this.functions.get(nodeInfo).setFunctionAtLevel(value, function);
	}

	public void addComponent(NodeInfo nodeInfo) {
		functions.put(nodeInfo.getNodeID(), new ComponentIntegrationFunctions(
				nodeInfo.getMax()));
	}

	public void removeComponent(NodeInfo c) {
		functions.remove(c.getNodeID());
	}

	public boolean containsKey(String component) {
		return this.functions.containsKey(component);
	}

	public Set<String> getComponents() {
		return Collections.unmodifiableSet(this.functions.keySet());
	}

	public ComponentIntegrationFunctions get(String component) {
		return this.functions.get(component);
	}
}
