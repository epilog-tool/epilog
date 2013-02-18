package pt.gulbenkian.igc.nmd;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;
import org.colomoto.logicalmodel.io.sbml.SBMLFormat;
import org.ginsim.servicegui.tool.composition.integrationgrammar.IntegrationFunctionSpecification;
import org.ginsim.servicegui.tool.composition.integrationgrammar.IntegrationFunctionSpecification.IntegrationExpression;

import composition.integrationgrammar.IntegrationGrammarLexer;
import composition.integrationgrammar.IntegrationGrammarParser;
import composition.RegulatoryIntegration;

public class MainPanel extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -685430043793074531L;

	private static int DEFAULT_WIDTH = 5;
	private static int DEFAULT_HEIGHT = 5;
	private static JTextField userDefinedWidth = new JTextField();
	private static JTextField userDefinedHeight = new JTextField();
	public Color colors[] = { Color.orange, Color.green, Color.blue,
			Color.pink, Color.yellow, Color.magenta, Color.cyan, Color.red,
			Color.LIGHT_GRAY, Color.black };
	private JFileChooser fc = new JFileChooser();

	static DrawPolygon hexagonsPanel = null;
	static JPanel contentPanel = new JPanel();
	static MapColorPanel buttonPanel = null;

	private JLabel selectedFilenameLabel = new JLabel();

	private Epithelium epithelium = new SphericalEpithelium(DEFAULT_WIDTH,
			DEFAULT_HEIGHT);

	public void initialize() throws Exception {

		UIManager.setLookAndFeel(UIManager
				.getCrossPlatformLookAndFeelClassName());

		userDefinedWidth.setText("" + DEFAULT_WIDTH);
		userDefinedHeight.setText("" + DEFAULT_HEIGHT);

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

		epithelium.setModel(logicalModel);
		getButtonPanel();

	}

	public static int getGridWidth() {
		String widthString = userDefinedWidth.getText();
		int x = Integer.parseInt(widthString);
		if (x % 2 != 0)
			x = x + 1;
		return x;
	}

	public static int getGridHeight() {
		String heightString = userDefinedHeight.getText();
		int x = Integer.parseInt(heightString);
		if (x % 2 != 0)
			x = x + 1;
		return x;
	}

	public MapColorPanel getButtonPanel() {

		MainPanel.buttonPanel = new MapColorPanel(Color.white);
		MainPanel.buttonPanel.setBounds(530, 130, 500, 500);
		MainPanel.buttonPanel.setBackground(Color.white);
		MainPanel.buttonPanel.setLayout(null);

		final LogicalModel model = epithelium.getModel();


		if (model != null) {
			MainPanelDescription.setupOptionsRunPanel();
			MainPanelDescription.setupOptionsStartPanel();

			List<NodeInfo> listNodes = model.getNodeOrder();

			for (NodeInfo node : listNodes) {
				final int position = listNodes.indexOf(node);

				JCheckBox nodeBox = new JCheckBox(node.getNodeID());
				nodeBox.setBackground(Color.white);
				nodeBox.setBounds(10, 10 + position * 60, 50, 30);
				final ColorButton colorChooser = new ColorButton(
						MainPanel.buttonPanel);
				colorChooser.setBounds(60, 10 + position * 60, 30, 30);
				colorChooser.setBackground(colors[position]);
				JComboBox comboBox = null;
				buttonPanel.add(nodeBox);
				buttonPanel.add(colorChooser);
				//nodeBox.addItemListener(this);
				

				final String nodeId = listNodes.get(position).getNodeID();
				// String SBMLpath = ;
				final int maxId = listNodes.get(position).getMax();

				if (node.isInput()) {
					comboBox = new JComboBox();

					comboBox.setBounds(100, 10 + position * 60, 120, 30);
					comboBox.addItem("Select input");
					comboBox.addItem(InputOption
							.getDescriptionString(InputOption.ENVIRONMENTAL_INPUT));
					comboBox.addItem(InputOption
							.getDescriptionString(InputOption.INTEGRATION_INPUT));
					buttonPanel.add(comboBox);

					final JPanel jpanel = new JPanel();
					jpanel.setBackground(Color.white);
					jpanel.setBounds(230, 10 + position * 60, 300, 60);
					jpanel.setLayout(null);

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
									btnDraw.setBounds(0, 0, 100, 30);
									btnDraw.removeActionListener(null);
									btnDraw.addActionListener(new ActionListener() {
										public void actionPerformed(
												ActionEvent e) {
											final Map map=	new Map(getGridWidth(),
													getGridHeight(),
													colorChooser
													.getBackground(),
													nodeId, maxId);
											map.initialize();
											map.initializeCells(map.cells);
											map.panelLights.removeAll();
											JButton btnAll=new JButton("Mark All");
											JButton btnClearAll=new JButton("Clear All");
											btnAll.setBounds(0,0,100,30);
											btnClearAll.setBounds(0,110,100,30);
											btnAll.addActionListener(new ActionListener() {
												public void actionPerformed(
														ActionEvent e){
													map.markAllCells(map.cells);

												}
											});

											btnClearAll.addActionListener(new ActionListener() {
												public void actionPerformed(
														ActionEvent e){
													map.clearAllCells(map.cells);

												}
											});


											map.panelLights.add(btnAll);
											map.panelLights.add(btnClearAll);
										}
									});

									JButton btnLoad = new JButton("Load");
									btnLoad.setBounds(110, 0, 100, 30);
									btnLoad.addActionListener(new ActionListener() {
										public void actionPerformed(
												ActionEvent e) {
											JFileChooser fc = new JFileChooser();

											fc.setDialogTitle("Choose file");
											int open = fc.showOpenDialog(null);
											if (open == 0) {
												File file = fc
														.getSelectedFile();

												System.out.println(colorChooser
														.getBackground());
												final Map map = new Map(
														getGridWidth(),
														getGridHeight(),
														colorChooser
														.getBackground(),
														nodeId, maxId);
												final ArrayList<ArrayList<Cell>> cells = DrawPolygon.getMappedCells(file
														.getAbsolutePath());
												map.MapPanel.cells=cells;
												map.cells=cells;
												map.initialize();
												map.panelLights.removeAll();
												JButton btnAll=new JButton("Mark All");
												JButton btnClearAll=new JButton("Clear All");
												btnAll.setBounds(0,0,100,30);
												btnClearAll.setBounds(0,110,100,30);
												btnAll.addActionListener(new ActionListener() {
													public void actionPerformed(
															ActionEvent e){
														map.markAllCells(map.cells);

													}
												});

												btnClearAll.addActionListener(new ActionListener() {
													public void actionPerformed(
															ActionEvent e){
														map.clearAllCells(map.cells);

													}
												});


												map.panelLights.add(btnAll);
												map.panelLights.add(btnClearAll);


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
									textFormula.setBounds(0, 0, 150, 30);
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
													try {
														IntegrationExpression expression = expressionCreator(logicalFunction);
														//RegulatoryIntegration (expression, model);
													} catch (Exception e1) {
														// TODO Auto-generated catch block
														e1.printStackTrace();
													}
													
												}
											});
									
						
									//RegulatoryIntegration (example, model);
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
		}

		return buttonPanel;
	}
	
	public static IntegrationExpression expressionCreator(String logicalFunction) throws Exception {
		ANTLRStringStream in = new ANTLRStringStream(logicalFunction);
		IntegrationGrammarLexer lexer = new IntegrationGrammarLexer(in);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		IntegrationGrammarParser parser = new IntegrationGrammarParser(tokens);
		return parser.eval();
		
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

		contentPanel.setPreferredSize(new Dimension(1100, 600));
		// contentPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY,2));
		contentPanel.setBackground(Color.white);
		this.setResizable(false);

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
			}
		});

		contentPanel.add(btnRestart);
		//HashSet<Integer> a = Topology.groupNeighbors(14, 2);
		HashSet<Integer> a  = Topology.neighborsOneDistanceAway(14);
		//System.out.println("Here we are"+a);
		//Topology.testNeighbors(14, 1);
		
		JButton btnModel = new JButton("Model");
		btnModel.setBounds(230, 13, 100, 30);

		selectedFilenameLabel.setBounds(335, 13, 100, 30);
		contentPanel.add(selectedFilenameLabel);

		btnModel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				askModel();

			}

		});

		contentPanel.add(btnModel);

		setContentPane(contentPanel);
		pack();

		// setLocationByPlatform(true);
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

		}
	}

	
}
