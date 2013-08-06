package pt.igc.nmd.epilog.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;

public class InputsPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5470169327482303234L;

	private MainFrame mainFrame;

	private JPanel auxiliaryPanel[];

	// private Hashtable<JComboBox, Integer> Jcombo2Node;
	public Hashtable<JCheckBox, Integer> Jcheck2Node;
	private Hashtable<JComboBox, Integer> JcomboInput2Node;
	private Hashtable<JButton, Integer> integrationFunctionButton2Node;

	private Hashtable<JButton, NodeInfo> button2Node;

	private JButton[] node2IntegrationFunctionButton;
	private JComboBox[] inputComboChooser;

	private JTextField setName;
	private JComboBox sets;

	private JButton integrationFunctionButton;

	
	/**
	 * Generates the inputs panel, to be inserted in the tab on Epilog's
	 * main panel.
	 * 
	 * @param mainFrame
	 */
	public InputsPanel(MainFrame mainFrame) {

		this.mainFrame = mainFrame;
		init();
	}

	/**
	 * Initializes the inputs panel, to be inserted in the tab on Epilog's main
	 * panel.
	 * 
	 */
	public void init() {

		Color backgroundColor = this.mainFrame.getBackground();

		setLayout(new BorderLayout());

		LogicalModel unitaryModel = this.mainFrame.epithelium.getUnitaryModel();

		if (unitaryModel != null) {
			List<NodeInfo> listNodes = this.mainFrame.epithelium
					.getUnitaryModel().getNodeOrder();

			// PAGE START PANEL

			JPanel optionsPanel = new JPanel();
			FlowLayout layout = new FlowLayout();
			layout.setAlignment(FlowLayout.LEFT);

			optionsPanel.setLayout(layout);

			// END PANEL

			JPanel endPanel = new JPanel(layout);

			JButton buttonAdd = new JButton("+");
			JButton buttonClear = new JButton("-");
			setName = new JTextField("", 15);
			sets = new JComboBox();
			sets.setPreferredSize(new Dimension(
					setName.getPreferredSize().width,
					sets.getPreferredSize().height));

			if (this.mainFrame.epithelium.getInputsIntegrationSet() != null) {
				for (String key : this.mainFrame.epithelium
						.getInputsIntegrationSet().keySet()) {
					sets.addItem(key);
				}
			}

			buttonAdd.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					inputsAdd();
				}
			});

			buttonClear.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					removeElementFromSet();
				}
			});

			sets.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					loadInitialconditions();
					mainFrame.fillHexagons();
				}
			});

			endPanel.add(setName);
			endPanel.add(buttonAdd);
			endPanel.add(sets);
			endPanel.add(buttonClear);

			add(endPanel, BorderLayout.PAGE_END);

			// CENTER PANEL

			JPanel centerPanel = new JPanel(layout);

			auxiliaryPanel = new JPanel[listNodes.size()];

			node2IntegrationFunctionButton = new JButton[listNodes.size()];
			inputComboChooser = new JComboBox[listNodes.size()];

			button2Node = new Hashtable<JButton, NodeInfo>();
			Jcheck2Node = new Hashtable<JCheckBox, Integer>();
			// Jcombo2Node = new Hashtable<JComboBox, Integer>();
			integrationFunctionButton2Node = new Hashtable<JButton, Integer>();
			JcomboInput2Node = new Hashtable<JComboBox, Integer>();

			for (int i = 0; i < listNodes.size(); i++) {

				if (listNodes.get(i).isInput()) {

					auxiliaryPanel[i] = new JPanel();
					auxiliaryPanel[i].setLayout(layout);
					auxiliaryPanel[i].setBackground(backgroundColor);
					auxiliaryPanel[i].setBorder(BorderFactory
							.createEtchedBorder(EtchedBorder.LOWERED));

					JLabel nodeID = new JLabel(listNodes.get(i).getNodeID());

					integrationFunctionButton = new JButton("Function");
					integrationFunctionButton
							.setToolTipText("Integration Function");

					integrationFunctionButton2Node.put(
							integrationFunctionButton, i);
					node2IntegrationFunctionButton[i] = integrationFunctionButton;
					integrationFunctionButton
							.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent arg0) {
									JButton src = (JButton) arg0.getSource();
									initializeIntegrationInterface(src);
								}
							});

					inputComboChooser[i] = new JComboBox();

					JcomboInput2Node.put(inputComboChooser[i], i);

					if (!this.mainFrame.epithelium.isIntegrationComponent(i)) {
						inputComboChooser[i]
								.addItem(InputOption
										.getDescriptionString(InputOption.ENVIRONMENTAL_INPUT));
						inputComboChooser[i]
								.addItem(InputOption
										.getDescriptionString(InputOption.INTEGRATION_INPUT));
					} else {
						inputComboChooser[i]
								.addItem(InputOption
										.getDescriptionString(InputOption.INTEGRATION_INPUT));
						inputComboChooser[i]
								.addItem(InputOption
										.getDescriptionString(InputOption.ENVIRONMENTAL_INPUT));
					}

					auxiliaryPanel[i].add(nodeID);
					if (this.mainFrame.epithelium.isIntegrationComponent(i))
						auxiliaryPanel[i].add(integrationFunctionButton);
					else {

					}

					auxiliaryPanel[i].add(inputComboChooser[i]);
					centerPanel.add(auxiliaryPanel[i]);

					inputComboChooser[i]
							.addActionListener(new ActionListener() {

								@Override
								public void actionPerformed(ActionEvent event) {
									JComboBox source = (JComboBox) event
											.getSource();
									String optionString = (String) source
											.getSelectedItem();

									InputOption option = InputOption
											.getOptionFromString(optionString);

									if (option != null) {
										switch (option) {
										case ENVIRONMENTAL_INPUT: {
											setEnvOptions(source, true);
											break;
										}
										case INTEGRATION_INPUT: {
											setEnvOptions(source, false);
											break;
										}
										default: {
											repaint();
										}
											break;
										}
									}
								}
							});
				}
			}
			add(centerPanel, BorderLayout.CENTER);
		}
	}

	/**
	 * Initiates the integration function editing panel.
	 * 
	 * @param src
	 *            button that associates a component with the integration
	 *            functions
	 * 
	 */
	protected void initializeIntegrationInterface(JButton src) {
		NodeInfo node = this.mainFrame.epithelium.getUnitaryModel()
				.getNodeOrder().get(integrationFunctionButton2Node.get(src));
		new IntegrationFunctionInterface(this.mainFrame.epithelium, this.mainFrame, node);
	}

	/**
	 * Sets an input as environment or integrations.
	 * 
	 * @param inputCombo
	 *            JComboBox associated with a node that indicates if it is an
	 *            integration or environment input
	 * @param b
	 *            boolean value that is true if a component is an environment
	 *            input, false otherwise
	 */
	protected void setEnvOptions(JComboBox inputCombo, boolean bool) {

		int i = JcomboInput2Node.get(inputCombo);
		this.mainFrame.epithelium.setIntegrationComponent(i, !bool);

		if (bool) {
			this.mainFrame.epithelium
					.resetIntegrationNode(this.mainFrame.epithelium
							.getUnitaryModel().getNodeOrder()
							.get(JcomboInput2Node.get(inputCombo)));
		} else {
			for (int instance = 0; instance < this.mainFrame.topology
					.getNumberInstances(); instance++) {
				this.mainFrame.epithelium.setGrid(instance,
						this.mainFrame.epithelium.getUnitaryModel()
								.getNodeOrder().get(i), (byte) 0);
			}
		}
		removeAll();
		repaint();
		revalidate();
		init();

		this.mainFrame.fillHexagons();
	}

	
	/**
	 * Enumeration of the options of the choice of input type.
	 * 
	 */
	private enum InputOption {
		ENVIRONMENTAL_INPUT, INTEGRATION_INPUT;

		public static String getDescriptionString(InputOption option) {
			switch (option) {
			case ENVIRONMENTAL_INPUT:
				return "Env";
			case INTEGRATION_INPUT:
				return "Int";
			default:
				return "";
			}
		}

		public static InputOption getOptionFromString(String optionString) {
			if (optionString.equals(InputOption
					.getDescriptionString(ENVIRONMENTAL_INPUT)))
				return ENVIRONMENTAL_INPUT;
			else if (optionString.equals(InputOption
					.getDescriptionString(INTEGRATION_INPUT)))
				return INTEGRATION_INPUT;
			else
				return null;
		}
	}

	
	/**
	 * Loads the initial conditions set selected when changing the sets at the inputs panel.
	 * 
	 */
	private void loadInitialconditions() {
		setName.setText((String) sets.getSelectedItem());
		if (sets.getSelectedItem() != null)
			if (this.mainFrame.epithelium.getInputsIntegrationSet().get(
					(String) sets.getSelectedItem()) != null) {
				this.mainFrame.epithelium.setSelectedInputSet((String) sets
						.getSelectedItem());
			}

	}

	// End Methods

	/**
	 * Adds an input set. If a name is already used, then the new set replaces
	 * the old one.
	 * 
	 */
	private void inputsAdd() {
		String name = setName.getText();
		if (!this.mainFrame.epithelium.getInputsIntegrationSet().containsKey(
				name))
			sets.addItem(name);
		{
			this.mainFrame.epithelium.setIntegrationInputsSet(name);
		}
	}

	/**
	 * Removes an element from the set of input sets.
	 * 
	 */
	private void removeElementFromSet() {
		String setToRemove = (String) sets.getSelectedItem();
		this.mainFrame.epithelium.getInputsIntegrationSet().remove(setToRemove);
		setName.setText("");
		sets.removeAllItems();

		for (String string : this.mainFrame.epithelium
				.getInputsIntegrationSet().keySet())
			if (string != "none")
				sets.addItem(string);
	}

}
