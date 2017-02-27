package org.cellularasynchrony;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class StochasticSimulationPanel extends JPanel {

	public JComboBox<String> updateMode;
	public JComboBox<Integer> alpha;
	public JComboBox<Integer> beta;
	public JComboBox<String> onlyUpdatable;
	public JTextField maxCellIterations;
	public JTextField maxSimulations;
	public JComboBox<String> initialState;

	public JComboBox<String> rollOver;
	public JComboBox<String> perturbations;
	public JComboBox<String> priorities;
	public JComboBox<String> integrationFunction;
	public JComboBox<String> compToShowCombo;
	public JCheckBox range;
	
	public JCheckBox green;
	public JCheckBox red;
	public JCheckBox orange;
	public JCheckBox yellow;
	public JCheckBox blue;
	public JCheckBox purple;
	
	private static final long serialVersionUID = 5470169327482303234L;

	private MainFrame mainFrame = null;
	/**
	 * Generates the priorities panel, to be inserted in the tab on Epilog's
	 * main panel.
	 * 
	 * @param mainFrame
	 */
	public StochasticSimulationPanel(MainFrame mainFrame) {

		this.mainFrame = mainFrame;
		init();
	}

	/**
	 * Initializes the stochastic simulation panel.
	 */
	public void init() {

		setLayout(new BorderLayout());

		// PAGE START PANEL
		JPanel simulationPageStart = new JPanel(new BorderLayout());

		// SIMULATION DEFs
		JPanel simulationDefinitionTop = new JPanel(new FlowLayout());
		JPanel simulationDefinitionBottom = new JPanel(new FlowLayout());
		JPanel simulationDefinition = new JPanel(new BorderLayout());


		LineBorder border = new LineBorder(Color.black, 1, true);
		TitledBorder south = new TitledBorder(border,
				"Update Mode Definitions", TitledBorder.LEFT,
				TitledBorder.DEFAULT_POSITION, new Font("Arial", Font.ITALIC,
						14), Color.black);
		simulationDefinition.setBorder(south);



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
			}
		});


		/*
		 * Option to choose update mode
		 */

		updateMode = new JComboBox<String>();

		updateMode.addItem("Random Independent");
		updateMode.addItem("Random Order");
		updateMode.addItem("Cyclic Order");
		updateMode.addItem("Exponential Clocked");
		updateMode.addItem("Synchronous");

		aux = (String) updateMode.getSelectedItem();
		if (mainFrame.getEpithelium().getUnitaryModel() != null)
			mainFrame.simulation.setUpdateMode(aux);


		updateMode.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				JComboBox<?> source = (JComboBox<?>) event.getSource();
				String optionString = (String) source.getSelectedItem();
				fireUpdateModeChange(optionString);
				if (optionString != "Synchronous")
					alpha.setEnabled(true);
				else
					alpha.setEnabled(false);

			}
		});


		/*
		 * Option to only look at updatable cells
		 */

		onlyUpdatable = new JComboBox<String>();

		onlyUpdatable.addItem("All cells");
		onlyUpdatable.addItem("Updatable cells");

		aux = (String) onlyUpdatable.getSelectedItem();
		if (mainFrame.getEpithelium().getUnitaryModel() != null)
			mainFrame.simulation.setOnlyUpdatable(aux);


		onlyUpdatable.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				JComboBox<?> source = (JComboBox<?>) event.getSource();
				String optionString = (String) source.getSelectedItem();
