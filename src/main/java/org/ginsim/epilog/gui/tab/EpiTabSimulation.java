package org.ginsim.epilog.gui.tab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.tree.TreePath;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;
import org.ginsim.epilog.ProjectModelFeatures;
import org.ginsim.epilog.core.Epithelium;
import org.ginsim.epilog.gui.widgets.JComboCheckBox;
import org.ginsim.epilog.gui.widgets.VisualGridSimulation;
import org.ginsim.epilog.io.ButtonFactory;

public class EpiTabSimulation extends EpiTab {
	private static final long serialVersionUID = 1394895739386499680L;

	private VisualGridSimulation visualGridSimulation;
	private Map<String, Boolean> mSelCheckboxes;
	private Map<String, JCheckBox> mNodeID2Checkbox;
	private ProjectModelFeatures modelFeatures;
	private List<String> lPresentComps;
	private List<String> lCompON;
	private Map<JButton, String> colorButton2Node;

	private JPanel jpRight;
	private JSplitPane jspLeft;
	private JPanel lRight;
	private JPanel lLeft;
	private JSplitPane jspRRCenter;

	public EpiTabSimulation(Epithelium e, TreePath path,
			ProjectModelFeatures modelFeatures) {
		super(e, path);
		this.modelFeatures = modelFeatures;
	}

