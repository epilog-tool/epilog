package org.epilogtool.core;

import java.util.HashMap;
import java.util.Map;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.perturbation.AbstractPerturbation;

public class EpitheliumCell {
	private Map<String, Byte> inputComponents;
	private EpitheliumLogicalCell logicalCell;

	public EpitheliumCell(LogicalModel m) {
		this.inputComponents = new HashMap<String, Byte>();
		this.logicalCell = new EpitheliumLogicalCell(m);
	}
	
	public EpitheliumCell() {
		this.inputComponents = new HashMap<String, Byte>();
		this.logicalCell = new EpitheliumLogicalCell(EmptyModel.getInstance().getModel());
	}
	
	private EpitheliumCell(EpitheliumLogicalCell logicalCell, Map<String, Byte> inputComponents){
		this.logicalCell = logicalCell;
		this.inputComponents = inputComponents;
	}
	
	private EpitheliumLogicalCell getLogicalCell() {
		return this.logicalCell;
	}
	
	public void setModel(LogicalModel m) {
		this.logicalCell.setModel(m);
	}
	
	public void setState(byte[] state) {
		this.logicalCell.setState(state);
	}
	
	public void setPerturbation(AbstractPerturbation ap) {
		this.logicalCell.setPerturbation(ap);
	}
	
	public void setValue(String nodeID, byte value) {
		this.logicalCell.setValue(nodeID, value);
	}

	public AbstractPerturbation getPerturbation() {
		return this.logicalCell.getPerturbation();
	}

	public byte[] getState() {
		return this.logicalCell.getState();
	}

	public LogicalModel getModel() {
		return this.logicalCell.getModel();
	}

	public int getNodeIndex(String nodeID) {
		return this.logicalCell.getNodeIndex(nodeID);
	}
	
	public byte getNodeValue(String nodeID) {
		return this.logicalCell.getNodeValue(nodeID);
	}
	
	public boolean isEmptyCell() {
		return this.logicalCell.isEmptyModel();
	}
	
	public String hashState() {
		return this.logicalCell.hashState();
	}

	public boolean equals(Object o) {
		EpitheliumCell eOut = (EpitheliumCell) o;
		return this.logicalCell.equals(eOut.getLogicalCell());
	}
	
	public EpitheliumCell clone() {
		EpitheliumLogicalCell logicalCell = this.logicalCell.clone();
		Map<String, Byte> inputComponents = new HashMap<String, Byte>(this.inputComponents);
		return new EpitheliumCell(logicalCell, inputComponents);
	}
}
