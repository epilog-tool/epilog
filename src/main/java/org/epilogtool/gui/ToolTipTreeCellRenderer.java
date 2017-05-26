package org.epilogtool.gui;

import java.awt.Component;
import java.util.List;
import java.util.Map;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.perturbation.AbstractPerturbation;
import org.epilogtool.core.ComponentIntegrationFunctions;
import org.epilogtool.core.Epithelium;
import org.epilogtool.core.EpitheliumIntegrationFunctions;
import org.epilogtool.core.ModelPerturbations;
import org.epilogtool.core.ModelPriorityClasses;
import org.epilogtool.core.cellDynamics.CellularEvent;
import org.epilogtool.core.cellDynamics.ModelEventManager;
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
						} else if (sLeaf.equals(EpiTab.TAB_CELLDIVISION)) {
							tipKey = this.getToolTipCellDivision(epi);
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
		tipKey += "Rollover: " + top.getRollOver() + "<br/>";
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
		for (ComponentPair cp : epiIF.getComponentPair()) {
			tipKey += "<b>" + cp.getNodeInfo().getNodeID() + " - "
					+ Project.getInstance().getProjectFeatures().getModelName(cp.getModel()) + "</b><br/>";
			ComponentIntegrationFunctions cif = epiIF.getComponentIntegrationFunctions(cp);
			List<String> lFunctions = cif.getFunctions();
			for (int i = 0; i < lFunctions.size(); i++) {
				tipKey += (i + 1) + ": " + lFunctions.get(i) + "<br/>";
			}
		}
		if (epiIF.getComponentPair().isEmpty()) {
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
			tipKey += "<b>" + Project.getInstance().getProjectFeatures().getModelName(m) + "</b>";
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
		Map<ComponentPair, Float> cpSigmas = epi.getUpdateSchemeInter().getCPSigmas();
		if (cpSigmas.size() == 0) {
			tipKey += "- Sigma = <i>Empty</i>";
		} else {
			tipKey += "- Sigma <br/>";
			for (ComponentPair cp : cpSigmas.keySet()) {
				tipKey += "&nbsp;&nbsp;. " + Project.getInstance().getProjectFeatures().getModelName(cp.getModel())
						+ " : " + cp.getNodeInfo().getNodeID() + " - " + cpSigmas.get(cp) + "<br/>";
			}
		}
		tipKey += "</html>";
		return tipKey;
	}

	private String getToolTipCellDivision(Epithelium epi) {
		String tipKey = "<html>";
		ModelEventManager events = epi.getModelEventManager();
		for (LogicalModel m : epi.getEpitheliumGrid().getModelSet()) {
			tipKey += "<b>" + Project.getInstance().getProjectFeatures().getModelName(m) + "</b><br/>";
			if (!events.containsModel(m)) {
				// System.out.println("Error tooltiptreecellrenderer");
				tipKey += "- " + Project.getInstance().getProjectFeatures().getModelName(m) + "   - Empty <br/>";
			} else if (events.getModelEvents(m).isEmpty()) {
				tipKey += "- " + Project.getInstance().getProjectFeatures().getModelName(m) + "   - Empty <br/>";
			} else {
				tipKey += "   - " + events.getModelEventExpression(m, CellularEvent.PROLIFERATION).getExpression()
						+ "<br/>";
			}
		}
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