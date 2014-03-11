package org.ginsim.epilog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.colomoto.logicalmodel.LogicalModel;
import org.ginsim.epilog.core.Epithelium;

public class Project {
	private int x;
	private int y;
	private List<Epithelium> epitheliumList;
	private Map<String, LogicalModel> modelMap;
	private String filenamePEPS;
	private boolean changed;

	public Project(int x, int y) {
		this.x = x;
		this.y = y;
		this.epitheliumList = new ArrayList<Epithelium>();
		this.modelMap = new HashMap<String, LogicalModel>();
		this.filenamePEPS = null;
		this.changed = true;
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

	public String getFilenamePEPS() {
		return this.filenamePEPS;
	}

	public void setFilenamePEPS(String filename) {
		this.filenamePEPS = filename;
	}

	public void newEpithelium(String userName, String modelName) {
		Epithelium epi = new Epithelium(this.x, this.y,
				this.modelMap.get(modelName), userName);
		this.epitheliumList.add(epi);
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
	}

	public Set<String> getModelNames() {
		return this.modelMap.keySet();
	}

	public LogicalModel getModel(String name) {
		return this.modelMap.get(name);
	}
}
