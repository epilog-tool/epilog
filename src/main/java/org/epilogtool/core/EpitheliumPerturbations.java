package org.epilogtool.core;

import java.awt.Color;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.colomoto.biolqm.LogicalModel;
import org.colomoto.biolqm.modifier.perturbation.AbstractPerturbation;

public class EpitheliumPerturbations {
	private Map<LogicalModel, ModelPerturbations> perturbations;

	public EpitheliumPerturbations() {
		this.perturbations = new HashMap<LogicalModel, ModelPerturbations>();
	}

	public EpitheliumPerturbations clone() {
		EpitheliumPerturbations epiPerturb = new EpitheliumPerturbations();
		for (LogicalModel m : this.getModelSet())
			epiPerturb.addModelPerturbation(m, this.getModelPerturbations(m)
					.clone());
		return epiPerturb;
	}

	public boolean hasModel(LogicalModel m) {
		return this.perturbations.containsKey(m);
	}

	public void addModel(LogicalModel m) {
		this.perturbations.put(m, new ModelPerturbations());
	}

	public void addModelPerturbation(LogicalModel m, ModelPerturbations mp) {
		this.perturbations.put(m, mp);
	}

	public void removeModel(LogicalModel m) {
		if (this.perturbations.containsKey(m))
			this.perturbations.remove(m);
	}

	public Set<LogicalModel> getModelSet() {
		return Collections.unmodifiableSet(this.perturbations.keySet());
	}

	public void addPerturbation(LogicalModel m, AbstractPerturbation ap) {
		if (!this.perturbations.containsKey(m)) {
			ModelPerturbations mp = new ModelPerturbations();
			this.perturbations.put(m, mp);
		}
		this.perturbations.get(m).addPerturbation(ap);
	}

	public void delPerturbation(LogicalModel m, AbstractPerturbation ap) {
		if (this.perturbations.containsKey(m)) {
			ModelPerturbations mp = this.perturbations.get(m);
			mp.delPerturbation(ap);
		}
	}

	public void addPerturbationColor(LogicalModel m, AbstractPerturbation ap,
			Color c) {
		this.perturbations.get(m).addPerturbationColor(ap, c);
	}

	public ModelPerturbations getModelPerturbations(LogicalModel m) {
		return this.perturbations.get(m);
	}

	public boolean equals(Object o) {
		EpitheliumPerturbations ep = (EpitheliumPerturbations) o;
		Set<LogicalModel> sAllModels = new HashSet<LogicalModel>();
		sAllModels.addAll(this.getModelSet());
		sAllModels.addAll(ep.getModelSet());
		for (LogicalModel m : sAllModels) {
			// ModelPerturbations in one and not the other
			ModelPerturbations mpIn = this.getModelPerturbations(m);
			ModelPerturbations mpOut = ep.getModelPerturbations(m);
			if (mpIn == null || mpOut == null)
				return false;
			if (!mpIn.equals(mpOut))
				return false;
		}
		return true;
	}
}
