package pt.igc.nmd.epilogue;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import org.colomoto.logicalmodel.NodeInfo;

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
	public Topology topology;
	public Simulation simulation;
	public LogicalModelComposition logicalModelComposition;
	// public Grid grid;
	// public Hashtable<NodeInfo, Boolean> integrationComponents;
	public boolean initialSetupHasChanged;
	public SetupConditions initialConditions;

	private JTabbedPane tabbedPane;

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
		// this.grid = new Grid(this);
		// integrationComponents = new Hashtable<NodeInfo, Boolean>();
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
				if (getEpithelium().getComposedModel() != null)
					System.out.println("mouse Dragged");

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
				if (i < mainPanel.getTopology().getWidth()
						&& j < mainPanel.getTopology().getHeight() && i >= 0
						&& j >= 0) {
					if (getEpithelium().getComposedModel() != null) {

						Hashtable<String, Byte> original = new Hashtable<String, Byte>();
						int instance = topology.coords2Instance(i, j);

						for (NodeInfo node : epithelium.getUnitaryModel()
								.getNodeOrder()) {
							String composedNodeID = logicalModelComposition
									.computeNewName(node.getNodeID(), instance);

							// original.put(composedNodeID,
							// simulation.composedState
							// .get(composedNodeID));

							original.put(node.getNodeID(),
									simulation.composedState
											.get(composedNodeID));

						}
						// hexagonsPanel.setToolTipText("instance: " + instance
						// + " -> "
						// + " " + original);
						hexagonsPanel.setToolTipText("" + original);
					}

				}

			}

		});

	}

	public SphericalEpithelium getEpithelium() {
		return this.epithelium;
	}
	
	public void setEpithelium(SphericalEpithelium epithelium){
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
		watcherPanel = new TextPanel(this);
		auxiliaryHexagonsPanel = new JPanel();

		// Distribute Panels
		startPanel.setBounds(0, 0, 1200, 40);
		auxiliaryHexagonsPanel.setBounds(10, 60, 500, 550);

		hexagonsPanel.setBounds(10, 80, 400, 500);
		tabbedPane.setBounds(535, 60, 630, 240);
		componentsPanel.setBounds(530, 310, 650, 250);

		tabbedPane.addTab("Value Analytics", watcherPanel);
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

		tabbedPane.addTab("Initial Conditions", initialConditions);
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

		UIManager
				.put("TitledBorder.border", new LineBorder(backgroundColor, 1));
		TitledBorder titleInitialConditions;
		titleInitialConditions = BorderFactory.createTitledBorder("");
		auxiliaryHexagonsPanel.setBorder(javax.swing.BorderFactory
				.createEmptyBorder());
		auxiliaryHexagonsPanel.setBorder(titleInitialConditions);
		titleInitialConditions.setTitleColor(Color.black);

		// LogicalModel model = getUnitaryModel();

//		watcherPanel.setVisible(false);
//		componentsPanel.setVisible(false);

		// watcherPanel.setBackground(backgroundColor);
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

}
