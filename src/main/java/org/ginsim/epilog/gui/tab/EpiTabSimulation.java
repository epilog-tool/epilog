package org.ginsim.epilog.gui.tab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
import org.ginsim.epilog.io.ButtonImageLoader;

public class EpiTabSimulation extends EpiTab {
	private static final long serialVersionUID = 1394895739386499680L;

	private VisualGridSimulation visualGridSimulation;
	private Map<String, Boolean> mSelCheckboxes;
	private ProjectModelFeatures modelFeatures;
	private List<String> lPresentComps;
	private List<String> lCompON;
	private Map<JButton, String> colorButton2Node;

	private JPanel left;
	private JSplitPane right;
	private JPanel rLeft;
	private JPanel rRight;
	private JSplitPane jspRRCenter;

	public EpiTabSimulation(Epithelium e, TreePath path,
			ProjectModelFeatures modelFeatures) {
		super(e, path);
		this.modelFeatures = modelFeatures;
	}

	public void initialize() {
		setLayout(new BorderLayout());

		this.left = new JPanel(new BorderLayout());
		this.add(this.left, BorderLayout.CENTER);

		this.lCompON = new ArrayList<String>();
		this.mSelCheckboxes = new HashMap<String, Boolean>();
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
		this.left.add(this.visualGridSimulation, BorderLayout.CENTER);

		JPanel bLeft = new JPanel(new FlowLayout());
		JButton jbBack = new JButton("back");
		bLeft.add(jbBack);
		JButton jbForward = new JButton("fwr");
		bLeft.add(jbForward);
		JTextField jtSteps = new JTextField("30");
		bLeft.add(jtSteps);
		JButton jbFastFwr = new JButton("fstfwr");
		bLeft.add(jbFastFwr);
		JButton jbPicture = ButtonImageLoader
				.newButtonNoBorder("screenshot-16.png");
		bLeft.add(jbPicture);
		this.left.add(bLeft, BorderLayout.SOUTH);

		this.rLeft = new JPanel(new BorderLayout());

		JPanel rlTop = new JPanel();
		rlTop.setLayout(new BoxLayout(rlTop, BoxLayout.Y_AXIS));
		JButton jbReset = new JButton("Reset");
		rlTop.add(jbReset);
		JButton jbClone = new JButton("Clone");
		rlTop.add(jbClone);
		this.rLeft.add(rlTop, BorderLayout.PAGE_START);

		JPanel rlBottom = new JPanel();
		rlBottom.add(new JLabel("TODO: Analytics"));
		this.rLeft.add(rlBottom, BorderLayout.CENTER);

		this.rRight = new JPanel(new BorderLayout());

		JPanel rrTop = new JPanel();
		rrTop.setLayout(new BoxLayout(rrTop, BoxLayout.Y_AXIS));

		JPanel rrTopSel = new JPanel();
		rrTopSel.setLayout(new BoxLayout(rrTopSel, BoxLayout.X_AXIS));
		JButton jbSelectAll = new JButton("SelectAll");
		rrTopSel.add(jbSelectAll);
		JButton jbDeselectAll = new JButton("DeselectAll");
		rrTopSel.add(jbDeselectAll);
		rrTop.add(rrTopSel);

		// TODO: clean afterwards
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
		rrTop.setBorder(BorderFactory.createTitledBorder("Display options"));
		this.rRight.add(rrTop, BorderLayout.NORTH);

		this.jspRRCenter = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		this.rRight.add(jspRRCenter, BorderLayout.CENTER);
		jccb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboCheckBox jccb = (JComboCheckBox) e.getSource();
				updateComponentList(jccb.getSelectedItems());
			}
		});

		this.right = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, rLeft, rRight);

		this.add(this.right, BorderLayout.LINE_END);
		updateComponentList(jccb.getSelectedItems());
	}

	private JPanel getCompMiniPanel(String nodeID) {
		JPanel jp = new JPanel();
		jp.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		JCheckBox jcb = new JCheckBox(nodeID, mSelCheckboxes.get(nodeID));
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
		jp.add(jcb, gbc);
		gbc.gridx = 1;
		gbc.gridy = 0;
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
		return jp;
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
		JPanel jpRRCTop = new JPanel();
		jpRRCTop.setLayout(new BoxLayout(jpRRCTop, BoxLayout.Y_AXIS));
		jpRRCTop.setBorder(BorderFactory
				.createTitledBorder("Proper components"));
		Set<String> sProperCompsFromSelectedModels = this.epithelium
				.getComponentFeatures().getModelsComponents(lModels, false);
		for (String nodeID : sProperCompsFromSelectedModels) {
			this.lPresentComps.add(nodeID);
			if (mSelCheckboxes.get(nodeID)) {
				this.lCompON.add(nodeID);
			}
			JPanel jpComp = this.getCompMiniPanel(nodeID);
			jpRRCTop.add(jpComp);
		}
		this.jspRRCenter.add(jpRRCTop);

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
			this.lPresentComps.add(nodeID);
			if (mSelCheckboxes.get(nodeID)) {
				this.lCompON.add(nodeID);
			}
			JPanel jpComp = this.getCompMiniPanel(nodeID);
			jpRRCBottom.add(jpComp);
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
