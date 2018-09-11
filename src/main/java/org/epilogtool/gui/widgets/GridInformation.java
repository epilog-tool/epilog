package org.epilogtool.gui.widgets;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.colomoto.biolqm.LogicalModel;
import org.colomoto.biolqm.NodeInfo;
import org.colomoto.biolqm.modifier.perturbation.AbstractPerturbation;
import org.epilogtool.OptionStore;
import org.epilogtool.common.ObjectComparator;
import org.epilogtool.common.Txt;
import org.epilogtool.core.EpitheliumGrid;
import org.epilogtool.core.EpitheliumIntegrationFunctions;
import org.epilogtool.core.cell.AbstractCell;
import org.epilogtool.gui.EpiLogGUIFactory;
import org.epilogtool.gui.dialog.EnumOrderNodes;
import org.epilogtool.project.Project;
import org.epilogtool.core.cell.LivingCell;

public class GridInformation extends JPanel {
	private static final long serialVersionUID = -1449994132920814592L;

	private static int WIDTH = 145;

	private EpitheliumIntegrationFunctions integrFunctions;
	private JPanel jCellPanel;

	public GridInformation(EpitheliumIntegrationFunctions eif) {
		this.integrFunctions = eif;

		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createTitledBorder(Txt.get("s_GRIDINFO_TITLE")));

		// JPanel
		this.jCellPanel = new JPanel(new GridBagLayout());

		// JScroll
		JScrollPane jscroll = new JScrollPane(this.jCellPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jscroll.setBorder(BorderFactory.createEmptyBorder());
		jscroll.setMinimumSize(new Dimension(GridInformation.WIDTH, 0));
		jscroll.setPreferredSize(new Dimension(GridInformation.WIDTH, 0));
		this.add(jscroll);

		this.updateValues(0, 0, null, null);
	}

	private void minimalSpace(GridBagConstraints gbc, int y) {
		gbc.gridy = y;
		gbc.gridx = 0;
		gbc.gridwidth = 2;
		JLabel jlTmp = new JLabel("                                ");
		this.jCellPanel.add(jlTmp, gbc);
	}

	private void constraints(GridBagConstraints gbc, int x, int y, int width) {
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
	}

