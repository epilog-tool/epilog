package pt.igc.nmd.epilog.gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
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

	//private JLabel iterationNumber = new JLabel();

	private JFileChooser fc = new JFileChooser();
	private MainFrame mainFrame = null;

	//private String SBMLFilename = null;

	public StartPanel(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
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
		newEpithelium = new JButton("New Epithelium");
		quitButton = new JButton("Quit");
		saveButton = new JButton("Save");

		// iterationNumber.setText(""
		// + mainPanel.getSimulation().getIterationNumber());

		/*
		 * Quit Button: This button closes the application. It also closes all
		 * opened windows.
		 */

		quitButton.setBackground(Color.red);
		quitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainFrame.dispose();
				System.exit(0);
			}
		});

		/*
		 * Model Loading: The model is loaded now from the SBML file. The
		 * function askmodel performs all the operations
		 */

		newEpithelium.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				mainFrame.initializePanelCenter();

				// askModel();
				// mainPanel.getContentPane().repaint();
				// mainPanel.getSimulation().resetIterationNumber();
				// mainPanel.getEpithelium().setNewEpithelium(true);
			}
		});

		loadEpithelium.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				askConfigurations();
				mainFrame.getContentPane().repaint();
				mainFrame.simulation.resetIterationNumber();
				mainFrame.getEpithelium().setNewEpithelium(false);
			}
		});

		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (mainFrame.topology == null
						|| mainFrame.getEpithelium().getUnitaryModel() != null)
					save();
			}
		});

		add(newEpithelium);
		add(loadEpithelium);
		add(saveButton);
		add(quitButton);

		return this;
	}

