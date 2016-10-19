package org.epilogtool.project;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;
import org.epilogtool.OptionStore;
import org.epilogtool.core.EmptyModel;
import org.epilogtool.gui.color.ColorUtils;

public class ProjectFeatures {

	private Map<String, LogicalModel> string2Model;
	private Map<LogicalModel, String> model2String;
	private Map<LogicalModel, Color> modelColor;

	// FIXME
	private Map<String, Set<NodeInfo>> nodeID2Info;
	private Map<String, Color> nodeColor;

	private Map<String, Set<ComponentPair>> string2ComponentFeature;

	public ProjectFeatures() {
		// model info
		this.string2Model = new HashMap<String, LogicalModel>();
		this.model2String = new HashMap<LogicalModel, String>();
		this.modelColor = new HashMap<LogicalModel, Color>();
		// node info
		this.nodeID2Info = new HashMap<String, Set<NodeInfo>>();
		this.nodeColor = new HashMap<String, Color>();
		// model 2 nodes
		this.string2ComponentFeature = new HashMap<String, Set<ComponentPair>>();
	}
	
	public void addModel(String name, LogicalModel m) {
		this.string2Model.put(name, m);
		this.model2String.put(m, name);
		this.modelColor.put(m, ColorUtils.random());
		this.addModelComponents(m);
	}
	
	public void addModelComponents(LogicalModel m) {
		for (NodeInfo node : m.getNodeOrder()) {
			String nodeID = node.getNodeID();
			if (!nodeID2Info.containsKey(nodeID)) {
				Set<NodeInfo> tmpSet = new HashSet<NodeInfo>();
				tmpSet.add(node);
				this.nodeID2Info.put(nodeID, tmpSet);
			} else {
				this.nodeID2Info.get(nodeID).add(node);
			}
			String OS_nodeID = "CC " + nodeID;
			if (OptionStore.getOption(OS_nodeID) != null) {
				this.nodeColor
						.put(nodeID, Color.decode((String) OptionStore
								.getOption(OS_nodeID)));
			} else {
				this.nodeColor.put(nodeID, ColorUtils.random());
			}
			ComponentPair cp = new ComponentPair(m, node);
			if (!this.string2ComponentFeature.containsKey(nodeID)) {
				Set<ComponentPair> tmpSet = new HashSet<ComponentPair>();
				tmpSet.add(cp);
				this.string2ComponentFeature.put(nodeID, tmpSet);
			} else {
				this.string2ComponentFeature.get(nodeID).add(cp);
			}
		}
	}
	
	public boolean hasModel(LogicalModel m) {
		return EmptyModel.getInstance().isEmptyModel(m)
				|| this.model2String.containsKey(m);
	}
	
	public LogicalModel getModel(String name) {
		if (EmptyModel.getInstance().isEmptyModel(name)) {
			return EmptyModel.getInstance().getModel();
		}
		return this.string2Model.get(name);
	}
	
	public Set<LogicalModel> getModels() {
		return this.model2String.keySet();
	}
	
	public String getModelName(LogicalModel m) {
		return this.model2String.get(m);
	}
	
	public List<String> getGUIModelNames() {
		List<String> ltmp = new ArrayList<String>();
		ltmp.add(EmptyModel.getInstance().getName());
		ltmp.addAll(this.string2Model.keySet());
		return ltmp;
	}
	
	public Set<String> getModelNames() {
		return Collections.unmodifiableSet(this.string2Model.keySet());
	}
	
	public Set<String> getModelNodeIDs(LogicalModel m, boolean input) {
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

	public Set<NodeInfo> getModelsNodeInfos(List<LogicalModel> lModels,
			boolean input) {
		Set<NodeInfo> sComps = new HashSet<NodeInfo>();
		if (!lModels.isEmpty()) {
			for (LogicalModel m : lModels) {
				sComps.addAll(this.getModelNodeInfos(m, input));
			}
		}
		return sComps;
	}
	
	public Color getModelColor(String name) {
		if (EmptyModel.getInstance().isEmptyModel(name)) {
			return EmptyModel.getInstance().getColor();
		}
		return this.getModelColor(this.string2Model.get(name));
	}
	
	public Color getModelColor(LogicalModel m) {
		return this.modelColor.get(m);
	}
	
	public NodeInfo getNodeInfo(String nodeID, LogicalModel m) {
		Set<ComponentPair> sCP = this.string2ComponentFeature.get(nodeID);
		if (sCP != null) {
			for (ComponentPair cp : sCP) {
				if (cp.getModel().equals(m)) {
					return cp.getNodeInfo();
				}
			}
		}
		return null;
	}
	
	public boolean hasNode(String nodeID, LogicalModel m) {
		if (this.getNodeInfo(nodeID, m) != null) {
			return true;
		}
		return false;
	}
	
	public Color getNodeColor(String nodeID) {
		return this.nodeColor.get(nodeID);
	}
	
	public Set<String> getNodeIDs() {
		return Collections.unmodifiableSet(this.nodeColor.keySet());
	}
	
	public Map<String, Color> getNodeID2ColorMap() {
		return this.nodeColor;
	}

	public void removeModel(String name) {
		LogicalModel m = this.getModel(name);
		this.modelColor.remove(m);
		this.model2String.remove(m);
		this.string2Model.remove(name);
		Set<String> emptyNodeIDs = new HashSet<String>();
		for (String nodeID : this.string2ComponentFeature.keySet()) {
			Set<ComponentPair> sToRemove = new HashSet<ComponentPair>();
			for (ComponentPair cf : this.string2ComponentFeature.get(nodeID)) {
				if (cf.getModel().equals(m)) {
					sToRemove.add(cf);
				}
			}
			this.string2ComponentFeature.get(nodeID).removeAll(sToRemove);
			if (this.string2ComponentFeature.get(nodeID).isEmpty()) {
				emptyNodeIDs.add(nodeID);
			}
		}
		for (String emptyNodeID : emptyNodeIDs){
			this.string2ComponentFeature.remove(emptyNodeID);
			this.nodeID2Info.remove(emptyNodeID);
			this.nodeColor.remove(emptyNodeID);
		}
	}

	public void setModelColor(String name, Color c) {
		this.modelColor.put(this.getModel(name), c);
	}
	
	public void setNodeColor(String nodeID, Color color) {
		if (!this.nodeColor.containsKey(nodeID)
				|| !color.equals(this.nodeColor.get(nodeID))) {
			this.nodeColor.put(nodeID, color);
		}
	}

	public void renameModel(String model, String newModel) {
		this.model2String.put(this.string2Model.get(model), newModel);
		this.string2Model.put(newModel, string2Model.remove(model));
		
	}
}