package org.ginsim.epilog.gui;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

import org.ginsim.epilog.core.Epithelium;

class ToolTipTreeCellRenderer implements TreeCellRenderer {
	DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();

	public ToolTipTreeCellRenderer() {
	}

	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		renderer.getTreeCellRendererComponent(tree, value, selected, expanded,
				leaf, row, hasFocus);
		if (value != null) {
			String tipKey = "";
			if (value instanceof DefaultMutableTreeNode) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
				if (!node.isLeaf() && node.getParent() != null) {
					// Epithelium
					Epithelium epiNode = (Epithelium) node.getUserObject();
					tipKey = "<html><b>Epithelium</b><br/>";
					tipKey += "Name: " + epiNode.getName() + "<br/>";
					tipKey += "Grid: " + epiNode.getEpitheliumGrid().getX()
							+ "x" + epiNode.getEpitheliumGrid().getY()
							+ "<br/>";
					tipKey += "Rollover: "
							+ epiNode.getEpitheliumGrid().getTopology()
									.getRollOver() + "<br/>";
					tipKey += "</html>";
				}
			} else {
				tipKey = tree.convertValueToText(value, selected, expanded,
						leaf, row, hasFocus);
			}
			renderer.setToolTipText(tipKey);
		}
		return renderer;
	}
}