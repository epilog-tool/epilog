package org.ginsim.epilog.core;

import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.perturbation.AbstractPerturbation;

public class EpitheliumPerturbations {
	private Map<LogicalModel, ModelPerturbations> perturbations;

	public EpitheliumPerturbations() {
		this.perturbations = new HashMap<LogicalModel, ModelPerturbations>();
	}

	public EpitheliumPerturbations clone() {
		EpitheliumPerturbations epiPerturb = new EpitheliumPerturbations();
		for (LogicalModel m : this.getModelSet())
			epiPerturb.addModelPerturbation(m, this.getModelPerturbations(m).clone());
		return epiPerturb;
	}

	private void addModelPerturbation(LogicalModel m, ModelPerturbations mp) {
		this.perturbations.put(m, mp);
	}

	public Set<LogicalModel> getModelSet() {
		return this.perturbations.keySet();
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
