package org.ginsim.epilog.core;

import java.util.HashMap;
import java.util.Map;

import org.colomoto.logicalmodel.LogicalModel;

public class EpitheliumAbstractPerturbations {
	private Map<LogicalModel, ModelAbstractPerturbations> perturbations;

	public EpitheliumAbstractPerturbations() {
		this.perturbations = new HashMap<LogicalModel, ModelAbstractPerturbations>();
	}

	public EpitheliumAbstractPerturbations clone() {
		EpitheliumAbstractPerturbations newEAP = new EpitheliumAbstractPerturbations();
		Map<LogicalModel, ModelAbstractPerturbations> p = new HashMap<LogicalModel, ModelAbstractPerturbations>(
				this.perturbations);
		newEAP.setPerturbations(p);
		return newEAP;
	}

	public void setPerturbations(Map<LogicalModel, ModelAbstractPerturbations> p) {
		this.perturbations = p;
	}
}
