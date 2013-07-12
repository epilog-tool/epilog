package pt.igc.nmd.epilog.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
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

import org.colomoto.logicalmodel.NodeInfo;

import pt.igc.nmd.epilog.Epithelium;
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
	public SimulationSetupPanel simulationSetupPanel = null;

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

	private String stringTextTool;

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

	/**
	 * Initializes epilog's main panel
	 * 
	 * @see setupMainFrame
	 */
	public void initialize() throws Exception {

		UIManager.setLookAndFeel(UIManager
				.getCrossPlatformLookAndFeelClassName());

		setTitle("Epilog");
		setupMainFrame();
	}

	/**
	 * Runs the main panel for the first time and summons the creation of the
	 * startPanel
	 * 
	 * @see StartPanel
	 */
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

	/**
	 * After an epithelium model is loaded (or a new model is created) a
	 * Topology, an Epithelium and a Simulation are created. It also creates the
	 * hexagons grid, with a predefined size (that a user can modify) and the
	 * option to load an SMBL file. MouseEvents over the hexagons grid are
	 * created.
	 * 
	 * @see StartPanel()
	 * @see Topology()
	 * @see Epithelium()
	 * @see Simulation()
	 * @see DrawPolygon()
	 * @see paintHexagons(int i, int j)
	 * @see setTool()
	 * @see isDrawingPerturbations()
	 * @see isEditable()
	 * @see isFillOn()
	 */
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
						setTool(string, instance);

					}
					if (isDrawingPerturbations()) {
						String string = ("<html>" + "instance: " + instance);
						if (epithelium.isCellPerturbed(instance))
							string = string
									+ ("<br>" + "Perturbation: " + epithelium
											.getInstancePerturbation(instance));

						string = string + ("</html>");
						hexagonsPanel.setToolTipText(string);
						setTool(string, instance);
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

				if (isEditable()) {
					List<Integer> instanceList = new ArrayList<Integer>();
					if (i < topology.getWidth() && j < topology.getHeight()
							&& i >= 0 && j >= 0) {

						if (isFillOn())
							instanceList = fill(fillXi, fillYi, i, j);

						for (int instance : instanceList) {
							if (isDrawingCells()) {
								epithelium.setInitialState(instance);

								hexagonsPanel.drawHexagon(instance,
										hexagonsPanel.getGraphics(), Color());
							} else if (isDrawingPerturbations()) {

								epithelium.setPerturbedInstance(instance);
								if (epithelium.getActivePerturbation() != null) {
									Color color = epithelium
											.getPerturbationColor(epithelium
													.getActivePerturbation()
													.toString());

									hexagonsPanel.drawHexagon(instance,
											hexagonsPanel.getGraphics(), color);
								}

							}

						}

					}
				}
			}
		});
	}

	/**
	 * If the mouse is over an hexagon, then the hexagon is filled with the
	 * color resulting from the selected components and its values. If the user
	 * is drawing a perturbation then it is painted with the corresponding
	 * color.
	 * 
	 * @see DrawPolygon
	 * @see Epithelium
	 * @see isDrawingPerturbations
	 * @see isDrawingCells
	 * @see Color
	 * @param i
	 *            position of the width coordinate
	 * @param j
	 *            position of the height coordinate
	 */
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
				}
			}
		}
	}

	/**
	 * Initializes the right panel of Epilog's main panel.
	 * 
	 * @see InitialState
	 * @see InputsPanel
	 * @see PerturbationsPanel
	 * @see PrioritiesPanel
	 * @see fillHexagons
	 */
	private void initializePanelCenterRight() {

		tabbedPane = new JTabbedPane();
		startPanel = new StartPanel(this);
		componentsPanel = new ComponentsPanel(this);
		simulationSetupPanel = new SimulationSetupPanel(this);

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

		panelCenterRight.add(tabbedPane, BorderLayout.CENTER);

		// PAGE_END

		JPanel end = new JPanel();
		panelCenterRight.add(end, BorderLayout.PAGE_END);

		// panelCenterRight.add(componentsPanel, BorderLayout.CENTER);

	}

	/**
	 * Repaints the right panel at Epilog's main Panel.
	 */
	private void simulationSetupPanelRepaint() {
		simulationSetupPanel = new SimulationSetupPanel(this);
		tabbedPane.setComponentAt(0, simulationSetupPanel);
	}

	/**
	 * Disables tabs when simulation is running.
	 * 
	 * @param b
	 *            boolean value
	 */
	public void disableTabs(Boolean bool) {

		tabbedPane.setEnabledAt(1, !bool);
		tabbedPane.setEnabledAt(2, !bool);
		tabbedPane.setEnabledAt(3, !bool);
		tabbedPane.setEnabledAt(4, !bool);
	}

	/**
	 * Repaints the left panel of Epilog's mainPanel, whenever the grid's
	 * dimension has changed
	 * 
	 * @see Topology
	 * @see DrawPolygon
	 * @return panel left panel
	 */
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

		setWidth.setText("Width: ");
		setHeight.setText("Height: ");

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

	// Getter, Setters and Boolean Methods

	/**
	 * Checks if the hexagons grid can be editable at that moment (whether a
	 * click on the mouse when is over the grid modifies it)
	 * 
	 * @return true if editable, false otherwise
	 */
	private boolean isEditable() {
		return !simulation.isRunning() & editableTab;
	}

	/**
	 * Checks if the hexagons grid can be editable with perturbations at that
	 * moment (whether a click on the mouse when is over the grid modifies it)
	 * 
	 * @return drawingPerturbations true if is drawing perturbations, false
	 *         otherwise
	 */
	public boolean isDrawingPerturbations() {
		return drawingPerturbations;
	}

	/**
	 * Checks if the hexagons grid can be editable with initial conditions at
	 * that moment (whether a click on the mouse when is over the grid modifies
	 * it)
	 * 
	 * @return drawingCells true if is drawing initial conditions, false
	 *         otherwise
	 */
	public boolean isDrawingCells() {
		return drawingCells;
	}

	/**
	 * Gets the epithelium used by the mainFrame
	 * 
	 * @return epithelium epithelium in use
	 */
	public SphericalEpithelium getEpithelium() {
		return this.epithelium;
	}

	/**
	 * Sets the epithelium used by the mainFrame.
	 * 
	 * @param epithelium
	 *            epithelium in use
	 */
	public void setEpithelium(SphericalEpithelium epithelium) {
		this.epithelium = epithelium;
	}

	/**
	 * Gets the composed model used by the mainFrame.
	 * 
	 * @return logicalModelComposition composed model in use
	 */
	public LogicalModelComposition getLogicalModelComposition() {
		return this.logicalModelComposition;
	}

	/**
	 * Sets that the simulation has begun
	 * 
	 * @param b
	 *            boolean value to update start
	 */
	public void setStart(boolean b) {
		this.start = b;
	}

	/**
	 * Asks if the simulation has begun.
	 * 
	 * @return start true if simulation has begun, false otherwise
	 */
	public boolean getStart() {
		return this.start;
	}

	/**
	 * Sets the initialSetupHasChanged value as true or false
	 * 
	 * @param b
	 *            boolean value to update initialSetupHasChanged with
	 */
	public void setInitialSetupHasChanged(boolean b) {
		initialSetupHasChanged = b;
	}

	// Value Analytics related Methods

	/**
	 * Sets the initialSetupHasChanged value as true or false
	 * 
	 * @param b
	 *            boolean value to update initialSetupHasChanged with
	 */
	private void setTool(String s, int instance) {
		stringTextTool = s;
		getTool(instance);
	}

	/**
	 * Sets the initialSetupHasChanged value as true or false
	 * 
	 * @param instance instance to analyse
	 */
	public void getTool(int instance) {

		JPanel test = new JPanel();
		JLabel aux = new JLabel(stringTextTool);
		test.add(aux);

		LineBorder border = new LineBorder(Color.black, 1, true);

		TitledBorder south = new TitledBorder(border, "Analytics @ instance: "
				+ instance, TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION,
				new Font("Arial", Font.ITALIC, 14), Color.black);

		test.setBorder(south);

		simulationSetupPanel.lineStartPanel.remove(2);
		simulationSetupPanel.lineStartPanel.repaint();
		simulationSetupPanel.lineStartPanel.revalidate();
		simulationSetupPanel.lineStartPanel.repaint();
		simulationSetupPanel.lineStartPanel.add(test, BorderLayout.PAGE_END);

	}

	// FILL related Methods

	/**
	 * Calculates which instances are to be painted when the fill rectangle
	 * option is selected.
	 * 
	 * @param xInitial
	 *            initial x-axis coordinate
	 * @param yInitial
	 *            initial y-axis coordinate
	 * @param xFinal
	 *            final x-axis coordinate
	 * @param yFinal
	 *            final y-axis coordinate
	 * @return instanceList instance list to be painted
	 */

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

	/**
	 * Sets fill option on.
	 * 
	 * @param b
	 *            boolean value to update fill
	 */
	public void setFill(boolean b) {
		fill = b;
	}

	/**
	 * Asks if the rectangle fill option is on
	 * 
	 * @return fill true if rectangle fill is on, false otherwise
	 */
	public boolean isFillOn() {
		return fill;
	}

	// Drawing Methods

	/**
	 * Updates the iteration number at the hexagons grid border, after the
	 * simulation has begun
	 * 
	 * @param iterationNumber
	 *            iteration number
	 */
	public void setBorderHexagonsPanel(int iterationNumber) {
		this.auxiliaryHexagonsPanel.setBorder(javax.swing.BorderFactory
				.createEmptyBorder());
		TitledBorder titleInitialConditions;

		if (iterationNumber != 0)
			titleInitialConditions = BorderFactory
					.createTitledBorder("Simulation Iteration: "
							+ iterationNumber);
		else
			titleInitialConditions = BorderFactory
					.createTitledBorder("Initial Conditions");
		this.auxiliaryHexagonsPanel.setBorder(titleInitialConditions);
	}

	/**
	 * Calculates the color of an hexagon to be painted with during the
	 * simulation. This color is the average of all colors selected to display.
	 * 
	 * @param instance
	 *            instance that is to be painted
	 * @return color color with which the instance is to be painted
	 */
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

	/**
	 * Calculates the color of an hexagon to be painted while drawing the
	 * initial conditions. This color is the average of all colors selected to
	 * display.
	 * 
	 * @param instance
	 *            instance that is to be painted
	 * @return color color with which the instance is to be painted
	 */
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

	/**
	 * Randomly creates colors to attribute to components.
	 * 
	 * @return newColor random color
	 */
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

	
	/**
	 * Paints instances
	 * 
	 * @see Color(int instance)
	 * @see DrawPolygon
	 */
	public void fillHexagons() {

		Color color = Color.white;
		if (epithelium.getUnitaryModel() == null)
			return;
		for (int instance = 0; instance < topology.getNumberInstances(); instance++) {

			color = Color(instance);
			hexagonsPanel.drawHexagon(instance, hexagonsPanel.getGraphics(),
					color);
		}
	}

	// Loading Methods

	/**
	 * Loads the sbml model.
	 * 
	 * @see startPanel.loadModel()
	 * 
	 * 
	 */
	private void askModel() {

		fc.setDialogTitle("Choose file");

		if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			selectedFilenameLabel.setText(fc.getSelectedFile().getName());
			selectedFilenameLabel.setForeground(Color.white);

			File file = fc.getSelectedFile();

			this.epithelium.setSBMLFilename(file.getName());
			this.epithelium.setSBMLPath(file.getAbsolutePath());

			startPanel.loadModel(file);
		}
	}

}
