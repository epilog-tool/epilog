package org.epilogtool.mdd;

import java.util.List;

import org.colomoto.biolqm.LogicalModel;
import org.colomoto.biolqm.NodeInfo;
import org.colomoto.mddlib.MDDManager;
import org.colomoto.mddlib.operators.MDDBaseOperators;
import org.epilogtool.function.ComponentExpression;
import org.epilogtool.function.FunctionExpression;
import org.epilogtool.function.FunctionNOT;
import org.epilogtool.function.FunctionOperationAND;
import org.epilogtool.function.FunctionOperationOR;
import org.epilogtool.function.FunctionTRUE;

public class ModelMDD {
	private MDDManager ddmanager;
	private List<NodeInfo> lNodes;

	public ModelMDD(LogicalModel m) {
		this.ddmanager = m.getMDDManager().getManager(m.getComponents());
		this.lNodes = m.getComponents();
	}

	public int getMDD(FunctionExpression expr, String sp) { // TODO indent with increasing spaces
		if (expr instanceof FunctionTRUE) {
			System.out.println(sp +"getMDD: TRUE");
			return 1;
		} else if (expr instanceof FunctionOperationAND) {
			System.out.println(sp +"getMDD: AND");
			FunctionOperationAND exprAND = (FunctionOperationAND) expr;
			return MDDBaseOperators.AND.combine(this.ddmanager, this.getMDD(exprAND.getExprLeft(), sp+"  "),
					this.getMDD(exprAND.getExprRight(), sp+"  "));
		} else if (expr instanceof FunctionOperationOR) {
			System.out.println(sp +"getMDD: OR");
			FunctionOperationOR exprOR = (FunctionOperationOR) expr;
			return MDDBaseOperators.OR.combine(this.ddmanager, this.getMDD(exprOR.getExprLeft(), sp+"  "),
					this.getMDD(exprOR.getExprRight(), sp+"  "));
		} else if (expr instanceof FunctionNOT) {
			System.out.println(sp +"getMDD: NOT");
			FunctionNOT exprNOT = (FunctionNOT) expr;
			return this.ddmanager.not(this.getMDD(exprNOT, sp+"  "));
		} else if (expr instanceof ComponentExpression) {
			System.out.println(sp +"getMDD: ComponentExpression");
			ComponentExpression exprComp = (ComponentExpression)expr;
			for (int i = 0; i < this.lNodes.size(); i++) {
				NodeInfo node = this.lNodes.get(i);
				if (node.getNodeID().equals(exprComp.getComponentName())) {
					int[] children = new int[node.getMax()+1];
					for (int j = exprComp.getMinThreshold(); j < children.length; j++) {
						children[j] = 1;
					}
					return this.ddmanager.getAllVariables()[i].getNode(children);
				}
			}
		}
		System.out.println(sp +"getMDD: TRUE");
		return 0; // Default FALSE
	}
	
	public int and(int mdd1, int mdd2) {
		return MDDBaseOperators.AND.combine(this.ddmanager, mdd1, mdd2);
	}
}
