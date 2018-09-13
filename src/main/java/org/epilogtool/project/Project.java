package org.epilogtool.project;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.colomoto.biolqm.LogicalModel;
import org.epilogtool.common.EnumRandomSeed;
import org.epilogtool.core.Epithelium;
import org.epilogtool.core.cell.AbstractCell;
import org.epilogtool.core.cell.CellFactory;
import org.epilogtool.core.topology.RollOver;
import org.epilogtool.gui.dialog.DialogMessage;

public class Project {
	private List<Epithelium> epitheliumList;
	private ProjectFeatures projectFeatures;
	private String filenamePEPS;
	private boolean isChanged;

	private static Project project;

	public static Project getInstance() {
		if (project == null) {
			project = new Project();
		}
		return project;
	}

	private Project() {
		this.epitheliumList = new ArrayList<Epithelium>();
		this.projectFeatures = new ProjectFeatures();
		this.filenamePEPS = null;
		this.isChanged = true;
	}

	public void reset() {
		project = null;
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
		String name = this.getNextAvailableName(epi.getName());
		epiClone.setName(name);
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

	public String getFilenamePEPS() {
		return this.filenamePEPS;
	}

	public void setFilenamePEPS(String filename) {
		this.filenamePEPS = filename;
	}

	public Epithelium newEpithelium(int x, int y, String topologyID, String userName, AbstractCell c,
			RollOver rollover, EnumRandomSeed randomSeedType, int randomSeed)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, ClassNotFoundException {
		Epithelium epi = new Epithelium(x, y, topologyID, userName, c, rollover,
				randomSeedType, randomSeed);
		this.epitheliumList.add(epi);
		return epi;
	}

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

	/**
	 * Method that returns an Hash with models as keys and a list of epithelia
	 * with each model as value.
	 * 
	 * @return
	 */
	public Map<String, List<Epithelium>> getHashModel2EpitheliumList() {
		Map<String, List<Epithelium>> model2EpitheliumList = new HashMap<String, List<Epithelium>>();

		for (String model : this.getModelNames()) {
			List<Epithelium> epiList = new ArrayList<Epithelium>();
			for (Epithelium epi : this.getEpitheliumList()) {
				if (epi.hasModel(this.getModel(model))) {
					epiList.add(epi);
				}
			}
			model2EpitheliumList.put(model, epiList);
		}
		return model2EpitheliumList;
	}

	/**
	 * Given an EpitheliumName (String) return the Epithelium
	 * 
	 * @param epiName
	 * @return Epithelium
	 */
	private Epithelium getEpitheliumFromName(String epiName) {

		for (Epithelium epi : this.getEpitheliumList()) {
			if (epi.getName().equals(epiName)) {
				return epi;
			}
		}
		return null;
	}

	public void replaceModel(String oldModelString, String newModelString, List<String> epiList,
			DialogMessage dialogMsg) {

		LogicalModel oldModel = this.getModel(oldModelString);
		LogicalModel newModel = this.getModel(newModelString);

		for (String epi : epiList) {
			Epithelium epithelium = this.getEpitheliumFromName(epi);
			epithelium.replacemodel(oldModel, newModel, dialogMsg);
			epithelium.update();
		}
	}

}
