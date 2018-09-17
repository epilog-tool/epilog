package org.epilogtool.core;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.antlr.runtime.RecognitionException;
import org.colomoto.biolqm.LogicalModel;
import org.colomoto.biolqm.NodeInfo;
import org.colomoto.biolqm.modifier.perturbation.AbstractPerturbation;
import org.epilogtool.common.EnumRandomSeed;
import org.epilogtool.common.Tuple2D;
import org.epilogtool.core.cell.AbstractCell;
import org.epilogtool.core.cell.LivingCell;
import org.epilogtool.core.topology.RollOver;
import org.epilogtool.gui.dialog.DialogMessage;
import org.epilogtool.project.Project;

public class Epithelium {
	private String name;
	private EpitheliumGrid grid;
	private EpitheliumIntegrationFunctions integrationFunctions;
	private EpitheliumPerturbations perturbations;
	private EpitheliumUpdateSchemeIntra priorities;
	private EpitheliumUpdateSchemeInter updateSchemeInter;
	private EpitheliumEvents epitheliumEvents;

	
	

	public Epithelium(int x, int y, String topologyID, String name, AbstractCell c, RollOver rollover,
			EnumRandomSeed randomSeedType, int randomSeed)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, ClassNotFoundException {
		this.name = name;
		this.grid = new EpitheliumGrid(x, y, topologyID, rollover,c);
		this.priorities = new EpitheliumUpdateSchemeIntra();
		if (c.isLivingCell()) {
			this.priorities.addModel(((LivingCell) c).getModel());
		}

		this.integrationFunctions = new EpitheliumIntegrationFunctions();
		this.perturbations = new EpitheliumPerturbations();
		this.updateSchemeInter = new EpitheliumUpdateSchemeInter(EpitheliumUpdateSchemeInter.DEFAULT_ALPHA,
				UpdateCells.UPDATABLECELLS, randomSeedType, randomSeed);
		this.epitheliumEvents = new EpitheliumEvents(EpitheliumEvents.DEFAULT_DIVISIONPROBABILITY, EpitheliumEvents.DEFAULT_DEATHPROBABILITY, EpitheliumEvents.DEFAULT_ORDER,EpitheliumEvents.DEFAULT_NEWCELL,EpitheliumEvents.DEFAULT_DEATHOPTION,null);
	
	}

	private Epithelium(String name, EpitheliumGrid grid, EpitheliumIntegrationFunctions eif,
			EpitheliumUpdateSchemeIntra epc, EpitheliumPerturbations eap, EpitheliumUpdateSchemeInter usi, EpitheliumEvents ev) {
		this.name = name;
		this.grid = grid;
		this.priorities = epc;
		this.integrationFunctions = eif;
		this.perturbations = eap;
		this.updateSchemeInter = usi;
		this.epitheliumEvents = ev;
		
	}

	public Epithelium clone() {
		return new Epithelium("CopyOf_" + this.name, this.grid.clone(), this.integrationFunctions.clone(),
				this.priorities.clone(), this.perturbations.clone(), this.updateSchemeInter.clone(), this.epitheliumEvents.clone());
	}

	public String toString() {
		return this.getName();
	}

	public boolean equals(Object o) {
		//TODO: solve the Events equals
		Epithelium otherEpi = (Epithelium) o;
		System.out.println("PT " + this.perturbations.equals(otherEpi.perturbations));
		System.out.println("PR " + this.priorities.equals(otherEpi.priorities));
//		System.out.println("EV: "+ this.epitheliumEvents.equals(otherEpi.getEpitheliumEvents()));
		System.out.println("UP: "+ this.updateSchemeInter.equals(otherEpi.getUpdateSchemeInter()));
		System.out.println("GR " + this.grid.equals(otherEpi.grid));
		return (this.grid.equals(otherEpi.grid) && this.priorities.equals(otherEpi.priorities)
				&& this.integrationFunctions.equals(otherEpi.integrationFunctions)
				&& this.perturbations.equals(otherEpi.perturbations)
//				&& this.epitheliumEvents.equals(otherEpi.getEpitheliumEvents())
				&& this.updateSchemeInter.equals(otherEpi.getUpdateSchemeInter()));
		
	}

	public void updateEpitheliumGrid(int gridX, int gridY, String topologyID, RollOver rollover)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, ClassNotFoundException {
		this.grid.editEpitheliumGrid(gridX, gridY, topologyID, rollover);
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

		}

