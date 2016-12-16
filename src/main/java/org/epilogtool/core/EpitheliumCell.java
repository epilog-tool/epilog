package org.epilogtool.core;

import java.util.ArrayList;
import java.util.List;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.perturbation.AbstractPerturbation;
import org.epilogtool.core.cellDynamics.CellularEvent;

public class EpitheliumCell {

	private LogicalModel model;
	private byte[] state;
	private AbstractPerturbation perturbation;
	private CellularEvent cellEvent;
	private EpitheliumCellIdentifier id;

	public EpitheliumCell(LogicalModel m) {
		this.id = new EpitheliumCellIdentifier();
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
		this.cellEvent = CellularEvent.DEFAULT;
		this.id.clear();
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
	
	public void setCellEvent(CellularEvent event) {
		this.cellEvent = event;
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
	
	public CellularEvent getCellEvent() {
		return this.cellEvent;
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
	
	public boolean isSame(Object o) {
		EpitheliumCell ecOut = (EpitheliumCell) o;
		return this.id.equals(ecOut.id);
	}
	
	public EpitheliumCellIdentifier getID() {
		return this.id;
	}
	
	public EpitheliumCell daughterCell() {
		EpitheliumCell daughterCell = new EpitheliumCell(this.model);
		daughterCell.setPerturbation(this.perturbation);
		daughterCell.setCellEvent(CellularEvent.DEFAULT);
		daughterCell.id = this.id.clone();
		return daughterCell;
	}
	
	public List<EpitheliumCell> daughterCells() {
		EpitheliumCell daughterCell1 = this.daughterCell();
		daughterCell1.id.addDepth(true);
		EpitheliumCell daughterCell2 = this.daughterCell();
		daughterCell2.id.addDepth(false);
		List<EpitheliumCell> daughterCellSet = new ArrayList<EpitheliumCell>();
		daughterCellSet.add(daughterCell1);
		daughterCellSet.add(daughterCell2);
		return daughterCellSet;
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
		newCell.setCellEvent(this.cellEvent);
		newCell.id = this.id.clone();
		return newCell;
	}

}
