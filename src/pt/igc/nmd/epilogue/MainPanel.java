package pt.igc.nmd.epilogue;

import java.awt.Color;
import java.awt.Dimension;


import javax.swing.BorderFactory;
import javax.swing.JFrame;

import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import org.colomoto.logicalmodel.LogicalModel;
import pt.igc.nmd.epilogue.LogicalModelComposition;



public class MainPanel extends JFrame {

	/**
	 * 
	 */
	private SphericalEpithelium epithelium ;
	private StartPanel startPanel = null;
	public DrawPolygon hexagonsPanel = null;
	public TextPanel watcherPanel;
	public ComponentsPanel componentsPanel = null;
	public Topology topology;
	public Simulation simulation;
	public LogicalModelComposition logicalModelComposition;
	
	//public LogicalModel model;
	
	
	//private InputPanel inputPanel = null;

	public MainPanel mainPanel = this;

	private static final long serialVersionUID = 1L;
	
	
	public MainPanel(){
		
		
		this.topology = new Topology(24,24);
		this.epithelium = new SphericalEpithelium(this.topology);
		this.simulation = new Simulation(this);
		this.logicalModelComposition = new LogicalModelComposition(this);
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
	 * 
	 */
	
	public SphericalEpithelium getEpithelium(){	
		return this.epithelium;
		
	}
	
	public LogicalModelComposition getLogicalModelComposition(){
		return this.logicalModelComposition;
	}
	
	public Topology getTopology(){
		return this.topology;
	}
	public Simulation getSimulation(){
		return this.simulation;
	}
	
	public void setupMainPanel() {
		startPanel = new StartPanel(this);
		hexagonsPanel = new DrawPolygon(this);
		componentsPanel = new ComponentsPanel(this, Color.white);
		watcherPanel = new TextPanel(this);

		getContentPane().setPreferredSize(new Dimension(1200, 600));
		
		getContentPane().setBackground(Color.white);
		this.setResizable(true);


		// Distribute Panels
		startPanel.setBounds(0, 0, 1200, 40);
		hexagonsPanel.setBounds(20, 60, 500, 500);
		
		TitledBorder title;
		title = BorderFactory.createTitledBorder("Value Analytics");
		//watcherPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		watcherPanel.setBorder(title);
		watcherPanel.setBounds(500, 60, 500, 200);
		watcherPanel.setVisible(false);
		watcherPanel.setBackground(Color.white);
		componentsPanel.setBounds(500, 310, 500, 250);
		componentsPanel.setVisible(false);

	
		//LogicalModel model = getUnitaryModel();
		
		getContentPane().setLayout(null);
		getContentPane().add(startPanel);
		
		getContentPane().add(hexagonsPanel);
		
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
	
	public LogicalModel getUnitaryModel(){
		repaint();
		return this.startPanel.getUnitaryModel();
	}


	
	
}
