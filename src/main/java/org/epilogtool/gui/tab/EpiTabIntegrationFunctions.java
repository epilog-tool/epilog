package org.epilogtool.gui.tab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.tree.TreePath;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;
import org.epilogtool.core.ComponentIntegrationFunctions;
import org.epilogtool.core.Epithelium;
import org.epilogtool.core.EpitheliumIntegrationFunctions;
import org.epilogtool.gui.EpiGUI.EpiTabChanged;
import org.epilogtool.gui.EpiGUI.ProjectChangedInTab;
import org.epilogtool.gui.color.ColorUtils;
import org.epilogtool.project.ComponentPair;
import org.epilogtool.project.ProjectFeatures;

public class EpiTabIntegrationFunctions extends EpiTabDefinitions {
	private static final long serialVersionUID = -2124909766318378839L;

	private final int JTF_WIDTH = 30;

	private EpitheliumIntegrationFunctions userIntegrationFunctions;
	private String activeNodeID;
	private String activeModel;

	private Map<NodeInfo, JRadioButton> mNode2RadioButton;
	private JPanel jpNLBottom;
	private JPanel jpNRTop;
	private JPanel jpNRBottom;
	private JPanel jpNLTop;

	public EpiTabIntegrationFunctions(Epithelium e, TreePath path,
			ProjectChangedInTab projChanged, EpiTabChanged tabChanged,
			ProjectFeatures projectFeatures) {
		super(e, path, projChanged, tabChanged, projectFeatures);
		this.mNode2RadioButton = new HashMap<NodeInfo, JRadioButton>();
	}

	public void initialize() {
		this.center.setLayout(new BorderLayout());

		this.userIntegrationFunctions = this.epithelium
				.getIntegrationFunctions().clone();
		this.activeNodeID = null;

		// North Panel
		JPanel jpNorth = new JPanel(new BorderLayout());
		JPanel jpNLeft = new JPanel(new BorderLayout());
		jpNorth.add(jpNLeft, BorderLayout.LINE_START);

		// Model selection list
		List<LogicalModel> modelList = new ArrayList<LogicalModel>(
				this.epithelium.getEpitheliumGrid().getModelSet());
		JComboBox<String> jcbSBML = this.newModelCombobox(modelList);
		this.jpNLTop = new JPanel();
		this.jpNLTop.setBorder(BorderFactory
				.createTitledBorder("Model selection"));
		this.jpNLTop.add(jcbSBML);
		jpNLeft.add(this.jpNLTop, BorderLayout.NORTH);

		// Component selection list
		this.jpNLBottom = new JPanel(new GridBagLayout());
		this.jpNLBottom.setBorder(BorderFactory
				.createTitledBorder("Input components"));
		jpNLeft.add(this.jpNLBottom, BorderLayout.CENTER);

		JPanel jpNRight = new JPanel(new BorderLayout());
		jpNorth.add(jpNRight, BorderLayout.CENTER);
		this.center.add(jpNorth, BorderLayout.NORTH);

		this.jpNRTop = new JPanel(new FlowLayout());
		jpNRight.add(this.jpNRTop, BorderLayout.NORTH);
		this.jpNRBottom = new JPanel(new GridBagLayout());
		jpNRight.add(this.jpNRBottom, BorderLayout.CENTER);

		// South Panel
		// JPanel jpSouth = new JPanel(new BorderLayout());
		// jpSouth.add(new JLabel("Explanation missing"));
		this.center.add(this.getExplanationPanel(), BorderLayout.SOUTH);
		this.activeModel = (String) jcbSBML.getSelectedItem();
		this.updateComponentList();
		this.isInitialized = true;
	}

	private JEditorPane getExplanationPanel() {
		JEditorPane jPane = new JEditorPane();
		jPane.setContentType("text/html");
		jPane.setEditable(false);
		jPane.setEnabled(true);
		jPane.setBackground(this.getBackground());
		String s = "<html><head>\n";
		s += "<style type=\"text/css\">\n";
		// s+="td { font-family:Courier; }\n";
		// s+="b { font-family:Courier; }\n";
		s += "</style>\n";
		s += "</head><body>\n";
		s += "<b>Grammar specification examples</b><br/>\n";
		s += "<table border=1>";
		s += "<tr><td>G<sub>0</sub>(1,1,4,1)</td><td>at least 1 and at most 4 "
				+ "neighbours at distance 1 have G<sub>0</sub> at least at "
				+ "level 1</td></tr>";
		s += "<tr><td>G<sub>0</sub>(1,1,_,1)</td><td>at least 1 neighbour at "
				+ "distance 1 has G<sub>0</sub> at least at level 1</td></tr>";
		s += "<tr><td>G<sub>0</sub>(1,1,_,1) | G<sub>0</sub>(2,3,_,1)</td><td>"
				+ "at least 1 neighbour at distance 1 has G<sub>0</sub> at least "
				+ "at level 1 <b>OR</b> at least 3 neighbours at distance 1 have "
				+ "G<sub>0</sub> at least at level 2</td></tr>";
		s += "<tr><td>G<sub>0</sub>(_,1,_,2:3)</td><td>at least 1 neighbour at "
				+ "distance 2 or 3 has G<sub>0</sub> at its maximum level</td></tr>";
		s += "</table></font></body>";
		jPane.setText(s);
		return jPane;
	}

