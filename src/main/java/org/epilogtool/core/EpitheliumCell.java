package org.epilogtool.core;

import java.util.List;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;
import org.colomoto.logicalmodel.perturbation.AbstractPerturbation;
import org.colomoto.logicalmodel.perturbation.FixedValuePerturbation;
import org.colomoto.logicalmodel.perturbation.InteractionPerturbation;
import org.colomoto.logicalmodel.perturbation.LogicalModelPerturbation;
import org.colomoto.logicalmodel.perturbation.MultiplePerturbation;
import org.colomoto.logicalmodel.perturbation.RangePerturbation;

public class EpitheliumCell {
	private LogicalModel model;
	private byte[] state;
	private AbstractPerturbation perturbation;

	public EpitheliumCell(LogicalModel m) {
		this.setModel(m);
	}

	public void restrictValueWithPerturbation() {
		if (this.perturbation != null) {
			this.restrictValueWith(this.perturbation);
		}
	}

	private void restrictValueWith(FixedValuePerturbation ap) {
		this.state[this.model.getNodeOrder().indexOf(ap.component)] = (byte) ap.value;
	}

	private void restrictValueWith(RangePerturbation ap) {
		int nodeIndex = this.model.getNodeOrder().indexOf(ap.component);
		if (this.state[nodeIndex] < ap.min) {
			this.state[nodeIndex] = (byte) ap.min;
		} else if (this.state[nodeIndex] > ap.max) {
			this.state[nodeIndex] = (byte) ap.max;
		} // else keep value
	}

	private void restrictValueWith(InteractionPerturbation ap) {
		// do nothing
	}

	private void restrictValueWith(MultiplePerturbation<?> mAp) {
		for (LogicalModelPerturbation ap : mAp.perturbations) {
			this.restrictValueWith(ap);
		}
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

	public AbstractPerturbation getPerturbation() {
		return this.perturbation;
	}

	public byte[] getState() {
		return this.state;
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

	public EpitheliumCell clone() {
		EpitheliumCell newCell = new EpitheliumCell(this.model);
		newCell.setState(this.state.clone());
		newCell.setPerturbation(this.perturbation);
		return newCell;
	}
}