	public void initialize() {
		setLayout(new BorderLayout());

		this.jpRight = new JPanel(new BorderLayout());
		this.add(this.jpRight, BorderLayout.CENTER);

		this.lCompON = new ArrayList<String>();
		this.mSelCheckboxes = new HashMap<String, Boolean>();
		this.mNodeID2Checkbox = new HashMap<String, JCheckBox>();
		this.colorButton2Node = new HashMap<JButton, String>();
		for (LogicalModel m : this.epithelium.getEpitheliumGrid().getModelSet()) {
			for (NodeInfo node : m.getNodeOrder()) {
				this.mSelCheckboxes.put(node.getNodeID(), false);
			}
		}
		this.visualGridSimulation = new VisualGridSimulation(this.epithelium
				.getEpitheliumGrid().getX(), this.epithelium
				.getEpitheliumGrid().getY(), this.epithelium
				.getEpitheliumGrid().getTopology(),
				this.epithelium.getComponentFeatures(),
				this.epithelium.getEpitheliumGrid(), this.lCompON);
		this.jpRight.add(this.visualGridSimulation, BorderLayout.CENTER);

		JPanel bLeft = new JPanel(new FlowLayout());
		JButton jbBack = new JButton("back");
		bLeft.add(jbBack);
		JButton jbForward = new JButton("fwr");
		bLeft.add(jbForward);
		JTextField jtSteps = new JTextField("30");
		bLeft.add(jtSteps);
		JButton jbFastFwr = new JButton("fstfwr");
		bLeft.add(jbFastFwr);
		JButton jbPicture = ButtonFactory
				.getImageNoBorder("screenshot-16.png");
		bLeft.add(jbPicture);
		this.jpRight.add(bLeft, BorderLayout.SOUTH);

		this.lRight = new JPanel(new BorderLayout());

		JPanel rlTop = new JPanel();
		rlTop.setLayout(new BoxLayout(rlTop, BoxLayout.Y_AXIS));
		JButton jbReset = new JButton("Reset");
		rlTop.add(jbReset);
		JButton jbClone = new JButton("Clone");
		rlTop.add(jbClone);
		this.lRight.add(rlTop, BorderLayout.PAGE_START);

		JPanel rlBottom = new JPanel();
		rlBottom.add(new JLabel("TODO: Analytics"));
		this.lRight.add(rlBottom, BorderLayout.CENTER);

		this.lLeft = new JPanel(new BorderLayout());

		JPanel rrTop = new JPanel();
		rrTop.setLayout(new BoxLayout(rrTop, BoxLayout.Y_AXIS));

		// Model combobox
		List<LogicalModel> modelList = new ArrayList<LogicalModel>(
				this.epithelium.getEpitheliumGrid().getModelSet());
		JCheckBox[] items = new JCheckBox[modelList.size()];
		for (int i = 0; i < modelList.size(); i++) {
			items[i] = new JCheckBox(this.modelFeatures.getName(modelList
					.get(i)));
			items[i].setSelected(false);
		}
		JComboCheckBox jccb = new JComboCheckBox(items);
		rrTop.add(jccb);

		// Select/Deselect buttons
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
				visualGridSimulation.paintComponent(visualGridSimulation
						.getGraphics());
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
				visualGridSimulation.paintComponent(visualGridSimulation
						.getGraphics());
			}
		});
		rrTopSel.add(jbDeselectAll);
		rrTop.add(rrTopSel);

		rrTop.setBorder(BorderFactory.createTitledBorder("Display options"));
		this.lLeft.add(rrTop, BorderLayout.NORTH);

		this.jspRRCenter = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		this.lLeft.add(jspRRCenter, BorderLayout.CENTER);
		jccb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboCheckBox jccb = (JComboCheckBox) e.getSource();
				updateComponentList(jccb.getSelectedItems());
			}
		});

		this.jspLeft = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, lLeft,
				lRight);

		this.add(this.jspLeft, BorderLayout.LINE_START);
		updateComponentList(jccb.getSelectedItems());
	}

	private void getCompMiniPanel(JPanel jp, GridBagConstraints gbc, int y,
			String nodeID) {
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
					visualGridSimulation.paintComponent(visualGridSimulation
							.getGraphics());
				}
			});
			this.mNodeID2Checkbox.put(nodeID, jcb);
		}
		jp.add(jcb, gbc);
		gbc.gridx = 1;
		JButton jbColor = new JButton();
		jbColor.setBackground(this.epithelium.getComponentFeatures()
				.getNodeColor(nodeID));
		jbColor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setNewColor((JButton) e.getSource());
			}
		});
		jp.add(jbColor, gbc);
		this.colorButton2Node.put(jbColor, nodeID);
	}

	private void setNewColor(JButton jb) {
		String nodeID = this.colorButton2Node.get(jb);
		Color newColor = JColorChooser.showDialog(jb, "Color chooser - "
				+ nodeID, jb.getBackground());
		if (newColor != null) {
			jb.setBackground(newColor);
			this.epithelium.getComponentFeatures().setNodeColor(nodeID,
					newColor);
			this.visualGridSimulation.paintComponent(this.visualGridSimulation
					.getGraphics());
		}
	}

	private void updateComponentList(List<String> items) {
		this.jspRRCenter.removeAll();
		this.lCompON.clear();
		this.colorButton2Node.clear();

		List<LogicalModel> lModels = new ArrayList<LogicalModel>();
		for (String modelName : items) {
			lModels.add(this.modelFeatures.getModel(modelName));
		}
		this.lPresentComps = new ArrayList<String>();

		// Proper components
		JPanel jpRRCTop = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(1, 5, 1, 0);
		jpRRCTop.setBorder(BorderFactory
				.createTitledBorder("Proper components"));
		Set<String> sProperCompsFromSelectedModels = this.epithelium
				.getComponentFeatures().getModelsComponents(lModels, false);
		int y = 0;
		for (String nodeID : sProperCompsFromSelectedModels) {
			this.lPresentComps.add(nodeID);
			if (mSelCheckboxes.get(nodeID)) {
				this.lCompON.add(nodeID);
			}
			this.getCompMiniPanel(jpRRCTop, gbc, y, nodeID);
			y++;
		}
		this.jspRRCenter.add(jpRRCTop);

		// Input components
		JPanel jpRRCBottom = new JPanel(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.insets = new Insets(1, 5, 1, 0);
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
		y = 0;
		for (String nodeID : lEnvInputCompsFromSelectedModels) {
			this.lPresentComps.add(nodeID);
			if (mSelCheckboxes.get(nodeID)) {
				this.lCompON.add(nodeID);
			}
			this.getCompMiniPanel(jpRRCBottom, gbc, y, nodeID);
			y++;
		}
		this.jspRRCenter.add(jpRRCBottom);
		this.visualGridSimulation.paintComponent(this.visualGridSimulation
				.getGraphics());
	}

	@Override
	public boolean canClose() {
		return true;
	}
}
