package org.ginsim.epilog.core;

import java.util.HashMap;
import java.util.Map;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;
import org.colomoto.logicalmodel.perturbation.AbstractPerturbation;

public class EpitheliumCell {
	private LogicalModel model;
	private byte[] state;
	private AbstractPerturbation perturbation;
	private Map<String, NodeInfo> id2Node;

	public EpitheliumCell(LogicalModel m) {
		this.model = m;
		this.state = new byte[m.getNodeOrder().size()];
		for (int i = 0; i < this.state.length; i++) {
			this.state[i] = 0;
		}
		this.perturbation = null;
		id2Node = new HashMap<String, NodeInfo>();
		for (NodeInfo node : m.getNodeOrder()) {
			id2Node.put(node.getNodeID(), node);
		}
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

	public void setPerturbation(AbstractPerturbation p) {
		this.perturbation = p;
	}

	public byte[] getState() {
		return this.state;
	}

	public void setState(byte[] state) {
		this.state = state;
	}

	public LogicalModel getModel() {
		return this.model;
	}

	public void setModel(LogicalModel m) {
		this.model = m;
	}
}
