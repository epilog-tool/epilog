package org.epilogtool.gui.widgets;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.perturbation.AbstractPerturbation;
import org.epilogtool.core.EpitheliumGrid;
import org.epilogtool.core.EpitheliumIntegrationFunctions;
import org.epilogtool.project.ProjectFeatures;

public class GridInformation extends JPanel {
	private static final long serialVersionUID = -1449994132920814592L;

	private EpitheliumIntegrationFunctions integrFunctions;
	private ProjectFeatures projectFeatures;
	private JPanel jIntPanel;

	public GridInformation(EpitheliumIntegrationFunctions eif,
			ProjectFeatures projectFeatures) {
		this.integrFunctions = eif;
		this.projectFeatures = projectFeatures;

		this.setBorder(BorderFactory.createTitledBorder("Grid information"));
		this.setLayout(new BorderLayout());

		// JPanel
		this.jIntPanel = new JPanel(new GridBagLayout());

		// JScroll
		JScrollPane jscroll = new JScrollPane(this.jIntPanel,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jscroll.setBorder(BorderFactory.createEmptyBorder());
		jscroll.setMinimumSize(new Dimension(135, 0));
		jscroll.setPreferredSize(new Dimension(135, 0));
		this.add(jscroll, BorderLayout.CENTER);

		this.updateValues(0, 0, null);
	}

	private void minimalSpace(GridBagConstraints gbc, int y) {
		gbc.gridy = y;
		gbc.gridx = 0;
		gbc.gridwidth = 2;
		JLabel jlTmp = new JLabel("                              ");
		this.jIntPanel.add(jlTmp, gbc);
	}

	public void updateValues(int posX, int posY, EpitheliumGrid grid) {
		// TODO: LogicalModel m, AbstractPerturbation ap, byte[] state) {
		this.jIntPanel.removeAll();
		JLabel jlTmp;
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		int y = 0;

		// Separation
		this.minimalSpace(gbc, y);
		if (grid != null) {
			List<String> lAllNodeIDs = new ArrayList<String>(
					this.projectFeatures.getComponents());
			Collections.sort(lAllNodeIDs, new Comparator<String>() {
				public int compare(String s1, String s2) {
					return s1.compareToIgnoreCase(s2);
				}
			});
			LogicalModel m = grid.getModel(posX, posY);

			// Cell Position
			gbc.gridy = ++y;
			gbc.gridx = 0;
			gbc.gridwidth = 2;
			gbc.anchor = GridBagConstraints.WEST;
			jlTmp = new JLabel("Cell: " + (posX + 1) + "," + (posY + 1));
			this.jIntPanel.add(jlTmp, gbc);

			// Separation
			this.minimalSpace(gbc, ++y);

			// Cell Model
			gbc.gridy = ++y;
			gbc.gridx = 0;
			gbc.gridwidth = 2;
			gbc.anchor = GridBagConstraints.WEST;
			jlTmp = new JLabel("Model:");
			this.jIntPanel.add(jlTmp, gbc);
			gbc.gridy = ++y;
			gbc.gridx = 0;
			gbc.gridwidth = 2;
			jlTmp = new JLabel("  " + projectFeatures.getModelName(m));
			this.jIntPanel.add(jlTmp, gbc);

			// Separation
			this.minimalSpace(gbc, ++y);

			// Perturbations
			gbc.gridy = ++y;
			gbc.gridx = 0;
			gbc.gridwidth = 2;
			gbc.anchor = GridBagConstraints.WEST;
			jlTmp = new JLabel("Perturbation:");
			this.jIntPanel.add(jlTmp, gbc);
			gbc.gridy = ++y;
			gbc.gridx = 0;
			gbc.gridwidth = 2;
			AbstractPerturbation ap = grid.getPerturbation(posX, posY);
			jlTmp = new JLabel("  " + ((ap == null) ? "none" : ap.toString()));
			this.jIntPanel.add(jlTmp, gbc);

			// Separation
			this.minimalSpace(gbc, ++y);

			// Proper values
			gbc.gridy = ++y;
			gbc.gridx = 0;
			gbc.gridwidth = 1;
			jlTmp = new JLabel("Proper:");
			this.jIntPanel.add(jlTmp, gbc);
			gbc.gridwidth = 1;
			for (String nodeID : lAllNodeIDs) {
				System.out.println(nodeID);
				if (!this.projectFeatures.hasNode(nodeID, m) ||
					this.projectFeatures.getNodeInfo(nodeID, m).isInput()) {
						continue;
				}
				gbc.gridy = ++y;
				gbc.gridx = 0;
				gbc.anchor = GridBagConstraints.WEST;
				jlTmp = new JLabel(nodeID + " ");
				this.jIntPanel.add(jlTmp, gbc);
				int index = grid.getNodeIndex(posX, posY, nodeID);
				if (index < 0)
					continue;
				gbc.gridx = 1;
				jlTmp = new JLabel(": " + grid.getCellState(posX, posY)[index]);
				this.jIntPanel.add(jlTmp, gbc);
			}

			// Separation
			this.minimalSpace(gbc, ++y);
			this.minimalSpace(gbc, ++y);
			
			//Inputs
			gbc.gridy = ++y;
			gbc.gridx = 0;
			gbc.gridwidth = 1;
			jlTmp = new JLabel("Input:");
			this.jIntPanel.add(jlTmp, gbc);
			
			// Separation
			this.minimalSpace(gbc, ++y);
			
			// Environmental Input values
			gbc.gridy = ++y;
			gbc.gridx = 0;
			gbc.gridwidth = 1;
			jlTmp = new JLabel("Environmental:");
			this.jIntPanel.add(jlTmp, gbc);
			gbc.gridwidth = 1;
			for (String nodeID : lAllNodeIDs) {
				if (!this.projectFeatures.hasNode(nodeID, m) ||
						!this.projectFeatures.getNodeInfo(nodeID, m).isInput()
							|| this.integrFunctions.containsKey(this.projectFeatures.getNodeInfo(nodeID, m))) {
						continue;
				}
				gbc.gridy = ++y;
				gbc.gridx = 0;
				gbc.anchor = GridBagConstraints.WEST;
				jlTmp = new JLabel(nodeID + " ");
				this.jIntPanel.add(jlTmp, gbc);
				int index = grid.getNodeIndex(posX, posY, nodeID);
				if (index < 0)
					continue;
				gbc.gridx = 1;
				jlTmp = new JLabel(": " + grid.getCellState(posX, posY)[index]);
				this.jIntPanel.add(jlTmp, gbc);
			}
			// Separation
			this.minimalSpace(gbc, ++y);

			// Integration Input values
			gbc.gridy = ++y;
			gbc.gridx = 0;
			gbc.gridwidth = 1;
			jlTmp = new JLabel("Integration:");
			this.jIntPanel.add(jlTmp, gbc);
			gbc.gridwidth = 1;
			for (String nodeID : lAllNodeIDs) {
				if (!this.projectFeatures.hasNode(nodeID, m) || 
						!this.integrFunctions.containsKey(this.projectFeatures.getNodeInfo(nodeID, m))) {
						continue;
				}
				gbc.gridy = ++y;
				gbc.gridx = 0;
				gbc.anchor = GridBagConstraints.WEST;
				jlTmp = new JLabel(nodeID + " ");
				this.jIntPanel.add(jlTmp, gbc);
				int index = grid.getNodeIndex(posX, posY, nodeID);
				if (index < 0)
					continue;
				gbc.gridx = 1;
				jlTmp = new JLabel(": " + grid.getCellState(posX, posY)[index]);
				this.jIntPanel.add(jlTmp, gbc);
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
		jlTmp = new JLabel(" ");
		this.jIntPanel.add(jlTmp, gbc);

		// Repaint
		this.jIntPanel.revalidate();
	}
}
