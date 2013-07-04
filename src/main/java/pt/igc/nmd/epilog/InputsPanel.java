package pt.igc.nmd.epilog;

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
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;

import pt.igc.nmd.epilog.gui.MainFrame;


public class InputsPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5470169327482303234L;

	private SphericalEpithelium epithelium;
	private Topology topology;
	private MainFrame mainFrame;

	private JPanel auxiliaryPanel[];

	private Hashtable<JComboBox, Integer> Jcombo2Node;
	public Hashtable<JCheckBox, Integer> Jcheck2Node;
	private Hashtable<JComboBox, Integer> JcomboInput2Node;
	private Hashtable<JButton, Integer> integrationFunctionButton2Node;
	
	private Hashtable<JButton, NodeInfo> button2Node;


	private JButton[] node2IntegrationFunctionButton;
	private JComboBox[] inputComboChooser;

	
	private JTextField setName;
	private JComboBox sets;
	
	private JButton integrationFunctionButton;

	public InputsPanel(SphericalEpithelium epithelium, Topology topology,
			MainFrame mainFrame) {

		this.mainFrame = mainFrame;
		this.topology = topology;
		this.epithelium = epithelium;

		init();
	}

	public void init() {

		Color backgroundColor = mainFrame.getBackground();

		setLayout(new BorderLayout());

		LogicalModel unitaryModel = mainFrame.getEpithelium().getUnitaryModel();

		if (unitaryModel != null) {
			List<NodeInfo> listNodes = epithelium.getUnitaryModel()
					.getNodeOrder();


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
			
			if (mainFrame.epithelium.getInputsIntegrationSet() != null) {
				for (String key : mainFrame.epithelium.getInputsIntegrationSet()
						.keySet()) {
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
					removeElementFromSet() ;
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
		

//			nodeBox = new JCheckBox[listNodes.size()];
//			node2Jcheck = new JCheckBox[listNodes.size()];
//			node2Jcombo = new JComboBox[listNodes.size()];
			node2IntegrationFunctionButton = new JButton[listNodes.size()];
//			initialStatePerComponent = new JComboBox[listNodes.size()];
			inputComboChooser = new JComboBox[listNodes.size()];
//			colorButton = new JButton[listNodes.size()];
			
			button2Node = new Hashtable<JButton, NodeInfo>();
			Jcheck2Node = new Hashtable<JCheckBox, Integer>();
			Jcombo2Node = new Hashtable<JComboBox, Integer>();
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

//					if (mainFrame.epithelium
//							.isDefinitionComponentDisplayOn(i))
//						nodeBox[i].isSelected();
//					if (mainFrame.epithelium.isIntegrationComponent(i))
//						nodeBox[i].setEnabled(false);

					auxiliaryPanel[i].add(nodeID);
					if (mainFrame.epithelium.isIntegrationComponent(i))
						auxiliaryPanel[i].add(integrationFunctionButton);
					else {
//						auxiliaryPanel[i].add(initialStatePerComponent[i]);
//						auxiliaryPanel[i].add(colorButton[i]);
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
	
//	private void fireOnFill(){
//		if(!mainFrame.isFillOn()){
//			buttonFill.setBackground(Color.yellow);
//			mainFrame.setFill(true);
//		}
//		else{
//			mainFrame.setFill(false);
//			buttonFill.setBackground(this.getBackground());
//		}
//	}
	
	
	private void setNewColor(JButton src){
		Color newColor = JColorChooser.showDialog(src, "Color Chooser", mainFrame.epithelium.getColor(button2Node.get(src)));
		src.setBackground(newColor);
		mainFrame.epithelium.setColor(button2Node.get(src),newColor);
		fillHexagons();
	}

	protected void initializeIntegrationInterface(JButton src) {
		NodeInfo node = mainFrame.epithelium.getUnitaryModel()
				.getNodeOrder().get(integrationFunctionButton2Node.get(src));
		new IntegrationFunctionInterface(this.epithelium, node);
		
		

	}


	public void setComponentDisplay(int i, boolean b) {
		mainFrame.epithelium.setDefinitionsComponentDisplay(i, b);
	}

	protected void setEnvOptions(JComboBox inputCombo, boolean bool) {

		int i = JcomboInput2Node.get(inputCombo);
		mainFrame.epithelium.setIntegrationComponent(i, !bool);

		if (bool) {
			epithelium.resetIntegrationNode(mainFrame.epithelium
					.getUnitaryModel().getNodeOrder()
					.get(JcomboInput2Node.get(inputCombo)));
		} else {
			for (int instance = 0; instance < mainFrame.topology
					.getNumberInstances(); instance++) {
				epithelium.setGrid(instance, mainFrame.epithelium
						.getUnitaryModel().getNodeOrder().get(i), (byte) 0);
			}
		}
		setInitialSetupHasChanged(true);
		removeAll();
		repaint();
		revalidate();
		init();

		fillHexagons();
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
		mainFrame.setInitialSetupHasChanged(b);
	}

	public void fillHexagons() {
		mainFrame.fillHexagons();
	}

	public void clearAllCells() {
		epithelium.initializeGrid();
		fillHexagons();
	}

	public void markAllCells() {

		for (int instance = 0; instance < mainFrame.topology
				.getNumberInstances(); instance++) {

			mainFrame.epithelium.setInitialState(instance);
		}
		fillHexagons();
	}
	
	
	private void loadInitialconditions() {
		setName.setText((String) sets.getSelectedItem());
		if (sets.getSelectedItem() != null)
			if (mainFrame.epithelium.getInputsIntegrationSet().get(
					(String) sets.getSelectedItem()) != null){
				mainFrame.epithelium.setSelectedInputSet((String) sets
						.getSelectedItem());
			}
		
	}
	
	
	// End Methods

	private void inputsAdd() {
		String name = setName.getText();
		if (!mainFrame.epithelium.getInputsIntegrationSet().containsKey(name))
			sets.addItem(name);{
		mainFrame.epithelium.setInputsSet(name);
			}
	}
	
	private void removeElementFromSet() {
		String setToRemove = (String) sets.getSelectedItem();
		mainFrame.epithelium.getInputsIntegrationSet().remove(setToRemove);
		setName.setText("");
		sets.removeAllItems();

		for (String string : mainFrame.epithelium.getInputsIntegrationSet().keySet())
			if (string != "none")
				sets.addItem(string);
	}
	
}
