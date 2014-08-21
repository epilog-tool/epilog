package org.ginsim.epilog.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;

public class ModelPriorityClasses {
	private LogicalModel model;
	// This must be a String because of splitted variable names
	private List<List<String>> priorityList;

	private ModelPriorityClasses(LogicalModel m, List<List<String>> pl) {
		this.model = m;
		this.priorityList = pl;
	}

	public ModelPriorityClasses(LogicalModel m) {
		this.model = m;
		List<String> vars = new ArrayList<String>();
		for (NodeInfo node : m.getNodeOrder()) {
			if (node.isInput())
				continue;
			vars.add(node.getNodeID());
		}
		this.priorityList = new ArrayList<List<String>>();
		Collections.sort(vars, new Comparator<String>() {
			public int compare(String s1, String s2) {
				return s1.compareToIgnoreCase(s2);
			}
		});
		this.priorityList.add(vars);
	}

	public LogicalModel getModel() {
		return this.model;
	}

	public ModelPriorityClasses clone() {
		List<List<String>> newPList = new ArrayList<List<String>>();
		for (List<String> pc : this.priorityList) {
			List<String> tmp = new ArrayList<String>();
			for (String s : pc) {
				tmp.add(s);
			}
			newPList.add(tmp);
		}
		return new ModelPriorityClasses(this.model, newPList);
	}

	public void setPriorities(String pcs) {
		// Format: varA,varB:varC,varD:varE
		// FIXME: parsing should be on the parser!!
		List<List<String>> newPLList = new ArrayList<List<String>>();
		String[] saTmp = pcs.split(":");
		for (int i = 0; i < saTmp.length; i++) {
			List<String> pcList = new ArrayList<String>();
			for (String comp : saTmp[i].split(",")) {
				pcList.add(comp);
			}
			newPLList.add(pcList);
		}
		this.priorityList = newPLList;
	}

	public List<List<String>> getPriorityList() {
		return Collections.unmodifiableList(this.priorityList);
	}

	public void decPriorities(int index, List<String> vars) {
		if (vars.size() < this.priorityList.get(index).size()) {
			this.priorityList.get(index).removeAll(vars);
			if (this.priorityList.size() == (index + 1)) {
				this.priorityList.add(new ArrayList<String>());
			}
			this.priorityList.get(index + 1).addAll(vars);
		}
	}

	public void incPriorities(int index, List<String> vars) {
		if (index > 0) {
			this.priorityList.get(index).removeAll(vars);
			this.priorityList.get(index - 1).addAll(vars);
			// TODO
//			Collections.sort(this.priorityList.get(index - 1), new Comparator<String>() {
//				public int compare(String s1, String s2) {
//					return s1.compareToIgnoreCase(s2);
//				}
//			});
			if (this.priorityList.get(index).size() == 0) {
				this.priorityList.remove(index);
			}
		}
	}

	public void singlePriorityClass() {
		List<String> firstClass = this.priorityList.get(0);
		for (int i = 1; i < this.priorityList.size(); i++) {
			for (String var : this.priorityList.get(i)) {
				firstClass.add(var);
			}
		}
		this.priorityList = new ArrayList<List<String>>();
		Collections.sort(firstClass, new Comparator<String>() {
			public int compare(String s1, String s2) {
				return s1.compareToIgnoreCase(s2);
			}
		});
		this.priorityList.add(firstClass);
	}

	public int size() {
		return this.priorityList.size();
	}

	public int sizeAtIndex(int index) {
		return this.priorityList.get(index).size();
	}

	public List<String> getVarsAtIndex(int index) {
		return this.priorityList.get(index);
	}

	public void split(int index, String var) {
		for (NodeInfo node : this.model.getNodeOrder()) {
			if (node.getNodeID().equals(var)) {
				this.priorityList.get(index).remove(var);
				this.priorityList.get(index).add(var + "[+]");
				this.priorityList.get(index).add(var + "[-]");
				return;
			}
		}
	}

	public void unsplit(int index, String varMm) {
		for (NodeInfo node : this.model.getNodeOrder()) {
			if (node.getNodeID().equals(varMm)) {
				return;
			}
		}
		String var = varMm.substring(0, -3);
		this.priorityList.get(index).remove(varMm);
		this.priorityList.get(index).add(var);
		var = var + (varMm.substring(-3).equals("[+]") ? "[-]" : "[+]");
		for (int i = 0; i < this.priorityList.size(); i++) {
			if (this.priorityList.get(i).contains(var)) {
				this.priorityList.get(i).remove(var);
				if (this.priorityList.get(i).isEmpty()) {
					this.priorityList.remove(i);
				}
				return;
			}
		}
	}
}