//				System.out.println(optionString);
				fireOnlyUpdatableChange(optionString);
			}
		});

		/*
		 * Option to choose probability of cells to update
		 */
		JLabel alphaLabel = new JLabel();
		alphaLabel.setText("alpha: ");

		alpha = new JComboBox<Integer>();
		alpha.setPreferredSize(new Dimension(50, 26));
		alpha.setEnabled(true);
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

			}
		});

		simulationDefinitionTop.add(rollOver);
		simulationDefinitionTop.add(updateMode);
		simulationDefinitionTop.add(onlyUpdatable);
		simulationDefinitionTop.add(alphaLabel);
		simulationDefinitionTop.add(alpha);


		simulationDefinition.add(simulationDefinitionTop, BorderLayout.PAGE_START);
	

		// simulationDefinitionBottom


		/*
		 * Option to choose what to do with the initial state
		 */

		initialState = new JComboBox<String>();

		initialState.addItem("Random");
		initialState.addItem("All Zeros");
		initialState.addItem("Saved 1");


		initialState.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				JComboBox<?> source = (JComboBox<?>) event.getSource();
				source.getSelectedItem();

			}
		});

		perturbations = new JComboBox<String>();

		JLabel initialConditionsLabel = new JLabel();
		JLabel inputsLabel = new JLabel();
		JLabel perturbationsLabel = new JLabel();
		JLabel prioritiesLabel = new JLabel();

		initialConditionsLabel.setText("Initial state set: ");
		inputsLabel.setText("Input set: ");
		perturbationsLabel.setText("Perturbation set: ");
		prioritiesLabel.setText("Priorities set: ");


		perturbations.addItem("None");
		perturbations.addItem("Saved 1");

		perturbations.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				JComboBox<?> source = (JComboBox<?>) event.getSource();
				source.getSelectedItem();

			}
		});

		priorities = new JComboBox<String>();

		priorities.addItem("None");
		priorities.addItem("Saved 1");

		priorities.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				JComboBox<?> source = (JComboBox<?>) event.getSource();
				source.getSelectedItem();

			}
		});

		integrationFunction = new JComboBox<String>();

		integrationFunction.addItem("None");
		integrationFunction.addItem("Saved 1");

		integrationFunction.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				JComboBox<?> source = (JComboBox<?>) event.getSource();
				source.getSelectedItem();

			}
		});

		simulationDefinitionBottom.add(initialConditionsLabel);
		simulationDefinitionBottom.add(initialState);
		simulationDefinitionBottom.add(perturbationsLabel);
		simulationDefinitionBottom.add(perturbations);
		simulationDefinitionBottom.add(prioritiesLabel);
		simulationDefinitionBottom.add(priorities);
		simulationDefinitionBottom.add(inputsLabel);
		simulationDefinitionBottom.add(integrationFunction);

		simulationDefinition.add(simulationDefinitionBottom, BorderLayout.PAGE_END);
		
		simulationPageStart.add(simulationDefinition, BorderLayout.PAGE_START);
		
		// Cellular spatial update
		
		// SIMULATION DEFs
				JPanel spatialDefinitionTop = new JPanel(new FlowLayout());
				JPanel spatialDefinitionBottom = new JPanel(new FlowLayout());
				JPanel spatialDefinition = new JPanel(new BorderLayout());


				TitledBorder spatial = new TitledBorder(border,
						"Spatial Update Definitions", TitledBorder.LEFT,
						TitledBorder.DEFAULT_POSITION, new Font("Arial", Font.ITALIC,
								14), Color.black);
				spatialDefinition.setBorder(spatial);

				/*
				 * Option to choose update mode
				 */

				updateMode = new JComboBox<String>();

				updateMode.addItem("Random Independent");
				updateMode.addItem("Random Order");
				updateMode.addItem("Cyclic Order");
				updateMode.addItem("Exponential Clocked");
				updateMode.addItem("Synchronous");

				aux = (String) updateMode.getSelectedItem();
				if (mainFrame.getEpithelium().getUnitaryModel() != null)
					mainFrame.topology.setUpdateMode(aux);


				updateMode.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent event) {
						JComboBox<?> source = (JComboBox<?>) event.getSource();
						String optionString = (String) source.getSelectedItem();
						fireUpdateModeChangeSpatial(optionString);
						if (optionString != "Synchronous")
							beta.setEnabled(true);
						else
							beta.setEnabled(false);

					}
				});


				/*
				 * Option to only look at updatable cells
				 */

				onlyUpdatable = new JComboBox<String>();

				onlyUpdatable.addItem("All cells");
				onlyUpdatable.addItem("Updatable cells");


				aux = (String) onlyUpdatable.getSelectedItem();
				if (mainFrame.getEpithelium().getUnitaryModel() != null){
					mainFrame.simulation.setOnlyUpdatable("All cells");
					mainFrame.stochasticSimulation.setOnlyUpdate("All cells");
				}
				
				onlyUpdatable.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent event) {
						JComboBox<?> source = (JComboBox<?>) event.getSource();
						String optionString = (String) source.getSelectedItem();
						fireOnlyUpdatableChangeSpatial(optionString);
						
					}
				});

				/*
				 * Option to choose probability of cells to update
				 */
				JLabel betaLabel = new JLabel();
				betaLabel.setText("beta: ");

				beta = new JComboBox<Integer>();
				beta.setPreferredSize(new Dimension(50, 26));
				beta.setEnabled(true);
				for(int i=0;i<=100;i++) 
				{
					beta.addItem(i);
				}

				mainFrame.topology.setBeta(0);

				beta.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent event) {
						JComboBox<?> source = (JComboBox<?>) event.getSource();
						int optionint = (int) source.getSelectedItem();
						fireBetaChange(optionint);

					}
				});

				spatialDefinitionTop.add(updateMode);
				spatialDefinitionTop.add(onlyUpdatable);
				spatialDefinitionTop.add(betaLabel);
				spatialDefinitionTop.add(beta);


				spatialDefinition.add(spatialDefinitionTop, BorderLayout.PAGE_START);
				simulationPageStart.add(spatialDefinition, BorderLayout.CENTER);
			
				
				simulationPageStart.add(simulationDefinition, BorderLayout.PAGE_START);
				add(simulationPageStart,BorderLayout.PAGE_START);
		

		// CENTER PANEL: SPATIAL DISTRIBUTION
		JPanel spatialDistributionsPanelLeft = new JPanel(new FlowLayout());
		JPanel spatialDistributionsPanelRight = new JPanel();
		JPanel spatialDistributionsPanel = new JPanel(new FlowLayout());

		
		
		
		
		
		LineBorder gridBorder = new LineBorder(Color.black, 1, true);
		TitledBorder center = new TitledBorder(gridBorder,
				"Spatial Definitions", TitledBorder.LEFT,
				TitledBorder.DEFAULT_POSITION, new Font("Arial", Font.ITALIC,
						14), Color.black);
		spatialDistributionsPanel.setBorder(center);

		ImageIcon hexagon = new ImageIcon("hexagon.png");
		JButton button = new JButton(hexagon);
		button.setPreferredSize(new Dimension(150, 150));
		button.setBackground(Color.white);


		spatialDistributionsPanelLeft.add(button);
		
		spatialDistributionsPanelRight.setLayout(new BoxLayout(spatialDistributionsPanelRight, BoxLayout.Y_AXIS));
		
		green = new JCheckBox("Green Hexagons");
		green.setSelected(true);
		yellow = new JCheckBox("Yellow Hexagons");
		yellow.setSelected(true);
		orange = new JCheckBox("Orange Hexagons");
		orange.setSelected(true);
		red = new JCheckBox("Red Hexagons");
		red.setSelected(true);
		purple = new JCheckBox("Purple Hexagons");
		purple.setSelected(true);
		blue = new JCheckBox("Blue Hexagons");
		blue.setSelected(true);

		spatialDistributionsPanelRight.add(green);
		spatialDistributionsPanelRight.add(yellow);
		spatialDistributionsPanelRight.add(orange);
		spatialDistributionsPanelRight.add(red);
		spatialDistributionsPanelRight.add(purple);
		spatialDistributionsPanelRight.add(blue);

		spatialDistributionsPanel.add(spatialDistributionsPanelLeft);
		spatialDistributionsPanel.add(spatialDistributionsPanelRight);
		
		add(spatialDistributionsPanel, BorderLayout.CENTER);
		
		// CENTER PANEL: Simulation specifications
		JPanel simulationSpecificationsPanel = new JPanel(new FlowLayout());

