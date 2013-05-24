package pt.igc.nmd.epilog.gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;
import org.colomoto.logicalmodel.io.sbml.SBMLFormat;

import pt.igc.nmd.epilog.UnZip;

public class StartPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2013094563809493130L;

	private JButton newEpithelium;
	private JButton loadEpithelium;
	private JButton saveButton;
	private JButton quitButton;

	private JLabel iterationNumber = new JLabel();



	private JFileChooser fc = new JFileChooser();
	private MainFrame mainPanel = null;

	private String SBMLFilename = null;

	public StartPanel(MainFrame mainPanel) {
		this.mainPanel = mainPanel;
		init();
	}

	private JPanel init() {

		FlowLayout layout = new FlowLayout();
		layout.setAlignment(FlowLayout.LEFT);
		setLayout(layout);

		/*
		 * Components definitions
		 */

		loadEpithelium = new JButton("Load Epithelium");
		newEpithelium = new JButton("New Epihtelium");
		quitButton = new JButton("Quit");
		saveButton = new JButton("Save");

		iterationNumber.setText(""
				+ mainPanel.getSimulation().getIterationNumber());


		/*
		 * Quit Button: This button closes the application. It also closes all
		 * opened windows.
		 */

		quitButton.setBackground(Color.red);
		quitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainPanel.dispose();
				System.exit(0);
			}
		});

		/*
		 * Model Loading: The model is loaded now from the SBML file. The
		 * function askmodel performs all the operations
		 */

		newEpithelium.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createNewEpitheliumIn(true);

				mainPanel.getContentPane().remove(mainPanel.startPanel);
				mainPanel.setupMainFrame();

				// askModel();
				// mainPanel.getContentPane().repaint();
				// mainPanel.getSimulation().resetIterationNumber();
				// mainPanel.getEpithelium().setNewEpithelium(true);
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

//		if (mainPanel.getStart()) {
//			newEpitheliumPanel();
//		}
		
		add(newEpithelium);
		add(loadEpithelium);
		add(saveButton);
		add(quitButton);

		return this;
	}



	private void createNewEpitheliumIn(boolean b) {
		mainPanel.setStart(b);
	}

//	private void askModel() {
//
//		fc.setDialogTitle("Choose file");
//
//		if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
//			selectedFilenameLabel.setText(fc.getSelectedFile().getName());
//			selectedFilenameLabel.setForeground(Color.white);
//
//			File file = fc.getSelectedFile();
//
//			mainPanel.getEpithelium().setSBMLFilename(file.getName());
//			mainPanel.getEpithelium().setSBMLPath(file.getAbsolutePath());
//
//			loadModel(file);
//		}
//	}

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

		mainPanel.getEpithelium().setUnitaryModel(logicalModel);
		resetAllPanels();

		mainPanel.hexagonsPanel.paintComponent(mainPanel.hexagonsPanel
				.getGraphics());

		mainPanel.getLogicalModelComposition().resetComposedModel();

		// setupDefinitionsButton.setEnabled(true);
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

