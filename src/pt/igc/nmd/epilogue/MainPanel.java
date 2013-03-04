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
	public TextPanel textPanel = null;
	public ComponentsPanel componentsPanel = null;
	public OptionsPanel optionsPanel = null;
	public Topology topology;
	public Simulation simulation;
	
	//private InputPanel inputPanel = null;

	public MainPanel mainPanel = this;

	private static final long serialVersionUID = 1L;
	
//	private Epithelium epithelium = new SphericalEpithelium(DEFAULT_WIDTH,
//			DEFAULT_HEIGHT);

	
	public MainPanel(){
		
		
		this.topology = new Topology();
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
		optionsPanel = new OptionsPanel(this);
		textPanel = new TextPanel(this);

		getContentPane().setPreferredSize(new Dimension(1100, 600));
		
		getContentPane().setBackground(Color.white);
		this.setResizable(true);

		//CLOSE button
		JButton btnClose = new JButton("Close");
		btnClose.setBounds(910, 5, 100, 25);
		btnClose.setBackground(Color.red);
		getContentPane().add(btnClose);
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		// Distribute Panels
		startPanel.setBounds(0, 0, 500, 40);
		hexagonsPanel.setBounds(0, 60, 500, 500);
		textPanel.setBounds(500, 60, 500, 250);
		textPanel.setVisible(false);
		componentsPanel.setBounds(500, 310, 500, 250);
		componentsPanel.setVisible(false);
		optionsPanel.setBounds(500,0,500,40);
		optionsPanel.setVisible(false);
	
		LogicalModel model = getUnitaryModel();
		
		getContentPane().setLayout(null);
		getContentPane().add(startPanel);
		
		getContentPane().add(hexagonsPanel);
		
		getContentPane().add(textPanel);
		getContentPane().add(componentsPanel);
		getContentPane().add(optionsPanel);
		
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
