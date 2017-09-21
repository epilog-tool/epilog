package org.epilogtool.core;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.antlr.runtime.RecognitionException;
import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;
import org.colomoto.logicalmodel.perturbation.AbstractPerturbation;
import org.epilogtool.common.EnumRandomSeed;
import org.epilogtool.common.Tuple2D;
import org.epilogtool.core.topology.RollOver;
import org.epilogtool.gui.dialog.DialogMessage;
import org.epilogtool.project.ComponentPair;
import org.epilogtool.project.Project;

public class Epithelium {
	private String name;
	private EpitheliumGrid grid;
	private EpitheliumIntegrationFunctions integrationFunctions;
	private EpitheliumPerturbations perturbations;
	private EpitheliumUpdateSchemeIntra priorities;
	private EpitheliumUpdateSchemeInter updateSchemeInter;
	// private HashMap<String, NodeInfo> sComponentsUsed2Node;

	public Epithelium(int x, int y, String topologyID, String name, LogicalModel m, RollOver rollover,
			EnumRandomSeed randomSeedType, int randomSeed)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, ClassNotFoundException {
		this.name = name;
		this.grid = new EpitheliumGrid(x, y, topologyID, rollover, m);
		this.priorities = new EpitheliumUpdateSchemeIntra();
		this.priorities.addModel(m);
		this.integrationFunctions = new EpitheliumIntegrationFunctions();
		this.perturbations = new EpitheliumPerturbations();
		this.updateSchemeInter = new EpitheliumUpdateSchemeInter(EpitheliumUpdateSchemeInter.DEFAULT_ALPHA,
				UpdateCells.UPDATABLECELLS, randomSeedType, randomSeed);
		// this.sComponentsUsed2Node = new HashMap<String, NodeInfo>();
	}

	private Epithelium(String name, EpitheliumGrid grid, EpitheliumIntegrationFunctions eif,
			EpitheliumUpdateSchemeIntra epc, EpitheliumPerturbations eap, EpitheliumUpdateSchemeInter usi) {
		this.name = name;
		this.grid = grid;
		this.priorities = epc;
		this.integrationFunctions = eif;
		this.perturbations = eap;
		this.updateSchemeInter = usi;
		// this.sComponentsUsed2Node = new HashMap<String, NodeInfo>();
	}

	public Epithelium clone() {
		return new Epithelium("CopyOf_" + this.name, this.grid.clone(), this.integrationFunctions.clone(),
				this.priorities.clone(), this.perturbations.clone(), this.updateSchemeInter.clone());
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
				&& this.updateSchemeInter.equals(otherEpi.getUpdateSchemeInter()));
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

	public void setGridWithComponentValue(String nodeID, byte value, List<Tuple2D<Integer>> lTuples) {
		for (Tuple2D<Integer> tuple : lTuples) {
			this.grid.setCellComponentValue(tuple.getX(), tuple.getY(), nodeID, value);
		}
	}

	public void setIntegrationFunction(String nodeID, LogicalModel m, byte value, String function)
			throws RecognitionException, RuntimeException {
		NodeInfo node = Project.getInstance().getProjectFeatures().getNodeInfo(nodeID, m);
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
	}

	public int getX() {
		return this.grid.getX();
	}

	public int getY() {
		return this.grid.getY();
	}

