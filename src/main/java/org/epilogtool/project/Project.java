package org.epilogtool.project;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;
import org.colomoto.logicalmodel.tool.simulation.updater.PriorityClasses;
import org.epilogtool.core.Epithelium;
import org.epilogtool.core.EpitheliumGrid;
import org.epilogtool.core.ModelPriorityClasses;
import org.epilogtool.core.topology.RollOver;

public class Project {
	private List<Epithelium> epitheliumList;
	private ProjectFeatures projectFeatures;
	private String filenamePEPS;
	private boolean isChanged;

	public Project(){
		this.epitheliumList = new ArrayList<Epithelium>();
		this.projectFeatures = new ProjectFeatures();
		this.filenamePEPS = null;
		this.isChanged = true;
	}

	public boolean hasChanged() {
		return isChanged;
	}

	public void setChanged(boolean state) {
		this.isChanged = state;
	}

	public List<Epithelium> getEpitheliumList() {
		return this.epitheliumList;
	}

	public List<String> getEpitheliumNameList() {
		List<String> l = new ArrayList<String>();
		for (Epithelium epi : this.epitheliumList) {
			l.add(epi.getName());
		}
		return l;
	}

	public Epithelium cloneEpithelium(Epithelium epi) {
		Epithelium epiClone = epi.clone();
		epiClone.setName(this.getNextAvailableName(epi.getName()));
		this.epitheliumList.add(epiClone);
		return epiClone;
	}

	private String getNextAvailableName(String basename) {
		List<String> l = new ArrayList<String>();
		for (Epithelium epi : this.epitheliumList) {
			l.add(epi.getName());
		}
		for (int i = 1; true; i++) {
			if (!l.contains(basename + "_Cloned" + i))
				return basename + "_Cloned" + i;
		}
	}

	public void removeEpithelium(Epithelium epi) {
		this.epitheliumList.remove(epi);
		// this.updateModelMap();
	}

	public ProjectFeatures getComponentFeatures() {
		return this.projectFeatures;
	}

	public String getFilenamePEPS() {
		return this.filenamePEPS;
	}

	public void setFilenamePEPS(String filename) {
		this.filenamePEPS = filename;
	}

	public Epithelium newEpithelium(int x, int y, String topologyID, 
			String userName, String modelName, RollOver rollover) 
					throws InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException,
			SecurityException, ClassNotFoundException {
		Epithelium epi = new Epithelium(x, y, topologyID, userName, 
				this.projectFeatures.getModel(modelName), rollover, this.projectFeatures);
		this.epitheliumList.add(epi);
		return epi;
	}

	// TODO: test with same name models in diff directories
	// public String getNextModelName(File file) {
	// String name = file.getName().substring(0, file.getName().indexOf("."));
	// while (this.modelMap.containsKey(name)) {
	// name += "_";
	// }
	// return name;
	// }

	public void loadModel(String name, LogicalModel m) {
		this.projectFeatures.addModel(name, m);
		// TODO: should the model be inserted somewhere else?
		this.isChanged = true;
	}

	public boolean removeModel(String name) {
		if (!this.isUsedModel(name)) {
			this.projectFeatures.removeModel(name);
			this.isChanged = true;
			return true;
		}
		return false;
	}

	public boolean isUsedModel(String name) {
		LogicalModel m = this.projectFeatures.getModel(name);
		for (Epithelium epi : this.epitheliumList) {
			if (epi.hasModel(m))
				return true;
		}
		return false;
	}

	public Set<String> getModelNames() {
		return this.projectFeatures.getModelNames();
	}

	public LogicalModel getModel(String name) {
		return this.projectFeatures.getModel(name);
	}
	
	public ProjectFeatures getProjectFeatures() {
		return this.projectFeatures;
	}
	
	/** Method that returns an Hash with models as keys and a list of epitheliums with each model as value.
	 * @return
	 */
	public Map<String,List<Epithelium>>  getHashModel2EpitheliumList(){
		Map<String,List<Epithelium>> model2EpitheliumList = new HashMap<String, List<Epithelium>>();
		
		for (String model: this.getModelNames()){
			List<Epithelium> epiList = new ArrayList<Epithelium>() ;
			for (Epithelium epi: this.getEpitheliumList()){
				if (epi.hasModel(this.getModel(model))){
					epiList.add(epi);
				}
			}
			model2EpitheliumList.put(model, epiList);
			}
		return model2EpitheliumList;
	}
	
	/** Given an EpitheliumName (String) return the Epithelium
	 * @param epiName
	 * @return Epithelium
	 */
	private Epithelium getEpitheliumFromName (String epiName){
		
		for (Epithelium epi: this.getEpitheliumList()){
			if (epi.getName()==epiName){
				return epi;
			}
		}
		return null;
	}
	
	public void replaceModel(String oldModelString, String newModelString, List<String> epiList){
		
		
		LogicalModel oldModel = this.getModel(oldModelString);
		LogicalModel newModel = this.getModel(newModelString);
		
		for (String epi: epiList){
			Epithelium epithelium = getEpitheliumFromName(epi);
			Epithelium oldEpi = cloneEpithelium(epithelium);
			EpitheliumGrid grid = epithelium.getEpitheliumGrid();
			List<String> commonNodeNames = new ArrayList<String>();
			
			//TODO: START REPLACING
		
			
			for (int y = 0; y < epithelium.getY(); y++) {
				for (int x = 0; x < epithelium.getX(); x++) {
					LogicalModel cellModel =grid.getModel(x, y);
					if (cellModel==oldModel){
						grid.setModel(x, y, newModel); //ReplaceModel

					}}}
		
			for (NodeInfo node: newModel.getNodeOrder()){
				epithelium.initPriorityClasses(newModel);
				
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
			
			ModelPriorityClasses oldModelPriorityClasses = oldEpi.getPriorityClasses(oldModel);
			PriorityClasses oldPriorityClasses = oldModelPriorityClasses.getPriorities();
			

			for (int i = 0; i<oldPriorityClasses.size();i++){
				for (String nodeName: oldModelPriorityClasses.getClassVars(i)){
					epithelium.getPriorityClasses(newModel).
				}
				
			}
			
			this.removeEpithelium(oldEpi);
			epithelium.update();
			}
			
	
		}}

