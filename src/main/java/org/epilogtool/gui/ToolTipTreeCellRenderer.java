package org.epilogtool.gui;

import java.awt.Component;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;
import org.colomoto.logicalmodel.perturbation.AbstractPerturbation;
import org.epilogtool.core.ComponentIntegrationFunctions;
import org.epilogtool.core.Epithelium;
import org.epilogtool.core.EpitheliumIntegrationFunctions;
import org.epilogtool.core.ModelPerturbations;
import org.epilogtool.core.ModelPriorityClasses;
import org.epilogtool.core.topology.Topology;
import org.epilogtool.project.ProjectFeatures;

class ToolTipTreeCellRenderer implements TreeCellRenderer {
	DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
	private ProjectFeatures projectFeatures;

	public ToolTipTreeCellRenderer(ProjectFeatures projectFeatures) {
		this.projectFeatures = projectFeatures;
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
		String tipKey = "<html><b>Model(s) in use</b>";
		for (LogicalModel m : epi.getEpitheliumGrid().getModelSet()) {
			tipKey += "<br/>- " + this.projectFeatures.getModelName(m);
		}
		tipKey += "</html>";
		return tipKey;
	}

	private String getTooltipIntegration(Epithelium epi) {
		String tipKey = "<html>";
		EpitheliumIntegrationFunctions epiIF = epi.getIntegrationFunctions();
		for (NodeInfo node : epiIF.getComponents()) {
			tipKey += "<b>" + node.getNodeID() + "</b><br/>";
			ComponentIntegrationFunctions cif = epiIF
					.getComponentIntegrationFunctions(node);
			List<String> lFunctions = cif.getFunctions();
			for (int i = 0; i < lFunctions.size(); i++) {
				tipKey += (i + 1) + ": " + lFunctions.get(i) + "<br/>";
			}
		}
		if (epiIF.getComponents().isEmpty()) {
			tipKey += "<i>Empty</i>";
		}
		tipKey += "</html>";
		return tipKey;
	}

	private String getTooltipInitCond(Epithelium epi) {
		String tipKey = "<html>To get information, please open the Tab</html>";
		return tipKey;
	}

	private String getTooltipPerturbations(Epithelium epi) {
		String tipKey = "<html>";
		boolean isEmpty = true;
		for (LogicalModel m : epi.getEpitheliumGrid().getModelSet()) {
			ModelPerturbations mp = epi.getModelPerturbations(m);
			if (mp == null) {
				continue;
			}
			List<AbstractPerturbation> apList = mp.getAllPerturbations();
			if (apList.size() == 0) {
				continue;
			}
			if (!isEmpty) {
				tipKey += "<br/>";
			}
			tipKey += "<b>" + this.projectFeatures.getModelName(m) + "</b>";
			for (AbstractPerturbation ap : apList) {
				tipKey += "<br/>&nbsp;. " + ap;
			}
			isEmpty = false;
		}
		if (isEmpty) {
			tipKey += "<i>Empty</i>";
		}
		tipKey += "</html>";
		return tipKey;
	}

	private String getTooltipUpdateScheme(Epithelium epi) {
		String tipKey = "<html><b>Intra-cellular</b><br/>";
		for (LogicalModel m : epi.getEpitheliumGrid().getModelSet()) {
			ModelPriorityClasses mpc = epi.getPriorityClasses(m);
			tipKey += "- " + this.projectFeatures.getModelName(m) + "</b><br/>";
			tipKey += "&nbsp;&nbsp;. " + mpc.size() + " class(es)<br/>";
		}
		tipKey += "<b>Inter-cellular</b><br/>";
		tipKey += "- alpha = " + epi.getUpdateSchemeInter().getAlpha();
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