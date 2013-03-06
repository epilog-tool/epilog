package pt.igc.nmd.epilogue;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import org.colomoto.logicalmodel.LogicalModel;



public class MainPanel extends JFrame {

	/**
	 * 
	 */
	private SphericalEpithelium epithelium ;
	private StartPanel startPanel = null;
	public static DrawPolygon hexagonsPanel = null;
	public TextPanel textPanel;
	public ComponentsPanel componentsPanel = null;
	public Topology topology;
	public Simulation simulation;
	
	//private InputPanel inputPanel = null;

	public MainPanel mainPanel = this;

	private static final long serialVersionUID = 1L;
	
	
	public MainPanel(){
		
		
		this.topology = new Topology(20,20);
		this.epithelium = new SphericalEpithelium(this.topology);
		this.simulation = new Simulation(this.epithelium);
	}
	
	
	public void initialize() throws Exception {

		UIManager.setLookAndFeel(UIManager
				.getCrossPlatformLookAndFeelClassName());

		setTitle("epilogue");
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
	
	public Topology getTopology(){
		return this.topology;
	}
	public Simulation getSimulation(){
		return this.simulation;
	}
	
	public void setupMainPanel() {
		startPanel = new StartPanel(this);
		hexagonsPanel = new DrawPolygon(this);
		hexagonsPanel.setBackground(Color.red);
		componentsPanel = new ComponentsPanel(this, Color.white);
		textPanel = new TextPanel(this);

		getContentPane().setPreferredSize(new Dimension(1100, 600));
		
		getContentPane().setBackground(Color.white);
		this.setResizable(true);


		// Distribute Panels
		startPanel.setBounds(0, 0, 1100, 40);
		hexagonsPanel.setBounds(20, 60, 500, 500);
		textPanel.setBounds(500, 60, 500, 250);
		textPanel.setVisible(false);
		componentsPanel.setBounds(500, 310, 500, 250);
		componentsPanel.setVisible(false);

	
		//LogicalModel model = getUnitaryModel();
		
		getContentPane().setLayout(null);
		getContentPane().add(startPanel);
		
		getContentPane().add(hexagonsPanel);
		
		getContentPane().add(textPanel);
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
