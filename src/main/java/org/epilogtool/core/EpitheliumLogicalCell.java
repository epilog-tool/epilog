package org.epilogtool.core;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.perturbation.AbstractPerturbation;

public class EpitheliumLogicalCell {

	private LogicalModel model;
	private byte[] state;
	private AbstractPerturbation perturbation;
	private StateClassification modelState;

	public EpitheliumLogicalCell(LogicalModel m) {
		this.setModel(m);
		this.modelState = StateClassification.DEFAULT;
	}
	
	public void setModel(LogicalModel m) {
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
		state[index] = value;
	}
	
	public void setModelState(StateClassification modelState) {
		this.modelState = modelState;
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
	
	public StateClassification getModelState() {
		return this.modelState;
	}
	
	public boolean isEmptyModel() {
		return EmptyModel.getInstance().isEmptyModel(this.getModel());
	}
	
	public String hashState() {
		String hash = "";
		for (int i = 0; i < this.state.length; i++) {
			hash += this.state[i];
		}
		return hash;
	}

	public boolean equals(Object o) {
		EpitheliumLogicalCell ecOut = (EpitheliumLogicalCell) o;
		if (!this.model.equals(ecOut.model))
			return false;
		if (this.perturbation == null) {
			if (ecOut.perturbation != null)
				return false;
		} else {
			if (ecOut.perturbation == null
					|| !this.perturbation.equals(ecOut.perturbation))
				return false;
		}
		if (state.length != ecOut.state.length)
			return false;
		for (int i = 0; i < state.length; i++) {
			if (state[i] != ecOut.state[i])
				return false;
		}
		return true;
	}
	
	public EpitheliumLogicalCell clone() {
		EpitheliumLogicalCell newCell = new EpitheliumLogicalCell(this.model);
		newCell.setState(this.state.clone());
		newCell.setPerturbation(this.perturbation);
		return newCell;
	}

}
