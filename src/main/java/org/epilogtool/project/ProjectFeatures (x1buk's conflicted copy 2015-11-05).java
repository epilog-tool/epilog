package org.epilogtool.project;

import java.awt.Color;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;
import org.epilogtool.OptionStore;
import org.epilogtool.gui.color.ColorUtils;

public class ProjectFeatures {

	private Map<String, LogicalModel> string2Model;
	private Map<LogicalModel, String> model2String;
	private Map<LogicalModel, Color> modelColor;

	// FIXME
	private Map<String, Set<NodeInfo>> nodeID2Info;
	private Map<String, Color> nodeColor;

	private Map<String, Set<ComponentFeature>> string2ComponentFeature;

	public ProjectFeatures() {
		// model info
		this.string2Model = new HashMap<String, LogicalModel>();
		this.model2String = new HashMap<LogicalModel, String>();
		this.modelColor = new HashMap<LogicalModel, Color>();
		// node info
		this.nodeID2Info = new HashMap<String, Set<NodeInfo>>();
		this.nodeColor = new HashMap<String, Color>();
		// model 2 nodes
		this.string2ComponentFeature = new HashMap<String, Set<ComponentFeature>>();
	}

	public void addModelComponents(LogicalModel m) {
		for (NodeInfo node : m.getNodeOrder()) {
			String nodeID = node.getNodeID();
			if (!nodeID2Info.containsKey(nodeID)) {
				Set<NodeInfo> tmpSet = new HashSet<NodeInfo>();
				tmpSet.add(node);
				this.nodeID2Info.put(nodeID, tmpSet);
			}
			else{
				this.nodeID2Info.get(nodeID).add(node);
			}
			String OS_nodeID = "CC " + nodeID;
			if (OptionStore.getOption(OS_nodeID) != null) {
				this.nodeColor.put(nodeID, Color.decode((String) OptionStore.getOption(OS_nodeID)));
			} else {
				this.nodeColor.put(nodeID, ColorUtils.random());
			}
			ComponentFeature cf = new ComponentFeature(m, node);
			this.string2ComponentFeature.get(nodeID).add(cf);
 		}
	}

	// MODELCOMPONENTFEATURES

	public void addModel(String name, LogicalModel m) {
		this.string2Model.put(name, m);
		this.model2String.put(m, name);
		this.modelColor.put(m, ColorUtils.random());
		this.addModelComponents(m);
	}

	public void removeModel(String name) {
		LogicalModel m = this.getModel(name);
		this.modelColor.remove(m);
		this.model2String.remove(m);
		this.string2Model.remove(name);
		for (String nodeID : this.string2ComponentFeature.keySet()) {
			Set<ComponentFeature> sToRemove = new HashSet<ComponentFeature>();
			for (ComponentFeature cf : this.string2ComponentFeature.get(nodeID)) {
				if (cf.getModel().equals(m)) {
					sToRemove.add(cf);
				}
			}
			this.string2ComponentFeature.get(nodeID).removeAll(sToRemove);
			if (this.string2ComponentFeature.get(nodeID).isEmpty()) {
				this.string2ComponentFeature.remove(nodeID);
				this.nodeID2Info.remove(nodeID);
				this.nodeColor.remove(nodeID);
			}
		}
	}

	public boolean hasModel(LogicalModel m) {
		return this.model2String.containsKey(m);
	}

	public LogicalModel getModel(String name) {
		return this.string2Model.get(name);
	}

	public Set<String> getModelNames() {
		return Collections.unmodifiableSet(this.string2Model.keySet());
	}

	// ProjectModelFeatures.setColor
	public void setModelColor(String name, Color c) {
		this.modelColor.put(this.getModel(name), c);
	}

	// ProjectModelFeatures.getColor
	public Color getModelColor(String name) {
		return this.getModelColor(this.string2Model.get(name));
	}

	// ProjectModelFeatures.getColor
	public Color getModelColor(LogicalModel m) {
		return this.modelColor.get(m);
	}

	// ProjectModelFeatures.changeColor
	public void changeModelColor(String name, Color c) {
		this.modelColor.put(this.getModel(name), c);
	}

	// ProjectModelFeatures.getName
	public String getModelName(LogicalModel m) {
		return this.model2String.get(m);
	}

	// PROJECTCOMPONENTFEATURES

	// ProjectComponentFeatures.getNodeInfo
	public NodeInfo getNodeInfo(String nodeID, LogicalModel m) {
		for (ComponentFeature cf : this.string2ComponentFeature.get(nodeID)) {
			if (cf.getModel().equals(m)) {
				return cf.getNodeInfo();
			}
		}
		return null;
	}
	
	public Color getNodeColor(String nodeID) {
		return this.nodeColor.get(nodeID);
	}

	public Map<String, Color> getColorMap() {
		return this.nodeColor;
	}

	// TODO THIS NEEDS TO BE ADAPTED
	public Set<String> getComponents() {
		return Collections.unmodifiableSet(this.nodeColor.keySet());
		// TODO return unmodifiable lists everywhere!
	}

	public void setNodeColor(String nodeID, Color color) {
		if (!this.nodeColor.containsKey(nodeID) || !color.equals(this.nodeColor.get(nodeID))) {
			this.nodeColor.put(nodeID, color);
		}
	}
	
	public Set<LogicalModel> getModels(){
		return this.model2String.keySet();
	}
	
	public boolean hasNode(String NodeID, LogicalModel m) {
		List<NodeInfo> modelNodeInfos = m.getNodeOrder();
		for (NodeInfo node : modelNodeInfos){
			if (node.getNodeID() == NodeID) {
				return true;
			}
		}
		return false;
	}
	
	public LogicalModel getNodeModel(NodeInfo node) {
		String nodeID = node.getNodeID();
		for (LogicalModel m : this.getModels()){
			if (this.string2ComponentFeature.get(nodeID).containsKey(m)){
				return m;
			}
		}
		return null;
	}

	public Set<String> getModelComponents(LogicalModel m, boolean input) {
		Set<String> sComps = new HashSet<String>();
		for (NodeInfo node : m.getNodeOrder()) {
			if (!input && !node.isInput() || input && node.isInput()) {
				sComps.add(node.getNodeID());
			}
		}
		return sComps;
	}

	public Set<NodeInfo> getModelNodeInfos(LogicalModel m, boolean input) {
		Set<NodeInfo> sComps = new HashSet<NodeInfo>();
		for (NodeInfo node : m.getNodeOrder()) {
			if (!input && !node.isInput() || input && node.isInput()) {
				sComps.add(node);
			}
		}
		return sComps;
	}

	public Set<String> getModelsComponents(List<LogicalModel> lModels, boolean input) {
		Set<String> sComps = new HashSet<String>();
		if (!lModels.isEmpty()) {
			for (LogicalModel m : lModels) {
				sComps.addAll(this.getModelComponents(m, input));
			}
		}
		return sComps;
	}

}