//	private void newEpitheliumPanel() {
//		/*
//		 * Grid Dimension: The number of rows and columns have to be even, so
//		 * even if the user writes an odd number, it is automatically corrected
//		 * to the next even number. Topology will hold the values. Whenever the
//		 * grid dimensions are changed the mainPanel is informed of that so that
//		 * the composed model can be recreated. It is particularly important
//		 * when the user has already defined a composed model and then restarts
//		 * the simulation and decides to change the grid's dimensions. If the
//		 * model was already loaded and the user change the grids dimensions
//		 * after a restart, then the grid has to reinitialize again.
//		 */
//
//		JLabel setWidth = new JLabel();
//		JLabel setHeight = new JLabel();
//		JButton loadSBML = new JButton("Load SBML");
//		JLabel labelFilename = new JLabel();
//
//		labelFilename.setText("Filename: ");
//		labelFilename.setForeground(Color.white);
//
//		setWidth.setText("Width: ");
//		setHeight.setText("Height: ");
//
//		setWidth.setForeground(Color.white);
//		setHeight.setForeground(Color.white);
//
//		userDefinedWidth.setPreferredSize(new Dimension(34, 26));
//		userDefinedHeight.setPreferredSize(new Dimension(34, 26));
//
//		userDefinedWidth.setHorizontalAlignment(JTextField.CENTER);
//		userDefinedHeight.setHorizontalAlignment(JTextField.CENTER);
//		userDefinedWidth.setText("" + mainPanel.getTopology().getWidth());
//		userDefinedHeight.setText("" + mainPanel.getTopology().getHeight());
//
//		userDefinedWidth.addFocusListener(new FocusListener() {
//
//			@Override
//			public void focusGained(FocusEvent arg0) {
//				if (mainPanel.getEpithelium().getUnitaryModel() != null)
//					mainPanel.fillHexagons();
//			}
//
//			@Override
//			public void focusLost(FocusEvent arg0) {
//				if (mainPanel.getEpithelium().getUnitaryModel() == null) {
//					sanityCheckDimension(userDefinedWidth);
//					sanityCheckDimension(userDefinedHeight);
//					setWidth();
//					setHeight();
//				}
//			}
//		});
//
//		userDefinedHeight.addFocusListener(new FocusListener() {
//
//			@Override
//			public void focusGained(FocusEvent arg0) {
//				if (mainPanel.getEpithelium().getUnitaryModel() != null)
//					mainPanel.fillHexagons();
//			}
//
//			@Override
//			public void focusLost(FocusEvent arg0) {
//				if (mainPanel.getEpithelium().getUnitaryModel() == null) {
//					sanityCheckDimension(userDefinedWidth);
//					sanityCheckDimension(userDefinedHeight);
//					setWidth();
//					setHeight();
//				}
//
//			}
//		});
//
//		addMouseListener(new MouseListener() {
//
//			@Override
//			public void mouseClicked(MouseEvent arg0) {
//				if (mainPanel.getEpithelium().getUnitaryModel() == null) {
//					sanityCheckDimension(userDefinedWidth);
//					sanityCheckDimension(userDefinedHeight);
//					setWidth();
//					setHeight();
//				}
//			}
//
//			@Override
//			public void mouseEntered(MouseEvent arg0) {
//
//				if (mainPanel.getEpithelium().getUnitaryModel() == null) {
//					sanityCheckDimension(userDefinedWidth);
//					sanityCheckDimension(userDefinedHeight);
//					setWidth();
//					setHeight();
//				}
//			}
//
//			@Override
//			public void mouseExited(MouseEvent arg0) {
//			}
//
//			@Override
//			public void mousePressed(MouseEvent arg0) {
//			}
//
//			@Override
//			public void mouseReleased(MouseEvent arg0) {
//
//			}
//		});
//
//		add(setWidth);
//		add(userDefinedWidth);
//		add(setHeight);
//		add(userDefinedHeight);
//		add(loadSBML);
//		if (mainPanel.getEpithelium().getUnitaryModel() != null) {
//			labelFilename.setText(SBMLFilename);
//			add(labelFilename);
//		}
//	}

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

	private void loadConfigurations() {

		mainPanel.setEpithelium(mainPanel.getEpithelium());
		resetAllPanels();

		mainPanel.hexagonsPanel.paintComponent(mainPanel.hexagonsPanel
				.getGraphics());
		// setupDefinitionsButton.setEnabled(true);
		mainPanel.auxiliaryHexagonsPanel.setBorder(javax.swing.BorderFactory
				.createEmptyBorder());
		TitledBorder titleInitialConditions;
		titleInitialConditions = BorderFactory.createTitledBorder("");
		mainPanel.auxiliaryHexagonsPanel.setBorder(titleInitialConditions);
	}
}
