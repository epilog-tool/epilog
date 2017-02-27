package org.cellularasynchrony;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import org.colomoto.logicalmodel.NodeInfo;
import org.colomoto.logicalmodel.perturbation.AbstractPerturbation;


public class SimulationSetupPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6167613300012277711L;

	public MainFrame mainFrame;
	
	public JComboBox<String> updateMode;
	public JComboBox<Integer> alpha;
	public JComboBox<String> onlyUpdatable;	
	
	public JComboBox<String> rollOver;
	

	private JButton runButton;
	private JButton stepButton;
	public JButton restartButton;
	public JTextField stopCriterium;
	private JPanel centerCenterPanel;

	public JComboBox<String> initialCombo;
	public JComboBox<String> inputCombo;
	public JComboBox<String> perturbationsCombo;
	public JComboBox<String> prioritiesCombo;

	JPanel leftPanel = new JPanel();

	public SimulationSetupPanel(MainFrame mainPanel) {
		this.mainFrame = mainPanel;
		init();

	}

	/**
	 * Initializes the simulation setup panel, to be inserted in the tab on
	 * CAAs main panel.
	 * 
	 * @return this panel
	 * 
	 */
	public JPanel init() {

		FlowLayout layout = new FlowLayout();
		layout.setAlignment(FlowLayout.LEFT);
		setLayout(new BorderLayout());

		// CENTER

		JPanel centerPanel = new JPanel(new BorderLayout());
		JPanel startCenterPanel = new JPanel(new FlowLayout());

		centerCenterPanel = mainFrame.componentsPanel.init();

		centerPanel.add(centerCenterPanel, BorderLayout.CENTER);
		centerPanel.add(startCenterPanel,BorderLayout.PAGE_START);
		//add(centerPanel, BorderLayout.CENTER);
		
		// PAGE START PANEL

		JPanel startPanel = new JPanel(layout);

		rollOver = new JComboBox<String>();

		rollOver.addItem("No Roll-Over");
		if (mainFrame.topology.getHeight() % 2 == 0)
			rollOver.addItem("Vertical Roll-Over");
		if (mainFrame.topology.getWidth() % 2 == 0)
			rollOver.addItem("Horizontal Roll-Over");
		if (mainFrame.topology.getHeight() % 2 == 0 && mainFrame.topology.getWidth() % 2 == 0)
			rollOver.addItem("Double Roll-Over");	
	
		if (mainFrame.topology.getHeight() % 2 != 0
				& mainFrame.topology.getWidth() % 2 != 0)
			rollOver.setEnabled(false);
		String aux = (String) rollOver.getSelectedItem();
		if (mainFrame.getEpithelium().getUnitaryModel() != null)
			mainFrame.topology.setRollOver(aux);

		rollOver.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				JComboBox<?> source = (JComboBox<?>) event.getSource();
				String optionString = (String) source.getSelectedItem();
				fireRollOverChange(optionString);
				mainFrame.setInitialSetupHasChanged(true);

			}
		});

		/*
		 * Option to choose update mode
		 */

		updateMode = new JComboBox<String>();

		updateMode.addItem("Synchronous");
		updateMode.addItem("Random Independent");
		updateMode.addItem("Random Order");
		updateMode.addItem("Cyclic Order");
		updateMode.addItem("Exponential Clocked");

		updateMode.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				JComboBox<?> source = (JComboBox<?>) event.getSource();
				String optionString = (String) source.getSelectedItem();
				fireUpdateModeChange(optionString);
				mainFrame.setInitialSetupHasChanged(true);

			}
		});
		

		/*
		 * Option to only look at updatable cells
		 */

		onlyUpdatable = new JComboBox<String>();

		onlyUpdatable.addItem("All cells");
		onlyUpdatable.addItem("Updatable cells");

