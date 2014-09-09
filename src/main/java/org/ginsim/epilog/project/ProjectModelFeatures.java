package org.ginsim.epilog;

import java.awt.Color;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.colomoto.logicalmodel.LogicalModel;
import org.ginsim.epilog.gui.color.ColorUtils;

public class ProjectModelFeatures {

	private Map<String, LogicalModel> string2Model;
	private Map<LogicalModel, String> model2String;
	private Map<LogicalModel, Color> colorMap;

	public ProjectModelFeatures() {
		this.string2Model = new HashMap<String, LogicalModel>();
		this.model2String = new HashMap<LogicalModel, String>();
		this.colorMap = new HashMap<LogicalModel, Color>();
	}

	public Map<String, LogicalModel> getModelsMap() {
		return Collections.unmodifiableMap(this.string2Model);
	}

	public void addModel(String name, LogicalModel m) {
		this.string2Model.put(name, m);
		this.model2String.put(m, name);
		this.colorMap.put(m, ColorUtils.random());
	}

	public void removeModel(String name) {
		LogicalModel m = this.getModel(name);
		this.colorMap.remove(m);
		this.model2String.remove(m);
		this.string2Model.remove(name);
	}

	public Set<String> getNames() {
		return Collections.unmodifiableSet(this.string2Model.keySet());
	}

	public LogicalModel getModel(String name) {
		return this.string2Model.get(name);
	}

	public void setColor(String name, Color c) {
		this.colorMap.put(this.getModel(name), c);
	}

	public Color getColor(String name) {
		return this.getColor(this.string2Model.get(name));
	}

	public Color getColor(LogicalModel m) {
		return this.colorMap.get(m);
	}

	public void changeColor(String name, Color c) {
		this.colorMap.put(this.getModel(name), c);
	}

	public String getName(LogicalModel m) {
		return this.model2String.get(m);
	}

}
