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
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;
import org.colomoto.logicalmodel.io.sbml.SBMLFormat;

public class StartPanel extends JPanel {

	/**
*
*/

	private static final long serialVersionUID = 1L;
	private JButton closeButton;
	private JButton loadModelButton;
	private JButton setupConditionsButton;
	private JButton restartButton;
	private JButton runButton;
	private JButton stepButton;
	private JButton quitButton;
	private JButton simulationButton;
	private JButton loadConfigurations;

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

		loadModelButton = new JButton("Load Model");
		selectedFilenameLabel = new JLabel();
		restartButton = new JButton("Restart");
		closeButton = new JButton("Close");
		quitButton = new JButton("Quit");
		simulationButton = new JButton("Simulation");
		setupConditionsButton = new JButton("Setup Conditions");
		runButton = new RunStopButton();
		stepButton = new JButton("Step");
		loadConfigurations = new JButton("Load Configurations");

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

		loadModelButton.setBounds(230, 13, 100, 30);
		selectedFilenameLabel.setBounds(335, 13, 100, 30);

		quitButton.setBackground(Color.red);

		closeButton.setVisible(false);
		stepButton.setVisible(false);
		runButton.setVisible(false);
		iterationNumber.setVisible(false);
		labelFilename.setVisible(false);
		setupConditionsButton.setVisible(false);
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
		 * invisible 5) Grids Dimension and the Initial Conditions button is
		 * enabled 6) The analytics model is restarted 7) The hexagons Panel is
		 * repainted 8) The Composed logical model and its variables are
		 * initialized
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
				setupConditionsButton.setVisible(false);
				simulationButton.setVisible(false);
				restartButton.setVisible(false);
				closeButton.setVisible(false);
				// labelFilename.setVisible(false);

				setupConditionsButton.setEnabled(true);
				userDefinedWidth.setEnabled(true);
				userDefinedHeight.setEnabled(true);

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
				integrationComponentsReset();
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

		restartButton.setVisible(false);
		restartButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				mainPanel.getSimulation().resetIterationNumber();
				mainPanel.setBorderHexagonsPanel();

				simulationButton.setVisible(true);
				stepButton.setVisible(false);
				runButton.setVisible(false);
				stepButton.setEnabled(true);
				runButton.setEnabled(true);
				setupConditionsButton.setEnabled(true);
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
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				sanityCheckDimension(userDefinedWidth);
				setWidth();
				mainPanel.setInitialSetupHasChanged(true);
				if (mainPanel.getEpithelium().getUnitaryModel() != null)
					mainPanel.getGrid().initializeGrid();
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
				if (mainPanel.getEpithelium().getUnitaryModel() != null)
					mainPanel.getGrid().initializeGrid();
			}
		});

		addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				sanityCheckDimension(userDefinedWidth);
				sanityCheckDimension(userDefinedHeight);
				setWidth();
				setHeight();
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {

				sanityCheckDimension(userDefinedWidth);
				sanityCheckDimension(userDefinedHeight);
				setWidth();
				setHeight();
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

		loadModelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				askModel();
				mainPanel.getContentPane().repaint();
				mainPanel.getSimulation().resetIterationNumber();

			}

		});

		loadConfigurations.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				askConfigurations();
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
				setupConditionsButton.setEnabled(false);
				userDefinedWidth.setEnabled(false);
				userDefinedHeight.setEnabled(false);

			}
		});

		/*
		 * Step Button: Initiates a step-by-step simulation. It also repaints
		 * the border of the hexagons panel with the iteration number and
		 * disables the step and run button when it is over
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
		setupConditionsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final SetupConditions initialConditionsPanel = new SetupConditions(
						epithelium, topology, mainPanel);
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
		add(loadConfigurations);
		add(loadModelButton);
		add(labelFilename);
		add(selectedFilenameLabel);
		add(runButton);
		add(stepButton);
		add(simulationButton);
		add(setupConditionsButton);
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

	private void integrationComponentsReset() {
		for (NodeInfo node : mainPanel.getEpithelium().getUnitaryModel()
				.getNodeOrder()) {
			if (node.isInput())
				mainPanel.getEpithelium().setIntegrationComponents(node, false);
		}
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
			selectedFilenameLabel.setForeground(Color.white);
			loadModel();
		}
	}

	private void askConfigurations() {

		SphericalEpithelium epithelium = new SphericalEpithelium(topology);

		fc.setDialogTitle("Choose file");

		if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			// selectedFilenameLabel.setText(fc.getSelectedFile().getName());
			// selectedFilenameLabel.setForeground(Color.white);

			try {
//				ObjectOutputStream oos = new ObjectOutputStream(
//						new FileOutputStream(fc.getSelectedFile()
//								.getAbsolutePath(), true));
				ObjectInputStream ois = new ObjectInputStream(
						new FileInputStream(fc.getSelectedFile()
								.getAbsolutePath()));

				epithelium = (SphericalEpithelium) ois.readObject();

			} catch (FileNotFoundException e) {

			} catch (IOException e) {

			} catch (ClassNotFoundException e) {

			}

		}

		loadConfigurations();
	}

	private void loadModel() {

		File file = fc.getSelectedFile();
		SBMLFormat sbmlFormat = new SBMLFormat();
		LogicalModel logicalModel = null;

		try {
			logicalModel = sbmlFormat.importFile(file);
			
//			FileOutputStream fos = new FileOutputStream("foo.dat");
//			ObjectOutputStream oos = new ObjectOutputStream(fos);
//			oos.writeObject(mainPanel.getEpithelium());
		
			
		} catch (IOException e) {
			System.err.println("Cannot import file " + file.getAbsolutePath()
					+ ": " + e.getMessage());
		}

		if (logicalModel == null)
			return;

		mainPanel.getEpithelium().setUnitaryModel(logicalModel);
		integrationComponentsReset();
		mainPanel.getEpithelium().initializeColors();
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
		setupConditionsButton.setVisible(true);
		restartButton.setVisible(true);
		closeButton.setVisible(true);
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
		setupConditionsButton.setEnabled(true);
		mainPanel.auxiliaryHexagonsPanel.setBorder(javax.swing.BorderFactory
				.createEmptyBorder());
		TitledBorder titleInitialConditions;
		titleInitialConditions = BorderFactory.createTitledBorder("");
		mainPanel.auxiliaryHexagonsPanel.setBorder(titleInitialConditions);

	}

	private void loadConfigurations() {


		System.out.println("Unitary Model @loadConfigurations" + epithelium.getUnitaryModel());
		System.out.println("Height @loadConfigurations" + epithelium.getTopology().getHeight());
		
//		FileInputStream fis = null;
//		try {
//			fis = new FileInputStream("foo.bin");
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		  ObjectInputStream ois = null;
//		try {
//			ois = new ObjectInputStream(fis);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		  try {
//			SphericalEpithelium aFoo = (SphericalEpithelium) ois.readObject();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		mainPanel.getEpithelium().initializeColors();
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
		setupConditionsButton.setVisible(true);
		restartButton.setVisible(true);
		closeButton.setVisible(true);
		stepButton.setEnabled(true);
		runButton.setEnabled(true);
		userDefinedWidth.setEnabled(true);
		userDefinedHeight.setEnabled(true);

		mainPanel.watcherPanel.init();
		mainPanel.hexagonsPanel.paintComponent(mainPanel.hexagonsPanel
				.getGraphics());
		setupConditionsButton.setEnabled(true);
		mainPanel.auxiliaryHexagonsPanel.setBorder(javax.swing.BorderFactory
				.createEmptyBorder());
		TitledBorder titleInitialConditions;
		titleInitialConditions = BorderFactory.createTitledBorder("");
		mainPanel.auxiliaryHexagonsPanel.setBorder(titleInitialConditions);

	}

}
