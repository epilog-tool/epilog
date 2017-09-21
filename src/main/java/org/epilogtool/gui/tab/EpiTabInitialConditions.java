package org.epilogtool.gui.tab;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.ScrollPaneConstants;
import javax.swing.tree.TreePath;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;
import org.epilogtool.OptionStore;
import org.epilogtool.core.Epithelium;
import org.epilogtool.core.EpitheliumGrid;
import org.epilogtool.gui.EpiGUI.ProjChangeNotifyTab;
import org.epilogtool.gui.EpiGUI.TabChangeNotifyProj;
import org.epilogtool.gui.dialog.EnumNodePercent;
import org.epilogtool.gui.widgets.GridInformation;
import org.epilogtool.gui.widgets.JComboCheckBox;
import org.epilogtool.gui.widgets.VisualGridInitialConditions;
import org.epilogtool.io.ButtonFactory;
import org.epilogtool.project.Project;

public class EpiTabInitialConditions extends EpiTabDefinitions {
	private static final long serialVersionUID = -3626371381385041594L;

	private VisualGridInitialConditions visualGridICs;
	private EpitheliumGrid epiGridClone;

	private TabProbablyChanged tpc;

	private JPanel jpRCenter;
	private JPanel jpLeftTop;
	private JPanel jpLeft;

	private GridInformation lRight;
	private JRadioButton randomNodesAll;
	private JRadioButton randomNodesSelected;

	private JComboCheckBox jccbSBML;

	private Set<String> lModelVisibleComps;
	private Map<String, Boolean> mSelCheckboxes;

	private Map<String, JCheckBox> mNodeID2Checkbox;
	private Map<String, JComboBox<Byte>> mNodeID2Combobox;
	private Map<String, JButton> mNodeID2JBColor;

	private Map<String, Byte> mNode2ValueSelected;

	public EpiTabInitialConditions(Epithelium e, TreePath path, ProjChangeNotifyTab projChanged,
			TabChangeNotifyProj tabChanged) {
		super(e, path, projChanged, tabChanged);
	}

