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

import org.antlr.runtime.RecognitionException;
import org.colomoto.biolqm.LogicalModel;
import org.colomoto.biolqm.NodeInfo;
import org.epilogtool.common.ObjectComparator;
import org.epilogtool.core.ComponentIntegrationFunctions;
import org.epilogtool.core.Epithelium;
import org.epilogtool.core.EpitheliumIntegrationFunctions;
import org.epilogtool.gui.EpiGUI.ProjChangeNotifyTab;
import org.epilogtool.gui.EpiGUI.TabChangeNotifyProj;
import org.epilogtool.gui.color.ColorUtils;
import org.epilogtool.gui.dialog.DialogMessage;
import org.epilogtool.gui.widgets.JComboWideBox;
import org.epilogtool.project.ComponentPair;
import org.epilogtool.project.Project;

public class EpiTabInputDefinition extends EpiTabDefinitions {
	private static final long serialVersionUID = -2124909766318378839L;

	private final int JTF_WIDTH = 30;

	private EpitheliumIntegrationFunctions userIntegrationFunctions;
	private String activeNodeID;
	private String activeModel;
	private TabProbablyChanged tpc;

	private Map<NodeInfo, JRadioButton> mNode2RadioButton;
	private JPanel jpNLBottom;
	private JPanel jpNRTop;
	private JPanel jpNRBottom;
	private JPanel jpNLTop;

	public EpiTabInputDefinition(Epithelium e, TreePath path, ProjChangeNotifyTab projChanged,
			TabChangeNotifyProj tabChanged) {
		super(e, path, projChanged, tabChanged);
		this.mNode2RadioButton = new HashMap<NodeInfo, JRadioButton>();
	}

