package org.ginsim.epilog.core;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;
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
		for (int i = 0; i < this.model.getNodeOrder().size(); i++) {
			if (this.model.getNodeOrder().get(i).getNodeID().equals(nodeID)) {
				state[i] = value;
				break;
			}
		}
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
		for (int i = 0; i < this.model.getNodeOrder().size(); i ++) {
			if (this.model.getNodeOrder().get(i).getNodeID().equals(nodeID))
				return i;
		}
		return -1;
	}
}
