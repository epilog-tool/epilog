package org.ginsim.epilog.core;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.colomoto.logicalmodel.perturbation.AbstractPerturbation;
import org.ginsim.epilog.gui.color.ColorUtils;

public class ModelAbstractPerturbations {
	private List<AbstractPerturbation> allPerturbations;
	private Map<AbstractPerturbation, Color> usedPerturbations;

	public ModelAbstractPerturbations() {
		this.allPerturbations = new ArrayList<AbstractPerturbation>();
		this.usedPerturbations = new HashMap<AbstractPerturbation, Color>();
	}

	public void addPerturbation(AbstractPerturbation p) {
		this.allPerturbations.add(p);
	}

	public void delPerturbation(AbstractPerturbation p) {
		this.allPerturbations.remove(p);
		this.usedPerturbations.remove(p);
	}

	public void setPerturbationUsed(AbstractPerturbation p, boolean used) {
		if (this.usedPerturbations.containsKey(p)) {
			if (!used) {
				this.usedPerturbations.remove(p);
			}
		} else if (used) {
			this.usedPerturbations.put(p, ColorUtils.random());
		}
	}

	public Color getPerturbationColor(AbstractPerturbation p) {
		return this.usedPerturbations.get(p);
	}

	public void setPerturbationColor(AbstractPerturbation p, Color c) {
		this.usedPerturbations.put(p, c);
	}
}
