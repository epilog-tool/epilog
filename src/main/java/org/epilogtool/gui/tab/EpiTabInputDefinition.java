package org.epilogtool.gui.tab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JCheckBox;
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
import org.epilogtool.common.Txt;
import org.epilogtool.core.ComponentIntegrationFunctions;
import org.epilogtool.core.Epithelium;
import org.epilogtool.core.EpitheliumIntegrationFunctions;
import org.epilogtool.gui.EpiGUI.TabChangeNotifyProj;
import org.epilogtool.gui.color.ColorUtils;
import org.epilogtool.gui.dialog.DialogMessage;
import org.epilogtool.gui.widgets.JComboCheckBox;
import org.epilogtool.project.Project;

public class EpiTabInputDefinition extends EpiTabDefinitions {
	private static final long serialVersionUID = -2124909766318378839L;

	private final int JTF_WIDTH = 30;

	private EpitheliumIntegrationFunctions userIntegrationFunctions;
	private String activeNodeID;
	private TabProbablyChanged tpc;

	private Map<NodeInfo, JRadioButton> mNode2RadioButton;

	private JPanel jpInputComp;
	private JPanel jpNRTop;
	private JPanel jpNRBottom;
	private JPanel jpNLTop;

	private ButtonGroup group;
	private JComboCheckBox jccbSBML;

	public EpiTabInputDefinition(Epithelium e, TreePath path, TabChangeNotifyProj tabChanged) {
		super(e, path, tabChanged);
		this.mNode2RadioButton = new HashMap<NodeInfo, JRadioButton>();
	}

