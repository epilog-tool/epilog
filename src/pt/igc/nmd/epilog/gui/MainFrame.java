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
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
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

import pt.igc.nmd.epilog.InitialConditions;
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

	public StartPanel startPanel = null;
	public JPanel panelCenter = null;
	public JPanel panelCenterLeft = null;
	public JPanel panelCenterRight = null;

	public DrawPolygon hexagonsPanel = null;
	public TextPanel watcherPanel;
	public ComponentsPanel componentsPanel = null;
	public JPanel auxiliaryHexagonsPanel;
	public InputsPanel inputsPanel = null;
	public PerturbationsPanel perturbationsPanel = null;
	public PrioritiesPanel prioritiesPanel = null;
	public InitialConditions initial = null;

	public Topology topology = null;
	public Simulation simulation;
	public LogicalModelComposition logicalModelComposition;

	public boolean initialSetupHasChanged;
	public boolean simulationHasBegan;

	private JTabbedPane tabbedPane;

	private JFileChooser fc = new JFileChooser();
	private boolean editableTab;
	public Color backgroundColor;

	private JTextField userDefinedWidth = new JTextField();
	private JTextField userDefinedHeight = new JTextField();
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
		// setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	}

	// Hexagons Panel
	public void initializePanelCenter() {
		if (auxiliaryHexagonsPanel != null) {

			this.remove(panelCenter);
		}

		this.topology = new Topology(20, 20);
		this.epithelium = new SphericalEpithelium(this.topology);
		this.simulation = new Simulation(this, epithelium);

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

		panelCenterLeft.add(gridSpecsPanel(), BorderLayout.PAGE_START);
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

		// auxiliaryHexagonsPanel.setBackground(backgroundColor);

		// hexagonsPanel.setBackground(backgroundColor);

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

				if (isEditable()) {
					if (i < topology.getWidth() && j < topology.getHeight()
							&& i >= 0 && j >= 0) {
						Color color = Color();
						hexagonsPanel.drawHexagon(i, j,
								hexagonsPanel.getGraphics(), color);
						getEpithelium().setInitialState(i, j);
//						System.out.println("instance @mainFrame-> "+ topology.coords2Instance(i, j));
					}
				}
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
					if (!isEditable() & epithelium.getUnitaryModel() != null) {

						String string = ("instance: "
									+ instance);
						for (NodeInfo node : epithelium.getUnitaryModel()
								.getNodeOrder()) {

							string = string + (" node: " + node + " -> value: "
									+ epithelium.getGridValue(instance, node)
									+ " ");

						}
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

				if (isEditable()) {
					if (i < topology.getWidth() && j < topology.getHeight()
							&& i >= 0 && j >= 0) {
						Color color = Color();
						hexagonsPanel.drawHexagon(i, j,
								hexagonsPanel.getGraphics(), color);
						getEpithelium().setInitialState(i, j);
						
					}
				}
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {

			}

			@Override
			public void mouseExited(MouseEvent arg0) {

			}

			@Override
			public void mousePressed(MouseEvent arg0) {

			}

			@Override
			public void mouseReleased(MouseEvent arg0) {

				// endX = arg0.getX();
				// endY = arg0.getX();
				//
				// int ind_it = (int) Math.floor((arg0.getX() / (1.5 *
				// MapPanel.radius)));
				//
				// int ind_yts = (int) (arg0.getY() - (ind_it % 2)
				// * MapPanel.height / 2);
				// int ind_jt = (int) Math.floor(ind_yts /
				// (MapPanel.height));
				//
				// int xt = (int) ((int) arg0.getX() - ind_it
				// * (1.5 * MapPanel.radius));
				// int yt = (int) (ind_yts - ind_jt * (MapPanel.height));
				// int i = 0, j = 0;
				// int deltaj = 0;
				//
				// if (yt > MapPanel.height / 2)
				// deltaj = 1;
				// else
				// deltaj = 0;
				//
				// if (xt > MapPanel.radius
				// * Math.abs(0.5 - (yt / MapPanel.height))) {
				// i = (int) ind_it;
				// j = (int) ind_jt;
				//
				// } else {
				// i = (int) ind_it - 1;
				// j = (int) (ind_jt - i % 2 + deltaj);
				// }
				//
				// Rectangle a = new Rectangle(startX - ind_it, startY -
				// ind_jt,
				// Math.abs(endX - startX), Math.abs(endY - startY));
				//
				// MapPanel.getGraphics().drawRect(startX - ind_it,
				// startY - ind_it, Math.abs(endX - startX),
				// Math.abs(endY - startY));
				//
				// for (int instance = 0; instance <
				// topology.getNumberInstances(); instance++) {
				//
				// }
			}
		});
	}

	private void initializePanelCenterRight() {

		tabbedPane = new JTabbedPane();
		startPanel = new StartPanel(this);
		componentsPanel = new ComponentsPanel(this, Color.white, epithelium);
		simulationSetupPanel = new SimulationSetupPanel(this);

		watcherPanel = new TextPanel(this);
		initial = new InitialConditions(epithelium, topology, this);
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
				if (tabbedPane.getSelectedIndex() != 0)
					editableTab = true;
				else
					editableTab = false;
			}
		});

		panelCenterRight.add(tabbedPane, BorderLayout.PAGE_START);
		panelCenterRight.add(componentsPanel, BorderLayout.CENTER);

	}

	public void setStart(boolean b) {
		this.start = b;
	}

	public boolean getStart() {
		return this.start;
	}

	public void refreshComponentsColors() {

		for (NodeInfo node : this.epithelium.getUnitaryModel().getNodeOrder()) {
			Color color = this.epithelium.getColor(node);
			componentsPanel.colorChooser2Node.get(node).setBackground(color);
			componentsPanel.colorChooser2Node.get(node).panel.mapcolor = color;
			componentsPanel.colorChooser2Node.get(node).panel.revalidate();
		}
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

	public Color Color(int i, int j) {

		int red = 255;
		int green = 255;
		int blue = 255;
		Color color = new Color(red, green, blue);
		List<NodeInfo> listNodes = this.epithelium.getUnitaryModel()
				.getNodeOrder();
		int instance = topology.coords2Instance(i, j);

		for (NodeInfo node : listNodes) {
			
			if (!epithelium.isIntegrationComponent(listNodes.indexOf(node)))
				if (this.epithelium.isDefinitionComponentDisplayOn(listNodes
						.indexOf(node))) {
					
					int value = this.epithelium.getGridValue(instance, node);
					if (value > 0) {
						color = this.epithelium.getColor(node);
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

	
	private JPanel gridSpecsPanel() {

		// TODO Passar para classe

		/*
		 * Grid Dimension: The number of rows and columns have to be even, so
		 * even if the user writes an odd number, it is automatically corrected
		 * to the next even number. Topology will hold the values. Whenever the
		 * grid dimensions are changed the mainPanel is informed of that so that
		 * the composed model can be recreated. It is particularly important
		 * when the user has already defined a composed model and then restarts
		 * the simulation and decides to change the grid's dimensions. If the
		 * model was already loaded and the user change the grids dimensions
		 * after a restart, then the grid has to reinitialize again.
		 */

		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel setWidth = new JLabel();
		JLabel setHeight = new JLabel();
		JButton loadSBML = new JButton("Load SBML");
		JLabel labelFilename = new JLabel();
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
					topology.setWidth(sanityCheckDimension(userDefinedWidth));
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
					topology.setHeight(sanityCheckDimension(userDefinedHeight));
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
					topology.setHeight(sanityCheckDimension(userDefinedWidth));
					hexagonsPanel.paintComponent(hexagonsPanel.getGraphics());
				}
			}
		});

		userDefinedHeight.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent arg0) {
				if (getEpithelium().getUnitaryModel() == null) {
					topology.setHeight(sanityCheckDimension(userDefinedHeight));
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

		int row;
		int column;

		for (int i = 0; i < topology.getNumberInstances(); i++) {

			row = topology.instance2i(i, topology.getWidth());
			column = topology.instance2j(i, topology.getHeight());

			color = Color(row, column);
			hexagonsPanel.drawHexagon(row, column, hexagonsPanel.getGraphics(),
					color);

		}
	}

	private int sanityCheckDimension(JTextField userDefined) {
		String dimString = userDefined.getText();
		int w = Integer.parseInt(dimString);
		w = (w % 2 == 0) ? w : w + 1;
		userDefined.setText("" + w);
		return w;
	}

	private void loadModel(File file) {

		SBMLFormat sbmlFormat = new SBMLFormat();
		LogicalModel logicalModel = null;

		try {
			logicalModel = sbmlFormat.importFile(file);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			sbmlFormat.export(logicalModel, baos);

			// String mySBML = new String(baos.toByteArray());

		} catch (IOException e) {
			System.err.println("Cannot import file " + file.getAbsolutePath()
					+ ": " + e.getMessage());
		}

		if (logicalModel == null)
			return;

		this.getEpithelium().setUnitaryModel(logicalModel);
		// resetAllPanels();

		this.getLogicalModelComposition().resetComposedModel();

		// setupDefinitionsButton.setEnabled(true);
		this.auxiliaryHexagonsPanel.setBorder(javax.swing.BorderFactory
				.createEmptyBorder());
		TitledBorder titleInitialConditions;
		titleInitialConditions = BorderFactory.createTitledBorder("");
		this.auxiliaryHexagonsPanel.setBorder(titleInitialConditions);

	}

	public void disableTabs(Boolean bool) {

		tabbedPane.setEnabledAt(1, !bool);
		tabbedPane.setEnabledAt(2, !bool);
		tabbedPane.setEnabledAt(3, !bool);

	}
}