	/**
	 * 
	 * Replace model in the grid. 1) Every cell with the oldModel is replaced with
	 * the newModel 2) Nodes of both models are compared (if types are changed) 2.1)
	 * If a node is an input and changes to proper then all nodes are at level zero
	 * 2.1.1) If a node is an integration input, and it still is an input in the new
	 * model, it remains as an integration input. 2.2) If a node is a proper
	 * component, then it changes to an positional input and the values remain the
	 * same 2.3) Perturbations are only applied to proper components, so
	 * perturbations applied to new inputs are removed 2.4) Priorities are only
	 * applied to proper components, so input components are removed from priorities
	 * 3) Integration Functions are validated. If a regulator no longer exists, the
	 * function is invalid and it is ignored
	 * 
	 * 
	 * @param oldModel
	 * @param newModel
	 */
	public void replacemodel(LogicalModel oldModel, LogicalModel newModel, DialogMessage dialogMsg) {

		Epithelium oldEpi = this;
		EpitheliumGrid gridCopy = this.getEpitheliumGrid().clone();

		List<String> commonNodeNames = new ArrayList<String>();

		// 1) Every cell with the oldModel is replaced with the newModel
		for (int y = 0; y < this.getY(); y++) {
			for (int x = 0; x < this.getX(); x++) {
				LogicalModel cellModel = grid.getModel(x, y);
				if (cellModel == oldModel) {
					grid.setModel(x, y, newModel); // ReplaceModel
				}
			}
		}

		// 2) Types of nodes with the same name are compared
		for (NodeInfo node : oldModel.getNodeOrder()) {
			Boolean flag = false;
			String mes = "";
			for (NodeInfo nNode : newModel.getNodeOrder()) {

				if (node.toString().equals(nNode.toString())) {
					flag = true;
					this.replaceInitialConditions(node, nNode, gridCopy);
					commonNodeNames.add(node.toString());

					if (oldEpi.isIntegrationComponent(node) && nNode.isInput()) {

						// TODO validate IntegrationFunction
						ComponentPair cp = new ComponentPair(oldModel, node);
						EpitheliumIntegrationFunctions eif = this.getIntegrationFunctions();
						ComponentIntegrationFunctions cif = eif.getComponentIntegrationFunctions(cp);
						List<String> lFunctions = cif.getFunctions();

						for (int i = 0; i < lFunctions.size(); i++) {
							String function = lFunctions.get(i);
							if (validateIntegrationFunction(function)) {
								try {
									this.setIntegrationFunction(nNode.getNodeID(), newModel, (byte) (i + 1), function);
								} catch (RecognitionException re) {
									// TODO Auto-generated catch block
								} catch (RuntimeException re) {
									// TODO Auto-generated catch block
									dialogMsg.addMessage("Integration function: " + nNode.getNodeID() + ":" + (i + 1)
											+ " has invalid expression: " + function);
								}
							}
						}
					}

					if (node.isInput() && !nNode.isInput()) {
						mes = "Input component " + node.toString() + " is now an internal component.";
						if (!Project.getInstance().getProjectFeatures().getReplaceMessages().contains(mes)) {
							Project.getInstance().getProjectFeatures().addReplaceMessages(mes);
						}
					}
					if (!node.isInput() && nNode.isInput()) {
						mes = "Internal component " + node.toString() + " is now an input component.";
						if (!Project.getInstance().getProjectFeatures().getReplaceMessages().contains(mes)) {
							Project.getInstance().getProjectFeatures().addReplaceMessages(mes);
						}
					}
				}
			}
			if (!flag) {
				// this is only relevant if this node is a regulator of any IF

				mes = "Node " + node.toString() + " does not exist in this new model.";
				if (!Project.getInstance().getProjectFeatures().getReplaceMessages().contains(mes)) {
					Project.getInstance().getProjectFeatures().addReplaceMessages(mes);
				}
			}
		}
		this.initPriorityClasses(newModel);
		// this.validateAllIntegrationFunctions(oldEpi, oldModel, newModel);
		// this.replacePriorities(oldEpi, oldModel, newModel, commonNodeNames);

		if (this.getModelPerturbations(oldModel) != null) {
			ModelPerturbations mpClone = this.getModelPerturbations(oldModel).clone();
			EpitheliumPerturbations epClone = this.getEpitheliumPerturbations().clone();
			this.replacePerturbations(gridCopy, mpClone, epClone, oldModel, newModel, commonNodeNames);
		}
	}

	private boolean validateIntegrationFunction(String func) {

		boolean flag = false;

		return flag;
	}

	/**
	 *
	 * Validates all the Integration Functions (IF) of a given epithelium.
	 * 
	 * @param oldEpi
	 * @param oldModel
	 * @param newModel
	 */
	private void validateAllIntegrationFunctions(Epithelium oldEpi, LogicalModel oldModel, LogicalModel newModel) {

		EpitheliumIntegrationFunctions intFunctions = this.getIntegrationFunctions();
		System.out.println(intFunctions);

		Map<ComponentPair, ComponentIntegrationFunctions> funcs = intFunctions.getAllIntegrationFunctions();

		// try {
		// this.setIntegrationFunction(nodeID, m, value, function);
		// } catch (RuntimeException re) {
		// NotificationManager.warning("Parser",
		// "Integration function: " + saTmp[2] + ":" + value + " has invalid expression:
		// " + function);
		// }

	}

	// /**
	// * Retrieve the regulators of an Integration Function (IF).
	// * This is used on the replace, but should be verified as needed?
	// *
	// * @param integrationString
	// * @return
	// */
	// private Set<String> getRegulators(String integrationString) {
	// // TODO Auto-generated method stub
	//
	// IntegrationFunctionExpression expr = string2Expression(integrationString)
	//
	// System.out.println(integrationString);
	//
	// return traverseIFTreeRegulators(expr);
	// }

	private void replaceInitialConditions(NodeInfo oldNode, NodeInfo newNode, EpitheliumGrid oldGrid) {

		for (int y = 0; y < this.getY(); y++) {
			for (int x = 0; x < this.getX(); x++) {
				byte value = oldGrid.getCellValue(x, y, oldNode.toString());
				if (value > 0) {

					if (value <= newNode.getMax()) {

						this.getEpitheliumGrid().setCellComponentValue(x, y, newNode.toString(), value);

					}
				}
			}
		}
	}

