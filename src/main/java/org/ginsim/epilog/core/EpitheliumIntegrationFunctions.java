package org.ginsim.epilog.core;

import java.util.HashMap;
import java.util.Map;

import org.colomoto.logicalmodel.NodeInfo;

public class EpitheliumIntegrationFunctions {
	private Map<String, ComponentIntegrationFunctions> functions;

	public EpitheliumIntegrationFunctions() {
		functions = new HashMap<String, ComponentIntegrationFunctions>();
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
