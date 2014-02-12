package org.ginsim.epilog.core;

public class Epithelium {
	private EpitheliumGrid grid;
	private EpitheliumPriorityClasses priorities;
	
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
}
