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
import java.util.List;
import java.util.Map;

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
import org.epilogtool.common.ObjectComparator;
import org.epilogtool.core.Epithelium;
import org.epilogtool.core.EpitheliumGrid;
import org.epilogtool.gui.EpiGUI.TabChangeNotifyProj;
import org.epilogtool.gui.EpiGUI.ProjChangeNotifyTab;
import org.epilogtool.gui.widgets.GridInformation;
import org.epilogtool.gui.widgets.JComboWideBox;
import org.epilogtool.gui.widgets.VisualGridInitialConditions;
import org.epilogtool.io.ButtonFactory;
import org.epilogtool.project.ProjectFeatures;

public class EpiTabInitialConditions extends EpiTabDefinitions {
	private static final long serialVersionUID = -3626371381385041594L;

	private VisualGridInitialConditions visualGridICs;
	private EpitheliumGrid epiGridClone;
	private List<String> lNodeInPanel;
	private Map<String, Byte> mNode2ValueSelected;
	
	// Reference for all Nodes
	private Map<String, JButton> mNode2JButton;
	private Map<String, JCheckBox> mNode2Checkbox;
	private Map<String, JComboBox<Byte>> mNode2Combobox;
	private TabProbablyChanged tpc;

	private JPanel jpRCenter;
	private GridInformation lRight;
	private JPanel rTop;
	private JRadioButton allNodes;
	private JRadioButton selectedNodes;
	
	private LogicalModel selectedModel;
	
	private JComboBox<String> jcbSBML;

	public EpiTabInitialConditions(Epithelium e, TreePath path,
			ProjChangeNotifyTab projChanged, TabChangeNotifyProj tabChanged,
			ProjectFeatures projectFeatures) {
		super(e, path, projChanged, tabChanged, projectFeatures);
	}

	public void initialize() {

		this.center.setLayout(new BorderLayout());

		this.epiGridClone = this.epithelium.getEpitheliumGrid().clone();

		this.mNode2ValueSelected = new HashMap<String, Byte>();
		this.lNodeInPanel = new ArrayList<String>();
		// Create everything at the beginning for every nodeID
		this.mNode2JButton = new HashMap<String, JButton>();
		this.mNode2Checkbox = new HashMap<String, JCheckBox>();
		this.mNode2Combobox = new HashMap<String, JComboBox<Byte>>();
		for (LogicalModel m : this.epiGridClone.getModelSet()) {
			this.createGUIForModel(m);
		}

		this.lRight = new GridInformation(
				this.epithelium.getIntegrationFunctions(), this.projectFeatures);

		this.tpc = new TabProbablyChanged();
		this.visualGridICs = new VisualGridInitialConditions(this.epiGridClone,
				this.epithelium.getProjectFeatures().getNodeID2ColorMap(),
				this.mNode2ValueSelected, this.lRight, this.tpc);
		this.center.add(this.visualGridICs, BorderLayout.CENTER);

		JPanel left = new JPanel(new BorderLayout());
		
		this.rTop = new JPanel();
		this.rTop.setLayout(new BoxLayout(this.rTop, BoxLayout.Y_AXIS));

		// Model selection list
		List<LogicalModel> modelList = new ArrayList<LogicalModel>(
				this.epithelium.getEpitheliumGrid().getModelSet());
		this.jcbSBML = this.newModelCombobox(modelList);
		this.rTop.add(this.jcbSBML);
		this.selectedModel = this.epithelium.getProjectFeatures().getModel((String) this.jcbSBML.getSelectedItem());

		// Select / Deselect buttons
		JPanel rTopSel = new JPanel(new FlowLayout());
		// rTopSel.setLayout(new BoxLayout(rTopSel, BoxLayout.X_AXIS));
		JButton jbSelectAll = new JButton("Select All");
		jbSelectAll.setMargin(new Insets(0, 0, 0, 0));
		jbSelectAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (String nodeID : lNodeInPanel) {
					mNode2Checkbox.get(nodeID).setSelected(true);
					mNode2ValueSelected
							.put(nodeID, (Byte) mNode2Combobox.get(nodeID)
									.getSelectedItem());
				}
				visualGridICs.paintComponent(visualGridICs.getGraphics());
			}
		});
		rTopSel.add(jbSelectAll);
		JButton jbDeselectAll = new JButton("Deselect All");
		jbDeselectAll.setMargin(new Insets(0, 0, 0, 0));
		jbDeselectAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (String nodeID : lNodeInPanel) {
					mNode2Checkbox.get(nodeID).setSelected(false);
					mNode2ValueSelected.remove(nodeID);
				}
				visualGridICs.paintComponent(visualGridICs.getGraphics());
			}
		});
		rTopSel.add(jbDeselectAll);
		this.rTop.add(rTopSel);

		this.rTop
				.setBorder(BorderFactory.createTitledBorder("Display options"));
		
		

		left.add(rTop, BorderLayout.NORTH);
		
		//INternal and Proper compontes JScrollPanel
		this.jpRCenter = new JPanel();
		this.jpRCenter.setLayout(new BoxLayout(jpRCenter, BoxLayout.Y_AXIS));
		JScrollPane jscroll = new JScrollPane(this.jpRCenter);
		jscroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		left.add(jscroll, BorderLayout.CENTER);

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
						mNode2ValueSelected.put(nodeID, (Byte) mNode2Combobox
								.get(nodeID).getSelectedItem());
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
						mNode2Combobox.get(nodeID).setSelectedIndex(0);
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
		

		//Create Panel for the random initial conditions
		
		JPanel RRandomInitialConditions = new JPanel(new BorderLayout());
		
		RRandomInitialConditions.setBorder(BorderFactory.createTitledBorder("Random Initial Conditions"));
		ButtonGroup group = new ButtonGroup();
