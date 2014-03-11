package org.ginsim.epilog.core;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.colomoto.logicalmodel.perturbation.AbstractPerturbation;
import org.ginsim.epilog.gui.color.ColorUtils;

public class ModelPerturbations {
	private List<AbstractPerturbation> allPerturbations;
	private Map<AbstractPerturbation, Color> usedPerturbations; // FIXME

	public ModelPerturbations() {
		this.allPerturbations = new ArrayList<AbstractPerturbation>();
		this.usedPerturbations = new HashMap<AbstractPerturbation, Color>();
	}

	public void addPerturbation(AbstractPerturbation ap) {
		this.allPerturbations.add(ap);
	}

	public void delPerturbation(AbstractPerturbation ap) {
		this.allPerturbations.remove(ap);
		this.usedPerturbations.remove(ap);
	}

	public void setPerturbationUsed(AbstractPerturbation ap, boolean used) {
		if (this.usedPerturbations.containsKey(ap)) {
			if (!used) {
				this.usedPerturbations.remove(ap);
			}
		} else if (used) {
			this.usedPerturbations.put(ap, ColorUtils.random());
		}
	}

	public Color getPerturbationColor(AbstractPerturbation ap) {
		return this.usedPerturbations.get(ap);
	}

	public void addPerturbationColor(AbstractPerturbation ap, Color c) {
		this.usedPerturbations.put(ap, c);
	}

	public List<AbstractPerturbation> getAllPerturbations() {
		return Collections.unmodifiableList(this.allPerturbations);
	}
}
