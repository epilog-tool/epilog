package org.epilogtool.project;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;

public class ComponentPair {
	private LogicalModel m;
	private NodeInfo node;

	public ComponentPair(LogicalModel m, NodeInfo node) {
		this.m = m;
		this.node = node;
	}

	public LogicalModel getModel() {
		return this.m;
	}

	public NodeInfo getNodeInfo() {
		return this.node;
	}

	@Override
	public boolean equals(Object obj) {
		ComponentPair out = (ComponentPair) obj;
		return this.m.equals(out.m) && this.node.equals(out.node);
	}
}
