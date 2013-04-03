package pt.igc.nmd.epilogue;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextArea;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;

public class InitialConditions extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JTextArea textArea;

	private Color color;
	public ArrayList<ArrayList<Cell>> cells;
	private int startX;
	private int startY;
	private int endX;
	private int endY;
	private JComboBox rollOver;

	private MainPanel mainPanel;
	private LogicalModel model;

	private JPanel properComponentsPanel;
	private JPanel inputsPanel;
	private JPanel perturbationsPanel;
	private JPanel optionsPanel;
	private DrawPolygonM MapPanel;

	// Nodes Information

	private List<NodeInfo> listNodes;
	private Color colors[] = { Color.orange, Color.green, Color.blue,
			Color.pink, Color.yellow, Color.magenta, Color.cyan, Color.red,
			Color.LIGHT_GRAY, Color.black };
	private JCheckBox nodeBox[];
	private ArrayList<ColorButton> colorChooser;
	private JComboBox[] initialStatePerComponent;

	private Hashtable<JComboBox, NodeInfo> Jcombo2Node;
	private Hashtable<JCheckBox, NodeInfo> Jcheck2Node;

	private Hashtable<NodeInfo, Boolean> componentDisplay;

	/*
	 * 
	 */
	


	public InitialConditions(MainPanel mainPanel) {

		super("Map");
		setLayout(null);

		this.mainPanel = mainPanel;
		this.model = this.mainPanel.getEpithelium().getUnitaryModel();

		mainPanel.getSimulation().resetIterationNumber();

		getContentPane().setPreferredSize(new Dimension(700, 700));
		getContentPane().setBackground(Color.white);
		setTitle("Initial Conditions");

		TitledBorder titleProperComponents;
		TitledBorder titleInputs;
		TitledBorder titlePerturbation;
		titleProperComponents = BorderFactory
				.createTitledBorder("Proper Components");
		titleInputs = BorderFactory.createTitledBorder("Inputs");
		titlePerturbation = BorderFactory.createTitledBorder("Perturbation");

		properComponentsPanel = new JPanel();
		inputsPanel = new JPanel();
		perturbationsPanel = new JPanel();
		optionsPanel = new JPanel();

		properComponentsPanel.setBounds(500, 0, 180, 200);
		inputsPanel.setBounds(500, 250, 180, 100);
		perturbationsPanel.setBounds(500, 400, 180, 100);

		optionsPanel.setBounds(0, 600, 700, 100);

		properComponentsPanel.setBackground(Color.white);
		inputsPanel.setBackground(Color.white);
		optionsPanel.setBackground(Color.white);
		perturbationsPanel.setBackground(Color.white);

		properComponentsPanel.setBorder(titleProperComponents);
		inputsPanel.setBorder(titleInputs);
		perturbationsPanel.setBorder(titlePerturbation);

		properComponentsPanel.setLayout(null);
		inputsPanel.setLayout(null);
		perturbationsPanel.setLayout(null);

		/* ProperComponents and Input panel */

		listNodes = model.getNodeOrder();

		nodeBox = new JCheckBox[listNodes.size()];
		initialStatePerComponent = new JComboBox[listNodes.size()];

		colorChooser = new ArrayList<ColorButton>();
		Jcombo2Node = new Hashtable<JComboBox, NodeInfo>();
		Jcheck2Node = new Hashtable<JCheckBox, NodeInfo>();

		componentDisplay = new Hashtable<NodeInfo, Boolean>();

		int inputCount = 0;
		int properCount = 0;

		for (int i = 0; i < listNodes.size(); i++) {

			if (mainPanel.componentsPanel.isEnv.get(listNodes.get(i))) {

				nodeBox[i] = new JCheckBox(listNodes.get(i).getNodeID());
				nodeBox[i].setBackground(Color.white);
				nodeBox[i].setBounds(10, 30 + inputCount * 40, 50, 25);
				Jcheck2Node.put(nodeBox[i], listNodes.get(i));
				componentDisplay.put(listNodes.get(i), false);

				nodeBox[i].addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						JCheckBox src = (JCheckBox) arg0.getSource();
						setComponentDisplay(Jcheck2Node.get(src),
								src.isSelected());
						fillhexagons();
					}
				});

				mainPanel.getSimulation().setNode2Int(listNodes.get(i), i);

				initialStatePerComponent[i] = new JComboBox();
				initialStatePerComponent[i].setBounds(60, 30 + inputCount * 40,
						40, 25);
				for (int maxValue = 0; maxValue < listNodes.get(i).getMax() + 1; maxValue++) {
					initialStatePerComponent[i].addItem(maxValue);
				}
				Jcombo2Node.put(initialStatePerComponent[i], listNodes.get(i));
				
				mainPanel.getSimulation().setInitialState(
						Jcombo2Node.get(initialStatePerComponent[i]), 0);
				
				initialStatePerComponent[i]
						.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent arg0) {
								JComboBox src = (JComboBox) arg0.getSource();
								// TODO Auto-generated method stub
								fireInitialStateChange(src);
							}
						});

				colorChooser.add(new ColorButton(mainPanel.componentsPanel,
						listNodes.get(i)));
				colorChooser.get(i).setBackground(
						mainPanel.getEpithelium().getColors()
								.get(listNodes.get(i)));
				colorChooser.get(i)
						.setBounds(120, 30 + inputCount * 40, 40, 25);
				inputsPanel.add(nodeBox[i]);
				inputsPanel.add(initialStatePerComponent[i]);
				inputsPanel.add(colorChooser.get(i));
				inputCount = inputCount + 1;


			} else if (!listNodes.get(i).isInput()) {
				nodeBox[i] = new JCheckBox(listNodes.get(i).getNodeID());
				nodeBox[i].setBackground(Color.white);
				nodeBox[i].setBounds(10, 30 + properCount * 40, 50, 25);
				Jcheck2Node.put(nodeBox[i], listNodes.get(i));
				componentDisplay.put(listNodes.get(i), false);

				nodeBox[i].addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						JCheckBox src = (JCheckBox) arg0.getSource();
					setComponentDisplay(Jcheck2Node.get(src),
								src.isSelected());
					fillhexagons();
					}

				});

				mainPanel.getSimulation().setNode2Int(listNodes.get(i), i);
				initialStatePerComponent[i] = new JComboBox();
				initialStatePerComponent[i].setBounds(60,
						30 + properCount * 40, 40, 25);
				for (int maxValue = 0; maxValue < listNodes.get(i).getMax() + 1; maxValue++) {
					initialStatePerComponent[i].addItem(maxValue);
				}

				Jcombo2Node.put(initialStatePerComponent[i], listNodes.get(i));

				mainPanel.getSimulation().setInitialState(
						Jcombo2Node.get(initialStatePerComponent[i]), 0);

				initialStatePerComponent[i]
						.addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent arg0) {
								JComboBox src = (JComboBox) arg0.getSource();
								// TODO Auto-generated method stub
								fireInitialStateChange(src);
							}

						});

				colorChooser.add(new ColorButton(mainPanel.componentsPanel,
						listNodes.get(i)));
				colorChooser.get(i).setBackground(
						mainPanel.getEpithelium().getColors()
								.get(listNodes.get(i)));
				colorChooser.get(i).setBounds(120, 30 + properCount * 40, 40,
						25);
				properComponentsPanel.add(initialStatePerComponent[i]);
				properComponentsPanel.add(nodeBox[i]);
				properComponentsPanel.add(colorChooser.get(i));
				properCount = properCount + 1;
			}
		}

		/* Perturbations panel */

		final JComboBox perturbedComponent = new JComboBox();
		final JComboBox perturbedExpression = new JComboBox();
		perturbedComponent.setBounds(10, 30, 60, 25);
		perturbedExpression.setBounds(90, 30, 60, 25);

		for (int i = 0; i < listNodes.size(); i++) {
			if (!listNodes.get(i).isInput()) {
				perturbedComponent.addItem(listNodes.get(i).getNodeID());
			}
		}
		perturbedComponent.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				// for (int maxValue = 0; maxValue < listNodes.get(i).getMax() +
				// 1; maxValue++) {
				// initialStatePerComponent.addItem(maxValue);

				// }

			}

		});

		perturbationsPanel.add(perturbedComponent);
		perturbationsPanel.add(perturbedExpression);

		/* options panel */

		JButton buttonMarkAll = new JButton("Mark All");
		JButton buttonClearAll = new JButton("Clear All");
		JButton buttonSave = new JButton("Save");
		JButton buttonClose = new JButton("Close");
		JButton buttonFill = new JButton("Fill");

		buttonFill.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

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

		buttonClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});
		buttonSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();

				int returnVal = fc.showSaveDialog(null);

				if (returnVal == JFileChooser.APPROVE_OPTION) {

					File file = fc.getSelectedFile(); // This grabs the File you
					// typed
					System.out.println(file.getAbsolutePath());

					try {
						MapPanel.repaint();
						FileOutputStream fos = new FileOutputStream(file
								.getAbsolutePath());

						ObjectOutputStream oos = new ObjectOutputStream(fos);
						oos.writeObject(cells);

						oos.close();

						System.out.println(file.getAbsolutePath() + " ");

					} catch (IOException e2)

					{
						e2.printStackTrace();
					}
				}

			}

		});

		/* RollOver */

		rollOver = new JComboBox();

		rollOver.addItem("No Roll-Over");
		rollOver.addItem("Vertical Roll-Over");
		rollOver.addItem("Horizontal Roll-Over");
		String aux = (String) rollOver.getSelectedItem();
		mainPanel.getTopology().setRollOver(aux);
		rollOver.setBackground(Color.white);

		rollOver.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				JComboBox source = (JComboBox) event.getSource();
				String optionString = (String) source.getSelectedItem();
				fireRollOverChange(optionString);
			}
		});
		optionsPanel.add(rollOver);
		optionsPanel.add(buttonMarkAll);
		optionsPanel.add(buttonClearAll);
		optionsPanel.add(buttonFill);
		optionsPanel.add(buttonSave);
		optionsPanel.add(buttonClose);

		/* Hexagon Grid */
		this.startX = 0;
		this.startY = 0;
		this.endX = 0;
		this.endY = 0;

		MapPanel = new DrawPolygonM(this, this.mainPanel);

		MapPanel.setLayout(null);
		MapPanel.setBounds(10, 10, 450, 500);
		// MapPanel.setBackground(Color.white);
		textArea = new JTextArea();
		textArea.setEditable(false);

		MapPanel.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				textArea.setBackground(Color.red);
				textArea.append("I'm here"
						+ e.getComponent().getClass().getName());
				textArea.setCaretPosition(textArea.getDocument().getLength());
			}
		});

		setResizable(false);

		/* Add stuff to the final panel */
		getContentPane().add(MapPanel);
		getContentPane().add(properComponentsPanel);
		getContentPane().add(inputsPanel);
		getContentPane().add(perturbationsPanel);
		getContentPane().add(optionsPanel);

		pack();
		setLocationByPlatform(true);
		setVisible(true);
		setLocationRelativeTo(null);

		// initializeInitialStates();