//		String updateModeAux = (String) updateMode.getSelectedItem();
//		if (mainFrame.getEpithelium().getUnitaryModel() != null)
//			mainFrame.topology.setRollOver(aux);

		onlyUpdatable.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				JComboBox<?> source = (JComboBox<?>) event.getSource();
				String optionString = (String) source.getSelectedItem();
				fireOnlyUpdatableChange(optionString);
				mainFrame.setInitialSetupHasChanged(true);

			}
		});
		
		/*
		 * Option to choose probability of cells to update
		 */
		JLabel alphaLabel = new JLabel();
		alphaLabel.setText("alpha: ");
		
		alpha = new JComboBox<Integer>();
		alpha.setPreferredSize(new Dimension(50, 26));
		alpha.setEnabled(false);
		for(int i=0;i<=100;i++) 
		{
			alpha.addItem(i);
			}
		
		mainFrame.simulation.setAlpha(0);
		
		alpha.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				JComboBox<?> source = (JComboBox<?>) event.getSource();
				 int optionint = (int) source.getSelectedItem();
				fireAlphaChange(optionint);
				mainFrame.setInitialSetupHasChanged(true);

			}
		});
		
		/*
		 * Maximum Number of Iterations:
		 */

//		JLabel maxIterationsLabel = new JLabel();
//		maxIterationsLabel.setText("Max Iter:");
//		maxIterations = new JTextField();
//		maxIterations.setPreferredSize(new Dimension(54, 26));
//		maxIterations.setText(""+3000);
//		startPanel.add(maxIterations);

		/*
		 * Number of Simulations:
		 */
//		JLabel simulationNumberLabel = new JLabel();
//		simulationNumberLabel.setText("#Sim.:");
//		simulationNumber = new JTextField();
//		simulationNumber.setPreferredSize(new Dimension(54, 26));
//		simulationNumber.setText(""+3000);
		
		add(startPanel, BorderLayout.PAGE_START);
		
		
		
		startPanel.add(rollOver);
		startPanel.add(updateMode);
		startPanel.add(onlyUpdatable);
		startPanel.add(alphaLabel);
		startPanel.add(alpha);
//		startPanel.add(maxIterationsLabel);
//		startPanel.add(maxIterations);
//		startPanel.add(simulationNumberLabel);
//		startPanel.add(simulationNumber);
		
		
		runButton = new JButton("R");
		
		stepButton = new JButton("S");

		/*
		 * Step Button: Initiates a step-by-step simulation. It also repaints
		 * the border of the hexagons panel with the iteration number and
		 * disables the step and run button when it is over
		 */
		stepButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				mainFrame.disableTabs(true);
				mainFrame.simulation.step();

				if (mainFrame.simulation.hasStableStateFound()) {
					stepButton.setEnabled(false);
					runButton.setEnabled(false);
				}
			}
		});

		/*
		 * Run Button:
		 */
		runButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainFrame.simulation.setStopCriterium(stopCriterium.getText());
		
				mainFrame.simulation.run();
				if (mainFrame.simulation.hasStableStateFound()) {
					stepButton.setEnabled(false);
					runButton.setEnabled(false);
				}
			}
		});
		
		stopCriterium= new JTextField();
		stopCriterium.setPreferredSize(new Dimension(29, 26));
		stopCriterium.setText(""+30);

		startPanel.add(runButton);
		startPanel.add(stopCriterium);
		startPanel.add(stepButton);
		

		restartButton = new JButton("Rst");

		/*
		 * Restart Button: Initials conditions are not changed and the
		 * simulation status is reset. The composed model is already created and
		 * it is only updated if the user changed the integration input
		 * definitions, grid dimensions or roll-over options.
		 * 
		 * After the Restart the following happens: 1) Reset the iteration
		 * number 2) Title border resets to "Initial Conditions" 3) Simulation
		 * Button is visible again 4) Step and Run buttons are enabled and
		 * invisible 5) Grid Dimensions are enabled 6) Analytics Board is
		 * restarted 7) Initial Conditions values are reloaded
		 */

		restartButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				mainFrame.disableTabs(false);
				mainFrame.simulation.reset();
				simulationPanelson();
				TitledBorder titleInitialConditions;
				titleInitialConditions = BorderFactory.createTitledBorder("");
				mainFrame.auxiliaryHexagonsPanel
						.setBorder(javax.swing.BorderFactory
								.createEmptyBorder());
				mainFrame.auxiliaryHexagonsPanel
						.setBorder(titleInitialConditions);
				runButton.setEnabled(true);
				stepButton.setEnabled(true);
				restartButton.setEnabled(false);
				mainFrame.componentsPanel.saveAsInitialState.setEnabled(false);
				mainFrame.componentsPanel.setICName.setEnabled(false);
				mainFrame.setUserMessageEmpty();

			}
		});

		restartButton.setEnabled(false);


		startPanel.add(restartButton);
		

		// LINE START

		JPanel simulationOptionsPanel = new JPanel(new BorderLayout());
		JPanel epitheliumOptionsPanel = new JPanel();
