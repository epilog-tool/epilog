package org.ginsim.epilog;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.colomoto.logicalmodel.LogicalModel;
import org.ginsim.epilog.core.Epithelium;
import org.ginsim.epilog.core.EpitheliumGrid;
import org.ginsim.epilog.core.topology.RollOver;

public class Project {
	private int x;
	private int y;
	private String topologyLayout;
	private List<Epithelium> epitheliumList;
	private Map<String, LogicalModel> modelMap;
	private String filenamePEPS;
	private boolean bChanged; // TODO

	public Project(int x, int y, String topologyLayout) {
		this.x = x;
		this.y = y;
		this.topologyLayout = topologyLayout;
		this.epitheliumList = new ArrayList<Epithelium>();
		this.modelMap = new HashMap<String, LogicalModel>();
		this.filenamePEPS = null;
		this.bChanged = true;
	}

	public boolean hasChanged() {
		return bChanged;
	}

	public void setChanged(boolean state) {
		this.bChanged = state;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public List<Epithelium> getEpitheliumList() {
		return this.epitheliumList;
	}

	public List<String> getEpitheliumNameList() {
		List<String> l = new ArrayList<String>();
		for (Epithelium epi : this.epitheliumList) {
			l.add(epi.toString());
		}
		return l;
	}

	public Epithelium cloneEpithelium(Epithelium epi) {
		Epithelium epiClone = epi.clone();
		epiClone.setName(this.getNextAvailableName(epi.toString()));
		this.epitheliumList.add(epiClone);
		return epiClone;
	}
	
	private String getNextAvailableName(String basename) {
		List<String> l = new ArrayList<String>();
		for (Epithelium epi : this.epitheliumList) {
			l.add(epi.toString());
		}
		for (int i=1; true ;i++) {
			if (!l.contains(basename + "_Cloned"+ i))
				return basename + "_Cloned"+ i;
		}
	}

	public void removeEpithelium(Epithelium epi) {
		this.epitheliumList.remove(epi);
		this.updateModelMap();
	}

	private void updateModelMap() {
		Set<LogicalModel> newModelSet = new HashSet<LogicalModel>();
		for (Epithelium epi : this.epitheliumList) {
			EpitheliumGrid grid = epi.getEpitheliumGrid();
			grid.updateModelSet();
			newModelSet.addAll(grid.getModelSet());
		}
		Map<String, LogicalModel> newModelMap = new HashMap<String, LogicalModel>();
		for (String name : this.modelMap.keySet()) {
			if (newModelSet.contains(this.modelMap.get(name))) {
				newModelMap.put(name, this.modelMap.get(name));
			}
		}
		this.modelMap = newModelMap;
	}

	public Map<String, LogicalModel> getModelsMap() {
		return Collections.unmodifiableMap(this.modelMap);
	}

	public String getFilenamePEPS() {
		return this.filenamePEPS;
	}

	public void setFilenamePEPS(String filename) {
		this.filenamePEPS = filename;
	}

	public Epithelium newEpithelium(String userName, String modelName, RollOver rollover) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
		Epithelium epi = new Epithelium(this.x, this.y, this.topologyLayout, rollover,
				this.modelMap.get(modelName), userName);
		this.epitheliumList.add(epi);
		return epi;
	}

	public String getNextModelName(File file) {
		String name = file.getName().substring(0, file.getName().indexOf("."));
		while (this.modelMap.containsKey(name)) {
			name += "_";
		}
		return name;
	}

	public void addModel(String name, LogicalModel m) {
		this.modelMap.put(name, m);
		// TODO: should the model be inserted somewhere else?
		this.bChanged = true;
	}

	public boolean removeModel(String name) {
		if (!this.hasModel(name)) {
			this.modelMap.remove(name);
			this.bChanged = true;
			return true;
		}
		return false;
	}

	public boolean hasModel(String name) {
		for (Epithelium epi : this.epitheliumList) {
			if (epi.hasModel(this.modelMap.get(name)))
				return true;
		}
		return false;
	}

	public Set<String> getModelNames() {
		return this.modelMap.keySet();
	}

	public LogicalModel getModel(String name) {
		return this.modelMap.get(name);
	}
}
