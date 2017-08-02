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
import java.util.Collections;
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
import org.epilogtool.common.ObjectComparator;
import org.epilogtool.core.Epithelium;
import org.epilogtool.core.EpitheliumGrid;
import org.epilogtool.gui.EpiGUI.ProjChangeNotifyTab;
import org.epilogtool.gui.EpiGUI.TabChangeNotifyProj;
import org.epilogtool.gui.dialog.GridNodePercent;
import org.epilogtool.gui.widgets.GridInformation;
import org.epilogtool.gui.widgets.JComboCheckBox;
import org.epilogtool.gui.widgets.JComboWideBox;
import org.epilogtool.gui.widgets.VisualGridInitialConditions;
import org.epilogtool.io.ButtonFactory;
import org.epilogtool.project.Project;

public class EpiTabInitialConditions extends EpiTabDefinitions {
	private static final long serialVersionUID = -3626371381385041594L;

	private VisualGridInitialConditions visualGridICs;
	private EpitheliumGrid epiGridClone;
	private List<String> lNodeInPanel;
	private Map<String, Byte> mNode2ValueSelected;

	// Reference for all Nodes
	private Map<String, JCheckBox> mNode2Checkbox;

	private TabProbablyChanged tpc;

	private JPanel jpRCenter;
	private JPanel jpLeftTop;
	private JPanel jpLeft;
	
	private GridInformation lRight;
	private JRadioButton allNodes;
	private JRadioButton selectedNodes;

	private LogicalModel selectedModel;
	private JComboCheckBox jccbSBML;
	
	private List<String> lPresentComps;
	private List<String> lCompON;
	private Map<String, Boolean> mSelCheckboxes;
	private Map<String, JCheckBox> mNodeID2Checkbox;
	private Map<String, JComboBox<Byte>> mNodeID2Combobox;
	private Map<JButton, String> colorButton2Node;

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

		this.lCompON = new ArrayList<String>();
		this.mSelCheckboxes = new HashMap<String, Boolean>();
		this.mNode2ValueSelected = new HashMap<String, Byte>();
		this.mNodeID2Checkbox = new HashMap<String, JCheckBox>();
		this.mNodeID2Combobox = new HashMap<String, JComboBox<Byte>>();
		this.colorButton2Node = new HashMap<JButton, String>();
		for (LogicalModel m : this.epithelium.getEpitheliumGrid().getModelSet()) {
			for (NodeInfo node : m.getNodeOrder()) {
				this.mSelCheckboxes.put(node.getNodeID(), false);
			}
		}
		this.lRight = new GridInformation(this.epithelium.getIntegrationFunctions());

		this.tpc = new TabProbablyChanged();
		this.visualGridICs = new VisualGridInitialConditions(this.epiGridClone, this.mNode2ValueSelected, this.lRight,
				this.tpc);
		this.center.add(this.visualGridICs, BorderLayout.CENTER);

		this.jpLeft = new JPanel(new BorderLayout());

		this.jpLeftTop = new JPanel();
		this.jpLeftTop.setLayout(new BoxLayout(this.jpLeftTop, BoxLayout.Y_AXIS));
		
//---------------------------------------------------------------------------
// Model selection jcomboCheckBox

		List<LogicalModel> modelList = new ArrayList<LogicalModel>(this.epithelium.getEpitheliumGrid().getModelSet());
		JCheckBox[] items = new JCheckBox[modelList.size()];
		for (int i = 0; i < modelList.size(); i++) {
			items[i] = new JCheckBox(Project.getInstance().getProjectFeatures().getModelName(modelList.get(i)));
			items[i].setSelected(false);
		}
		this.jccbSBML = new JComboCheckBox(items);
		this.jpLeftTop.add(this.jccbSBML);
		
//---------------------------------------------------------------------------
// Select/Deselect active nodes Buttons
		
		this.jpLeftTop.setBorder(BorderFactory.createTitledBorder("Display options"));
		this.jpLeft.add(this.jpLeftTop, BorderLayout.NORTH);
		
