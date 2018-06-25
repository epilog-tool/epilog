package org.epilogtool.gui.widgets;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.colomoto.biolqm.LogicalModel;
import org.colomoto.biolqm.modifier.perturbation.AbstractPerturbation;
import org.epilogtool.common.ObjectComparator;
import org.epilogtool.core.EmptyModel;
import org.epilogtool.core.EpitheliumGrid;
import org.epilogtool.gui.EpiLogGUIFactory;
import org.epilogtool.project.Project;

public class SimulationInformation extends JPanel {
	private static final long serialVersionUID = -1449994132920814592L;

	private static int WIDTH = 145;

	private JPanel jCellPanel;

	public SimulationInformation() {

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createTitledBorder("Simulation information"));

		// JPanel
		this.jCellPanel = new JPanel(new GridBagLayout());

		// JScroll
		JScrollPane jscroll = new JScrollPane(this.jCellPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jscroll.setBorder(BorderFactory.createEmptyBorder());
		jscroll.setMinimumSize(new Dimension(SimulationInformation.WIDTH, 0));
		jscroll.setPreferredSize(new Dimension(SimulationInformation.WIDTH, 0));
		this.add(jscroll);

		this.updateValues(0, 0, null);
	}

	private void minimalSpace(GridBagConstraints gbc, int y) {
		gbc.gridy = y;
		gbc.gridx = 0;
		gbc.gridwidth = 2;
		JLabel jlTmp = new JLabel("                              ");
		this.jCellPanel.add(jlTmp, gbc);
	}

	private void constraints(GridBagConstraints gbc, int x, int y, int width) {
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
	}

	public void updateValues(int posX, int posY, EpitheliumGrid grid) {
		this.jCellPanel.removeAll();
		JLabel jlTmp;
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.EAST;
		int y = 0;

		// Separation
		if (grid != null) {

			List<String> lAllNodeIDs = new ArrayList<String>(Project.getInstance().getProjectFeatures().getNodeIDs());
			Collections.sort(lAllNodeIDs, ObjectComparator.STRING);
			LogicalModel m = grid.getModel(posX, posY);

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
			if (EmptyModel.getInstance().getModel().equals(grid.getModel(posX, posY))) {
				this.constraints(gbc, 0, ++y, 2);
				jlTmp = new JLabel(EmptyModel.getInstance().getName());
				this.jCellPanel.add(jlTmp, gbc);
			}

			else {

				// Cell Model specification
				this.constraints(gbc, 0, ++y, 2);
				jlTmp = new JLabel("Model:");
				this.jCellPanel.add(jlTmp, gbc);
				this.constraints(gbc, 0, ++y, 2);
				jlTmp = new JLabel("  " + Project.getInstance().getProjectFeatures().getModelName(m));
				this.jCellPanel.add(jlTmp, gbc);

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

				// Internal values
				jlTmp = new JLabel("Internal:");
				this.constraints(gbc, 0, ++y, 1);
				this.jCellPanel.add(jlTmp, gbc);
				gbc.gridwidth = 1;
				boolean isEmpty = true;
				for (String nodeID : lAllNodeIDs) {
					if (!Project.getInstance().getProjectFeatures().hasNode(nodeID, m)
							|| Project.getInstance().getProjectFeatures().getNodeInfo(nodeID).isInput()) {
						continue;
					}
					jlTmp = new JLabel("  " + nodeID + " ");
					this.constraints(gbc, 0, ++y, 1);
					this.jCellPanel.add(jlTmp, gbc);
					isEmpty = false;
					int index = grid.getNodeIndex(posX, posY, nodeID);
					if (index < 0)
						continue;
					this.constraints(gbc, 1, y, 1);
					jlTmp = new JLabel(": " + grid.getCellState(posX, posY)[index]);
					this.jCellPanel.add(jlTmp, gbc);
				}
				this.checkEmpty(gbc, ++y, isEmpty);

				// Separation
				this.minimalSpace(gbc, ++y);

				// Environmental Input values

				this.minimalSpace(gbc, ++y);

				// Integration Input values
				jlTmp = new JLabel("Integration:");
				this.constraints(gbc, 0, ++y, 1);
				this.jCellPanel.add(jlTmp, gbc);
				gbc.gridwidth = 1;
				isEmpty = true;
				for (String nodeID : lAllNodeIDs) {

					jlTmp = new JLabel("  " + nodeID + " ");
					this.constraints(gbc, 0, ++y, 1);
					this.jCellPanel.add(jlTmp, gbc);
					isEmpty = false;
					int index = grid.getNodeIndex(posX, posY, nodeID);
					if (index < 0)
						continue;
					jlTmp = new JLabel(": " + grid.getCellState(posX, posY)[index]);
					this.constraints(gbc, 1, y, 1);
					this.jCellPanel.add(jlTmp, gbc);
				}
				this.checkEmpty(gbc, ++y, isEmpty);
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
		// jlTmp = new JLabel(" ");
		// this.jCellPanel.add(jlTmp, gbc);

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
