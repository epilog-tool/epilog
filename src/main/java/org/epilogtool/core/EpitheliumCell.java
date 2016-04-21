package org.epilogtool.core;

import java.util.HashMap;
import java.util.Map;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.perturbation.AbstractPerturbation;
import org.epilogtool.core.cellDynamics.CellStatus;
import org.epilogtool.project.ComponentPair;

public class EpitheliumCell {
	private Map<ComponentPair, Byte> inputComponents;
	private EpitheliumLogicalCell logicalCell;

	public EpitheliumCell(LogicalModel m) {
		this.inputComponents = new HashMap<ComponentPair, Byte>();
		this.logicalCell = new EpitheliumLogicalCell(m);
		this.updateEnvironmentalInputs();
		
	}
	
	public EpitheliumCell() {
		this.inputComponents = new HashMap<ComponentPair, Byte>();
		this.logicalCell = new EpitheliumLogicalCell(EmptyModel.getInstance().getModel());
	}
	
	private EpitheliumCell(EpitheliumLogicalCell logicalCell, Map<ComponentPair, Byte> inputComponents){
		this.logicalCell = logicalCell;
		this.inputComponents = inputComponents;
	}
	
	private EpitheliumLogicalCell getLogicalCell() {
		return this.logicalCell;
	}
	
	public void setModel(LogicalModel m) {
		this.logicalCell.setModel(m);
		this.updateEnvironmentalInputs();
	}
	
	private void updateEnvironmentalInputs() {
		for (ComponentPair cp : this.inputComponents.keySet()) {
			if (this.logicalCell.getModel().equals(cp.getModel())) {
				this.setValue(cp.getNodeInfo().getNodeID(), this.inputComponents.get(cp));
			}
		}
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
	
	public void setCellStatus(CellStatus status) {
		this.logicalCell.setCellStatus(status);
	}
	
	public void addInputComponent(ComponentPair cp, byte value) {
		this.inputComponents.put(cp, value);
		this.setValue(cp.getNodeInfo().getNodeID(), value);
	}
	
	public void removeInputComponent(ComponentPair cp) {
		this.inputComponents.remove(cp);
		this.setValue(cp.getNodeInfo().getNodeID(), (byte) 0);
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
	
	public CellStatus getCellStatus() {
		return this.logicalCell.getCellStatus();
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
		Map<ComponentPair, Byte> inputComponents = new HashMap<ComponentPair, Byte>(this.inputComponents);
		return new EpitheliumCell(logicalCell, inputComponents);
	}
}
