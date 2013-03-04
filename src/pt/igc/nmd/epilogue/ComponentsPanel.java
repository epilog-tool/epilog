package pt.igc.nmd.epilogue;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;

public class ComponentsPanel extends MapColorPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Color colors[] = { Color.orange, Color.green, Color.blue,
			Color.pink, Color.yellow, Color.magenta, Color.cyan, Color.red,
			Color.LIGHT_GRAY, Color.black };
	public static JCheckBox nodeBox[];
	public static ColorButton colorChooser[];
	static MapColorPanel buttonPanel;
	public static int numberOfNodes;
	public static List<NodeInfo> listNodes;
	public JTextField initialState[];
	public static ArrayList<Integer> initialStateArray = null;
	public static ArrayList<Integer> userDefinedInitialState = new ArrayList<Integer>();
	public static DrawPolygon hexagonsPanel;
	private Epithelium epithelium;

	LogicalModel model = null;
	private MainPanel mainPanel;

	JButton componentsButton;

	public ComponentsPanel(MainPanel mainPanel, Color color) {
		super(color);
		this.mainPanel = mainPanel;

	}

	public void init() {
		hexagonsPanel = this.mainPanel.hexagonsPanel;
		setLayout(null);
		setBackground(Color.white);
		model = mainPanel.getEpithelium().getUnitaryModel();
		if (model != null) {

			listNodes = model.getNodeOrder();
			nodeBox = new JCheckBox[listNodes.size()];
			colorChooser = new ColorButton[listNodes.size()];
			hexagonsPanel.initializeCellGenes(listNodes.size());
			final int size = listNodes.size();

			for (int i = 0; i < listNodes.size(); i++) {
				
				nodeBox[i] = new JCheckBox(listNodes.get(i).getNodeID());
				nodeBox[i].setBackground(Color.white);
				nodeBox[i].setBounds(10, 10 + i * 60, 50, 30);

				final JCheckBox checkbox = nodeBox[i];
				final String nodeID=listNodes.get(i).getNodeID();
				
				mainPanel.getEpithelium().setActiveComponents(nodeID, false);
				
				nodeBox[i].addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent event) {

						if (checkbox.isSelected()){
							mainPanel.getEpithelium().setActiveComponents(nodeID, true);
							hexagonsPanel.countSelected++;
						}
						else
							hexagonsPanel.countSelected--;
						hexagonsPanel.initializeCellGenes(size);
						hexagonsPanel.paintComponent(hexagonsPanel
								.getGraphics());

					}

				});

				colorChooser[i] = new ColorButton(this, hexagonsPanel);
				colorChooser[i].setBounds(90, 10 + i * 60, 20, 30);
				colorChooser[i].setBackground(colors[i]);
				
				mainPanel.getEpithelium().setColor(listNodes.get(i).getNodeID(), colors[i]);
				
				JComboBox comboBox = null;
				add(nodeBox[i]);

				add(colorChooser[i]);

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
					add(comboBox);

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
											final Map map = new Map(mainPanel.getTopology()
													.getWidth(), mainPanel.getTopology()
													.getHeight(),
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
														mainPanel.getTopology()
																.getWidth(),
																mainPanel.getTopology()
																.getHeight(),
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
									add(jpanel);
									revalidate();
									repaint();

								}

									break;

								case INTEGRATION_INPUT: {

									final JTextField textFormula = new JTextField();
									textFormula.setBounds(10, 0, 150, 30);
									jpanel.removeAll();
									jpanel.add(textFormula);
									jpanel.revalidate();
									jpanel.repaint();
									add(jpanel);
									revalidate();
									repaint();

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
									add(jpanel);
									revalidate();
									repaint();
								}
									break;
								}
							}
						}

					});
				}
			}
			// setInitialState(userDefinedInitialState);

		}
		//
		// // componentsButton = new JButton("Work in Progress");
		// // add(componentsButton);
		// return buttonPanel;
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
}