//		JPanel southLineStartPanel = new JPanel();

		JPanel[] auxiliary = new JPanel[4];

		leftPanel.setLayout(new BorderLayout());

		initialCombo = new JComboBox<String>();
		inputCombo = new JComboBox<String>();
		perturbationsCombo = new JComboBox<String>();
		prioritiesCombo = new JComboBox<String>();

		JLabel initialConditionsLabel = new JLabel();
		JLabel inputsLabel = new JLabel();
		JLabel perturbationsLabel = new JLabel();
		JLabel prioritiesLabel = new JLabel();

		initialConditionsLabel.setText("Choose an initial state set: ");
		inputsLabel.setText("Choose an input set: ");
		perturbationsLabel.setText("Choose a perturbation set: ");
		prioritiesLabel.setText("Choose a priorities set: ");

		prioritiesCombo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JComboBox<?> src = (JComboBox<?>) arg0.getSource();
				mainFrame.epithelium.setSelectedPriority((String) src
						.getSelectedItem());
				String string = "priorities";
				// needToResetComposedModel(string, (String)
				// src.getSelectedItem());
			}
		});

		initialCombo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JComboBox<?> src = (JComboBox<?>) arg0.getSource();
				mainFrame.epithelium.setSelectedInitialSet((String) src
						.getSelectedItem());
				mainFrame.hexagonsPanel.repaint();
				mainFrame.fillHexagons();
			}
		});

		inputCombo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JComboBox<?> src = (JComboBox<?>) arg0.getSource();
				mainFrame.epithelium.setSelectedInputSet((String) src
						.getSelectedItem());
				String string = "input";
