//package pt.igc.nmd.epilog;
//
//import java.awt.Color;
//import java.awt.Dimension;
//import java.awt.FlowLayout;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.awt.event.MouseListener;
//import java.awt.event.MouseMotionListener;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.ArrayList;
//import java.util.Hashtable;
//import java.util.List;
//import java.util.Set;
//import java.util.zip.ZipEntry;
//import java.util.zip.ZipOutputStream;
//
//import javax.swing.BorderFactory;
//import javax.swing.JButton;
//import javax.swing.JCheckBox;
//import javax.swing.JComboBox;
//import javax.swing.JFileChooser;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JPanel;
//import javax.swing.JTextArea;
//import javax.swing.JTextField;
//import javax.swing.border.LineBorder;
//import javax.swing.border.TitledBorder;
//
//import org.colomoto.logicalmodel.NodeInfo;
//
//import pt.igc.nmd.epilog.gui.ColorButton;
//import pt.igc.nmd.epilog.gui.DrawPolygonM;
//import pt.igc.nmd.epilog.gui.MainFrame;
//
//public class SetupDefinitions extends JFrame {
//
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 1L;
//
//	private SphericalEpithelium epithelium;
//	private Topology topology;
//
//	private JTextArea textArea;
//
//	private Color color;
//	// public List<List<Cell>> cells;
//	private int startX;
//	private int startY;
//	private int endX;
//	private int endY;
//
//	private MainFrame mainPanel;
//
//	private JPanel properComponentsPanel;
//	private JPanel inputsPanel;
//	private JPanel perturbationsPanel;
//	private JPanel optionsPanel;
//	private JPanel composedPanel;
//
//	private JPanel line1;
//	private JPanel line2;
//	private DrawPolygonM MapPanel;
//
//	// Nodes Information
//
//	private List<NodeInfo> listNodes;
//
//	private JCheckBox nodeBox[];
//	private List<ColorButton> colorChooser;
//	private JComboBox[] initialStatePerComponent;
//	private JComboBox[] inputComboChooser;
//
//	private JButton integrationFunctionButton;
//
//	private Hashtable<JComboBox, NodeInfo> Jcombo2Node;
//	private Hashtable<JCheckBox, NodeInfo> Jcheck2Node;
//	private Hashtable<JComboBox, NodeInfo> JcomboInput2Node;
//	private Hashtable<NodeInfo, JComboBox> node2Jcombo;
//	private Hashtable<NodeInfo, JCheckBox> node2Jcheck;
//
//	private Hashtable<NodeInfo, ColorButton> node2Color;
//	private Hashtable<NodeInfo, Boolean> node2IntInput;
//	private Hashtable<NodeInfo, Boolean> componentDisplay;
//	private Hashtable<JButton, NodeInfo> integrationFunctionButton2Node;
//	private Hashtable<String, NodeInfo> string2Node;
//	private Hashtable<NodeInfo, JButton> node2IntegrationFunctionButton;
//
//	private boolean composedModelActive;
//
//	private NodeInfo selectedPerturbedComponent;
//
//	private Color backgroundColor;
//
//	// private JTextField filename = new JTextField(), dir = new JTextField();
//
//	/*
//	 * 
//	 */
//
//	public SetupDefinitions(SphericalEpithelium epithelium, Topology topology,
//			MainFrame mainPanel) {
//
//		super("Setup Definitions");
//		setLayout(null);
//
//		this.mainPanel = mainPanel;
//		this.epithelium = epithelium;
//		this.topology = topology;
//
//		this.backgroundColor = new Color(0xD3D3D3);
//
//		// composedModelActive = true;
//
//		FlowLayout layout = new FlowLayout();
//		layout.setAlignment(FlowLayout.LEFT);
//
//		colorChooser = new ArrayList<ColorButton>();
//
//		Jcombo2Node = new Hashtable<JComboBox, NodeInfo>();
//		Jcheck2Node = new Hashtable<JCheckBox, NodeInfo>();
//		JcomboInput2Node = new Hashtable<JComboBox, NodeInfo>();
//		node2Jcombo = new Hashtable<NodeInfo, JComboBox>();
//		node2Jcheck = new Hashtable<NodeInfo, JCheckBox>();
//
//		node2Color = new Hashtable<NodeInfo, ColorButton>();
//		integrationFunctionButton2Node = new Hashtable<JButton, NodeInfo>();
//		componentDisplay = new Hashtable<NodeInfo, Boolean>();
//		node2IntInput = new Hashtable<NodeInfo, Boolean>();
//		string2Node = new Hashtable<String, NodeInfo>();
//		node2IntegrationFunctionButton = new Hashtable<NodeInfo, JButton>();
//		// instanceReturnsPerturbation = new Hashtable<Integer, String>();
//
//		getContentPane().setPreferredSize(new Dimension(900, 600));
//		getContentPane().setBackground(backgroundColor);
//
//		TitledBorder titleProperComponents;
//		TitledBorder titleInputs;
//		TitledBorder titlePerturbation;
//		TitledBorder titleComposedModelSetup;
//
//		titleInputs = BorderFactory.createTitledBorder("Inputs");
//		titlePerturbation = BorderFactory.createTitledBorder("Perturbations");
//
//		LineBorder border = new LineBorder(Color.black, 1, true);
//		titleComposedModelSetup = new TitledBorder(border,
//				"Epithelium Model Setup", TitledBorder.LEFT,
//				TitledBorder.DEFAULT_POSITION);
//
//		titleProperComponents = new TitledBorder(border, "Proper Components",
//				TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION);
//
//		properComponentsPanel = new JPanel();
//		inputsPanel = new JPanel();
//		perturbationsPanel = new JPanel();
//		line1 = new JPanel();
//		line2 = new JPanel();
//		optionsPanel = new JPanel();
//		composedPanel = new JPanel();
//
//		MapPanel = new DrawPolygonM(this.mainPanel);
//
//		composedPanel.setLayout(layout);
//		MapPanel.setLayout(null);
//
//		inputsPanel.setPreferredSize(new Dimension(410, 180));
//		perturbationsPanel.setPreferredSize(new Dimension(410, 100));
//
//		properComponentsPanel.setBounds(455, 20, 430, 200);
//		composedPanel.setBounds(455, 230, 430, 350);
//		MapPanel.setBounds(10, 30, 440, 500);
//		optionsPanel.setBounds(20, 550, 400, 35);
//		line1.setBounds(20, 20, 280, 30);
//		line2.setBounds(20, 50, 280, 30);
//
//		properComponentsPanel.setBackground(backgroundColor);
//		inputsPanel.setBackground(backgroundColor);
//		optionsPanel.setBackground(backgroundColor);
//		perturbationsPanel.setBackground(backgroundColor);
//		composedPanel.setBackground(backgroundColor);
//		MapPanel.setBackground(backgroundColor);
//		line1.setBackground(backgroundColor);
//		line2.setBackground(backgroundColor);
//
//		perturbationsPanel.setBackground(backgroundColor);
//
//		properComponentsPanel.setBorder(titleProperComponents);
//		inputsPanel.setBorder(titleInputs);
//		perturbationsPanel.setBorder(titlePerturbation);
//		composedPanel.setBorder(titleComposedModelSetup);
//
//		properComponentsPanel.setLayout(null);
//		inputsPanel.setLayout(null);
//		perturbationsPanel.setLayout(null);
//		line1.setLayout(layout);
//		line2.setLayout(layout);
//
//		/* ProperComponents and Input panel */
//
//		listNodes = epithelium.getUnitaryModel().getNodeOrder();
//
//		nodeBox = new JCheckBox[listNodes.size()];
//		initialStatePerComponent = new JComboBox[listNodes.size()];
//		inputComboChooser = new JComboBox[listNodes.size()];
//
//		int inputCount = 0;
//		int properCount = 0;
//		int xOffset = 0;
//		int yOffset = 0;
//		int xOffsetInput = 0;
//		int yOffsetInput = 0;
//
//		for (int i = 0; i < listNodes.size(); i++) {
//			node2IntInput.put(listNodes.get(i), false);
//
//			if (listNodes.get(i).isInput()) {
//
//				if (inputCount % 2 != 0)
//					xOffsetInput = 200;
//				else if (inputCount % 2 == 0) {
//					xOffsetInput = 0;
//					yOffsetInput = inputCount / 2 * 40;
//				}
//
//				nodeBox[i] = new JCheckBox(listNodes.get(i).getNodeID());
//				nodeBox[i].setToolTipText(listNodes.get(i).getNodeID());
//				nodeBox[i].setBackground(backgroundColor);
//				nodeBox[i].setBounds(10 + xOffsetInput, 20 + yOffsetInput, 70,
//						25);
//				Jcheck2Node.put(nodeBox[i], listNodes.get(i));
//				node2Jcheck.put(listNodes.get(i), nodeBox[i]);
//				componentDisplay.put(listNodes.get(i), false);
//
//				nodeBox[i].addActionListener(new ActionListener() {
//					@Override
//					public void actionPerformed(ActionEvent arg0) {
//						setMarkPerturbation(!getMarkPerturbation());
//						JCheckBox src = (JCheckBox) arg0.getSource();
//						setComponentDisplay(Jcheck2Node.get(src),
//								src.isSelected());
//						fillhexagons();
//					}
//				});
//
//				mainPanel.getSimulation().setNode2Int(listNodes.get(i), i);
//
//				initialStatePerComponent[i] = new JComboBox();
//				initialStatePerComponent[i].setBounds(85 + xOffsetInput,
//						20 + yOffsetInput, 35, 25);
//				for (int maxValue = 0; maxValue < listNodes.get(i).getMax() + 1; maxValue++) {
//					initialStatePerComponent[i].addItem(maxValue);
//				}
//				Jcombo2Node.put(initialStatePerComponent[i], listNodes.get(i));
//				node2Jcombo.put(listNodes.get(i), initialStatePerComponent[i]);
//
//				epithelium.setInitialState(
//						Jcombo2Node.get(initialStatePerComponent[i]), (byte) 0);
//
//				initialStatePerComponent[i]
//						.addActionListener(new ActionListener() {
//							@Override
//							public void actionPerformed(ActionEvent arg0) {
//								JComboBox src = (JComboBox) arg0.getSource();
//
//								fireInitialStateChange(src);
//							}
//						});
//
//				colorChooser.add(new ColorButton(mainPanel.componentsPanel,
//						listNodes.get(i)));
//				colorChooser.get(i).setBackground(
//						epithelium.getColor(listNodes.get(i)));
//				colorChooser.get(i).setBounds(125 + xOffsetInput,
//						20 + yOffsetInput, 20, 25);
//
//				node2Color.put(listNodes.get(i), colorChooser.get(i));
//
//				inputsPanel.add(nodeBox[i]);
//				inputsPanel.add(initialStatePerComponent[i]);
//				inputsPanel.add(colorChooser.get(i));
//
//				integrationFunctionButton = new JButton("Function");
//				integrationFunctionButton
//						.setToolTipText("Integration Function");
//				integrationFunctionButton.setBounds(85 + xOffsetInput,
//						20 + yOffsetInput, 65, 25);
//				inputsPanel.add(integrationFunctionButton);
//
//				integrationFunctionButton2Node.put(integrationFunctionButton,
//						listNodes.get(i));
//				node2IntegrationFunctionButton.put(listNodes.get(i),
//						integrationFunctionButton);
//				integrationFunctionButton
//						.addActionListener(new ActionListener() {
//							public void actionPerformed(ActionEvent arg0) {
//								JButton src = (JButton) arg0.getSource();
//								initializeIntegrationInterface(src);
//							}
//						});
//
//				inputComboChooser[i] = new JComboBox();
//
//				JcomboInput2Node.put(inputComboChooser[i], listNodes.get(i));
//
//				inputComboChooser[i].setBounds(155 + xOffsetInput,
//						20 + yOffsetInput, 50, 25);
//				if (!epithelium.isIntegrationComponent(listNodes.get(i))) {
//					inputComboChooser[i]
//							.addItem(InputOption
//									.getDescriptionString(InputOption.ENVIRONMENTAL_INPUT));
//					inputComboChooser[i]
//							.addItem(InputOption
//									.getDescriptionString(InputOption.INTEGRATION_INPUT));
//				} else {
//					inputComboChooser[i]
//							.addItem(InputOption
//									.getDescriptionString(InputOption.INTEGRATION_INPUT));
//					inputComboChooser[i]
//							.addItem(InputOption
//									.getDescriptionString(InputOption.ENVIRONMENTAL_INPUT));
//				}
//
//				inputsPanel.add(inputComboChooser[i]);
//
//				inputComboChooser[i].addActionListener(new ActionListener() {
//
//					@Override
//					public void actionPerformed(ActionEvent event) {
//						JComboBox source = (JComboBox) event.getSource();
//						String optionString = (String) source.getSelectedItem();
//
//						InputOption option = InputOption
//								.getOptionFromString(optionString);
//
//						if (option != null) {
//							switch (option) {
//							case ENVIRONMENTAL_INPUT:
//								setEnvOptions(source, true);
//								setInitialSetupHasChanged(true);
//								break;
//
//							case INTEGRATION_INPUT: {
//
//								setEnvOptions(source, false);
//								setInitialSetupHasChanged(true);
//							}
//								break;
//							default: {
//
//								repaint();
//							}
//								break;
//							}
//						}
//					}
//				});
//
//				inputCount = inputCount + 1;
//
//				integrationFunctionButton.setVisible(epithelium
//						.isIntegrationComponent(listNodes.get(i)));
//				nodeBox[i].setEnabled(!epithelium
//						.isIntegrationComponent(listNodes.get(i)));
//				initialStatePerComponent[i].setVisible(!epithelium
//						.isIntegrationComponent(listNodes.get(i)));
//				colorChooser.get(i).setVisible(
//						!epithelium.isIntegrationComponent(listNodes.get(i)));
//
//			} else if (!listNodes.get(i).isInput()) {
//
//				string2Node.put(listNodes.get(i).getNodeID(), listNodes.get(i));
//				nodeBox[i] = new JCheckBox(listNodes.get(i).getNodeID());
//				nodeBox[i].setToolTipText(listNodes.get(i).getNodeID());
//				nodeBox[i].setBackground(backgroundColor);
//				if (properCount % 3 == 2)
//					xOffset = 140;
//				else if (properCount % 3 == 1)
//					xOffset = 280;
//				else if (properCount % 3 == 0) {
//					xOffset = 0;
//					yOffset = properCount / 3 * 40;
//				}
//
//				nodeBox[i].setBounds(10 + xOffset, 30 + yOffset, 65, 25);
//				Jcheck2Node.put(nodeBox[i], listNodes.get(i));
//				componentDisplay.put(listNodes.get(i), false);
//
//				nodeBox[i].addActionListener(new ActionListener() {
//					@Override
//					public void actionPerformed(ActionEvent arg0) {
//						setMarkPerturbation(!getMarkPerturbation());
//						JCheckBox src = (JCheckBox) arg0.getSource();
//						setComponentDisplay(Jcheck2Node.get(src),
//								src.isSelected());
//						fillhexagons();
//					}
//				});
//
//				mainPanel.getSimulation().setNode2Int(listNodes.get(i), i);
//				initialStatePerComponent[i] = new JComboBox();
//				initialStatePerComponent[i].setBounds(75 + xOffset,
//						30 + yOffset, 35, 25);
//				for (int maxValue = 0; maxValue < listNodes.get(i).getMax() + 1; maxValue++) {
//					initialStatePerComponent[i].addItem(maxValue);
//				}
//
//				Jcombo2Node.put(initialStatePerComponent[i], listNodes.get(i));
//
//				epithelium.setInitialState(
//						Jcombo2Node.get(initialStatePerComponent[i]), (byte) 0);
//
//				initialStatePerComponent[i]
//						.addActionListener(new ActionListener() {
//
//							@Override
//							public void actionPerformed(ActionEvent arg0) {
//								JComboBox src = (JComboBox) arg0.getSource();
//
//								fireInitialStateChange(src);
//							}
//						});
//
//				colorChooser.add(new ColorButton(mainPanel.componentsPanel,
//						listNodes.get(i)));
//				colorChooser.get(i).setBackground(
//						epithelium.getColor(listNodes.get(i)));
//				colorChooser.get(i).setBounds(120 + xOffset, 30 + yOffset, 20,
//						25);
//				properComponentsPanel.add(initialStatePerComponent[i]);
//				properComponentsPanel.add(nodeBox[i]);
//				properComponentsPanel.add(colorChooser.get(i));
//				properCount = properCount + 1;
//			}
//		}
//
//		/*
//		 * PerturbationsPanel : Here the perturbations are defined.
//		 * Perturbations at this point are performed by forcing a proper
//		 * component to assume an expression level, or a range of expression
//		 * values. Whenever a perturbation is performed the composed model does
//		 * not have to be recreated.
//		 */
//
//		JButton markPerturbation = new JButton("Mark");
//		JButton clearPerturbation = new JButton("Clear");
//		JButton clearAllPerturbation = new JButton("Clear All");
//		JComboBox perturbedComponent = new JComboBox();
//
//		JTextField viewPerturbation = new JTextField();
//
//		String control = "";
//		for (int i = 0; i < listNodes.size(); i++) {
//			if (!listNodes.get(i).isInput()) {
//				perturbedComponent.addItem(listNodes.get(i).getNodeID());
//				control = listNodes.get(i).getNodeID();
//			}
//		}
//
//		perturbedComponent.setSelectedItem(control);
//		selectedPerturbedComponent = string2Node.get(perturbedComponent
//				.getSelectedItem());
//
//		// final JPanel line1 = new JPanel();
//		// JPanel line2 = new JPanel();
//
//		JComboBox perturbedExpressionMin = getperturbedExpressionCombo();
//		JComboBox perturbedExpressionMax = getperturbedExpressionCombo();
//		perturbedExpressionMax.setSelectedItem(perturbedExpressionMax
//				.getItemCount() - 1);
//		perturbedComponent.setPreferredSize(new Dimension(60, 24));
//		perturbedExpressionMin.setPreferredSize(new Dimension(60, 24));
//		perturbedExpressionMax.setPreferredSize(new Dimension(60, 24));
//		viewPerturbation.setPreferredSize(new Dimension(100, 24));
//
//		perturbedComponent.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				JComboBox src = (JComboBox) arg0.getSource();
//				setSelectedPerturbedComponent((String) src.getSelectedItem());
//				resetMinAndMaxCombo(line1);
//			}
//		});
//
//		markPerturbation.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				setClearPerturbation(!getClearPerturbation());
//				setMarkPerturbation(!getMarkPerturbation());
//				if (getMarkPerturbation()) {
//
//				}
//			}
//		});
//
//		clearPerturbation.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				setMarkPerturbation(!getMarkPerturbation());
//				setClearPerturbation(!getClearPerturbation());
//			}
//		});
//
//		clearAllPerturbation.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				setMarkPerturbation(!getMarkPerturbation());
//				setClearPerturbation(!getClearPerturbation());
//				clearAllPerturbations();
//				fillhexagons();
//
//			}
//		});
//
//		line1.add(markPerturbation);
//		line2.add(clearPerturbation);
//		line2.add(clearAllPerturbation);
//		line1.add(perturbedComponent);
//		line1.add(perturbedExpressionMin);
//		line1.add(perturbedExpressionMax);
//		line2.add(viewPerturbation);
//		perturbationsPanel.add(line1);
//		perturbationsPanel.add(line2);
//
//		/*
//		 * options panel: This is the Panel that will have the buttons at the
//		 * bottom of the Frame. There is an option to: 1) mark all hexagons with
//		 * the components that are checked with the selected expression level 2)
//		 * clear all expression values for the checked components 3) Save the
//		 * setup conditions (Note that it will save the model, integration
//		 * function, grid dimensions, roll over option, and all that the user
//		 * has specified so far 4) Close will close the window and the
//		 * information will be locally saved 5) The fill button will allow for
//		 * the user to select an hexagon and all neighborhod cells that have no
//		 * expression value for the checked components will be filled with the
//		 * selected expression values.
//		 */
//
//		JButton buttonMarkAll = new JButton("Apply All");
//		JButton buttonClearAll = new JButton("Clear All");
//		JButton buttonSave = new JButton("Save");
//		JButton buttonClose = new JButton("Close");
//		JButton buttonFill = new JButton("Fill");
//
//		buttonFill.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//
//				fill();
//
//				// TO DO: Fill has to check for each component individually if
//				// they have non zero expression neighbors. The fact that one
//				// component closes doesn't mean that the others do
//			}
//		});
//
//		buttonMarkAll.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				markAllCells();
//
//			}
//		});
//
//		buttonClearAll.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				clearAllCells();
//
//			}
//		});
//
//		buttonClose.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				close();
//			}
//		});
//		buttonSave.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				save();
//
//			}
//		});
//
//		/*
//		 * ComposedPanel : In the Composed Panel there are all the
//		 * functionalities relating to the creation or modification of the
//		 * composed model. Perturbations, Inputs definitions and Roll Over
//		 * Options
//		 */
//
//		JCheckBox composedPanelActive = new JCheckBox();
//		composedPanelActive.setBackground(backgroundColor);
//		composedPanelActive.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				setComposedModelDisplay(!composedModelActive);
//			}
//		});
//
//		JLabel textFile = new JLabel();
//		textFile.setText("Create Composed Model");
//		composedPanel.add(composedPanelActive);
//		composedPanel.add(textFile);
//
//		/*
//		 * Construction of the hexagons Grid. This Grid is different from the
//		 * the one in the mainPanel because it allows for the painting of the
//		 * hexagons. It calls the DraPolygonM that allows that drawing.
//		 */
//
//		this.startX = 0;
//		this.startY = 0;
//		this.endX = 0;
//		this.endY = 0;
//
//		textArea = new JTextArea();
//		textArea.setEditable(false);
//
//		MapPanel.addMouseListener(new MouseAdapter() {
//			public void mouseEntered(MouseEvent e) {
//				textArea.setBackground(Color.red);
//				textArea.append("I'm here"
//						+ e.getComponent().getClass().getName());
//				textArea.setCaretPosition(textArea.getDocument().getLength());
//			}
//		});
//
//		setResizable(false);
//
//		/*
//		 * Add buttons to the options Panel
//		 */
//
//		optionsPanel.add(buttonMarkAll);
//		optionsPanel.add(buttonClearAll);
//		optionsPanel.add(buttonFill);
//		optionsPanel.add(buttonSave);
//		optionsPanel.add(buttonClose);
//
//		/*
//		 * Add Panels to the ContentPane of the Frame
//		 */
//
//		getContentPane().add(MapPanel);
//		getContentPane().add(properComponentsPanel);
//
//		composedPanel.add(inputsPanel);
//		composedPanel.add(perturbationsPanel);
//
//		getContentPane().add(composedPanel);
//		getContentPane().add(optionsPanel);
//
//		pack();
//		setLocationByPlatform(true);
//		setVisible(true);
//		setLocationRelativeTo(null);
//	}
//
//	public void save() {
//
//		// TODO: Provisory method. It will evolve to something more elaborate
//		JFileChooser fc = new JFileChooser();
//		PrintWriter out;
//		if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
//
//			try {
//
//				out = new PrintWriter(new FileWriter(fc.getSelectedFile()
//						.getAbsolutePath() + "_config.txt"));
//				createConfigFile(out);
//				out.close();
//
//				String zipFile = fc.getSelectedFile().getAbsolutePath()
//						+ ".zip";
//
//				String unitarySBML = "";
//				if (mainPanel.getEpithelium().isNewEpithelium()) {
//					unitarySBML = mainPanel.getEpithelium().getSBMLFilePath();
//				} else {
//					unitarySBML = mainPanel.getEpithelium().getSBMLLoadPath();
//
//				}
//				System.out.println("Unitary SBML" + unitarySBML);
//
//				String[] sourceFiles = {
//						fc.getSelectedFile().getAbsolutePath() + "_config.txt",
//						unitarySBML };
//
//				byte[] buffer = new byte[1024];
//				FileOutputStream fout = new FileOutputStream(zipFile);
//				ZipOutputStream zout = new ZipOutputStream(fout);
//
//				for (int i = 0; i < sourceFiles.length; i++) {
//					System.out.println("Adding " + sourceFiles[i]);
//					// create object of FileInputStream for source file
//					FileInputStream fin = new FileInputStream(sourceFiles[i]);
//					zout.putNextEntry(new ZipEntry(sourceFiles[i]));
//					int length;
//
//					while ((length = fin.read(buffer)) > 0) {
//						zout.write(buffer, 0, length);
//					}
//					zout.closeEntry();
//					fin.close();
//				}
//				zout.close();
//				System.out.println("Zip file has been created!");
//				File toDelete = new File(fc.getSelectedFile().getAbsolutePath()
//						+ "_config.txt");
//				toDelete.delete();
//
//				// File bundle = new File(fc
//				// .getSelectedFile().getAbsolutePath());
//				// FileOutputStream stream = new FileOutputStream(bundle);
//				// ZipOutputStream zos = new ZipOutputStream(stream);
//				// ZipEntry ze = null;
//				//
//				// ze = new ZipEntry("config.txt");
//				// ze.setSize((long) out.toString().getBytes().length);
//				// zos.setLevel(9);
//				// zos.putNextEntry(ze);
//				// zos.write(out.toString().getBytes(), 0,
//				// out.toString().getBytes().length);
//				// zos.closeEntry();
//				//
//				// File file = mainPanel.getEpithelium().getSBMLFile();
//				//
//				// ze = new
//				// ZipEntry(mainPanel.getEpithelium().getSBMLFilename());
//				// ze.setSize((long) sbmlFormat.toString().getBytes().length);
//				// zos.setLevel(9);
//				// zos.putNextEntry(ze);
//				// zos.write(file.toString().getBytes(), 0,
//				// file.toString().getBytes().length);
//				// zos.closeEntry();
//				//
//				// zos.finish();
//				// zos.close();
//
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//
//	}
//
//	private void createConfigFile(PrintWriter out) {
//
//		// TODO : Change PrintWriter to FileWriter - Tratamento de Excepções. o
//		// file writer espera ate ter uma quantiodade grande de dados para
//		// enviar tudo de uma vez. importante quando estamos numa ligaão remota
//
//		Topology topology = mainPanel.getTopology();
//		SphericalEpithelium epithelium = mainPanel.getEpithelium();
//
//		// SBML Filename
//		out.write("SN " + epithelium.getSBMLFilename() + "\n");
//
//		// Grid Dimensions
//		out.write("GD " + topology.getWidth() + "," + topology.getHeight()
//				+ "\n");
//
//		out.write("\n");
//		// Roll-Over option
//		out.write("RL " + topology.getRollOver() + "\n");
//
//		out.write("\n");
//		// InitialState
//		for (NodeInfo node : epithelium.getUnitaryModel().getNodeOrder()) {
//			if (!epithelium.isIntegrationComponent(node)) {
//				for (int value = 1; value < node.getMax() + 1; value++) {
//					out.write("IC "
//							+ node.getNodeID()
//							+ " "
//							+ epithelium.getUnitaryModel().getNodeOrder()
//									.indexOf(node) + " : " + value + " ( ");
//					int previous = 0;
//					int inDash = 0;
//					for (int instance = 0; instance < topology
//							.getNumberInstances(); instance++) {
//						if (epithelium.getGridValue(instance, node) == value) {
//							if (previous != instance - 1) {
//								if (inDash == 1) {
//									out.write("-" + previous + ",");
//								} else if (previous != 0) {
//									out.write(",");
//								}
//								out.write("" + instance);
//								inDash = 0;
//							} else {
//								inDash = 1;
//								if (instance == topology.getNumberInstances() - 1
//										& previous == instance - 1) {
//									out.write("-" + instance);
//								}
//							}
//							previous = instance;
//						}
//					}
//					out.write(" )\n");
//				}
//			}
//		}
//
//		out.write("\n");
//		// Integration Components
//		for (NodeInfo node : epithelium.getUnitaryModel().getNodeOrder()) {
//			if (epithelium.isIntegrationComponent(node)) {
//
//				for (byte value = 1; value < node.getMax() + 1; value++) {
//					out.write("IT "
//							+ node.getNodeID()
//							+ " "
//							+ epithelium.getUnitaryModel().getNodeOrder()
//									.indexOf(node) + " : " + value + " : "
//							+ epithelium.getIntegrationFunction(node, value)
//							+ "\n");
//				}
//
//			}
//
//		}
//
//		out.write("\n");
//		// Colors
//
//		for (NodeInfo node : epithelium.getUnitaryModel().getNodeOrder()) {
//			out.write("CL " + node.getNodeID() + " "
//					+ epithelium.getUnitaryModel().getNodeOrder().indexOf(node)
//					+ " : " + epithelium.getColor(node).getRGB() + "\n");
//		}
//
//		out.write("\n");
//		// Perturbations
//
//		// Priorities
//
//	}
//
//	protected void setComposedModelDisplay(boolean b) {
//		JCheckBox aux = new JCheckBox();
//		composedModelActive = b;
//		// for (Component c : line1.getComponents())
//		// c.setEnabled(composedModelActive);
//		// for (Component c : line2.getComponents())
//		// c.setEnabled(composedModelActive);
//		// for (Component c : inputsPanel.getComponents())
//		// c.setEnabled(composedModelActive);
//		// for (Component c : composedPanel.getComponents()) {
//		//
//		// if (c.getClass() != aux.getClass())
//		// c.setEnabled(composedModelActive);
//		// }
//	}
//
//	protected void clearAllPerturbations() {
//
//		for (int instance = 0; instance < topology.getNumberInstances(); instance++) {
//			// epithelium.setPerturbedInstance(instance, false);
//		}
//	}
////
////	public void setMarkPerturbation(boolean b) {
////		mainPanel.setMarkPerturbation(b);
////		// System.out.println(mainPanel.getMarkPerturbation());
////	}
////
////	public boolean getMarkPerturbation() {
////		return mainPanel.getMarkPerturbation();
////	}
////
////	public void setClearPerturbation(boolean b) {
////		mainPanel.setClearPerturbation(b);
////	}
////
////	public boolean getClearPerturbation() {
////		return mainPanel.getClearPerturbation();
////	}
//
//	protected void resetMinAndMaxCombo(JPanel line1) {
//		line1.remove(3);
//		line1.remove(2);
//		JComboBox perturbedExpressionMin = getperturbedExpressionCombo();
//		JComboBox perturbedExpressionMax = getperturbedExpressionCombo();
//
//		perturbedExpressionMax.setSelectedItem(perturbedExpressionMax
//				.getItemCount() - 1);
//
//		line1.add(perturbedExpressionMin);
//		line1.add(perturbedExpressionMax);
//		perturbedExpressionMin.setPreferredSize(new Dimension(60, 24));
//		perturbedExpressionMax.setPreferredSize(new Dimension(60, 24));
//		line1.repaint();
//		line1.revalidate();
//
//	}
//
//	private JComboBox getperturbedExpressionCombo() {
//		JComboBox perturbedExpressionCombo = new JComboBox();
//		for (int i = 0; i <= selectedPerturbedComponent.getMax(); i++)
//			perturbedExpressionCombo.addItem(i);
//		return perturbedExpressionCombo;
//	}
//
//	protected void setSelectedPerturbedComponent(String selectedItem) {
//
//		this.selectedPerturbedComponent = string2Node.get(selectedItem);
//
//	}
//
//	protected void setInitialSetupHasChanged(boolean b) {
//
//		mainPanel.setInitialSetupHasChanged(b);
//	}
//
//	protected void initializeIntegrationInterface(JButton src) {
//		NodeInfo node = integrationFunctionButton2Node.get(src);
//		new IntegrationFunctionInterface(this.epithelium, node);
//
//	}
//
//
//
//	protected void setEnvOptions(JComboBox inputCombo, boolean bool) {
//
//		node2Jcheck.get(JcomboInput2Node.get(inputCombo)).setEnabled(bool);
//		node2Jcheck.get(JcomboInput2Node.get(inputCombo)).setSelected(bool);
//		componentDisplay.put(JcomboInput2Node.get(inputCombo), bool);
//		fillhexagons();
//
//		node2Jcombo.get(JcomboInput2Node.get(inputCombo)).setVisible(bool);
//		node2Color.get(JcomboInput2Node.get(inputCombo)).setVisible(bool);
//		node2IntegrationFunctionButton.get(JcomboInput2Node.get(inputCombo))
//				.setVisible(!bool);
//		node2IntInput.put(JcomboInput2Node.get(inputCombo), !bool);
//
//		if (bool) {
//			epithelium.resetIntegrationNode(JcomboInput2Node.get(inputCombo));
//		} else {
//			for (int instance = 0; instance < mainPanel.getTopology()
//					.getNumberInstances(); instance++) {
//				epithelium.setGrid(instance, JcomboInput2Node.get(inputCombo),
//						(byte) 0);
//			}
//
//		}
//
//		// System.out.println(JcomboInput2Node.get(inputCombo)
//		// + " "
//		// + mainPanel.getEpithelium().getIntegrationComponents()
//		// .get(JcomboInput2Node.get(inputCombo)));
//	}
//
//	private void fireInitialStateChange(JComboBox combo) {
//
//		epithelium.setInitialState(Jcombo2Node.get(combo),
//				((Integer) combo.getSelectedItem()).byteValue());
//	}
//
//	// private void fireRollOverChange(String optionString) {
//	// topology.setRollOver(optionString);
//	// }
//
//	public void setComponentDisplay(NodeInfo nodeInfo, boolean b) {
//		componentDisplay.put(nodeInfo, b);
//	}
//
//	public void fillhexagons() {
//
//		Color color = Color.white;
//
//		int row;
//		int column;
//
//		for (int i = 0; i < topology.getNumberInstances(); i++) {
//
//			row = topology.instance2i(i, topology.getWidth());
//			column = topology.instance2j(i, topology.getHeight());
//
//			color = Color(row, column);
//			MapPanel.drawHexagon(row, column, MapPanel.getGraphics(), color);
//		}
//	}
//
//	public void close() {
//
//		mainPanel.refreshComponentsColors();
//
//		// for (NodeInfo node : listNodes) {
//		// if (node2IntInput.get(node)) {
//		// for (int instance = 0; instance < topology.getNumberInstances();
//		// instance++) {
//		// mainPanel.getGrid().getGrid().get(instance)
//		// .put(node, (byte) 0);
//		// }
//		// }
//		// }
//		mainPanel.componentsPanel.removeAll();
//		mainPanel.componentsPanel.init();
//		dispose();
//	}
//
//	public void initialize() {
//
//
//		MapPanel.paintComponent(MapPanel.getGraphics());
//
//		MapPanel.addMouseMotionListener(new MouseMotionListener() {
//
//			@Override
//			public void mouseDragged(MouseEvent arg0) {
//
//				int ind_it = (int) Math.floor((arg0.getX() / (1.5 * MapPanel.radius)));
//
//				double ind_yts = (arg0.getY() - (ind_it % 2) * MapPanel.height
//						/ 2);
//				double ind_jt = Math.floor(ind_yts / (MapPanel.height));
//
//				double xt = arg0.getX() - ind_it * (1.5 * MapPanel.radius);
//				double yt = ind_yts - ind_jt * (MapPanel.height);
//				int i = 0, j = 0;
//				int deltaj = 0;
//
//				if (yt > MapPanel.height / 2)
//					deltaj = 1;
//				else
//					deltaj = 0;
//
//				if (xt > MapPanel.radius
//						* Math.abs(0.5 - (yt / MapPanel.height))) {
//					i = (int) ind_it;
//					j = (int) ind_jt;
//
//				} else {
//					i = (int) ind_it - 1;
//					j = (int) (ind_jt - i % 2 + deltaj);
//				}
//
//				// The mouse is over a cell that belongs to the grid
//			
//					if (i < topology.getWidth() && j < topology.getHeight()
//							&& i >= 0 && j >= 0) {
//						color = Color();
//						MapPanel.drawHexagon(i, j, MapPanel.getGraphics(),
//								color);
//						setInitialState(i, j);
//						
//					}
//				
//			}
//
//			@Override
//			public void mouseMoved(MouseEvent arg0) {
//
//				int ind_it = (int) Math.floor((arg0.getX() / (1.5 * MapPanel.radius)));
//
//				double ind_yts = (arg0.getY() - (ind_it % 2) * MapPanel.height
//						/ 2);
//				double ind_jt = Math.floor(ind_yts / (MapPanel.height));
//
//				double xt = arg0.getX() - ind_it * (1.5 * MapPanel.radius);
//				double yt = ind_yts - ind_jt * (MapPanel.height);
//				int i = 0, j = 0;
//				int deltaj = 0;
//
//				if (yt > MapPanel.height / 2)
//					deltaj = 1;
//				else
//					deltaj = 0;
//
//				if (xt > MapPanel.radius
//						* Math.abs(0.5 - (yt / MapPanel.height))) {
//					i = (int) ind_it;
//					j = (int) ind_jt;
//
//				} else {
//					i = (int) ind_it - 1;
//					j = (int) (ind_jt - i % 2 + deltaj);
//				}
//				if (i < topology.getWidth() && j < topology.getHeight()
//						&& i >= 0 && j >= 0) {
//
//					getInitialState(i, j);
//				}
//			}
//		});
//
//		MapPanel.addMouseListener(new MouseListener() {
//
//			@Override
//			public void mouseClicked(MouseEvent arg0) {
//
//				int ind_it = (int) Math.floor((arg0.getX() / (1.5 * MapPanel.radius)));
//
//				double ind_yts = (arg0.getY() - (ind_it % 2) * MapPanel.height
//						/ 2);
//				double ind_jt = Math.floor(ind_yts / (MapPanel.height));
//
//				double xt = arg0.getX() - ind_it * (1.5 * MapPanel.radius);
//				double yt = ind_yts - ind_jt * (MapPanel.height);
//				int i = 0, j = 0;
//				int deltaj = 0;
//
//				if (yt > MapPanel.height / 2)
//					deltaj = 1;
//				else
//					deltaj = 0;
//
//				if (xt > MapPanel.radius
//						* Math.abs(0.5 - (yt / MapPanel.height))) {
//					i = (int) ind_it;
//					j = (int) ind_jt;
//
//				} else {
//					i = (int) ind_it - 1;
//					j = (int) (ind_jt - i % 2 + deltaj);
//				}
//
//				// The mouse is over a cell that belongs to the grid
//
//				if (i < topology.getWidth() && j < topology.getHeight()
//						&& i >= 0 && j >= 0) {
//					color = Color();
//					setInitialState(i, j);
//					MapPanel.drawHexagon(i, j, MapPanel.getGraphics(), color);
//
//				}
//
//			}
//
//			@Override
//			public void mouseEntered(MouseEvent arg0) {
//
//			}
//
//			@Override
//			public void mouseExited(MouseEvent arg0) {
//
//			}
//
//			@Override
//			public void mousePressed(MouseEvent arg0) {
//
//				startX = arg0.getX();
//				startY = arg0.getX();
//			}
//
//			@Override
//			public void mouseReleased(MouseEvent arg0) {
//
//				// endX = arg0.getX();
//				// endY = arg0.getX();
//				//
//				// int ind_it = (int) Math.floor((arg0.getX() / (1.5 *
//				// MapPanel.radius)));
//				//
//				// int ind_yts = (int) (arg0.getY() - (ind_it % 2)
//				// * MapPanel.height / 2);
//				// int ind_jt = (int) Math.floor(ind_yts / (MapPanel.height));
//				//
//				// int xt = (int) ((int) arg0.getX() - ind_it
//				// * (1.5 * MapPanel.radius));
//				// int yt = (int) (ind_yts - ind_jt * (MapPanel.height));
//				// int i = 0, j = 0;
//				// int deltaj = 0;
//				//
//				// if (yt > MapPanel.height / 2)
//				// deltaj = 1;
//				// else
//				// deltaj = 0;
//				//
//				// if (xt > MapPanel.radius
//				// * Math.abs(0.5 - (yt / MapPanel.height))) {
//				// i = (int) ind_it;
//				// j = (int) ind_jt;
//				//
//				// } else {
//				// i = (int) ind_it - 1;
//				// j = (int) (ind_jt - i % 2 + deltaj);
//				// }
//				//
//				// Rectangle a = new Rectangle(startX - ind_it, startY - ind_jt,
//				// Math.abs(endX - startX), Math.abs(endY - startY));
//				//
//				// MapPanel.getGraphics().drawRect(startX - ind_it,
//				// startY - ind_it, Math.abs(endX - startX),
//				// Math.abs(endY - startY));
//				//
//				// for (int instance = 0; instance <
//				// topology.getNumberInstances(); instance++) {
//				//
//				// }
//			}
//
//		});
//	}
//
//	/*
//	 * All cels within a selected space are filled with the value selecte
//	 */
//	public void fill() {
//
//		MapPanel.setBorder(BorderFactory.createLineBorder(Color.black));
//		// List[] neiList = new List[];
//		// if (i < topology.getWidth()
//		// && j < topology.getHeight()
//		// && i >= 0 && j >= 0) {
//		// color = Color();
//		// int instance = topology.coords2Instance(
//		// i, j);
//		//
//		// while (neiList!=[]){
//		// for (int h : topology.groupNeighbors(
//		// instance, 1)) {
//		//
//		// int m = topology.instance2i(h,
//		// topology.getWidth());
//		// int n = topology.instance2j(h,
//		// topology.getWidth());
//		//
//		// if (Color(m,n)=color.white){
//		// MapPanel.drawHexagon(m, n, MapPanel.getGraphics(),
//		// color);
//		//
//		// }
//		// }
//		//
//		// setFill(false);
//		//
//		// }
//		// }
//	}
//
//	public void getInitialState(int i, int j) {
//		Set<NodeInfo> a = mainPanel.getSimulation().getNode2Int().keySet();
//	}
//
//	public void setInitialState(int i, int j) {
//		List<NodeInfo> a = epithelium.getUnitaryModel().getNodeOrder();
//
//		int instance = topology.coords2Instance(i, j);
//		for (NodeInfo node : a) {
//
//			// System.out.println(componentDisplay + " " + a2.getNodeID());
//			if (componentDisplay.get(node)) {
//
//				epithelium.setGrid(instance, node,
//						epithelium.getInitialState(node));
//			}
//		}
//	}
//
//	public Color Color() {
//
//		int red = 255;
//		int green = 255;
//		int blue = 255;
//		color = new Color(red, green, blue);
//
//		Set<NodeInfo> a = componentDisplay.keySet();
//
//		for (NodeInfo a2 : a) {
//			if (componentDisplay.get(a2)) {
//				int value = epithelium.getInitialState(a2);
//
//				if (value > 0) {
//					color = epithelium.getColor(a2);
//					color = ColorBrightness(color, value);
//
//					red = (red + color.getRed()) / 2;
//					green = (green + color.getGreen()) / 2;
//					blue = (blue + color.getBlue()) / 2;
//					color = new Color(red, green, blue);
//				}
//
//				else if (value == 0) {
//					color = new Color(red, green, blue);
//				}
//			}
//		}
//		return color;
//
//	}
//
//	public Color Color(int i, int j) {
//
//		int red = 255;
//		int green = 255;
//		int blue = 255;
//		color = new Color(red, green, blue);
//
//		Set<NodeInfo> a = componentDisplay.keySet();
//
//		int instance = topology.coords2Instance(i, j);
//
//		for (NodeInfo node : a) {
//			if (componentDisplay.get(node)) {
//
//				int value = epithelium.getGridValue(instance, node);
//				;
//
//				if (value > 0) {
//					color = epithelium.getColor(node);
//					color = ColorBrightness(color, value);
//
//					red = (red + color.getRed()) / 2;
//					green = (green + color.getGreen()) / 2;
//					blue = (blue + color.getBlue()) / 2;
//					color = new Color(red, green, blue);
//				} else if (value == 0) {
//					color = new Color(red, green, blue);
//				}
//			}
//		}
//		return color;
//
//	}
//
//	public Color ColorBrightness(Color color, int value) {
//		if (value > 0) {
//
//			for (int j = 2; j <= value; j++) {
//				color = color.darker();
//			}
//		} else if (value == 0) {
//			color = Color.white;
//		}
//		return color;
//
//	}
//
//	public void clearAllCells() {
//		// adicionar try catch para textFx e fy
//		epithelium.initializeGrid();
//		for (int i = 0; i < topology.getWidth(); i++) {
//			for (int j = 0; j < topology.getHeight(); j++) {
//				// mainPanel.getSimulation().initializeInitialStates();
//				MapPanel.drawHexagon(i, j, MapPanel.getGraphics(), Color.white);
//			}
//		}
//	}
//
//	public void markAllCells() {
//
//		// clearAllCells(cells);
//		for (int i = 0; i < topology.getWidth(); i++) {
//			for (int j = 0; j < topology.getHeight(); j++) {
//				setInitialState(i, j);
//				MapPanel.drawHexagon(i, j, MapPanel.getGraphics(), Color());
//			}
//		}
//	}
//
//	private enum InputOption {
//		ENVIRONMENTAL_INPUT, INTEGRATION_INPUT;
//
//		public static String getDescriptionString(InputOption option) {
//			switch (option) {
//			case ENVIRONMENTAL_INPUT:
//				return "Env";
//			case INTEGRATION_INPUT:
//				return "Int";
//			default:
//				return "";
//			}
//		}
//
//		public static InputOption getOptionFromString(String optionString) {
//			if (optionString.equals(InputOption
//					.getDescriptionString(ENVIRONMENTAL_INPUT)))
//				return ENVIRONMENTAL_INPUT;
//			else if (optionString.equals(InputOption
//					.getDescriptionString(INTEGRATION_INPUT)))
//				return INTEGRATION_INPUT;
//			else
//				return null;
//		}
//	}
//
//	public void resetInitialConditions() {
//		Jcombo2Node = new Hashtable<JComboBox, NodeInfo>();
//		Jcheck2Node = new Hashtable<JCheckBox, NodeInfo>();
//		JcomboInput2Node = new Hashtable<JComboBox, NodeInfo>();
//
//		node2Jcombo = new Hashtable<NodeInfo, JComboBox>();
//		node2Jcheck = new Hashtable<NodeInfo, JCheckBox>();
//		// node2JcomboInput = new Hashtable<NodeInfo, JComboBox>();
//
//		// color2Node = new Hashtable<ColorButton, NodeInfo>();
//		node2Color = new Hashtable<NodeInfo, ColorButton>();
//
//		node2IntInput = new Hashtable<NodeInfo, Boolean>();
//
//		componentDisplay = new Hashtable<NodeInfo, Boolean>();
//
//		// integrationComponents = new Hashtable<NodeInfo, Boolean>();
//		integrationFunctionButton2Node = new Hashtable<JButton, NodeInfo>();
//	}
//
//}
