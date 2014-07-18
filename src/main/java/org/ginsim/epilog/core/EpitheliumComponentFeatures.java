package org.ginsim.epilog.core;

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
import org.ginsim.epilog.gui.color.ColorUtils;

public class EpitheliumComponentFeatures {
	private Map<String, NodeInfo> nodeID2Info;
	private Map<String, Color> nodeColor;

	public EpitheliumComponentFeatures() {
		this.nodeID2Info = new HashMap<String, NodeInfo>();
		this.nodeColor = new HashMap<String, Color>();
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

	public Color getCellColor(LogicalModel cellModel, byte[] cellState,
			List<String> components) {
		List<Color> cellColors = new ArrayList<Color>();
		for (String comp : components) {
			NodeInfo node = this.nodeID2Info.get(comp);
			byte value = cellState[cellModel.getNodeOrder().indexOf(node)];
			cellColors.add(ColorUtils.getColorAtValue(this.nodeColor.get(comp),
					node.getMax(), value));
		}
		return ColorUtils.combine(cellColors);
	}

	public Set<String> getModelsComponents(List<LogicalModel> lModels,
			boolean input) {
		Set<String> sComps = new HashSet<String>();
		if (!lModels.isEmpty()) {
			System.out.println(lModels);
			for (LogicalModel m : lModels) {

				for (NodeInfo node : m.getNodeOrder()) {
					if (!input && !node.isInput() || input && node.isInput()) {
						sComps.add(node.getNodeID());
					}
				}
			}
		}
		return sComps;
	}
}
