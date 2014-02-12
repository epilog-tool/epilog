package org.ginsim.epilog.core;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.perturbation.AbstractPerturbation;

public class EpitheliumCell {
	private LogicalModel model;
	private byte[] state;
	private AbstractPerturbation perturbation;

	public EpitheliumCell(LogicalModel m) {
		this.model = m;
		this.state = new byte[m.getNodeOrder().size()];
		for (int i = 0; i < this.state.length; i++) {
			this.state[i] = 0;
		}
		this.perturbation = null;
	}
	
	public EpitheliumCell clone() {
		EpitheliumCell newCell = new EpitheliumCell(this.model);
		for (int i = 0; i < this.state.length; i++ ) {
			newCell.setValue(i, this.state[i]);
		}
		newCell.setPerturbation(this.perturbation);
		return newCell;
	}

	public AbstractPerturbation getPerturbation() {
		return this.perturbation;
	}

	public void setPerturbation(AbstractPerturbation p) {
		this.perturbation = p;
	}

	public byte[] getState() {
		return this.state;
	}

	public void setValue(int index, byte v) {
		this.state[index] = v;
	}

	public LogicalModel getModel() {
		return this.model;
	}

	public void setModel(LogicalModel m) {
		this.model = m;
	}
}
