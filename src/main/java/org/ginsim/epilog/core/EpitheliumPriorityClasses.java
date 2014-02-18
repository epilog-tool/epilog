package org.ginsim.epilog.core;

import java.util.HashMap;
import java.util.Map;

import org.colomoto.logicalmodel.LogicalModel;

public class EpitheliumPriorityClasses {
	private Map<LogicalModel, ModelPriorityClasses> priorityClassSet;

	public EpitheliumPriorityClasses() {
		this.priorityClassSet = new HashMap<LogicalModel, ModelPriorityClasses>();
	}

	public void addModel(LogicalModel m) {
		this.priorityClassSet.put(m, new ModelPriorityClasses(m));
	}

	public ModelPriorityClasses getModelPriorityClasses(LogicalModel m) {
		return this.priorityClassSet.get(m);
	}

	public void addModelPriorityClasses(ModelPriorityClasses mpc) {
		this.priorityClassSet.put(mpc.getModel(), mpc);
	}

	public EpitheliumPriorityClasses clone() {
		EpitheliumPriorityClasses newPCs = new EpitheliumPriorityClasses();
		for (LogicalModel m : this.priorityClassSet.keySet()) {
			newPCs.addModelPriorityClasses(this.getModelPriorityClasses(m)
					.clone());
		}
		return newPCs;
	}
}