//		JCheckBox randomInitialConditions = new JCheckBox("Random Initial Conditions");
		this.allNodes = new JRadioButton ("All components");
		this.selectedNodes = new JRadioButton ("Selected components");
		
		allNodes.setSelected(true);
		group.add(allNodes);
		group.add(selectedNodes);
		
		JButton jbApplyRandom = ButtonFactory.getNoMargins("Apply");
		jbApplyRandom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				List<String> nodes = new ArrayList<String>();
				for (String nodeID : lNodeInPanel) {
					if (allNodes.isSelected() | (selectedNodes.isSelected() & mNode2Checkbox.get(nodeID).isSelected())) {
						nodes.add(nodeID);
						
					}
					}
				randomMarkCells(nodes);
			}

		});
		
//		RBottomRandomInitialConditions.add(randomInitialConditions);
		RRandomInitialConditions.add(allNodes, BorderLayout.NORTH);
		RRandomInitialConditions.add(selectedNodes, BorderLayout.CENTER);
		RRandomInitialConditions.add(jbApplyRandom, BorderLayout.SOUTH);
		
		JPanel RRbottom = new JPanel(new BorderLayout());
		
		RRbottom.add(rBottom, BorderLayout.NORTH);
		RRbottom.add(RRandomInitialConditions, BorderLayout.SOUTH);
		

		
		left.add(RRbottom, BorderLayout.SOUTH);

		JPanel jpLeftAggreg = new JPanel(new BorderLayout());
		jpLeftAggreg.add(left, BorderLayout.LINE_START);
		jpLeftAggreg.add(this.lRight, BorderLayout.LINE_END);

		this.center.add(jpLeftAggreg, BorderLayout.LINE_START);
		updateComponentList((String) this.jcbSBML.getSelectedItem());
		this.isInitialized = true;
	}


	private void randomMarkCells(List<String> nodes) {
		// TODO Auto-generated method stub
		List<NodeInfo> lNodes = new ArrayList<NodeInfo>();
		for (String sNode: nodes){
		NodeInfo node = this.epithelium.getProjectFeatures().getNodeInfo(sNode, this.selectedModel);
		if (!node.isInput())
			lNodes.add(node);
		}
			this.visualGridICs.setRandomValue(lNodes);
		
			updateComponentList((String) this.jcbSBML.getSelectedItem());
		
	
		
	}
	private void createGUIForModel(LogicalModel m) {
		for (NodeInfo node : m.getNodeOrder()) {
			String nodeID = node.getNodeID();
			// Color
			JButton jButton = new JButton();
			jButton.setBackground(this.epithelium.getProjectFeatures()
					.getNodeColor(nodeID));
			jButton.setToolTipText(nodeID);
			jButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setNewColor((JButton) e.getSource());
				}
			});
			this.mNode2JButton.put(nodeID, jButton);
			// Checkbox
			JCheckBox jcheckb = new JCheckBox(nodeID, false);
			jcheckb.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JCheckBox jcb = (JCheckBox) e.getSource();
					String nodeID = jcb.getText();
					if (jcb.isSelected()) {
						mNode2ValueSelected.put(nodeID, (Byte) mNode2Combobox
								.get(nodeID).getSelectedItem());
					} else {
						mNode2ValueSelected.remove(nodeID);
					}
					// Repaint
					visualGridICs.paintComponent(visualGridICs.getGraphics());
				}
			});
			this.mNode2Checkbox.put(nodeID, jcheckb);
			// Combobox
			int max = this.epithelium.getProjectFeatures()
					.getNodeInfo(nodeID, m).getMax();
			JComboBox<Byte> jcombob = new JComboBox<Byte>();
			jcombob.setToolTipText(nodeID);
			for (byte i = 0; i <= max; i++)
				jcombob.addItem(i);
			jcombob.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					@SuppressWarnings("unchecked")
					JComboBox<Byte> jcb = (JComboBox<Byte>) e.getSource();
					String nodeID = jcb.getToolTipText();
					if (mNode2ValueSelected.containsKey(nodeID)) {
						mNode2ValueSelected.put(nodeID,
								(Byte) jcb.getSelectedItem());
					}
				}
			});
			this.mNode2Combobox.put(nodeID, jcombob);
		}
	}

	private JComboBox<String> newModelCombobox(List<LogicalModel> modelList) {
		// Model selection list
		String[] saSBML = new String[modelList.size()];
		for (int i = 0; i < modelList.size(); i++) {
			saSBML[i] = this.projectFeatures.getModelName(modelList.get(i));
		}
		JComboBox<String> jcb = new JComboWideBox(saSBML);
		jcb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unchecked")
				JComboBox<String> jcb = (JComboBox<String>) e.getSource();
				setSelectedModel((String) jcb.getSelectedItem());
				updateComponentList((String) jcb.getSelectedItem());
			}
		});
		return jcb;
	}
	
	private void setSelectedModel(String model){
		this.selectedModel = this.projectFeatures.getModel(model);
	}

	//Assign a new color to a component
	private void setNewColor(JButton jb) {
		String nodeID = jb.getToolTipText();
		Color newColor = JColorChooser.showDialog(jb, "Color chooser - "
				+ nodeID, jb.getBackground());
		if (newColor != null
				&& !newColor.equals(projectFeatures.getNodeColor(nodeID))) {
			jb.setBackground(newColor);
			this.epithelium.getProjectFeatures().setNodeColor(nodeID,
					newColor);
			this.projChanged.setChanged(this);
			if (this.mNode2ValueSelected.containsKey(nodeID)) {
				// Paint only if NodeID is selected!!
				this.visualGridICs.paintComponent(this.visualGridICs
						.getGraphics());
			}
		}
	}

	//When a new model is selected, and at the beggining 
	private void updateComponentList(String sModel) {
		this.jpRCenter.removeAll();
		this.mNode2ValueSelected.clear();
		this.lNodeInPanel.clear();

		LogicalModel m = this.projectFeatures.getModel(sModel);
		this.visualGridICs.setModel(m);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(1, 5, 1, 0);
		int y = 0;
		
		

		// Internal components

		List<String> lInternal = new ArrayList<String>(this.epithelium
				.getProjectFeatures().getModelNodeIDs(m, false));
		Collections.sort(lInternal, ObjectComparator.STRING);
		
		if (lInternal.size()>0){
		
		JPanel jpRRCTop = new JPanel(new GridBagLayout());
		jpRRCTop.setBorder(BorderFactory
				.createTitledBorder("Internal components"));
		for (String nodeID : lInternal) {
			
			gbc.gridy = y;
			y++;
			this.lNodeInPanel.add(nodeID);
			if (this.mNode2Checkbox.get(nodeID).isSelected()) {
				this.mNode2ValueSelected.put(nodeID, (Byte) this.mNode2Combobox
						.get(nodeID).getSelectedItem());
			}
			gbc.gridx = 0;
			gbc.anchor = GridBagConstraints.WEST;
			jpRRCTop.add(this.mNode2Checkbox.get(nodeID), gbc);
			gbc.gridx = 1;
			jpRRCTop.add(this.mNode2Combobox.get(nodeID), gbc);
			gbc.gridx = 2;
			JButton jbTmp = this.mNode2JButton.get(nodeID);
			jbTmp.setBackground(this.epithelium.getProjectFeatures()
					.getNodeColor(nodeID));
			
			jpRRCTop.add(jbTmp, gbc);
			
			JLabel percentage = new JLabel(this.visualGridICs.getEpitheliumGrid().getPercentage(nodeID));
			gbc.gridx = 3;
			jpRRCTop.add(percentage,gbc);
		}
		this.jpRCenter.add(jpRRCTop);
		}
		// Input components
		
		List<String> lInputs = new ArrayList<String>(this.epithelium
				.getProjectFeatures().getModelNodeIDs(m, true));
		Collections.sort(lInputs, ObjectComparator.STRING);
		List<String> lEnvInputCompsFromSelectedModels = new ArrayList<String>();
		for (String nodeID : lInputs) {
			if (!this.epithelium.isIntegrationComponent(this.epithelium
					.getProjectFeatures().getNodeInfo(nodeID, m))) {
				lEnvInputCompsFromSelectedModels.add(nodeID);
			}
		}
		//If no env inputs then the box is not created
		if (lEnvInputCompsFromSelectedModels.size()!=0)
			{
		JPanel jpRRCBottom = new JPanel(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.insets = new Insets(1, 5, 1, 0);
		jpRRCBottom.setBorder(BorderFactory
				.createTitledBorder("Input components"));

		y = 0;
		for (String nodeID : lEnvInputCompsFromSelectedModels) {
			
			gbc.gridy = y;
			y++;
			this.lNodeInPanel.add(nodeID);
			if (this.mNode2Checkbox.get(nodeID).isSelected()) {
				this.mNode2ValueSelected.put(nodeID, (Byte) this.mNode2Combobox
						.get(nodeID).getSelectedItem());
			}
			gbc.gridx = 0;
			gbc.anchor = GridBagConstraints.WEST;
			jpRRCBottom.add(this.mNode2Checkbox.get(nodeID), gbc);
			gbc.gridx = 1;
			jpRRCBottom.add(this.mNode2Combobox.get(nodeID), gbc);
			gbc.gridx = 2;
			JButton jbTmp = this.mNode2JButton.get(nodeID);
			jbTmp.setBackground(this.epithelium.getProjectFeatures()
					.getNodeColor(nodeID));
			
			jpRRCBottom.add(jbTmp, gbc);
			
			JLabel percentage = new JLabel(this.visualGridICs.getEpitheliumGrid().getPercentage(nodeID));
			gbc.gridx = 3;
			jpRRCBottom.add(percentage,gbc);
		}
		this.jpRCenter.add(jpRRCBottom);

	
	}
		// Re-Paint
		this.getParent().repaint();}

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
//		System.out.println("EpiTab.applyChange");
		List<LogicalModel> modelList = new ArrayList<LogicalModel>(
				this.epithelium.getEpitheliumGrid().getModelSet());
		// Update grid
		EpitheliumGrid grid = this.epithelium.getEpitheliumGrid();
		for (int x = 0; x < this.epiGridClone.getX(); x++) {
			for (int y = 0; y < this.epiGridClone.getY(); y++) {
				LogicalModel newModel = grid.getModel(x, y);
				if (!this.epiGridClone.getModel(x, y).equals(newModel)) {
					if (!this.epiGridClone.getModelSet().contains(newModel)) {
						this.createGUIForModel(newModel);
					}
					this.epiGridClone.setModel(x, y, grid.getModel(x, y));
				}
			}
		}

		this.rTop.remove(0);
		this.rTop.add(this.newModelCombobox(modelList), 0);
//		this.updateComponentList(this.projectFeatures.getModelName(modelList
//				.get(0)));
	}
}