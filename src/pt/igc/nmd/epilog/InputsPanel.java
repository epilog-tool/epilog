package pt.igc.nmd.epilog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;

import pt.igc.nmd.epilog.gui.ColorButton;
import pt.igc.nmd.epilog.gui.MainFrame;

public class InputsPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5470169327482303234L;

	private SphericalEpithelium epithelium;
	private Topology topology;
	private MainFrame mainPanel;

	private JPanel auxiliaryPanel[];

	private Hashtable<JComboBox, Integer> Jcombo2Node;
	private Hashtable<JCheckBox, Integer> Jcheck2Node;
	private Hashtable<JComboBox, Integer> JcomboInput2Node;
	private Hashtable<JButton, Integer> integrationFunctionButton2Node;

	private ColorButton[] node2Color;
	private JComboBox[] node2Jcombo;
	private JCheckBox[] node2Jcheck;
	private JButton[] node2IntegrationFunctionButton;
	private JComboBox[] initialStatePerComponent;
	private JComboBox[] inputComboChooser;
	private JCheckBox nodeBox[];

	private List<ColorButton> colorChooser;

	private JButton integrationFunctionButton;
//	private JPanel optionsPanel;

	public InputsPanel(SphericalEpithelium epithelium, Topology topology,
			MainFrame mainFrame) {

		this.mainPanel = mainFrame;
		this.topology = topology;
		this.epithelium = epithelium;

		init();
	}

	public void init() {

		Color backgroundColor = mainPanel.getBackground();

		setLayout(new BorderLayout());

		LogicalModel unitaryModel = mainPanel.getEpithelium().getUnitaryModel();

		if (unitaryModel != null) {
			List<NodeInfo> listNodes = epithelium.getUnitaryModel()
					.getNodeOrder();

			// CREATION OF THE INPUT GRID
			List<NodeInfo> listEnvNodes = new ArrayList<NodeInfo>();
			for (NodeInfo node : listNodes)
				if (node.isInput())
					listEnvNodes.add(node);

//			Grid envGrid = new Grid(topology.getNumberInstances(), listEnvNodes);

			// PAGE START PANEL

			JPanel optionsPanel = new JPanel();
			FlowLayout layout = new FlowLayout();
			layout.setAlignment(FlowLayout.LEFT);

			optionsPanel.setLayout(layout);

			JButton buttonMarkAll = new JButton("Apply All");
			JButton buttonClearAll = new JButton("Clear All");
			JButton buttonFill = new JButton("Fill");

			buttonFill.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					// TO DO: Fill has to check for each component individually
					// if
					// they have non zero expression neighbors. The fact that
					// one
					// component closes doesn't mean that the others do
				}
			});

			buttonMarkAll.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					markAllCells();

				}
			});

			buttonClearAll.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					clearAllCells();

				}
			});

			optionsPanel.add(buttonMarkAll);
			optionsPanel.add(buttonClearAll);
			optionsPanel.add(buttonFill);

			add(optionsPanel, BorderLayout.PAGE_START);

			// END PANEL

			JPanel endPanel = new JPanel(layout);

			JButton buttonAdd = new JButton("+");
			JButton buttonClear = new JButton("-");
			JTextField setName = new JTextField("", 15);
			JComboBox sets = new JComboBox();
			sets.setPreferredSize(new Dimension(
					setName.getPreferredSize().width,
					sets.getPreferredSize().height));

			buttonAdd.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// TODO: Add the selected grid to the sets of initial
					// conditions with the name of the textfield
				}
			});

			buttonClear.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// TODO: Erase from the list of sets a specific set
				}
			});

			sets.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO: Choose a set and insert the name of that set on the
					// textfield
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

			nodeBox = new JCheckBox[listNodes.size()];
			node2Jcheck = new JCheckBox[listNodes.size()];
			node2Color = new ColorButton[listNodes.size()];
			node2Jcombo = new JComboBox[listNodes.size()];
			node2IntegrationFunctionButton = new JButton[listNodes.size()];
			initialStatePerComponent = new JComboBox[listNodes.size()];
			inputComboChooser = new JComboBox[listNodes.size()];

			Jcheck2Node = new Hashtable<JCheckBox, Integer>();
			Jcombo2Node = new Hashtable<JComboBox, Integer>();
			integrationFunctionButton2Node = new Hashtable<JButton, Integer>();
			JcomboInput2Node = new Hashtable<JComboBox, Integer>();

			colorChooser = new ArrayList<ColorButton>();

			for (int i = 0; i < listNodes.size(); i++) {

				if (!listNodes.get(i).isInput())
					colorChooser.add(new ColorButton(mainPanel.componentsPanel,
							listNodes.get(i)));
				if (listNodes.get(i).isInput()) {

					auxiliaryPanel[i] = new JPanel();
					auxiliaryPanel[i].setLayout(layout);
					auxiliaryPanel[i].setBackground(backgroundColor);
					auxiliaryPanel[i].setBorder(BorderFactory
							.createEtchedBorder(EtchedBorder.LOWERED));

					nodeBox[i] = new JCheckBox(listNodes.get(i).getNodeID());
					nodeBox[i].setBackground(backgroundColor);
					nodeBox[i].setToolTipText(listNodes.get(i).getNodeID());
					nodeBox[i].setBackground(backgroundColor);

					Jcheck2Node.put(nodeBox[i], i);
					node2Jcheck[i] = nodeBox[i];
					epithelium.setDefinitionsComponentDisplay(i, false);

					nodeBox[i].addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent arg0) {
							JCheckBox src = (JCheckBox) arg0.getSource();
							setComponentDisplay(Jcheck2Node.get(src),
									src.isSelected());
							fillHexagons();
						}
					});


					initialStatePerComponent[i] = new JComboBox();

					for (int maxValue = 0; maxValue < listNodes.get(i).getMax() + 1; maxValue++) {
						initialStatePerComponent[i].addItem(maxValue);
					}
					Jcombo2Node.put(initialStatePerComponent[i], i);
					node2Jcombo[i] = initialStatePerComponent[i];

					epithelium.setInitialState(listNodes.get(Jcombo2Node
							.get(initialStatePerComponent[i])), (byte) 0);

					initialStatePerComponent[i]
							.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent arg0) {
									JComboBox src = (JComboBox) arg0
											.getSource();

									fireInitialStateChange(src);
								}
							});

					colorChooser.add(new ColorButton(mainPanel.componentsPanel,
							listNodes.get(i)));
					colorChooser.get(i).setBackground(
							epithelium.getColor(listNodes.get(i)));
					colorChooser.get(i).setPreferredSize(
							node2Jcombo[i].getPreferredSize());

					node2Color[i] = colorChooser.get(i);

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

					if (!epithelium.isIntegrationComponent(i)) {
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

					if (mainPanel.getEpithelium()
							.isDefinitionComponentDisplayOn(i))
						nodeBox[i].isSelected();
					if (mainPanel.getEpithelium().isIntegrationComponent(i))
						nodeBox[i].setEnabled(false);

					auxiliaryPanel[i].add(nodeBox[i]);
					if (mainPanel.getEpithelium().isIntegrationComponent(i))
						auxiliaryPanel[i].add(integrationFunctionButton);
					else {
						auxiliaryPanel[i].add(initialStatePerComponent[i]);
						auxiliaryPanel[i].add(colorChooser.get(i));
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

	protected void initializeIntegrationInterface(JButton src) {
		NodeInfo node = mainPanel.getEpithelium().getUnitaryModel()
				.getNodeOrder().get(integrationFunctionButton2Node.get(src));
		new IntegrationFunctionInterface(this.epithelium, node);

	}

	private void fireInitialStateChange(JComboBox combo) {
		epithelium.setInitialState(mainPanel.getEpithelium().getUnitaryModel()
				.getNodeOrder().get(Jcombo2Node.get(combo)),
				((Integer) combo.getSelectedItem()).byteValue());
	}

	public void setComponentDisplay(int i, boolean b) {
		mainPanel.getEpithelium().setDefinitionsComponentDisplay(i, b);
	}

	protected void setEnvOptions(JComboBox inputCombo, boolean bool) {

		int i = JcomboInput2Node.get(inputCombo);
		mainPanel.getEpithelium().setIntegrationComponent(i, !bool);

		if (bool) {
			epithelium.resetIntegrationNode(mainPanel.getEpithelium()
					.getUnitaryModel().getNodeOrder()
					.get(JcomboInput2Node.get(inputCombo)));
		} else {
			for (int instance = 0; instance < mainPanel.topology
					.getNumberInstances(); instance++) {
				epithelium.setGrid(instance, mainPanel.getEpithelium()
						.getUnitaryModel().getNodeOrder().get(i), (byte) 0);
			}
		}
		setInitialSetupHasChanged(true);
		removeAll();
		repaint();
		revalidate();
		init();

		fillHexagons();

		System.out.println(mainPanel.getEpithelium().getUnitaryModel()
				.getNodeOrder().get(i).getNodeID());

		// System.out.println(JcomboInput2Node.get(inputCombo)
		// + " "
		// + mainPanel.getEpithelium().getIntegrationComponents()
		// .get(JcomboInput2Node.get(inputCombo)));
	}

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

	protected void setInitialSetupHasChanged(boolean b) {
		mainPanel.setInitialSetupHasChanged(b);
	}

	public void fillHexagons() {
		mainPanel.fillHexagons();
	}

	public void clearAllCells() {
		epithelium.initializeGrid();
		fillHexagons();
	}

	public void markAllCells() {

		for (int i = 0; i < topology.getWidth(); i++) {
			for (int j = 0; j < topology.getHeight(); j++) {
				epithelium.setInitialState(i, j);
			}
		}
		fillHexagons();
	}
}
