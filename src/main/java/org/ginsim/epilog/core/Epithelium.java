package org.ginsim.epilog.core;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;
import org.colomoto.logicalmodel.perturbation.AbstractPerturbation;
import org.ginsim.epilog.common.Tuple2D;
import org.ginsim.epilog.core.topology.RollOver;

public class Epithelium {
	
	private int x;
	private int y;
	private String topologyLayout;
	
	private String name;
	private EpitheliumGrid grid;
	private EpitheliumComponentFeatures componentFeatures;
	private EpitheliumPriorityClasses priorities;
	private EpitheliumIntegrationFunctions integrationFunctions;
	private EpitheliumPerturbations perturbations;

	public Epithelium(int x, int y, String topologyLayout,String name,
			LogicalModel m, RollOver rollover) throws InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException,
			SecurityException, ClassNotFoundException {
		this.x = x;
		this.y = y;
		this.topologyLayout = topologyLayout;
		this.name = name;
		this.grid = new EpitheliumGrid(x, y, topologyLayout, rollover, m);
		this.priorities = new EpitheliumPriorityClasses();
		this.priorities.addModel(m);
		this.integrationFunctions = new EpitheliumIntegrationFunctions();
		this.perturbations = new EpitheliumPerturbations();
		this.perturbations.addModel(m);
		this.componentFeatures = new EpitheliumComponentFeatures();
		this.componentFeatures.addModel(m);
	}

	private Epithelium(int x, int y, String topologyLayout, String name, EpitheliumGrid grid,
			EpitheliumIntegrationFunctions eif, EpitheliumPriorityClasses epc,
			EpitheliumPerturbations eap, EpitheliumComponentFeatures ecf) {
		this.x = x;
		this.y = y;
		this.topologyLayout = topologyLayout;
		this.name = name;
		this.grid = grid;
		this.priorities = epc;
		this.integrationFunctions = eif;
		this.componentFeatures = ecf;
		this.perturbations = eap;
	}

	public boolean hasModel(LogicalModel m) {
		return this.grid.hasModel(m);
	}

	public Epithelium clone() {
		return new Epithelium(this.x, this.y, this.topologyLayout, "CopyOf_" + this.name, this.grid.clone(),
				this.integrationFunctions.clone(), this.priorities.clone(),
				this.perturbations.clone(), this.componentFeatures.clone());
	}

	public void update() {
		this.grid.updateModelSet();
		Set<LogicalModel> modelSet = this.grid.getModelSet();

		// Add to Epithelium state new models from modelSet
		for (LogicalModel mSet : modelSet) {
			// Priority classes
			if (this.priorities.getModelPriorityClasses(mSet) == null) {
				this.priorities.addModel(mSet);
			}
			// Component features
			this.componentFeatures.addModel(mSet);
			// Perturbations
			if (!this.perturbations.hasModel(mSet))
				this.perturbations.addModel(mSet);
		}

		// Remove from Epithelium state absent models from modelSet
		for (LogicalModel mPriorities : new ArrayList<LogicalModel>(
				this.priorities.getModelSet())) {
			if (!modelSet.contains(mPriorities)) {
				this.priorities.removeModel(mPriorities);
			}
		}
		for (LogicalModel mPerturbation : new ArrayList<LogicalModel>(
				this.perturbations.getModelSet())) {
			if (!modelSet.contains(mPerturbation)) {
				this.perturbations.removeModel(mPerturbation);
			}
		}

		// Create list with all existing Components
		Set<String> sNodeIDs = new HashSet<String>();
		for (LogicalModel m : modelSet) {
			for (NodeInfo node : m.getNodeOrder()) {
				sNodeIDs.add(node.getNodeID());
			}
		}
		// Clean Epithelium components
		List<String> lOldNodes = new ArrayList<String>(
				this.componentFeatures.getComponents());
		for (String oldNodeID : lOldNodes) {
			if (!sNodeIDs.contains(oldNodeID)) {
				System.out.println("OldNode: " + oldNodeID);
				this.componentFeatures.removeComponent(oldNodeID);
				if (this.isIntegrationComponent(oldNodeID)) {
					this.integrationFunctions.removeComponent(oldNodeID);
				}
			}
		}
	}

	public EpitheliumComponentFeatures getComponentFeatures() {
		return this.componentFeatures;
	}

	public String toString() {
		return this.getName();
//		return this.name + " ("
//				+ this.grid.getTopology().getRollOver().toString() + ")";
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

	public void setGridWithModel(LogicalModel m, List<Tuple2D<Integer>> lTuples) {
		for (Tuple2D<Integer> tuple : lTuples) {
			this.grid.setModel(tuple.getX(), tuple.getY(), m);
		}
	}

	public void setGridWithComponentValue(String nodeID, byte value,
			List<Tuple2D<Integer>> lTuples) {
		for (Tuple2D<Integer> tuple : lTuples) {
			this.grid.setCellComponentValue(tuple.getX(), tuple.getY(),
					nodeID, value);
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

	public void setPriorityClasses(ModelPriorityClasses mpc) {
		this.priorities.addModelPriorityClasses(mpc);
	}

	public void addPerturbation(LogicalModel m, AbstractPerturbation ap) {
		this.perturbations.addPerturbation(m, ap);
	}

	public void delPerturbation(LogicalModel m, AbstractPerturbation ap) {
		this.perturbations.delPerturbation(m, ap);
	}

	public void applyPerturbation(LogicalModel m, AbstractPerturbation ap,
			Color c, List<Tuple2D<Integer>> lTuples) {
		this.perturbations.addPerturbationColor(m, ap, c);
		if (lTuples != null) {
			this.grid.setPerturbation(m, lTuples, ap);
		}
	}

	public EpitheliumGrid getEpitheliumGrid() {
		return this.grid;
	}

	public ModelPriorityClasses getPriorityClasses(LogicalModel m) {
		return this.priorities.getModelPriorityClasses(m);
	}

	public ComponentIntegrationFunctions getIntegrationFunctionsForComponent(
			String nodeID) {
		return this.integrationFunctions
				.getComponentIntegrationFunctions(nodeID);
	}

	public Set<String> getIntegrationFunctionsComponents() {
		return this.integrationFunctions.getComponents();
	}

	public boolean isIntegrationComponent(String nodeID) {
		return this.integrationFunctions.containsKey(nodeID);
	}

	public EpitheliumIntegrationFunctions getIntegrationFunctions() {
		return this.integrationFunctions;
	}

	public ModelPerturbations getModelPerturbations(LogicalModel m) {
		return this.perturbations.getModelPerturbations(m);
	}

	public EpitheliumPerturbations getEpitheliumPerturbations() {
		return this.perturbations;
	}

	public void setModel(int x, int y, LogicalModel m) {
		this.grid.setModel(x, y, m);
	}
	
	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}
	
	public String getTopologyLayout() {
		// TODO: improve this
		return this.topologyLayout;
	}

	public boolean equals(Object o) {
		Epithelium otherEpi = (Epithelium) o;
		return (this.grid.equals(otherEpi.grid)
				&& this.componentFeatures.equals(otherEpi.componentFeatures)
				&& this.priorities.equals(otherEpi.priorities)
				&& this.integrationFunctions
						.equals(otherEpi.integrationFunctions) && this.perturbations
					.equals(otherEpi.perturbations));
	}
}
