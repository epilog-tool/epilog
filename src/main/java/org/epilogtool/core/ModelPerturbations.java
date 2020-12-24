package org.epilogtool.core;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.colomoto.biolqm.modifier.perturbation.LogicalModelPerturbation;

public class ModelPerturbations {
	private List<LogicalModelPerturbation> allPerturbations;
	private Map<LogicalModelPerturbation, Color> usedPerturbations;

	public ModelPerturbations() {
		this.allPerturbations = new ArrayList<LogicalModelPerturbation>();
		this.usedPerturbations = new HashMap<LogicalModelPerturbation, Color>();
	}

	public ModelPerturbations clone() {
		ModelPerturbations mp = new ModelPerturbations();
		for (LogicalModelPerturbation ap : this.allPerturbations)
			mp.addPerturbation(ap);
		for (LogicalModelPerturbation ap : this.usedPerturbations.keySet())
			mp.addPerturbationColor(ap, this.usedPerturbations.get(ap));
		return mp;
	}

	public void addPerturbation(LogicalModelPerturbation ap) {
		this.allPerturbations.add(ap);
	}

	public void delPerturbation(LogicalModelPerturbation ap) {
		this.allPerturbations.remove(ap);
		this.usedPerturbations.remove(ap);
	}

	public Color getPerturbationColor(LogicalModelPerturbation ap) {
		return this.usedPerturbations.get(ap);
	}

	public void addPerturbationColor(LogicalModelPerturbation ap, Color c) {
		this.usedPerturbations.put(ap, c);
	}

	public void delPerturbationColor(LogicalModelPerturbation ap) {
		this.usedPerturbations.remove(ap);
	}

	public List<LogicalModelPerturbation> getAllPerturbations() {
		return Collections.unmodifiableList(this.allPerturbations);
	}

	public boolean equals(Object a) {
		ModelPerturbations mp = (ModelPerturbations) a;
		List<LogicalModelPerturbation> apList = new ArrayList<LogicalModelPerturbation>();
		apList.addAll(this.allPerturbations);
		apList.addAll(mp.getAllPerturbations());
		for (LogicalModelPerturbation ap : apList) {
			// Perturbation created in one and not the other
			if (!this.allPerturbations.contains(ap)
					|| !mp.getAllPerturbations().contains(ap))
				return false;
			// Color existing in one and not the other
			if (this.usedPerturbations.get(ap) != null
					&& mp.getPerturbationColor(ap) == null
					|| this.usedPerturbations.get(ap) == null
					&& mp.getPerturbationColor(ap) != null)
				return false;
		}
		return true;
	}
}
