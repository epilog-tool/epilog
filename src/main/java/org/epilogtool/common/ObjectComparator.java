package org.epilogtool.common;

import java.util.Comparator;

import org.colomoto.biolqm.NodeInfo;
import org.colomoto.biolqm.modifier.perturbation.AbstractPerturbation;
import org.epilogtool.project.ComponentPair;

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
	public static Comparator<ComponentPair> COMPONENTPAIR = new Comparator<ComponentPair>() {
		public int compare(ComponentPair cp1, ComponentPair cp2) {
			return cp1.getNodeInfo().getNodeID().compareToIgnoreCase(cp2.getNodeInfo().getNodeID());
		}
	};
	public static Comparator<AbstractPerturbation> ABSTRACT_PERTURB = new Comparator<AbstractPerturbation>() {
		public int compare(AbstractPerturbation ap1, AbstractPerturbation ap2) {
			return ap1.toString().compareTo(ap2.toString());
		}
	};
}