//				repaintSimulationSetupPanel();

			}
		});

		perturbationsCombo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				JComboBox<?> src = (JComboBox<?>) arg0.getSource();
				mainFrame.epithelium.setSelectedPerturbation((String) src
						.getSelectedItem());
				String string = "perturbation";
				mainFrame.hexagonsPanel.repaint();
				mainFrame.fillHexagons();
			}
		});

		fillCombo(initialCombo, "initial");
		fillCombo(inputCombo, "input");
		fillCombo(perturbationsCombo, "perturbations");
		fillCombo(prioritiesCombo, "priorities");

		auxiliary[0] = new JPanel();
		auxiliary[1] = new JPanel();
		auxiliary[2] = new JPanel();
		auxiliary[3] = new JPanel();

		auxiliary[0].add(initialConditionsLabel);
		auxiliary[0].add(initialCombo);
		auxiliary[1].add(inputsLabel);
		auxiliary[1].add(inputCombo);
		auxiliary[2].add(perturbationsLabel);
		auxiliary[2].add(perturbationsCombo);
		auxiliary[3].add(prioritiesLabel);
		auxiliary[3].add(prioritiesCombo);

		simulationOptionsPanel.add(auxiliary[0], BorderLayout.PAGE_START);
		simulationOptionsPanel.add(auxiliary[3], BorderLayout.CENTER);
		simulationOptionsPanel.add(auxiliary[2], BorderLayout.PAGE_END);

		epitheliumOptionsPanel.add(auxiliary[1]);

		LineBorder border = new LineBorder(Color.black, 1, true);

		TitledBorder north = new TitledBorder(border, "Definitions",
				TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION, new Font(
						"Arial", Font.ITALIC, 14), Color.black);

		TitledBorder center = new TitledBorder(border, "Epithelium",
				TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION, new Font(
						"Arial", Font.ITALIC, 14), Color.black);




		JScrollPane valueAnalyticsPanel = new JScrollPane(mainFrame.panelToolTip);


		simulationOptionsPanel.setBorder(north);
		epitheliumOptionsPanel.setBorder(center);

		
		leftPanel.setBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED));
		
		
		JPanel simulationOptionsPanels = new JPanel();
		simulationOptionsPanels.setLayout(new BoxLayout(simulationOptionsPanels, BoxLayout.Y_AXIS));
		
		simulationOptionsPanels.add(simulationOptionsPanel);
		simulationOptionsPanels.add(epitheliumOptionsPanel);

		leftPanel.add(simulationOptionsPanels, BorderLayout.PAGE_START);
		leftPanel.add(valueAnalyticsPanel, BorderLayout.CENTER);

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,leftPanel,centerPanel);
		splitPane.setDividerLocation(leftPanel.getPreferredSize().width);
		splitPane.setDividerSize(3);
		add(splitPane, BorderLayout.CENTER);

		return this;
	}



	protected void fireAlphaChange(int optionint) {
		// TODO Auto-generated method stub
		mainFrame.simulation.setAlpha(optionint);
	}

	@SuppressWarnings("unused")
	private void repaintSimulationSetupPanel() {
		centerCenterPanel.removeAll();
		centerCenterPanel.repaint();
		centerCenterPanel.revalidate();
		this.remove(0);
		JPanel aux = new JPanel();
		aux = mainFrame.componentsPanel.init();
		add(aux, 0);

	}



	/**
	 * Adds names of the sets to each of the combo box related to simulation and
	 * epithelium definitions in the simulations setup panel.
	 * 
	 * @param string
	 *            string related with the type of modification that will force
	 *            the creation of a new composed model
	 * @param combo
	 *            comboBox related with the type of modifications
	 * 
	 */
	private void fillCombo(JComboBox<String> combo, String string) {

		if (string == "initial") {
			Hashtable<String, Grid> set = mainFrame.epithelium
					.getInitialStateSet();
			for (String i : set.keySet()) {
				if (i == "none")
					combo.addItem(i);
			}
			for (String i : set.keySet()) {
				if (i != "none")
					combo.addItem(i);
			}
		} else if (string == "input") {
			// combo.addItem("none");
			Hashtable<String, Hashtable<NodeInfo, List<String>>> set = mainFrame.epithelium
					.getInputsIntegrationSet();
			for (String i : set.keySet()) {
				if (i == "none")
					combo.addItem(i);
			}
			for (String i : set.keySet()) {
				if (i != "none")
					combo.addItem(i);
			}

		} else if (string == "perturbations") {

			Hashtable<String, AbstractPerturbation[]> set = mainFrame.epithelium
					.getPerturbationsSet();
			for (String i : set.keySet()) {
				if (i == "none")
					combo.addItem(i);
			}
			for (String i : set.keySet()) {
				if (i != "none")
//					System.out.println(i);
					combo.addItem(i);
					
			}

		} else if (string == "priorities") {
			combo.addItem("none");
			Hashtable<String, List<List<String>>> set = mainFrame.epithelium
					.getPrioritiesSet();
			for (String i : set.keySet()) {
				combo.addItem(i);
			}
		}
	}

	/**
	 * Updates Topology with the selected roll over option.
	 * 
	 * @param optionString
	 *            roll-over option selected
	 * 
	 * @see Topology
	 */
	private void fireRollOverChange(String optionString) { // ROLL OVER
		mainFrame.topology.setRollOver(optionString);
	}

	protected void fireUpdateModeChange(String optionString) {
		// TODO Auto-generated method stub
		mainFrame.simulation.setUpdateMode(optionString);
		if (optionString != "Synchronous")
			alpha.setEnabled(true);
		else
			alpha.setEnabled(false);
	}


	protected void fireOnlyUpdatableChange(String optionString) {
		// TODO Auto-generated method stub
		mainFrame.simulation.setOnlyUpdatable(optionString);
	}
	
	// Tabs on or off

	/**
	 * Disables all tabs, except the simulation tab.
	 * 
	 */
	public void simulationPanelsoff() {
		initialCombo.setEnabled(false);
		inputCombo.setEnabled(false);
		perturbationsCombo.setEnabled(false);
		prioritiesCombo.setEnabled(false);
		updateMode.setEnabled(false);
		rollOver.setEnabled(false);
		alpha.setEnabled(false);
		onlyUpdatable.setEnabled(false);
//		maxIterations.setEnabled(false);
//		simulationNumber.setEnabled(false);
		
		
	}

	/**
	 * Enables all tabs.
	 * 
	 */
	public void simulationPanelson() {
		initialCombo.setEnabled(true);
		inputCombo.setEnabled(true);
		perturbationsCombo.setEnabled(true);
		prioritiesCombo.setEnabled(true);
		updateMode.setEnabled(true);
		rollOver.setEnabled(true);
		alpha.setEnabled(true);
		onlyUpdatable.setEnabled(true);
//		maxIterations.setEnabled(true);
//		simulationNumber.setEnabled(true);
	}

}
