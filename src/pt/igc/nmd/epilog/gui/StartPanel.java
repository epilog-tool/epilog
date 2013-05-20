package pt.igc.nmd.epilog.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;
import org.colomoto.logicalmodel.io.sbml.SBMLFormat;

import pt.igc.nmd.epilog.RunStopButton;
//import pt.igc.nmd.epilog.SetupDefinitions;
import pt.igc.nmd.epilog.SphericalEpithelium;
import pt.igc.nmd.epilog.Topology;
import pt.igc.nmd.epilog.UnZip;

public class StartPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2013094563809493130L;

	private JButton closeButton;
	private JButton newEpithelium;
	//private JButton setupDefinitionsButton;
	private JButton restartButton;
	private JButton runButton;
	private JButton stepButton;
	private JButton quitButton;
	private JButton simulationButton;
	private JButton loadEpithelium;
	private JComboBox rollOver;

	private JLabel labelFilename = new JLabel();
	private JLabel iterationNumber = new JLabel();
	private JLabel selectedFilenameLabel;

	private static JTextField userDefinedWidth = new JTextField();
	private static JTextField userDefinedHeight = new JTextField();

	private JFileChooser fc = new JFileChooser();
	private MainPanel mainPanel = null;
	private SphericalEpithelium epithelium;
	private Topology topology;

	public StartPanel(MainPanel mainPanel) {
		this.mainPanel = mainPanel;
		init();
		setWidth();
		setHeight();
		this.topology = mainPanel.getTopology();
		this.epithelium = mainPanel.getEpithelium();

	}

	private JPanel init() {

		FlowLayout layout = new FlowLayout();
		layout.setAlignment(FlowLayout.LEFT);
		setLayout(new FlowLayout());

		/*
		 * Components definitions
		 */

		newEpithelium = new JButton("New Epihtelium");
		selectedFilenameLabel = new JLabel();
		restartButton = new JButton("Restart");
		closeButton = new JButton("Close");
		quitButton = new JButton("Quit");
		simulationButton = new JButton("Simulation");
		//setupDefinitionsButton = new JButton("Setup Definitions");
		runButton = new RunStopButton();
		stepButton = new JButton("Step");
		loadEpithelium = new JButton("Load Epithelium");

		JLabel setWidth = new JLabel();
		JLabel setHeight = new JLabel();
		JLabel emptySpaceLabel = new JLabel();

		emptySpaceLabel.setText("    ");

		setWidth.setText("Width: ");
		setHeight.setText("Height: ");

		setWidth.setForeground(Color.white);
		setHeight.setForeground(Color.white);
		labelFilename.setText("Filename: ");
		labelFilename.setForeground(Color.white);
		iterationNumber.setText(""
				+ mainPanel.getSimulation().getIterationNumber());

		newEpithelium.setBounds(230, 13, 100, 30);
		selectedFilenameLabel.setBounds(335, 13, 100, 30);

		quitButton.setBackground(Color.red);

		/*
		 * Close Button: This button will close the current simulation and erase
		 * the initial conditions. It is assumed that the simulation has changed
		 * so that at the simulation a new composed model is created. The
		 * following are also modified: 1) the border is removed 2) The
		 * simulation will be reset to zero 3) The model name is changed to an
		 * empty field 4) The components panel, watcher panel, step button, run
		 * button, simulation button, restart button and close button are set
		 * invisible 5) Grids Dimension and the Initial Conditions button is
		 * enabled 6) The analytics model is restarted 7) The hexagons Panel is
		 * repainted 8) The Composed logical model and its variables are
		 * initialized
		 */

		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				mainPanel.setInitialSetupHasChanged(true);
				mainPanel.getSimulation().setHasInitiated(false);
				mainPanel.getEpithelium().reset();
				resetAllPanels();

				selectedFilenameLabel.setText("");

				TitledBorder titleInitialConditions;
				titleInitialConditions = BorderFactory.createTitledBorder("");
				mainPanel.auxiliaryHexagonsPanel
						.setBorder(javax.swing.BorderFactory
								.createEmptyBorder());
				mainPanel.auxiliaryHexagonsPanel
						.setBorder(titleInitialConditions);

				mainPanel.hexagonsPanel.paintComponent(mainPanel.hexagonsPanel
						.getGraphics());

				mainPanel.getLogicalModelComposition()
						.resetLogicalModelComposition();

			}
		});

		/*
		 * Quit Button: This button closes the application. It also closes all
		 * opened windows.
		 */

		quitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainPanel.dispose();
				System.exit(0);
			}
		});

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
				resetAllPanels();

			}
		});

		/*
		 * Grid Dimension: The number of rows and columns have to be even, so
		 * even if the user writes an odd number, it is automatically corrected
		 * to the next even number. Topology will hold the values. Whenever the
		 * grid dimensions are changed the mainPanel is informed of that so that
		 * the composed model can be recreated. It is particularly important
		 * when the user has already defined a composed model and then restarts
		 * the simulation and decides to change the grid's dimensions. If the
		 * model was already loaded and the user change the grids dimensions
		 * after a restart, then the grid has to reinitialize again.
		 */

		userDefinedWidth.setPreferredSize(new Dimension(34, 26));
		userDefinedHeight.setPreferredSize(new Dimension(34, 26));

		userDefinedWidth.setHorizontalAlignment(JTextField.CENTER);
		userDefinedHeight.setHorizontalAlignment(JTextField.CENTER);
		userDefinedWidth.setText("" + mainPanel.getTopology().getWidth());
		userDefinedHeight.setText("" + mainPanel.getTopology().getHeight());

		userDefinedWidth.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) {
				if (mainPanel.getEpithelium().getUnitaryModel() != null)
					mainPanel.fillHexagons();
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				if (mainPanel.getEpithelium().getUnitaryModel() == null) {
					sanityCheckDimension(userDefinedWidth);
					sanityCheckDimension(userDefinedHeight);
					setWidth();
					setHeight();
				}
			}
		});

		userDefinedHeight.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) {
				if (mainPanel.getEpithelium().getUnitaryModel() != null)
					mainPanel.fillHexagons();
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				if (mainPanel.getEpithelium().getUnitaryModel() == null) {
					sanityCheckDimension(userDefinedWidth);
					sanityCheckDimension(userDefinedHeight);
					setWidth();
					setHeight();
				}

			}
		});

		addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (mainPanel.getEpithelium().getUnitaryModel() == null) {
					sanityCheckDimension(userDefinedWidth);
					sanityCheckDimension(userDefinedHeight);
					setWidth();
					setHeight();
				}
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {

				if (mainPanel.getEpithelium().getUnitaryModel() == null) {
					sanityCheckDimension(userDefinedWidth);
					sanityCheckDimension(userDefinedHeight);
					setWidth();
					setHeight();
				}
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {

			}
		});

		/*
		 * Model Loading: The model is loaded now from the SBML file. The
		 * function askmodel performs all the operations
		 */

		newEpithelium.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				askModel();
				mainPanel.getContentPane().repaint();
				mainPanel.getSimulation().resetIterationNumber();
				mainPanel.getEpithelium().setNewEpithelium(true);
			}
		});

		loadEpithelium.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				askConfigurations();
				mainPanel.getContentPane().repaint();
				mainPanel.getSimulation().resetIterationNumber();
				mainPanel.getEpithelium().setNewEpithelium(false);
			}
		});

		/*
		 * SimulationButton: This Button will start the simulation. If there was
		 * no setup of initial conditions then the option "no Roll-Over" and all
		 * inputs as environment inputs with zero as expression levels. The
		 * initial state is all zeros. After the Simulation is pressed the
		 * hexagons panel is loaded with the initial conditions expression
		 * values. The following are also changed: 1) the border is renamed to
		 * "Initial Conditions" 2) Simulation Button is set to invisible 3) Step
		 * and Run button are set as visible 4) Initial conditions button and
		 * grid's dimensions are disabled and the user no longer can modify them
		 */

		simulationButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainPanel.getSimulation().initializeSimulation();
				// mainPanel.getSimulation().setAutomata(false);
				removeAll();
				init();
				mainPanel.auxiliaryHexagonsPanel
						.setBorder(javax.swing.BorderFactory
								.createEmptyBorder());
				TitledBorder titleInitialConditions;
				titleInitialConditions = BorderFactory
						.createTitledBorder("Initial Conditions");
				mainPanel.auxiliaryHexagonsPanel
						.setBorder(titleInitialConditions);

			}
		});

		// automataButton.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent e) {
		// //mainPanel.getSimulation().initializeAutomata();
		// mainPanel.getSimulation().setAutomata(true);
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
			topology.setRollOver(aux);
		// rollOver.setBackground(Color.white);

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
		 * Initial Conditions Button:
		 */
