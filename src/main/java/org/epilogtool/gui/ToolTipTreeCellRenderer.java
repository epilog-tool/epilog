package org.epilogtool.gui;

import java.awt.Component;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

import org.colomoto.biolqm.LogicalModel;
import org.colomoto.biolqm.NodeInfo;
import org.colomoto.biolqm.modifier.perturbation.AbstractPerturbation;
import org.epilogtool.core.ComponentIntegrationFunctions;
import org.epilogtool.core.Epithelium;
import org.epilogtool.core.EpitheliumIntegrationFunctions;
import org.epilogtool.core.ModelPriorityClasses;
import org.epilogtool.core.topology.Topology;
import org.epilogtool.gui.tab.EpiTab;
import org.epilogtool.project.ComponentPair;
import org.epilogtool.project.Project;

class ToolTipTreeCellRenderer implements TreeCellRenderer {
	DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();

	public ToolTipTreeCellRenderer() {
	}

	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
			boolean leaf, int row, boolean hasFocus) {
		renderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		if (value != null) {
			String tipKey = "";
			if (value instanceof DefaultMutableTreeNode) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
				if (node.getParent() != null) {
					if (!node.isLeaf()) {
						// Epithelium
						tipKey = this.getTooltipEpithelium((Epithelium) node.getUserObject());
					} else {
						DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) node.getParent();
						Epithelium epi = (Epithelium) parentNode.getUserObject();
						String sLeaf = (String) node.getUserObject();
						if (sLeaf.equals(EpiTab.TAB_MODELGRID)) {
							tipKey = this.getTooltipModelGrid(epi);
						} else if (sLeaf.equals(EpiTab.TAB_INTEGRATION)) {
							tipKey = this.getTooltipInputDefinition(epi);
						} else if (sLeaf.equals(EpiTab.TAB_INITCONDITIONS)) {
							tipKey = this.getTooltipInitCond(epi);
						} else if (sLeaf.equals(EpiTab.TAB_PERTURBATIONS)) {
							tipKey = this.getTooltipPerturbations(epi);
						} else if (sLeaf.equals(EpiTab.TAB_PRIORITIES)) {
							tipKey = this.getTooltipModelUpdateScheme(epi);
						} else if (sLeaf.equals(EpiTab.TAB_EPIUPDATING)) {
							tipKey = this.getTooltipEpithelialUpdateScheme(epi);
						} else if (sLeaf.equals(EpiTab.TOOL_SIMULATION)) {
							tipKey = this.getTooltipSimulation(epi);
						} else if (sLeaf.equals(EpiTab.TOOL_MONTECARLO)) {
							tipKey = this.getTooltipSimulation(epi);
						}
					}
				}
			} else {
				tipKey = tree.convertValueToText(value, selected, expanded, leaf, row, hasFocus);
			}
			renderer.setToolTipText(tipKey);
		}
		return renderer;
	}

	private String getTooltipEpithelium(Epithelium epi) {
		String tipKey = "<html><b>Epithelium</b><br/>";
		tipKey += "Name: " + epi.getName() + "<br/>";
		tipKey += "Grid: " + epi.getEpitheliumGrid().getX() + " (width) x " + epi.getEpitheliumGrid().getY()
				+ " (height)<br/>";
		Topology top = epi.getEpitheliumGrid().getTopology();
		tipKey += "Borders: " + top.getRollOver() + "<br/>";
		tipKey += "Topology: " + top.getDescription() + "<br/>";
		tipKey += "</html>";
		return tipKey;
	}

	private String getTooltipModelGrid(Epithelium epi) {
		String tipKey = "<html><b>Model(s) in use</b>";
		for (LogicalModel m : epi.getEpitheliumGrid().getModelSet()) {
			tipKey += "<br/>- " + Project.getInstance().getProjectFeatures().getModelName(m);
		}
		tipKey += "</html>";
		return tipKey;
	}

	private String getTooltipInputDefinition(Epithelium epi) {
		String tipKey = "<html>";
		EpitheliumIntegrationFunctions epiIF = epi.getIntegrationFunctions();
		for (NodeInfo node : epiIF.getNodes()) {
			tipKey += "<b>" + node.getNodeID() +"</b><br/>";
			ComponentIntegrationFunctions cif = epiIF.getComponentIntegrationFunctions(node);
			List<String> lFunctions = cif.getFunctions();
			for (int i = 0; i < lFunctions.size(); i++) {
				tipKey += (i + 1) + ": " + lFunctions.get(i) + "<br/>";
			}
		}
		if (epiIF.getNodes().isEmpty()) {
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
		Map<LogicalModel, Set<AbstractPerturbation>> map = epi.getEpitheliumGrid().getAppliedPerturb();
		for (LogicalModel m : map.keySet()) {
			if (map.get(m).isEmpty())
				continue;
			tipKey += "<b>" + Project.getInstance().getProjectFeatures().getModelName(m) + "</b>";
			for (AbstractPerturbation ap : map.get(m)) {
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

	private String getTooltipModelUpdateScheme(Epithelium epi) {
		String tipKey = "<html>";
		for (LogicalModel m : epi.getEpitheliumGrid().getModelSet()) {
			ModelPriorityClasses mpc = epi.getPriorityClasses(m);
			tipKey += "- " + Project.getInstance().getProjectFeatures().getModelName(m) + "</b><br/>";
			tipKey += "&nbsp;&nbsp;. " + mpc.size() + " class(es)<br/>";
		}
		tipKey += "</html>";
		return tipKey;
	}

	private String getTooltipEpithelialUpdateScheme(Epithelium epi) {
		String tipKey = "<html>";
		tipKey += "<b>Parameters</b><br/>";
		tipKey += "- Alpha = " + epi.getUpdateSchemeInter().getAlpha() + "<br/>";
		tipKey += "- Cells to Update: " + epi.getUpdateSchemeInter().getUpdateCells() + "<br/>";
		tipKey += "- Number generator seed: " + epi.getUpdateSchemeInter().getRandomSeedType() + "<br/>";
		tipKey += "</html>";
		return tipKey;
	}

	private String getTooltipSimulation(Epithelium epi) {
		String tipKey = "<html>To get information, please open the Tab";
		// TODO
		tipKey += "</html>";
		return tipKey;
	}
}