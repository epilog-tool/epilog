package org.epilogtool.core;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.colomoto.logicalmodel.LogicalModel;
import org.epilogtool.project.ComponentPair;

public class EpitheliumEnvironmentalInputs {
	
	private Set<ComponentPair> environmentInputs;

	public EpitheliumEnvironmentalInputs() {
		this.environmentInputs = new HashSet<ComponentPair>();
	}
	
	private EpitheliumEnvironmentalInputs(Set<ComponentPair> environmentInputs) {
		this.environmentInputs = environmentInputs;
	}
	
	public void addComponent(ComponentPair cp) {
		this.environmentInputs.add(cp);
	}
	
	public void removeComponent(ComponentPair cp) {
		this.environmentInputs.remove(cp);
	}
	
	public boolean containsComponent(ComponentPair cp) {
		return this.environmentInputs.contains(cp);
	}
	
	public void removeModel(LogicalModel m) {
		Set<ComponentPair> tmpSet = new HashSet<ComponentPair>(this.environmentInputs);
		for (ComponentPair cp : tmpSet) {
			if (cp.getModel().equals(m)) {
				this.environmentInputs.remove(cp);
			}
		}
	}
	
	public Set<ComponentPair> getAllEnvironmentalComponents() {
		return Collections.unmodifiableSet(this.environmentInputs);
	}
	
	public Set<ComponentPair> getModelEnvironmentalComponents(LogicalModel m) {
		Set<ComponentPair> modelEnvComponents = new HashSet<ComponentPair>();
		for (ComponentPair cp : this.environmentInputs) {
			if (cp.getModel().equals(m)) {
				modelEnvComponents.add(cp);
			}
		}
		return modelEnvComponents;
	}
	
	public EpitheliumEnvironmentalInputs clone() {
		return new EpitheliumEnvironmentalInputs(new HashSet<ComponentPair>(this.environmentInputs));
	}

}
