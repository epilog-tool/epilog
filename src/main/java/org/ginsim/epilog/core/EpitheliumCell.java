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

	public byte getComponentValue(String component) {
		return state[this.model.getNodeOrder().indexOf(id2Node.get(component))];
	}

	public byte getComponentMax(String component) {
		return id2Node.get(component).getMax();
	}

	public EpitheliumCell clone() {
		EpitheliumCell newCell = new EpitheliumCell(this.model);
		for (int i = 0; i < this.state.length; i++) {
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

	private void setValue(int index, byte v) {
		this.state[index] = v;
	}

	public LogicalModel getModel() {
		return this.model;
	}

	public void setModel(LogicalModel m) {
		this.model = m;
	}
}