	/**
	 * @param epithelium
	 * @param oldEpi
	 * @param oldModel
	 * @param newModel
	 * @param commonNodeNames
	 */
	public void replacePriorities(Epithelium oldEpi, LogicalModel oldModel, LogicalModel newModel,
			List<String> commonNodeNames) {
		ModelPriorityClasses oldMpc = oldEpi.getPriorityClasses(oldModel);

		Boolean hasChanged = false;

		String sPCs = "";
		for (int idxPC = 0; idxPC < oldMpc.size(); idxPC++) {
			if (!sPCs.isEmpty())
				sPCs += ":";

			List<String> pcVars = oldMpc.getClassVars(idxPC);
			List<String> newPCVars = new ArrayList<String>();

			for (int pcVarIndex = 0; pcVarIndex < pcVars.size(); ++pcVarIndex) {
				String component = pcVars.get(pcVarIndex);
				if (commonNodeNames.contains(component)) {

					newPCVars.add(component);
				}
				if (pcVarIndex == 0 && idxPC == 0) {
					for (NodeInfo node : newModel.getNodeOrder()) {
						if (!commonNodeNames.contains(node.toString()) && !node.isInput()) {
							// System.out.println(node);
							newPCVars.add(node.toString());
						}
					}
				}
			}
			sPCs += join(newPCVars, ",");

			if (!pcVars.equals(newPCVars))
				hasChanged = true;
		}
		this.setPriorityClasses(newModel, sPCs);

		if (hasChanged) {
			String msg = "" + this.toString() + ": " + "Priorities were changed.";
			if (!Project.getInstance().getProjectFeatures().getReplaceMessages().contains(msg)) {
				Project.getInstance().getProjectFeatures().addReplaceMessages(msg);
			}
		}
	}

	private void replacePerturbations(EpitheliumGrid gridCopy, ModelPerturbations mpClone,
			EpitheliumPerturbations epClone, LogicalModel oldModel, LogicalModel newModel,
			List<String> commonNodeNames) {

		ModelPerturbations oldPerturbations = this.perturbations.getModelPerturbations(oldModel);
		List<AbstractPerturbation> perturbation = new ArrayList<AbstractPerturbation>();
		Boolean hasChanged = false;
		if (oldPerturbations != null) {
			for (AbstractPerturbation p : oldPerturbations.getAllPerturbations()) {
				int indexPerturbationShared = 0;
				for (String pert : p.toString().split(",")) {
					if (commonNodeNames.contains(pert.trim().split(" ")[0].trim())) {
						indexPerturbationShared++;
					}
				}
				if (p.toString().contains(",")) {
					if (indexPerturbationShared == p.toString().split(",").length) {
						perturbation.add(p);
						this.addPerturbation(newModel, p);
					}
				} else {
					if (indexPerturbationShared == 1) {
						perturbation.add(p);
						this.addPerturbation(newModel, p);
					}
				}
				if (!perturbation.contains(p))
					hasChanged = true;
			}

			// Add perturbation to cell
			for (int y = 0; y < this.getY(); y++) {
				for (int x = 0; x < this.getX(); x++) {
					AbstractPerturbation p = gridCopy.getPerturbation(x, y);

					if (p != null) {
						Boolean apply = false;
						// System.out.println(p);
						// Tuple2D<Integer> tmpTuple = new Tuple2D<Integer>(x,
						// y);
						List<Tuple2D<Integer>> tmpList = new ArrayList<Tuple2D<Integer>>();
						Color c = mpClone.getPerturbationColor(p);

						if (perturbation.contains(p)) {
							tmpList.add(new Tuple2D<Integer>(x, y));
							this.getEpitheliumGrid().setPerturbation(x, y, p);
							apply = true;
						}
						if (apply)
							this.applyPerturbation(newModel, p, c, tmpList);
					}
				}
			}
		}

		if (hasChanged) {
			String msg = "" + this.toString() + ": " + "Perturbations were changed.";
			if (!Project.getInstance().getProjectFeatures().getReplaceMessages().contains(msg)) {
				Project.getInstance().getProjectFeatures().addReplaceMessages(msg);
			}
		}
	}

	private static String join(List<String> list, String sep) {
		String s = "";
		for (int i = 0; i < list.size(); i++) {
			if (i > 0)
				s += sep;
			s += list.get(i);
		}
		return s;
	}

	public void setRandomInitialConditions() {
		// TODO Auto-generated method stub

		Random randomGenerator = new Random();

		for (int x = 0; x < this.getX(); x++) {
			for (int y = 0; y < this.getY(); y++) {
				List<NodeInfo> listNodes = this.getModel(x, y).getNodeOrder();
				for (NodeInfo node : listNodes) {
					if (!node.isInput()) {
						byte maxValue = node.getMax();
						int value = randomGenerator.nextInt(maxValue + 1);
						this.getEpitheliumGrid().setCellComponentValue(x, y, node.getNodeID(), (byte) value);
					}
				}
			}
		}
	}

	/**
	 * From the list of models used in the list, an hashset created, translating the
	 * node from string format to NodeInfo
	 * 
	 * @param componentName
	 * @return
	 */
	// TODO: This is a test
	public NodeInfo getComponentUsed(String componentName) {
		this.getEpitheliumGrid().updateModelSet();
		for (LogicalModel model : this.getEpitheliumGrid().getModelSet()) {
			for (NodeInfo node : model.getNodeOrder()) {
				if (node.getNodeID().equals(componentName))
					return node;

			}
		}

		return null;
	}
}
