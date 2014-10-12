package org.ginsim.epilog.gui.tab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import javax.swing.tree.TreePath;

import org.colomoto.logicalmodel.LogicalModel;
import org.ginsim.epilog.project.ProjectModelFeatures;
import org.ginsim.epilog.core.Epithelium;
import org.ginsim.epilog.core.EpitheliumGrid;
import org.ginsim.epilog.gui.widgets.VisualGridModel;

public class EpiTabModelGrid extends EpiTabDefinitions {
	private static final long serialVersionUID = -5262665948855829161L;

	private VisualGridModel visualGridModel;
	private Map<JRadioButton, JButton> mapSBMLMiniPanels;
	private LogicalModel[][] modelGridClone;
	private Map<LogicalModel, Color> colorMapClone;
	private JPanel lCenter;

	public EpiTabModelGrid(Epithelium e, TreePath path,
			ProjectModelFeatures modelFeatures) {
		super(e, path, modelFeatures);
	}

	public void initialize() {
		this.center.setLayout(new BorderLayout());

		this.mapSBMLMiniPanels = new HashMap<JRadioButton, JButton>();
		EpitheliumGrid grid = this.epithelium.getEpitheliumGrid();
		this.modelGridClone = new LogicalModel[grid.getX()][grid.getY()];
		this.colorMapClone = new HashMap<LogicalModel, Color>();

		JPanel lTopButtons = new JPanel(new FlowLayout());
		JButton jbApplyAll = new JButton("Apply All");
		jbApplyAll.setMargin(new Insets(0, 0, 0, 0));
		jbApplyAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				visualGridModel.applyDataToAll();
			}
		});
		lTopButtons.add(jbApplyAll);
		JToggleButton jtbRectFill = new JToggleButton("Rectangle Fill", false);
		jtbRectFill.setMargin(new Insets(0, 0, 0, 0));
		jtbRectFill.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				JToggleButton jtb = (JToggleButton) e.getSource();
				visualGridModel.isRectangleFill(jtb.isSelected());
			}
		});
		lTopButtons.add(jtbRectFill);

		this.lCenter = new JPanel(new GridBagLayout());
		this.lCenter.setBorder(BorderFactory
				.createTitledBorder("Model selection"));

		JPanel left = new JPanel(new BorderLayout());
		JPanel lTop = new JPanel(new BorderLayout());
		lTop.add(lTopButtons, BorderLayout.NORTH);
		lTop.add(this.lCenter, BorderLayout.CENTER);
		left.add(lTop, BorderLayout.PAGE_START);
		this.center.add(left, BorderLayout.LINE_START);

		this.visualGridModel = new VisualGridModel(this.epithelium
				.getEpitheliumGrid().getX(), this.epithelium
				.getEpitheliumGrid().getY(), this.epithelium
				.getEpitheliumGrid().getTopology(), this.modelGridClone,
				this.colorMapClone, this.modelFeatures);
		this.center.add(this.visualGridModel, BorderLayout.CENTER);

		this.buttonReset();
		this.updateModelList();
		this.isInitialized = true;
	}

	private void updateModelList() {
		this.lCenter.removeAll();
		ButtonGroup group = new ButtonGroup();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(1, 5, 1, 0);
		int i = 0;
		for (String name : this.modelFeatures.getNames()) {
			gbc.gridy = i;
			i++;
			gbc.gridx = 0;
			gbc.anchor = GridBagConstraints.WEST;
			JRadioButton jrButton = new JRadioButton(name);
			jrButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JRadioButton jrb = (JRadioButton) e.getSource();
					visualGridModel.setSelModelName(jrb.getText());
				}
			});
			this.lCenter.add(jrButton, gbc);
			group.add(jrButton);

			gbc.gridx = 1;
			Color newColor = this.modelFeatures.getColor(name);
			this.colorMapClone.put(this.modelFeatures.getModel(name), newColor);
			JButton jbColor = new JButton();
			jbColor.setBackground(newColor);
			jbColor.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setNewColor((JButton) e.getSource());
				}
			});
			this.lCenter.add(jbColor, gbc);
			this.mapSBMLMiniPanels.put(jrButton, jbColor);
		}
		// Clean grid of models that were deleted before an accept
		String defaultModel = this.modelFeatures.getNames().iterator().next();
		for (int x = 0; x < this.modelGridClone.length; x++) {
			for (int y = 0; y < this.modelGridClone[0].length; y++) {
				if (!this.modelFeatures.hasModel(this.modelGridClone[x][y])) {
					this.modelGridClone[x][y] = this.modelFeatures
							.getModel(defaultModel);
				}
			}
		}
		visualGridModel.setSelModelName(null);
		this.revalidate();
		this.visualGridModel.paintComponent(this.visualGridModel.getGraphics());
	}

	private void setNewColor(JButton jb) {
		for (JRadioButton jrb : this.mapSBMLMiniPanels.keySet()) {
			if (this.mapSBMLMiniPanels.get(jrb).equals(jb)) {
				String name = jrb.getText();
				Color newColor = JColorChooser.showDialog(jb,
						"Color chooser - " + name, jb.getBackground());
				if (newColor != null) {
					jb.setBackground(newColor);
					this.colorMapClone.put(this.modelFeatures.getModel(name),
							newColor);
					this.visualGridModel.paintComponent(this.visualGridModel
							.getGraphics());
				}
				break;
			}
		}
	}

	@Override
	protected void buttonReset() {
		// Cancel Models
		for (int x = 0; x < this.modelGridClone.length; x++) {
			for (int y = 0; y < this.modelGridClone[0].length; y++) {
				this.modelGridClone[x][y] = this.epithelium.getModel(x, y);
			}
		}
		// Cancel Colors
		this.colorMapClone.clear();
		for (JRadioButton jrb : this.mapSBMLMiniPanels.keySet()) {
			String modelName = jrb.getText();
			Color c = this.modelFeatures.getColor(modelName);
			this.mapSBMLMiniPanels.get(jrb).setBackground(c);
			this.colorMapClone.put(this.modelFeatures.getModel(modelName), c);
		}
		this.visualGridModel.paintComponent(this.visualGridModel.getGraphics());
	}

	@Override
	protected void buttonAccept() {
		// Copy modelClone to modelGrid
		for (int x = 0; x < this.modelGridClone.length; x++) {
			for (int y = 0; y < this.modelGridClone[0].length; y++) {
				if (!this.epithelium.getModel(x, y).equals(
						this.modelGridClone[x][y])) {
					this.epithelium.setModel(x, y, this.modelGridClone[x][y]);
				}
			}
		}
		// Copy colorMapClone to ProjectModelFeatures
		for (JRadioButton jrb : this.mapSBMLMiniPanels.keySet()) {
			String modelName = jrb.getText();
			this.modelFeatures.setColor(modelName,
					this.mapSBMLMiniPanels.get(jrb).getBackground());
		}
		// Make Epithelium structures coherent
		this.epithelium.update();
		// TODO Open dependent tabs, should
	}

	@Override
	protected boolean isChanged() {
		// Check modifications on model
		for (int x = 0; x < this.modelGridClone.length; x++) {
			for (int y = 0; y < this.modelGridClone[0].length; y++) {
				if (!this.modelGridClone[x][y].equals(this.epithelium.getModel(
						x, y))) {
					return true;
				}
			}
		}
		// Check modifications on color
		for (JRadioButton jrb : this.mapSBMLMiniPanels.keySet()) {
			String modelName = jrb.getText();
			Color cOrig = this.modelFeatures.getColor(modelName);
			if (!this.mapSBMLMiniPanels.get(jrb).getBackground().equals(cOrig)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void notifyChange() {
		if (!this.isInitialized)
			return;
		this.updateModelList();
	}
}