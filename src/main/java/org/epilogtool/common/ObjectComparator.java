package org.epilogtool.common;

import java.util.Comparator;

import org.colomoto.logicalmodel.NodeInfo;

public class ObjectComparator {
	public static Comparator<NodeInfo> NODE_INFO = new Comparator<NodeInfo>() {
		public int compare(NodeInfo s1, NodeInfo s2) {
			return s1.getNodeID().compareToIgnoreCase(s2.getNodeID());
		}
	};
	public static Comparator<String> STRING = new Comparator<String>() {
		public int compare(String s1, String s2) {
			return s1.compareToIgnoreCase(s2);
		}
	};
}
