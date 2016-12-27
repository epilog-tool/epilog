package org.epilogtool.core;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.antlr.runtime.RecognitionException;
import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;
import org.colomoto.logicalmodel.perturbation.AbstractPerturbation;
import org.epilogtool.common.Tuple2D;
import org.epilogtool.core.cellDynamics.ModelEventManager;
import org.epilogtool.core.cellDynamics.ModelHeritableNodes;
import org.epilogtool.core.topology.RollOver;
import org.epilogtool.project.ComponentPair;
import org.epilogtool.project.ProjectFeatures;

public class Epithelium {
	private String name;
	private EpitheliumGrid grid;
	private EpitheliumIntegrationFunctions integrationFunctions;
	private EpitheliumPerturbations perturbations;
	private EpitheliumUpdateSchemeIntra priorities;
	private EpitheliumUpdateSchemeInter updateSchemeInter;
	private ProjectFeatures projectFeatures;

	// TODO: requires refactoring - these are the static properties used for
	// cell division
	private ModelEventManager modelEventManager;
	private ModelHeritableNodes modelHeritableNodes;

	public Epithelium(int x, int y, String topologyID, String name, LogicalModel m, RollOver rollover,
			ProjectFeatures projectFeatures)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, ClassNotFoundException {
		this.name = name;
		this.grid = new EpitheliumGrid(x, y, topologyID, rollover, m);
		this.priorities = new EpitheliumUpdateSchemeIntra();
		this.priorities.addModel(m);
		this.integrationFunctions = new EpitheliumIntegrationFunctions();
		this.perturbations = new EpitheliumPerturbations();
		this.projectFeatures = projectFeatures;
		this.updateSchemeInter = new EpitheliumUpdateSchemeInter();
		this.modelEventManager = new ModelEventManager(this.grid.getModelSet());
		this.modelHeritableNodes = new ModelHeritableNodes();
		this.modelHeritableNodes.addModel(m);
	}

	private Epithelium(String name, EpitheliumGrid grid, EpitheliumIntegrationFunctions eif,
			EpitheliumUpdateSchemeIntra epc, EpitheliumPerturbations eap, ProjectFeatures pf,
			EpitheliumUpdateSchemeInter usi, ModelEventManager eventsManager, ModelHeritableNodes modelHeritableNodes) {
		this.name = name;
		this.grid = grid;
		this.priorities = epc;
		this.integrationFunctions = eif;
		this.projectFeatures = pf;
		this.perturbations = eap;
		this.updateSchemeInter = usi;
		this.modelEventManager = eventsManager;
		this.modelHeritableNodes = modelHeritableNodes;
		;
	}

	public Epithelium clone() {
		return new Epithelium("CopyOf_" + this.name, this.grid.clone(), this.integrationFunctions.clone(),
				this.priorities.clone(), this.perturbations.clone(), this.projectFeatures,
				this.updateSchemeInter.clone(), this.modelEventManager.clone(), this.modelHeritableNodes.clone());
	}

	public String toString() {
		return this.getName();
		// return this.name + " ("
		// + this.grid.getTopology().getRollOver().toString() + ")";
	}

	public boolean equals(Object o) {
		Epithelium otherEpi = (Epithelium) o;
		return (this.grid.equals(otherEpi.grid) && this.priorities.equals(otherEpi.priorities)
				&& this.integrationFunctions.equals(otherEpi.integrationFunctions)
				&& this.perturbations.equals(otherEpi.perturbations)
				&& this.updateSchemeInter.equals(otherEpi.getUpdateSchemeInter())
				&& this.modelHeritableNodes.equals(otherEpi.modelHeritableNodes))
				&& this.modelEventManager.equals(otherEpi.modelEventManager);
	}

	public void updateEpitheliumGrid(int gridX, int gridY, String topologyID, RollOver rollover)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, ClassNotFoundException {
		this.grid.updateEpitheliumGrid(gridX, gridY, topologyID, rollover);
	}

	public boolean hasModel(LogicalModel m) {
		return this.grid.hasModel(m);
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
			// Perturbations
			if (!this.perturbations.hasModel(mSet))
				this.perturbations.addModel(mSet);

			// Grid dynamics
			if (!this.modelEventManager.hasModel(mSet))
				this.modelEventManager.addModel(mSet);

			// Heritable components
			if (!this.modelHeritableNodes.hasModel(mSet)) {
				this.modelHeritableNodes.addModel(mSet);
			}
		}