		// Remove from Epithelium state absent models from modelSet
		for (LogicalModel mPriorities : new ArrayList<LogicalModel>(this.priorities.getModelSet())) {
			if (!modelSet.contains(mPriorities)) {
				this.priorities.removeModel(mPriorities);
			}
		}


		// Create list with all existing Components
		Set<String> sNodeIDs = new HashSet<String>();
		for (LogicalModel m : modelSet) {
			for (NodeInfo node : m.getComponents()) {
				sNodeIDs.add(node.getNodeID());
			}
		}
		// Clean Epithelium components
		Set<NodeInfo> sNodeInfo = new HashSet<NodeInfo>(this.integrationFunctions.getNodes());
		for (NodeInfo node : sNodeInfo) {
			if (!sNodeIDs.contains(node.getNodeID())) {
				this.integrationFunctions.removeComponent(node);
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
	
	public EpitheliumEvents getEpitheliumEvents() {
		return this.epitheliumEvents;
	}

	public ModelPriorityClasses getPriorityClasses(LogicalModel m) {
		return this.priorities.getModelPriorityClasses(m);
	}

	public ComponentIntegrationFunctions getIntegrationFunctionsForComponent(NodeInfo node) {
		return this.integrationFunctions.getComponentIntegrationFunctions(node);
	}

	public Set<NodeInfo> getIntegrationNodes() {
		return this.integrationFunctions.getNodes();
	}

	public boolean isIntegrationComponent(String nodeID) {
		NodeInfo node = Project.getInstance().getProjectFeatures().getNodeInfo(nodeID);
		
		for (NodeInfo nodeInf : this.integrationFunctions.getNodes()) {
			if (node.equals(nodeInf))
				return true;
		}
		return false;
	}
	
	public boolean isIntegrationComponent(NodeInfo node) {
		for (NodeInfo nodeInf : this.integrationFunctions.getNodes()) {
			if (node.equals(nodeInf))
				return true;
		}
		return false;
	}

	public EpitheliumIntegrationFunctions getIntegrationFunctions() {
		return this.integrationFunctions;
	}

	public EpitheliumPerturbations getEpitheliumPerturbations() {
		return this.perturbations;
	}

	public void setGridWithComponentValue(String nodeID, byte value, List<Tuple2D<Integer>> lTuples) {
		for (Tuple2D<Integer> tuple : lTuples) {
			this.grid.setCellComponentValue(tuple.getX(), tuple.getY(), nodeID, value);
		}
	}

	public void setIntegrationFunction(String nodeID, byte value, String function)
			throws RecognitionException, RuntimeException {
		NodeInfo node = Project.getInstance().getProjectFeatures().getNodeInfo(nodeID);
		if (!this.integrationFunctions.containsNode(node)) {
			this.integrationFunctions.addComponent(node);
		}
		this.integrationFunctions.setFunctionAtLevel(node, value, function);
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

	public void addPerturbation(AbstractPerturbation ap) {
		this.perturbations.addPerturbation(ap);
	}

	public void delPerturbation(AbstractPerturbation ap) {
		this.perturbations.delPerturbation(ap);
	}

	public void applyPerturbation(AbstractPerturbation ap, Color c, List<Tuple2D<Integer>> lTuples) {
		this.perturbations.addPerturbationColor(ap, c);
		if (lTuples != null) {
//			System.out.println("Epithelium: " + ap);
//			System.out.println("Epithelium: " + c);
//			System.out.println("Epithelium: " + lTuples);
			this.grid.setPerturbation(lTuples, ap);
		}
	}

	public void setModel(int x, int y, AbstractCell c) {
		this.grid.setAbstractCell(x,y,c);
	}

	public void setGridWithCell(AbstractCell c, List<Tuple2D<Integer>> lTuples) {
		for (Tuple2D<Integer> tuple : lTuples) {
			this.setModel(tuple.getX(), tuple.getY(), c);
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
		for (NodeInfo node : oldModel.getComponents()) {
			Boolean flag = false;
			String mes = "";
			for (NodeInfo nNode : newModel.getComponents()) {

				if (node.toString().equals(nNode.toString())) {
					flag = true;
					this.replaceInitialConditions(node, nNode, gridCopy);
					commonNodeNames.add(node.getNodeID());
					
					if (oldEpi.isIntegrationComponent(node) && nNode.isInput()) {
						
						//i) validate integration function
						EpitheliumIntegrationFunctions eif = this.getIntegrationFunctions();
						ComponentIntegrationFunctions cif = eif.getComponentIntegrationFunctions(node);
						List<String> lFunctions = cif.getFunctions();

						for (int i = 0; i < lFunctions.size(); i++) {
							String function = lFunctions.get(i);
							if (validateIntegrationFunction(function)) {
								try {
									this.setIntegrationFunction(nNode.getNodeID(), (byte) (i + 1), function);
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
//		 this.validateAllIntegrationFunctions(oldEpi, oldModel, newModel);
		 this.replacePriorities(oldEpi, oldModel, newModel, commonNodeNames);

//		if (this.getModelPerturbations(oldModel) != null) {
//			ModelPerturbations mpClone = this.getModelPerturbations(oldModel).clone();
//			EpitheliumPerturbations epClone = this.getEpitheliumPerturbations().clone();
//			this.replacePerturbations(gridCopy, mpClone, epClone, oldModel, newModel, commonNodeNames);
//		}
	}

	private boolean validateIntegrationFunction(String func) {

		boolean flag = false;

		return flag;
	}

	private void replaceInitialConditions(NodeInfo oldNode, NodeInfo newNode, EpitheliumGrid oldGrid) {
		for (int y = 0; y < this.getY(); y++) {
			for (int x = 0; x < this.getX(); x++) {
				byte value = oldGrid.getCellValue(x, y, oldNode.toString());
				if (0 < value && value <= newNode.getMax()) {
					this.getEpitheliumGrid().setCellComponentValue(x, y, newNode.toString(), value);
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
					for (NodeInfo node : newModel.getComponents()) {
						if (!commonNodeNames.contains(node.toString()) && !node.isInput()) {
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

	private void replacePerturbations(EpitheliumGrid gridCopy,
			EpitheliumPerturbations epClone, LogicalModel oldModel, LogicalModel newModel,
			List<String> commonNodeNames) {

			//TODO
//		List<AbstractPerturbation> perturbation = new ArrayList<AbstractPerturbation>();
//		Boolean hasChanged = false;
//		if (oldPerturbations != null) {
//			for (AbstractPerturbation p : oldPerturbations.getAllPerturbations()) {
//				int indexPerturbationShared = 0;
//				for (String pert : p.toString().split(",")) {
//					if (commonNodeNames.contains(pert.trim().split(" ")[0].trim())) {
//						indexPerturbationShared++;
//					}
//				}
//				if (p.toString().contains(",")) {
//					if (indexPerturbationShared == p.toString().split(",").length) {
//						perturbation.add(p);
//						this.addPerturbation(p);
//					}
//				} else {
//					if (indexPerturbationShared == 1) {
//						perturbation.add(p);
//						this.addPerturbation(p);
//					}
//				}
//				if (!perturbation.contains(p))
//					hasChanged = true;
//			}
//
//			// Add perturbation to cell
//			for (int y = 0; y < this.getY(); y++) {
//				for (int x = 0; x < this.getX(); x++) {
//					AbstractPerturbation p = gridCopy.getPerturbation(x, y);
//
//					if (p != null) {
//						Boolean apply = false;
//						List<Tuple2D<Integer>> tmpList = new ArrayList<Tuple2D<Integer>>();
//						Color c = mpClone.getPerturbationColor(p);
//
//						if (perturbation.contains(p)) {
//							tmpList.add(new Tuple2D<Integer>(x, y));
//							this.getEpitheliumGrid().setPerturbation(x, y, p);
//							apply = true;
//						}
//						if (apply)
//							this.applyPerturbation(p, c, tmpList);
//					}
//				}
//			}
//		}
//
//		if (hasChanged) {
//			String msg = "" + this.toString() + ": " + "Perturbations were changed.";
//			if (!Project.getInstance().getProjectFeatures().getReplaceMessages().contains(msg)) {
//				Project.getInstance().getProjectFeatures().addReplaceMessages(msg);
//			}
//		}
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
		Random randomGenerator = new Random();

		for (int x = 0; x < this.getX(); x++) {
			for (int y = 0; y < this.getY(); y++) {
				List<NodeInfo> listNodes = this.getModel(x, y).getComponents();
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
			for (NodeInfo node : model.getComponents()) {
				if (node.getNodeID().equals(componentName))
					return node;

			}
		}

		return null;
	}
}