//		if (!mainPanel.getSimulation().getHasInitiated()) {
//			mainPanel.getSimulation().initializeInitialStates();
//			mainPanel.getSimulation().setHasInitiated(true);
//
//		}

	}

	private void fireInitialStateChange(JComboBox combo) {

		mainPanel.getSimulation().setInitialState(Jcombo2Node.get(combo),
				(Integer) combo.getSelectedItem());
	}

	private void fireRollOverChange(String optionString) {
		mainPanel.getTopology().setRollOver(optionString);
	}

	public void setComponentDisplay(NodeInfo nodeInfo, boolean b) {
		componentDisplay.put(nodeInfo, b);
	}

	public void fillhexagons() {

		Color color = Color.white;

		int row;
		int column;

		for (int i = 0; i < mainPanel.getTopology().getNumberInstances(); i++) {

			row = mainPanel.getTopology().instance2i(i,
					mainPanel.getTopology().getWidth());
			column = mainPanel.getTopology().instance2j(i,
					mainPanel.getTopology().getHeight());

			color = Color(row, column);
			MapPanel.drawHexagon(row, column, MapPanel.getGraphics(), color);
		}
	}

	public void close() {
		mainPanel.componentsPanel.repaint();
		mainPanel.componentsPanel.revalidate();
		
		dispose();
	}

	public void initialize() {

		MapPanel.paintComponent(MapPanel.getGraphics());

		MapPanel.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent arg0) {
				// TODO Auto-generated method stub
				int ind_it = (int) Math.floor((arg0.getX() / (1.5 * MapPanel.radius)));

				double ind_yts = (arg0.getY() - (ind_it % 2) * MapPanel.height
						/ 2);
				double ind_jt = Math.floor(ind_yts / (MapPanel.height));

				double xt = arg0.getX() - ind_it * (1.5 * MapPanel.radius);
				double yt = ind_yts - ind_jt * (MapPanel.height);
				int i = 0, j = 0;
				int deltaj = 0;

				if (yt > MapPanel.height / 2)
					deltaj = 1;
				else
					deltaj = 0;

				if (xt > MapPanel.radius
						* Math.abs(0.5 - (yt / MapPanel.height))) {
					i = (int) ind_it;
					j = (int) ind_jt;

				} else {
					i = (int) ind_it - 1;
					j = (int) (ind_jt - i % 2 + deltaj);
				}

				// The mouse is over a cell that belongs to the grid

				if (i < mainPanel.getTopology().getWidth()
						&& j < mainPanel.getTopology().getHeight() && i >= 0
						&& j >= 0) {
					color = Color();
					MapPanel.drawHexagon(i, j, MapPanel.getGraphics(), color);

					setInitialState(i, j);

				}

			}

			@Override
			public void mouseMoved(MouseEvent arg0) {
				// TODO Auto-generated method stub
				int ind_it = (int) Math.floor((arg0.getX() / (1.5 * MapPanel.radius)));

				double ind_yts = (arg0.getY() - (ind_it % 2) * MapPanel.height
						/ 2);
				double ind_jt = Math.floor(ind_yts / (MapPanel.height));

				double xt = arg0.getX() - ind_it * (1.5 * MapPanel.radius);
				double yt = ind_yts - ind_jt * (MapPanel.height);
				int i = 0, j = 0;
				int deltaj = 0;

				if (yt > MapPanel.height / 2)
					deltaj = 1;
				else
					deltaj = 0;

				if (xt > MapPanel.radius
						* Math.abs(0.5 - (yt / MapPanel.height))) {
					i = (int) ind_it;
					j = (int) ind_jt;

				} else {
					i = (int) ind_it - 1;
					j = (int) (ind_jt - i % 2 + deltaj);
				}
				if (i < mainPanel.getTopology().getWidth()
						&& j < mainPanel.getTopology().getHeight() && i >= 0
						&& j >= 0) {

					getInitialState(i, j);
				}

			}
		});

		MapPanel.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub

				startX = arg0.getX();
				startY = arg0.getX();

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				MapPanel.setBackground(Color.white);
				endX = arg0.getX();
				endY = arg0.getX();
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub

				int ind_it = (int) Math.floor((arg0.getX() / (1.5 * MapPanel.radius)));

				double ind_yts = (arg0.getY() - (ind_it % 2) * MapPanel.height
						/ 2);
				double ind_jt = Math.floor(ind_yts / (MapPanel.height));

				double xt = arg0.getX() - ind_it * (1.5 * MapPanel.radius);
				double yt = ind_yts - ind_jt * (MapPanel.height);
				int i = 0, j = 0;
				int deltaj = 0;

				if (yt > MapPanel.height / 2)
					deltaj = 1;
				else
					deltaj = 0;

				if (xt > MapPanel.radius
						* Math.abs(0.5 - (yt / MapPanel.height))) {
					i = (int) ind_it;
					j = (int) ind_jt;

				} else {
					i = (int) ind_it - 1;
					j = (int) (ind_jt - i % 2 + deltaj);
				}

				// The mouse is over a cell that belongs to the grid

				if (i < mainPanel.getTopology().getWidth()
						&& j < mainPanel.getTopology().getHeight() && i >= 0
						&& j >= 0) {
					color = Color();
					setInitialState(i, j);
					MapPanel.drawHexagon(i, j, MapPanel.getGraphics(), color);

					// int instance = mainPanel.getTopology()
					// .coords2Instance(i, j);
					//
					// // System.out.println(instance);
					// for (int h : mainPanel.getTopology().groupNeighbors(
					// instance, 4)) {
					//
					// int m = mainPanel.getTopology().instance2i(h,
					// mainPanel.getTopology().getWidth());
					// int n = mainPanel.getTopology().instance2j(h,
					// mainPanel.getTopology().getWidth());
					//
					// MapPanel.drawHexagon(m, n, MapPanel.getGraphics(),
					// color);
					// }
				}
			}
		});
	}

	public void getInitialState(int i, int j) {
		Set<NodeInfo> a = mainPanel.getSimulation().getNode2Int().keySet();
		// for (NodeInfo a2 : a) {
		//
		// textArea.append(a2
		// + " "
		// + mainPanel.getTopology().coords2Instance(i, j)
		// + " "
		// + a2
		// + " "
		// + mainPanel
		// .getSimulation()
		// .getComposedInitialState()
		// .get(mainPanel.getLogicalModelComposition()
		// .computeNewName(
		// a2.getNodeID(),
		// mainPanel.getTopology()
		// .coords2Instance(i, j))));
		// }
	}

	public void setInitialState(int i, int j) {
		Set<NodeInfo> a = mainPanel.getSimulation().getNode2Int().keySet();
		
		int instance = mainPanel.getTopology().coords2Instance(i, j);
		for (NodeInfo a2 : a) {

			if (componentDisplay.get(a2)) {

				mainPanel.getGrid().setGrid(instance, a2,
						(byte) mainPanel.getSimulation().getInitialState(a2));
				

			}
		}
	}

	public Color Color() {

		int red = 255;
		int green = 255;
		int blue = 255;
		color = new Color(red, green, blue);

		Set<NodeInfo> a = componentDisplay.keySet();

		for (NodeInfo a2 : a) {
			if (componentDisplay.get(a2)) {
				int value = mainPanel.getSimulation().getInitialState(a2);

				if (value > 0) {
					color = mainPanel.getEpithelium().getColors().get(a2);
					color = ColorBrightness(color, value);

					red = (red + color.getRed()) / 2;
					green = (green + color.getGreen()) / 2;
					blue = (blue + color.getBlue()) / 2;
					color = new Color(red, green, blue);
				}

				else if (value == 0) {
					color = new Color(red, green, blue);
				}
			}
		}
		return color;

	}

	public Color Color(int i, int j) {

		int red = 255;
		int green = 255;
		int blue = 255;
		color = new Color(red, green, blue);

		Set<NodeInfo> a = componentDisplay.keySet();

		int instance = mainPanel.getTopology().coords2Instance(i, j);

		for (NodeInfo a2 : a) {
			if (componentDisplay.get(a2)) {

				int value = mainPanel.getGrid().getGrid().get(instance).get(a2);

				if (value > 0) {
					color = mainPanel.getEpithelium().getColors().get(a2);
					color = ColorBrightness(color, value);

					red = (red + color.getRed()) / 2;
					green = (green + color.getGreen()) / 2;
					blue = (blue + color.getBlue()) / 2;
					color = new Color(red, green, blue);
				} else if (value == 0) {
					color = new Color(red, green, blue);
				}
			}
		}
		return color;

	}

	public Color ColorBrightness(Color color, int value) {
		if (value > 0) {

			for (int j = 2; j <= value; j++) {
				color = color.darker();
			}
		} else if (value == 0) {
			color = Color.white;
		}
		return color;

	}

	public void initializeCells(ArrayList<ArrayList<Cell>> cells) {
		// adicionar try catch para textFx e fy

		cells = new ArrayList<ArrayList<Cell>>();
		for (int i = 0; i < mainPanel.getTopology().getWidth(); i++) {

			cells.add(new ArrayList<Cell>());
			for (int j = 0; j < mainPanel.getTopology().getHeight(); j++) {
				int G0 = 0;
				cells.get(i).add(new Cell(G0));
			}
		}
	}

	public void clearAllCells() {
		// adicionar try catch para textFx e fy
		mainPanel.getGrid().initializeGrid();
		for (int i = 0; i < mainPanel.getTopology().getWidth(); i++) {
			for (int j = 0; j < mainPanel.getTopology().getHeight(); j++) {
				//mainPanel.getSimulation().initializeInitialStates();
				MapPanel.drawHexagon(i, j, MapPanel.getGraphics(), Color.white);
			}
		}
	}

	public void markAllCells() {

		// clearAllCells(cells);
		for (int i = 0; i < mainPanel.getTopology().getWidth(); i++) {
			for (int j = 0; j < mainPanel.getTopology().getHeight(); j++) {
				setInitialState(i, j);
				MapPanel.drawHexagon(i, j, MapPanel.getGraphics(), Color());
			}
		}
	}

//	private void resetInitialConditions(){
//		mainPanel.getSimulation().resetIterationNumber();
//		JCheckBox nodeBox[]=null;
//		Jcombo2Node=null;
//		Jcheck2Node=null;
//		initialStatePerComponent=null;
//		colorChooser=null;
//		listNodes=null;
//	}
	
}