//	private void resetAllPanels() {
//		removeAll();
//		init();
//		mainFrame.componentsPanel.removeAll();
//		mainFrame.componentsPanel.init();
//		mainFrame.watcherPanel.removeAll();
//		mainFrame.watcherPanel.init();
//		mainFrame.inputsPanel.removeAll();
//		mainFrame.inputsPanel.init();
//		mainFrame.initial.removeAll();
//		mainFrame.initial.init();
//		mainFrame.getContentPane().repaint();
//	}

	private void loadModel(File file) {

		SBMLFormat sbmlFormat = new SBMLFormat();
		LogicalModel logicalModel = null;

		try {
			logicalModel = sbmlFormat.importFile(file);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			sbmlFormat.export(logicalModel, baos);

		//	String mySBML = new String(baos.toByteArray());

		} catch (IOException e) {
			System.err.println("Cannot import file " + file.getAbsolutePath()
					+ ": " + e.getMessage());
		}

		if (logicalModel == null)
			return;

		mainFrame.getEpithelium().setUnitaryModel(logicalModel);
//		resetAllPanels();

		mainFrame.hexagonsPanel.paintComponent(mainFrame.hexagonsPanel
				.getGraphics());

		mainFrame.getLogicalModelComposition().resetComposedModel();

		// setupDefinitionsButton.setEnabled(true);
		mainFrame.auxiliaryHexagonsPanel.setBorder(javax.swing.BorderFactory
				.createEmptyBorder());
		TitledBorder titleInitialConditions;
		titleInitialConditions = BorderFactory.createTitledBorder("");
		mainFrame.auxiliaryHexagonsPanel.setBorder(titleInitialConditions);

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
					mainFrame.getEpithelium().setSBMLLoadPath(
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
				mainFrame.getEpithelium().setSBMLFilename(SBMLFilename);
			}

			if (identifier.contains("GD")) {
				mainFrame.topology.setWidth(
						Integer.parseInt(line.split(" ")[1].split(",")[0]));
				mainFrame.topology.setHeight(
						Integer.parseInt(line.split(" ")[1].split(",")[1]));
				removeAll();
				init();

			}
			if (identifier.contains("RL")) {
				mainFrame.topology.setRollOver(
						line.split(" ")[1].split(",")[0]);
			}
			if (identifier.contains("IC")) {
				NodeInfo node = mainFrame.getEpithelium().getUnitaryModel()
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
							mainFrame.getEpithelium().setGrid(instance, node,
									value);
						}
					}
				}
			}

			if (identifier.contains("IT")) {
				NodeInfo node = mainFrame.getEpithelium().getUnitaryModel()
						.getNodeOrder()
						.get(Integer.parseInt(line.split(" ")[2]));
				byte value = (byte) Integer.parseInt(line.split(" ")[4]);
				String expression = line.split(" ")[6];
				mainFrame.getEpithelium().setIntegrationFunctions(node, value,
						expression);
			}

			if (identifier.contains("CL")) {

				NodeInfo node = mainFrame.getEpithelium().getUnitaryModel()
						.getNodeOrder()
						.get(Integer.parseInt(line.split(" ")[2]));
				Color color = new Color(Integer.parseInt(line.split(" ")[4]));

				mainFrame.getEpithelium().setColor(node, color);
			}
		}
	}

	private void loadConfigurations() {

		mainFrame.setEpithelium(mainFrame.getEpithelium());
//		resetAllPanels();

		mainFrame.hexagonsPanel.paintComponent(mainFrame.hexagonsPanel
				.getGraphics());
		// setupDefinitionsButton.setEnabled(true);
		mainFrame.auxiliaryHexagonsPanel.setBorder(javax.swing.BorderFactory
				.createEmptyBorder());
		TitledBorder titleInitialConditions;
		titleInitialConditions = BorderFactory.createTitledBorder("");
		mainFrame.auxiliaryHexagonsPanel.setBorder(titleInitialConditions);
	}

	public void save() {

		// TODO: Provisory method. It will evolve to something more elaborate
		JFileChooser fc = new JFileChooser();
		PrintWriter out;
		if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)

			try {

				out = new PrintWriter(new FileWriter(fc.getSelectedFile()
						.getAbsolutePath() + "_config.txt"));
				createConfigFile(out);
				out.close();

				String zipFile = fc.getSelectedFile().getAbsolutePath()
						+ ".zip";

				String unitarySBML = "";
				if (mainFrame.getEpithelium().isNewEpithelium()) {
					unitarySBML = mainFrame.getEpithelium().getSBMLFilePath();
				} else {
					unitarySBML = mainFrame.getEpithelium().getSBMLLoadPath();

				}
				System.out.println("Unitary SBML" + unitarySBML);

				String[] sourceFiles = {
						fc.getSelectedFile().getAbsolutePath() + "_config.txt",
						unitarySBML };

				byte[] buffer = new byte[1024];
				FileOutputStream fout = new FileOutputStream(zipFile);
				ZipOutputStream zout = new ZipOutputStream(fout);

				for (int i = 0; i < sourceFiles.length; i++) {
					System.out.println("Adding " + sourceFiles[i]);
					// create object of FileInputStream for source file
					FileInputStream fin = new FileInputStream(sourceFiles[i]);
					zout.putNextEntry(new ZipEntry(sourceFiles[i]));
					int length;

					while ((length = fin.read(buffer)) > 0) {
						zout.write(buffer, 0, length);
					}
					zout.closeEntry();
					fin.close();
				}
				zout.close();
				System.out.println("Zip file has been created!");
				File toDelete = new File(fc.getSelectedFile().getAbsolutePath()
						+ "_config.txt");
				toDelete.delete();
			} catch (IOException e) {
				e.printStackTrace();
			}

	}

	private void createConfigFile(PrintWriter out) {

		// TODO : Change PrintWriter to FileWriter - Tratamento de Excepções. o
		// file writer espera ate ter uma quantiodade grande de dados para
		// enviar tudo de uma vez. importante quando estamos numa ligaão remota

		// SBML Filename
		out.write("SN " + mainFrame.getEpithelium().getSBMLFilename() + "\n");

		// Grid Dimensions
		out.write("GD " + mainFrame.topology.getWidth() + ","
				+ mainFrame.topology.getHeight() + "\n");

		out.write("\n");
		// Roll-Over option
		out.write("RL " + mainFrame.topology.getRollOver() + "\n");

		out.write("\n");
		// InitialState
		for (NodeInfo node : mainFrame.getEpithelium().getUnitaryModel()
				.getNodeOrder()) {
			if (!mainFrame.getEpithelium().isIntegrationComponent(node)) {
				for (int value = 1; value < node.getMax() + 1; value++) {
					out.write("IC "
							+ node.getNodeID()
							+ " "
							+ mainFrame.getEpithelium().getUnitaryModel()
									.getNodeOrder().indexOf(node) + " : "
							+ value + " ( ");
					int previous = 0;
					int inDash = 0;
					for (int instance = 0; instance < mainFrame.topology
							.getNumberInstances(); instance++) {
						if (mainFrame.getEpithelium().getGridValue(instance,
								node) == value) {
							if (previous != instance - 1) {
								if (inDash == 1) {
									out.write("-" + previous + ",");
								} else if (previous != 0) {
									out.write(",");
								}
								out.write("" + instance);
								inDash = 0;
							} else {
								inDash = 1;
								if (instance == mainFrame.topology
										.getNumberInstances() - 1
										& previous == instance - 1) {
									out.write("-" + instance);
								}
							}
							previous = instance;
						}
					}
					out.write(" )\n");
				}
			}
		}

		out.write("\n");
		// Integration Components
		for (NodeInfo node : mainFrame.getEpithelium().getUnitaryModel()
				.getNodeOrder()) {
			if (mainFrame.getEpithelium().isIntegrationComponent(node)) {

				for (byte value = 1; value < node.getMax() + 1; value++) {
					out.write("IT "
							+ node.getNodeID()
							+ " "
							+ mainFrame.getEpithelium().getUnitaryModel()
									.getNodeOrder().indexOf(node)
							+ " : "
							+ value
							+ " : "
							+ mainFrame.getEpithelium().getIntegrationFunction(
									node, value) + "\n");
				}
			}
		}

		out.write("\n");
		// Colors

		for (NodeInfo node : mainFrame.getEpithelium().getUnitaryModel()
				.getNodeOrder()) {
			out.write("CL "
					+ node.getNodeID()
					+ " "
					+ mainFrame.getEpithelium().getUnitaryModel()
							.getNodeOrder().indexOf(node) + " : "
					+ mainFrame.getEpithelium().getColor(node).getRGB() + "\n");
		}

		out.write("\n");
		// Perturbations

		// Priorities

	}
}