	private JComboBox<String> newModelCombobox(List<LogicalModel> modelList) {
		// Model selection list
		String[] saSBML = new String[modelList.size()];
		for (int i = 0; i < modelList.size(); i++) {
			saSBML[i] = this.projectFeatures.getModelName(modelList.get(i));
		}
		JComboBox<String> jcb = new JComboBox<String>(saSBML);
		jcb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unchecked")
				JComboBox<String> jcb = (JComboBox<String>) e.getSource();
				activeModel = (String) jcb.getSelectedItem();
				updateComponentList();
				// Re-Paint
				getParent().repaint();
			}
		});
		ButtonGroup group = new ButtonGroup();
		for (LogicalModel m : modelList) {
			for (NodeInfo node : m.getNodeOrder()) {
				if (!node.isInput())
					continue;
				String nodeID = node.getNodeID();
				JRadioButton jrb;
				if (this.mNode2RadioButton.containsKey(nodeID)) {
					jrb = this.mNode2RadioButton.get(nodeID);
				} else {
					jrb = new JRadioButton(nodeID);
					jrb.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							JRadioButton jrb = (JRadioButton) e.getSource();
							activeNodeID = jrb.getText();
							updateNodeID();
							// Re-Paint
							getParent().repaint();
						}
					});
					this.mNode2RadioButton.put(node, jrb);
				}
				group.add(jrb);
			}
		}
		return jcb;
	}

	private void updateNodeID() {
		this.jpNRTop.removeAll();
		ButtonGroup group = new ButtonGroup();
		this.jpNRTop.add(new JLabel(this.activeNodeID + ": "));
		JRadioButton jrEnv = new JRadioButton("Environment");
		jrEnv.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				paintEnvironmentPanel();
				// Re-Paint
				getParent().repaint();
			}
		});
		group.add(jrEnv);
		this.jpNRTop.add(jrEnv);
		JRadioButton jrInt = new JRadioButton("Integration");
		jrInt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				paintIntegrationPanel();
				// Re-Paint
				getParent().repaint();
			}
		});
		group.add(jrInt);
		this.jpNRTop.add(jrInt);
		LogicalModel m = this.epithelium.getComponentFeatures().getModel(
				this.activeModel);
		NodeInfo node = this.epithelium.getComponentFeatures().getNodeInfo(
				this.activeNodeID, m);
		if (this.userIntegrationFunctions
				.containsComponentPair(new ComponentPair(m, node))) {
			jrInt.setSelected(true);
			paintIntegrationPanel();
		} else {
			jrEnv.setSelected(true);
			paintEnvironmentPanel();
		}
	}

	private NodeInfo getActiveNodeInfo() {
		LogicalModel m = this.epithelium.getComponentFeatures().getModel(
				this.activeModel);
		return this.epithelium.getComponentFeatures().getNodeInfo(
				this.activeNodeID, m);
	}

	private void paintIntegrationPanel() {
		// GUI
		this.jpNRBottom.removeAll();

		LogicalModel m = this.projectFeatures.getModel(this.activeModel);
		ComponentPair cp = new ComponentPair(m, this.getActiveNodeInfo());
		System.out.println("paintPanel: " + cp);
		if (!this.userIntegrationFunctions.containsComponentPair(cp)) {
			this.userIntegrationFunctions.addComponent(cp);
		}
		ComponentIntegrationFunctions cfi = this.userIntegrationFunctions
				.getComponentIntegrationFunctions(cp);

		List<String> functions = cfi.getFunctions();
		GridBagConstraints gbc = new GridBagConstraints();
		for (int i = 0; i < functions.size(); i++) {
			gbc.gridy = i;
			gbc.ipady = 5;
			gbc.insets.bottom = 5;
			gbc.gridx = 0;
			gbc.anchor = GridBagConstraints.WEST;
			this.jpNRBottom.add(new JLabel("Level " + (i + 1) + " "), gbc);
			gbc.gridx = 1;
			JTextField jtf = new JTextField(functions.get(i));
			jtf.setToolTipText("" + (i + 1));
			jtf.addKeyListener(new KeyListener() {
				@Override
				public void keyTyped(KeyEvent e) {
				}

				@Override
				public void keyReleased(KeyEvent e) {
					JTextField jtf = (JTextField) e.getSource();
					byte value = Byte.parseByte(jtf.getToolTipText());

					setIntegrationFunction(value, jtf.getText());
					validateIntegrationFunction(jtf);
				}

				@Override
				public void keyPressed(KeyEvent e) {
				}
			});
			jtf.setColumns(this.JTF_WIDTH);
			this.jpNRBottom.add(jtf, gbc);
		}
	}

	private void setIntegrationFunction(byte level, String function) {
		LogicalModel m = this.projectFeatures.getModel(this.activeModel);
		ComponentPair cp = new ComponentPair(m, this.getActiveNodeInfo());
		ComponentIntegrationFunctions cif = this.userIntegrationFunctions
				.getComponentIntegrationFunctions(cp);
		cif.setFunctionAtLevel(level, function);
	}

	private void validateIntegrationFunction(JTextField jtf) {
		LogicalModel m = this.projectFeatures.getModel(this.activeModel);
		ComponentPair cp = new ComponentPair(m, this.getActiveNodeInfo());
		ComponentIntegrationFunctions cif = this.userIntegrationFunctions
				.getComponentIntegrationFunctions(cp);
		byte value = Byte.parseByte(jtf.getToolTipText());
		if (jtf.getText().trim().isEmpty() || cif.isValidAtLevel(value)) {
			jtf.setBackground(Color.WHITE);
		} else {
			jtf.setBackground(ColorUtils.LIGHT_RED);
		}
	}

	private void paintEnvironmentPanel() {
		LogicalModel m = this.projectFeatures.getModel(this.activeModel);
		ComponentPair cp = new ComponentPair(m, this.getActiveNodeInfo());
		this.userIntegrationFunctions.removeComponent(cp);
		this.jpNRBottom.removeAll();
	}

	private void updateComponentList() {
		this.jpNLBottom.removeAll();
		LogicalModel m = this.projectFeatures.getModel(this.activeModel);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(1, 5, 1, 0);
		Set<NodeInfo> sInputs = this.epithelium.getComponentFeatures()
				.getModelNodeInfos(m, true);
		List<NodeInfo> lInputs = new ArrayList<NodeInfo>(sInputs);
		Collections.sort(lInputs, new Comparator<NodeInfo>() {
			public int compare(NodeInfo s1, NodeInfo s2) {
				return s1.getNodeID().compareToIgnoreCase(s2.getNodeID());
			}
		});
		int y = 0;
		for (NodeInfo node : lInputs) {
			if (y == 0) {
				this.activeNodeID = node.getNodeID();
				updateNodeID();
				this.mNode2RadioButton.get(node).setSelected(true);
			}
			gbc.gridy = y++;
			gbc.gridx = 0;
			gbc.anchor = GridBagConstraints.WEST;
			this.jpNLBottom.add(this.mNode2RadioButton.get(node), gbc);
		}
	}

	@Override
	protected void buttonReset() {
		this.userIntegrationFunctions = this.epithelium
				.getIntegrationFunctions().clone();
		this.updateNodeID();
		// Repaint
		this.getParent().repaint();
	}

	@Override
	protected void buttonAccept() {
		for (NodeInfo node : mNode2RadioButton.keySet()) {
			ComponentPair cp = new ComponentPair(this.projectFeatures.
					getModel(this.activeModel), node);
			ComponentIntegrationFunctions cifClone = this.userIntegrationFunctions
					.getComponentIntegrationFunctions(cp);
			EpitheliumIntegrationFunctions eifOrig = this.epithelium
					.getIntegrationFunctions();
			if (cifClone == null) {
				eifOrig.removeComponent(cp);
			} else {
				eifOrig.addComponent(cp);
				for (byte i = 1; i <= node.getMax(); i++) {
					eifOrig.getComponentIntegrationFunctions(cp)
							.setFunctionAtLevel(i,
									cifClone.getFunctions().get(i - 1));
				}
			}
		}
	}

	@Override
	protected boolean isChanged() {
		for (NodeInfo node : mNode2RadioButton.keySet()) {
			ComponentPair cp = new ComponentPair(this.projectFeatures.
					getModel(this.activeModel), node);
			ComponentIntegrationFunctions cifClone = this.userIntegrationFunctions
					.getComponentIntegrationFunctions(cp);
			ComponentIntegrationFunctions cifOrig = this.epithelium
					.getIntegrationFunctions()
					.getComponentIntegrationFunctions(cp);
			if (cifClone == null && cifOrig == null)
				continue;
			if (cifClone == null && cifOrig != null || cifClone != null
					&& cifOrig == null)
				return true;
			if (!cifOrig.equals(cifClone))
				return true;
		}
		return false;
	}

	@Override
	public void notifyChange() {
		if (!this.isInitialized)
			return;
		List<LogicalModel> modelList = new ArrayList<LogicalModel>(
				this.epithelium.getEpitheliumGrid().getModelSet());
		EpitheliumIntegrationFunctions epiFunc = new EpitheliumIntegrationFunctions();
		for (LogicalModel m : modelList) {
			for (NodeInfo node : m.getNodeOrder()) {
				ComponentPair cp = new ComponentPair(m, node);
				if (this.userIntegrationFunctions.containsComponentPair(cp)) {
					// Already exists
					epiFunc.addComponentFunctions(cp,
							this.userIntegrationFunctions
									.getComponentIntegrationFunctions(cp));
				} else {
					// Adds a new one
					epiFunc.addComponent(cp);
				}
			}
		}
		this.jpNLTop.removeAll();
		this.jpNLTop.add(this.newModelCombobox(modelList));
		this.activeModel = this.projectFeatures.getModelName(modelList.get(0));
		this.updateComponentList();
	}
}