	public void updateValues(int posX, int posY, EpitheliumGrid grid, AbstractCell[][] cellGrid) {
		this.jCellPanel.removeAll();
		JLabel jlTmp;
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.EAST;
		int y = 0;

		// Separation
		this.minimalSpace(gbc, ++y);

		// Separation
		if (grid != null || cellGrid != null) {
			if (cellGrid[posX][posY].isLivingCell()) {
			LogicalModel m = (grid != null) ? grid.getModel(posX, posY) : ((LivingCell) cellGrid[posX][posY]).getModel();
			}
			// Cell Position
			jlTmp = new JLabel("Grid position:");
			this.constraints(gbc, 0, ++y, 2);
			this.jCellPanel.add(jlTmp, gbc);
			this.constraints(gbc, 0, ++y, 2);
			jlTmp = new JLabel("  " + (posX + 1) + "," + (posY + 1));
			this.jCellPanel.add(jlTmp, gbc);

			// Separation
			this.minimalSpace(gbc, ++y);

			// Empty cell specification
			if (cellGrid[posX][posY].isEmptyCell()) {
				this.constraints(gbc, 0, ++y, 2);
				jlTmp = new JLabel(Txt.get("s_EMPTY_CELL"));
				this.jCellPanel.add(jlTmp, gbc);

			} else {

				// Cell Model specification
				this.constraints(gbc, 0, ++y, 2);
				jlTmp = new JLabel("Model:");
				this.jCellPanel.add(jlTmp, gbc);
				this.constraints(gbc, 0, ++y, 2);
				if (cellGrid[posX][posY].isLivingCell()) {
					LogicalModel m = (grid != null) ? grid.getModel(posX, posY) : ((LivingCell) cellGrid[posX][posY]).getModel();
					jlTmp = new JLabel("  " + Project.getInstance().getProjectFeatures().getModelName(m));
					jlTmp.setToolTipText(Project.getInstance().getProjectFeatures().getModelName(m));
					this.jCellPanel.add(jlTmp, gbc);
					}


				if (grid != null) {
					// Separation
					this.minimalSpace(gbc, ++y);

					// Perturbations
					jlTmp = new JLabel("Perturbation:");
					this.constraints(gbc, 0, ++y, 2);
					this.jCellPanel.add(jlTmp, gbc);
					AbstractPerturbation ap = grid.getPerturbation(posX, posY);
					if (ap == null) {
						jlTmp = EpiLogGUIFactory.getJLabelItalic("  None");
					} else {
						jlTmp = new JLabel("  " + ap.toString());
						jlTmp.setToolTipText(ap.toString());
					}
					this.constraints(gbc, 0, ++y, 2);
					this.jCellPanel.add(jlTmp, gbc);

					// Separation
					this.minimalSpace(gbc, ++y);

					// Components
					jlTmp = new JLabel("--- Components ---");
					this.constraints(gbc, 0, ++y, 2);
					this.jCellPanel.add(jlTmp, gbc);

					// Separation
					this.minimalSpace(gbc, ++y);

					List<String> lAllNodeIDs = new ArrayList<String>(
							Project.getInstance().getProjectFeatures().getNodeIDs());
					
					String orderPref = (String) OptionStore.getOption("PrefsAlphaOrderNodes");
					
					if (orderPref != null && orderPref.equals(EnumOrderNodes.ALPHA.toString())) {
						Collections.sort(lAllNodeIDs, ObjectComparator.STRING);
					}
					

					// Proper values
					jlTmp = new JLabel("Internal:");
					this.constraints(gbc, 0, ++y, 1);
					this.jCellPanel.add(jlTmp, gbc);
					gbc.gridwidth = 1;
					boolean isEmpty = true;
					for (String nodeID : lAllNodeIDs) {
						if (cellGrid[posX][posY].isLivingCell()) {
							LogicalModel m = (grid != null) ? grid.getModel(posX, posY) : ((LivingCell) cellGrid[posX][posY]).getModel();
							
						if (!Project.getInstance().getProjectFeatures().hasNode(nodeID, m)
								|| Project.getInstance().getProjectFeatures().getNodeInfo(nodeID).isInput()) {
							continue;
						}}
					
						jlTmp = new JLabel("  " + nodeID + " ");
						jlTmp.setToolTipText(nodeID);
						this.constraints(gbc, 0, ++y, 1);
						this.jCellPanel.add(jlTmp, gbc);
						isEmpty = false;
						int index = grid.getNodeIndex(posX, posY, nodeID);
						if (index < 0)
							continue;
						this.constraints(gbc, 1, y, 1);
						jlTmp = new JLabel(": " + grid.getCellState(posX, posY)[index]);
						jlTmp.setToolTipText(nodeID);
						this.jCellPanel.add(jlTmp, gbc);
					}
					this.checkEmpty(gbc, ++y, isEmpty);

					// Separation
					this.minimalSpace(gbc, ++y);

					// Environmental Input values
					jlTmp = new JLabel("Positional inputs:");
					this.constraints(gbc, 0, ++y, 2);
					this.jCellPanel.add(jlTmp, gbc);
					gbc.gridwidth = 1;
					isEmpty = true;
					for (String nodeID : lAllNodeIDs) {
						NodeInfo node = Project.getInstance().getProjectFeatures().getNodeInfo(nodeID);
						
						if (node == null || !node.isInput() || this.integrFunctions.containsNode(node)) {
							continue;
						}
						jlTmp = new JLabel("  " + nodeID + " ");
						jlTmp.setToolTipText(nodeID);
						this.constraints(gbc, 0, ++y, 1);
						this.jCellPanel.add(jlTmp, gbc);
						isEmpty = false;
						int index = grid.getNodeIndex(posX, posY, nodeID);
						if (index < 0)
							continue;
						jlTmp = new JLabel(": " + grid.getCellState(posX, posY)[index]);
						jlTmp.setToolTipText(nodeID);
						this.constraints(gbc, 1, y, 1);
						this.jCellPanel.add(jlTmp, gbc);
					}
					this.checkEmpty(gbc, ++y, isEmpty);

					// Separation
					this.minimalSpace(gbc, ++y);

					// Integration Input values
					jlTmp = new JLabel("Integration inputs:");
					this.constraints(gbc, 0, ++y, 2);
					this.jCellPanel.add(jlTmp, gbc);
					gbc.gridwidth = 1;
					isEmpty = true;
					for (String nodeID : lAllNodeIDs) {
						NodeInfo node = Project.getInstance().getProjectFeatures().getNodeInfo(nodeID);
						if (node == null || !this.integrFunctions.containsNode(node)) {
							continue;
						}
						jlTmp = new JLabel("  " + nodeID + " ");
						jlTmp.setToolTipText(nodeID);
						this.constraints(gbc, 0, ++y, 1);
						this.jCellPanel.add(jlTmp, gbc);
						isEmpty = false;
						int index = grid.getNodeIndex(posX, posY, nodeID);
						if (index < 0)
							continue;
						jlTmp = new JLabel(": " + grid.getCellState(posX, posY)[index]);
						jlTmp.setToolTipText(nodeID);
						this.constraints(gbc, 1, y, 1);
						this.jCellPanel.add(jlTmp, gbc);
					}
					this.checkEmpty(gbc, ++y, isEmpty);
				}
			}

			gbc.anchor = GridBagConstraints.PAGE_END;
			// Separation
			this.minimalSpace(gbc, ++y);
		}

		gbc.weighty = 1.0;
		gbc.anchor = GridBagConstraints.PAGE_END;
		gbc.gridy = ++y;
		gbc.gridx = 0;
		gbc.gridwidth = 2;
		this.minimalSpace(gbc, y);

		// Repaint
		this.jCellPanel.revalidate();
		this.jCellPanel.repaint();
	}

	private void checkEmpty(GridBagConstraints gbc, int y, boolean isEmpty) {
		if (isEmpty) {
			JLabel jlTmp = EpiLogGUIFactory.getJLabelItalic("  None");
			this.constraints(gbc, 0, y, 2);
			this.jCellPanel.add(jlTmp, gbc);
		}
	}
}
