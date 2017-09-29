package org.epilogtool.project;

import org.colomoto.biolqm.LogicalModel;
import org.colomoto.biolqm.NodeInfo;

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
	
	@Override
	public int hashCode() {
		return this.m.getNodeOrder().size() + 1000*this.m.getExtraComponents().size() + 100000*this.node.getMax()
		+ 1000000*this.node.getNodeID().length();
	}
	
	@Override
	public String toString() {
		return this.getNodeInfo().getNodeID();
	}
	
}
