package org.ginsim.epilog.core;

import org.colomoto.logicalmodel.LogicalModel;

public class Epithelium {
	private EpitheliumGrid grid;
	private EpitheliumPriorityClasses priorities;
	private EpitheliumIntegrationFunctions integrationFunctions;
	
	public EpitheliumGrid getEpitheliumGrid() {
		return this.grid;
	}
	
	public void setEpitheliumGrid(EpitheliumGrid grid) {
		this.grid = grid;
	}
	
	public void setPriorityClasses(EpitheliumPriorityClasses pc) {
		this.priorities = pc;
	}
	
	public Epithelium clone() {
		Epithelium newepi = new Epithelium();
		newepi.setEpitheliumGrid(this.grid.clone());
		newepi.setPriorityClasses(this.priorities.clone());
		return newepi;
	}
	
	public int getX() {
		// TODO
		return 0;
	}
	public int getY() {
		// TODO
		return 0;
	}
	public LogicalModel getModel(int x, int y) {
		return this.grid.getModel(x, y);
	}
	
	public ModelPriorityClasses getPriorityClasses(LogicalModel m) {
		return this.priorities.getModelPriorityClasses(m);
	}
	
	public EpitheliumIntegrationFunctions getEpitheliumIntegrationFunctions() {
		return this.integrationFunctions;
	}
}
