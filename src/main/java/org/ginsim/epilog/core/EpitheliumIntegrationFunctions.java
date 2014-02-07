package org.ginsim.epilog.core;

import java.util.HashMap;
import java.util.List;
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

	public boolean isIntegrationNode(NodeInfo c) {
		return functions.containsKey(c.getNodeID());
	}

	public List<String> getIntegrationFunctions(NodeInfo c) {
		return functions.get(c.getNodeID()).get();
	}
	
	public void setIntegrationFunctions(NodeInfo c, List<String> integrationFunctions) {
		functions.get(c.getNodeID()).set(integrationFunctions);
	}
}
