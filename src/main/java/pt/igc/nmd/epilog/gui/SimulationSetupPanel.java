package pt.igc.nmd.epilog.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import org.colomoto.logicalmodel.NodeInfo;
import org.colomoto.logicalmodel.perturbation.AbstractPerturbation;

import pt.igc.nmd.epilog.Grid;

public class SimulationSetupPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6167613300012277711L;

	public MainFrame mainFrame;
	public JComboBox rollOver;
	public JCheckBox createComposedModel;

	private JButton runButton;
	private JButton stepButton;
	private JButton restartButton;
	private JPanel centerPanel;

	// private boolean test = false;

	public JComboBox initialCombo;
	public JComboBox inputCombo;
	public JComboBox perturbationsCombo;
	public JComboBox prioritiesCombo;

	JPanel lineStartPanel = new JPanel();

	public SimulationSetupPanel(MainFrame mainPanel) {
		this.mainFrame = mainPanel;
		init();

	}

	/**
	 * Initializes the simulation setup panel, to be inserted in the tab on
	 * Epilog's main panel.
	 * 
	 * @return this panel
	 * 
	 */
	public JPanel init() {

		FlowLayout layout = new FlowLayout();
		layout.setAlignment(FlowLayout.LEFT);
		setLayout(new BorderLayout());


		// CENTER

		centerPanel = mainFrame.componentsPanel.init();
		add(centerPanel, BorderLayout.CENTER);
		
		// PAGE START PANEL

		JPanel startPanel = new JPanel(layout);

		rollOver = new JComboBox();

		rollOver.addItem("No Roll-Over");
		if (mainFrame.topology.getHeight() % 2 == 0)
			rollOver.addItem("Vertical Roll-Over");
		if (mainFrame.topology.getWidth() % 2 == 0)
			rollOver.addItem("Horizontal Roll-Over");
		if (mainFrame.topology.getHeight() % 2 != 0
				& mainFrame.topology.getWidth() % 2 != 0)
			rollOver.setEnabled(false);
		String aux = (String) rollOver.getSelectedItem();
		if (mainFrame.getEpithelium().getUnitaryModel() != null)
			mainFrame.topology.setRollOver(aux);

		rollOver.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				JComboBox source = (JComboBox) event.getSource();
				String optionString = (String) source.getSelectedItem();
				fireRollOverChange(optionString);
				mainFrame.setInitialSetupHasChanged(true);

			}
		});

		/*
		 * Option to create a composed Model
		 */

		createComposedModel = new JCheckBox("Create composed model");

		createComposedModel.setSelected(mainFrame.needsComposedModel);
		createComposedModel.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				JCheckBox src = (JCheckBox) event.getSource();
				if (src.isSelected()) {
					mainFrame.needsComposedModel = true;
				} else {
					mainFrame.needsComposedModel = false;
				}
				mainFrame.hexagonsPanel.paintComponent(mainFrame.hexagonsPanel
						.getGraphics());

			}

		});

		startPanel.add(rollOver);
		startPanel.add(createComposedModel);

		runButton = new JButton("Run");
		stepButton = new JButton("Step");

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
				mainFrame.simulation.run();
				if (mainFrame.simulation.hasStableStateFound()) {
					stepButton.setEnabled(false);
					runButton.setEnabled(false);
				}
			}
		});

		startPanel.add(runButton);
		startPanel.add(stepButton);

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
//				removeAll();
//				repaint();
//				revalidate();
//				init();
			}
		});
		startPanel.add(restartButton);
		add(startPanel, BorderLayout.PAGE_START);

		// LINE START

		JPanel northLineStartPanel = new JPanel(new BorderLayout());
		JPanel centerLineStartPanel = new JPanel();
		JPanel southLineStartPanel = new JPanel();

		JPanel[] auxiliary = new JPanel[4];

		// LineStartPanel.setLayout(new BoxLayout(LineStartPanel,
		// BoxLayout.PAGE_AXIS));
		lineStartPanel.setLayout(new BorderLayout());

		initialCombo = new JComboBox();
		inputCombo = new JComboBox();
		perturbationsCombo = new JComboBox();
		prioritiesCombo = new JComboBox();

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
				JComboBox src = (JComboBox) arg0.getSource();
				mainFrame.epithelium.setSelectedPriority((String) src
						.getSelectedItem());
				String string = "priorities";
				//needToResetComposedModel(string, (String) src.getSelectedItem());
			}
		});

		initialCombo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JComboBox src = (JComboBox) arg0.getSource();
				mainFrame.epithelium.setSelectedInitialSet((String) src
						.getSelectedItem());
				mainFrame.hexagonsPanel.repaint();
				mainFrame.fillHexagons();
			}
		});

		inputCombo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JComboBox src = (JComboBox) arg0.getSource();
				mainFrame.epithelium.setSelectedInputSet((String) src
						.getSelectedItem());
				String string = "input";
				needToResetComposedModel(string, (String) src.getSelectedItem());
				repaintSimulationSetupPanel();

			}
		});

		perturbationsCombo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				JComboBox src = (JComboBox) arg0.getSource();
				mainFrame.epithelium.setSelectedPerturbation((String) src
						.getSelectedItem());
				String string = "perturbation";
				needToResetComposedModel(string, (String) src.getSelectedItem());
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

		northLineStartPanel.add(auxiliary[0], BorderLayout.PAGE_START);
		northLineStartPanel.add(auxiliary[2], BorderLayout.LINE_START);
		northLineStartPanel.add(auxiliary[3], BorderLayout.PAGE_END);

		centerLineStartPanel.add(auxiliary[1]);

		LineBorder border = new LineBorder(Color.black, 1, true);

		TitledBorder north = new TitledBorder(border, "Definitions",
				TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION, new Font(
						"Arial", Font.ITALIC, 14), Color.black);

		TitledBorder center = new TitledBorder(border, "Epithelium",
				TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION, new Font(
						"Arial", Font.ITALIC, 14), Color.black);


		
		TitledBorder south = new TitledBorder(border, "Analytics @ instance: "
				+ ("(" + mainFrame.topology.instance2j(0)+","+ mainFrame.topology.instance2i(0) + ")"), TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION,
				new Font("Arial", Font.ITALIC, 14), Color.black);
		
		String string = ("<html>");
		for (NodeInfo node : mainFrame.epithelium.getUnitaryModel()
				.getNodeOrder()) {
			if (!mainFrame.epithelium.isIntegrationComponent(node)) {

				string = string
						+ ("<br>" + "node: " + node + " -> value: " + mainFrame.epithelium
								.getGridValue(0,
										node));

			}
		}
		
		string = string + ("</html>");
		JLabel f = new JLabel(string);
		southLineStartPanel.add(f);
		
		JScrollPane nw = new JScrollPane(southLineStartPanel);
		//nw.add(southLineStartPanel);

		northLineStartPanel.setBorder(north);
		centerLineStartPanel.setBorder(center);
		nw.setBorder(south);

		lineStartPanel.setBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED));

		lineStartPanel.add(northLineStartPanel, BorderLayout.PAGE_START);
		lineStartPanel.add(centerLineStartPanel, BorderLayout.LINE_START);
		lineStartPanel.add(nw, BorderLayout.PAGE_END);

		add(lineStartPanel, BorderLayout.LINE_START);
		

		
		return this;
	}
	
	
	
	private void repaintSimulationSetupPanel(){
		centerPanel.removeAll();
		centerPanel.repaint();
		centerPanel.revalidate();
		this.remove(0);
		JPanel aux = new JPanel();
		aux = mainFrame.componentsPanel.init();
		add(aux, 0);
		
		
	}
	

	/**
	 * Forces the creation of a new composed model, if any of the simulation
	 * settings changed.
	 * 
	 * @param string
	 *            string related with the type of modification that will force
	 *            the creation of a new composed model
	 * @param selected
	 *            name of the new set of the modification that forced the reset
	 *            of the composed model
	 * 
	 */
	private void needToResetComposedModel(String string, String selected) {
		if (string == "perturbation") {
			if (selected == mainFrame.previsioulySelectedPeturbationSet)
				mainFrame.resetComposedModel = false;
			else
				mainFrame.resetComposedModel = true;
			mainFrame.previsioulySelectedPeturbationSet = selected;
		} else if (string == "input") {
			if (selected == mainFrame.previsioulySelectedInputSet)
				mainFrame.resetComposedModel = false;
			else
				mainFrame.resetComposedModel = true;
			mainFrame.previsioulySelectedInputSet = selected;
		} 
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
	private void fillCombo(JComboBox combo, String string) {

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
//			combo.addItem("none");
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
		createComposedModel.setEnabled(false);
		rollOver.setEnabled(false);
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
		createComposedModel.setEnabled(true);
		rollOver.setEnabled(true);
	}

}