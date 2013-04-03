package pt.igc.nmd.epilogue;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import org.colomoto.logicalmodel.LogicalModel;
import pt.igc.nmd.epilogue.LogicalModelComposition;

public class MainPanel extends JFrame {

	/**
*
*/
	private SphericalEpithelium epithelium;
	private StartPanel startPanel = null;
	public DrawPolygon hexagonsPanel = null;
	public TextPanel watcherPanel;
	public ComponentsPanel componentsPanel = null;
	public JPanel auxiliaryHexagonsPanel;
	public Topology topology;
	public Simulation simulation;
	public LogicalModelComposition logicalModelComposition;
	public Grid grid;

	public MainPanel mainPanel = this;

	private static final long serialVersionUID = 1L;

	public MainPanel() {

		this.topology = new Topology(20, 20);
		this.epithelium = new SphericalEpithelium(this.topology);
		this.simulation = new Simulation(this);
		this.logicalModelComposition = new LogicalModelComposition(this);
		this.grid = new Grid(this);
	}

	public void initialize() throws Exception {

		UIManager.setLookAndFeel(UIManager
				.getCrossPlatformLookAndFeelClassName());

		setTitle("Epilog");
		setupMainPanel();

	}

	/*
	 * 
	 * START PANEL
	 */

	public SphericalEpithelium getEpithelium() {
		return this.epithelium;

	}
	
	public Grid getGrid() {
		return this.grid;
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
		startPanel = new StartPanel(this);
		hexagonsPanel = new DrawPolygon(this);
		componentsPanel = new ComponentsPanel(this, Color.white);
		watcherPanel = new TextPanel(this);
		auxiliaryHexagonsPanel = new JPanel();

		getContentPane().setPreferredSize(new Dimension(1200, 600));

		getContentPane().setBackground(Color.white);
		this.setResizable(true);

		// Distribute Panels
		startPanel.setBounds(0, 0, 1200, 40);
		auxiliaryHexagonsPanel.setBounds(10, 60, 500, 550);
		auxiliaryHexagonsPanel.setBackground(Color.white);
		hexagonsPanel.setBounds(10, 80, 500, 500);

		LineBorder border = new LineBorder(Color.black, 1, true);
		TitledBorder title = new TitledBorder(border, "Value Analytics",
				TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION, new Font(
						"Arial", Font.ITALIC, 14), Color.black);

		// title = BorderFactory.createTitledBorder("Value Analytics");
		// watcherPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		watcherPanel.setBorder(title);
		watcherPanel.setBounds(550, 60, 500, 200);
		watcherPanel.setVisible(false);
		watcherPanel.setBackground(Color.white);
		componentsPanel.setBounds(550, 310, 500, 250);
		componentsPanel.setVisible(false);
		UIManager.put("TitledBorder.border", new LineBorder(new Color(255, 255,
				255), 1));
		TitledBorder titleInitialConditions;
		titleInitialConditions = BorderFactory
				.createTitledBorder("");
		auxiliaryHexagonsPanel.setBorder(javax.swing.BorderFactory
				.createEmptyBorder());
		auxiliaryHexagonsPanel.setBorder(titleInitialConditions);
		titleInitialConditions.setTitleColor(Color.black);
		// LogicalModel model = getUnitaryModel();

		getContentPane().setLayout(null);
		getContentPane().add(startPanel);

		auxiliaryHexagonsPanel.add(hexagonsPanel);
		getContentPane().add(auxiliaryHexagonsPanel);
		getContentPane().add(watcherPanel);
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

	public LogicalModel getUnitaryModel() {
		repaint();
		return this.startPanel.getUnitaryModel();
	}

}
