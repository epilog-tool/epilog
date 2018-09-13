package org.epilogtool.project;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.colomoto.biolqm.LogicalModel;
import org.colomoto.biolqm.NodeInfo;
import org.epilogtool.OptionStore;
import org.epilogtool.common.Txt;

import org.epilogtool.gui.color.ColorUtils;

public class ProjectFeatures {

	private Map<String, LogicalModel> string2Model;
	private Map<LogicalModel, String> model2String;
	private Map<LogicalModel, Color> modelColor;

	private Map<String, Color> abstCell2Color;
	
	private List<String> replaceMessages;

	private Map<String, NodeInfo> nodeID2Info;
	private Map<String, Color> nodeColor;

	private Map<String, Set<ComponentPair>> string2ComponentFeature;

	public ProjectFeatures() {
		// model info
		this.string2Model = new HashMap<String, LogicalModel>();
		this.model2String = new HashMap<LogicalModel, String>();
		this.modelColor = new HashMap<LogicalModel, Color>();
		// node info
		this.nodeID2Info = new HashMap<String, NodeInfo>();
		
		this.abstCell2Color = new HashMap<String, Color>();
		
		this.nodeColor = new LinkedHashMap<String, Color>();
		// model 2 nodes
//		this.string2ComponentFeature = new HashMap<String, Set<ComponentPair>>();
		this.replaceMessages = new ArrayList<String>();
	}

	public void addModel(String name, LogicalModel m) {
		this.string2Model.put(name, m);
		this.model2String.put(m, name);
		this.modelColor.put(m, ColorUtils.random());
		this.addModelComponents(m);
	}

	public byte getNodeMax(String nodeID) {
		NodeInfo node = this.nodeID2Info.get(nodeID);
		if (node == null)
			return -1;
		return node.getMax();
	}

	public List<String> getReplaceMessages() {
		return this.replaceMessages;
	}

	public void initializeReplaceMessages() {
		this.replaceMessages = new ArrayList<String>();
	}

	public void addReplaceMessages(String msg) {
		this.replaceMessages.add(msg);
	}

	public void addModelComponents(LogicalModel m) {
		for (NodeInfo node : m.getComponents()) {
			String nodeID = node.getNodeID();
			this.nodeID2Info.put(nodeID, node);
			String OS_nodeID = "CC " + nodeID;
			if (OptionStore.getOption(OS_nodeID) != null) {
				this.nodeColor.put(nodeID, Color.decode((String) OptionStore.getOption(OS_nodeID)));
			} else {
				this.nodeColor.put(nodeID, ColorUtils.random());
			}

		}
	}


	public LogicalModel getModel(String name) {
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
		ltmp.add(Txt.get("s_EMPTY_CELL"));
		ltmp.add(Txt.get("s_INVALID_CELL"));
		ltmp.add(Txt.get("s_DEAD_CELL"));
		ltmp.addAll(this.string2Model.keySet());
		return ltmp;
	}

	public Set<String> getModelNames() {
		return Collections.unmodifiableSet(this.string2Model.keySet());
	}

	public Set<String> getModelNodeIDs(LogicalModel m, boolean input) {
		Set<String> sComps = new LinkedHashSet<String>();//LinkedHashSet: so that the set returns components with the same order
		for (NodeInfo node : m.getComponents()) {
			if (!input && !node.isInput() || input && node.isInput()) {
				sComps.add(node.getNodeID());
			}
		}
		return sComps;
	}

	public Set<NodeInfo> getModelNodeInfos(LogicalModel m, boolean input) {
		Set<NodeInfo> sComps = new LinkedHashSet<NodeInfo>();  //LinkedHashSet: so that the set returns components with the same order
		for (NodeInfo node : m.getComponents()) {
			if (!input && !node.isInput() || input && node.isInput()) {
				sComps.add(node);
			}
		}
		return sComps;
	}

	public Set<NodeInfo> getModelsNodeInfos(List<LogicalModel> lModels, boolean input) {
		Set<NodeInfo> sComps = new LinkedHashSet<NodeInfo>(); //LinkedHashSet: so that the set returns components with the same order
		if (!lModels.isEmpty()) {
			for (LogicalModel m : lModels) {
				sComps.addAll(this.getModelNodeInfos(m, input));
			}
		}
		
		return sComps;
	}
	
	public Set<String> getModelsNodeIDs(List<LogicalModel> lModels, boolean input) {
		Set<String> sComps = new LinkedHashSet<String>(); //LinkedHashSet: so that the set returns components with the same order
		if (!lModels.isEmpty()) {
			for (LogicalModel m : lModels) {
				sComps.addAll(this.getModelNodeIDs(m, input));
			}
		}
		return sComps;
	}

	public Color getModelColor(String name) {
		return this.getModelColor(this.string2Model.get(name));
	}

	public Color getModelColor(LogicalModel m) {
		return this.modelColor.get(m);
	}

//	public NodeInfo getNodeInfo(String nodeID, LogicalModel m) {
//		Set<ComponentPair> sCP = this.string2ComponentFeature.get(nodeID);
//		if (sCP != null) {
//			for (ComponentPair cp : sCP) {
//				if (cp.getModel().equals(m)) {
//					return cp.getNodeInfo();
//				}
//			}
//		}
//		return null;
//	}
	
	public NodeInfo getNodeInfo(String nodeID) {
		return nodeID2Info.get(nodeID);
	}


	public boolean hasNode(String nodeID, LogicalModel m) {
		if (this.getNodeInfo(nodeID) != null) {
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
		for (String emptyNodeID : emptyNodeIDs) {
			this.string2ComponentFeature.remove(emptyNodeID);
			this.nodeID2Info.remove(emptyNodeID);
			this.nodeColor.remove(emptyNodeID);
		}
	}

	public void setModelColor(LogicalModel m, Color c) {
		this.modelColor.put(m, c);
	}
	
	public void setModelColor(String modelName, Color c) {
//		System.out.println(modelName);
		if (modelName.equals(Txt.get("s_DEAD_CELL")) || modelName.equals(Txt.get("s_EMPTY_CELL")) || modelName.equals(Txt.get("s_INVALID_CELL"))){
			setAbstCellColor(modelName, c);
		}
		else setModelColor(this.string2Model.get(modelName), c);
	}


	public void setNodeColor(String nodeID, Color color) {
		if (!this.nodeColor.containsKey(nodeID) || !color.equals(this.nodeColor.get(nodeID))) {
			this.nodeColor.put(nodeID, color);
		}
	}

	/**
	 * This function renames the model.
	 * 
	 * @param model
	 * @param newModel
	 */
	public void renameModel(String model, String newModel) {
		this.model2String.put(this.string2Model.get(model), newModel);
		this.string2Model.put(newModel, string2Model.remove(model));
	}

	public boolean hasModel(LogicalModel model) {
		return model2String.containsKey(model);
	}

	public Color getAbstCellColor(String name) {
		return abstCell2Color.get(name);
	}
	
	public Color setAbstCellColor(String name, Color c) {
		return abstCell2Color.put(name,c);
	}
}