	public void initialize() {

		this.center.setLayout(new BorderLayout());

		this.userIntegrationFunctions = this.epithelium.getIntegrationFunctions().clone();
		this.activeNodeID = null;
		this.tpc = new TabProbablyChanged();
		
		this.group = new ButtonGroup();

		// North Panel
		JPanel jpNorth = new JPanel(new BorderLayout());
		JPanel jpNLeft = new JPanel(new BorderLayout());
		this.jpNLTop = new JPanel(new BorderLayout());

		JPanel jpNRight = new JPanel(new BorderLayout());
		jpNorth.add(jpNRight, BorderLayout.CENTER);
		this.center.add(jpNorth, BorderLayout.NORTH);

		this.jpNRTop = new JPanel(new FlowLayout());
		jpNRight.add(this.jpNRTop, BorderLayout.NORTH);
		this.jpNRBottom = new JPanel(new GridBagLayout());
		jpNRight.add(this.jpNRBottom, BorderLayout.CENTER);

		jpNorth.add(jpNLeft, BorderLayout.LINE_START);

		// ---------------------------------------------------------------------------
		// Model selection jcomboCheckBox

		List<LogicalModel> modelList = new ArrayList<LogicalModel>(this.epithelium.getEpitheliumGrid().getModelSet());

		JCheckBox[] items = new JCheckBox[modelList.size()];

		for (int i = 0; i < modelList.size(); i++) {
			items[i] = new JCheckBox(Project.getInstance().getProjectFeatures().getModelName(modelList.get(i)));
			items[i].setSelected(true);
		}
		this.jccbSBML = new JComboCheckBox(items);

		this.jccbSBML.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboCheckBox jccb = (JComboCheckBox) e.getSource();
				jccb.updateSelected();
				updateComponentList(jccb.getSelectedItems());
			}
		});

		this.jpNLTop.setBorder(BorderFactory.createTitledBorder(Txt.get("s_MODEL_SELECT")));
		this.jpNLTop.add(this.jccbSBML);
		jpNLeft.add(this.jpNLTop, BorderLayout.NORTH);

		// Component selection list
		this.jpInputComp = new JPanel(new GridBagLayout());
		this.jpInputComp.setBorder(BorderFactory.createTitledBorder("Input component"));
		jpNLeft.add(this.jpInputComp, BorderLayout.CENTER);

		updateComponentList(this.jccbSBML.getSelectedItems());

		this.isInitialized = true;
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

		this.jpInputComp.removeAll();

		List<NodeInfo> lInputs = new ArrayList<NodeInfo>(
				Project.getInstance().getProjectFeatures().getModelsNodeInfos(lModels, true));

		for (NodeInfo node : lInputs) {
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
//						getParent().repaint();
					}
				});
				this.mNode2RadioButton.put(node, jrb);
			}
			this.group.add(jrb);
		}

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(1, 5, 1, 0);

		if (lInputs.size() == 0) {
			this.getNoInputTextField();
		} else {

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
				this.jpInputComp.add(this.mNode2RadioButton.get(node), gbc);
			}
		}

		System.out.println(this.jpInputComp.getComponentCount());
		this.jpInputComp.repaint();
		this.jpInputComp.revalidate();
		this.repaint();
		this.revalidate();
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
		ButtonGroup groupRole = new ButtonGroup();
		this.jpNRTop.add(new JLabel(this.activeNodeID + ": "));
		JRadioButton jrModelInput = new JRadioButton("Positional input");
		jrModelInput.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				paintModelInputPanel();
				tpc.setChanged();
				// Re-Paint
				getParent().repaint();
			}
		});
		groupRole.add(jrModelInput);
		this.jpNRTop.add(jrModelInput);
		JRadioButton jrModelInt = new JRadioButton("Integration input");
		jrModelInt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				paintModelIntegrationPanel();
				tpc.setChanged();
				// Re-Paint
				getParent().repaint();
			}
		});
		groupRole.add(jrModelInt);
		this.jpNRTop.add(jrModelInt);

		NodeInfo node = Project.getInstance().getProjectFeatures().getNodeInfo(this.activeNodeID);

		if (this.userIntegrationFunctions.containsNode(node)) {
			jrModelInt.setSelected(true);
			paintModelIntegrationPanel();
		} else {
			jrModelInput.setSelected(true);
			paintModelInputPanel();
		}
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
		NodeInfo node = Project.getInstance().getProjectFeatures().getNodeInfo(this.activeNodeID);
		ComponentIntegrationFunctions cif = this.userIntegrationFunctions.getComponentIntegrationFunctions(node);
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

		NodeInfo node = Project.getInstance().getProjectFeatures().getNodeInfo(this.activeNodeID);

		ComponentIntegrationFunctions cfi = this.epithelium.getIntegrationFunctionsForComponent(node);
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

	private void getNoInputTextField() {
		this.jpNRBottom.removeAll();
		this.jpNRTop.removeAll();
		this.jpInputComp.setVisible(false);
		this.activeNodeID = null;
		JEditorPane jEmptyInputPane = new JEditorPane();
		jEmptyInputPane.setContentType("text/html");
		jEmptyInputPane.setEditable(false);
		jEmptyInputPane.setEnabled(true);
		jEmptyInputPane.setBackground(this.jpNRBottom.getBackground());
		jEmptyInputPane.setText("There are no input components in this model");
		this.jpNRBottom.add(jEmptyInputPane);
	}

	private void paintModelInputPanel() {
		NodeInfo node = Project.getInstance().getProjectFeatures().getNodeInfo(this.activeNodeID);
		this.userIntegrationFunctions.removeComponent(node);
		this.jpNRBottom.removeAll();
	}

	private void paintModelIntegrationPanel() {
		// GUI
		this.jpNRBottom.removeAll();

		NodeInfo node = Project.getInstance().getProjectFeatures().getNodeInfo(this.activeNodeID);
		if (!this.userIntegrationFunctions.containsNode(node)) {
			this.userIntegrationFunctions.addComponent(node);
		}
		ComponentIntegrationFunctions cfi = this.userIntegrationFunctions.getComponentIntegrationFunctions(node);

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

			ComponentIntegrationFunctions cifClone = this.userIntegrationFunctions
					.getComponentIntegrationFunctions(node);
			EpitheliumIntegrationFunctions eifOrig = this.epithelium.getIntegrationFunctions();
			if (cifClone == null) {
				eifOrig.removeComponent(node);
			} else {
				eifOrig.addComponent(node);
				for (byte i = 1; i <= node.getMax(); i++) {
					try {
						eifOrig.getComponentIntegrationFunctions(node).setFunctionAtLevel(i,
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
		for (NodeInfo node : mNode2RadioButton.keySet()) {

			ComponentIntegrationFunctions cifClone = this.userIntegrationFunctions
					.getComponentIntegrationFunctions(node);
			ComponentIntegrationFunctions cifOrig = this.epithelium.getIntegrationFunctions()
					.getComponentIntegrationFunctions(node);

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

		// New (potential) model list -> Update JComboCheckBox
		// and (potential) new node value counts
		
		this.epithelium.getEpitheliumGrid().updateGrid();
		List<String> newModelList = new ArrayList<String>();
		for (LogicalModel m : this.epithelium.getEpitheliumGrid().getModelSet()) {
			newModelList.add(Project.getInstance().getProjectFeatures().getModelName(m));
		}
		this.jccbSBML.updateItemList(newModelList);
		
		updateComponentList(this.jccbSBML.getSelectedItems());
		

		
	}
}
