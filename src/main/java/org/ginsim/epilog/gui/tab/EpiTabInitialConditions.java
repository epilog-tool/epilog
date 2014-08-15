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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.ScrollPaneConstants;
import javax.swing.tree.TreePath;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;
import org.ginsim.epilog.ProjectModelFeatures;
import org.ginsim.epilog.core.Epithelium;
import org.ginsim.epilog.core.EpitheliumCell;
import org.ginsim.epilog.core.EpitheliumGrid;
import org.ginsim.epilog.gui.widgets.VisualGridInitialConditions;
import org.ginsim.epilog.io.ButtonFactory;

public class EpiTabInitialConditions extends EpiTabDefinitions {
	private static final long serialVersionUID = -3626371381385041594L;

	private VisualGridInitialConditions visualGridICs;
	private EpitheliumCell[][] cellGridClone;
	private List<String> lNodeInPanel;
	private Map<String, Byte> mNode2ValueSelected;
	// Reference for all Nodes
	private Map<String, Color> mNode2Color;
	private Map<String, JButton> mNode2JButton;
	private Map<String, JCheckBox> mNode2Checkbox;
	private Map<String, JComboBox<Byte>> mNode2Combobox;

	private JPanel jspRCenter;

	public EpiTabInitialConditions(Epithelium e, TreePath path,
			ProjectModelFeatures modelFeatures) {
		super(e, path, modelFeatures);
	}

