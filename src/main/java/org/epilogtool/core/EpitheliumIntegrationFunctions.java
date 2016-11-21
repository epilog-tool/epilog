package org.epilogtool.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.epilogtool.project.ComponentPair;

public class EpitheliumIntegrationFunctions {
	private Map<ComponentPair, ComponentIntegrationFunctions> functions;

	public EpitheliumIntegrationFunctions() {
		this.functions = new HashMap<ComponentPair, ComponentIntegrationFunctions>();
	}

	public EpitheliumIntegrationFunctions clone() {
		EpitheliumIntegrationFunctions newEIF = new EpitheliumIntegrationFunctions();
		Map<ComponentPair, ComponentIntegrationFunctions> newFuncs = new HashMap<ComponentPair, ComponentIntegrationFunctions>();
		for (ComponentPair cp : this.functions.keySet()) {
			newFuncs.put(cp, this.functions.get(cp).clone());
		}
		newEIF.functions = newFuncs;
		return newEIF;
	}

	public void setFunctionAtLevel(ComponentPair cp, byte value, String function) {
		this.functions.get(cp).setFunctionAtLevel(value, function);
	}

	public void addComponent(ComponentPair cp) {
		this.addComponentFunctions(cp, new ComponentIntegrationFunctions(cp
				.getNodeInfo().getMax()));
	}

	public void addComponentFunctions(ComponentPair cp,
			ComponentIntegrationFunctions funcs) {
		this.functions.put(cp, funcs);
	}

	public void removeComponent(ComponentPair cp) {
		this.functions.remove(cp);
	}

	public boolean containsComponentPair(ComponentPair cp) {
		return this.functions.containsKey(cp);
	}

	public Set<ComponentPair> getComponentPair() {
		return Collections.unmodifiableSet(this.functions.keySet());
	}

	public ComponentIntegrationFunctions getComponentIntegrationFunctions(
			ComponentPair cp) {
		return this.functions.get(cp);
	}
	
	public Map<ComponentPair, ComponentIntegrationFunctions> getAllIntegrationFunctions() {
		return this.functions;
	}

	public boolean equals(Object o) {
		EpitheliumIntegrationFunctions eifOut = (EpitheliumIntegrationFunctions) o;
		Set<ComponentPair> sAllNodes = new HashSet<ComponentPair>();
		sAllNodes.addAll(this.functions.keySet());
		sAllNodes.addAll(eifOut.functions.keySet());
		for (ComponentPair cp : sAllNodes) {
			if (!this.functions.containsKey(cp)
					|| !eifOut.functions.containsKey(cp))
				return false;
			if (!this.functions.get(cp).equals(eifOut.functions.get(cp)))
				return false;
		}
		return true;
	}
}