//		LineBorder gridBorder_SSP = new LineBorder(Color.black, 1, true);
		TitledBorder center_SSP = new TitledBorder(gridBorder,
				"Simulation Specifications", TitledBorder.LEFT,
				TitledBorder.DEFAULT_POSITION, new Font("Arial", Font.ITALIC,
						14), Color.black);
		simulationSpecificationsPanel.setBorder(center_SSP);

		JLabel compToShow = new JLabel();
		compToShow.setText("Select component to show: ");

		compToShowCombo = new JComboBox<String>();

		for (int i = 0; i <mainFrame.getEpithelium().getUnitaryModel().getNodeOrder().size();i++)
			compToShowCombo.addItem(mainFrame.getEpithelium().getUnitaryModel().getNodeOrder().get(i).toString());

		compToShowCombo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				JComboBox<?> source = (JComboBox<?>) event.getSource();
				String optionString = (String) source.getSelectedItem();
				fireCompToShowCombo(optionString);
			}
		});
		
		/*
		 * Maximum Number of Iterations:
		 */

		JLabel maxIterationsLabel = new JLabel();
		maxIterationsLabel.setText("Max. Iter.:");
		maxCellIterations = new JTextField();
		maxCellIterations.setPreferredSize(new Dimension(54, 26));
		maxCellIterations.setText(""+30);


		/*
		 * Number of Simulations:
		 */
		JLabel simulationNumberLabel = new JLabel();
		simulationNumberLabel.setText("#Sim.:");
		maxSimulations = new JTextField();
		maxSimulations.setPreferredSize(new Dimension(54, 26));
		maxSimulations.setText(""+1000);
		
		range = new JCheckBox();
		JLabel rangeLabel = new JLabel();
		rangeLabel.setText("Range Stochastics");
		range.setSelected(true);
	
		simulationSpecificationsPanel.add(compToShow);
		simulationSpecificationsPanel.add(compToShowCombo);
		simulationSpecificationsPanel.add(rangeLabel);
		simulationSpecificationsPanel.add(range);
		simulationSpecificationsPanel.add(maxIterationsLabel);
		simulationSpecificationsPanel.add(maxCellIterations);
		simulationSpecificationsPanel.add(simulationNumberLabel);
		simulationSpecificationsPanel.add(maxSimulations);