	public void initialize() {
		this.center.setLayout(new BorderLayout());

		EpitheliumGrid grid = this.epithelium.getEpitheliumGrid();
		this.cellGridClone = new EpitheliumCell[grid.getX()][grid.getY()];
		for (int x = 0; x < this.cellGridClone.length; x++) {
			for (int y = 0; y < this.cellGridClone[0].length; y++) {
				this.cellGridClone[x][y] = grid.cloneEpitheliumCellAt(x, y);
			}
		}

		this.mNode2ValueSelected = new HashMap<String, Byte>();
		this.lNodeInPanel = new ArrayList<String>();
		// Create everything at the beginning for every nodeID
		this.mNode2Color = new HashMap<String, Color>();
		this.mNode2JButton = new HashMap<String, JButton>();
		this.mNode2Checkbox = new HashMap<String, JCheckBox>();
		this.mNode2Combobox = new HashMap<String, JComboBox<Byte>>();
		for (LogicalModel m : grid.getModelSet()) {
			for (NodeInfo node : m.getNodeOrder()) {
				String nodeID = node.getNodeID();
				// Color
				this.mNode2Color.put(nodeID, this.epithelium
						.getComponentFeatures().getNodeColor(nodeID));
				JButton jButton = new JButton();
				jButton.setBackground(this.mNode2Color.get(nodeID));
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
							mNode2ValueSelected.put(nodeID,
									(Byte) mNode2Combobox.get(nodeID)
											.getSelectedItem());
						} else {
							mNode2ValueSelected.remove(nodeID);
						}
						// Repaint
						visualGridICs.paintComponent(visualGridICs
								.getGraphics());
					}
				});
				this.mNode2Checkbox.put(nodeID, jcheckb);
				// Combobox
				int max = this.epithelium.getComponentFeatures()
						.getNodeInfo(nodeID).getMax();
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

		this.visualGridICs = new VisualGridInitialConditions(this.epithelium
				.getEpitheliumGrid().getX(), this.epithelium
				.getEpitheliumGrid().getY(), this.epithelium
				.getEpitheliumGrid().getTopology(), this.cellGridClone,
				this.mNode2Color, this.mNode2ValueSelected);
		this.center.add(this.visualGridICs, BorderLayout.CENTER);

		JPanel left = new JPanel(new BorderLayout());

		JPanel rTop = new JPanel();
		rTop.setLayout(new BoxLayout(rTop, BoxLayout.Y_AXIS));

		// Model selection list
		List<LogicalModel> modelList = new ArrayList<LogicalModel>(
				this.epithelium.getEpitheliumGrid().getModelSet());
		String[] saSBML = new String[modelList.size()];
		for (int i = 0; i < modelList.size(); i++) {
			saSBML[i] = this.modelFeatures.getName(modelList.get(i));
		}
		JComboBox<String> jcbSBML = new JComboBox<String>(saSBML);
		jcbSBML.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unchecked")
				JComboBox<String> jcb = (JComboBox<String>) e.getSource();
				updateComponentList((String) jcb.getSelectedItem());
			}
		});
		rTop.add(jcbSBML);

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
		rTop.add(rTopSel);

		rTop.setBorder(BorderFactory.createTitledBorder("Display options"));
		left.add(rTop, BorderLayout.NORTH);

		this.jspRCenter = new JPanel();
		this.jspRCenter.setLayout(new BoxLayout(jspRCenter, BoxLayout.Y_AXIS));
		JScrollPane jscroll = new JScrollPane(this.jspRCenter);
		jscroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
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
		left.add(rBottom, BorderLayout.SOUTH);

		this.center.add(left, BorderLayout.LINE_START);
		updateComponentList((String) jcbSBML.getSelectedItem());
	}

	private void setNewColor(JButton jb) {
		String nodeID = jb.getToolTipText();
		Color newColor = JColorChooser.showDialog(jb, "Color chooser - "
				+ nodeID, jb.getBackground());
		if (newColor != null) {
			jb.setBackground(newColor);
			this.mNode2Color.put(nodeID, newColor);
			if (this.mNode2ValueSelected.containsKey(nodeID)) {
				// Paint only if NodeID is selected!!
				this.visualGridICs.paintComponent(this.visualGridICs
						.getGraphics());
			}
		}
	}

	private void updateComponentList(String sModel) {
		this.jspRCenter.removeAll();
		this.mNode2ValueSelected.clear();
		this.lNodeInPanel.clear();

		LogicalModel m = this.modelFeatures.getModel(sModel);
		this.visualGridICs.setModel(m);

		// Proper components
		JPanel jpRRCTop = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(1, 5, 1, 0);
		jpRRCTop.setBorder(BorderFactory
				.createTitledBorder("Proper components"));
		Set<String> sProperCompsFromSelectedModels = this.epithelium
				.getComponentFeatures().getModelComponents(m, false);
		int y = 0;
		for (String nodeID : sProperCompsFromSelectedModels) {
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
			jpRRCTop.add(this.mNode2JButton.get(nodeID), gbc);
		}
		this.jspRCenter.add(jpRRCTop);

		// Input components
		JPanel jpRRCBottom = new JPanel(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.insets = new Insets(1, 5, 1, 0);
		jpRRCBottom.setBorder(BorderFactory
				.createTitledBorder("Input components"));
		Set<String> sInputCompsFromSelectedModels = this.epithelium
				.getComponentFeatures().getModelComponents(m, true);
		List<String> lEnvInputCompsFromSelectedModels = new ArrayList<String>();
		for (String nodeID : sInputCompsFromSelectedModels) {
			if (!this.epithelium.isIntegrationComponent(nodeID)) {
				lEnvInputCompsFromSelectedModels.add(nodeID);
			}
		}
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
			jpRRCBottom.add(this.mNode2JButton.get(nodeID), gbc);
		}
		this.jspRCenter.add(jpRRCBottom);

		// Re-Paint
		this.getParent().repaint();
	}

	@Override
	protected void buttonReset() {
		// Cancel CellGrid
		EpitheliumGrid grid = this.epithelium.getEpitheliumGrid();
		for (int x = 0; x < this.cellGridClone.length; x++) {
			for (int y = 0; y < this.cellGridClone[0].length; y++) {
				this.cellGridClone[x][y] = new EpitheliumCell(grid.getModel(x,
						y));
				this.cellGridClone[x][y].setState(this.epithelium
						.getEpitheliumGrid().getCellState(x, y));
			}
		}
		// Cancel Colors
		ArrayList<String> lTmp = new ArrayList<String>(
				this.mNode2Color.keySet());
		// lTmp to avoid iterating over a Map that we're changing
		for (String nodeID : lTmp) {
			Color cOrig = this.epithelium.getComponentFeatures().getNodeColor(
					nodeID);
			this.mNode2JButton.get(nodeID).setBackground(cOrig);
			this.mNode2Color.put(nodeID, cOrig);
		}
		// Repaint
		this.visualGridICs.paintComponent(this.visualGridICs.getGraphics());
	}

	@Override
	protected void buttonAccept() {
		// Copy cellGridClone to EpitheliumGrid
		EpitheliumGrid grid = this.epithelium.getEpitheliumGrid();
		for (int x = 0; x < this.cellGridClone.length; x++) {
			for (int y = 0; y < this.cellGridClone[0].length; y++) {
				byte[] stateClone = this.cellGridClone[x][y].getState();
				byte[] stateOrig = grid.getCellState(x, y);
				if (!Arrays.equals(stateOrig, stateClone)) {
					grid.setCellState(x, y, stateClone);
				}
			}
		}
		// Copy colorMapClone to ComponentModelFeatures
		for (String nodeID : this.mNode2Color.keySet()) {
			Color c = this.mNode2Color.get(nodeID);
			this.epithelium.getComponentFeatures().setNodeColor(nodeID, c);
		}
	}

	@Override
	protected boolean isChanged() {
		// Check modifications on state
		EpitheliumGrid grid = this.epithelium.getEpitheliumGrid();
		for (int x = 0; x < this.cellGridClone.length; x++) {
			for (int y = 0; y < this.cellGridClone[0].length; y++) {
				byte[] stateClone = this.cellGridClone[x][y].getState();
				byte[] stateOrig = grid.getCellState(x, y);
				if (!Arrays.equals(stateOrig, stateClone)) {
					return true;
				}
			}
		}
		// Check modifications on color
		for (String nodeID : this.mNode2Color.keySet()) {
			Color c = this.mNode2Color.get(nodeID);
			if (!this.epithelium.getComponentFeatures().getNodeColor(nodeID)
					.equals(c)) {
				return true;
			}
		}
		return false;
	}
}