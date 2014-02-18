package org.ginsim.epilog.core;

import org.colomoto.logicalmodel.LogicalModel;

public class Epithelium {
	private String name;
	private EpitheliumGrid grid;
	private EpitheliumPriorityClasses priorities;
	private EpitheliumIntegrationFunctions integrationFunctions;
	private EpitheliumAbstractPerturbations perturbations;

	public Epithelium(int x, int y, LogicalModel m, String name) {
		this.name = name;
		this.grid = new EpitheliumGrid(x, y, m);
		this.priorities = new EpitheliumPriorityClasses();
		this.integrationFunctions = new EpitheliumIntegrationFunctions();
		this.perturbations = new EpitheliumAbstractPerturbations();
	}

	private Epithelium(String name, EpitheliumGrid grid,
			EpitheliumIntegrationFunctions eif, EpitheliumPriorityClasses epc,
			EpitheliumAbstractPerturbations eap) {
		this.name = name;
		this.grid = grid;
		this.priorities = epc;
		this.integrationFunctions = eif;
		this.perturbations = eap;
	}

	public Epithelium clone() {
		return new Epithelium("CopyOf_" + this.name, this.grid.clone(),
				this.integrationFunctions.clone(), this.priorities.clone(),
				this.perturbations.clone());
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LogicalModel getModel(int x, int y) {
		return this.grid.getModel(x, y);
	}

	public EpitheliumGrid getEpitheliumGrid() {
		return this.grid;
	}

	public ModelPriorityClasses getPriorityClasses(LogicalModel m) {
		return this.priorities.getModelPriorityClasses(m);
	}

	public ComponentIntegrationFunctions getIntegrationFunctions(String c) {
		return this.integrationFunctions.get(c);
	}
	
	public EpitheliumAbstractPerturbations getPerturbations() {
		return this.perturbations;
	}
}