//		setupDefinitionsButton.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				final SetupDefinitions initialConditionsPanel = new SetupDefinitions(
//						epithelium, topology, mainPanel);
//				initialConditionsPanel.initialize();
//			}
//		});

		/*
		 * Add Components to Panel
		 */

		if (mainPanel.getEpithelium().getUnitaryModel() != null) {
			userDefinedWidth.setEditable(false);
			userDefinedHeight.setEditable(false);
			userDefinedWidth.setEnabled(false);
			userDefinedHeight.setEnabled(false);

		}

		add(setWidth);
		add(userDefinedWidth);
		add(setHeight);
		add(userDefinedHeight);
		add(rollOver);
		add(loadEpithelium);
		add(newEpithelium);

		if (mainPanel.getEpithelium().getUnitaryModel() != null) {
			add(labelFilename);
			add(selectedFilenameLabel);

			if (mainPanel.getSimulation().getHasInitiated()) {
				add(runButton);
				add(stepButton);
//
//				setupDefinitionsButton.setEnabled(false);
				userDefinedWidth.setEnabled(false);
				userDefinedHeight.setEnabled(false);
			} else {
				add(simulationButton);
//				add(setupDefinitionsButton);
				userDefinedWidth.setEnabled(true);
				userDefinedHeight.setEnabled(true);
			}
			
			add(emptySpaceLabel);
			add(restartButton);
			add(closeButton);
		}
		add(quitButton);

		return this;
	}

	private void sanityCheckDimension(JTextField userDefined) {
		String dimString = userDefined.getText();
		int w = Integer.parseInt(dimString);
		w = (w % 2 == 0) ? w : w + 1;
		userDefined.setText("" + w);
		mainPanel.repaint();
		if (mainPanel.getEpithelium().getUnitaryModel() != null)
			mainPanel.fillHexagons();
	}

	private void setHeight() {
		mainPanel.getTopology().setHeight(
				Integer.parseInt(userDefinedHeight.getText()));
		mainPanel.setInitialSetupHasChanged(true);
	}

	private void setWidth() {

		mainPanel.getTopology().setWidth(
				Integer.parseInt(userDefinedWidth.getText()));
		mainPanel.setInitialSetupHasChanged(true);
	}

	private void askModel() {

		fc.setDialogTitle("Choose file");

		if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			selectedFilenameLabel.setText(fc.getSelectedFile().getName());
			selectedFilenameLabel.setForeground(Color.white);

			File file = fc.getSelectedFile();

			epithelium.setSBMLFilename(file.getName());
			epithelium.setSBMLPath(file.getAbsolutePath());

			loadModel(file);
		}
	}

	private void resetAllPanels() {
		removeAll();
		init();
		mainPanel.componentsPanel.removeAll();
		mainPanel.componentsPanel.init();
		mainPanel.watcherPanel.removeAll();
		mainPanel.watcherPanel.init();
		mainPanel.inputsPanel.removeAll();
		mainPanel.inputsPanel.init();
		mainPanel.initial.removeAll();
		mainPanel.initial.init();
		mainPanel.getContentPane().repaint();
	}

	private void loadModel(File file) {

		SBMLFormat sbmlFormat = new SBMLFormat();
		LogicalModel logicalModel = null;

		try {
			logicalModel = sbmlFormat.importFile(file);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			sbmlFormat.export(logicalModel, baos);

			String mySBML = new String(baos.toByteArray());

		} catch (IOException e) {
			System.err.println("Cannot import file " + file.getAbsolutePath()
					+ ": " + e.getMessage());
		}

		if (logicalModel == null)
			return;

		epithelium.setUnitaryModel(logicalModel);
		resetAllPanels();

		mainPanel.hexagonsPanel.paintComponent(mainPanel.hexagonsPanel
				.getGraphics());

		mainPanel.getLogicalModelComposition().resetComposedModel();

		//setupDefinitionsButton.setEnabled(true);
		mainPanel.auxiliaryHexagonsPanel.setBorder(javax.swing.BorderFactory
				.createEmptyBorder());
		TitledBorder titleInitialConditions;
		titleInitialConditions = BorderFactory.createTitledBorder("");
		mainPanel.auxiliaryHexagonsPanel.setBorder(titleInitialConditions);

	}

	private void askConfigurations() {

		fc.setDialogTitle("Choose file");

		if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			File folder = new File("temp");
			for (File file : folder.listFiles())
				file.delete();
			UnZip.main(fc.getSelectedFile().getAbsolutePath());

			for (final File fileEntry : folder.listFiles()) {
				if (fileEntry.getName().contains("sbml")) {
					loadModel(fileEntry);
					mainPanel.getEpithelium().setSBMLLoadPath(
							fileEntry.getAbsolutePath());
				}
			}
			for (final File fileEntry : folder.listFiles()) {
				if (fileEntry.getName().contains("config")) {
					try {
						Scanner fileIn = new Scanner(new File(
								fileEntry.getAbsolutePath()));
						ld(fileIn);

					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					loadConfigurations();
				}
			}
		}
	}

	private void ld(Scanner fileIn) {

		// TODO: Mudar para Buffereader: Lê muitas coisas de cada vez, lida
		// tambem com caracteres especiais

		while (fileIn.hasNext()) {

			String line = fileIn.nextLine();

			String identifier = line.split(" ")[0];

			if (identifier.contains("SN")) {

				String SBMLFilename = line.split(" ")[1];
				mainPanel.getEpithelium().setSBMLFilename(SBMLFilename);
			}

			if (identifier.contains("GD")) {
				mainPanel.getTopology().setWidth(
						Integer.parseInt(line.split(" ")[1].split(",")[0]));
				mainPanel.getTopology().setHeight(
						Integer.parseInt(line.split(" ")[1].split(",")[1]));
				removeAll();
				init();

			}
			if (identifier.contains("RL")) {
				mainPanel.getTopology().setRollOver(
						line.split(" ")[1].split(",")[0]);
			}
			if (identifier.contains("IC")) {
				NodeInfo node = mainPanel.getEpithelium().getUnitaryModel()
						.getNodeOrder()
						.get(Integer.parseInt(line.split(" ")[2]));
				byte value = (byte) Integer.parseInt(line.split(" ")[4]);

				for (String range : line.split(" ")[6].split(",")) {
					range.replace("(", "");
					range.replace(")", "");

					if (range.contains("-")) {
						range.replace("(", "");

						int init = Integer.parseInt(range.split("-")[0]);
						int end = Integer.parseInt(range.split("-")[1]);
						for (int instance = init; instance < end + 1; instance++) {
							mainPanel.getEpithelium().setGrid(instance, node,
									value);
						}
					}
				}
			}

			if (identifier.contains("IT")) {
				NodeInfo node = mainPanel.getEpithelium().getUnitaryModel()
						.getNodeOrder()
						.get(Integer.parseInt(line.split(" ")[2]));
				byte value = (byte) Integer.parseInt(line.split(" ")[4]);
				String expression = line.split(" ")[6];
				mainPanel.getEpithelium().setIntegrationFunctions(node, value,
						expression);
			}

			if (identifier.contains("CL")) {

				NodeInfo node = mainPanel.getEpithelium().getUnitaryModel()
						.getNodeOrder()
						.get(Integer.parseInt(line.split(" ")[2]));
				Color color = new Color(Integer.parseInt(line.split(" ")[4]));

				mainPanel.getEpithelium().setColor(node, color);
			}
		}
	}

	private void fireRollOverChange(String optionString) {
		topology.setRollOver(optionString);
	}

	private void loadConfigurations() {

		mainPanel.setEpithelium(epithelium);
		resetAllPanels();

		mainPanel.hexagonsPanel.paintComponent(mainPanel.hexagonsPanel
				.getGraphics());
		//setupDefinitionsButton.setEnabled(true);
		mainPanel.auxiliaryHexagonsPanel.setBorder(javax.swing.BorderFactory
				.createEmptyBorder());
		TitledBorder titleInitialConditions;
		titleInitialConditions = BorderFactory.createTitledBorder("");
		mainPanel.auxiliaryHexagonsPanel.setBorder(titleInitialConditions);
	}
}
