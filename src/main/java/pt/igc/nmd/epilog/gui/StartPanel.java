package pt.igc.nmd.epilog.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;
import org.colomoto.logicalmodel.io.sbml.SBMLFormat;
import org.colomoto.logicalmodel.perturbation.AbstractPerturbation;
import org.colomoto.logicalmodel.perturbation.FixedValuePerturbation;
import org.colomoto.logicalmodel.perturbation.MultiplePerturbation;
import org.colomoto.logicalmodel.perturbation.RangePerturbation;

import pt.igc.nmd.epilog.FileIO;
import pt.igc.nmd.epilog.Topology;

public class StartPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2013094563809493130L;

	private JButton newEpithelium;
	private JButton loadEpithelium;
	public JButton saveButton;
	private JButton quitButton;

	private JTextField userDefinedWidth;
	private JTextField userDefinedHeight;

	private boolean heightControl = true;
	private boolean widthControl = true;

	JButton loadSBML;

	public JLabel selectedFilenameLabel;

	// private JLabel iterationNumber = new JLabel();

	private JFileChooser fc = new JFileChooser();
	private MainFrame mainFrame = null;

	// private String SBMLFilename = null;

	public StartPanel(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		init();
	}

	/**
	 * Initializes the startPanel, which is the command line at the top of
	 * Epilog's mainPanel. If an epithelium model is loaded (or created) the
	 * main panel is modified, and only then can a user save an epithelium
	 * model.
	 * 
	 * @return startPanel the produced panel
	 * 
	 * @see MainFrame
	 * @see initializePanelCenter()
	 * @see save
	 */
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
		 * New Epithelium: The model is loaded now from the SBML file. Previous
		 * simulations are erased and a new epithelium is created.
		 */

		newEpithelium.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainFrame.initializePanelCenter();
				mainFrame.setTitle("Epilog - " + "New File");
				mainFrame.simulation.reset();
				mainFrame.epithelium.setNewEpithelium(true);
				saveButton.setEnabled(false);
				mainFrame.setUserMessageEmpty();

			}
		});

		/*
		 * Load Epithelium: a previously saved model is loaded.
		 */
		loadEpithelium.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainFrame.initializePanelCenter();
				askConfigurations();
				mainFrame.setTitle("Epilog - "
						+ mainFrame.epithelium.zepiFilename);
				mainFrame.getContentPane().repaint();
				mainFrame.simulation.reset();
				mainFrame.epithelium.setNewEpithelium(false);
				saveButton.setEnabled(true);
				mainFrame.setUserMessageEmpty();

			}
		});

		if (mainFrame.epithelium == null)
			saveButton.setEnabled(false);
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (mainFrame.epithelium != null) {
					saveEpitheliumModel();
					mainFrame.setTitle("Epilog - "
							+ mainFrame.epithelium.zepiFilename);
				}
			}
		});

		add(newEpithelium);
		add(loadEpithelium);
		add(saveButton);
		add(quitButton);

		return this;
	}

	/**
	 * Repaints the left panel of Epilog's mainPanel, whenever the grid's
	 * dimension has changed
	 * 
	 * @see Topology
	 * @see DrawPolygon
	 * @return panel left panel
	 */

	public JPanel gridSpecsPanel() {

		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel setWidth = new JLabel();
		JLabel setHeight = new JLabel();
		loadSBML = new JButton("Load SBML");
		userDefinedWidth = new JTextField();
		userDefinedHeight = new JTextField();
		selectedFilenameLabel = new JLabel();
		final Color lighterRed = new Color(255, 120, 120);

		// labelFilename.setText("Filename: ");

		setWidth.setText("Height: ");
		setHeight.setText("Width: ");

		userDefinedWidth.setPreferredSize(new Dimension(34, 26));
		userDefinedHeight.setPreferredSize(new Dimension(34, 26));

		userDefinedWidth.setHorizontalAlignment(JTextField.CENTER);
		userDefinedHeight.setHorizontalAlignment(JTextField.CENTER);
		userDefinedWidth.setText("" + mainFrame.topology.getWidth());
		userDefinedHeight.setText("" + mainFrame.topology.getHeight());

		userDefinedWidth.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent arg0) {

				if (mainFrame.epithelium.getUnitaryModel() == null) {
					JTextField src = (JTextField) arg0.getSource();

					try {
						Integer.parseInt(src.getText());
						mainFrame.topology.setWidth(Integer.parseInt(src
								.getText()));
						widthControl = true;
						userDefinedWidth.setBackground(Color.white);
						mainFrame.setUserMessageEmpty();
					} catch (NumberFormatException e) {
						System.out.println("Must be higher than zero");
						userDefinedWidth.setBackground(lighterRed);
						widthControl = false;
						mainFrame.setUserMessage(MsgStatus.ERROR,
								"Dimension must be an integer");
					}
					mainFrame.hexagonsPanel
							.paintComponent(mainFrame.hexagonsPanel
									.getGraphics());
				}
			}

			@Override
			public void focusGained(FocusEvent arg0) {
			}
		});

		userDefinedHeight.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent arg0) {
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
			}

			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					JTextField src = (JTextField) arg0.getSource();

					mainFrame.topology.setHeight(Integer.parseInt(src.getText()));
					mainFrame.hexagonsPanel
							.paintComponent(mainFrame.hexagonsPanel
									.getGraphics());
				}
			}
		});
		userDefinedWidth.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent arg0) {
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
			}

			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					JTextField src = (JTextField) arg0.getSource();

					mainFrame.topology.setWidth(Integer.parseInt(src.getText()));

					mainFrame.hexagonsPanel
							.paintComponent(mainFrame.hexagonsPanel
									.getGraphics());
				}
			}
		});

		userDefinedHeight.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent arg0) {
				if (mainFrame.epithelium.getUnitaryModel() == null) {
					JTextField src = (JTextField) arg0.getSource();

					try {
						Integer.parseInt(src.getText());
						mainFrame.topology.setHeight(Integer.parseInt(src
								.getText()));
						heightControl = true;
						userDefinedHeight.setBackground(Color.white);
						mainFrame.setUserMessageEmpty();
					} catch (NumberFormatException e) {
						System.out.println("Must be higher than zero");
						userDefinedHeight.setBackground(lighterRed);
						heightControl = false;
						mainFrame.setUserMessage(MsgStatus.ERROR,
								"Dimension must be an integer");
					}

					mainFrame.hexagonsPanel
							.paintComponent(mainFrame.hexagonsPanel
									.getGraphics());
				}
			}

			@Override
			public void focusGained(FocusEvent arg0) {
			}
		});

		panel.add(setHeight);
		panel.add(userDefinedWidth);
		panel.add(setWidth);
		panel.add(userDefinedHeight);
		panel.add(loadSBML);

		if (mainFrame.epithelium.getUnitaryModel() != null) {
			mainFrame.initializePanelCenterRight();

		}

		loadSBML.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (mainFrame.epithelium.getUnitaryModel() == null
						&& (heightControl && widthControl)) {
					askModel();

					if (mainFrame.epithelium.getUnitaryModel() != null)
						mainFrame.initializePanelCenterRight();
				} else
					mainFrame.setUserMessage(MsgStatus.ERROR,
							"Dimension must be an integer");
			}
		});

		return panel;
	}

	/**
	 * Loads the sbml model.
	 * 
	 * @see startPanel.loadModel()
	 * 
	 */
	private void askModel() {

		FileNameExtensionFilter xmlfilter = new FileNameExtensionFilter(
				"sbml files (*.sbml)", "sbml");
		fc.setFileFilter(xmlfilter);

		fc.setDialogTitle("Choose file");

		if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			selectedFilenameLabel.setText(fc.getSelectedFile().getName());
			selectedFilenameLabel.setForeground(Color.white);

			File file = fc.getSelectedFile();

			mainFrame.epithelium.setSBMLFilename(file.getName());
			mainFrame.epithelium.setSBMLPath(file.getAbsolutePath());

			loadModel(file);
			saveButton.setEnabled(true);
			mainFrame.gridSpecsPanel.getComponent(4).setEnabled(false);
			mainFrame.gridSpecsPanel.getComponent(3).setEnabled(false);
			mainFrame.gridSpecsPanel.getComponent(1).setEnabled(false);
			repaint();
		}

	}

	/**
	 * Loads the sbml model zipped in the epithelium model
	 * 
	 * @param file
	 *            sbml file with the original regulatory model
	 * 
	 * 
	 */
	public void loadModel(File file) {

		SBMLFormat sbmlFormat = new SBMLFormat();
		LogicalModel logicalModel = null;

		try {
			logicalModel = sbmlFormat.importFile(file);

			// ByteArrayOutputStream baos = new ByteArrayOutputStream();
			// sbmlFormat.export(logicalModel, baos);

		} catch (IOException e) {
			System.err.println("Cannot import file " + file.getAbsolutePath()
					+ ": " + e.getMessage());
		}

		if (logicalModel == null)
			return;

		this.mainFrame.epithelium.setUnitaryModel(logicalModel);

		this.mainFrame.hexagonsPanel
				.paintComponent(this.mainFrame.hexagonsPanel.getGraphics());

		this.mainFrame.getLogicalModelComposition().resetComposedModel();

		this.mainFrame.auxiliaryHexagonsPanel
				.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		TitledBorder titleInitialConditions;
		titleInitialConditions = BorderFactory.createTitledBorder("");
		this.mainFrame.auxiliaryHexagonsPanel.setBorder(titleInitialConditions);

	}

	/**
	 * The user chooses a previously saved epithelium model. A sbml file and a
	 * configurations file are loaded. If a composed was saved it is also
	 * loaded.
	 * 
	 * @see loadModel
	 * @see loadConfigurations
	 * @see ld
	 */
	private void askConfigurations() {

		FileNameExtensionFilter xmlfilter = new FileNameExtensionFilter(
				"zepi files (*.zepi)", "zepi");
		fc.setFileFilter(xmlfilter);

		fc.setDialogTitle("Choose file");

		if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			File folder;
			try {
				mainFrame.epithelium.zepiFilename = fc.getSelectedFile()
						.getName();
				folder = FileIO.unzipZepiTmpDir(fc.getSelectedFile()
						.getAbsolutePath());

				for (final File fileEntry : folder.listFiles()) {
					if (fileEntry.getName().endsWith(".sbml")
							|| fileEntry.getName().endsWith(".SBML")) {
						loadModel(fileEntry);
						this.mainFrame.epithelium.setSBMLLoadPath(fileEntry
								.getAbsolutePath());
					}
				}
				for (final File fileEntry : folder.listFiles()) {
					if (fileEntry.getName().endsWith("config.txt")
							|| fileEntry.getName().endsWith("CONFIG.TXT")) {
						try {
							Scanner fileIn = new Scanner(fileEntry);
							loadConfigFile(fileIn);
							loadConfigurations();

						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				deleteTempDir(folder);
			} catch (IOException e1) {
				// TODO: send this to User Error message!!! ptgm
				e1.printStackTrace();
			}

		}
	}

	/**
	 * Recursive directory cleaning
	 * 
	 * @param fdir
	 */
	private void deleteTempDir(File fdir) {
		if (fdir == null)
			return;
		if (fdir.isDirectory()) {
			for (File entry : fdir.listFiles()) {
				deleteTempDir(entry);
			}
		}
		fdir.delete();
	}

	/**
	 * Loads the information retrieved from the config file
	 * 
	 * @param fileIn
	 *            config file with the saved configurations of the epithelium
	 *            model
	 * @see setPerturbation
	 */
	private void loadConfigFile(Scanner fileIn) {
		// int numberOfSets = 0;

		Hashtable<String, NodeInfo> string2Node = new Hashtable<String, NodeInfo>();
		Hashtable<Integer, AbstractPerturbation> mlist = new Hashtable<Integer, AbstractPerturbation>();
		Hashtable<Integer, String> setPrioritiesDescription = new Hashtable<Integer, String>();
		Hashtable<Integer, String> setPerturbationsDescription = new Hashtable<Integer, String>();
		Hashtable<Integer, String> setInitialStateDescription = new Hashtable<Integer, String>();
		Hashtable<Integer, String> setInputsDescription = new Hashtable<Integer, String>();

		Hashtable<String, Hashtable<NodeInfo, List<String>>> p0 = new Hashtable<String, Hashtable<NodeInfo, List<String>>>();

		Hashtable<String, AbstractPerturbation[]> perturbationsSet = new Hashtable<String, AbstractPerturbation[]>();
		List<AbstractPerturbation> perturbationsList = new ArrayList<AbstractPerturbation>();

		for (NodeInfo node : mainFrame.epithelium.getUnitaryModel()
				.getNodeOrder()) {
			string2Node.put(node.getNodeID(), node);
		}

		// TODO: Mudar para Buffereader: Le muitas coisas de cada vez, lida
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
					value = value.replace(" ", "");
					setInitialStateDescription.put(key, value);
					System.out.print(value);
					mainFrame.epithelium.setInitialStateSet(value,
							mainFrame.simulation.isRunning());

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

								mainFrame.epithelium.getInitialStateSet()
										.get(name)
										.setGrid(instance, node, value);
							}

						} else if (range.length() >= 1) {
							int instance = Integer
									.parseInt(range.split(" ")[0]);

							mainFrame.epithelium.getInitialStateSet().get(name)
									.setGrid(instance, node, value);

						}
					}
				}
			}

			if (identifier.contains("IT")) {

				if (line.contains("name")) {
					int key = Integer.parseInt(line.split(" ")[1]);
					String setName = line.split(":")[1].replace(" ", "").split(
							"\\[")[0];
					setName.replace(" ", "");

					setInputsDescription.put(key, setName);
					Hashtable<NodeInfo, List<String>> p1 = new Hashtable<NodeInfo, List<String>>();

					p0.put(setName, p1);

				} else {

					// System.out.println(line);
					NodeInfo node = this.mainFrame.epithelium.getUnitaryModel()
							.getNodeOrder()
							.get(Integer.parseInt(line.split(" ")[3]));

					mainFrame.epithelium.setIntegrationComponent(
							Integer.parseInt(line.split(" ")[3]), true);
					// byte value = (byte) Integer.parseInt(line.split(" ")[5]);

					String expression = line.split(" ")[7];
					String setName = setInputsDescription.get(Integer
							.parseInt(line.split(" ")[1]));

					if (p0.get(setName).get(node) == null) {
						List<String> p2 = new ArrayList<String>();
						p0.get(setName).put(node, p2);
					}
					p0.get(setName).get(node).add(expression);
				}
			}

			mainFrame.epithelium.integrationInputsSet = p0;

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
					// numberOfSets = Integer.parseInt(line.split(" ")[3]);
				} else if (line.contains("name")) {
					int setNumber = Integer.parseInt(line.split(" ")[1]);
					String setName = line.split(" ")[3];
					setPrioritiesDescription.put(setNumber, setName);

				} else {
					List<List<String>> prioritiesClass = new ArrayList<List<String>>();
					int setNumber = Integer.parseInt(line.split(" ")[1]);
					String priorities = line.split(":")[1];
					priorities = priorities.replace("]]", "]");
					priorities = priorities.replace("[", "");
					priorities = priorities.replace(" ", "");
					String[] p1 = priorities.split("]");

					for (String aux : p1) {
						String[] prioritiesElementsString = aux.split(",");
						List<String> prioritiesOfThisClass = new ArrayList<String>();
						for (String aux_3 : prioritiesElementsString) {
							// NodeInfo node = string2Node.get(aux_3);
							// if (node != null)
							prioritiesOfThisClass.add(aux_3);
						}

						prioritiesClass.add(prioritiesOfThisClass);
					}

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
						}
					}

					mainFrame.epithelium
							.setLoadedPerturbations(perturbationsList);

				}

				else if (line.contains("mutations")) {

					String mutationsString = line.split(":")[1];
					mutationsString = mutationsString.replace("(", "");
					mutationsString = mutationsString.replace("[", "");
					mutationsString = mutationsString.replace("]", "");
					String[] mutationsArray = mutationsString.split("\\)");
					NodeInfo node = null;

					for (String aux : mutationsArray) {
						List<AbstractPerturbation> perturbation = new ArrayList<AbstractPerturbation>();

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

					List<AbstractPerturbation> aux = new ArrayList<AbstractPerturbation>();
					List<String> aux2 = new ArrayList<String>();

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
	System.out.print("control");
	System.out.print(this.mainFrame.epithelium.getUnitaryModel());
	}

	/**
	 * Updates Epilog's main Panel with the loaded configurations
	 * 
	 */
	private void loadConfigurations() {

		this.mainFrame.setEpithelium(this.mainFrame.epithelium);
		this.mainFrame.repaint();
		JPanel panel = gridSpecsPanel();

		this.mainFrame.gridSpecsPanel.remove(4);
		this.mainFrame.gridSpecsPanel.add(panel.getComponent(4), 4);
		this.mainFrame.gridSpecsPanel.getComponent(4).setEnabled(false);

		this.mainFrame.gridSpecsPanel.remove(3);
		this.mainFrame.gridSpecsPanel.add(panel.getComponent(3), 3);
		this.mainFrame.gridSpecsPanel.getComponent(3).setEnabled(false);
		this.mainFrame.gridSpecsPanel.remove(1);
		this.mainFrame.gridSpecsPanel.add(panel.getComponent(1), 1);
		this.mainFrame.gridSpecsPanel.getComponent(1).setEnabled(false);

		this.mainFrame.repaint();
		this.mainFrame.hexagonsPanel
				.paintComponent(this.mainFrame.hexagonsPanel.getGraphics());

		this.mainFrame.auxiliaryHexagonsPanel
				.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		TitledBorder titleInitialConditions;
		titleInitialConditions = BorderFactory.createTitledBorder("");
		this.mainFrame.auxiliaryHexagonsPanel.setBorder(titleInitialConditions);
	}

	/**
	 * Saves the epithelium model
	 * 
	 * @see createConfigFile
	 * 
	 */
	public void saveEpitheliumModel() {
		JFileChooser fc = new JFileChooser();

		if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			String newUserZipFilename = fc.getSelectedFile().getAbsolutePath();
			newUserZipFilename += (newUserZipFilename.endsWith(".zepi")?"":".zepi");
			
			try {
				// Create new Zepi temp directory
				File newZepiTmpDir = FileIO.createTempDirectory();

				String fileSBMLName = "generated.sbml";

				// On all cases we need to write the config file
				String configFile = newZepiTmpDir.getAbsolutePath()
						+ "/config.txt";
				PrintWriter outConfig = new PrintWriter(new FileWriter(
						configFile));
				writeConfigFile(outConfig);
				outConfig.close();

				File fNewSBML;
				if (this.mainFrame.epithelium.isNewEpithelium()) {
					// We might have an old SBML file
					File fOldSBML = new File(
							this.mainFrame.epithelium.getSBMLFilePath());
					if (fOldSBML.isFile()) {
						// There's an old SBML file!
						fNewSBML = FileIO.copyFile(fOldSBML,
								newZepiTmpDir.getAbsolutePath());
					} else {
						// Someone deleted my SBML :/ Export a new one
						fNewSBML = this.mainFrame.epithelium
								.writeModelToSBMLFile(newZepiTmpDir,
										fileSBMLName);
					}
				} else {
					File oldZepi = new File(mainFrame.epithelium.zepiFilename);
					if (oldZepi.isFile()) {
						// unzip zepi into a temporary directory
						File tmppath = FileIO
								.unzipZepiTmpDir(mainFrame.epithelium.zepiFilename);
						// gets oldSBML path from temporary directory
						File fOldSBML = FileIO.getSBMLfileInDir(tmppath);
						// copy old SBML to new Zepi temporary directory
						fNewSBML = FileIO.copyFile(fOldSBML,
								newZepiTmpDir.getAbsolutePath());
						// Deletes oldZepi temporary directory
						FileIO.deleteTempDirectory(tmppath);
					} else {
						// Someone deleted my SBML :/ Export a new one
						fNewSBML = this.mainFrame.epithelium
								.writeModelToSBMLFile(newZepiTmpDir,
										fileSBMLName);
					}
				}
				// TODO
				// if (mainFrame.epithelium.getComposedModel() != null) {
				// SBMLFormat sbmlFormat = new SBMLFormat();
				// ByteArrayOutputStream baos = new ByteArrayOutputStream();
				// sbmlFormat.export(mainFrame.epithelium.getComposedModel(),
				// baos);
				// }
				FileIO.zipTmpDir(newZepiTmpDir, newUserZipFilename);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Creates the configuration file to save with the epithelium model
	 * 
	 * @param out
	 *            config file (.txt)
	 * 
	 */
	private void writeConfigFile(PrintWriter out) {

		// TODO : Change PrintWriter to FileWriter - Tratamento de Excepcoes. o
		// file writer espera ate ter uma quantiodade grande de dados para
		// enviar tudo de uma vez. importante quando estamos numa ligacao remota

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

				for (int value = 1; value < node.getMax() + 1; value++) {

					out.write("IS "
							+ index
							+ " "
							+ node.getNodeID()
							+ " "
							+ this.mainFrame.epithelium.getUnitaryModel()
									.getNodeOrder().indexOf(node) + " : "
							+ value + " ( ");

					// int next = 0;
					int current = 0;
					boolean start = true;
					boolean ongoing = false;

					for (int instance = 0; instance < this.mainFrame.topology
							.getNumberInstances(); instance++) {
						if (this.mainFrame.epithelium.getGridValue(instance,
								node) == value) {

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

			}// ends for (NodeInfo node :
				// this.mainFrame.epithelium.getUnitaryModel().getNodeOrder())
		}// for (String key: mainFrame.epithelium.getInitialStateSet().keySet())

		out.write("\n");

		// Env Inputs

		index = -1;
		for (String key : mainFrame.epithelium.getInputsIntegrationSet()
				.keySet()) {
			mainFrame.epithelium.setSelectedInputSet(key);
			index = index + 1;

			out.write("IT " + index + " name : " + key + " [ ");

			for (NodeInfo node : this.mainFrame.epithelium.getUnitaryModel()
					.getNodeOrder()) {
				if (this.mainFrame.epithelium.isIntegrationComponent(node))
					out.write(this.mainFrame.epithelium.getUnitaryModel()
							.getNodeOrder().indexOf(node)
							+ " ");
			}

			out.write("]" + "\n");

			for (NodeInfo node : this.mainFrame.epithelium.getUnitaryModel()
					.getNodeOrder()) {

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

					// int next = 0;
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

	/**
	 * Retrieves the information of the config file and re-creates the
	 * perturbation.
	 * 
	 * @param node
	 *            component with the perturbation
	 * @param minValue
	 *            minimum value of the perturbation
	 * @param maxValue
	 *            maximum value of the perturbation
	 * @return resultingPerturbation component perturbation
	 * 
	 */
	private AbstractPerturbation setPerturbation(NodeInfo node, int minValue,
			int maxValue) {
		AbstractPerturbation resultingPerturbation = null;
		if (minValue == maxValue)
			resultingPerturbation = new FixedValuePerturbation(node, minValue);
		else
			resultingPerturbation = new RangePerturbation(node, minValue,
					maxValue);
		return resultingPerturbation;
	}

}
