package org.epilogtool.core;

import org.colomoto.biolqm.LogicalModel;
import org.colomoto.biolqm.modifier.perturbation.LogicalModelPerturbation;

public class EpitheliumCell {

	private LogicalModel model;
	private byte[] state;
	private LogicalModelPerturbation perturbation;

	public EpitheliumCell(LogicalModel m) {
		this.setModel(m);
	}
	
	public boolean isEmptyCell() {
		return EmptyModel.getInstance().isEmptyModel(this.model);
	}
	
	public void restrictValuesWithPerturbation() {
		if (perturbation != null) {
			this.perturbation.restrictValues(state, model.getComponents());
		}
	}
	
	public void setModel(LogicalModel m) {
		if (this.model != null && this.model.equals(m)) {
			return;
		}
		this.model = m;
		this.state = new byte[m.getComponents().size()];
		for (int i = 0; i < this.state.length; i++) {
			this.state[i] = 0;
		}
		this.perturbation = null;
	}

	public void setState(byte[] state) {
		this.state = state;
	}
	
	public void setPerturbation(LogicalModelPerturbation ap) {
		this.perturbation = ap;
	}

	public void setValue(String nodeID, byte value) {
		int index = this.getNodeIndex(nodeID);
		if (index < 0)
			return;
		value = (byte) Math.min(value, this.model.getComponents().get(index).getMax());
		state[index] = value;
	}
	
	public byte getValue(String nodeID) {
		int index = this.getNodeIndex(nodeID);
		if (index < 0)
			return 0; // Should not happen!!
		return state[index];
	}

	public LogicalModelPerturbation getPerturbation() {
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
		for (int i = 0; i < this.model.getComponents().size(); i++) {
			if (this.model.getComponents().get(i).getNodeID().equals(nodeID))
				return i;
		}
		return -1;
	}

	public boolean hasEmptyModel() {
		return EmptyModel.getInstance().isEmptyModel(this.getModel());
	}
	
	public long hashState() {
		long hash = 1;
		for (int i = 0; i < model.getComponents().size(); i++) {
			int vals = model.getComponents().get(i).getMax() + 1;
			hash += i * Math.pow(vals, this.state[i]);
		}
		return hash;
	}
	
	public boolean equals(Object o) {
		EpitheliumCell ecOut = (EpitheliumCell) o;
		if (!this.model.equals(ecOut.model)) {
			return false;
		}
		if (this.perturbation == null) {
			if (ecOut.perturbation != null) {
				return false;
			}
		} else {
			if (ecOut.perturbation == null
					|| !this.perturbation.equals(ecOut.perturbation)) {
				return false;
			}
		}
		if (state.length != ecOut.state.length) {
			return false;
		}
		for (int i = 0; i < state.length; i++) {
			if (state[i] != ecOut.state[i]) {
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
