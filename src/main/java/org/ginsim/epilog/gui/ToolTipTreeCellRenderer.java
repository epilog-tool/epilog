package org.ginsim.epilog.gui;

import java.awt.Component;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

import org.colomoto.logicalmodel.LogicalModel;
import org.ginsim.epilog.core.ComponentIntegrationFunctions;
import org.ginsim.epilog.core.Epithelium;
import org.ginsim.epilog.core.EpitheliumIntegrationFunctions;
import org.ginsim.epilog.core.ModelPriorityClasses;
import org.ginsim.epilog.core.topology.Topology;
import org.ginsim.epilog.gui.tab.EpiTabIntegrationFunctions;
import org.ginsim.epilog.project.Project;

class ToolTipTreeCellRenderer implements TreeCellRenderer {
	DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
	private Project project;

	public ToolTipTreeCellRenderer(Project project) {
		this.project = project;
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
				if (node.getParent() != null) {
					if (!node.isLeaf()) {
						// Epithelium
						tipKey = this.getTooltipEpithelium((Epithelium) node
								.getUserObject());
					} else {
						DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) node
								.getParent();
						Epithelium epi = (Epithelium) parentNode
								.getUserObject();
						String sLeaf = (String) node.getUserObject();
						if (sLeaf.equals("Model Grid")) {
							tipKey = this.getTooltipModelGrid(epi);
						} else if (sLeaf.equals("Integration Components")) {
							tipKey = this.getTooltipIntegration(epi);
						} else if (sLeaf.equals("Initial Condition")) {
							tipKey = this.getTooltipInitCond(epi);
						} else if (sLeaf.equals("Perturbations")) {
							tipKey = this.getTooltipPerturbations(epi);
						} else if (sLeaf.equals("Updating Scheme")) {
							tipKey = this.getTooltipUpdateScheme(epi);
						} else if (sLeaf.equals("Simulation")) {
							tipKey = this.getTooltipSimulation(epi);
						}
					}
				}
			} else {
				tipKey = tree.convertValueToText(value, selected, expanded,
						leaf, row, hasFocus);
			}
			renderer.setToolTipText(tipKey);
		}
		return renderer;
	}

	private String getTooltipEpithelium(Epithelium epi) {
		String tipKey = "<html><b>Epithelium</b><br/>";
		tipKey += "Name: " + epi.getName() + "<br/>";
		tipKey += "Grid: " + epi.getEpitheliumGrid().getX() + " (width) x "
				+ epi.getEpitheliumGrid().getY() + " (height)<br/>";
		Topology top = epi.getEpitheliumGrid().getTopology();
		tipKey += "Rollover: " + top.getRollOver() + "<br/>";
		tipKey += "Topology: " + top.getDescription() + "<br/>";
		tipKey += "</html>";
		return tipKey;
	}

	private String getTooltipModelGrid(Epithelium epi) {
		String tipKey = "<html><b>Models in use</b>";
		for (LogicalModel m : epi.getEpitheliumGrid().getModelSet()) {
			tipKey += "<br/>- " + this.project.getModelFeatures().getName(m);
		}
		tipKey += "</html>";
		return tipKey;
	}

	private String getTooltipIntegration(Epithelium epi) {
		String tipKey = "<html>";
		EpitheliumIntegrationFunctions epiIF = epi.getIntegrationFunctions();
		for (String nodeID : epiIF.getComponents()) {
			tipKey += "<b>" + nodeID + "</b><br/>";
			ComponentIntegrationFunctions cif = epiIF
					.getComponentIntegrationFunctions(nodeID);
			List<String> lFunctions = cif.getFunctions();
			for (int i = 0; i < lFunctions.size(); i++) {
				tipKey += (i + 1) + ": " + lFunctions.get(i) + "<br/>";
			}
		}
		if (epiIF.getComponents().isEmpty()) {
			tipKey += "<i>None</i>";
		}
		tipKey += "</html>";
		return tipKey;
	}

	private String getTooltipInitCond(Epithelium epi) {
		String tipKey = "<html><b>Ideas for information to put here ???</b>";
// TODO
		tipKey += "</html>";
		return tipKey;
	}

	private String getTooltipPerturbations(Epithelium epi) {
		String tipKey = "<html><b>per model</b>";
// TODO
		tipKey += "</html>";
		return tipKey;
	}

	private String getTooltipUpdateScheme(Epithelium epi) {
		String tipKey = "<html><b>Intra-cellular</b><br/>";
		for (LogicalModel m : epi.getEpitheliumGrid().getModelSet()) {
			ModelPriorityClasses mpc = epi.getPriorityClasses(m);
			tipKey += this.project.getModelFeatures().getName(m)
					+ "</b><br/>";
			tipKey += "  - "+mpc.size() + " classes<br/>";
		}
		tipKey += "<b>Inter-cellular</b><br/>";
		tipKey += "alpha = " + epi.getUpdateSchemeInter().getAlpha();
		tipKey += "</html>";
		return tipKey;
	}

	private String getTooltipSimulation(Epithelium epi) {
		String tipKey = "<html><b>Ideas for information to put here ???</b>";
// TODO
		tipKey += "</html>";
		return tipKey;
	}
}