		// Remove from Epithelium state absent models from modelSet
		for (LogicalModel mPriorities : new ArrayList<LogicalModel>(this.priorities.getModelSet())) {
			if (!modelSet.contains(mPriorities)) {
				this.priorities.removeModel(mPriorities);
			}
		}
		for (LogicalModel mPerturbation : new ArrayList<LogicalModel>(this.perturbations.getModelSet())) {
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
		Set<ComponentPair> sCP = new HashSet<ComponentPair>(this.integrationFunctions.getComponentPair());
		for (ComponentPair cp : sCP) {
			NodeInfo node = cp.getNodeInfo();
			if (!sNodeIDs.contains(node.getNodeID())) {
				this.integrationFunctions.removeComponent(cp);
			}
		}
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

	public EpitheliumGrid getEpitheliumGrid() {
		return this.grid;
	}

	public ProjectFeatures getProjectFeatures() {
		return this.projectFeatures;
	}

	public EpitheliumUpdateSchemeInter getUpdateSchemeInter() {
		return this.updateSchemeInter;
	}

	public ModelPriorityClasses getPriorityClasses(LogicalModel m) {
		return this.priorities.getModelPriorityClasses(m);
	}

	public ComponentIntegrationFunctions getIntegrationFunctionsForComponent(ComponentPair cp) {
		return this.integrationFunctions.getComponentIntegrationFunctions(cp);
	}

	public Set<ComponentPair> getIntegrationComponentPairs() {
		return this.integrationFunctions.getComponentPair();
	}

	public boolean isIntegrationComponent(NodeInfo node) {
		for (ComponentPair cp : this.integrationFunctions.getComponentPair()) {
			if (node.equals(cp.getNodeInfo()))
				return true;
		}
		return false;
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

	public ModelHeritableNodes getModelHeritableNodes() {
		return this.modelHeritableNodes;
	}

	public void setModelHeritableNode(ModelHeritableNodes mhn) {
		this.modelHeritableNodes = mhn;
	}

	public void setGridWithComponentValue(String nodeID, byte value, List<Tuple2D<Integer>> lTuples) {
		for (Tuple2D<Integer> tuple : lTuples) {
			this.grid.setCellComponentValue(tuple.getX(), tuple.getY(), nodeID, value);
		}
	}

	public void setIntegrationFunction(String nodeID, LogicalModel m, byte value, String function)
			throws RecognitionException, RuntimeException {
		NodeInfo node = this.projectFeatures.getNodeInfo(nodeID, m);
		ComponentPair cp = new ComponentPair(m, node);
		if (!this.integrationFunctions.containsComponentPair(cp)) {
			this.integrationFunctions.addComponent(cp);
		}
		this.integrationFunctions.setFunctionAtLevel(cp, value, function);
	}

	public void initPriorityClasses(LogicalModel m) {
		ModelPriorityClasses mpc = new ModelPriorityClasses(m);
		this.priorities.addModelPriorityClasses(mpc);
	}

	public void initComponentFeatures(LogicalModel m) {
		this.projectFeatures.addModelComponents(m);
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

	public void applyPerturbation(LogicalModel m, AbstractPerturbation ap, Color c, List<Tuple2D<Integer>> lTuples) {
		this.perturbations.addPerturbationColor(m, ap, c);
		if (lTuples != null) {
			this.grid.setPerturbation(m, lTuples, ap);
		}
	}

	public void setModel(int x, int y, LogicalModel m) {
		this.grid.setModel(x, y, m);
	}

	public void setGridWithModel(LogicalModel m, List<Tuple2D<Integer>> lTuples) {
		for (Tuple2D<Integer> tuple : lTuples) {
			this.setModel(tuple.getX(), tuple.getY(), m);
		}
		if (!this.modelEventManager.containsModel(m)) {
			this.modelEventManager.addModel(m);
		}
	}

	public void initModelEventManager() {
		this.modelEventManager = new ModelEventManager();
	}

	public void initModelHeritableNodes() {
		this.modelHeritableNodes = new ModelHeritableNodes();
	}

	public ModelEventManager getModelEventManager() {
		return this.modelEventManager;
	}

	public void setModelEventManager(ModelEventManager tem) {
		this.modelEventManager = tem;
	}

	public int getX() {
		return this.grid.getX();
	}

	public int getY() {
		return this.grid.getY();
	}
}
