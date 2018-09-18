package org.epilogtool.gui.tab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.TreePath;

import org.colomoto.biolqm.LogicalModel;
import org.colomoto.biolqm.NodeInfo;
import org.epilogtool.OptionStore;
import org.epilogtool.common.ObjectComparator;
import org.epilogtool.common.Txt;
import org.epilogtool.core.Epithelium;
import org.epilogtool.core.EpitheliumGrid;
import org.epilogtool.gui.EpiGUI.SimulationEpiClone;
import org.epilogtool.gui.EpiGUI.TabChangeNotifyProj;
import org.epilogtool.gui.color.ColorUtils;
import org.epilogtool.gui.dialog.EnumNodePercent;
import org.epilogtool.gui.dialog.EnumOrderNodes;
import org.epilogtool.gui.widgets.GridInformation;
import org.epilogtool.gui.widgets.JComboCheckBox;
import org.epilogtool.gui.widgets.VisualGridSimulation;
import org.epilogtool.io.ButtonFactory;
import org.epilogtool.io.FileIO;
import org.epilogtool.project.Project;
import org.epilogtool.project.Simulation;

public class EpiTabSimulation extends EpiTabTools {
	private static final long serialVersionUID = -1993376856622915249L;

	private VisualGridSimulation visualGridSimulation;
	private Simulation simulation;

	private JPanel jpRCenter;
	private JPanel jpLeftTop;
	private JPanel jpLeft;

	private GridInformation gridInformation;

	private JComboCheckBox jccbSBML;

	private Set<String> lModelVisibleComps;
	private Map<String, Boolean> mSelCheckboxes;

	private Map<String, JCheckBox> mNodeID2Checkbox;
	private Map<String, JButton> mNodeID2JBColor;

	private Set<String> nodesSelected;

	private SimulationEpiClone simEpiClone;

	private int iUserBurst;
	private int iCurrSimIter;
	private JLabel jlStep;
	private JLabel jlAttractor;
	private JButton jbRewind;
	private JButton jbBack;
	private JButton jbForward;
	private JButton jbFastFwr;
	private JButton jbRestart;

	private JButton jbSelectAll;
	private JButton jbDeselectAll;

	public EpiTabSimulation(Epithelium e, TreePath path, TabChangeNotifyProj tabChanged,
			SimulationEpiClone simEpiClone) {
		super(e, path, tabChanged);
		this.simEpiClone = simEpiClone;
	}

