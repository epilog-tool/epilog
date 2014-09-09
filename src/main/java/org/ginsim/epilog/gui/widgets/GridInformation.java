package org.ginsim.epilog.gui.widgets;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.perturbation.AbstractPerturbation;
import org.ginsim.epilog.project.ProjectModelFeatures;
import org.ginsim.epilog.core.EpitheliumComponentFeatures;
import org.ginsim.epilog.core.EpitheliumGrid;
import org.ginsim.epilog.core.EpitheliumIntegrationFunctions;

public class GridInformation extends JPanel {
	private static final long serialVersionUID = -1449994132920814592L;

	private EpitheliumComponentFeatures compFeatures;
	private EpitheliumIntegrationFunctions integrFunctions;
	private ProjectModelFeatures modelFeatures;

	public GridInformation(EpitheliumComponentFeatures compFeatures,
			EpitheliumIntegrationFunctions eif,
			ProjectModelFeatures modelFeatures) {
		this.compFeatures = compFeatures;
		this.integrFunctions = eif;
		this.modelFeatures = modelFeatures;
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createTitledBorder("Grid information"));
		this.updateValues(0, 0, null);
	}

	private void minimalSpace(GridBagConstraints gbc, int y) {
		gbc.gridy = y;
		gbc.gridx = 0;
		gbc.gridwidth = 2;
		JLabel jlTmp = new JLabel("                              ");
		this.add(jlTmp, gbc);
	}

	public void updateValues(int posX, int posY, EpitheliumGrid grid) {
		// TODO: LogicalModel m, AbstractPerturbation ap, byte[] state) {
		this.removeAll();
		JLabel jlTmp;
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		int y = 0;

		// Separation
		this.minimalSpace(gbc, y);
		if (grid != null) {
			List<String> lAllNodeIDs = new ArrayList<String>(
					this.compFeatures.getComponents());
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
			this.add(jlTmp, gbc);

			// Separation
			this.minimalSpace(gbc, ++y);

			// Cell Model
			gbc.gridy = ++y;
			gbc.gridx = 0;
			gbc.gridwidth = 2;
			gbc.anchor = GridBagConstraints.WEST;
			jlTmp = new JLabel("Model:");
			this.add(jlTmp, gbc);
			gbc.gridy = ++y;
			gbc.gridx = 0;
			gbc.gridwidth = 2;
			jlTmp = new JLabel("  " + modelFeatures.getName(m));
			this.add(jlTmp, gbc);

			// Separation
			this.minimalSpace(gbc, ++y);

			// Perturbations
			gbc.gridy = ++y;
			gbc.gridx = 0;
			gbc.gridwidth = 2;
			gbc.anchor = GridBagConstraints.WEST;
			jlTmp = new JLabel("Perturbation:");
			this.add(jlTmp, gbc);
			gbc.gridy = ++y;
			gbc.gridx = 0;
			gbc.gridwidth = 2;
			AbstractPerturbation ap = grid.getPerturbation(posX, posY);
			jlTmp = new JLabel("  " + ((ap == null) ? "none" : ap.toString()));
			this.add(jlTmp, gbc);

			// Separation
			this.minimalSpace(gbc, ++y);

			// Proper values
			gbc.gridy = ++y;
			gbc.gridx = 0;
			gbc.gridwidth = 1;
			jlTmp = new JLabel("Proper:");
			this.add(jlTmp, gbc);
			gbc.gridwidth = 1;
			for (String nodeID : lAllNodeIDs) {
				if (this.compFeatures.getNodeInfo(nodeID).isInput())
					continue;
				gbc.gridy = ++y;
				gbc.gridx = 0;
				gbc.anchor = GridBagConstraints.WEST;
				jlTmp = new JLabel(nodeID + " ");
				this.add(jlTmp, gbc);
				int index = grid.getNodeIndex(posX, posY, nodeID);
				if (index < 0)
					continue;
				gbc.gridx = 1;
				jlTmp = new JLabel(": " + grid.getCellState(posX, posY)[index]);
				this.add(jlTmp, gbc);
			}

			// Separation
			this.minimalSpace(gbc, ++y);

			// Input values
			gbc.gridy = ++y;
			gbc.gridx = 0;
			gbc.gridwidth = 1;
			jlTmp = new JLabel("Input:");
			this.add(jlTmp, gbc);
			gbc.gridwidth = 1;
			for (String nodeID : lAllNodeIDs) {
				if (!this.compFeatures.getNodeInfo(nodeID).isInput()
						|| this.integrFunctions.containsKey(nodeID))
					continue;
				gbc.gridy = ++y;
				gbc.gridx = 0;
				gbc.anchor = GridBagConstraints.WEST;
				jlTmp = new JLabel(nodeID + " ");
				this.add(jlTmp, gbc);
				int index = grid.getNodeIndex(posX, posY, nodeID);
				if (index < 0)
					continue;
				gbc.gridx = 1;
				jlTmp = new JLabel(": " + grid.getCellState(posX, posY)[index]);
				this.add(jlTmp, gbc);
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
		this.add(jlTmp, gbc);

		// Repaint
		// TODO: this.setSize(this.getPreferredSize());
		this.revalidate();
		this.repaint();
	}
}