		JPanel rrTopSel = new JPanel(new FlowLayout());
		
		JButton jbSelectAll = new JButton("SelectAll");
		jbSelectAll.setMargin(new Insets(0, 0, 0, 0));
		jbSelectAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (String nodeID : lPresentComps) {
					if (mNodeID2Checkbox.containsKey(nodeID)) {
						mNodeID2Checkbox.get(nodeID).setSelected(true);
					}
					mSelCheckboxes.put(nodeID, true);
					if (!lCompON.contains(nodeID))
						lCompON.add(nodeID);
				}
				visualGridICs.paintComponent(visualGridICs.getGraphics());
			}
		});
		rrTopSel.add(jbSelectAll);
		JButton jbDeselectAll = new JButton("DeselectAll");
		jbDeselectAll.setMargin(new Insets(0, 0, 0, 0));
		jbDeselectAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (String nodeID : lPresentComps) {
					if (mNodeID2Checkbox.containsKey(nodeID)) {
						mNodeID2Checkbox.get(nodeID).setSelected(false);
					}
					mSelCheckboxes.put(nodeID, false);
					lCompON.remove(nodeID);
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
		
//---------------------------------------------------------------------------
// Apply/Clear/Rectangle buttons
		JPanel rBottom = new JPanel();
		rBottom.setLayout(new BoxLayout(rBottom, BoxLayout.Y_AXIS));
		JPanel rBottomApplyClear = new JPanel(new FlowLayout());
		JButton jbApplyAll = ButtonFactory.getNoMargins("Apply All");
		jbApplyAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (String nodeID : lNodeInPanel) {
					if (mNode2Checkbox.get(nodeID).isSelected()) {
						mNode2ValueSelected.put(nodeID, (Byte) mNodeID2Combobox.get(nodeID).getSelectedItem());
					}
				}
				visualGridICs.applyDataToAll();
			}
		});
		rBottomApplyClear.add(jbApplyAll);
		JButton jbClearAll = ButtonFactory.getNoMargins("Clear All");
		jbClearAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (String nodeID : lNodeInPanel) {
					if (mNode2Checkbox.get(nodeID).isSelected()) {
						mNode2ValueSelected.put(nodeID, (byte) 0);
						mNodeID2Combobox.get(nodeID).setSelectedIndex(0);
					}
				}
				visualGridICs.applyDataToAll();
			}
		});
		rBottomApplyClear.add(jbClearAll);
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

//---------------------------------------------------------------------------
// Create Panel for the random initial conditions

		JPanel RRandomInitialConditions = new JPanel(new BorderLayout());

		RRandomInitialConditions.setBorder(BorderFactory.createTitledBorder("Random Initial Conditions"));
		ButtonGroup group = new ButtonGroup();
		// JCheckBox randomInitialConditions = new JCheckBox("Random Initial
		// Conditions");
		this.allNodes = new JRadioButton("All components");
		this.selectedNodes = new JRadioButton("Selected components");

		allNodes.setSelected(true);
		group.add(allNodes);
		group.add(selectedNodes);

		JButton jbApplyRandom = ButtonFactory.getNoMargins("Apply");
		jbApplyRandom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				List<String> nodes = new ArrayList<String>();
				for (String nodeID : lNodeInPanel) {
					if (allNodes.isSelected()
							| (selectedNodes.isSelected() & mNode2Checkbox.get(nodeID).isSelected())) {
						nodes.add(nodeID);
					}
				}
				randomMarkCells(nodes);
			}

		});

		RRandomInitialConditions.add(allNodes, BorderLayout.NORTH);
		RRandomInitialConditions.add(selectedNodes, BorderLayout.CENTER);
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
//---------------------------------------------------------------------------
//End initialize

/**
 * Creates the panel with the selection of the components to display.
 * 
 * @param sNodeIDs : List with the nodes names to be written
 * @param titleBorder : String with the title of the panel
 */
