package org.ginsim.epilog.core;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.perturbation.AbstractPerturbation;

public class EpitheliumPerturbations {
	private Map<LogicalModel, ModelPerturbations> perturbations;

	public EpitheliumPerturbations() {
		this.perturbations = new HashMap<LogicalModel, ModelPerturbations>();
	}

	public EpitheliumPerturbations clone() {
		EpitheliumPerturbations epiPerturb = new EpitheliumPerturbations();
		Map<LogicalModel, ModelPerturbations> p = new HashMap<LogicalModel, ModelPerturbations>(
				this.perturbations);
		epiPerturb.setPerturbations(p);
		return epiPerturb;
	}

	public void setPerturbations(Map<LogicalModel, ModelPerturbations> p) {
		this.perturbations = p;
	}

	public void addPerturbation(LogicalModel m, AbstractPerturbation ap) {
		if (!this.perturbations.containsKey(m)) {
			ModelPerturbations mp = new ModelPerturbations();
			this.perturbations.put(m, mp);
		}
		this.perturbations.get(m).addPerturbation(ap);
	}

	public void addPerturbationColor(LogicalModel m, AbstractPerturbation ap,
			Color c) {
		this.perturbations.get(m).addPerturbationColor(ap, c);
	}

	public ModelPerturbations getModelPerturbations(LogicalModel m) {
		return this.perturbations.get(m);
	}
}
