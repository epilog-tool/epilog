package org.epilogtool.core;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;
import org.colomoto.logicalmodel.perturbation.AbstractPerturbation;
import org.colomoto.logicalmodel.perturbation.FixedValuePerturbation;
import org.colomoto.logicalmodel.perturbation.MultiplePerturbation;
import org.colomoto.logicalmodel.perturbation.RangePerturbation;
import org.epilogtool.common.Tuple2D;
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

	public Epithelium(int x, int y, String topologyID, String name,
			LogicalModel m, RollOver rollover,
			ProjectFeatures projectFeatures)
			throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, ClassNotFoundException {
		this.name = name;
		this.grid = new EpitheliumGrid(x, y, topologyID, rollover, m);
		this.priorities = new EpitheliumUpdateSchemeIntra();
		this.priorities.addModel(m);
		this.integrationFunctions = new EpitheliumIntegrationFunctions();
		this.perturbations = new EpitheliumPerturbations();
		this.projectFeatures = projectFeatures;
		this.updateSchemeInter = new EpitheliumUpdateSchemeInter(
				EpitheliumUpdateSchemeInter.DEFAULT_ALPHA, new HashMap<ComponentPair, Float>());
	}

	private Epithelium(String name,
			EpitheliumGrid grid, EpitheliumIntegrationFunctions eif,
			EpitheliumUpdateSchemeIntra epc, EpitheliumPerturbations eap,
			ProjectFeatures pf, EpitheliumUpdateSchemeInter usi) {
		this.name = name;
		this.grid = grid;
		this.priorities = epc;
		this.integrationFunctions = eif;
		this.projectFeatures = pf;
		this.perturbations = eap;
		this.updateSchemeInter = usi;
	}
	
	public void updateEpitheliumGrid(int gridX, int gridY, String topologyID, RollOver rollover) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
		this.grid.updateEpitheliumGrid(gridX, gridY, topologyID, rollover);
	}

	public boolean hasModel(LogicalModel m) {
		return this.grid.hasModel(m);
	}

	public Epithelium clone() {
		return new Epithelium("CopyOf_"
				+ this.name, this.grid.clone(),
				this.integrationFunctions.clone(), this.priorities.clone(),
				this.perturbations.clone(), this.projectFeatures,
				this.updateSchemeInter.clone());
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
		Set<ComponentPair> sCP = new HashSet<ComponentPair>(this.integrationFunctions.getComponentPair());
		for (ComponentPair cp : sCP) {
			NodeInfo node = cp.getNodeInfo();
			if (!sNodeIDs.contains(node.getNodeID())) {
				this.integrationFunctions.removeComponent(cp);
			}
		}
	}

	public EpitheliumUpdateSchemeInter getUpdateSchemeInter() {
		return this.updateSchemeInter;
	}

	public String toString() {
		return this.getName();
		// return this.name + " ("
		// + this.grid.getTopology().getRollOver().toString() + ")";
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

	public ProjectFeatures getProjectFeatures() {
		return this.projectFeatures;
	}

	public void setGridWithModel(LogicalModel m, List<Tuple2D<Integer>> lTuples) {
		for (Tuple2D<Integer> tuple : lTuples) {
			this.grid.setModel(tuple.getX(), tuple.getY(), m);
		}
	}

	public void setGridWithComponentValue(String nodeID, byte value,
			List<Tuple2D<Integer>> lTuples) {
		for (Tuple2D<Integer> tuple : lTuples) {
			this.grid.setCellComponentValue(tuple.getX(), tuple.getY(), nodeID,
					value);
		}
	}

	public void setIntegrationFunction(String nodeID, LogicalModel m, byte value,
			String function) {
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
			ComponentPair cp) {
		return this.integrationFunctions
				.getComponentIntegrationFunctions(cp);
	}

	public Set<ComponentPair> getIntegrationComponentPairs() {
		return this.integrationFunctions.getComponentPair();
	}

	public boolean isIntegrationComponent(NodeInfo node) {
		for (ComponentPair cp : this.integrationFunctions.getComponentPair()){
			if (node.equals(cp.getNodeInfo())) return true;
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

	public void setModel(int x, int y, LogicalModel m) {
		this.grid.setModel(x, y, m);
	}

	public int getX() {
		return this.grid.getX();
	}

	public int getY() {
		return this.grid.getY();
	}

	public boolean equals(Object o) {
		Epithelium otherEpi = (Epithelium) o;
		return (this.grid.equals(otherEpi.grid)
				&& this.priorities.equals(otherEpi.priorities)
				&& this.integrationFunctions
						.equals(otherEpi.integrationFunctions)
				&& this.perturbations.equals(otherEpi.perturbations) && this.updateSchemeInter
					.equals(otherEpi.getUpdateSchemeInter()));
	}
	
	
	

	
	
	/**
	 * @param oldModel
	 * @param newModel
	 * @param oldEpi
	 */
	public void replacemodel(LogicalModel oldModel, LogicalModel newModel, Epithelium oldEpi) {
		// TODO Auto-generated method stub
		
		EpitheliumGrid grid = this.getEpitheliumGrid();
		List<String> commonNodeNames = new ArrayList<String>();
		
		//TODO: START REPLACING
		
		for (int y = 0; y < this.getY(); y++) {
			for (int x = 0; x < this.getX(); x++) {
				LogicalModel cellModel =grid.getModel(x, y);
				if (cellModel==oldModel){
					grid.setModel(x, y, newModel); //ReplaceModel

				}}}
		
		for (NodeInfo node: newModel.getNodeOrder()){
			this.initPriorityClasses(newModel);
			
			
			//Check multivalued
			for (NodeInfo oldNode: oldModel.getNodeOrder()){
				if (node.toString().equals(oldNode.toString())){ //there is a node with the same name n both epitheliums
					//TODO:If there is a node with the same name n both epitheliums
					if (node.isInput() && oldNode.isInput()){//The shared node is an input in both epitheliums
						if (oldEpi.isIntegrationComponent(oldNode)){//If the input is a integration Input it remais as an integration input
							//TODO: set epi.integration function, but check if the regulators are there
						}
						else{
							//TODO: Set node as env input and set initial condition
						}
					}
					else if(!node.isInput() && oldNode.isInput()){
						if (!oldEpi.isIntegrationComponent(oldNode)){
							//TODO: set initial conditions of new internal comp (node) with value of the the env input (oldNode)
						}
					}
					else if(node.isInput() && !oldNode.isInput()){
							//TODO: set initial conditions of new env comp (node-by default it starts as env) with value of the intiial conditions of the internal comp (oldNode)
						}
					else if(!node.isInput() && !oldNode.isInput()){
						//TODO: set initial conditions
						//TODO: If there is a perturbation associated with this node then set it on the new epithelium
						//TODO: Set priorities for the common internal nodes
						commonNodeNames.add(node.toString());
					}
					}
				}
			}
		
		//TODO: check of the remaining models in the epithelium if the regulator is
		
		this.replacePriorities(oldEpi,oldModel,newModel,commonNodeNames);
		this.replacePerturbations(oldEpi,oldModel,newModel,commonNodeNames);
	}

	
	/** 
	 * @param epithelium
	 * @param oldEpi
	 * @param oldModel
	 * @param newModel
	 * @param commonNodeNames
	 */
	public void replacePriorities(Epithelium oldEpi, LogicalModel oldModel, LogicalModel newModel, List<String> commonNodeNames) {
		ModelPriorityClasses oldMpc = oldEpi.getPriorityClasses(oldModel);
		
		String sPCs = "";
		for (int idxPC = 0; idxPC < oldMpc.size(); idxPC++) {
			if (!sPCs.isEmpty())
				sPCs += ":";
			
			List<String> pcVars = oldMpc.getClassVars(idxPC);
			List<String> newPCVars = new ArrayList<String>();
			
				for (int pcVarIndex = 0; pcVarIndex<pcVars.size();++pcVarIndex){
					String component = pcVars.get(pcVarIndex);
					if (commonNodeNames.contains(component)){
						newPCVars.add(component);
					}
					if (pcVarIndex==0 && idxPC == 0){
						for (NodeInfo node:newModel.getNodeOrder()){
							if (!commonNodeNames.contains(node.toString()) && !node.isInput()){
								newPCVars.add(node.toString());
							}}}}
			sPCs += join(newPCVars, ",");}
		this.setPriorityClasses(newModel, sPCs);}
	
	
	private void replacePerturbations(Epithelium oldEpi, LogicalModel oldModel, LogicalModel newModel,
			List<String> commonNodeNames) {
		// TODO Auto-generated method stub
		ModelPerturbations oldPerturbations = this.perturbations.getModelPerturbations(oldModel);
		List<AbstractPerturbation> perturbation = new ArrayList<AbstractPerturbation>();


		
		for (AbstractPerturbation p :oldPerturbations.getAllPerturbations()){
			List<String> perturbedComponents = new ArrayList<String>();
			int indexPerturbationShared = 0;
			for (String pert: p.toString().split(",")){
				if (commonNodeNames.contains(pert.split(" ")[0].trim())){
					indexPerturbationShared = ++indexPerturbationShared;}
			}
			if (p.toString().contains(",")){
				
					if(indexPerturbationShared==p.toString().split(",").length){
						perturbation.add(p);
						this.addPerturbation(newModel, p);
//						this.getModelPerturbations(newModel).addPerturbation(p);
//						Color c = oldEpi.getModelPerturbations(oldModel).getPerturbationColor(p);
//						this.getModelPerturbations(newModel).addPerturbationColor(p, c);
			}}
					else{
						if(indexPerturbationShared==1){
							perturbation.add(p);
							this.addPerturbation(newModel, p);
//							this.getModelPerturbations(newModel).addPerturbation(p);
//							Color c = oldEpi.getModelPerturbations(oldModel).getPerturbationColor(p);
//							this.getModelPerturbations(newModel).addPerturbationColor(p, c);
							System.out.println("Added pert [single]: "+ p );
							
					}}}

		
		//Add perturbation to cell
		for (int y = 0; y < this.getY(); y++) {
			for (int x = 0; x < this.getX(); x++) {
				AbstractPerturbation p = oldEpi.getEpitheliumGrid().getPerturbation(x, y);
//				System.out.println(p);
				Tuple2D<Integer> tmpTuple = new Tuple2D<Integer>(x, y);
				List<Tuple2D<Integer>> tmpList = new ArrayList<Tuple2D<Integer>>();
				Color c = oldEpi.getModelPerturbations(oldModel).getPerturbationColor(p);
				if (p!=null & perturbation.contains(p)){
					
					tmpList.add(tmpTuple);
					this.getEpitheliumGrid().setPerturbation(x, y, p);
					
					
				}this.applyPerturbation(newModel, p, c,tmpList);
			}}
		
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
	
}
