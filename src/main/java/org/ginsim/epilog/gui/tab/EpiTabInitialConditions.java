package org.ginsim.epilog.gui.tab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JSplitPane;
import javax.swing.tree.TreePath;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;
import org.ginsim.epilog.ProjectModelFeatures;
import org.ginsim.epilog.core.Epithelium;
import org.ginsim.epilog.core.EpitheliumCell;
import org.ginsim.epilog.core.EpitheliumGrid;
import org.ginsim.epilog.gui.widgets.JComboCheckBox;
import org.ginsim.epilog.gui.widgets.VisualGridInitialConditions;

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

	private JSplitPane jspRCenter;

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
				this.cellGridClone[x][y] = new EpitheliumCell(grid.getModel(x,
						y));
				this.cellGridClone[x][y].setState(this.epithelium
						.getEpitheliumGrid().getCellState(x, y));
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

		JPanel right = new JPanel(new BorderLayout());

		JPanel rTop = new JPanel();
		rTop.setLayout(new BoxLayout(rTop, BoxLayout.Y_AXIS));

		JPanel rrTopSel = new JPanel();
		rrTopSel.setLayout(new BoxLayout(rrTopSel, BoxLayout.X_AXIS));
		JButton jbSelectAll = new JButton("SelectAll");
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
		rrTopSel.add(jbSelectAll);
		JButton jbDeselectAll = new JButton("DeselectAll");
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
		rrTopSel.add(jbDeselectAll);
		rTop.add(rrTopSel);

		List<LogicalModel> modelList = new ArrayList<LogicalModel>(
				this.epithelium.getEpitheliumGrid().getModelSet());
		JCheckBox[] items = new JCheckBox[modelList.size()];
		for (int i = 0; i < modelList.size(); i++) {
			items[i] = new JCheckBox(this.modelFeatures.getName(modelList
					.get(i)));
			items[i].setSelected(false);
		}
		JComboCheckBox jccb = new JComboCheckBox(items);
		rTop.add(jccb);
		rTop.setBorder(BorderFactory.createTitledBorder("Display options"));
		right.add(rTop, BorderLayout.NORTH);
		jccb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboCheckBox jccb = (JComboCheckBox) e.getSource();
				updateComponentList(jccb.getSelectedItems());
			}
		});

		this.jspRCenter = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		right.add(this.jspRCenter, BorderLayout.CENTER);
		this.center.add(right, BorderLayout.LINE_END);
		this.buttonReset();
		updateComponentList(jccb.getSelectedItems());
	}

	private JPanel getCompMiniPanel(String nodeID) {
		JPanel jp = new JPanel();
		jp.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		jp.add(this.mNode2Checkbox.get(nodeID), gbc);
		gbc.gridx = 1;
		gbc.gridy = 0;
		jp.add(this.mNode2Combobox.get(nodeID), gbc);
		gbc.gridx = 2;
		gbc.gridy = 0;
		jp.add(this.mNode2JButton.get(nodeID), gbc);
		return jp;
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

	private void updateComponentList(List<String> items) {
		this.jspRCenter.removeAll();
		this.mNode2ValueSelected.clear();
		this.lNodeInPanel.clear();

		List<LogicalModel> lModels = new ArrayList<LogicalModel>();
		for (String modelName : items) {
			lModels.add(this.modelFeatures.getModel(modelName));
		}

		// Proper components
		JPanel jpRRCTop = new JPanel();
		jpRRCTop.setLayout(new BoxLayout(jpRRCTop, BoxLayout.Y_AXIS));
		jpRRCTop.setBorder(BorderFactory
				.createTitledBorder("Proper components"));
		Set<String> sProperCompsFromSelectedModels = this.epithelium
				.getComponentFeatures().getModelsComponents(lModels, false);
		for (String nodeID : sProperCompsFromSelectedModels) {
			this.lNodeInPanel.add(nodeID);
			if (this.mNode2Checkbox.get(nodeID).isSelected()) {
				this.mNode2ValueSelected.put(nodeID, (Byte) this.mNode2Combobox
						.get(nodeID).getSelectedItem());
			}
			JPanel jpComp = this.getCompMiniPanel(nodeID);
			jpRRCTop.add(jpComp);
		}
		this.jspRCenter.add(jpRRCTop);

		// Input components
		JPanel jpRRCBottom = new JPanel();
		jpRRCBottom.setLayout(new BoxLayout(jpRRCBottom, BoxLayout.Y_AXIS));
		jpRRCBottom.setBorder(BorderFactory
				.createTitledBorder("Input components"));
		Set<String> sInputCompsFromSelectedModels = this.epithelium
				.getComponentFeatures().getModelsComponents(lModels, true);
		List<String> lEnvInputCompsFromSelectedModels = new ArrayList<String>();
		for (String nodeID : sInputCompsFromSelectedModels) {
			if (!this.epithelium.isIntegrationComponent(nodeID)) {
				lEnvInputCompsFromSelectedModels.add(nodeID);
			}
		}
		for (String nodeID : lEnvInputCompsFromSelectedModels) {
			this.lNodeInPanel.add(nodeID);
			if (this.mNode2Checkbox.get(nodeID).isSelected()) {
				this.mNode2ValueSelected.put(nodeID, (Byte) this.mNode2Combobox
						.get(nodeID).getSelectedItem());
			}
			JPanel jpComp = this.getCompMiniPanel(nodeID);
			jpRRCBottom.add(jpComp);
		}
		this.jspRCenter.add(jpRRCBottom);

		// Paint
		this.visualGridICs.paintComponent(this.visualGridICs.getGraphics());
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
				byte[] state = this.cellGridClone[x][y].getState();
				grid.setCellState(x, y, state);
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