	/**
	 * Creates the InitialConditionsPanel, the first time the tab is created.
	 * 
	 */
	public void initialize() {

		this.center.setLayout(new BorderLayout());

		this.epiGridClone = this.epithelium.getEpitheliumGrid().clone();
		this.mSelCheckboxes = new HashMap<String, Boolean>();
		this.mNode2ValueSelected = new HashMap<String, Byte>();
		this.mNodeID2Checkbox = new HashMap<String, JCheckBox>();
		this.mNodeID2Combobox = new HashMap<String, JComboBox<Byte>>();
		this.mNodeID2JBColor = new HashMap<String, JButton>();

		this.lRight = new GridInformation(this.epithelium.getIntegrationFunctions());

		this.tpc = new TabProbablyChanged();
		this.visualGridICs = new VisualGridInitialConditions(this.epiGridClone, this.mNode2ValueSelected, this.lRight,
				this.tpc);
		this.center.add(this.visualGridICs, BorderLayout.CENTER);

		this.jpLeft = new JPanel(new BorderLayout());

		this.jpLeftTop = new JPanel();
		this.jpLeftTop.setLayout(new BoxLayout(this.jpLeftTop, BoxLayout.Y_AXIS));

		// ---------------------------------------------------------------------------
		// Model selection jcomboCheckBox

		List<LogicalModel> modelList = new ArrayList<LogicalModel>(this.epithelium.getEpitheliumGrid().getModelSet());
		JCheckBox[] items = new JCheckBox[modelList.size()];
		for (int i = 0; i < modelList.size(); i++) {
			items[i] = new JCheckBox(Project.getInstance().getProjectFeatures().getModelName(modelList.get(i)));
			items[i].setSelected(false);
		}
		this.jccbSBML = new JComboCheckBox(items);
		this.jpLeftTop.add(this.jccbSBML);

		// ---------------------------------------------------------------------------
		// Select/Deselect active nodes Buttons

		this.jpLeftTop.setBorder(BorderFactory.createTitledBorder("Display options"));
		this.jpLeft.add(this.jpLeftTop, BorderLayout.NORTH);

		JPanel rrTopSel = new JPanel(new FlowLayout());

		JButton jbSelectAll = new JButton("Select All");
		jbSelectAll.setMargin(new Insets(0, 0, 0, 0));
		jbSelectAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (String nodeID : lModelVisibleComps) {
					if (mNodeID2Checkbox.containsKey(nodeID)) {
						mNodeID2Checkbox.get(nodeID).setSelected(true);
					}
					// mSelCheckboxes.put(nodeID, true);
					mNode2ValueSelected.put(nodeID, (Byte) mNodeID2Combobox.get(nodeID).getSelectedItem());

				}
				visualGridICs.paintComponent(visualGridICs.getGraphics());
			}
		});
		rrTopSel.add(jbSelectAll);
		JButton jbDeselectAll = new JButton("Deselect All");
		jbDeselectAll.setMargin(new Insets(0, 0, 0, 0));
		jbDeselectAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (String nodeID : lModelVisibleComps) {
					if (mNodeID2Checkbox.containsKey(nodeID)) {
						mNodeID2Checkbox.get(nodeID).setSelected(false);
					}
					// mSelCheckboxes.put(nodeID, false);
					mNode2ValueSelected.remove(nodeID);
				}
				visualGridICs.paintComponent(visualGridICs.getGraphics());
			}
		});
		rrTopSel.add(jbDeselectAll);
		this.jpLeftTop.add(rrTopSel);

		this.jpRCenter = new JPanel();
		this.jpRCenter.setLayout(new BoxLayout(jpRCenter, BoxLayout.Y_AXIS));
		JScrollPane jsLeftCenter = new JScrollPane(this.jpRCenter);
		jsLeftCenter.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.jpLeft.add(jsLeftCenter, BorderLayout.CENTER);
		this.jccbSBML.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboCheckBox jccb = (JComboCheckBox) e.getSource();
				jccb.updateSelected();
				updateComponentList(jccb.getSelectedItems());
			}
		});

		this.jpLeft.add(jsLeftCenter, BorderLayout.CENTER);

		// ---------------------------------------------------------------------------
		// Apply
		JPanel rBottom = new JPanel();
		rBottom.setLayout(new BoxLayout(rBottom, BoxLayout.Y_AXIS));
		JPanel rBottomApplyClear = new JPanel(new FlowLayout());
		JButton jbApplyAll = ButtonFactory.getNoMargins("Apply Grid");
		jbApplyAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (String nodeID : lModelVisibleComps) {
					if (mNodeID2Checkbox.get(nodeID).isSelected()) {
						mNode2ValueSelected.put(nodeID, (Byte) mNodeID2Combobox.get(nodeID).getSelectedItem());
					}
				}
				visualGridICs.applyDataToAll();
			}
		});
		rBottomApplyClear.add(jbApplyAll);

		// Clear
		JButton jbClearAll = ButtonFactory.getNoMargins("Clear Grid");
		jbClearAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				visualGridICs.clearGrid();
			}
		});
		rBottomApplyClear.add(jbClearAll);

		// Rectangle fill
		JPanel rBottomRect = new JPanel(new FlowLayout());
		JToggleButton jtbRectFill = new JToggleButton("Rectangle Fill", false);
		jtbRectFill.setMargin(new Insets(0, 0, 0, 0));
		jtbRectFill.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				JToggleButton jtb = (JToggleButton) e.getSource();
				visualGridICs.isRectangleFill(jtb.isSelected());
			}
		});
		rBottomRect.add(jtbRectFill);

		rBottom.add(rBottomApplyClear);
		rBottom.add(rBottomRect);

		// ---------------------------------------------------------------------------
		// Create Panel for the random initial conditions

		JPanel RRandomInitialConditions = new JPanel(new BorderLayout());

		RRandomInitialConditions.setBorder(BorderFactory.createTitledBorder("Random Initial Conditions"));
		ButtonGroup group = new ButtonGroup();
		// JCheckBox randomInitialConditions = new JCheckBox("Random Initial
		// Conditions");
		this.randomNodesAll = new JRadioButton("All components");
		this.randomNodesSelected = new JRadioButton("Selected components");

		randomNodesAll.setSelected(true);
		group.add(randomNodesAll);
		group.add(randomNodesSelected);

		JButton jbApplyRandom = ButtonFactory.getNoMargins("Apply");
		jbApplyRandom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				randomMarkCells();

			}

		});

		RRandomInitialConditions.add(randomNodesAll, BorderLayout.NORTH);
		RRandomInitialConditions.add(randomNodesSelected, BorderLayout.CENTER);
		RRandomInitialConditions.add(jbApplyRandom, BorderLayout.SOUTH);

		JPanel jpLeftBottom = new JPanel(new BorderLayout());

		jpLeftBottom.add(rBottom, BorderLayout.NORTH);
		jpLeftBottom.add(RRandomInitialConditions, BorderLayout.SOUTH);

		this.jpLeft.add(jpLeftBottom, BorderLayout.SOUTH);

		JPanel jpLeftAggreg = new JPanel(new BorderLayout());
		jpLeftAggreg.add(this.jpLeft, BorderLayout.LINE_START);
		jpLeftAggreg.add(this.lRight, BorderLayout.LINE_END);

		this.center.add(jpLeftAggreg, BorderLayout.LINE_START);
		updateComponentList(this.jccbSBML.getSelectedItems());
		this.isInitialized = true;
	}
	// ---------------------------------------------------------------------------
	// End initialize

	/**
	 * Creates the panel with the selection of the components to display.
	 * 
	 * @param sNodeIDs
	 *            : List with the nodes names to be written
	 * @param titleBorder
	 *            : String with the title of the panel
	 */
	private void setComponentTypeList(List<NodeInfo> lNodes, String titleBorder, List<LogicalModel> listModels) {
		JPanel jpRRC = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 4, 0);
		jpRRC.setBorder(BorderFactory.createTitledBorder(titleBorder));
		// Collections.sort(nodeList, ObjectComparator.STRING); // orders the numbers
		int y = 0;
		for (NodeInfo node : lNodes) {
			for (LogicalModel m : listModels) {
				if (m.getNodeOrder().contains(node) && !this.epithelium.isIntegrationComponent(node)) {
					this.lModelVisibleComps.add(node.getNodeID());
					this.getCompMiniPanel(jpRRC, gbc, y++, node);
					break;
				}
			}
		}
		this.jpRCenter.add(jpRRC);
	}

	/**
	 * Updates components check selection list, once the selected model to display
	 * is changed.
	 * 
	 * @param modelNames
	 */
	private void updateComponentList(List<String> modelNames) {
		List<LogicalModel> lModels = new ArrayList<LogicalModel>();
		for (String modelName : modelNames) {
			lModels.add(Project.getInstance().getProjectFeatures().getModel(modelName));
		}
		this.visualGridICs.setModels(lModels);

		this.lModelVisibleComps = new HashSet<String>();
		this.jpRCenter.removeAll();

		List<NodeInfo> lInternal = new ArrayList<NodeInfo>(
				Project.getInstance().getProjectFeatures().getModelsNodeInfos(lModels, false));
		List<NodeInfo> lInputs = new ArrayList<NodeInfo>(
				Project.getInstance().getProjectFeatures().getModelsNodeInfos(lModels, true));

		if (!lInternal.isEmpty())
			this.setComponentTypeList(lInternal, "Internal Components", lModels);
		if (!lInputs.isEmpty())
			this.setComponentTypeList(lInputs, "Input Components", lModels);

		this.visualGridICs.paintComponent(this.visualGridICs.getGraphics());
		this.jpRCenter.revalidate();
		this.jpRCenter.repaint();
	}

	/**
	 * Creates the inner panel of each component (checkbox, value and color)
	 * 
	 * @param jp
	 * @param gbc
	 * @param y
	 * @param nodeID
	 * @param max
	 */
	private void getCompMiniPanel(JPanel jp, GridBagConstraints gbc, int y, NodeInfo node) {
		String nodeID = node.getNodeID();
		EpitheliumGrid grid = this.epiGridClone;

		gbc.gridy = y;
		gbc.anchor = GridBagConstraints.WEST;

		// ----------------------------------------------------------------------------
		gbc.gridx = 0;

		JCheckBox jcb = this.mNodeID2Checkbox.get(nodeID);
		if (jcb == null) {
			this.mSelCheckboxes.put(nodeID, false);
			jcb = new JCheckBox(nodeID, this.mSelCheckboxes.get(nodeID));
			jcb.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JCheckBox jcb = (JCheckBox) e.getSource();
					mSelCheckboxes.put(jcb.getText(), jcb.isSelected());
					if (jcb.isSelected()) {
						mNode2ValueSelected.put(jcb.getText(),
								(Byte) mNodeID2Combobox.get(jcb.getText()).getSelectedItem());
					} else {
						mNode2ValueSelected.remove(jcb.getText());
					}

					visualGridICs.paintComponent(visualGridICs.getGraphics());
				}
			});
			this.mNodeID2Checkbox.put(nodeID, jcb);
		}
		jp.add(jcb, gbc);

		// ----------------------------------------------------------------------------
		gbc.gridx = 1;

		JComboBox<Byte> jcombob = this.mNodeID2Combobox.get(nodeID);
		if (jcombob == null) {
			jcombob = new JComboBox<Byte>();
			jcombob.setToolTipText(nodeID);
			for (byte i = 0; i <= node.getMax(); i++)
				jcombob.addItem(i);

			jcombob.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					@SuppressWarnings("unchecked")
					JComboBox<Byte> jcb = (JComboBox<Byte>) e.getSource();
					String nodeID = jcb.getToolTipText();
					if (mNodeID2Checkbox.get(nodeID).isSelected())
						mNode2ValueSelected.put(nodeID, (Byte) jcb.getSelectedItem());
				}
			});
			this.mNodeID2Combobox.put(nodeID, jcombob);
		}
		jp.add(jcombob, gbc);

		// ----------------------------------------------------------------------------
		gbc.gridx = 2;

		JButton jbColor = this.mNodeID2JBColor.get(nodeID);
		if (jbColor == null) {
			jbColor = new JButton();
			jbColor.setToolTipText(nodeID);
			jbColor.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setNewColor((JButton) e.getSource());
				}
			});
			this.mNodeID2JBColor.put(nodeID, jbColor);
		}
		jbColor.setBackground(Project.getInstance().getProjectFeatures().getNodeColor(nodeID));
		System.out.println(" . setColor: " + jbColor.getBackground() + " "
				+ Project.getInstance().getProjectFeatures().getNodeColor(nodeID));
		jp.add(jbColor, gbc);

		String nodePercent = (String) OptionStore.getOption("PrefsNodePercent");
		if (nodePercent != null && nodePercent.equals(EnumNodePercent.YES.toString())) {
			gbc.gridx = 2;
			JLabel percentage = new JLabel(grid.getPercentage(nodeID));
			jp.add(percentage, gbc);
		}
	}

	/**
	 * Randomly assign a state to the cells. Note that only the list of nodes
	 * provided are randomly changed.
	 * 
	 * @param nodes
	 */
	private void randomMarkCells() {
		List<String> nodesToMark = new ArrayList<String>();
		for (String nodeID : this.lModelVisibleComps) {
			if (this.randomNodesAll.isSelected()
					| (this.randomNodesSelected.isSelected() & this.mNodeID2Checkbox.get(nodeID).isSelected())) {
				nodesToMark.add(nodeID);
			}
		}

		List<String> models = jccbSBML.getSelectedItems();
		List<NodeInfo> lNodes = new ArrayList<NodeInfo>();

		for (String sNode : nodesToMark) {
			for (String sModel : models) {
				LogicalModel lmModel = Project.getInstance().getProjectFeatures().getModel(sModel);
				for (NodeInfo node : lmModel.getNodeOrder()) {
					if (node.getNodeID().equals(sNode)) {
						if (!this.epithelium.isIntegrationComponent(node)) {
							lNodes.add(node);
							break;
						}
					}
				}
			}

		}
		this.visualGridICs.setRandomValue(lNodes);
	}

	/**
	 * Changes the color assigned to a node.
	 * 
	 * @param jb
	 */
	private void setNewColor(JButton jb) {
		String nodeID = jb.getToolTipText();
		Color newColor = JColorChooser.showDialog(jb, "Color chooser - " + nodeID, jb.getBackground());
		if (newColor != null && !newColor.equals(Project.getInstance().getProjectFeatures().getNodeColor(nodeID))) {
			jb.setBackground(newColor);
			Project.getInstance().getProjectFeatures().setNodeColor(nodeID, newColor);
			this.projChanged.setChanged(this);
			if (this.mNode2ValueSelected.containsKey(nodeID)) {
				// Paint only if NodeID is selected!!
				this.visualGridICs.paintComponent(this.visualGridICs.getGraphics());
			}
		}
	}

	/**
	 * Updates the list of components to present. If a positional input is now an
	 * integration input then it is no longer set to show in the components panel.
	 * On the other hand if an integration input is change to a positional input it
	 * will appear in the components list.
	 * 
	 * @param node
	 *            node to the removed/added in String format.
	 * @param b
	 *            if b is true, then it should be added to the list, otherwise
	 *            removed.
	 */
	public void updatelPresentComps(String node, boolean b) {
		if (b)
			this.lModelVisibleComps.add(node);
		else
			this.lModelVisibleComps.remove(node);
	}

	@Override
	protected void buttonReset() {
		// Cancel CellGrid
		for (int x = 0; x < this.epiGridClone.getX(); x++) {
			for (int y = 0; y < this.epiGridClone.getY(); y++) {
				byte[] currState = this.epiGridClone.getCellState(x, y);
				for (int i = 0; i < currState.length; i++) {
					currState[i] = this.epithelium.getEpitheliumGrid().getCellState(x, y)[i];
				}
			}
		}
		// Repaint
		this.visualGridICs.paintComponent(this.visualGridICs.getGraphics());
	}

	@Override
	protected void buttonAccept() {

		EpitheliumGrid gridOrig = this.epithelium.getEpitheliumGrid();
		for (int x = 0; x < this.epiGridClone.getX(); x++) {
			for (int y = 0; y < this.epiGridClone.getY(); y++) {
				byte[] stateClone = this.epiGridClone.getCellState(x, y);
				byte[] stateOrig = gridOrig.getCellState(x, y);
				if (!Arrays.equals(stateOrig, stateClone)) {
					gridOrig.setCellState(x, y, stateClone);
				}
			}
		}
	}

	@Override
	protected boolean isChanged() {
		// Check modifications on state
		EpitheliumGrid grid = this.epithelium.getEpitheliumGrid();
		for (int x = 0; x < this.epiGridClone.getX(); x++) {
			for (int y = 0; y < this.epiGridClone.getY(); y++) {
				byte[] stateClone = this.epiGridClone.getCellState(x, y);
				byte[] stateOrig = grid.getCellState(x, y);
				if (!Arrays.equals(stateOrig, stateClone)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void applyChange() {
		System.out.println("EpiTabIC.applyChange()");
		// Update grid
		EpitheliumGrid projEpiGrid = this.epithelium.getEpitheliumGrid();
		for (int x = 0; x < this.epiGridClone.getX(); x++) {
			for (int y = 0; y < this.epiGridClone.getY(); y++) {
				if (!this.epiGridClone.getModel(x, y).equals(projEpiGrid.getModel(x, y))) {
					this.epiGridClone.setModel(x, y, projEpiGrid.getModel(x, y));
					if (!Arrays.equals(projEpiGrid.getCellState(x, y), this.epiGridClone.getCellState(x, y))) {
						this.epiGridClone.setCellState(x, y, projEpiGrid.getCellState(x, y));
					}
				}
			}
		}
		// New (potential) model list -> Update JComboCheckBox
		this.epithelium.getEpitheliumGrid().updateModelSet();
		List<String> newModelList = new ArrayList<String>();
		for (LogicalModel m : this.epithelium.getEpitheliumGrid().getModelSet()) {
			newModelList.add(Project.getInstance().getProjectFeatures().getModelName(m));
		}
		this.jccbSBML.updateItemList(newModelList);

		updateComponentList(this.jccbSBML.getSelectedItems());
	}
}
