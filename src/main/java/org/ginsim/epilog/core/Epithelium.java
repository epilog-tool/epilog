package org.ginsim.epilog.core;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;
import org.colomoto.logicalmodel.perturbation.AbstractPerturbation;
import org.ginsim.epilog.Tuple2D;
import org.ginsim.epilog.core.topology.RollOver;

public class Epithelium {
	private String name;
	private EpitheliumGrid grid;
	private EpitheliumComponentFeatures componentFeatures;
	private EpitheliumPriorityClasses priorities;
	private EpitheliumIntegrationFunctions integrationFunctions;
	private EpitheliumPerturbations perturbations;

	public Epithelium(int x, int y, String topologyLayout, RollOver rollover, LogicalModel m, String name) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
		this.name = name;
		this.grid = new EpitheliumGrid(x, y, topologyLayout, rollover, m);
		this.priorities = new EpitheliumPriorityClasses();
		this.integrationFunctions = new EpitheliumIntegrationFunctions();
		this.perturbations = new EpitheliumPerturbations();
		this.componentFeatures = new EpitheliumComponentFeatures();
		this.componentFeatures.addModel(m);
	}

	private Epithelium(String name, EpitheliumGrid grid,
			EpitheliumIntegrationFunctions eif, EpitheliumPriorityClasses epc,
			EpitheliumPerturbations eap) {
		this.name = name;
		this.grid = grid;
		this.priorities = epc;
		this.integrationFunctions = eif;
		this.perturbations = eap;
	}
	
	public boolean hasModel(LogicalModel m) {
		return this.grid.hasModel(m);
	}

	public Epithelium clone() {
		return new Epithelium("CopyOf_" + this.name, this.grid.clone(),
				this.integrationFunctions.clone(), this.priorities.clone(),
				this.perturbations.clone());
	}

	public EpitheliumComponentFeatures getComponentFeatures() {
		return this.componentFeatures;
	}

	public String toString() {
		return this.name + " - " + this.grid.getTopology().getRollOver().toString();
	}
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LogicalModel getModel(int x, int y) {
		return this.grid.getModel(x, y);
	}

	public void setGridWithModel(LogicalModel m, List<Tuple2D> lTuples) {
		for (Tuple2D tuple : lTuples) {
			this.grid.setModel(tuple.getX(), tuple.getY(), m);
		}
	}

	public void setGridWithComponentValue(String nodeID, byte value,
			List<Tuple2D> lTuples) {
		for (Tuple2D tuple : lTuples) {
			this.grid.setCellComponentValue(tuple.getX(), tuple.getY(), nodeID,
					value);
		}
	}

	public void setComponentColor(String nodeID, Color color) {
		this.componentFeatures.setNodeColor(nodeID, color);
	}

	public void setIntegrationFunction(String nodeID, byte value,
			String function) {
		NodeInfo node = this.componentFeatures.getNodeInfo(nodeID);
		if (!this.integrationFunctions.containsKey(nodeID)) {
			this.integrationFunctions.addComponent(node);
		}
		this.integrationFunctions.setFunctionAtLevel(node, value, function);
	}

	public void initPriorityClasses(LogicalModel m) {
		ModelPriorityClasses mpc = new ModelPriorityClasses(m);
		this.priorities.addModelPriorityClasses(mpc);
	}

	public void initComponentFeatures(LogicalModel m) {
		this.componentFeatures.addModel(m);
	}

	public void setPriorityClasses(LogicalModel m, String pcs) {
		ModelPriorityClasses mpc = new ModelPriorityClasses(m);
		mpc.setPriorities(pcs);
		this.priorities.addModelPriorityClasses(mpc);
	}

	public void addPerturbation(LogicalModel m, AbstractPerturbation ap) {
		this.perturbations.addPerturbation(m, ap);
	}

	public void applyPerturbation(LogicalModel m, AbstractPerturbation ap,
			Color c, List<Tuple2D> lTuples) {
		this.perturbations.addPerturbationColor(m, ap, c);
		this.grid.setPerturbation(m, lTuples, ap);
	}

	public EpitheliumGrid getEpitheliumGrid() {
		return this.grid;
	}

	public ModelPriorityClasses getPriorityClasses(LogicalModel m) {
		return this.priorities.getModelPriorityClasses(m);
	}

	public ComponentIntegrationFunctions getIntegrationFunctionsForComponent(
			String c) {
		return this.integrationFunctions.getComponentIntegrationFunctions(c);
	}

	public Set<String> getIntegrationFunctionsComponents() {
		return this.integrationFunctions.getComponents();
	}

	public boolean isIntegrationComponent(String nodeID) {
		return this.integrationFunctions.containsKey(nodeID);
	}

	public ModelPerturbations getPerturbations(LogicalModel m) {
		return this.perturbations.getModelPerturbations(m);
	}
	
	public Color getCellColor(int x, int y, List<String> compON) {
		byte[] cellState = this.grid.getCellState(x, y);
		LogicalModel m = this.grid.getModel(x, y);
		return this.componentFeatures.getCellColor(m, cellState, compON);
	}

}
