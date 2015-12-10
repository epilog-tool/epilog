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

	public EpitheliumCell clone() {
		EpitheliumCell newCell = new EpitheliumCell(this.model);
		newCell.setState(this.state.clone());
		newCell.setPerturbation(this.perturbation);
		return newCell;
	}

	public AbstractPerturbation getPerturbation() {
		return this.perturbation;
	}

	public long hashState() {
		long hash = 1;
		for (int i = 0; i < model.getNodeOrder().size(); i++) {
			int vals = model.getNodeOrder().get(i).getMax() + 1;
			hash += i * Math.pow(vals, this.state[i]);
		}
		return hash;
	}

	public void setPerturbation(AbstractPerturbation ap) {
		this.perturbation = ap;
	}

	public byte[] getState() {
		return this.state;
	}

	public void setState(byte[] state) {
		this.state = state;
	}

	public void setValue(String nodeID, byte value) {
		int index = this.getNodeIndex(nodeID);
		if (index < 0)
			return;
		state[index] = value;
	}

	public LogicalModel getModel() {
		return this.model;
	}

	public void setModel(LogicalModel m) {
		this.model = m;
		this.state = new byte[m.getNodeOrder().size()];
		for (int i = 0; i < this.state.length; i++) {
			this.state[i] = 0;
		}
		this.perturbation = null;
	}

	public int getNodeIndex(String nodeID) {
		for (int i = 0; i < this.model.getNodeOrder().size(); i++) {
			if (this.model.getNodeOrder().get(i).getNodeID().equals(nodeID))
				return i;
		}
		return -1;
	}

	public boolean equals(Object o) {
		EpitheliumCell ecOut = (EpitheliumCell) o;
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
}
