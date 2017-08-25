package org.epilogtool.gui.tab;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.tree.TreePath;

import org.colomoto.logicalmodel.LogicalModel;
import org.epilogtool.core.EmptyModel;
import org.epilogtool.core.Epithelium;
import org.epilogtool.core.EpitheliumGrid;
import org.epilogtool.gui.EpiGUI.ProjChangeNotifyTab;
import org.epilogtool.gui.EpiGUI.TabChangeNotifyProj;
import org.epilogtool.gui.widgets.GridInformation;
import org.epilogtool.gui.widgets.JRadioComponentButton;
import org.epilogtool.gui.widgets.VisualGridModel;
import org.epilogtool.project.Project;

public class EpiTabModelGrid extends EpiTabDefinitions {
	private static final long serialVersionUID = -5262665948855829161L;

	private VisualGridModel visualGridModel;
	private Map<JRadioComponentButton, JButton> mapSBMLMiniPanels;
	private LogicalModel[][] modelGridClone;
	private Map<LogicalModel, Color> colorMapClone;
	private JPanel jpModelSelection;
	private JPanel jpModelsUsed;
	private GridInformation gridInfo;
	private TabProbablyChanged tpc;
//	private List<LogicalModel> modelsAssigned;

	JToggleButton jtbRectFill;
	JButton jbApplyAll;

	public EpiTabModelGrid(Epithelium e, TreePath path, ProjChangeNotifyTab projChanged,
			TabChangeNotifyProj tabChanged) {
		super(e, path, projChanged, tabChanged);
	}

