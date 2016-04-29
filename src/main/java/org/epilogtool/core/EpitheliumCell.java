package org.epilogtool.core;

import java.util.HashMap;
import java.util.Map;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.perturbation.AbstractPerturbation;
import org.epilogtool.core.cellDynamics.CellTrigger;
import org.epilogtool.project.ComponentPair;

public class EpitheliumCell {
	private Map<ComponentPair, Byte> environmentalInputs;
	private EpitheliumLogicalCell logicalCell;

	public EpitheliumCell(LogicalModel m) {
		this.environmentalInputs = new HashMap<ComponentPair, Byte>();
		this.logicalCell = new EpitheliumLogicalCell(m);
		this.updateEnvironmentalInputs();
		
	}
	
	public EpitheliumCell() {
		this.environmentalInputs = new HashMap<ComponentPair, Byte>();
		this.logicalCell = new EpitheliumLogicalCell(EmptyModel.getInstance().getModel());
	}
	
	private EpitheliumCell(EpitheliumLogicalCell logicalCell, Map<ComponentPair, Byte> inputComponents){
		this.logicalCell = logicalCell;
		this.environmentalInputs = inputComponents;
		this.updateEnvironmentalInputs();
	}
	
	public void setLogicalCell(EpitheliumLogicalCell logicalCell) {
		this.logicalCell = logicalCell;
		this.updateEnvironmentalInputs();
	}
	
	public EpitheliumLogicalCell getLogicalCell() {
		return this.logicalCell;
	}
	
	public void setModel(LogicalModel m) {
		this.logicalCell.setModel(m);
		this.updateEnvironmentalInputs();
	}
	
	private void updateEnvironmentalInputs() {
		for (ComponentPair cp : this.environmentalInputs.keySet()) {
			if (this.getModel().equals(cp.getModel())) {
				this.setValue(cp.getNodeInfo().getNodeID(), this.environmentalInputs.get(cp));
			}
		}
	}
	
	public void setState(byte[] state) {
		this.logicalCell.setState(state);
	}
	
	public void setInitialState(byte[] state) {
		this.logicalCell.setInitialState(state);
	}
	
	public void setPerturbation(AbstractPerturbation ap) {
		this.logicalCell.setPerturbation(ap);
	}
	
	public void setValue(String nodeID, byte value) {
		this.logicalCell.setValue(nodeID, value);
	}
	
	public void setCellTrigger(CellTrigger trigger) {
		this.logicalCell.setCellTrigger(trigger);
	}
	
	public void addEnvironmentalInput(ComponentPair cp, byte value) {
		this.environmentalInputs.put(cp, value);
		if (this.getModel().equals(cp.getModel())) {
			this.setValue(cp.getNodeInfo().getNodeID(), value);
		}
	}
	
	public void removeEnvironmentalInput(ComponentPair cp) {
		this.environmentalInputs.remove(cp);
		if (this.getModel().equals(cp.getModel())) {
			this.setValue(cp.getNodeInfo().getNodeID(), (byte) 0);
		}
	}

	public AbstractPerturbation getPerturbation() {
		return this.logicalCell.getPerturbation();
	}

	public byte[] getState() {
		return this.logicalCell.getState();
	}
	
	public byte[] getInitialState() {
		return this.logicalCell.getInitialState();
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
	
	public CellTrigger getCellTrigger() {
		return this.logicalCell.getCellTrigger();
	}
	
	public Map<ComponentPair, Byte> getCellEnvironment() {
		return this.environmentalInputs;
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
		Map<ComponentPair, Byte> inputComponents = new HashMap<ComponentPair, Byte>(this.environmentalInputs);
		return new EpitheliumCell(logicalCell, inputComponents);
	}
}
