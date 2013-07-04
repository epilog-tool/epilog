package pt.igc.nmd.epilog.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;
import org.colomoto.logicalmodel.io.sbml.SBMLFormat;

import pt.igc.nmd.epilog.InitialState;
import pt.igc.nmd.epilog.InputsPanel;
import pt.igc.nmd.epilog.LogicalModelComposition;
import pt.igc.nmd.epilog.PerturbationsPanel;
import pt.igc.nmd.epilog.PrioritiesPanel;
import pt.igc.nmd.epilog.Simulation;
import pt.igc.nmd.epilog.SphericalEpithelium;
import pt.igc.nmd.epilog.TextPanel;
import pt.igc.nmd.epilog.Topology;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1779945742155950400L;

	public SphericalEpithelium epithelium = null;
	private SimulationSetupPanel simulationSetupPanel = null;

	private boolean start; // variable that returns true when the user wants to
							// create a new epithelium

	private StartPanel startPanel = null;
	private JPanel panelCenter = null;
	private JPanel panelCenterLeft = null;
	private JPanel panelCenterRight = null;
	public JPanel gridSpecsPanel = null;

	public DrawPolygon hexagonsPanel = null;
	private TextPanel watcherPanel;
	public ComponentsPanel componentsPanel = null;
	public JPanel auxiliaryHexagonsPanel;
	public InputsPanel inputsPanel = null;
	public PerturbationsPanel perturbationsPanel = null;
	private PrioritiesPanel prioritiesPanel = null;
	public InitialState initial = null;

	private int fillXi;
	private int fillYi;

	public Topology topology = null;
	public Simulation simulation;
	private LogicalModelComposition logicalModelComposition;

	public boolean needsComposedModel;
	public boolean resetComposedModel;

	public String previsioulySelectedPeturbationSet = "none";
	public String previsioulySelectedPrioritiesSet = "none";
	public String previsioulySelectedInputSet = "none";

	private boolean initialSetupHasChanged;
	private boolean simulationHasBegan;
	private boolean drawingPerturbations;
	private boolean drawingPriorities;
	private boolean drawingCells;

	private boolean fill = false;

	private JTabbedPane tabbedPane;

	private JFileChooser fc = new JFileChooser();
	private boolean editableTab;
	private Color backgroundColor;

	private JLabel selectedFilenameLabel;

	public MainFrame() {

		this.logicalModelComposition = new LogicalModelComposition(this);
		initialSetupHasChanged = false;

		this.backgroundColor = new Color(0xD3D3D3);

		this.start = false;
	}

	public void initialize() throws Exception {

		UIManager.setLookAndFeel(UIManager
				.getCrossPlatformLookAndFeelClassName());

		setTitle("Epilog");
		setupMainFrame();
	}

	private boolean isEditable() {
		return !simulation.isRunning() & editableTab;
	}

	public boolean isDrawingPerturbations() {
		return drawingPerturbations;
	}

	public boolean isDrawingPriorities() {
		return drawingPriorities;
	}

	public boolean isDrawingCells() {
		return drawingCells;
	}

	public SphericalEpithelium getEpithelium() {
		return this.epithelium;
	}

	public void setEpithelium(SphericalEpithelium epithelium) {
		this.epithelium = epithelium;
	}

	public LogicalModelComposition getLogicalModelComposition() {
		return this.logicalModelComposition;
	}

	public void setupMainFrame() {

		getContentPane().setPreferredSize(new Dimension(1200, 600));
		getContentPane().setBackground(backgroundColor);
		getContentPane().setLayout(new BorderLayout());
		this.setResizable(true);

		// Start Panel
		startPanel = new StartPanel(this);
		startPanel.setBackground(backgroundColor);

		getContentPane().add(startPanel, BorderLayout.PAGE_START);

		pack();
		setVisible(true);
		setLocationRelativeTo(null);

	}

	// Hexagons Panel
	public void initializePanelCenter() {
		if (auxiliaryHexagonsPanel != null) {

			this.remove(panelCenter);
		}

		this.topology = new Topology(24, 24);
		this.epithelium = new SphericalEpithelium(this.topology);
		this.simulation = new Simulation(this);

		panelCenter = new JPanel(new BorderLayout());
		getContentPane().add(panelCenter, BorderLayout.CENTER);

		panelCenterLeft = new JPanel(new BorderLayout());
		panelCenter.add(panelCenterLeft, BorderLayout.LINE_START);

		panelCenterRight = new JPanel(new BorderLayout());
		panelCenterRight.setPreferredSize(new Dimension(20, 20));
		panelCenter.add(panelCenterRight, BorderLayout.LINE_END);

		panelCenterRight = new JPanel(new BorderLayout());
		panelCenter.add(panelCenterRight, BorderLayout.CENTER);

		auxiliaryHexagonsPanel = new JPanel();
		hexagonsPanel = new DrawPolygon(this);
		gridSpecsPanel = gridSpecsPanel();

		panelCenterLeft.add(gridSpecsPanel, BorderLayout.PAGE_START);
		auxiliaryHexagonsPanel = new JPanel(new BorderLayout());
		panelCenterLeft.add(auxiliaryHexagonsPanel, BorderLayout.CENTER);

		UIManager.put("TitledBorder.border",
				new LineBorder(panelCenter.getBackground(), 1));
		TitledBorder titleInitialConditions;
		titleInitialConditions = BorderFactory.createTitledBorder("");
		auxiliaryHexagonsPanel.setBorder(javax.swing.BorderFactory
				.createEmptyBorder());
		auxiliaryHexagonsPanel.setBorder(titleInitialConditions);
		titleInitialConditions.setTitleColor(Color.black);

		auxiliaryHexagonsPanel.add(hexagonsPanel, BorderLayout.CENTER);

		hexagonsPanel.paintComponent(hexagonsPanel.getGraphics());

		hexagonsPanel.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent arg0) {
				int ind_it = (int) Math.floor((arg0.getX() / (1.5 * hexagonsPanel.radius)));

				double ind_yts = (arg0.getY() - (ind_it % 2)
						* hexagonsPanel.height / 2);
				double ind_jt = Math.floor(ind_yts / hexagonsPanel.height);

				double xt = arg0.getX() - ind_it * (1.5 * hexagonsPanel.radius);
				double yt = ind_yts - ind_jt * (hexagonsPanel.height);
				int i = 0;
				int j = 0;
				int deltaj = 0;

				if (yt > hexagonsPanel.height / 2)
					deltaj = 1;
				else
					deltaj = 0;

				if (xt > hexagonsPanel.radius
						* Math.abs(0.5 - (yt / hexagonsPanel.height))) {
					i = (int) ind_it;
					j = (int) ind_jt;

				} else {
					i = (int) ind_it - 1;
					j = (int) (ind_jt - i % 2 + deltaj);
				}

				// The mouse is over a cell that belongs to the grid
				paintHexagons(i, j);
			}

			@Override
			public void mouseMoved(MouseEvent arg0) {
				int ind_it = (int) Math.floor((arg0.getX() / (1.5 * hexagonsPanel.radius)));

				double ind_yts = (arg0.getY() - (ind_it % 2)
						* hexagonsPanel.height / 2);
				double ind_jt = Math.floor(ind_yts / (hexagonsPanel.height));

				double xt = arg0.getX() - ind_it * (1.5 * hexagonsPanel.radius);
				double yt = ind_yts - ind_jt * (hexagonsPanel.height);
				int i = 0, j = 0;
				int deltaj = 0;

				if (yt > hexagonsPanel.height / 2)
					deltaj = 1;
				else
					deltaj = 0;

				if (xt > hexagonsPanel.radius
						* Math.abs(0.5 - (yt / hexagonsPanel.height))) {
					i = (int) ind_it;
					j = (int) ind_jt;

				} else {
					i = (int) ind_it - 1;
					j = (int) (ind_jt - i % 2 + deltaj);
				}

				if (i < topology.getWidth() && j < topology.getHeight()
						&& i >= 0 && j >= 0) {

					int instance = topology.coords2Instance(i, j);

					if (!isDrawingPerturbations()
							& epithelium.getUnitaryModel() != null) {
						String string = ("<html>" + "instance: " + instance);
						for (NodeInfo node : epithelium.getUnitaryModel()
								.getNodeOrder()) {
							// if (!node.isInput()) {

							string = string
									+ ("<br>" + " node: " + node
											+ " -> value: " + simulation
												.getCurrentGlobalStateValue(
														instance, node));

						}
						// }
						if (epithelium.isCellPerturbed(instance))
							string = string
									+ ("<br>" + "Perturbation: " + epithelium
											.getInstancePerturbation(instance));
						string = string + ("</html>");
						hexagonsPanel.setToolTipText(string);

					}
					if (isDrawingPerturbations()) {
						String string = ("<html>" + "instance: " + instance);
						if (epithelium.isCellPerturbed(instance))
							string = string
									+ ("<br>" + "Perturbation: " + epithelium
											.getInstancePerturbation(instance));

						string = string + ("</html>");
						hexagonsPanel.setToolTipText(string);

					}
				}

			}
		});

		hexagonsPanel.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {

				int ind_it = (int) Math.floor((arg0.getX() / (1.5 * hexagonsPanel.radius)));

				double ind_yts = (arg0.getY() - (ind_it % 2)
						* hexagonsPanel.height / 2);
				double ind_jt = Math.floor(ind_yts / (hexagonsPanel.height));

				double xt = arg0.getX() - ind_it * (1.5 * hexagonsPanel.radius);
				double yt = ind_yts - ind_jt * (hexagonsPanel.height);
				int i = 0, j = 0;
				int deltaj = 0;

				if (yt > hexagonsPanel.height / 2)
					deltaj = 1;
				else
					deltaj = 0;

				if (xt > hexagonsPanel.radius
						* Math.abs(0.5 - (yt / hexagonsPanel.height))) {
					i = (int) ind_it;
					j = (int) ind_jt;

				} else {
					i = (int) ind_it - 1;
					j = (int) (ind_jt - i % 2 + deltaj);
				}

				paintHexagons(i, j);

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {

			}

			@Override
			public void mouseExited(MouseEvent arg0) {

			}

			@Override
			public void mousePressed(MouseEvent arg0) {

				int ind_it = (int) Math.floor((arg0.getX() / (1.5 * hexagonsPanel.radius)));

				double ind_yts = (arg0.getY() - (ind_it % 2)
						* hexagonsPanel.height / 2);
				double ind_jt = Math.floor(ind_yts / (hexagonsPanel.height));

				double xt = arg0.getX() - ind_it * (1.5 * hexagonsPanel.radius);
				double yt = ind_yts - ind_jt * (hexagonsPanel.height);
				int i = 0, j = 0;
				int deltaj = 0;

				if (yt > hexagonsPanel.height / 2)
					deltaj = 1;
				else
					deltaj = 0;

				if (xt > hexagonsPanel.radius
						* Math.abs(0.5 - (yt / hexagonsPanel.height))) {
					i = (int) ind_it;
					j = (int) ind_jt;

				} else {
					i = (int) ind_it - 1;
					j = (int) (ind_jt - i % 2 + deltaj);
				}

				if (isEditable() & isFillOn()) {
					if (i < topology.getWidth() && j < topology.getHeight()
							&& i >= 0 && j >= 0) {
						// int instance = topology.coords2Instance(i, j);
						if (isFillOn()) {
							fillXi = i;
							fillYi = j;
						}

					}
				}
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				int ind_it = (int) Math.floor((arg0.getX() / (1.5 * hexagonsPanel.radius)));

				double ind_yts = (arg0.getY() - (ind_it % 2)
						* hexagonsPanel.height / 2);
				double ind_jt = Math.floor(ind_yts / (hexagonsPanel.height));

				double xt = arg0.getX() - ind_it * (1.5 * hexagonsPanel.radius);
				double yt = ind_yts - ind_jt * (hexagonsPanel.height);
				int i = 0, j = 0;
				int deltaj = 0;

				if (yt > hexagonsPanel.height / 2)
					deltaj = 1;
				else
					deltaj = 0;

				if (xt > hexagonsPanel.radius
						* Math.abs(0.5 - (yt / hexagonsPanel.height))) {
					i = (int) ind_it;
					j = (int) ind_jt;

				} else {
					i = (int) ind_it - 1;
					j = (int) (ind_jt - i % 2 + deltaj);
				}

				paintHexagons(i, j);
			}
		});
	}

	private void paintHexagons(int i, int j) {

		if (isEditable()) {
			if (i < topology.getWidth() && j < topology.getHeight() && i >= 0
					&& j >= 0) {

				int instance = topology.coords2Instance(i, j);

				if (isDrawingCells()) {
					Color color = Color();
					hexagonsPanel.drawHexagon(instance,
							hexagonsPanel.getGraphics(), color);
					getEpithelium().setInitialState(instance);
				} else if (isDrawingPerturbations()) {

					epithelium.setPerturbedInstance(instance);
					if (epithelium.getActivePerturbation() != null) {
						Color color = epithelium
								.getPerturbationColor(epithelium
										.getActivePerturbation().toString());

						hexagonsPanel.drawHexagon(instance,
								hexagonsPanel.getGraphics(), color);
					}

				} else if (isDrawingPriorities()) {

				}
			}
		}
	}

	public List<Integer> fill(int xInitial, int yInitial, int xFinal, int yFinal) {
		List<Integer> instanceList = new ArrayList<Integer>();
		int x1 = xInitial;
		int x2 = xFinal;
		int y1 = yInitial;
		int y2 = yFinal;

		if (xInitial > xFinal) {
			x1 = xFinal;
			x2 = xInitial;
		}
		if (yInitial > yFinal) {
			y1 = yFinal;
			y2 = yInitial;
		}

		for (int i = x1; i <= x2; i++)
			for (int j = y1; j <= y2; j++) {
				if (i > topology.getWidth())
					i = topology.getWidth();
				if (j > topology.getHeight())
					j = topology.getHeight();

				if (i >= 0 && j >= 0) {
					int instance = topology.coords2Instance(i, j);
					instanceList.add(instance);
				}
			}
		return instanceList;
	}

	public void setFill(boolean b) {
		fill = b;
	}

	public boolean isFillOn() {
		return fill;
	}

	public void simulationPanelsoff() {
		simulationSetupPanel.initialCombo.setEnabled(false);
		simulationSetupPanel.inputCombo.setEnabled(false);
		simulationSetupPanel.perturbationsCombo.setEnabled(false);
		simulationSetupPanel.prioritiesCombo.setEnabled(false);
		simulationSetupPanel.createComposedModel.setEnabled(false);
		simulationSetupPanel.rollOver.setEnabled(false);
	}

	public void simulationPanelson() {
		simulationSetupPanel.initialCombo.setEnabled(true);
		simulationSetupPanel.inputCombo.setEnabled(true);
		simulationSetupPanel.perturbationsCombo.setEnabled(true);
		simulationSetupPanel.prioritiesCombo.setEnabled(true);
		simulationSetupPanel.createComposedModel.setEnabled(true);
		simulationSetupPanel.rollOver.setEnabled(true);
	}

	private void initializePanelCenterRight() {

		tabbedPane = new JTabbedPane();
		startPanel = new StartPanel(this);
		componentsPanel = new ComponentsPanel(this);
		simulationSetupPanel = new SimulationSetupPanel(this);

		watcherPanel = new TextPanel(this);
		initial = new InitialState(this);
		inputsPanel = new InputsPanel(epithelium, topology, this);
		perturbationsPanel = new PerturbationsPanel(this);
		prioritiesPanel = new PrioritiesPanel(this);

		tabbedPane.addTab("Simulation", simulationSetupPanel);
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

		tabbedPane.addTab("Initial Conditions", initial);
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

		tabbedPane.addTab("Inputs", inputsPanel);
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

		tabbedPane.addTab("Perturbations", perturbationsPanel);
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

		tabbedPane.addTab("Priorities", prioritiesPanel);
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

		tabbedPane.setForegroundAt(2, Color.white);
		tabbedPane.setBackgroundAt(2, Color.gray);
		tabbedPane.setForegroundAt(3, Color.white);
		tabbedPane.setBackgroundAt(3, Color.gray);
		tabbedPane.setForegroundAt(4, Color.white);
		tabbedPane.setBackgroundAt(4, Color.gray);

		tabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				editableTab = false;
				drawingPerturbations = false;
				drawingCells = false;
				drawingPriorities = false;
				hexagonsPanel.clearAllCells(hexagonsPanel.getGraphics());
				componentsPanel.setVisible(false);
				setFill(false);

				if (tabbedPane.getSelectedIndex() == 0) {
					simulationSetupPanelRepaint();
					componentsPanel.setVisible(true);
					componentsPanel.removeAll();
					componentsPanel.init();
					setFill(false);
				} else if (tabbedPane.getSelectedIndex() == 3) {
					editableTab = true;
					drawingPerturbations = true;
					fillHexagons();
					setFill(false);
					perturbationsPanel.buttonFill
							.setBackground(getBackground());
				} else if (tabbedPane.getSelectedIndex() == 4) {
					editableTab = true;
					drawingCells = false;
					// drawingPriorities = true;
					setFill(false);
				} else if (tabbedPane.getSelectedIndex() == 2) {
					editableTab = true;
					drawingCells = true;
					setFill(false);
					// inputsPanel.buttonFill.setBackground(getBackground());
					for (JCheckBox singleNodeBox : initial.nodeBox) {

						singleNodeBox.setSelected(false);
						initial.setComponentDisplay(
								initial.Jcheck2Node.get(singleNodeBox),
								singleNodeBox.isSelected());
					}
					fillHexagons();

				} else if (tabbedPane.getSelectedIndex() == 1) {
					initial.removeAll();
					initial.init();
					editableTab = true;
					drawingCells = true;
					setFill(false);
					initial.buttonFill.setBackground(getBackground());

					// for (JCheckBox singleNodeBox : inputsPanel.nodeBox) {
					// if (singleNodeBox != null) {
					// singleNodeBox.setSelected(false);
					// inputsPanel.setComponentDisplay(
					// inputsPanel.Jcheck2Node.get(singleNodeBox),
					// singleNodeBox.isSelected());
					// }
					// }
					fillHexagons();
				}
			}
		});

		panelCenterRight.add(tabbedPane, BorderLayout.PAGE_START);
		panelCenterRight.add(componentsPanel, BorderLayout.CENTER);

	}

	private void simulationSetupPanelRepaint() {
		simulationSetupPanel = new SimulationSetupPanel(this);
		tabbedPane.setComponentAt(0, simulationSetupPanel);
	}

	public void setStart(boolean b) {
		this.start = b;
	}

	public boolean getStart() {
		return this.start;
	}

	public void refreshComponentsColors() {
		componentsPanel.removeAll();
		componentsPanel.init();

	}

	public void restartAnalytics() {
		watcherPanel.restartAnalytics();
	}

	public void setInitialSetupHasChanged(boolean b) {
		initialSetupHasChanged = b;
	}

	public void setBorderHexagonsPanel(int iterationNumber) {
		this.auxiliaryHexagonsPanel.setBorder(javax.swing.BorderFactory
				.createEmptyBorder());
		TitledBorder titleInitialConditions;
		titleInitialConditions = BorderFactory
				.createTitledBorder("Simulation Iteration: " + iterationNumber);
		this.auxiliaryHexagonsPanel.setBorder(titleInitialConditions);
	}

	public void setBorderHexagonsPanel() {
		this.auxiliaryHexagonsPanel.setBorder(javax.swing.BorderFactory
				.createEmptyBorder());
		TitledBorder titleInitialConditions;
		titleInitialConditions = BorderFactory
				.createTitledBorder("Initial Conditions");
		this.auxiliaryHexagonsPanel.setBorder(titleInitialConditions);

	}

	// public Color ColorBrightness(Color color, int value) {
	// if (value > 0) {
	//
	// for (int j = 2; j <= value; j++) {
	// color = color.darker();
	// }
	// } else if (value == 0) {
	// color = Color.white;
	// }
	// return color;
	// }

	public Color Color(int instance) {

		int red = 255;
		int green = 255;
		int blue = 255;
		Color color = new Color(red, green, blue);
		List<NodeInfo> listNodes = this.epithelium.getUnitaryModel()
				.getNodeOrder();

		for (NodeInfo node : listNodes) {

			if (!epithelium.isIntegrationComponent(listNodes.indexOf(node)))
				if (this.epithelium.isDefinitionComponentDisplayOn(listNodes
						.indexOf(node))) {

					int value = this.epithelium.getGridValue(instance, node);

					if (value > 0) {
						color = epithelium.getColor(node);
						if (value > 1)
							color = simulation.getColorLevel(color, value,
									node.getMax());

						if (red != 255)
							red = (red + color.getRed()) / 2;
						else
							red = color.getRed();

						if (green != 255)
							green = (green + color.getGreen()) / 2;
						else
							green = color.getGreen();

						if (blue != 255)
							blue = (blue + color.getBlue()) / 2;
						else
							blue = color.getBlue();

						color = new Color(red, green, blue);

					}
				}

		}

		return color;
	}

	public Color Color() {

		int red = 255;
		int green = 255;
		int blue = 255;
		Color color = new Color(red, green, blue);
		List<NodeInfo> listNodes = this.epithelium.getUnitaryModel()
				.getNodeOrder();

		for (NodeInfo node : listNodes) {

			if (!epithelium.isIntegrationComponent(listNodes.indexOf(node)))
				if (this.epithelium.isDefinitionComponentDisplayOn(listNodes
						.indexOf(node))) {

					int value = this.epithelium.getInitialState(node);
					if (value > 0) {
						color = this.epithelium.getColor(node);
						color = simulation.getColorLevel(color, value,
								node.getMax());
						if (red != 255)
							red = (red + color.getRed()) / 2;
						else
							red = color.getRed();

						if (green != 255)
							green = (green + color.getGreen()) / 2;
						else
							green = color.getGreen();

						if (blue != 255)
							blue = (blue + color.getBlue()) / 2;
						else
							blue = color.getBlue();
						color = new Color(red, green, blue);
					} else if (value == 0) {
						color = new Color(red, green, blue);
					}

				}
		}

		return color;
	}

	public JPanel gridSpecsPanel() {

		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel setWidth = new JLabel();
		JLabel setHeight = new JLabel();
		JButton loadSBML = new JButton("Load SBML");
		JLabel labelFilename = new JLabel();
		JTextField userDefinedWidth = new JTextField();
		JTextField userDefinedHeight = new JTextField();
		selectedFilenameLabel = new JLabel();

		labelFilename.setText("Filename: ");
		// labelFilename.setForeground(Color.white);

		setWidth.setText("Width: ");
		setHeight.setText("Height: ");

		// setWidth.setForeground(Color.white);
		// setHeight.setForeground(Color.white);

		userDefinedWidth.setPreferredSize(new Dimension(34, 26));
		userDefinedHeight.setPreferredSize(new Dimension(34, 26));

		userDefinedWidth.setHorizontalAlignment(JTextField.CENTER);
		userDefinedHeight.setHorizontalAlignment(JTextField.CENTER);
		userDefinedWidth.setText("" + topology.getWidth());
		userDefinedHeight.setText("" + topology.getHeight());

		userDefinedWidth.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent arg0) {

				if (getEpithelium().getUnitaryModel() == null) {
					JTextField src = (JTextField) arg0.getSource();
					topology.setWidth(Integer.parseInt(src.getText()));
					hexagonsPanel.paintComponent(hexagonsPanel.getGraphics());
				}
			}

			@Override
			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
			}
		});

		userDefinedHeight.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent arg0) {
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
			}

			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					JTextField src = (JTextField) arg0.getSource();

					topology.setHeight(Integer.parseInt(src.getText()));
					hexagonsPanel.paintComponent(hexagonsPanel.getGraphics());
				}
			}
		});
		userDefinedWidth.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent arg0) {
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
			}

			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					JTextField src = (JTextField) arg0.getSource();

					topology.setWidth(Integer.parseInt(src.getText()));
					hexagonsPanel.paintComponent(hexagonsPanel.getGraphics());
				}
			}
		});

		userDefinedHeight.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent arg0) {
				if (getEpithelium().getUnitaryModel() == null) {
					JTextField src = (JTextField) arg0.getSource();
					topology.setHeight(Integer.parseInt(src.getText()));
					hexagonsPanel.paintComponent(hexagonsPanel.getGraphics());
				}
			}

			@Override
			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
			}
		});

		panel.add(setWidth);
		panel.add(userDefinedWidth);
		panel.add(setHeight);
		panel.add(userDefinedHeight);
		panel.add(loadSBML);
		if (getEpithelium().getUnitaryModel() != null) {
			panel.add(labelFilename);
			panel.add(selectedFilenameLabel);
			initializePanelCenterRight();
		}

		loadSBML.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				askModel();
				initializePanelCenterRight();
			}
		});

		return panel;
	}

	private void askModel() {

		fc.setDialogTitle("Choose file");

		if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			selectedFilenameLabel.setText(fc.getSelectedFile().getName());
			selectedFilenameLabel.setForeground(Color.white);

			File file = fc.getSelectedFile();

			this.getEpithelium().setSBMLFilename(file.getName());
			this.getEpithelium().setSBMLPath(file.getAbsolutePath());

			loadModel(file);
		}
	}

	public void fillHexagons() {

		Color color = Color.white;
		if (epithelium.getUnitaryModel() != null)
			return;
		for (int instance = 0; instance < topology.getNumberInstances(); instance++) {

			color = Color(instance);
			hexagonsPanel.drawHexagon(instance, hexagonsPanel.getGraphics(),
					color);
		}
	}

	// private JTextField sanityCheckDimension(JTextField userDefined) {
	// String dimString = userDefined.getText();
	// int w = Integer.parseInt(dimString);
	// // w = (w % 2 == 0) ? w : w + 1;
	// userDefined.setText("" + w);
	// return userDefined;
	// }

	private void loadModel(File file) {

		SBMLFormat sbmlFormat = new SBMLFormat();
		LogicalModel logicalModel = null;

		try {
			logicalModel = sbmlFormat.importFile(file);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			sbmlFormat.export(logicalModel, baos);

		} catch (IOException e) {
			System.err.println("Cannot import file " + file.getAbsolutePath()
					+ ": " + e.getMessage());
		}

		if (logicalModel == null)
			return;

		this.getEpithelium().setUnitaryModel(logicalModel);
		this.getLogicalModelComposition().resetComposedModel();
		this.auxiliaryHexagonsPanel.setBorder(javax.swing.BorderFactory
				.createEmptyBorder());
		TitledBorder titleInitialConditions;
		titleInitialConditions = BorderFactory.createTitledBorder("");
		this.auxiliaryHexagonsPanel.setBorder(titleInitialConditions);

	}

	public Color getRandomColor() {
		String[] letters = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
				"A", "B", "C", "D", "E", "F" };
		String color = "#";
		for (int i = 0; i < 6; i++) {
			color += letters[(int) Math.round(Math.random() * 15)];
		}
		Color newColor = Color.decode(color);
		return newColor;
	}

	public void disableTabs(Boolean bool) {

		tabbedPane.setEnabledAt(1, !bool);
		tabbedPane.setEnabledAt(2, !bool);
		tabbedPane.setEnabledAt(3, !bool);
		tabbedPane.setEnabledAt(4, !bool);

	}
}
