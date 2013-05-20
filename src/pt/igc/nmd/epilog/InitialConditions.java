package pt.igc.nmd.epilog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;

import pt.igc.nmd.epilog.gui.ColorButton;
import pt.igc.nmd.epilog.gui.MainPanel;

public class InitialConditions extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5470169327482303234L;

	private SphericalEpithelium epithelium;
	private Topology topology;
	private MainPanel mainPanel;

	private Hashtable<JComboBox, Integer> Jcombo2Node;
	private Hashtable<JCheckBox, Integer> Jcheck2Node;

	private ColorButton[] node2Color;
	private JComboBox[] node2Jcombo;
	private JCheckBox[] node2Jcheck;
	private JComboBox[] initialStatePerComponent;
	private JCheckBox nodeBox[];

	private JPanel auxiliaryPanel[];
	private JPanel optionsPanel;

	private List<ColorButton> colorChooser;

	public InitialConditions(SphericalEpithelium epithelium, Topology topology,
			MainPanel mainPanel) {

		this.mainPanel = mainPanel;
		this.topology = topology;
		this.epithelium = epithelium;

		init();
	}

	public void init() {

		Color backgroundColor = mainPanel.getBackground();

		FlowLayout layout = new FlowLayout();
		layout.setAlignment(FlowLayout.LEFT);

		setLayout(layout);

		LogicalModel unitaryModel = mainPanel.getEpithelium().getUnitaryModel();

		if (unitaryModel != null) {
			List<NodeInfo> listNodes = epithelium.getUnitaryModel()
					.getNodeOrder();

			auxiliaryPanel = new JPanel[listNodes.size()];

			nodeBox = new JCheckBox[listNodes.size()];

			node2Jcheck = new JCheckBox[listNodes.size()];
			node2Color = new ColorButton[listNodes.size()];
			node2Jcombo = new JComboBox[listNodes.size()];

			initialStatePerComponent = new JComboBox[listNodes.size()];

			Jcheck2Node = new Hashtable<JCheckBox, Integer>();
			Jcombo2Node = new Hashtable<JComboBox, Integer>();

			colorChooser = new ArrayList<ColorButton>();

			for (int i = 0; i < listNodes.size(); i++) {

				if (listNodes.get(i).isInput())
					colorChooser.add(new ColorButton(mainPanel.componentsPanel,
							listNodes.get(i)));

				if (!listNodes.get(i).isInput()) {

					auxiliaryPanel[i] = new JPanel();
					auxiliaryPanel[i].setLayout(layout);
					auxiliaryPanel[i].setBackground(backgroundColor);
					auxiliaryPanel[i].setPreferredSize(new Dimension(180, 30));

					nodeBox[i] = new JCheckBox(listNodes.get(i).getNodeID());
					nodeBox[i].setBackground(backgroundColor);
					nodeBox[i].setPreferredSize(new Dimension(60, 25));
					nodeBox[i].setToolTipText(listNodes.get(i).getNodeID());

					Jcheck2Node.put(nodeBox[i], i);
					node2Jcheck[i] = nodeBox[i];

					nodeBox[i].addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent arg0) {
							JCheckBox src = (JCheckBox) arg0.getSource();
							setComponentDisplay(Jcheck2Node.get(src),
									src.isSelected());
							fillHexagons();
						}
					});

					initialStatePerComponent[i] = new JComboBox();
					initialStatePerComponent[i].setPreferredSize(new Dimension(
							60, 25));
					for (int maxValue = 0; maxValue < listNodes.get(i).getMax() + 1; maxValue++) {
						initialStatePerComponent[i].addItem(maxValue);
					}
					Jcombo2Node.put(initialStatePerComponent[i], i);
					node2Jcombo[i] = initialStatePerComponent[i];

					epithelium.setInitialState(listNodes.get(Jcombo2Node
							.get(initialStatePerComponent[i])), (byte) 0);

					initialStatePerComponent[i]
							.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent arg0) {
									JComboBox src = (JComboBox) arg0
											.getSource();
									fireInitialStateChange(src);
								}
							});

					colorChooser.add(new ColorButton(mainPanel.componentsPanel,
							listNodes.get(i)));
					colorChooser.get(i).setBackground(
							epithelium.getColor(listNodes.get(i)));
					colorChooser.get(i).setPreferredSize(new Dimension(20, 25));

					node2Color[i] = colorChooser.get(i);

					auxiliaryPanel[i].add(nodeBox[i]);
					auxiliaryPanel[i].add(initialStatePerComponent[i]);
					auxiliaryPanel[i].add(colorChooser.get(i));
					add(auxiliaryPanel[i]);
				}
			}
			optionsPanel = GraphicToolsPanel();

		}

	}

	private void fireInitialStateChange(JComboBox combo) {
		epithelium.setInitialState(mainPanel.getEpithelium().getUnitaryModel()
				.getNodeOrder().get(Jcombo2Node.get(combo)),
				((Integer) combo.getSelectedItem()).byteValue());
	}

	public void setComponentDisplay(int i, boolean b) {
		mainPanel.getEpithelium().setDefinitionsComponentDisplay(i, b);
	}

	public void fillHexagons() {
		mainPanel.fillHexagons();
	}

	protected void setInitialSetupHasChanged(boolean b) {
		mainPanel.setInitialSetupHasChanged(b);
	}

	private JPanel GraphicToolsPanel() {

		FlowLayout layout = new FlowLayout();
		layout.setAlignment(FlowLayout.LEFT);

		setLayout(layout);

		JButton buttonMarkAll = new JButton("Apply All");
		JButton buttonClearAll = new JButton("Clear All");
		JButton buttonSave = new JButton("Save");
		JButton buttonFill = new JButton("Fill");

		buttonFill.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setFill(true);
				fill();

				// TO DO: Fill has to check for each component individually if
				// they have non zero expression neighbors. The fact that one
				// component closes doesn't mean that the others do
			}
		});

		buttonMarkAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				markAllCells();

			}
		});

		buttonClearAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearAllCells();

			}
		});

		buttonSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save();

			}
		});

		add(buttonMarkAll);
		add(buttonClearAll);
		add(buttonFill);
		add(buttonSave);
		return this;
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
				if (mainPanel.getEpithelium().isNewEpithelium()) {
					unitarySBML = mainPanel.getEpithelium().getSBMLFilePath();
				} else {
					unitarySBML = mainPanel.getEpithelium().getSBMLLoadPath();

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

				// File bundle = new File(fc
				// .getSelectedFile().getAbsolutePath());
				// FileOutputStream stream = new FileOutputStream(bundle);
				// ZipOutputStream zos = new ZipOutputStream(stream);
				// ZipEntry ze = null;
				//
				// ze = new ZipEntry("config.txt");
				// ze.setSize((long) out.toString().getBytes().length);
				// zos.setLevel(9);
				// zos.putNextEntry(ze);
				// zos.write(out.toString().getBytes(), 0,
				// out.toString().getBytes().length);
				// zos.closeEntry();
				//
				// File file = mainPanel.getEpithelium().getSBMLFile();
				//
				// ze = new
				// ZipEntry(mainPanel.getEpithelium().getSBMLFilename());
				// ze.setSize((long) sbmlFormat.toString().getBytes().length);
				// zos.setLevel(9);
				// zos.putNextEntry(ze);
				// zos.write(file.toString().getBytes(), 0,
				// file.toString().getBytes().length);
				// zos.closeEntry();
				//
				// zos.finish();
				// zos.close();

			} catch (IOException e) {
				e.printStackTrace();
			}

	}

	private void createConfigFile(PrintWriter out) {

		// TODO : Change PrintWriter to FileWriter - Tratamento de Excepções. o
		// file writer espera ate ter uma quantiodade grande de dados para
		// enviar tudo de uma vez. importante quando estamos numa ligaão remota

		Topology topology = mainPanel.getTopology();
		SphericalEpithelium epithelium = mainPanel.getEpithelium();

		// SBML Filename
		out.write("SN " + epithelium.getSBMLFilename() + "\n");

		// Grid Dimensions
		out.write("GD " + topology.getWidth() + "," + topology.getHeight()
				+ "\n");

		out.write("\n");
		// Roll-Over option
		out.write("RL " + topology.getRollOver() + "\n");

		out.write("\n");
		// InitialState
		for (NodeInfo node : epithelium.getUnitaryModel().getNodeOrder()) {
			if (!epithelium.isIntegrationComponent(node)) {
				for (int value = 1; value < node.getMax() + 1; value++) {
					out.write("IC "
							+ node.getNodeID()
							+ " "
							+ epithelium.getUnitaryModel().getNodeOrder()
									.indexOf(node) + " : " + value + " ( ");
					int previous = 0;
					int inDash = 0;
					for (int instance = 0; instance < topology
							.getNumberInstances(); instance++) {
						if (epithelium.getGridValue(instance, node) == value) {
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
								if (instance == topology.getNumberInstances() - 1
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
		for (NodeInfo node : epithelium.getUnitaryModel().getNodeOrder()) {
			if (epithelium.isIntegrationComponent(node)) {

				for (byte value = 1; value < node.getMax() + 1; value++) {
					out.write("IT "
							+ node.getNodeID()
							+ " "
							+ epithelium.getUnitaryModel().getNodeOrder()
									.indexOf(node) + " : " + value + " : "
							+ epithelium.getIntegrationFunction(node, value)
							+ "\n");
				}

			}

		}

		out.write("\n");
		// Colors

		for (NodeInfo node : epithelium.getUnitaryModel().getNodeOrder()) {
			out.write("CL " + node.getNodeID() + " "
					+ epithelium.getUnitaryModel().getNodeOrder().indexOf(node)
					+ " : " + epithelium.getColor(node).getRGB() + "\n");
		}

		out.write("\n");
		// Perturbations

		// Priorities

	}

	protected void setFill(boolean b) {
		// this.fill = b;
	}

	public void fill() {

		// MapPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		// List[] neiList = new List[];
		// if (i < topology.getWidth()
		// && j < topology.getHeight()
		// && i >= 0 && j >= 0) {
		// color = Color();
		// int instance = topology.coords2Instance(
		// i, j);
		//
		// while (neiList!=[]){
		// for (int h : topology.groupNeighbors(
		// instance, 1)) {
		//
		// int m = topology.instance2i(h,
		// topology.getWidth());
		// int n = topology.instance2j(h,
		// topology.getWidth());
		//
		// if (Color(m,n)=color.white){
		// MapPanel.drawHexagon(m, n, MapPanel.getGraphics(),
		// color);
		//
		// }
		// }
		//
		// setFill(false);
		//
		// }
		// }
	}

	public void clearAllCells() {
		// adicionar try catch para textFx e fy
		epithelium.initializeGrid();
		fillHexagons();
	}

	public void markAllCells() {

		// clearAllCells(cells);
		for (int i = 0; i < topology.getWidth(); i++) {
			for (int j = 0; j < topology.getHeight(); j++) {
				epithelium.setInitialState(i, j);
				// MapPanel.drawHexagon(i, j, MapPanel.getGraphics(), Color());
				
			}
		}
		fillHexagons();
	}

}
