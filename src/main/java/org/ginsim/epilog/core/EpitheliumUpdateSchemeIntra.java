package org.ginsim.epilog.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.colomoto.logicalmodel.LogicalModel;

public class EpitheliumUpdateSchemeIntra {
	private Map<LogicalModel, ModelPriorityClasses> priorityClassSet;

	public EpitheliumUpdateSchemeIntra() {
		this.priorityClassSet = new HashMap<LogicalModel, ModelPriorityClasses>();
	}

	public void addModel(LogicalModel m) {
		this.priorityClassSet.put(m, new ModelPriorityClasses(m));
	}

	public void removeModel(LogicalModel m) {
		if (this.priorityClassSet.containsKey(m))
			this.priorityClassSet.remove(m);
	}

	public ModelPriorityClasses getModelPriorityClasses(LogicalModel m) {
		return this.priorityClassSet.get(m);
	}

	public void addModelPriorityClasses(ModelPriorityClasses mpc) {
		this.priorityClassSet.put(mpc.getModel(), mpc);
	}

	public Set<LogicalModel> getModelSet() {
		return this.priorityClassSet.keySet();
	}

	public EpitheliumUpdateSchemeIntra clone() {
		EpitheliumUpdateSchemeIntra newPCs = new EpitheliumUpdateSchemeIntra();
		for (LogicalModel m : this.priorityClassSet.keySet()) {
			newPCs.addModelPriorityClasses(this.getModelPriorityClasses(m)
					.clone());
		}
		return newPCs;
	}

	public boolean equals(Object o) {
		EpitheliumUpdateSchemeIntra epcOut = (EpitheliumUpdateSchemeIntra) o;
		Set<LogicalModel> sAllModels = new HashSet<LogicalModel>();
		sAllModels.addAll(this.priorityClassSet.keySet());
		sAllModels.addAll(epcOut.priorityClassSet.keySet());
		for (LogicalModel m : sAllModels) {
			if (!this.priorityClassSet.containsKey(m)
					|| !epcOut.priorityClassSet.containsKey(m))
				return false;
			if (!this.priorityClassSet.get(m).equals(
					epcOut.priorityClassSet.get(m)))
				return false;
		}
		return true;
	}
}
