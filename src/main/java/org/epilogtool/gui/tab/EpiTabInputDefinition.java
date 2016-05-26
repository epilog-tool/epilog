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
import org.epilogtool.common.ObjectComparator;
import org.epilogtool.core.ComponentIntegrationFunctions;
import org.epilogtool.core.Epithelium;
import org.epilogtool.core.EpitheliumEnvironmentalInputs;
import org.epilogtool.core.EpitheliumIntegrationFunctions;
import org.epilogtool.gui.EpiGUI.EpiTabChanged;
import org.epilogtool.gui.EpiGUI.ProjectChangedInTab;
import org.epilogtool.gui.color.ColorUtils;
import org.epilogtool.gui.widgets.JComboWideBox;
import org.epilogtool.project.ComponentPair;
import org.epilogtool.project.ProjectFeatures;

public class EpiTabInputDefinition extends EpiTabDefinitions {
	private static final long serialVersionUID = -2124909766318378839L;

	private final int JTF_WIDTH = 30;

	private EpitheliumIntegrationFunctions userIntegrationFunctions;
	private EpitheliumEnvironmentalInputs environmentalInputs;
	private String activeNodeID;
	private String activeModel;

	private Map<NodeInfo, JRadioButton> mNode2RadioButton;
	private JPanel jpNLBottom;
	private JPanel jpNRTop;
	private JPanel jpNRBottom;
	private JPanel jpNLTop;

	public EpiTabInputDefinition(Epithelium e, TreePath path,
			ProjectChangedInTab projChanged, EpiTabChanged tabChanged,
			ProjectFeatures projectFeatures) {
		super(e, path, projChanged, tabChanged, projectFeatures);
		this.mNode2RadioButton = new HashMap<NodeInfo, JRadioButton>();
	}

