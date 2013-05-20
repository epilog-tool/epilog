package pt.igc.nmd.epilog.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.colomoto.logicalmodel.NodeInfo;

import pt.igc.nmd.epilog.InitialConditions;
import pt.igc.nmd.epilog.InputsPanel;
import pt.igc.nmd.epilog.LogicalModelComposition;
import pt.igc.nmd.epilog.PerturbationsPanel;
import pt.igc.nmd.epilog.SetupDefinitions;
import pt.igc.nmd.epilog.Simulation;
import pt.igc.nmd.epilog.SphericalEpithelium;
import pt.igc.nmd.epilog.TextPanel;
import pt.igc.nmd.epilog.Topology;

public class MainPanel extends JFrame {

	/**
*
*/

	private SphericalEpithelium epithelium = null;

	private StartPanel startPanel = null;
	public DrawPolygon hexagonsPanel = null;
	public TextPanel watcherPanel;
	public ComponentsPanel componentsPanel = null;
	public JPanel auxiliaryHexagonsPanel;
	public InputsPanel inputsPanel = null;
	public PerturbationsPanel perturbationsPanel = null;
	public InitialConditions initial = null;

	public Topology topology;
	public Simulation simulation;
	public LogicalModelComposition logicalModelComposition;
	public boolean initialSetupHasChanged;
	public SetupDefinitions initialConditions;

	private JTabbedPane tabbedPane;

	private boolean editableTab;
	private boolean markPerturbationControl;
	private boolean clearPerturbationControl;
	public Color backgroundColor;

	public MainPanel mainPanel = this;

	private static final long serialVersionUID = 1L;

	public MainPanel() {

		this.topology = new Topology(20, 20);
		this.epithelium = new SphericalEpithelium(this.topology);
		this.simulation = new Simulation(this, epithelium);
		this.logicalModelComposition = new LogicalModelComposition(this);
		initialSetupHasChanged = false;
		this.markPerturbationControl = false;
		this.clearPerturbationControl = true;
		this.backgroundColor = new Color(0xD3D3D3);

	}