private void setComponentTypeList(Set<String> sNodeIDs, String titleBorder,List<LogicalModel> listModels) {
	JPanel jpRRC = new JPanel(new GridBagLayout());
	GridBagConstraints gbc = new GridBagConstraints();
	gbc.insets = new Insets(5, 5, 4, 0);
	jpRRC.setBorder(BorderFactory.createTitledBorder(titleBorder));
	List<String> nodeList = new ArrayList<String>(sNodeIDs);
	Collections.sort(nodeList, ObjectComparator.STRING);
	int y = 0;
	int max = 0;
	for (String nodeID : nodeList) {
		for (LogicalModel m: listModels) {
			for (NodeInfo n: m.getNodeOrder()) {
				if (n.getNodeID().equals(nodeID)) {
				max = Project.getInstance().getProjectFeatures().getNodeInfo(nodeID, m).getMax();
				break;
			}}
		}
		this.lPresentComps.add(nodeID);
		if (mSelCheckboxes.get(nodeID)) {
			this.lCompON.add(nodeID);
		}
		this.getCompMiniPanel(jpRRC, gbc, y, nodeID, max);
		y++;
	}
	this.jpRCenter.add(jpRRC);
}

/**
 * Updates components check selection list, once the selected model to display is changed.
 * 
 * @param modelNames
 */
