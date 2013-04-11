package pt.igc.nmd.epilogue;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.io.sbml.SBMLFormat;

public class StartPanel extends JPanel {

	/**
*
*/

	private static final long serialVersionUID = 1L;
	private JButton closeButton;
	private JButton modelButton;
	private JButton initialConditionsButton;
	private JButton restartButton;
	private JButton runButton;
	private JButton stepButton;
	private JButton quitButton;
	private JButton simulationButton;

	private JLabel labelFilename = new JLabel();
	private JLabel iterationNumber = new JLabel();

	private static JTextField userDefinedWidth = new JTextField();
	private static JTextField userDefinedHeight = new JTextField();
	private JLabel selectedFilenameLabel;
	private JFileChooser fc = new JFileChooser();
	private LogicalModel model;

	// private ComponentsPanel componentsPanel = new ComponentsPanel();
	private MainPanel mainPanel = null;

	public StartPanel(MainPanel mainPanel) {
		this.mainPanel = mainPanel;
		init();
		setWidth();
		setHeight();

	}

	private JPanel init() {

		FlowLayout layout = new FlowLayout();
		layout.setAlignment(FlowLayout.LEFT);
		setLayout(new FlowLayout());

		/*
		 * Components definitions
		 */

		modelButton = new JButton("Model");
		selectedFilenameLabel = new JLabel();
		restartButton = new JButton("Restart");
		closeButton = new JButton("Close");
		quitButton = new JButton("Quit");
		simulationButton = new JButton("Simulation");
		initialConditionsButton = new JButton("Initial Conditions");
		runButton = new RunStopButton();
		stepButton = new JButton("Step");

		JLabel setWidth = new JLabel();
		JLabel setHeight = new JLabel();
		JLabel emptySpaceLabel = new JLabel();

		emptySpaceLabel.setText("    ");

		setWidth.setText("Width: ");
		setHeight.setText("Height: ");
		labelFilename.setText("Filename: ");
		iterationNumber.setText(""
				+ mainPanel.getSimulation().getIterationNumber());

		modelButton.setBounds(230, 13, 100, 30);
		selectedFilenameLabel.setBounds(335, 13, 100, 30);

		quitButton.setBackground(Color.red);

		closeButton.setVisible(false);
		stepButton.setVisible(false);
		runButton.setVisible(false);
		iterationNumber.setVisible(false);
		labelFilename.setVisible(false);
		initialConditionsButton.setVisible(false);
		simulationButton.setVisible(false);

		userDefinedWidth.setEnabled(true);
		userDefinedHeight.setEnabled(true);

		/*
		 * Close Button: This button will close the current simulation and erase
		 * the initial conditions. It is assumed that the simulation has changed
		 * so that at the simulation a new composed model is created. The
		 * following are also modified: 1) the border is removed 2) The
		 * simulation will be reset to zero 3) The model name is changed to an
		 * empty field 4) The components panel, watcher panel, step button, run
		 * button, simulation button, restart button and close button are set
		 * invisible 5) Initial Conditions button is enabled 6) The analytics
		 * model is restarted 7) The Hexagons Panel is repainted
		 */

		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainPanel.getSimulation().resetIterationNumber();

				mainPanel.setInitialSetupHasChanged(true);
				mainPanel.getSimulation().setHasInitiated(false);
				mainPanel.restartAnalytics();

				selectedFilenameLabel.setText("");

				mainPanel.componentsPanel.setVisible(false);
				mainPanel.watcherPanel.setVisible(false);
				stepButton.setVisible(false);
				runButton.setVisible(false);
				initialConditionsButton.setVisible(false);
				simulationButton.setVisible(false);
				restartButton.setVisible(false);
				closeButton.setVisible(false);
				// labelFilename.setVisible(false);

				initialConditionsButton.setEnabled(true);

				TitledBorder titleInitialConditions;
				titleInitialConditions = BorderFactory.createTitledBorder("");
				mainPanel.auxiliaryHexagonsPanel
						.setBorder(javax.swing.BorderFactory
								.createEmptyBorder());
				mainPanel.auxiliaryHexagonsPanel
						.setBorder(titleInitialConditions);

				mainPanel.hexagonsPanel.paintComponent(mainPanel.hexagonsPanel
						.getGraphics());

			}
		});

		/*
		 * Quit Button: This button closes the application. It also closes all opened windows.
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
		 * definitions, grid dimentions or roll-over options.
		 * 
		 * After the Restart the following happens: 1) Reset the iteration
		 * number 2) Title border resets to "Initial Conditions" 3) Simulation
		 * Button is visible again 4) Step and Run buttons are enabled and
		 * invisible 5) Grid Dimensions are enabled 6) Analytics Board is
		 * restarted 7) Initial Conditions values are reloaded
		 */

		restartButton.setVisible(false);
		restartButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				mainPanel.getSimulation().resetIterationNumber();

				TitledBorder titleInitialConditions;
				titleInitialConditions = BorderFactory
						.createTitledBorder("Initial Conditions");
				mainPanel.auxiliaryHexagonsPanel
						.setBorder(javax.swing.BorderFactory
								.createEmptyBorder());
				mainPanel.auxiliaryHexagonsPanel
						.setBorder(titleInitialConditions);

				simulationButton.setVisible(true);
				stepButton.setVisible(false);
				runButton.setVisible(false);
				stepButton.setEnabled(true);
				runButton.setEnabled(true);
				initialConditionsButton.setEnabled(true);
				userDefinedWidth.setEnabled(true);
				userDefinedHeight.setEnabled(true);

				mainPanel.getSimulation().initializeSimulation();
				mainPanel.restartAnalytics();

			}
		});

		/*
		 * Grid Dimension: The number of rows and columns have to be even, so
		 * even if the user writes an odd number, it is automatically corrected
		 * to the next even number. Topology will hold the values. Whenever the
		 * grid dimensions are changed the mainPanel is informed of that so that
		 * the composed model can be recreated. It is Particularly important
		 * when the user has already defined a composed model and then restarts
		 * the simulation and decides to change the grid's dimensions.
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
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				sanityCheckDimension(userDefinedWidth);
				setWidth();
				mainPanel.setInitialSetupHasChanged(true);

			}
		});

		userDefinedHeight.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) {
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				sanityCheckDimension(userDefinedHeight);
				setHeight();
				mainPanel.setInitialSetupHasChanged(true);
			}
		});

		addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				sanityCheckDimension(userDefinedWidth);
				sanityCheckDimension(userDefinedHeight);
				setWidth();
				setHeight();
				mainPanel.setInitialSetupHasChanged(true);
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				sanityCheckDimension(userDefinedWidth);
				sanityCheckDimension(userDefinedHeight);
				setWidth();
				setHeight();
				mainPanel.setInitialSetupHasChanged(true);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
			}
		});

		/*
		 * Model Loading: The model is loaded now from the SBML file. The
		 * function askmodel performs all the operations
		 */

		modelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				askModel();
				mainPanel.getContentPane().repaint();
				mainPanel.getSimulation().resetIterationNumber();

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

				mainPanel.auxiliaryHexagonsPanel
						.setBorder(javax.swing.BorderFactory
								.createEmptyBorder());
				TitledBorder titleInitialConditions;
				titleInitialConditions = BorderFactory
						.createTitledBorder("Initial Conditions");
				mainPanel.auxiliaryHexagonsPanel
						.setBorder(titleInitialConditions);

				simulationButton.setVisible(false);
				stepButton.setVisible(true);
				runButton.setVisible(true);
				mainPanel.getSimulation().initializeSimulation();
				initialConditionsButton.setEnabled(false);
				userDefinedWidth.setEnabled(false);
				userDefinedHeight.setEnabled(false);

			}
		});

		/*
		 * Step Button:
		 */
		stepButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
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
		 * Initial Conditions Button:
		 */
		
		initialConditionsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final InitialConditions initialConditionsPanel = new InitialConditions(
						mainPanel);
				initialConditionsPanel.initialize();
				initialConditionsPanel
						.initializeCells(initialConditionsPanel.cells);

			}
		});

		/*
		 * Add Components to Panel
		 */
		add(setWidth);
		add(userDefinedWidth);
		add(setHeight);
		add(userDefinedHeight);
		add(modelButton);
		add(labelFilename);
		add(selectedFilenameLabel);
		add(runButton);
		add(stepButton);
		add(simulationButton);
		add(initialConditionsButton);
		add(emptySpaceLabel);
		add(restartButton);
		add(closeButton);
		add(quitButton);
		return this;
	}

	private void sanityCheckDimension(JTextField userDefined) {
		String dimString = userDefined.getText();
		int w = Integer.parseInt(dimString);
		w = (w % 2 == 0) ? w : w + 1;
		userDefined.setText("" + w);
		mainPanel.repaint();
	}

	private void setHeight() {
		mainPanel.getTopology().setHeight(
				Integer.parseInt(userDefinedHeight.getText()));

	}

	private void setWidth() {

		mainPanel.getTopology().setWidth(
				Integer.parseInt(userDefinedWidth.getText()));

	}

	private void askModel() {

		fc.setDialogTitle("Choose file");

		if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			selectedFilenameLabel.setText(fc.getSelectedFile().getName());
			loadModel();

		}
	}

	private void loadModel() {

		File file = fc.getSelectedFile();
		SBMLFormat sbmlFormat = new SBMLFormat();
		LogicalModel logicalModel = null;

		try {
			logicalModel = sbmlFormat.importFile(file);
		} catch (IOException e) {
			System.err.println("Cannot import file " + file.getAbsolutePath()
					+ ": " + e.getMessage());
		}

		if (logicalModel == null)
			return;

		mainPanel.getEpithelium().setUnitaryModel(logicalModel);

		mainPanel.componentsPanel.removeAll();
		mainPanel.componentsPanel.init();
		mainPanel.getContentPane().repaint();
		mainPanel.componentsPanel.setVisible(true);
		mainPanel.watcherPanel.setVisible(true);
		labelFilename.setVisible(true);
		stepButton.setVisible(true);
		runButton.setVisible(false);
		stepButton.setVisible(false);
		simulationButton.setVisible(true);
		initialConditionsButton.setVisible(true);
		restartButton.setVisible(true);
		closeButton.setVisible(true);
		setUnitaryModel(logicalModel);
		stepButton.setEnabled(true);
		runButton.setEnabled(true);
		userDefinedWidth.setEnabled(true);
		userDefinedHeight.setEnabled(true);

		mainPanel.watcherPanel.init();

		mainPanel.getGrid().initializeGrid();
		mainPanel.getSimulation().fillHexagons();
		mainPanel.hexagonsPanel.paintComponent(mainPanel.hexagonsPanel
				.getGraphics());

		mainPanel.getLogicalModelComposition().resetComposedModel();
		mainPanel.getSimulation().setHasInitiated(false);
		initialConditionsButton.setEnabled(true);
		mainPanel.auxiliaryHexagonsPanel.setBorder(javax.swing.BorderFactory
				.createEmptyBorder());
		TitledBorder titleInitialConditions;
		titleInitialConditions = BorderFactory.createTitledBorder("");
		mainPanel.auxiliaryHexagonsPanel.setBorder(titleInitialConditions);

	}

	public void setUnitaryModel(LogicalModel chosenmodel) {
		model = chosenmodel;
	}

	public LogicalModel getUnitaryModel() {
		return this.model;
	}
}