	public void initialize() throws Exception {

		UIManager.setLookAndFeel(UIManager
				.getCrossPlatformLookAndFeelClassName());

		setTitle("Epilog");
		setupMainPanel();
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
						Color color = Color(i, j);
						hexagonsPanel.drawHexagon(i, j,
								hexagonsPanel.getGraphics(), color);
						epithelium.setInitialState(i, j);
						fillHexagons();

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

					if (!isEditable() & epithelium.getUnitaryModel()!=null) {

						Hashtable<String, Byte> original = new Hashtable<String, Byte>();
						int instance = topology.coords2Instance(i, j);

						for (NodeInfo node : epithelium.getUnitaryModel()
								.getNodeOrder()) {
							if (epithelium.getComposedModel() != null) {
								String composedNodeID = logicalModelComposition
										.computeNewName(node.getNodeID(),
												instance);

								original.put(node.getNodeID(),
										simulation.composedState
												.get(composedNodeID));
								hexagonsPanel.setToolTipText("" + original);
							}
							else{
								hexagonsPanel.setToolTipText("" + node.getNodeID() + ": "+ epithelium.getGridValue(instance, node)+"\n");
							}
						}
						
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

				if (i < topology.getWidth() && j < topology.getHeight()
						&& i >= 0 && j >= 0 && isEditable()) {
					Color color = Color(i, j);
					hexagonsPanel.drawHexagon(i, j,
							hexagonsPanel.getGraphics(), color);
					epithelium.setInitialState(i, j);
					fillHexagons();
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
				// int ind_jt = (int) Math.floor(ind_yts / (MapPanel.height));
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
				// Rectangle a = new Rectangle(startX - ind_it, startY - ind_jt,
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

	private boolean isEditable() {
		return !simulation.getHasInitiated() & editableTab;
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

	public Topology getTopology() {
		return this.topology;
	}

	public Simulation getSimulation() {
		return this.simulation;
	}

	public void setupMainPanel() {

		getContentPane().setPreferredSize(new Dimension(1200, 600));
		getContentPane().setBackground(backgroundColor);
		getContentPane().setLayout(null);
		this.setResizable(true);

		tabbedPane = new JTabbedPane();
		startPanel = new StartPanel(this);
		hexagonsPanel = new DrawPolygon(this);
		componentsPanel = new ComponentsPanel(this, Color.white, epithelium);
		auxiliaryHexagonsPanel = new JPanel();

		watcherPanel = new TextPanel(this);
		initial = new InitialConditions(epithelium, topology, this);
		inputsPanel = new InputsPanel(epithelium, topology, this);
		perturbationsPanel = new PerturbationsPanel(epithelium, topology, this);

		// Distribute Panels
		startPanel.setBounds(0, 0, 1200, 40);
		auxiliaryHexagonsPanel.setBounds(10, 60, 500, 550);

		hexagonsPanel.setBounds(10, 80, 400, 500);
		tabbedPane.setBounds(535, 60, 630, 240);
		componentsPanel.setBounds(530, 310, 650, 250);

		tabbedPane.addTab("Value Analytics", watcherPanel);
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

		tabbedPane.addTab("Initial Conditions", initial);
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

		tabbedPane.addTab("Inputs", inputsPanel);
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

		tabbedPane.addTab("Perturbations and Priorities", perturbationsPanel);
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

		tabbedPane.setForegroundAt(2, Color.white);
		tabbedPane.setBackgroundAt(2, Color.gray);
		tabbedPane.setForegroundAt(3, Color.white);
		tabbedPane.setBackgroundAt(3, Color.gray);

		tabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				if (tabbedPane.getSelectedIndex() != 0)
					editableTab = true;
				else
					editableTab = false;
			}
		});

		UIManager
				.put("TitledBorder.border", new LineBorder(backgroundColor, 1));
		TitledBorder titleInitialConditions;
		titleInitialConditions = BorderFactory.createTitledBorder("");
		auxiliaryHexagonsPanel.setBorder(javax.swing.BorderFactory
				.createEmptyBorder());
		auxiliaryHexagonsPanel.setBorder(titleInitialConditions);
		titleInitialConditions.setTitleColor(Color.black);

		auxiliaryHexagonsPanel.setBackground(backgroundColor);
		startPanel.setBackground(Color.gray);
		hexagonsPanel.setBackground(backgroundColor);
		componentsPanel.setBackground(backgroundColor);

		auxiliaryHexagonsPanel.add(hexagonsPanel);
		getContentPane().add(startPanel);
		getContentPane().add(auxiliaryHexagonsPanel);
		getContentPane().add(tabbedPane);
		getContentPane().add(componentsPanel);

		repaint();

		// Adding overall ScrollPane
		JScrollPane scrollPane = new JScrollPane(getContentPane());
		setContentPane(scrollPane);

		// House Keeping
		pack();
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

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
		mainPanel.auxiliaryHexagonsPanel.setBorder(javax.swing.BorderFactory
				.createEmptyBorder());
		TitledBorder titleInitialConditions;
		titleInitialConditions = BorderFactory
				.createTitledBorder("Simulation Iteration: " + iterationNumber);
		mainPanel.auxiliaryHexagonsPanel.setBorder(titleInitialConditions);
	}

	public void setBorderHexagonsPanel() {
		mainPanel.auxiliaryHexagonsPanel.setBorder(javax.swing.BorderFactory
				.createEmptyBorder());
		TitledBorder titleInitialConditions;
		titleInitialConditions = BorderFactory
				.createTitledBorder("Initial Conditions");
		mainPanel.auxiliaryHexagonsPanel.setBorder(titleInitialConditions);

	}

	public void setMarkPerturbation(boolean b) {
		this.markPerturbationControl = b;
	}

	public boolean getMarkPerturbation() {
		return this.markPerturbationControl;
	}

	public void setClearPerturbation(boolean b) {
		this.clearPerturbationControl = b;
	}

	public boolean getClearPerturbation() {
		return this.clearPerturbationControl;
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

}
