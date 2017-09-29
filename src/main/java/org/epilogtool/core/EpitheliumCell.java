package org.epilogtool.core;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.perturbation.AbstractPerturbation;

public class EpitheliumCell {

	private LogicalModel model;
	private byte[] state;
	private AbstractPerturbation perturbation;

	public EpitheliumCell(LogicalModel m) {
		this.setModel(m);
	}
	
	public boolean isEmptyCell() {
		return EmptyModel.getInstance().isEmptyModel(this.model);
	}
	
	public void restrictValuesWithPerturbation() {
		if (perturbation != null) {
			this.perturbation.restrictValues(state, model.getNodeOrder());
		}
	}
	
	public void setModel(LogicalModel m) {
		if (this.model != null && this.model.equals(m)) {
			return;
		}
		this.model = m;
		this.state = new byte[m.getNodeOrder().size()];
		for (int i = 0; i < this.state.length; i++) {
			this.state[i] = 0;
		}
		this.perturbation = null;
	}

	public void setState(byte[] state) {
		this.state = state;
	}
	
	public void setPerturbation(AbstractPerturbation ap) {
		this.perturbation = ap;
	}

	public void setValue(String nodeID, byte value) {
		int index = this.getNodeIndex(nodeID);
		if (index < 0)
			return;
		value = (byte) Math.min(value, this.model.getNodeOrder().get(index).getMax());
		state[index] = value;
	}
	
	public byte getValue(String nodeID) {
		int index = this.getNodeIndex(nodeID);
		if (index < 0)
			return 0; // Should not happen!!
		return state[index];
	}

	public AbstractPerturbation getPerturbation() {
		return this.perturbation;
	}

	public byte[] getState() {
		return this.state;
	}
	
	public byte getNodeValue(String nodeID){
		return this.getState()[this.getNodeIndex(nodeID)];
	}

	public LogicalModel getModel() {
		return this.model;
	}

	public int getNodeIndex(String nodeID) {
		for (int i = 0; i < this.model.getNodeOrder().size(); i++) {
			if (this.model.getNodeOrder().get(i).getNodeID().equals(nodeID))
				return i;
		}
		return -1;
	}

	public boolean hasEmptyModel() {
		return EmptyModel.getInstance().isEmptyModel(this.getModel());
	}
	
	public long hashState() {
		long hash = 1;
		for (int i = 0; i < model.getNodeOrder().size(); i++) {
			int vals = model.getNodeOrder().get(i).getMax() + 1;
			hash += i * Math.pow(vals, this.state[i]);
		}
		return hash;
	}
	
	public boolean equals(Object o) {
		EpitheliumCell ecOut = (EpitheliumCell) o;
		if (!this.model.equals(ecOut.model)) {
			System.out.println("  EpiCell.equals: =/= model");
			return false;
		}
		if (this.perturbation == null) {
			if (ecOut.perturbation != null) {
				System.out.println("  EpiCell.equals: =/= perturb 1");
				return false;
			}
		} else {
			if (ecOut.perturbation == null
					|| !this.perturbation.equals(ecOut.perturbation)) {
				System.out.println("  EpiCell.equals: =/= perturb 2");
				return false;
			}
		}
		if (state.length != ecOut.state.length) {
			System.out.println("  EpiCell.equals: =/= state len");
			return false;
		}
		for (int i = 0; i < state.length; i++) {
			if (state[i] != ecOut.state[i]) {
				System.out.println("  EpiCell.equals: state " + this.state2str(state) + " =/= " + this.state2str(ecOut.state));
				return false;
			}
		}
		return true;
	}
	public String state2str(byte[] state) {
		String str = "";
		for (int i = 0; i < state.length; i++)
			str += state[i];
		return str;
	}

	public EpitheliumCell clone() {
		EpitheliumCell newCell = new EpitheliumCell(this.model);
		newCell.setState(this.state.clone());
		newCell.setPerturbation(this.perturbation);
		return newCell;
	}

}
