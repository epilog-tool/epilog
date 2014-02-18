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

	private Epithelium(String name) {
		this.name = name;
	}

	public Epithelium clone() {
		Epithelium newepi = new Epithelium("CopyOf_" + this.name);
		newepi.setEpitheliumGrid(this.grid.clone());
		newepi.setPriorityClasses(this.priorities.clone());
		newepi.setIntegrationFunctions(this.integrationFunctions.clone());
		newepi.setPerturbations(this.perturbations.clone());
		return newepi;
	}

	public EpitheliumGrid getEpitheliumGrid() {
		return this.grid;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEpitheliumGrid(EpitheliumGrid grid) {
		this.grid = grid;
	}

	public void setPriorityClasses(EpitheliumPriorityClasses pc) {
		this.priorities = pc;
	}

	public LogicalModel getModel(int x, int y) {
		return this.grid.getModel(x, y);
	}

	public ModelPriorityClasses getPriorityClasses(LogicalModel m) {
		return this.priorities.getModelPriorityClasses(m);
	}

	public EpitheliumIntegrationFunctions getIntegrationFunctions() {
		return this.integrationFunctions;
	}

	public void setIntegrationFunctions(EpitheliumIntegrationFunctions eif) {
		this.integrationFunctions = eif;
	}

	public EpitheliumAbstractPerturbations getPerturbations() {
		return this.perturbations;
	}

	public void setPerturbations(EpitheliumAbstractPerturbations eap) {
		this.perturbations = eap;
	}
}