//		add(simulationSpecificationsPanel,BorderLayout.PAGE_END);


//		// END Panel with Run Button
//
//		JPanel runningSetupPanel = new JPanel(new FlowLayout());

		JButton runButton = new JButton("Run");

		runButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					fireRunStochastcSimulation();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		simulationSpecificationsPanel.add(runButton);
		add(simulationSpecificationsPanel, BorderLayout.PAGE_END);


	}

	protected void fireCompToShowCombo(String optionString) {
		mainFrame.stochasticSimulation.setCompToShow(optionString);
		mainFrame.stochasticSimulation.getCumulativeGrid();
		mainFrame.hexagonsPanel.repaint();
		mainFrame.stochasticSimulation.paintCumulativeGrid();

	}

	protected void fireAlphaChange(int optionint) {
		// TODO Auto-generated method stub
		mainFrame.simulation.setAlpha(optionint);
	}
	
	protected void fireBetaChange(int optionint) {
		// TODO Auto-generated method stub
		mainFrame.topology.setBeta(optionint);
	}

	protected void fireRunStochastcSimulation() throws IOException {

		if (!range.isSelected()){
			mainFrame.stochasticSimulation.reset();


			mainFrame.topology.setBeta((int) beta.getSelectedItem());
			mainFrame.stochasticSimulation.setMaxIteration(Integer.parseInt(maxCellIterations.getText()));
			mainFrame.stochasticSimulation.setAlpha((int) alpha.getSelectedItem());
			mainFrame.stochasticSimulation.setRollOver((String) rollOver.getSelectedItem());
			mainFrame.stochasticSimulation.setUpdateScheme((String) updateMode.getSelectedItem());
			mainFrame.stochasticSimulation.setOnlyUpdate(mainFrame.stochasticSimulation.getOnlyUpdate());
			mainFrame.stochasticSimulation.setSimulationsNumber(Integer.parseInt(maxSimulations.getText()));
			
			mainFrame.topology.SetBlue(blue.isSelected());
			mainFrame.topology.SetGreen(green.isSelected());
			mainFrame.topology.SetRed(red.isSelected());
			mainFrame.topology.SetYellow(yellow.isSelected());
			mainFrame.topology.SetOrange(orange.isSelected());
			mainFrame.topology.SetPurple(purple.isSelected());
			

			mainFrame.simulation.setMaxCellIterationNumber(maxCellIterations.getText());
			mainFrame.simulation.setAlpha((int) alpha.getSelectedItem());
			mainFrame.simulation.setUpdateMode((String) updateMode.getSelectedItem());
			mainFrame.simulation.setOnlyUpdatable((String) onlyUpdatable.getSelectedItem());
			mainFrame.simulation.setOnlyUpdatable("Updatable cells");
			

			mainFrame.topology.setRollOver((String) rollOver.getSelectedItem());

			mainFrame.stochasticSimulation.setCompToShow(compToShowCombo.getSelectedItem().toString());

			mainFrame.simulation.setStochastic(true);
			for (int i =0;i<mainFrame.stochasticSimulation.getSimulationsNumber() ; i++){
				mainFrame.simulation.reset();
				mainFrame.simulation.runStochastic();
				System.out.println(i);
			}
			mainFrame.stochasticSimulation.getCumulativeGrid();
			mainFrame.hexagonsPanel.repaint();
			mainFrame.stochasticSimulation.paintCumulativeGrid();
			mainFrame.simulation.setStochastic(false);
			mainFrame.stochasticSimulation.printSolutions();
		}

		else{

			System.out.println((String) onlyUpdatable.getSelectedItem());
			System.out.println(mainFrame.stochasticSimulation.getOnlyUpdate());
			for (int beta = 70; beta<=100; beta++){
				for (int alpha = 0; alpha<=100; alpha++){
					
					mainFrame.simulation.reset();
					mainFrame.simulation.setStochastic(true);
					mainFrame.stochasticSimulation.reset();

				
				String updateModeAux = "";
				for (int j = 0; j<1; j++){

					if (j==0)
						updateModeAux = "Random Independent";
					else if (j==1)
						updateModeAux = "Random Order";
					else if (j==2)
						updateModeAux = "Exponential Clocked";
				
					mainFrame.topology.setBeta(beta);
					mainFrame.stochasticSimulation.setAlpha(alpha);
					mainFrame.simulation.setAlpha(alpha);
					
					
					mainFrame.stochasticSimulation.setMaxIteration(4);
					
					mainFrame.stochasticSimulation.setUpdateScheme(updateModeAux);				
					mainFrame.stochasticSimulation.setRollOver((String) rollOver.getSelectedItem());
					mainFrame.stochasticSimulation.setOnlyUpdate(mainFrame.stochasticSimulation.getOnlyUpdate());
					
			
//					mainFrame.stochasticSimulation.setSimulationsNumber(Integer.parseInt(maxSimulations.getText()));
					mainFrame.stochasticSimulation.setSimulationsNumber(4);
					
					mainFrame.topology.SetBlue(blue.isSelected());
					mainFrame.topology.SetGreen(green.isSelected());
					mainFrame.topology.SetRed(red.isSelected());
					mainFrame.topology.SetYellow(yellow.isSelected());
					mainFrame.topology.SetOrange(orange.isSelected());
					mainFrame.topology.SetPurple(purple.isSelected());

					mainFrame.simulation.setMaxCellIterationNumber("4");

					mainFrame.simulation.setUpdateMode(updateModeAux);
					mainFrame.simulation.setOnlyUpdatable(mainFrame.simulation.getOnlyUpdatable());

					mainFrame.topology.setRollOver((String) rollOver.getSelectedItem());
					Date date = new Date();
					System.out.println(date.toString());
//					System.out.println(alpha+" "+ beta);
//					System.out.println(mainFrame.stochasticSimulation.getName());
					if (!mainFrame.stochasticSimulation.fileExists(mainFrame.stochasticSimulation.getName())){
						System.out.println(mainFrame.stochasticSimulation.getName());
						
					for (int i =0;i<mainFrame.stochasticSimulation.getSimulationsNumber() ; i++){
						mainFrame.simulation.reset();
						mainFrame.simulation.runStochastic();
					
					}}}
					mainFrame.stochasticSimulation.printSolutions();

				}
			}
				mainFrame.simulation.setStochastic(false);
				//			mainFrame.stochasticSimulation.setCompToShow(compToShowCombo.getSelectedItem().toString());
				//			mainFrame.stochasticSimulation.getCumulativeGrid();
				//			mainFrame.hexagonsPanel.repaint();
				//			mainFrame.stochasticSimulation.paintCumulativeGrid();
		}
			}





			protected void fireRollOverChange(String optionString) {
				mainFrame.simulation.setUpdateMode(optionString);
			}

			protected void fireUpdateModeChange(String optionString) {
				mainFrame.simulation.setUpdateMode(optionString);
			}

			protected void fireOnlyUpdatableChange(String optionString) {
				mainFrame.simulation.setOnlyUpdatable(optionString);
				mainFrame.stochasticSimulation.setOnlyUpdate(optionString);
//				System.out.println(optionString);
//				System.out.println(mainFrame.simulation.getOnlyUpdatable());
//				System.out.println(mainFrame.stochasticSimulation.getOnlyUpdate());
				
			}

			protected void fireOnlyUpdatableChangeSpatial(String optionString) {
				mainFrame.topology.setOnlyUpdatable(optionString);
				
			}

			protected void fireUpdateModeChangeSpatial(String optionString) {
				mainFrame.topology.setUpdateMode(optionString);
			}


		}


