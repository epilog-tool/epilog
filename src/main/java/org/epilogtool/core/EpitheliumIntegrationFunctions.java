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
		for (ComponentPair cf : this.functions.keySet()) {
			newFuncs.put(cf, this.functions.get(cf).clone());
		}
		newEIF.functions = newFuncs;
		return newEIF;
	}

	public void setFunctionAtLevel(ComponentPair cf, byte value, String function) {
		this.functions.get(cf).setFunctionAtLevel(value, function);
	}

	public void addComponent(ComponentPair cp) {
		this.addComponentFunctions(cp, new ComponentIntegrationFunctions(cp
				.getNodeInfo().getMax()));
	}

	public void addComponentFunctions(ComponentPair cf,
			ComponentIntegrationFunctions funcs) {
		this.functions.put(cf, funcs);
	}

	public void removeComponent(ComponentPair cf) {
		this.functions.remove(cf);
	}

	public boolean containsComponentPair(ComponentPair cp) {
		return this.functions.containsKey(cp);
	}

	public Set<ComponentPair> getComponentPair() {
		return Collections.unmodifiableSet(this.functions.keySet());
	}

	public ComponentIntegrationFunctions getComponentIntegrationFunctions(
			ComponentPair cf) {
		return this.functions.get(cf);
	}
	
	public Map<ComponentPair, ComponentIntegrationFunctions> getAllIntegrationFunctions() {
		return this.functions;
	}

	public boolean equals(Object o) {
		EpitheliumIntegrationFunctions eifOut = (EpitheliumIntegrationFunctions) o;
		Set<ComponentPair> sAllNodes = new HashSet<ComponentPair>();
		sAllNodes.addAll(this.functions.keySet());
		sAllNodes.addAll(eifOut.functions.keySet());
		for (ComponentPair cf : sAllNodes) {
			if (!this.functions.containsKey(cf)
					|| !eifOut.functions.containsKey(cf))
				return false;
			if (!this.functions.get(cf).equals(eifOut.functions.get(cf)))
				return false;
		}
		return true;
	}
}