	public void initialize() {
		this.center.setLayout(new BorderLayout());

		this.userIntegrationFunctions = this.epithelium
				.getIntegrationFunctions().clone();
		this.environmentalInputs = this.epithelium.getEnvironmentalInputs().clone();
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

		this.activeModel = (String) jcbSBML.getSelectedItem();
		this.updateComponentList();
		this.isInitialized = true;
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
		this.jpNRBottom.removeAll();
		ButtonGroup group = new ButtonGroup();
		this.jpNRTop.add(new JLabel(this.activeNodeID + ": "));
		JRadioButton jrModelInput = new JRadioButton("Model Input");
		jrModelInput.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				paintModelInputPanel();
				// Re-Paint
				getParent().repaint();
			}
		});
		group.add(jrModelInput);
		this.jpNRTop.add(jrModelInput);
		JRadioButton jrModelInt = new JRadioButton("Model Integration");
		jrModelInt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				paintModelIntegrationPanel();
				// Re-Paint
				getParent().repaint();
			}
		});
		group.add(jrModelInt);
		this.jpNRTop.add(jrModelInt);
		JRadioButton jrEnv = new JRadioButton("Epithelium Environmental");
		jrEnv.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				paintEpitheliumInputPanel();
				// Re-Paint
				getParent().repaint();
			}
		});
		group.add(jrEnv);
		this.jpNRTop.add(jrEnv);
		LogicalModel m = this.epithelium.getProjectFeatures().getModel(
				this.activeModel);
		NodeInfo node = this.epithelium.getProjectFeatures().getNodeInfo(
				this.activeNodeID, m);
		if (this.userIntegrationFunctions
				.containsComponentPair(new ComponentPair(m, node))) {
			jrModelInt.setSelected(true);
			paintModelIntegrationPanel();
		} else if (this.environmentalInputs.containsComponent(new ComponentPair(m, node))) {
			jrEnv.setSelected(true);
		}else {
			jrModelInput.setSelected(true);
			paintModelInputPanel();
		}
	}

	private NodeInfo getActiveNodeInfo() {
		LogicalModel m = this.epithelium.getProjectFeatures().getModel(
				this.activeModel);
		return this.epithelium.getProjectFeatures().getNodeInfo(
				this.activeNodeID, m);
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

	private void paintModelInputPanel() {
		LogicalModel m = this.projectFeatures.getModel(this.activeModel);
		ComponentPair cp = new ComponentPair(m, this.getActiveNodeInfo());
		this.userIntegrationFunctions.removeComponent(cp);
		this.environmentalInputs.removeComponent(cp);
		this.jpNRBottom.removeAll();
	}
	
	private void paintEpitheliumInputPanel() {
		LogicalModel m = this.projectFeatures.getModel(this.activeModel);
		ComponentPair cp = new ComponentPair(m, this.getActiveNodeInfo());
		this.environmentalInputs.addComponent(cp);
		this.userIntegrationFunctions.removeComponent(cp);
		this.jpNRBottom.removeAll();
	}
	
	private void paintModelIntegrationPanel() {
		// GUI
		this.jpNRBottom.removeAll();

		LogicalModel m = this.projectFeatures.getModel(this.activeModel);
		ComponentPair cp = new ComponentPair(m, this.getActiveNodeInfo());
		if (!this.userIntegrationFunctions.containsComponentPair(cp)) {
			this.userIntegrationFunctions.addComponent(cp);
			this.environmentalInputs.removeComponent(cp);
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


	private void updateComponentList() {
		this.jpNLBottom.removeAll();
		this.jpNLBottom.setVisible(true);
		LogicalModel m = this.projectFeatures.getModel(this.activeModel);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(1, 5, 1, 0);
		Set<NodeInfo> sInputs = this.epithelium.getProjectFeatures()
				.getModelNodeInfos(m, true);
		
		if (sInputs.size()==0){
			this.getNoInputTextField();
		} else {
			List<NodeInfo> lInputs = new ArrayList<NodeInfo>(sInputs);
			Collections.sort(lInputs, ObjectComparator.NODE_INFO);
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
	}
	
	private void getNoInputTextField(){
		this.jpNRBottom.removeAll();
		this.jpNRTop.removeAll();
		this.jpNLBottom.setVisible(false);
		this.activeNodeID = null;
		JEditorPane jEmptyInputPane = new JEditorPane();
		jEmptyInputPane.setContentType("text/html");
		jEmptyInputPane.setEditable(false);
		jEmptyInputPane.setEnabled(true);
		jEmptyInputPane.setBackground(this.jpNRBottom.getBackground());
		jEmptyInputPane.setText("There are no Input nodes in this Model");
		this.jpNRBottom.add(jEmptyInputPane);
	}

	@Override
	protected void buttonReset() {
		this.userIntegrationFunctions = this.epithelium
				.getIntegrationFunctions().clone();
		this.environmentalInputs = this.epithelium
				.getEnvironmentalInputs().clone();
		this.updateNodeID();
		// Repaint
		this.getParent().repaint();
	}

	@Override
	protected void buttonAccept() {
		for (NodeInfo node : mNode2RadioButton.keySet()) {
			ComponentPair cp = new ComponentPair(
					this.projectFeatures.getModel(this.activeModel), node);
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
			if (this.environmentalInputs.containsComponent(cp) && 
					!this.epithelium.getEnvironmentalInputs().containsComponent(cp)) {
				this.epithelium.getEnvironmentalInputs().addComponent(cp);
				this.epithelium.getEpitheliumGrid().addGridEnvironment(cp);
			}
			if (!this.environmentalInputs.containsComponent(cp) &&
					this.epithelium.getEnvironmentalInputs().containsComponent(cp)) {
				this.epithelium.getEnvironmentalInputs().removeComponent(cp);
				this.epithelium.getEpitheliumGrid().removeGridEnvironment(cp);
			}
		}
	}

	@Override
	protected boolean isChanged() {
		for (NodeInfo node : mNode2RadioButton.keySet()) {
			ComponentPair cp = new ComponentPair(
					this.projectFeatures.getModel(this.activeModel), node);
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
		if (!this.environmentalInputs.getAllEnvironmentalComponents()
				.equals(this.epithelium.getEnvironmentalInputs()
						.getAllEnvironmentalComponents())) 
			return true;
		return false;
	}

	@Override
	public void applyChange() {
		//FIXME: if a model is no longer in the epi, should we still save its 
		//input definitions?
		List<LogicalModel> modelList = new ArrayList<LogicalModel>(
				this.epithelium.getEpitheliumGrid().getModelSet());
		this.jpNLTop.removeAll();
		this.jpNLTop.add(this.newModelCombobox(modelList));
		this.activeModel = this.projectFeatures.getModelName(modelList.get(0));
		this.updateComponentList();
	}
}