	/**
	 * Creates the InitialConditionsPanel, the first time the tab is created.
	 * 
	 */
	public void initialize() {

		this.center.setLayout(new BorderLayout());
		this.south.setLayout(new BorderLayout());

		this.iUserBurst = 30;
		this.iCurrSimIter = 0;

		this.mSelCheckboxes = new HashMap<String, Boolean>();
		this.mNodeID2Checkbox = new HashMap<String, JCheckBox>();
		this.mNodeID2JBColor = new HashMap<String, JButton>();

		this.simulation = new Simulation(this.epithelium.clone());
		this.gridInformation = new GridInformation(this.epithelium.getIntegrationFunctions());
		this.nodesSelected = new HashSet<String>();

		this.visualGridSimulation = new VisualGridSimulation(this.simulation.getGridAt(0), this.nodesSelected,
				this.gridInformation);

		this.center.add(this.visualGridSimulation, BorderLayout.CENTER);

		this.jpLeft = new JPanel(new BorderLayout());

		this.jpLeftTop = new JPanel();
		this.jpLeftTop.setLayout(new BoxLayout(this.jpLeftTop, BoxLayout.Y_AXIS));

		// South Panel

		// Iteration
		JPanel jpIteration = new JPanel(new GridBagLayout());
		jpIteration.setBorder(BorderFactory.createLineBorder(Color.GRAY));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 0;
		gbc.gridx = 0;
		gbc.ipadx = gbc.ipady = 5;
		gbc.anchor = GridBagConstraints.WEST;
		jpIteration.add(new JLabel("Iteration:"), gbc);
		gbc.gridx = 1;
		this.jlStep = new JLabel("" + this.iCurrSimIter);
		jpIteration.add(this.jlStep, gbc);
		gbc.gridy = 1;
		gbc.gridx = 0;
		gbc.gridwidth = 2;
		this.jlAttractor = new JLabel("");
		this.jlAttractor.setForeground(Color.RED);
		this.setGridGUIStable(false);
		jpIteration.add(this.jlAttractor, gbc);

		this.south.add(jpIteration, BorderLayout.CENTER);

		// Buttons

		JPanel jpButtons = new JPanel(new BorderLayout());
		jpButtons.setBorder(BorderFactory.createTitledBorder(""));
		jpButtons.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		JPanel jpButtonsC = new JPanel();
		jpButtons.add(jpButtonsC, BorderLayout.CENTER);

		this.jbRewind = ButtonFactory.getImageNoBorder("media_step_0.png");// media_rewind-26x24.png");
		this.jbRewind.setToolTipText(Txt.get("s_TAB_SIM_BACK_DESC"));
		this.jbRewind.setEnabled(false);
		this.jbRewind.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				simulationRewind();
			}
		});
		jpButtonsC.add(this.jbRewind);

		this.jbBack = ButtonFactory.getImageNoBorder("media_step_back-24x24.png");
		this.jbBack.setToolTipText(Txt.get("s_TAB_SIM_BACK1_DESC"));
		this.jbBack.setEnabled(false);
		this.jbBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				simulationStepBack();
			}
		});
		jpButtonsC.add(this.jbBack);

		this.jbForward = ButtonFactory.getImageNoBorder("media_step_forward-24x24.png");
		this.jbForward.setToolTipText(Txt.get("s_TAB_SIM_FWR1_DESC"));
		this.jbForward.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				simulationStepFwr();
			}
		});
		jpButtonsC.add(this.jbForward);

		JTextField jtSteps = new JTextField("" + this.iUserBurst);
		jtSteps.setToolTipText(Txt.get("s_TAB_SIM_BURST_DESC"));
		jtSteps.setColumns(3);
		jtSteps.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				JTextField jtf = (JTextField) e.getSource();
				try {
					iUserBurst = Integer.parseInt(jtf.getText());
					jtf.setBackground(Color.WHITE);
				} catch (NumberFormatException nfe) {
					jtf.setBackground(ColorUtils.LIGHT_RED);
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		jpButtonsC.add(jtSteps);

		this.jbFastFwr = ButtonFactory.getImageNoBorder("media_fast_forward-26x24.png");
		this.jbFastFwr.setToolTipText(Txt.get("s_TAB_SIM_FWR_DESC"));
		this.jbFastFwr.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				simulationFastFwr();
			}
		});
		jpButtonsC.add(this.jbFastFwr);

		JPanel jpButtonsR = new JPanel();
		JButton jbClone = ButtonFactory.getNoMargins(Txt.get("s_TAB_SIM_CLONE"));
		jbClone.setToolTipText(Txt.get("s_TAB_SIM_CLONE_DESC"));
		jbClone.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cloneEpiWithCurrGrid();
			}
		});
		jpButtonsR.add(jbClone);

		// Button to save an image from the simulated grid
		JButton jbPicture = ButtonFactory.getImageNoBorder("fotography-24x24.png");
		jbPicture.setToolTipText(Txt.get("s_TAB_SIM_SAVE"));
		jbPicture.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					saveEpiGrid2File(true);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		jpButtonsR.add(jbPicture);

		// Button to save all simulated grid images
		JButton jbSaveAll = ButtonFactory.getImageNoBorder("fotography-mult-24x24.png");
		jbSaveAll.setToolTipText(Txt.get("s_TAB_SIM_SAVE_ALL"));
		jbSaveAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					saveEpiGrid2File(false);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		jpButtonsR.add(jbSaveAll);

		// jpButtons.setPreferredSize(new
		// Dimension(jpButtons.getPreferredSize().width+110,
		// jpIteration.getPreferredSize().height));

		jpButtons.add(jpButtonsR, BorderLayout.LINE_END);
		this.south.add(jpButtons, BorderLayout.LINE_END);

		// ---------------------------------------------------------------------------
		// Model selection jcomboCheckBox

		List<LogicalModel> modelList = new ArrayList<LogicalModel>(this.epithelium.getEpitheliumGrid().getModelSet());
		JCheckBox[] items = new JCheckBox[modelList.size()];
		for (int i = 0; i < modelList.size(); i++) {
			items[i] = new JCheckBox(Project.getInstance().getProjectFeatures().getModelName(modelList.get(i)));
			items[i].setSelected(true);
		}
		this.jccbSBML = new JComboCheckBox(items);
		this.jpLeftTop.add(this.jccbSBML);
		this.jpLeftTop.setBorder(BorderFactory.createTitledBorder(Txt.get("s_MODEL_SELECT")));

		// ---------------------------------------------------------------------------
		// Select/Deselect active nodes Buttons

		this.jpLeft.add(this.jpLeftTop, BorderLayout.NORTH);

		// JButton select all
		JPanel rrTopSel = new JPanel(new FlowLayout());
		this.jbSelectAll = new JButton("Select all");
		this.jbSelectAll.setMargin(new Insets(0, 0, 0, 0));
		this.jbSelectAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (String nodeID : lModelVisibleComps) {
					if (mNodeID2Checkbox.containsKey(nodeID)) {
						mNodeID2Checkbox.get(nodeID).setSelected(true);
					}
					nodesSelected.add(nodeID);

				}
				visualGridSimulation.paintComponent(visualGridSimulation.getGraphics());
			}
		});

		rrTopSel.add(this.jbSelectAll);

		// JButton deselect all
		this.jbDeselectAll = new JButton("Deselect all");
		this.jbDeselectAll.setMargin(new Insets(0, 0, 0, 0));
		this.jbDeselectAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (String nodeID : lModelVisibleComps) {
					if (mNodeID2Checkbox.containsKey(nodeID)) {
						mNodeID2Checkbox.get(nodeID).setSelected(false);
					}
					nodesSelected.remove(nodeID);
				}
				visualGridSimulation.paintComponent(visualGridSimulation.getGraphics());
			}
		});

		rrTopSel.add(this.jbDeselectAll);

		// ---------------------------------------------------------------------------
		// Components Panel

		JPanel jpLeftCenter = new JPanel(new BorderLayout());
		jpLeftCenter.setBorder(BorderFactory.createTitledBorder("Components"));

		jpLeftCenter.add(rrTopSel, BorderLayout.NORTH);

		this.jpRCenter = new JPanel();
		this.jpRCenter.setLayout(new BoxLayout(jpRCenter, BoxLayout.Y_AXIS));

		JScrollPane jsLeftCenter = new JScrollPane(this.jpRCenter);
		jsLeftCenter.setBorder(BorderFactory.createEmptyBorder());
		jsLeftCenter.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.jccbSBML.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboCheckBox jccb = (JComboCheckBox) e.getSource();
				jccb.updateSelected();
				updateComponentList(jccb.getSelectedItems());
			}
		});

		jpLeftCenter.add(jsLeftCenter, BorderLayout.CENTER);
		this.jpLeft.add(jpLeftCenter, BorderLayout.CENTER);

		JPanel jpLeftAggreg = new JPanel(new BorderLayout());
		jpLeftAggreg.add(this.jpLeft, BorderLayout.LINE_START);
		jpLeftAggreg.add(this.gridInformation, BorderLayout.LINE_END);

		this.center.add(jpLeftAggreg, BorderLayout.LINE_START);
		updateComponentList(this.jccbSBML.getSelectedItems());
		this.isInitialized = true;

		// Restart

		JPanel jpRestart = new JPanel();
		jpRestart.setBorder(BorderFactory.createTitledBorder(""));
		jpRestart.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		// jpRestart.setPreferredSize(new Dimension(40, 40));

		this.jbRestart = ButtonFactory.getNoMargins(Txt.get("s_TAB_SIM_RESTART"));
		this.jbRestart.setToolTipText(Txt.get("s_TAB_SIM_RESTART_DESC"));
		this.jbRestart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				restartSimulationTab();
				jbRestart.setBackground(jbBack.getBackground());
			}
		});
		this.jbRestart.setPreferredSize(new Dimension(this.jbRestart.getPreferredSize().width + 30,
				jpIteration.getPreferredSize().height - 10));
		jpRestart.add(this.jbRestart);
		// jpButtons.add(jpRestart,BorderLayout.LINE_START);
		this.south.add(jpRestart, BorderLayout.LINE_START);

	}
	// ---------------------------------------------------------------------------
	// End initialize

	protected void restartSimulationTab() {

		this.simulation = new Simulation(this.epithelium.clone());
		this.simulationRewind();

		for (int i = 0; i < this.south.getComponentCount(); i++) {
			Component c = this.south.getComponent(i);
			if (c instanceof JTextPane) {
				this.south.remove(i);
				break;
			}
		}

		// TODO: Test integration functions; perturbations; initial conditions,
		// integration inputs; priorities; update scheme

		this.south.repaint();
		this.repaint();
		this.revalidate();

	}

	/**
	 * Creates the panel with the selection of the components to display.
	 * 
	 * @param sNodeIDs
	 *            : List with the nodes names to be written
	 * @param titleBorder
	 *            : String with the title of the panel
	 */
	private void setComponentTypeList(List<String> lNodes, String titleBorder, List<LogicalModel> listModels) {
		JPanel jpRRC = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 4, 0);
		jpRRC.setBorder(BorderFactory.createTitledBorder(titleBorder));
		// Collections.sort(nodeList, ObjectComparator.STRING); // orders the numbers
		int y = 0;

		String orderPref = (String) OptionStore.getOption("PrefsAlphaOrderNodes");

		if (orderPref != null && orderPref.equals(EnumOrderNodes.ALPHA.toString())) {
			lNodes = getAlphaOrderedNodes(lNodes);
		}

		for (String nodeId : lNodes) {
			for (LogicalModel m : listModels) {
				// if (m.getComponents().contains(node) &&
				// !this.epithelium.isIntegrationComponent(node)) { //Integration input are not
				// visible on the simulation
				NodeInfo node = Project.getInstance().getProjectFeatures().getNodeInfo(nodeId);
				if (m.getComponents().contains(node)) {
					this.lModelVisibleComps.add(node.getNodeID());

					this.getCompMiniPanel(jpRRC, gbc, y++, node);
					break;
				}
			}
		}
		this.jpRCenter.add(jpRRC);
	}

	private List<String> getAlphaOrderedNodes(List<String> lNodeID) {

		List<String> lOrderedNods = new ArrayList<String>();

		Collections.sort(lNodeID, ObjectComparator.STRING); // Orders alphabetically, not case-sensitive

		for (String nodeID : lNodeID) {
			lOrderedNods.add(nodeID);
			continue;
		}
		return lOrderedNods;
	}

	/**
	 * Updates components check selection list, once the selected model to display
	 * is changed.
	 * 
	 * @param modelNames
	 */
	private void updateComponentList(List<String> modelNames) {

		List<LogicalModel> lModels = new ArrayList<LogicalModel>();
		for (String modelName : modelNames) {
			lModels.add(Project.getInstance().getProjectFeatures().getModel(modelName));
		}

		this.lModelVisibleComps = new HashSet<String>();
		this.jpRCenter.removeAll();

		List<String> lInternal = new ArrayList<String>(
				Project.getInstance().getProjectFeatures().getModelsNodeIDs(lModels, false));
		List<String> lInputs = new ArrayList<String>(
				Project.getInstance().getProjectFeatures().getModelsNodeIDs(lModels, true));

		if ((lInternal.isEmpty()) & (lInputs.isEmpty())) {
			this.jbDeselectAll.setEnabled(false);
			this.jbSelectAll.setEnabled(false);
		} else {
			this.jbDeselectAll.setEnabled(true);
			this.jbSelectAll.setEnabled(true);
		}

		// for (int i = lInputs.size() - 1; i >= 0; i--) {
		// if (this.epithelium.isIntegrationComponent(lInputs.get(i))) {
		// lInputs.remove(i);
		// }
		// }

		updateSelectedNodes(lInternal, lInputs);

		if (!lInternal.isEmpty())
			this.setComponentTypeList(lInternal, "Internal", lModels);

		List<String> lIntegrationInputs = new ArrayList<String>();
		for (int i = lInputs.size() - 1; i >= 0; i--) {
			if (this.epithelium.isIntegrationComponent(lInputs.get(i))) {
				lIntegrationInputs.add(lInputs.get(i));
				lInputs.remove(i);
			}
		}
		if (!lInputs.isEmpty()) {
			this.setComponentTypeList(lInputs, "Positional inputs", lModels);
		}
		if (!lIntegrationInputs.isEmpty()) {
			this.setComponentTypeList(lIntegrationInputs, "Integration inputs", lModels);
		}

		visualGridSimulation.paintComponent(visualGridSimulation.getGraphics());
		this.jpRCenter.revalidate();
		this.jpRCenter.repaint();
	}

	private void updateSelectedNodes(List<String> lInternal, List<String> lInputs) {

		List<String> nodes = new ArrayList<String>();
		nodes.addAll(lInputs);
		nodes.addAll(lInternal);
		for (String nodeID : this.nodesSelected) {
			if (!nodes.contains(nodeID)) {
				this.mNodeID2Checkbox.get(nodeID).setSelected(false);
			}
		}

		for (String nodeID : this.mNodeID2Checkbox.keySet()) {
			if (!this.mNodeID2Checkbox.get(nodeID).isSelected()) {
				this.nodesSelected.remove(nodeID);
			}
		}
	}

	/**
	 * Creates the inner panel of each component (checkbox and color)
	 * 
	 * @param jp
	 * @param gbc
	 * @param y
	 * @param nodeID
	 */
	private void getCompMiniPanel(JPanel jp, GridBagConstraints gbc, int y, NodeInfo node) {
		String nodeID = node.getNodeID();

		gbc.gridy = y;
		gbc.anchor = GridBagConstraints.WEST;

		// ----------------------------------------------------------------------------
		gbc.gridx = 1;

		JButton jbColor = this.mNodeID2JBColor.get(nodeID);
		if (jbColor == null) {
			jbColor = new JButton();
			jbColor.setToolTipText(nodeID);
			jbColor.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setNewColor((JButton) e.getSource());
				}
			});
			this.mNodeID2JBColor.put(nodeID, jbColor);
		}
		jbColor.setBackground(Project.getInstance().getProjectFeatures().getNodeColor(nodeID));
		jp.add(jbColor, gbc);

		// ----------------------------------------------------------------------------
		gbc.gridx = 0;

		JCheckBox jcb = this.mNodeID2Checkbox.get(nodeID);

		if (jcb == null) {
			this.mSelCheckboxes.put(nodeID, false);
			// node percentage is the checkbox text

			jcb = new JCheckBox(nodeID);
			jcb.setToolTipText(nodeID);
			jcb.setSelected(this.mSelCheckboxes.get(nodeID));
			jcb.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JCheckBox jcb = (JCheckBox) e.getSource();
					String nodeID = jcb.getToolTipText();
					mSelCheckboxes.put(nodeID, jcb.isSelected());
					if (jcb.isSelected()) {
						nodesSelected.add(nodeID);
					} else {
						nodesSelected.remove(nodeID);
					}

					visualGridSimulation.paintComponent(visualGridSimulation.getGraphics());
				}
			});
			this.mNodeID2Checkbox.put(nodeID, jcb);
		}
		jp.add(jcb, gbc);

		String nodePercent = (String) OptionStore.getOption("PrefsNodePercent");
		if (nodePercent != null && nodePercent.equals(EnumNodePercent.YES.toString())) {
			gbc.gridx = 3;
			JLabel percentage = new JLabel(this.simulation.getGridAt(this.iCurrSimIter).getPercentage(nodeID));
			jp.add(percentage, gbc);
		}
	}

	/**
	 * Changes the color assigned to a node.
	 * 
	 * @param jb
	 */
	private void setNewColor(JButton jb) {
		String nodeID = jb.getToolTipText();
		Color newColor = JColorChooser.showDialog(jb, "Color chooser - " + nodeID, jb.getBackground());
		if (newColor != null && !newColor.equals(Project.getInstance().getProjectFeatures().getNodeColor(nodeID))) {
			jb.setBackground(newColor);
			Project.getInstance().getProjectFeatures().setNodeColor(nodeID, newColor);
			this.tabChanged.setEpiChanged();
			if (this.nodesSelected.contains(nodeID)) {
				// Paint only if NodeID is selected!!
				visualGridSimulation.paintComponent(visualGridSimulation.getGraphics());
			}
		}
	}

	public void saveEpiGrid2File(boolean single) throws IOException {
		// declare JFileChooser
		JFileChooser fileChooser = new JFileChooser();

		FileNameExtensionFilter filter = null;
		if (single)
			filter = new FileNameExtensionFilter("PNG files", "png");
		else
			filter = new FileNameExtensionFilter("ZIP files", "zip");

		fileChooser.setFileFilter(filter);
		if (Project.getInstance().getFilenamePEPS() != null) {
			fileChooser.setCurrentDirectory(new java.io.File(Project.getInstance().getFilenamePEPS()));
		}

		// let the user choose the destination file
		if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {

			// indicates whether the user still wants to save the picture
			boolean doExport = true;

			// indicates whether to override an already existing file
			boolean overrideExistingFile = false;

			// get destination file
			String filename = fileChooser.getSelectedFile().getAbsolutePath();
			if (single)
				filename += (filename.endsWith("." + "png") ? "" : "." + "png");
			else
				filename += (filename.endsWith("." + "zip") ? "" : "." + "zip");

			File destinationFile = new File(filename);

			// check if file already exists
			while (doExport && destinationFile.exists() && !overrideExistingFile) {
				// let the user decide whether to override the existing file
				overrideExistingFile = (JOptionPane.showConfirmDialog(this, "Replace file?", "Export settings",
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION);

				// let the user choose another file if the existing file shall not be overridden
				if (!overrideExistingFile) {
					if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
						// get new destination file
						filename = fileChooser.getSelectedFile().getAbsolutePath();

						if (single)
							filename += (filename.endsWith("." + "png") ? "" : "." + "png");
						else
							filename += (filename.endsWith("." + "zip") ? "" : "." + "zip");

						destinationFile = new File(filename);

					} else {
						// seems like the user does not want to export the settings any longer
						doExport = false;
					}
				}
			}

			if (doExport) {
				if (single)
					FileIO.writeEpitheliumGrid2File(filename, this.visualGridSimulation, "png");
				else {
					File temp = FileIO.createTempDirectory();
					for (int i = 0; i <= this.iCurrSimIter; i++) {
						String imageName = "image" + i + ".png";
						EpitheliumGrid grid = this.simulation.getGridAt(i);
						this.visualGridSimulation.setEpitheliumGrid(grid);
						String imageFile = temp + "/" + imageName;
						FileIO.writeEpitheliumGrid2File(imageFile, this.visualGridSimulation, "png");
					}
					FileIO.zipTmpDir(temp, filename);
					FileIO.deleteTempDirectory(temp);
				}
			}
		}
	}


	private void cloneEpiWithCurrGrid() {
		this.simEpiClone.cloneEpithelium(this.epithelium, this.simulation.getGridAt(this.iCurrSimIter));
	}

	private void simulationFastFwr() {
		EpitheliumGrid nextGrid = this.simulation.getGridAt(this.iCurrSimIter);
		for (int i = 0; i < this.iUserBurst; i++) {
			nextGrid = this.simulation.getGridAt(this.iCurrSimIter + 1);
			if (this.simulation.isStableAt(this.iCurrSimIter + 1)) {
				setGridGUIStable(true);
				break;
			} else {
				int len = this.simulation.getTerminalCycleLen();
				if (len > 0) {
					this.setGUITerminalCycle(len);
					// break;
				}
			}
			this.iCurrSimIter++;
			//TODO
			
		}
		this.visualGridSimulation.setEpitheliumGrid(nextGrid);
		this.jlStep.setText("" + this.iCurrSimIter);
		this.jbRewind.setEnabled(true);
		this.jbBack.setEnabled(true);
		String nodePercent = (String) OptionStore.getOption("PrefsNodePercent");

		if (nodePercent != null && nodePercent.equals(EnumNodePercent.YES.toString())) {
			nextGrid.updateNodeValueCounts();
		}
		this.updateComponentList(this.jccbSBML.getSelectedItems());
		// Re-Paint
		this.repaint();
	}

	private void simulationStepFwr() {
		EpitheliumGrid nextGrid = this.simulation.getGridAt(this.iCurrSimIter + 1);
		if (this.simulation.isStableAt(this.iCurrSimIter + 1)) {
			setGridGUIStable(true);
		} else {
			this.iCurrSimIter++;
			//TODO
//			this.simulation.updateCellularEvents();
			this.visualGridSimulation.setEpitheliumGrid(nextGrid);
			this.jlStep.setText("" + this.iCurrSimIter);
			this.setGUITerminalCycle(this.simulation.getTerminalCycleLen());
		}
		this.jbRewind.setEnabled(true);
		this.jbBack.setEnabled(true);
		String nodePercent = (String) OptionStore.getOption("PrefsNodePercent");
		if (nodePercent != null && nodePercent.equals(EnumNodePercent.YES.toString())) {
			nextGrid.updateNodeValueCounts();
		}
		this.updateComponentList(this.jccbSBML.getSelectedItems());
		// Re-Paint
		this.repaint();
	}

	private void simulationRewind() {
		this.iCurrSimIter = 0;
		this.jlStep.setText("" + this.iCurrSimIter);
		EpitheliumGrid firstGrid = this.simulation.getGridAt(this.iCurrSimIter);
		this.visualGridSimulation.setEpitheliumGrid(firstGrid);
		setGridGUIStable(false);
		this.jbRewind.setEnabled(false);
		this.jbBack.setEnabled(false);
		this.jbForward.setEnabled(true);
		this.jbFastFwr.setEnabled(true);
		String nodePercent = (String) OptionStore.getOption("PrefsNodePercent");
		if (nodePercent != null && nodePercent.equals(EnumNodePercent.YES.toString())) {
			firstGrid.updateNodeValueCounts();
		}

		this.updateComponentList(this.jccbSBML.getSelectedItems());
		// Re-Paint
		this.center.repaint();
		this.revalidate();
		this.repaint();
	}

	private void simulationStepBack() {
		if (this.iCurrSimIter == 0) {
			return;
		}
		EpitheliumGrid prevGrid = this.simulation.getGridAt(--this.iCurrSimIter);
		this.jlStep.setText("" + this.iCurrSimIter);
		this.visualGridSimulation.setEpitheliumGrid(prevGrid);
		setGridGUIStable(false);
		this.setGUITerminalCycle(this.simulation.getTerminalCycleLen());
		if (this.iCurrSimIter == 0) {
			this.jbRewind.setEnabled(false);
			this.jbBack.setEnabled(false);
		}
		this.jbForward.setEnabled(true);
		this.jbFastFwr.setEnabled(true);
		String nodePercent = (String) OptionStore.getOption("PrefsNodePercent");
		if (nodePercent != null && nodePercent.equals(EnumNodePercent.YES.toString())) {
			prevGrid.updateNodeValueCounts();
		}
		this.updateComponentList(this.jccbSBML.getSelectedItems());
		// Re-Paint
		this.repaint();
	}

	private void setGridGUIStable(boolean stable) {
		if (stable) {
			this.jlAttractor.setText(Txt.get("s_TAB_SIM_STABLE"));
			this.jbForward.setEnabled(false);
			this.jbFastFwr.setEnabled(false);
		} else {
			this.jlAttractor.setText("           ");
		}
	}

	private void setGUITerminalCycle(int len) {
		if (len > 0) {
			this.jlAttractor.setText(Txt.get("s_TAB_SIM_CYCLE") + " (len=" + len + ")");
		} else {
			this.jlAttractor.setText("           ");
		}
	}

	/**
	 * Updates the list of components to present. If a positional input is now an
	 * integration input then it is no longer set to show in the components panel.
	 * On the other hand if an integration input is change to a positional input it
	 * will appear in the components list.
	 * 
	 * @param node
	 *            node to the removed/added in String format.
	 * @param b
	 *            if b is true, then it should be added to the list, otherwise
	 *            removed.
	 */
	public void updatelPresentComps(String node, boolean b) {
		if (b)
			this.lModelVisibleComps.add(node);
		else
			this.lModelVisibleComps.remove(node);
	}

	@Override
	public String getName() {
		return EpiTab.TOOL_SIMULATION;
	}

	@Override
	public boolean canClose() {
		return true;
	}

	private boolean hasChangedEpithelium() {

		return !this.epithelium.equals(this.simulation.getEpithelium());
	}

	@Override
	public void applyChange() {
		if (this.hasChangedEpithelium()) {
			JTextPane jtp = new JTextPane();
			jtp.setContentType("text/html");
			String color = ColorUtils.getColorCode(this.south.getBackground());
			color = "#FF9A99";
			jtp.setText("<html><body style=\"background-color:" + color + "\">" + "<font color=\"#000000\">"
					+ "New Epithelium definitions detected!!<br/>"
					+ "Continue current simulation with old definitions, or press <b>Restart</b> to apply the new ones."
					+ "</font></body></html>");
			jtp.setBorder(javax.swing.BorderFactory.createEmptyBorder());
			jtp.setHighlighter(null);
			// this.jbRestart.setBackground(Color.getHSBColor(226, 68, 93));
			this.jbRestart.setBackground(new Color(255, 154, 153));
			this.south.add(jtp, BorderLayout.NORTH);
		} else {
			for (int i = 0; i < this.south.getComponentCount(); i++) {
				Component c = this.south.getComponent(i);
				if (c instanceof JTextPane) {
					this.south.remove(i);
					break;
				}
			}
		}

		// New (potential) model list -> Update JComboCheckBox
		// and (potential) new node value counts
		this.epithelium.getEpitheliumGrid().updateGrid();
		List<String> newModelList = new ArrayList<String>();
		for (LogicalModel m : this.epithelium.getEpitheliumGrid().getModelSet()) {
			newModelList.add(Project.getInstance().getProjectFeatures().getModelName(m));
		}
		this.jccbSBML.updateItemList(newModelList);

		this.updateComponentList(this.jccbSBML.getSelectedItems());
		this.south.repaint();
		this.repaint();
		this.revalidate();
	}

}
