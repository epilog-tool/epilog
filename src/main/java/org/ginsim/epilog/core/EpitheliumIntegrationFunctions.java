package org.ginsim.epilog.core;

import java.util.HashMap;
import java.util.Map;

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

	public void setFunctions(Map<String, ComponentIntegrationFunctions> f) {
		this.functions = f;
	}

	public void addComponent(NodeInfo c) {
		functions.put(c.getNodeID(),
				new ComponentIntegrationFunctions(c.getMax()));
	}

	public void removeComponent(NodeInfo c) {
		functions.remove(c.getNodeID());
	}

	public boolean containsKey(String component) {
		return this.functions.containsKey(component);
	}

	public ComponentIntegrationFunctions get(String component) {
		return this.functions.get(component);
	}
}
