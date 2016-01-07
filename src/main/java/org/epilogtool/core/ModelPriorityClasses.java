package org.epilogtool.core;

import java.util.ArrayList;
import java.util.List;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;
import org.colomoto.logicalmodel.tool.simulation.updater.PriorityClasses;

public class ModelPriorityClasses {

	public static final String INC = "[+]";
	public static final String DEC = "[-]";

	private LogicalModel model;
	private PriorityClasses priorities;

	private ModelPriorityClasses(LogicalModel m, PriorityClasses pcs) {
		this.model = m;
		this.priorities = pcs;
	}

	public ModelPriorityClasses(LogicalModel m) {
		this.model = m;
		this.priorities = new PriorityClasses();
		List<NodeInfo> tmpNodeList = new ArrayList<NodeInfo>();
		for (NodeInfo node: m.getNodeOrder()){
			if (!node.isInput()){
				tmpNodeList.add(node);
			}
		}
		int[] tmp = new int[tmpNodeList.size() * 2];
		for (int n = 0; n < tmpNodeList.size(); n++) {
			tmp[n * 2] = n;
			tmp[n * 2 + 1] = 0;
		}
		this.priorities.add(tmp, true);
	}

	public LogicalModel getModel() {
		return this.model;
	}
	
	public PriorityClasses getPriorities() {
		return this.priorities;
	}

	public ModelPriorityClasses clone() {
		return new ModelPriorityClasses(this.model, this.priorities.clone());
	}

	public List<String> getClassVars(int idxPC) {
		List<String> lVars = new ArrayList<String>();
		int[] iaPCidx = this.priorities.getClass(idxPC);
		for (int i = 0; i < this.priorities.getClass(idxPC).length; i += 2) {
			NodeInfo node = model.getNodeOrder().get(iaPCidx[i]);
			if (node.isInput()) continue;
			String var = node.getNodeID();
			if (iaPCidx[i + 1] == 1) {
				var += INC;
			} else if (iaPCidx[i + 1] == -1) {
				var += DEC;
			}
			lVars.add(var);
		}
		return lVars;
	}

	public void setPriorities(String pcs) {
		this.priorities = new PriorityClasses();
		// Format: varA,varB[+],varC:varB[-],varD
		String[] sClasses = pcs.split(":");
		for (String sClass : sClasses) {
			String[] sVars = sClass.split(",");
			int[] newTmp = new int[sVars.length * 2];
			for (int i = 0; i < sVars.length; i++) {
				String var = sVars[i];
				int split = 0;
				if (sVars[i].endsWith(DEC)) {
					split = -1;
					var = sVars[i].substring(0, var.length() - DEC.length());
				} else if (sVars[i].endsWith(INC)) {
					split = 1;
					var = sVars[i].substring(0, var.length() - INC.length());
				}
				for (int idx = 0; idx < this.model.getNodeOrder().size(); idx++) {
					NodeInfo node = this.model.getNodeOrder().get(idx);
					if (node.isInput()) continue;
					if (node.getNodeID().equals(var)) {
						newTmp[i * 2] = idx;
						newTmp[i * 2 + 1] = split;
						break;
					}
				}
			}
			this.priorities.add(newTmp, true);
		}
	}

	public void decPriorities(int idxPC, List<String> vars) {
		if ((idxPC + 1) > this.priorities.size())
			return;
		int split;
		String var;
		for (String varMm : vars) {
			if (varMm.endsWith(INC)) {
				split = 1;
				var = varMm.substring(0, varMm.length() - INC.length());
			} else if (varMm.endsWith(DEC)) {
				split = -1;
				var = varMm.substring(0, varMm.length() - DEC.length());
			} else {
				split = 0;
				var = varMm;
			}
			for (int idx = 0; idx < this.model.getNodeOrder().size(); idx++) {
				NodeInfo node = this.model.getNodeOrder().get(idx);
				if (node.getNodeID().equals(var)) {
					this.priorities.decPriority(idxPC, idx, split);
					break;
				}
			}
		}
	}

	public void incPriorities(int idxPC, List<String> vars) {
		if (idxPC <= 0)
			return;
		int split;
		String var;
		for (String varMm : vars) {
			if (varMm.endsWith(INC)) {
				split = 1;
				var = varMm.substring(0, varMm.length() - INC.length());
			} else if (varMm.endsWith(DEC)) {
				split = -1;
				var = varMm.substring(0, varMm.length() - DEC.length());
			} else {
				split = 0;
				var = varMm;
			}
			for (int idx = 0; idx < this.model.getNodeOrder().size(); idx++) {
				NodeInfo node = this.model.getNodeOrder().get(idx);
				if (node.getNodeID().equals(var)) {
					this.priorities.incPriority(idxPC, idx, split);
					break;
				}
			}
		}
	}

	public void singlePriorityClass() {
		// the new collapsed class will be Synchronous
		this.priorities.collapse(true);
	}

	/**
	 * It's the number of classes
	 * 
	 * @return
	 */
	public int size() {
		return this.priorities.size();
	}

	// /**
	// * It's the number of variables (including splitted transitions) in a
	// given
	// * class
	// *
	// * @param index
	// * @return
	// */
	// public int sizeAtIndex(int index) {
	// return (this.priorities.get(index).variables.length / 2);
	// }

	// public List<String> getVarsAtIndex(int index) {
	// return this.priorityList.get(index);
	// }

	public void split(int idxPC, String var) {
		for (int idx = 0; idx < this.model.getNodeOrder().size(); idx++) {
			NodeInfo node = this.model.getNodeOrder().get(idx);
			if (node.getNodeID().equals(var)) {
				this.priorities.split(idxPC, idx);
				return;
			}
		}
	}

	public void unsplit(int idxPC, String varMm) {
		if (!varMm.endsWith(INC) && !varMm.endsWith(DEC))
			return;
		String var = varMm.substring(0, varMm.length() - INC.length());
		int split = varMm.endsWith(INC) ? 1 : -1;
		for (int idx = 0; idx < this.model.getNodeOrder().size(); idx++) {
			NodeInfo node = this.model.getNodeOrder().get(idx);
			if (node.getNodeID().equals(var)) {
				this.priorities.unsplit(idxPC, idx, split);
			}
		}
	}

	public boolean equals(Object a) {
		ModelPriorityClasses outMPC = (ModelPriorityClasses) a;
		return (this.model.equals(outMPC.model) && this.priorities
				.equals(outMPC.priorities));
	}
}