	public void initialize() {
		this.center.setLayout(new BorderLayout());

		this.mapSBMLMiniPanels = new HashMap<JRadioComponentButton, JButton>();
		EpitheliumGrid grid = this.epithelium.getEpitheliumGrid();
		this.modelGridClone = new LogicalModel[grid.getX()][grid.getY()];
		this.colorMapClone = new HashMap<LogicalModel, Color>();

		// Panel with the model selection
		this.jpModelSelection = new JPanel(new GridBagLayout());
		this.jpModelSelection.setBorder(BorderFactory.createTitledBorder("Model Selection"));

		// Panel with the grid Info
		this.gridInfo = new GridInformation(this.epithelium.getIntegrationFunctions());

		// Panel with the cell selection
		JPanel jpCellSelection = new JPanel(new GridBagLayout());
		jpCellSelection.setBorder(BorderFactory.createTitledBorder("Apply Selection"));
		this.jbApplyAll = new JButton("Apply All");
		jbApplyAll.setEnabled(false);
		jbApplyAll.setMargin(new Insets(0, 0, 0, 0));
		jbApplyAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				visualGridModel.applyDataToAll();
			}
		});
		jpCellSelection.add(jbApplyAll);
		this.jtbRectFill = new JToggleButton("Rectangle Fill", false);
		jtbRectFill.setEnabled(false);
		jtbRectFill.setMargin(new Insets(0, 0, 0, 0));
		jtbRectFill.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				JToggleButton jtb = (JToggleButton) e.getSource();
				visualGridModel.isRectangleFill(jtb.isSelected());
			}
		});
		jpCellSelection.add(jtbRectFill);

		// Panel with the models assigned to the grid
		this.jpModelsUsed = new JPanel(new GridBagLayout());

		this.jpModelsUsed.setBorder(BorderFactory.createTitledBorder("Models Assigned"));

		// Panel on the left bottom
		JPanel jpLeftBottom = new JPanel(new BorderLayout());
		jpLeftBottom.add(jpCellSelection, BorderLayout.PAGE_START);
		jpLeftBottom.add(this.jpModelsUsed, BorderLayout.PAGE_END);

		// Left Panel
		JPanel lTop = new JPanel(new BorderLayout());
		lTop.add(this.jpModelSelection, BorderLayout.PAGE_START);
		lTop.add(this.gridInfo);
		lTop.add(jpLeftBottom, BorderLayout.PAGE_END);

		JPanel left = new JPanel(new BorderLayout());
		left.add(lTop, BorderLayout.CENTER);
		this.center.add(left, BorderLayout.LINE_START);

		this.tpc = new TabProbablyChanged();
		this.visualGridModel = new VisualGridModel(this.epithelium.getEpitheliumGrid().getX(),
				this.epithelium.getEpitheliumGrid().getY(), this.epithelium.getEpitheliumGrid().getTopology(),
				this.modelGridClone, this.colorMapClone, this.gridInfo, this.tpc, this.jpModelsUsed);
		this.center.add(this.visualGridModel, BorderLayout.CENTER);

		this.buttonReset();
		this.updateModelList();
		this.visualGridModel.updateModelUsed();
		this.isInitialized = true;
	}



	private void updateCellSelectionButtons() {
		this.jtbRectFill.setEnabled(true);
		this.jbApplyAll.setEnabled(true);
	}

	/**
	 * Updates the model selection list.
	 * Whenever an SBML is added/removed from the project, the model selection list is automatically updated.
	 */
	private void updateModelList() {
		this.jpModelSelection.removeAll();
		ButtonGroup group = new ButtonGroup();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(1, 5, 1, 0);
		int i = 0;
		for (String name : Project.getInstance().getProjectFeatures().getGUIModelNames()) {
			gbc.gridy = i;
			i++;
			gbc.gridx = 0;
			gbc.anchor = GridBagConstraints.WEST;
			JRadioComponentButton jrButton = new JRadioComponentButton(name);
			jrButton.setToolTipText(name);
			jrButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JRadioComponentButton jrb = (JRadioComponentButton) e.getSource();
					updateCellSelectionButtons();
					visualGridModel.setSelModelName(jrb.getComponentText());
				}
			});
			this.jpModelSelection.add(jrButton, gbc);
			group.add(jrButton);

			gbc.gridx = 1;
			Color newColor = Project.getInstance().getProjectFeatures().getModelColor(name);
			this.colorMapClone.put(Project.getInstance().getProjectFeatures().getModel(name), newColor);
			JButton jbColor = new JButton();
			jbColor.setBackground(newColor);
			jbColor.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setNewColor((JButton) e.getSource());
				}
			});
			this.jpModelSelection.add(jbColor, gbc);
			this.mapSBMLMiniPanels.put(jrButton, jbColor);
		}
		// Clean grid of models that were deleted before an accept
		String defaultModel = Project.getInstance().getProjectFeatures().getModelNames().iterator().next();
		for (int x = 0; x < this.modelGridClone.length; x++) {
			for (int y = 0; y < this.modelGridClone[0].length; y++) {
				if (!Project.getInstance().getProjectFeatures().hasModel(this.modelGridClone[x][y])) {
					this.modelGridClone[x][y] = Project.getInstance().getProjectFeatures().getModel(defaultModel);
				}
			}
		}
		visualGridModel.setSelModelName(null);
		this.revalidate();
		this.visualGridModel.paintComponent(this.visualGridModel.getGraphics());
	}

	/**
	 * Changes the color associated with a model.
	 * PV: Tab knows that it changed if the new color exists and it is different from the previous color.
	 *
	 * @param jb
	 */
	private void setNewColor(JButton jb) {
		for (JRadioComponentButton jrb : this.mapSBMLMiniPanels.keySet()) {
			if (this.mapSBMLMiniPanels.get(jrb).equals(jb)) {
				String name = jrb.getComponentText();

				Color newColor = JColorChooser.showDialog(jb, "Color Chooser - " + name, jb.getBackground());
				if (newColor != null && newColor !=jb.getBackground()) {
					jb.setBackground(newColor);
					this.tpc.setChanged();
					this.colorMapClone.put(Project.getInstance().getProjectFeatures().getModel(name), newColor);
					this.visualGridModel.paintComponent(this.visualGridModel.getGraphics());
					if (EmptyModel.getInstance().isEmptyModel(name)) {
						EmptyModel.getInstance().setColor(newColor);
					}
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
		for (JRadioComponentButton jrb : this.mapSBMLMiniPanels.keySet()) {
			String modelName = jrb.getComponentText();
			Color c = Project.getInstance().getProjectFeatures().getModelColor(modelName);
			this.mapSBMLMiniPanels.get(jrb).setBackground(c);
			this.colorMapClone.put(Project.getInstance().getProjectFeatures().getModel(modelName), c);
		}
		this.visualGridModel.paintComponent(this.visualGridModel.getGraphics());
		this.visualGridModel.updateModelUsed();
	}

	@Override
	protected void buttonAccept() {
		boolean isEmptyGrid = true;
		for (int x = 0; x < this.modelGridClone.length; x++) {
			for (int y = 0; y < this.modelGridClone[0].length; y++) {
				if (Project.getInstance().getProjectFeatures().getModelName(this.modelGridClone[x][y]) != null) {
					isEmptyGrid = false;
					// FIXME: this should break 2 for's right?
				}
			}
		}
		if (isEmptyGrid) {
			this.userMessageEmptyError();
			return;
		}

		// Copy modelClone to modelGrid
		for (int x = 0; x < this.modelGridClone.length; x++) {
			for (int y = 0; y < this.modelGridClone[0].length; y++) {
				if (!this.epithelium.getModel(x, y).equals(this.modelGridClone[x][y])) {
					this.epithelium.setModel(x, y, this.modelGridClone[x][y]);
				}
			}
		}
		// Copy colorMapClone to ProjectModelFeatures
		for (JRadioComponentButton jrb : this.mapSBMLMiniPanels.keySet()) {
			String modelName = jrb.getComponentText();
			if (EmptyModel.getInstance().getName().equals(modelName)) {
				EmptyModel.getInstance().setColor(this.mapSBMLMiniPanels.get(jrb).getBackground());
			} else {
				Project.getInstance().getProjectFeatures().setModelColor(modelName,
						this.mapSBMLMiniPanels.get(jrb).getBackground());
			}
		}
		// Make Epithelium structures coherent
		this.epithelium.update();
		// TODO Open dependent tabs, should
	}

	private void userMessageEmptyError() {
		JOptionPane.showMessageDialog(this,
				"There is no Logical Model associated to this epithelium.\n"
						+ "An epithelium must have at least one cell associated \n" + "to a Logical Model.",
				"Empty epithelium", JOptionPane.ERROR_MESSAGE);
		this.buttonReset();
	}

	@Override
	protected boolean isChanged() {

//------------------ Models were added/removed to the model list

		if (this.modelGridClone.length != this.epithelium.getX()
				|| this.modelGridClone[0].length != this.epithelium.getY()) {
			return true;
		}
//------------------ Models were added/removed to the grid
		for (int x = 0; x < this.modelGridClone.length; x++) {
			for (int y = 0; y < this.modelGridClone[0].length; y++) {
				if (!this.modelGridClone[x][y].equals(this.epithelium.getModel(x, y))) {
					return true;
				}
			}
		}
//------------------ Models colors were changed
		for (JRadioComponentButton jrb : this.mapSBMLMiniPanels.keySet()) {
			String modelName = jrb.getComponentText();
			LogicalModel model = Project.getInstance().getProjectFeatures().getModel(modelName);
			Color cOrig = Project.getInstance().getProjectFeatures().getModelColor(modelName);
			if (!this.colorMapClone.get(model).equals(cOrig)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void applyChange() {
		this.updateModelList();
	}
}
