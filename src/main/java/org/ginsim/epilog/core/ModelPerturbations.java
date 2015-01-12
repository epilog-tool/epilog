package org.ginsim.epilog.core;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.colomoto.logicalmodel.perturbation.AbstractPerturbation;

public class ModelPerturbations {
	private List<AbstractPerturbation> allPerturbations;
	private Map<AbstractPerturbation, Color> usedPerturbations;

	public ModelPerturbations() {
		this.allPerturbations = new ArrayList<AbstractPerturbation>();
		this.usedPerturbations = new HashMap<AbstractPerturbation, Color>();
	}

	public ModelPerturbations clone() {
		ModelPerturbations mp = new ModelPerturbations();
		for (AbstractPerturbation ap : this.allPerturbations)
			mp.addPerturbation(ap);
		for (AbstractPerturbation ap : this.usedPerturbations.keySet())
			mp.addPerturbationColor(ap, this.usedPerturbations.get(ap));
		return mp;
	}

	public void addPerturbation(AbstractPerturbation ap) {
		this.allPerturbations.add(ap);
	}

	public void delPerturbation(AbstractPerturbation ap) {
		this.allPerturbations.remove(ap);
		this.usedPerturbations.remove(ap);
	}

	public Color getPerturbationColor(AbstractPerturbation ap) {
		return this.usedPerturbations.get(ap);
	}

	public void addPerturbationColor(AbstractPerturbation ap, Color c) {
		this.usedPerturbations.put(ap, c);
	}

	public void delPerturbationColor(AbstractPerturbation ap) {
		this.usedPerturbations.remove(ap);
	}

	public List<AbstractPerturbation> getAllPerturbations() {
		return Collections.unmodifiableList(this.allPerturbations);
	}

	public boolean equals(Object a) {
		ModelPerturbations mp = (ModelPerturbations) a;
		List<AbstractPerturbation> apList = new ArrayList<AbstractPerturbation>();
		apList.addAll(this.allPerturbations);
		apList.addAll(mp.getAllPerturbations());
		for (AbstractPerturbation ap : apList) {
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
