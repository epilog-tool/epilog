package pt.igc.nmd.epilog.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import pt.igc.nmd.epilog.RunStopButton;
import pt.igc.nmd.epilog.Topology;

public class SimulationSetupPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6167613300012277711L;

	public MainFrame mainPanel;
	private JComboBox rollOver;

	private JButton runButton;
	private JButton stepButton;
	private JButton simulationButton;
	private JButton restartButton;

	public SimulationSetupPanel(MainFrame mainPanel) {
		this.mainPanel = mainPanel;
		init();

	}

	public JPanel init() {

		FlowLayout layout = new FlowLayout();
		layout.setAlignment(FlowLayout.LEFT);
		setLayout(new FlowLayout());

		/*
		 * RollOver: This will allow the user to define if there is any
		 * neighborhood relation between hexagons at the extremes (horizontal
		 * and vertical). Note that there is no vertical and horizontal roll
		 * over, due to the Euler Theory. At least one pentagon had to be
		 * presented. By default, there is no roll-over selected
		 */

		rollOver = new JComboBox();

		rollOver.addItem("No Roll-Over");
		rollOver.addItem("Vertical Roll-Over");
		rollOver.addItem("Horizontal Roll-Over");
		String aux = (String) rollOver.getSelectedItem();
		if (mainPanel.getEpithelium().getUnitaryModel() != null)
			mainPanel.getTopology().setRollOver(aux);

		rollOver.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				JComboBox source = (JComboBox) event.getSource();
				String optionString = (String) source.getSelectedItem();
				fireRollOverChange(optionString);
				mainPanel.setInitialSetupHasChanged(true);

			}
		});

		/*
		 * Option to create a composed Model
		 */

		JCheckBox createComposedModel = new JCheckBox("Create composed model");

		add(rollOver);
		add(createComposedModel);

		runButton = new RunStopButton();
		stepButton = new JButton("Step");
		simulationButton = new JButton("Simulation");

		/*
		 * Step Button: Initiates a step-by-step simulation. It also repaints
		 * the border of the hexagons panel with the iteration number and
		 * disables the step and run button when it is over
		 */
		stepButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// if (mainPanel.getSimulation().isAutomata())
				// mainPanel.getSimulation().automataStep();
				// else
				mainPanel.getSimulation().step();

				if (mainPanel.getSimulation().hasStableStateFound()) {
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
				mainPanel.getSimulation().run();
				stepButton.setEnabled(false);
				runButton.setEnabled(false);
			}
		});

		add(runButton);
		add(stepButton);
		
		restartButton = new JButton("Restart");

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
				mainPanel.getSimulation().setHasInitiated(false);

				TitledBorder titleInitialConditions;
				titleInitialConditions = BorderFactory.createTitledBorder("");
				mainPanel.auxiliaryHexagonsPanel
						.setBorder(javax.swing.BorderFactory
								.createEmptyBorder());
				mainPanel.auxiliaryHexagonsPanel
						.setBorder(titleInitialConditions);

				removeAll();
				init();

				mainPanel.getSimulation().initializeSimulation();
				//resetAllPanels();

			}
		});

		// simulationButton.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent e) {
		// mainPanel.getSimulation().initializeSimulation();
		// // mainPanel.getSimulation().setAutomata(false);
		// removeAll();
		// init();
		// mainPanel.auxiliaryHexagonsPanel
		// .setBorder(javax.swing.BorderFactory
		// .createEmptyBorder());
		// TitledBorder titleInitialConditions;
		// titleInitialConditions = BorderFactory
		// .createTitledBorder("Initial Conditions");
		// mainPanel.auxiliaryHexagonsPanel
		// .setBorder(titleInitialConditions);
		//
		// }
		// });

		return this;
	}

	private void fireRollOverChange(String optionString) { // ROLL OVER
		mainPanel.getTopology().setRollOver(optionString);
	}
}