	public void initialize() {
		this.center.setLayout(new BorderLayout());

		this.userIntegrationFunctions = this.epithelium.getIntegrationFunctions().clone();
		this.activeNodeID = null;
		this.tpc = new TabProbablyChanged();

		// North Panel
		JPanel jpNorth = new JPanel(new BorderLayout());
		JPanel jpNLeft = new JPanel(new BorderLayout());
		jpNorth.add(jpNLeft, BorderLayout.LINE_START);

		// Model selection list
		JComboBox<String> jcbSBML = this.newModelCombobox(this.epithelium.getEpitheliumGrid().getModelSet());
		this.jpNLTop = new JPanel();
		this.jpNLTop.setBorder(BorderFactory.createTitledBorder("Model Selection"));
		this.jpNLTop.add(jcbSBML);
		jpNLeft.add(this.jpNLTop, BorderLayout.NORTH);

		// Component selection list
		this.jpNLBottom = new JPanel(new GridBagLayout());
		this.jpNLBottom.setBorder(BorderFactory.createTitledBorder("Input Component"));
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

	private JComboBox<String> newModelCombobox(Set<LogicalModel> sModels) {
		// Model selection list
		String[] saSBML = new String[sModels.size()];
		int i = 0;
		for (LogicalModel m : sModels) {
			saSBML[i++] = Project.getInstance().getProjectFeatures().getModelName(m);
		}
		JComboBox<String> jcb = new JComboWideBox<String>(saSBML);
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
		for (LogicalModel m : sModels) {
			for (NodeInfo node : m.getComponents()) {
				if (!node.isInput())
					continue;
				JRadioButton jrb;
				if (this.mNode2RadioButton.containsKey(node)) {
					jrb = this.mNode2RadioButton.get(node);
				} else {
					jrb = new JRadioButton(node.getNodeID());
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

	/**
	 * Reaction function to the change of the selected input to defined as either
	 * Positional or Integration. 1) By default an input is positional, if it is
	 * already defined as an integration, the integration functions immediately
	 * appear. 2) Inputs are defined locally, i.e. if more than one model has an
	 * input with the same name, functions must be defined for each. 3) Same name
	 * inputs (from different models) may be positional in one and integration in
	 * another
	 */
	private void updateNodeID() {
		this.jpNRTop.removeAll();
		this.jpNRBottom.removeAll();
		ButtonGroup group = new ButtonGroup();
		this.jpNRTop.add(new JLabel(this.activeNodeID + ": "));
		JRadioButton jrModelInput = new JRadioButton("Positional Input");
		jrModelInput.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				paintModelInputPanel();
				tpc.setChanged();
				// Re-Paint
				getParent().repaint();
			}
		});
		group.add(jrModelInput);
		this.jpNRTop.add(jrModelInput);
		JRadioButton jrModelInt = new JRadioButton("Integration Input");
		jrModelInt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				paintModelIntegrationPanel();
				tpc.setChanged();
				// Re-Paint
				getParent().repaint();
			}
		});
		group.add(jrModelInt);
		this.jpNRTop.add(jrModelInt);
		LogicalModel m = Project.getInstance().getProjectFeatures().getModel(this.activeModel);
		NodeInfo node = Project.getInstance().getProjectFeatures().getNodeInfo(this.activeNodeID, m);
		if (this.userIntegrationFunctions.containsComponentPair(new ComponentPair(m, node))) {
			jrModelInt.setSelected(true);
			paintModelIntegrationPanel();
		} else {
			jrModelInput.setSelected(true);
			paintModelInputPanel();
		}
	}

	/**
	 * Returns the input selected in the radio button, given the selected model
	 * 
	 * @return
	 */
	private NodeInfo getActiveNodeInfo() {
		LogicalModel m = Project.getInstance().getProjectFeatures().getModel(this.activeModel);
		return Project.getInstance().getProjectFeatures().getNodeInfo(this.activeNodeID, m);
	}

	/**
	 * Sets the integration function (IF) defined in the integration function box
	 * assigned to a component pair (model, node). Only IF well written are
	 * assigned.
	 * 
	 * @param level
	 * @param function
	 * @throws RecognitionException
	 * @throws RuntimeException
	 */
	private void setIntegrationFunction(byte level, String function) throws RecognitionException, RuntimeException {
		LogicalModel m = Project.getInstance().getProjectFeatures().getModel(this.activeModel);
		ComponentPair cp = new ComponentPair(m, this.getActiveNodeInfo());
		ComponentIntegrationFunctions cif = this.userIntegrationFunctions.getComponentIntegrationFunctions(cp);
		cif.setFunctionAtLevel(level, function);
	}

	/**
	 * Integration Function (IF) validation function. If the IF is well written, the
	 * text box is white, otherwise Red. Badly written IF are accepted, but the
	 * system just erases it.
	 * 
	 * The value is the level of the function. The toolTip is used as a "shortcut"
	 * to identify the level of the function
	 * 
	 * @param jtf
	 */
	private void validateTextField(JTextField jtf) {
		byte value = Byte.parseByte(jtf.getToolTipText());
		try {
			setIntegrationFunction(value, jtf.getText());
			jtf.setBackground(Color.WHITE);
		} catch (RecognitionException re) {
			jtf.setBackground(ColorUtils.LIGHT_RED);
		} catch (RuntimeException re) {
			jtf.setBackground(ColorUtils.LIGHT_RED);
		}

		LogicalModel m = Project.getInstance().getProjectFeatures().getModel(this.activeModel);
		ComponentPair cp = new ComponentPair(m, this.getActiveNodeInfo());
		ComponentIntegrationFunctions cfi = this.epithelium.getIntegrationFunctionsForComponent(cp);
		if (cfi != null) {
			List<String> lFunctions = cfi.getFunctions();
			if (lFunctions.size() >= value) {
				if (jtf.getText().equals(lFunctions.get(value - 1))) {
					return;
				}
			}
		}
		tpc.setChanged();
	}

	private void updateComponentList() {
		this.jpNLBottom.removeAll();
		this.jpNLBottom.setVisible(true);
		LogicalModel m = Project.getInstance().getProjectFeatures().getModel(this.activeModel);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(1, 5, 1, 0);
		Set<NodeInfo> sInputs = Project.getInstance().getProjectFeatures().getModelNodeInfos(m, true);

		if (sInputs.size() == 0) {
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

	private void getNoInputTextField() {
		this.jpNRBottom.removeAll();
		this.jpNRTop.removeAll();
		this.jpNLBottom.setVisible(false);
		this.activeNodeID = null;
		JEditorPane jEmptyInputPane = new JEditorPane();
		jEmptyInputPane.setContentType("text/html");
		jEmptyInputPane.setEditable(false);
		jEmptyInputPane.setEnabled(true);
		jEmptyInputPane.setBackground(this.jpNRBottom.getBackground());
		jEmptyInputPane.setText("There are no Input Components in this Model");
		this.jpNRBottom.add(jEmptyInputPane);
	}

	private void paintModelInputPanel() {
		LogicalModel m = Project.getInstance().getProjectFeatures().getModel(this.activeModel);
		ComponentPair cp = new ComponentPair(m, this.getActiveNodeInfo());
		this.userIntegrationFunctions.removeComponent(cp);
		this.jpNRBottom.removeAll();
	}

	private void paintModelIntegrationPanel() {
		// GUI
		this.jpNRBottom.removeAll();

		LogicalModel m = Project.getInstance().getProjectFeatures().getModel(this.activeModel);
		ComponentPair cp = new ComponentPair(m, this.getActiveNodeInfo());
		if (!this.userIntegrationFunctions.containsComponentPair(cp)) {
			this.userIntegrationFunctions.addComponent(cp);
		}
		ComponentIntegrationFunctions cfi = this.userIntegrationFunctions.getComponentIntegrationFunctions(cp);

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
					validateTextField(jtf);
				}

				@Override
				public void keyPressed(KeyEvent e) {
				}
			});
			jtf.setColumns(this.JTF_WIDTH);
			this.validateTextField(jtf);
			this.jpNRBottom.add(jtf, gbc);
		}
	}

	@Override
	protected void buttonReset() {
		this.userIntegrationFunctions = this.epithelium.getIntegrationFunctions().clone();
		this.updateNodeID();
		// Repaint
		this.getParent().repaint();
	}

	@Override
	protected void buttonAccept() {
		allNodes: for (NodeInfo node : mNode2RadioButton.keySet()) {
			ComponentPair cp = new ComponentPair(Project.getInstance().getProjectFeatures().getModel(this.activeModel),
					node);
			ComponentIntegrationFunctions cifClone = this.userIntegrationFunctions.getComponentIntegrationFunctions(cp);
			EpitheliumIntegrationFunctions eifOrig = this.epithelium.getIntegrationFunctions();
			if (cifClone == null) {
				eifOrig.removeComponent(cp);
			} else {
				eifOrig.addComponent(cp);
				for (byte i = 1; i <= node.getMax(); i++) {
					try {
						eifOrig.getComponentIntegrationFunctions(cp).setFunctionAtLevel(i,
								cifClone.getFunctions().get(i - 1));
					} catch (RecognitionException re) {
					} catch (RuntimeException re) {
						DialogMessage.showError(this, "Integration function error", node.getNodeID() + ":" + i
								+ " has invalid expression: " + cifClone.getFunctions().get(i - 1));
						break allNodes;
					}
				}
			}
		}
	}

	@Override
	protected boolean isChanged() {
		System.out.println("EpiTabInputDefinition.isChanged()");
		for (NodeInfo node : mNode2RadioButton.keySet()) {
			ComponentPair cp = new ComponentPair(Project.getInstance().getProjectFeatures().getModel(this.activeModel),
					node);
			ComponentIntegrationFunctions cifClone = this.userIntegrationFunctions.getComponentIntegrationFunctions(cp);
			ComponentIntegrationFunctions cifOrig = this.epithelium.getIntegrationFunctions()
					.getComponentIntegrationFunctions(cp);
			System.out.println("isChanged.cifClone: " + cifClone);
			System.out.println("isChanged.cifOrig: " + cifOrig);
			if (cifClone == null && cifOrig == null)
				continue;
			if (cifClone == null && cifOrig != null || cifClone != null && cifOrig == null)
				return true;
			if (!cifOrig.equals(cifClone))
				return true;
		}
		return false;
	}

	@Override
	public void applyChange() {
		// FIXME: if a model is no longer in the epi, should we still save its
		// input definitions?
		this.jpNLTop.removeAll();
		JComboBox<String> jcbSBML = this.newModelCombobox(this.epithelium.getEpitheliumGrid().getModelSet());
		this.jpNLTop.add(jcbSBML);
		this.activeModel = (String) jcbSBML.getSelectedItem();
		this.updateComponentList();
	}
}
