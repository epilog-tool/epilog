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
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
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
import org.colomoto.logicalmodel.perturbation.AbstractPerturbation;
import org.colomoto.logicalmodel.perturbation.FixedValuePerturbation;
import org.colomoto.logicalmodel.perturbation.MultiplePerturbation;
import org.colomoto.logicalmodel.perturbation.RangePerturbation;

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

	// private JLabel iterationNumber = new JLabel();

	private JFileChooser fc = new JFileChooser();
	private MainFrame mainFrame = null;

	// private String SBMLFilename = null;

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
				mainFrame.simulation.reset();
				mainFrame.epithelium.setNewEpithelium(true);
			}
		});

		loadEpithelium.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainFrame.initializePanelCenter();
				askConfigurations();
				mainFrame.getContentPane().repaint();
				mainFrame.simulation.reset();
				mainFrame.epithelium.setNewEpithelium(false);
			}
		});

		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (mainFrame.topology == null
						|| mainFrame.epithelium.getUnitaryModel() != null)
					save();
			}
		});

		add(newEpithelium);
		add(loadEpithelium);
		add(saveButton);
		add(quitButton);

		return this;
	}

	private void loadModel(File file) {

		SBMLFormat sbmlFormat = new SBMLFormat();
		LogicalModel logicalModel = null;

		try {
			logicalModel = sbmlFormat.importFile(file);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			sbmlFormat.export(logicalModel, baos);

			// String mySBML = new String(baos.toByteArray());

		} catch (IOException e) {
			System.err.println("Cannot import file " + file.getAbsolutePath()
					+ ": " + e.getMessage());
		}

		if (logicalModel == null)
			return;

		this.mainFrame.epithelium.setUnitaryModel(logicalModel);
		// resetAllPanels();

		this.mainFrame.hexagonsPanel
				.paintComponent(this.mainFrame.hexagonsPanel.getGraphics());

		this.mainFrame.getLogicalModelComposition().resetComposedModel();

		// setupDefinitionsButton.setEnabled(true);
		this.mainFrame.auxiliaryHexagonsPanel
				.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		TitledBorder titleInitialConditions;
		titleInitialConditions = BorderFactory.createTitledBorder("");
		this.mainFrame.auxiliaryHexagonsPanel.setBorder(titleInitialConditions);

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
					this.mainFrame.epithelium.setSBMLLoadPath(fileEntry
							.getAbsolutePath());
				}
			}
			for (final File fileEntry : folder.listFiles()) {
				if (fileEntry.getName().contains("config")) {
					try {
						Scanner fileIn = new Scanner(new File(
								fileEntry.getAbsolutePath()));
						ld(fileIn);
						loadConfigurations();

					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		}
	}

	private void ld(Scanner fileIn) {
		int numberOfSets = 0;

		Hashtable<String, NodeInfo> string2Node = new Hashtable<String, NodeInfo>();
		Hashtable<Integer, AbstractPerturbation> mlist = new Hashtable<Integer, AbstractPerturbation>();
		Hashtable<Integer, String> setPrioritiesDescription = new Hashtable<Integer, String>();
		Hashtable<Integer, String> setPerturbationsDescription = new Hashtable<Integer, String>();
		Hashtable<Integer, String> setInitialStateDescription = new Hashtable<Integer, String>();
		Hashtable<Integer, String> setInputsDescription = new Hashtable<Integer, String>();

		Hashtable<String, AbstractPerturbation[]> perturbationsSet = new Hashtable<String, AbstractPerturbation[]>();
		List perturbationsList = new ArrayList<AbstractPerturbation>();
		// List mutationsList = new ArrayList<AbstractPerturbation>();

		for (NodeInfo node : mainFrame.epithelium.getUnitaryModel()
				.getNodeOrder()) {
			string2Node.put(node.getNodeID(), node);
		}

		// TODO: Mudar para Buffereader: Lê muitas coisas de cada vez, lida
		// tambem com caracteres especiais

		while (fileIn.hasNext()) {

			String line = fileIn.nextLine();

			String identifier = line.split(" ")[0];

			if (identifier.contains("SN")) {

				String SBMLFilename = line.split(" ")[1];
				this.mainFrame.epithelium.setSBMLFilename(SBMLFilename);
			}

			if (identifier.contains("GD")) {
				this.mainFrame.topology.setWidth(Integer.parseInt(line
						.split(" ")[1].split(",")[0]));
				this.mainFrame.topology.setHeight(Integer.parseInt(line
						.split(" ")[1].split(",")[1]));
				// this.mainFrame.userDefinedWidth.setText(""
				// + this.mainFrame.topology.getWidth());
				// this.mainFrame.userDefinedHeight.setText(""
				// + this.mainFrame.topology.getHeight());
				// removeAll();
				// init();
				this.mainFrame.setEpithelium(this.mainFrame.epithelium);

			}
			if (identifier.contains("RL")) {
				this.mainFrame.topology.setRollOver(line.split(" ")[1]
						.split(",")[0]);
			}
			if (identifier.contains("IS")) {
				if (line.contains("name")) {
					int key = Integer.parseInt(line.split(" ")[1]);
					String value = line.split(":")[1];
					setInitialStateDescription.put(key, value);
					mainFrame.epithelium.setInitialStateSet(value);
					
				} else {

					NodeInfo node = this.mainFrame.epithelium.getUnitaryModel()
							.getNodeOrder()
							.get(Integer.parseInt(line.split(" ")[3]));
					byte value = (byte) Integer.parseInt(line.split(" ")[5]);
					String name = setInitialStateDescription.get(Integer
							.parseInt(line.split(" ")[1]));
					

					for (String range : line.split(" ")[7].split(",")) {
						range.replace("(", "");
						range.replace(")", "");
						range.replace(" ", "");

						if (range.contains("-")) {

							int init = Integer.parseInt(range.split("-")[0]);
							int end = Integer.parseInt(range.split("-")[1]);
							for (int instance = init; instance <= end; instance++) {

								mainFrame.epithelium.getInitialStateSet().get(name).setGrid(instance, node, value);
							}

						} else if (range.length() >= 1) {
							int instance = Integer
									.parseInt(range.split(" ")[0]);

							mainFrame.epithelium.getInitialStateSet().get(name).setGrid(instance, node, value);

						}
					}
				}
			}

			if (identifier.contains("IT")) {
				NodeInfo node = this.mainFrame.epithelium.getUnitaryModel()
						.getNodeOrder()
						.get(Integer.parseInt(line.split(" ")[2]));
				byte value = (byte) Integer.parseInt(line.split(" ")[4]);
				String expression = line.split(" ")[6];
				this.mainFrame.epithelium.setIntegrationFunctions(node, value,
						expression);
				this.mainFrame.epithelium.setIntegrationComponent(
						mainFrame.epithelium.getUnitaryModel().getNodeOrder()
								.indexOf(node), true);
			}

			if (identifier.contains("CL")) {

				NodeInfo node = this.mainFrame.epithelium.getUnitaryModel()
						.getNodeOrder()
						.get(Integer.parseInt(line.split(" ")[2]));
				Color color = new Color(Integer.parseInt(line.split(" ")[4]));

				this.mainFrame.epithelium.setColor(node, color);
			}

			if (identifier.contains("PR")) {

				// TODO: Change to while

				if (line.contains("#sets")) {
					numberOfSets = Integer.parseInt(line.split(" ")[3]);
				} else if (line.contains("name")) {
					int setNumber = Integer.parseInt(line.split(" ")[1]);
					String setName = line.split(" ")[3];
					setPrioritiesDescription.put(setNumber, setName);

				} else {
					List<List<NodeInfo>> prioritiesClass = new ArrayList<List<NodeInfo>>();
					int setNumber = Integer.parseInt(line.split(" ")[1]);
					String priorities = line.split(":")[1];
					priorities = priorities.replace("]]", "]");
					priorities = priorities.replace("[", "");
					priorities = priorities.replace(" ", "");
					String[] p1 = priorities.split("]");

					for (String aux : p1) {
						String[] prioritiesElementsString = aux.split(",");
						List<NodeInfo> prioritiesOfThisClass = new ArrayList<NodeInfo>();
						for (String aux_3 : prioritiesElementsString) {
							NodeInfo node = string2Node.get(aux_3);
							if (node != null)
								prioritiesOfThisClass.add(node);
						}

						prioritiesClass.add(prioritiesOfThisClass);
					}
					// System.out.println(setNumber);

					if (setPrioritiesDescription.get(setNumber) != null)
						mainFrame.epithelium.setPrioritiesSet(
								setPrioritiesDescription.get(setNumber),
								prioritiesClass);
				}

			}

			if (identifier.contains("PT")) {

				int index = 0;

				if (line.contains("perturbations")) {
					String perturbationsString = line.split(":")[1];
					perturbationsString = perturbationsString.replace("(", "");

					String[] perturbationsArray = perturbationsString
							.split("\\)");

					for (String aux : perturbationsArray) {
						aux = aux.replace("]", "");
						if (aux.contains(",")) {
							aux = aux.replace(" ", "");
							int max = Integer.parseInt(aux.split(",")[1]);
							int min = Integer.parseInt(aux.split(",")[0]
									.split("\\[")[1]);

							NodeInfo node = string2Node
									.get(aux.split("\\[")[0]);

							AbstractPerturbation a = setPerturbation(node, min,
									max);
							perturbationsList.add(a);
						} else if (aux.contains("KO")) {
							aux = aux.replace(" ", "");
							NodeInfo node = string2Node.get(aux.split("KO")[0]);
							AbstractPerturbation a = setPerturbation(node, 0, 0);
							perturbationsList.add(a);
						} else {

							NodeInfo node = string2Node.get(aux.split(" ")[0]);
							int min = Integer.parseInt(aux.split(" ")[1]
									.split("E")[1]);
							int max = min;
							AbstractPerturbation a = setPerturbation(node, min,
									max);
							perturbationsList.add(a);
							// TODO
						}
					}

					mainFrame.epithelium
							.setLoadedPerturbations(perturbationsList);

				}

				else if (line.contains("mutations")) {
					// TODO ECTOPIC AND KO

					String mutationsString = line.split(":")[1];
					mutationsString = mutationsString.replace("(", "");
					mutationsString = mutationsString.replace("[", "");
					mutationsString = mutationsString.replace("]", "");
					String[] mutationsArray = mutationsString.split("\\)");
					NodeInfo node = null;

					for (String aux : mutationsArray) {
						List perturbation = new ArrayList<AbstractPerturbation>();

						if (aux.length() > 1) {

							String[] aux_2 = aux.split(" ");
							for (String aux_3 : aux_2) {
								if (aux_3.length() > 1) {
									if (string2Node.get(aux_3) != null) {

										node = string2Node.get(aux_3);

									}// Ends if (string2Node.get(aux_3)!=null)
									else if (aux_3.contains(",")) {
										int min = Integer.parseInt(aux_3
												.split(",")[0]);
										int max = Integer.parseInt(aux_3
												.split(",")[1]);
										AbstractPerturbation a = setPerturbation(
												node, min, max);
										perturbation.add(a);
									} // Ends else if (aux_3.contains(","))
									else if (aux_3.contains("KO")) {
										int max = 0;
										int min = 0;
										AbstractPerturbation a = setPerturbation(
												node, min, max);
										perturbation.add(a);
									}// ENds else if (aux_3.contains("KO"))

									else if (aux_3.contains("E")) {
										int min = Integer.parseInt(aux
												.split("E")[1].split(" ")[0]);
										int max = min;
										AbstractPerturbation a = setPerturbation(
												node, min, max);
										perturbation.add(a);
									}// Ends else if (aux_3.contains("E"))

								}// Ends if (aux_3.length()>1)
							}// Ends for (String aux_3 : aux_2)
							MultiplePerturbation mutation = new MultiplePerturbation(
									perturbation);
							mlist.put(index, mutation);

							index = index + 1;
						}// ends if (aux.length() > 1)

					}// Ends for (String aux : mutationsArray)

					List aux = new ArrayList<AbstractPerturbation>();
					List aux2 = new ArrayList<String>();

					for (AbstractPerturbation a : mlist.values()) {

						if (!aux2.contains(a.toString()))
							aux.add(a);
						aux2.add(a.toString());
					}
					mainFrame.epithelium.setLoadedMutations(aux);

				} else if (line.contains("name")) {

					int setNumber = Integer.parseInt(line.split(" ")[1]);
					String setName = line.split(" ")[3];
					setPerturbationsDescription.put(setNumber, setName);
					if (perturbationsSet.get(setName) == null) {

						perturbationsSet.put(setName,
								new AbstractPerturbation[mainFrame.topology
										.getNumberInstances()]);
					}
				} else if (line.contains("color")) {

				} else if (line.contains("MT")) {
					// System.out.println(line);
					String setName = setPerturbationsDescription.get(
							Integer.parseInt(line.split(" ")[1])).replace(":",
							"");
					AbstractPerturbation p = mlist.get(Integer.parseInt(line
							.split(" ")[3]));
					String aux = line.split(":")[1];

					for (String range : aux.split(",")) {
						range = range.replace("(", "");
						range = range.replace(")", "");
						range = range.replace(" ", "");

						if (range.contains("-")) {

							int init = Integer.parseInt(range.split("-")[0]);
							int end = Integer.parseInt(range.split("-")[1]);
							for (int instance = init; instance <= end; instance++) {
								if (perturbationsSet.get(setName) == null) {

									perturbationsSet
											.put(setName,
													new AbstractPerturbation[mainFrame.topology
															.getNumberInstances()]);
								}
								perturbationsSet.get(setName)[instance] = p;

							}

						} else if (range.length() >= 1) {
							int instance = Integer
									.parseInt(range.split(" ")[0]);

							if (perturbationsSet.get(setName) == null)
								perturbationsSet
										.put(setName,
												new AbstractPerturbation[mainFrame.topology
														.getNumberInstances()]);
							perturbationsSet.get(setName)[instance] = p;
						}
					}
					if (perturbationsSet.get(setName) != null)
						mainFrame.epithelium.setPerturbationSet(setName,
								perturbationsSet.get(setName));

				}

			}

		}
	}

	private void loadConfigurations() {

		this.mainFrame.setEpithelium(this.mainFrame.epithelium);
		this.mainFrame.setProv(true);
		this.mainFrame.repaint();
		this.mainFrame.gridSpecsPanel();
		this.mainFrame.repaint();
		this.mainFrame.hexagonsPanel
				.paintComponent(this.mainFrame.hexagonsPanel.getGraphics());

		this.mainFrame.auxiliaryHexagonsPanel
				.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		TitledBorder titleInitialConditions;
		titleInitialConditions = BorderFactory.createTitledBorder("");
		this.mainFrame.auxiliaryHexagonsPanel.setBorder(titleInitialConditions);
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
				if (this.mainFrame.epithelium.isNewEpithelium()) {
					unitarySBML = this.mainFrame.epithelium.getSBMLFilePath();
				} else {
					unitarySBML = this.mainFrame.epithelium.getSBMLLoadPath();

				}

				// unitarySBML =
				// this.mainFrame.getEpithelium().getSBMLFilePath();
				// System.out.println("Unitary SBML" + unitarySBML);

				String[] sourceFiles = {
						fc.getSelectedFile().getAbsolutePath() + "_config.txt",
						unitarySBML };

				byte[] buffer = new byte[1024];
				FileOutputStream fout = new FileOutputStream(zipFile);
				ZipOutputStream zout = new ZipOutputStream(fout);

				for (int i = 0; i < sourceFiles.length; i++) {

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
				// System.out.println("Zip file has been created!");
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
		out.write("SN " + this.mainFrame.epithelium.getSBMLFilename() + "\n");

		// Grid Dimensions
		out.write("GD " + this.mainFrame.topology.getWidth() + ","
				+ this.mainFrame.topology.getHeight() + "\n");

		out.write("\n");
		// Roll-Over option
		out.write("RL " + this.mainFrame.topology.getRollOver() + "\n");

		out.write("\n");
		// InitialState

		int index = -1;
		for (String key : mainFrame.epithelium.getInitialStateSet().keySet()) {
			mainFrame.epithelium.setSelectedInitialSet(key);
			index = index + 1;

			out.write("IS " + index + " name : " + key + "\n");

			for (NodeInfo node : this.mainFrame.epithelium.getUnitaryModel()
					.getNodeOrder()) {
				if (!node.isInput()) {
					for (int value = 1; value < node.getMax() + 1; value++) {

						out.write("IS "
								+ index
								+ " "
								+ node.getNodeID()
								+ " "
								+ this.mainFrame.epithelium.getUnitaryModel()
										.getNodeOrder().indexOf(node) + " : "
								+ value + " ( ");

						int next = 0;
						int current = 0;
						boolean start = true;
						boolean ongoing = false;

						for (int instance = 0; instance < this.mainFrame.topology
								.getNumberInstances(); instance++) {
							if (this.mainFrame.epithelium.getGridValue(
									instance, node) == value) {

								if (start) {
									out.write("" + instance);
									start = false;
									current = instance;
								} else if ((instance != current + 1)
										& ongoing == false) {
									out.write(",");
									out.write("" + instance);
									current = instance;
								} else if ((instance == current + 1)) {
									ongoing = true;
									current = instance;
								}
							} else if (ongoing == true) {
								ongoing = false;
								out.write("-");
								out.write("" + (current));
							}
							if (instance == (this.mainFrame.topology
									.getNumberInstances() - 1)
									& ongoing == true) {
								out.write("-");
								out.write("" + (instance));
							}

						}
						out.write(" )\n");
					}
				}
			}// ends for (NodeInfo node :
				// this.mainFrame.epithelium.getUnitaryModel().getNodeOrder())
		}// for (String key: mainFrame.epithelium.getInitialStateSet().keySet())

		out.write("\n");

		// Env Inputs

		index = -1;
		for (String key : mainFrame.epithelium.getInputsSet().keySet()) {
			mainFrame.epithelium.setSelectedInputSet(key);
			index = index + 1;

			out.write("IC " + index + " name : " + key + "\n");

			for (NodeInfo node : this.mainFrame.epithelium.getUnitaryModel()
					.getNodeOrder()) {
				if (node.isInput()
						& !this.mainFrame.epithelium
								.isIntegrationComponent(node)) {
					for (int value = 1; value < node.getMax() + 1; value++) {

						out.write("IC "
								+ index
								+ " "
								+ node.getNodeID()
								+ " "
								+ this.mainFrame.epithelium.getUnitaryModel()
										.getNodeOrder().indexOf(node) + " : "
								+ value + " ( ");

						int next = 0;
						int current = 0;
						boolean start = true;
						boolean ongoing = false;

						for (int instance = 0; instance < this.mainFrame.topology
								.getNumberInstances(); instance++) {
							if (this.mainFrame.epithelium.getGridValue(
									instance, node) == value) {

								if (start) {
									out.write("" + instance);
									start = false;
									current = instance;
								} else if ((instance != current + 1)
										& ongoing == false) {
									out.write(",");
									out.write("" + instance);
									current = instance;
								} else if ((instance == current + 1)) {
									ongoing = true;
									current = instance;
								}
							} else if (ongoing == true) {
								ongoing = false;
								out.write("-");
								out.write("" + (current));
							}
							if (instance == (this.mainFrame.topology
									.getNumberInstances() - 1)
									& ongoing == true) {
								out.write("-");
								out.write("" + (instance));
							}

						}
						out.write(" )\n");
					}
				}// Ends the if (node.isInput()&
					// !this.mainFrame.epithelium.isIntegrationComponent(node))

				if (this.mainFrame.epithelium.isIntegrationComponent(node)) {

					for (byte value = 1; value < node.getMax() + 1; value++) {
						out.write("IT "
								+ index
								+ " "
								+ node.getNodeID()
								+ " "
								+ this.mainFrame.epithelium.getUnitaryModel()
										.getNodeOrder().indexOf(node)
								+ " : "
								+ value
								+ " : "
								+ this.mainFrame.epithelium
										.getIntegrationFunction(node, value)
								+ "\n");
					}
				}

			}// ends for (NodeInfo node :
				// this.mainFrame.epithelium.getUnitaryModel().getNodeOrder())
		}// for (String key: mainFrame.epithelium.getInputSet().keySet())

		out.write("\n");

		// Colors

		for (NodeInfo node : this.mainFrame.epithelium.getUnitaryModel()
				.getNodeOrder()) {
			out.write("CL "
					+ node.getNodeID()
					+ " "
					+ this.mainFrame.epithelium.getUnitaryModel()
							.getNodeOrder().indexOf(node) + " : "
					+ this.mainFrame.epithelium.getColor(node).getRGB() + "\n");
		}

		out.write("\n");

		// Perturbations

		if (this.mainFrame.epithelium.getPerturbationsSet().keySet().size() != 1) {

			String string = ("PT " + "perturbations" + " : ");

			for (String s : this.mainFrame.perturbationsPanel
					.getPerturbationsStrings())
				string = string + ("(" + s + ")");
			out.write(string);

			out.write("\n");

			if (this.mainFrame.perturbationsPanel.getMutationStrings() != null) {
				String string_2 = ("PT " + "mutations" + " : ");

				for (AbstractPerturbation s : this.mainFrame.perturbationsPanel
						.getMutationStrings()) {
					string_2 = string_2 + ("(" + s.toString() + ")");

				}
				out.write(string_2);
				out.write("\n");

			}
			int numberOfSets = (this.mainFrame.epithelium.getPerturbationsSet()
					.keySet().size() - 1);
			out.write("PT " + "#sets" + " : " + numberOfSets);
			out.write("\n");

			int setIndex = 0;
			for (String s : this.mainFrame.epithelium.getPerturbationsSet()
					.keySet()) {
				out.write("PT " + setIndex + " name " + ":" + s);
				out.write("\n");

				for (int mutationIndex = 0; mutationIndex < this.mainFrame.perturbationsPanel
						.getMutationStrings().length; mutationIndex++) {
					out.write("PT " + setIndex + " MT " + mutationIndex + " : "
							+ " ( ");

					int next = 0;
					int current = 0;
					boolean start = true;
					boolean ongoing = false;

					for (int instance = 0; instance < this.mainFrame.topology
							.getNumberInstances(); instance++) {
						if (this.mainFrame.epithelium.getPerturbationsSet()
								.get(s)[instance] == this.mainFrame.perturbationsPanel
								.getMutationStrings()[mutationIndex]) {

							if (start) {
								out.write("" + instance);
								start = false;
								current = instance;
							} else if ((instance != current + 1)
									& ongoing == false) {
								out.write(",");
								out.write("" + instance);
								current = instance;
							} else if ((instance == current + 1)) {
								ongoing = true;
								current = instance;
							}
						} else if (ongoing == true) {
							ongoing = false;
							out.write("-");
							out.write("" + (current));
						}
						if (instance == (this.mainFrame.topology
								.getNumberInstances() - 1) & ongoing == true) {
							out.write("-");
							out.write("" + (instance));
						}

					}
					out.write(" )\n");
				}
				setIndex++;
			}

		}// ends the perturbations
		out.write("\n");

		// Priorities
		// System.out.println(this.mainFrame.epithelium.getPrioritiesSet()
		// .keySet().size());
		if (this.mainFrame.epithelium.getPrioritiesSet().keySet().size() != 0) {
			int numberOfSets = (this.mainFrame.epithelium.getPrioritiesSet()
					.keySet().size() - 1);
			out.write("PR " + "#sets" + " : " + numberOfSets);
			out.write("\n");

			index = 0;
			for (String s : this.mainFrame.epithelium.getPrioritiesSet()
					.keySet()) {
				out.write("PR " + index + " name " + s);
				out.write("\n");
				out.write("PR "
						+ index
						+ " : "
						+ this.mainFrame.epithelium.getPrioritiesSet().get(s)
								.toString());
				out.write("\n");
				index++;
			}

		}

	}

	// Auxiliary Methods

	private AbstractPerturbation setPerturbation(NodeInfo node, int minValue,
			int maxValue) {
		AbstractPerturbation a = null;
		if (minValue == maxValue)
			a = new FixedValuePerturbation(node, minValue);
		else
			a = new RangePerturbation(node, minValue, maxValue);
		return a;
	}

}
