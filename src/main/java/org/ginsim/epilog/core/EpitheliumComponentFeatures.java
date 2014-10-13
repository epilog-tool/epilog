package org.ginsim.epilog.core;

import java.awt.Color;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;
import org.ginsim.epilog.gui.color.ColorUtils;

public class EpitheliumComponentFeatures {
	private Map<String, NodeInfo> nodeID2Info;
	private Map<String, Color> nodeColor;

	public EpitheliumComponentFeatures() {
		this.nodeID2Info = new HashMap<String, NodeInfo>();
		this.nodeColor = new HashMap<String, Color>();
	}

	public EpitheliumComponentFeatures clone() {
		EpitheliumComponentFeatures ecf = new EpitheliumComponentFeatures();
		for (String nodeID : this.nodeColor.keySet()) {
			ecf.setNodeColor(nodeID, this.nodeColor.get(nodeID));
		}
		ecf.cloneNode2Info(this.nodeID2Info);
		return ecf;
	}

	private void cloneNode2Info(Map<String, NodeInfo> old) {
		for (String nodeID : old.keySet()) {
			this.nodeID2Info.put(nodeID, old.get(nodeID));
		}
	}

	public void removeComponent(String nodeID) {
		this.nodeID2Info.remove(nodeID);
		this.nodeColor.remove(nodeID);
	}

	public void addModel(LogicalModel m) {
		for (NodeInfo node : m.getNodeOrder()) {
			if (!nodeID2Info.containsKey(node.getNodeID())) {
				this.nodeID2Info.put(node.getNodeID(), node);
				this.nodeColor.put(node.getNodeID(), ColorUtils.random());
			}
		}
	}

	public NodeInfo getNodeInfo(String nodeID) {
		return this.nodeID2Info.get(nodeID);
	}

	public Color getNodeColor(String nodeID) {
		return this.nodeColor.get(nodeID);
	}

	public Set<String> getComponents() {
		return Collections.unmodifiableSet(this.nodeColor.keySet());
		// TODO return unmodifiable lists everywhere!
	}

	public void setNodeColor(String nodeID, Color color) {
		this.nodeColor.put(nodeID, color);
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

	public Set<String> getModelsComponents(List<LogicalModel> lModels,
			boolean input) {
		Set<String> sComps = new HashSet<String>();
		if (!lModels.isEmpty()) {
			for (LogicalModel m : lModels) {
				sComps.addAll(this.getModelComponents(m, input));
			}
		}
		return sComps;
	}
	
	public boolean equals(Object o) {
		EpitheliumComponentFeatures ecfOut = (EpitheliumComponentFeatures) o;
		Set<String> sAllNodes = new HashSet<String>();
		// NodeInfo
		sAllNodes.addAll(this.nodeID2Info.keySet());
		sAllNodes.addAll(ecfOut.nodeID2Info.keySet());
		for (String nodeID : sAllNodes) {
			if (!this.nodeID2Info.containsKey(nodeID) || !this.nodeID2Info.containsKey(nodeID))
				return false;
			if (!this.nodeID2Info.get(nodeID).equals(ecfOut.nodeID2Info.get(nodeID)))
				return false;
		}
		sAllNodes.clear();
		// Colors
		sAllNodes.addAll(this.nodeColor.keySet());
		sAllNodes.addAll(ecfOut.nodeColor.keySet());
		for (String nodeID : sAllNodes) {
			if (!this.nodeColor.containsKey(nodeID) || !this.nodeColor.containsKey(nodeID))
				return false;
			if (!this.nodeColor.get(nodeID).equals(ecfOut.nodeColor.get(nodeID)))
				return false;
		}
		return true;
	}
}