private void updateComponentList(List<String> modelNames) {

	this.jpRCenter.removeAll();
	this.lCompON.clear();
	this.colorButton2Node.clear();

	List<LogicalModel> lModels = new ArrayList<LogicalModel>();
	for (String modelName : modelNames) {
		lModels.add(Project.getInstance().getProjectFeatures().getModel(modelName));
	}
	
	this.visualGridICs.setModels(lModels);
	
	
	List<NodeInfo> lInternal = new ArrayList<NodeInfo>(
			Project.getInstance().getProjectFeatures().getModelsNodeInfos(lModels, false));
	
	List<NodeInfo> lInputs = new ArrayList<NodeInfo>(
			Project.getInstance().getProjectFeatures().getModelsNodeInfos(lModels, true));

	this.lPresentComps = new ArrayList<String>();
	
	Set<String> sInternalNodeIDs = new HashSet<String>();
	Set<String> sInputNodeIDs = new HashSet<String>();
	Set<String> sCommonNodeIDs = new HashSet<String>();

	for (NodeInfo node : lInternal)
		sInternalNodeIDs.add(node.getNodeID());

	for (NodeInfo node : lInputs) {
		if (sInternalNodeIDs.contains(node.getNodeID())) {
			sCommonNodeIDs.add(node.getNodeID());
			sInternalNodeIDs.remove(node.getNodeID());
		} else if (!sCommonNodeIDs.contains(node.getNodeID())) {
			sInputNodeIDs.add(node.getNodeID());
		}
	}
	
	if (!sCommonNodeIDs.isEmpty())
		this.setComponentTypeList(sCommonNodeIDs, "Internal/Input Components",lModels);
	if (!sInternalNodeIDs.isEmpty())
		this.setComponentTypeList(sInternalNodeIDs, "Internal Components",lModels);
	if (!sInputNodeIDs.isEmpty())
		this.setComponentTypeList(sInputNodeIDs, "Input Components",lModels);
	
	
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
private void getCompMiniPanel(JPanel jp, GridBagConstraints gbc, int y, String nodeID, int max) {

	EpitheliumGrid  grid = this.epiGridClone;
			
	gbc.gridy = y;
	gbc.gridx = 0;
	gbc.anchor = GridBagConstraints.WEST;

	JCheckBox jcb = this.mNodeID2Checkbox.get(nodeID);
	
	if (jcb == null) {
		jcb = new JCheckBox(nodeID, mSelCheckboxes.get(nodeID));
		jcb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JCheckBox jcb = (JCheckBox) e.getSource();
				mSelCheckboxes.put(jcb.getText(), jcb.isSelected());
				if (jcb.isSelected()) {
					lCompON.add(jcb.getText());
				} else {
					lCompON.remove(jcb.getText());
				}
				visualGridICs.paintComponent(visualGridICs.getGraphics());
			}
		});
		this.mNodeID2Checkbox.put(nodeID, jcb);
	}
	jp.add(jcb, gbc);
	gbc.gridx = 1;
	
	// Combobox
	
	JComboBox<Byte> jcombob = new JComboBox<Byte>();
	jcombob.setToolTipText(nodeID);
	for (byte i = 0; i <= max; i++)
		jcombob.addItem(i);
	
	if (mNode2ValueSelected.get(nodeID)!=null)
		jcombob.setSelectedItem(mNode2ValueSelected.get(nodeID));
	
	jcombob.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			
			@SuppressWarnings("unchecked")
			JComboBox<Byte> jcb = (JComboBox<Byte>) e.getSource();
			String nodeID = jcb.getToolTipText();
				
			mNode2ValueSelected.put(nodeID, (Byte) jcb.getSelectedItem());
			
		}
	});
	
	this.mNodeID2Combobox.put(nodeID, jcombob);
	
	jp.add(jcombob, gbc);
	gbc.gridx = 2;
	
	JButton jbColor = new JButton();
	jbColor.setBackground(Project.getInstance().getProjectFeatures().getNodeColor(nodeID));
	jbColor.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			setNewColor((JButton) e.getSource());
		}
	});
	
	jp.add(jbColor, gbc);
	
	String nodePercent = (String)OptionStore.getOption("PrefsNodePercent");
	if (nodePercent != null && nodePercent.equals(GridNodePercent.YES.toString())) {
		gbc.gridx = 2;
		JLabel percentage = new JLabel(grid.getPercentage(nodeID));
		jp.add(percentage, gbc);
	}
	this.colorButton2Node.put(jbColor, nodeID);
}
	/**
	 * Randomly assign a state to the cells. Note that only the list of nodes provided are randomly changed.
	 * 
	 * @param nodes
	 */
	private void randomMarkCells(List<String> nodes) {

		List<NodeInfo> lNodes = new ArrayList<NodeInfo>();
		for (String sNode : nodes) {
			NodeInfo node = Project.getInstance().getProjectFeatures().getNodeInfo(sNode, this.selectedModel);
			if (!node.isInput())
				lNodes.add(node);
		}
		this.visualGridICs.setRandomValue(lNodes);
		this.updateComponentList(this.jccbSBML.getSelectedItems());
	}

	private JComboBox<String> newModelCombobox(List<LogicalModel> modelList) {
		// Model selection list
		String[] saSBML = new String[modelList.size()];
		for (int i = 0; i < modelList.size(); i++) {
			saSBML[i] = Project.getInstance().getProjectFeatures().getModelName(modelList.get(i));
		}
		JComboBox<String> jcb = new JComboWideBox<String>(saSBML);
		jcb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				@SuppressWarnings("unchecked")
				JComboCheckBox jccb = (JComboCheckBox) e.getSource();
				jccb.updateSelected();
				updateComponentList(jccb.getSelectedItems());
			}
		});
		return jcb;
	}

	// Assign a new color to a component
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
		// Copy cellGridClone to EpitheliumGrid
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
		List<LogicalModel> modelList = new ArrayList<LogicalModel>(this.epithelium.getEpitheliumGrid().getModelSet());
		
		// Update grid
		EpitheliumGrid grid = this.epithelium.getEpitheliumGrid();
		for (int x = 0; x < this.epiGridClone.getX(); x++) {
			for (int y = 0; y < this.epiGridClone.getY(); y++) {
				LogicalModel newModel = grid.getModel(x, y);
				if (!this.epiGridClone.getModel(x, y).equals(newModel)) {
					if (!this.epiGridClone.getModelSet().contains(newModel)) {
						updateComponentList(this.jccbSBML.getSelectedItems());
					}
					this.epiGridClone.setModel(x, y, grid.getModel(x, y));
				}
			}
		}

		this.jpLeftTop.remove(0);
		this.jpLeftTop.add(this.newModelCombobox(modelList), 0);
		// this.updateComponentList(this.projectFeatures.getModelName(modelList
		// .get(0)));
	}
}