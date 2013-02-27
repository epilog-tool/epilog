package pt.gulbenkian.igc.nmd;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;
import org.colomoto.logicalmodel.io.sbml.SBMLFormat;

import composition.IntegrationFunctionMapping;
import composition.Topology;

public class MainPanel extends JFrame implements CompositionDialog,
		SimulationDialog {

	/**
	 * 
	 */

	private static final long serialVersionUID = -685430043793074531L;

	private static int DEFAULT_WIDTH = 6;
	private static int DEFAULT_HEIGHT = 6;
	private static JTextField userDefinedWidth = new JTextField();
	private static JTextField userDefinedHeight = new JTextField();
	private static JLabel selectedFilenameLabel = new JLabel();
	private JFileChooser fc = new JFileChooser();
	public static LogicalModel model = null;
	
	
	public Color colors[] = { Color.orange, Color.green, Color.blue,
			Color.pink, Color.yellow, Color.magenta, Color.cyan, Color.red,
			Color.LIGHT_GRAY, Color.black };
	public static JCheckBox nodeBox[];
	public ColorButton colorChooser[];
	static MapColorPanel buttonPanel = null;
	public static int numberOfNodes;
	public List<NodeInfo> listNodes;
	public JTextField initialState[];
		public static ArrayList<Integer> initialStateArray = null;
	public static ArrayList<Integer> userDefinedInitialState = new ArrayList<Integer>();	
	
	public static DrawPolygon hexagonsPanel = null;
	
	static Container contentPanel = new JPanel();
	
	

	
	private Epithelium epithelium = new SphericalEpithelium(DEFAULT_WIDTH,
			DEFAULT_HEIGHT);
	public MainPanel mainPanel = this;



	public void initialize() throws Exception {

		UIManager.setLookAndFeel(UIManager
				.getCrossPlatformLookAndFeelClassName());

		userDefinedWidth.setHorizontalAlignment(JTextField.CENTER);
		userDefinedHeight.setHorizontalAlignment(JTextField.CENTER);
		userDefinedWidth.setText("" + DEFAULT_WIDTH);
		userDefinedHeight.setText("" + DEFAULT_HEIGHT);

		userDefinedWidth.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) {
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				sanityCheckDimension(userDefinedWidth);
			}
		});

		userDefinedHeight.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) {
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				sanityCheckDimension(userDefinedHeight);
			}
		});

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		setupMainPanel();

	}

	private void loadModel() {

		File file = fc.getSelectedFile();
		SBMLFormat sbmlFormat = new SBMLFormat();
		LogicalModel logicalModel = null;

		try {
			logicalModel = sbmlFormat.importFile(file);
		} catch (IOException e) {
			System.err.println("Cannot import file " + file.getAbsolutePath()
					+ ": " + e.getMessage());
		}

		if (logicalModel == null)
			return;

		epithelium.setUnitaryModel(logicalModel);
		getButtonPanel();

	}

	public static int getGridWidth() {
		// sanityCheckDimension(userDefinedWidth);
		return Integer.parseInt(userDefinedWidth.getText());
	}

	public static int getGridHeight() {
		// sanityCheckDimension(userDefinedHeight);
		return Integer.parseInt(userDefinedHeight.getText());
	}

	private void sanityCheckDimension(JTextField userDefined) {
		String dimString = userDefined.getText();
		int w = Integer.parseInt(dimString);
		w = (w % 2 == 0) ? w : w + 1;
		userDefined.setText("" + w);
	}

	public void setmodel(LogicalModel chosenmodel) {
		model = chosenmodel;
	}

	public static LogicalModel getmodel() {
		return model;
	}

	public void setInitialState(ArrayList<Integer> userDefinedInitialState2) {
		initialStateArray = userDefinedInitialState2;

	}

	public static ArrayList<Integer> getInitialState() {
		return initialStateArray;
	}

	public void setupInitialState(int NodeIndex) {
		LogicalModel model = epithelium.getUnitaryModel();
		setmodel(model);
		listNodes = model.getNodeOrder();
		userDefinedInitialState = new ArrayList<Integer>();

		if (!listNodes.get(NodeIndex).isInput()) {
			initialState[NodeIndex] = new JTextField("0");
			initialState[NodeIndex].setBounds(60, 10 + NodeIndex * 60, 20, 30);
			buttonPanel.add(initialState[NodeIndex]);

			userDefinedInitialState.add(Integer
					.parseInt(initialState[NodeIndex].getText()));

		}
	}

	public MapColorPanel getButtonPanel() {

		buttonPanel = new MapColorPanel(Color.white);
		buttonPanel.setBounds(530, 130, 500, 500);
		buttonPanel.setBackground(Color.white);
		buttonPanel.setLayout(null);

		LogicalModel model = epithelium.getUnitaryModel();
		setmodel(model);

		if (model != null) {
			MainPanelDescription.setupOptionsRunPanel();
			MainPanelDescription.setupOptionsStartPanel();
			MainPanelDescription.setupRollOverPanel();
			MainPanelDescription.setupIterationNumberPanel();

			listNodes = model.getNodeOrder();
			userDefinedInitialState = new ArrayList<Integer>();

			nodeBox = new JCheckBox[listNodes.size()];
			initialState = new JTextField[listNodes.size()];

			colorChooser = new ColorButton[listNodes.size()];
			hexagonsPanel.initializeCellGenes(listNodes.size());
			final int size = listNodes.size();

			for (int i = 0; i < listNodes.size(); i++) {

				nodeBox[i] = new JCheckBox(listNodes.get(i).getNodeID());
				nodeBox[i].setBackground(Color.white);
				nodeBox[i].setBounds(10, 10 + i * 60, 50, 30);

				setupInitialState(i);

				final JCheckBox checkbox = nodeBox[i];

				nodeBox[i].addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent event) {

						if (checkbox.isSelected())
							hexagonsPanel.countSelected++;
						else
							hexagonsPanel.countSelected--;
						hexagonsPanel.initializeCellGenes(size);
						hexagonsPanel.paintComponent(hexagonsPanel
								.getGraphics());

					}

				});

				colorChooser[i] = new ColorButton(MainPanel.buttonPanel,
						hexagonsPanel);
				colorChooser[i].setBounds(90, 10 + i * 60, 20, 30);
				colorChooser[i].setBackground(colors[i]);
				JComboBox comboBox = null;
				buttonPanel.add(nodeBox[i]);

				buttonPanel.add(colorChooser[i]);

				final String nodeId = listNodes.get(i).getNodeID();
				// String SBMLpath = ;
				final int maxId = listNodes.get(i).getMax();

				if (listNodes.get(i).isInput()) {
					comboBox = new JComboBox();
					comboBox.setBounds(130, 10 + i * 60, 120, 30);
					comboBox.addItem("Select input");
					comboBox.addItem(InputOption
							.getDescriptionString(InputOption.ENVIRONMENTAL_INPUT));
					comboBox.addItem(InputOption
							.getDescriptionString(InputOption.INTEGRATION_INPUT));
					buttonPanel.add(comboBox);

					final JPanel jpanel = new JPanel();
					jpanel.setBackground(Color.white);
					jpanel.setBounds(250, 10 + i * 60, 300, 60);
					jpanel.setLayout(null);
					final ColorButton colorChooserBtn = colorChooser[i];
					comboBox.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent event) {
							JComboBox source = (JComboBox) event.getSource();
							String optionString = (String) source
									.getSelectedItem();

							InputOption option = InputOption
									.getOptionFromString(optionString);

							if (option != null) {
								switch (option) {
								case ENVIRONMENTAL_INPUT: {

									JButton btnDraw = new JButton("Draw");
									btnDraw.setBounds(10, 0, 100, 30);
									btnDraw.removeActionListener(null);
									btnDraw.addActionListener(new ActionListener() {
										public void actionPerformed(
												ActionEvent e) {
											final Map map = new Map(
													getGridWidth(),
													getGridHeight(),
													colorChooserBtn
															.getBackground(),
													nodeId, maxId, mainPanel);
											map.initialize();
											map.initializeCells(map.cells);
											map.panelLights.removeAll();
											JButton btnAll = new JButton(
													"Mark All");
											JButton btnClearAll = new JButton(
													"Clear All");
											btnAll.setBounds(0, 0, 100, 20);
											btnClearAll.setBounds(0, 110, 100,
													30);
											btnAll.addActionListener(new ActionListener() {
												public void actionPerformed(
														ActionEvent e) {
													map.markAllCells(map.cells);

												}
											});

											btnClearAll
													.addActionListener(new ActionListener() {
														public void actionPerformed(
																ActionEvent e) {
															map.clearAllCells(map.cells);

														}
													});

											map.panelLights.add(btnAll);
											map.panelLights.add(btnClearAll);
										}
									});

									JButton btnLoad = new JButton("Load");
									btnLoad.setBounds(120, 0, 100, 30);
									btnLoad.addActionListener(new ActionListener() {
										public void actionPerformed(
												ActionEvent e) {
											JFileChooser fc = new JFileChooser();
											fc.setDialogTitle("Choose file");
											int open = fc.showOpenDialog(null);
											if (open == 0) {
												File file = fc
														.getSelectedFile();
												System.out
														.println(colorChooserBtn
																.getBackground());
												final Map map = new Map(
														getGridWidth(),
														getGridHeight(),
														colorChooserBtn
																.getBackground(),
														nodeId, maxId,
														mainPanel);
												final ArrayList<ArrayList<Cell>> cells = DrawPolygonM.getMappedCells(file
														.getAbsolutePath());
												map.MapPanel.cells = cells;
												map.cells = cells;

												map.initialize();
												map.panelLights.removeAll();
												JButton btnAll = new JButton(
														"Mark All");
												JButton btnClearAll = new JButton(
														"Clear All");
												btnAll.setBounds(0, 0, 100, 30);
												btnClearAll.setBounds(0, 110,
														100, 30);
												btnAll.addActionListener(new ActionListener() {
													public void actionPerformed(
															ActionEvent e) {
														map.markAllCells(map.cells);

													}
												});

												btnClearAll
														.addActionListener(new ActionListener() {
															public void actionPerformed(
																	ActionEvent e) {
																map.clearAllCells(map.cells);

															}
														});

												map.panelLights.add(btnAll);
												map.panelLights
														.add(btnClearAll);
											}
										}
									});

									jpanel.removeAll();
									jpanel.revalidate();
									jpanel.repaint();
									jpanel.add(btnDraw);
									jpanel.add(btnLoad);
									buttonPanel.add(jpanel);
									buttonPanel.revalidate();
									buttonPanel.repaint();

								}

									break;

								case INTEGRATION_INPUT: {

									final JTextField textFormula = new JTextField();
									textFormula.setBounds(10, 0, 150, 30);
									jpanel.removeAll();
									jpanel.add(textFormula);
									jpanel.revalidate();
									jpanel.repaint();
									buttonPanel.add(jpanel);
									buttonPanel.revalidate();
									buttonPanel.repaint();

									textFormula
											.addActionListener(new ActionListener() {
												@Override
												public void actionPerformed(
														ActionEvent e) {
													String logicalFunction = textFormula
															.getText();
													String[] result = logicalFunction
															.split(";");

													System.out
															.println(result[0]);
													System.out
															.println(result[1]);
													try {

														// ANTLRDemo.Aeval(result);
														ANTLRDemo
																.teste(result[0]);
													} catch (Exception e1) {
														// TODO Auto-generated
														// catch block
														e1.printStackTrace();
													}
												}
											});

									// IntegrationFunctionSpecification.IntegrationExpression
									// expression = null;

									// RegulatoryIntegration (example, model);
								}
									break;
								default: {
									jpanel.removeAll();
									jpanel.revalidate();
									jpanel.repaint();
									buttonPanel.add(jpanel);
									buttonPanel.revalidate();
									buttonPanel.repaint();
								}
									break;
								}
							}
						}

					});
				}
			}
			setInitialState(userDefinedInitialState);

		}

		return buttonPanel;
	}

	private enum InputOption {
		ENVIRONMENTAL_INPUT, INTEGRATION_INPUT;

		public static String getDescriptionString(InputOption option) {
			switch (option) {
			case ENVIRONMENTAL_INPUT:
				return "Env input";
			case INTEGRATION_INPUT:
				return "Int input";
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

	public void setupMainPanel() {
		
		contentPanel = getContentPane();

		setSize(1100, 600);
		contentPanel.setPreferredSize(new Dimension(1100, 600));
		// contentPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY,2));
		contentPanel.setBackground(Color.white);
		this.setResizable(true);

		userDefinedHeight.setColumns(20);
		userDefinedWidth.setColumns(20);

		userDefinedWidth.setBounds(10, 20, 40, 20);
		userDefinedHeight.setBounds(70, 20, 40, 20);

		contentPanel.add(userDefinedWidth);
		contentPanel.add(userDefinedHeight);

		JButton btnClose = new JButton("Close");
		btnClose.setBounds(900, 500, 100, 30);
		contentPanel.add(btnClose);
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		hexagonsPanel = new DrawPolygon(this);
		// hexagonsPanel.setBorder(BorderFactory.createLineBorder(Color.blue,5));
		// hexagonsPanel.setBackground(Color.cyan);
		hexagonsPanel.setBounds(10, 50, 500, 500);

		contentPanel.setLayout(null);
		contentPanel.add(hexagonsPanel);

		buttonPanel = getButtonPanel();
		contentPanel.add(buttonPanel);

		JButton btnRestart = new JButton("Restart");
		btnRestart.setBounds(120, 13, 100, 30);

		btnRestart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainPanelDescription.paintHexagons();
				MainPanelDescription.cleanButtonPanel();
				MainPanelDescription.cleanOptionsRunPanel();
				MainPanelDescription.cleanOptionsStartPanel();
				MainPanelDescription.cleanIterationNumberPanel();
				repaint();

				if (MainPanelDescription.RollOverPanel != null)
					MainPanelDescription.cleanRollOverPanel();
				selectedFilenameLabel.setText("");
			}
		});

		contentPanel.add(btnRestart);

		JButton btnModel = new JButton("Model");
		btnModel.setBounds(230, 13, 100, 30);

		selectedFilenameLabel.setBounds(335, 13, 100, 30);
		contentPanel.add(selectedFilenameLabel);

		btnModel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				askModel();
				teste.exp();

			}

		});

		contentPanel.add(btnModel);

		
		// Adding overall ScrollPane
		JScrollPane scrollPane = new JScrollPane(getContentPane());
		setContentPane(scrollPane);
		
		
		setContentPane(contentPanel);
		pack();
		setVisible(true);
		setLocationRelativeTo(null);

	}

	private void askModel() {
		fc.setDialogTitle("Choose file");

		if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			selectedFilenameLabel.setText(fc.getSelectedFile().getName());
			loadModel();
			contentPanel.removeAll();
			setupMainPanel();
			teste.exp();

		}
	}

	@Override
	public byte[] getInitialUnitaryValues() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] getEnvironmentalValues() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SimulationType getSimulationType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Topology getTopology() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IntegrationFunctionMapping getMapping() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LogicalModel getUnitaryModel() {
		// TODO Auto-generated method stub
		return null;
	}

}
