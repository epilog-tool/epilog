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
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.colomoto.logicalmodel.NodeInfo;

import pt.igc.nmd.epilog.gui.MainFrame;

public class PerturbationsPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5470169327482303234L;

	private SphericalEpithelium epithelium;
	private Topology topology;
	private MainFrame mainPanel;

	public PerturbationsPanel(SphericalEpithelium epithelium,
			Topology topology, MainFrame mainPanel) {

		this.mainPanel = mainPanel;
		this.topology = topology;
		this.epithelium = epithelium;

		init();
	}

	public void init() {

		JPanel perturbationsPanel = new JPanel();
		JPanel prioritiesPanel = new JPanel();
		Color backgroundColor = mainPanel.getBackground();
		
		
		

		add(pertubationsPanelSetup(perturbationsPanel));
		add(prioritiesPanelSetup(prioritiesPanel));

	}
	
	private JPanel prioritiesPanelSetup(JPanel panel) {

		FlowLayout layout = new FlowLayout();
		layout.setAlignment(FlowLayout.LEFT);

		panel.setLayout(layout);
		
		
		panel.setBorder(javax.swing.BorderFactory
				.createEmptyBorder());
		TitledBorder titleInitialConditions;
		titleInitialConditions = BorderFactory
				.createTitledBorder("Priorities");
		panel.setBorder(titleInitialConditions);
		
		
		JButton buttonNewPriority = new JButton("New");
		
		buttonNewPriority.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// TO DO: Fill has to check for each component individually if
				// they have non zero expression neighbors. The fact that one
				// component closes doesn't mean that the others do
			}
		});
		panel.add(buttonNewPriority);
		return panel;
	}
	
	private JPanel pertubationsPanelSetup(JPanel panel) {

		
		FlowLayout layout = new FlowLayout();
		layout.setAlignment(FlowLayout.LEFT);

		panel.setLayout(layout);

		panel.setBorder(javax.swing.BorderFactory
				.createEmptyBorder());
		TitledBorder titleInitialConditions;
		titleInitialConditions = BorderFactory
				.createTitledBorder("Perturbations");
		panel.setBorder(titleInitialConditions);
		
		JButton buttonMarkAll = new JButton("Apply All");
		JButton buttonClearAll = new JButton("Clear All");
		JButton buttonSave = new JButton("Save");
		JButton buttonNewPerturbation = new JButton("New");
		
		JComboBox perturbations = new JComboBox();
		

		buttonNewPerturbation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

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
		
		
		
		
		
		
		panel.add(perturbations);
		panel.add(buttonNewPerturbation);
		panel.add(buttonMarkAll);
		panel.add(buttonClearAll);
		panel.add(buttonSave);
		return panel;
	}

//	protected void clearAllPerturbations() {
//
//		for (int instance = 0; instance < topology.getNumberInstances(); instance++) {
//			// epithelium.setPerturbedInstance(instance, false);
//		}
//	}


//	protected void resetMinAndMaxCombo(JPanel line1) {
//		line1.remove(3);
//		line1.remove(2);
//		JComboBox perturbedExpressionMin = getperturbedExpressionCombo();
//		JComboBox perturbedExpressionMax = getperturbedExpressionCombo();
//
//		perturbedExpressionMax.setSelectedItem(perturbedExpressionMax
//				.getItemCount() - 1);
//
//		line1.add(perturbedExpressionMin);
//		line1.add(perturbedExpressionMax);
//		perturbedExpressionMin.setPreferredSize(new Dimension(60, 24));
//		perturbedExpressionMax.setPreferredSize(new Dimension(60, 24));
//		line1.repaint();
//		line1.revalidate();
//
//	}
//
//	private JComboBox getperturbedExpressionCombo() {
//		JComboBox perturbedExpressionCombo = new JComboBox();
//		for (int i = 0; i <= selectedPerturbedComponent.getMax(); i++)
//			perturbedExpressionCombo.addItem(i);
//		return perturbedExpressionCombo;
//	}
	
	
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

	public void clearAllCells() {
		// adicionar try catch para textFx e fy
	}

	public void markAllCells() {

		// clearAllCells(cells);
		for (int i = 0; i < topology.getWidth(); i++) {
			for (int j = 0; j < topology.getHeight(); j++) {

			}
		}

	}

}
