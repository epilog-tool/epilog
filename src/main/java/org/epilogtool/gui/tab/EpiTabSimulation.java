package org.epilogtool.gui.tab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.tree.TreePath;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;
import org.epilogtool.common.ObjectComparator;
import org.epilogtool.core.Epithelium;
import org.epilogtool.core.EpitheliumGrid;
import org.epilogtool.gui.EpiGUI.ProjectChangedInTab;
import org.epilogtool.gui.EpiGUI.SimulationEpiClone;
import org.epilogtool.gui.color.ColorUtils;
import org.epilogtool.gui.widgets.GridInformation;
import org.epilogtool.gui.widgets.JComboCheckBox;
import org.epilogtool.gui.widgets.VisualGridSimulation;
import org.epilogtool.io.ButtonFactory;
import org.epilogtool.io.EpilogFileFilter;
import org.epilogtool.io.FileIO;
import org.epilogtool.project.ProjectFeatures;
import org.epilogtool.project.Simulation;

public class EpiTabSimulation extends EpiTab {
	private static final long serialVersionUID = 1394895739386499680L;

	private VisualGridSimulation visualGridSimulation;
	private Simulation simulation;
	private Map<String, Boolean> mSelCheckboxes;
	private Map<String, JCheckBox> mNodeID2Checkbox;
	private ProjectFeatures projectFeatures;
	private List<String> lPresentComps;
	private List<String> lCompON;
	private Map<JButton, String> colorButton2Node;
	private SimulationEpiClone simEpiClone;

	private int iUserBurst;
	private int iCurrSimIter;
	private JLabel jlStep;
	private JLabel jlStable;
	private JButton jbRewind;
	private JButton jbBack;
	private JButton jbForward;
	private JButton jbFastFwr;

	private JPanel jpRight;
	private GridInformation lRight;
	private JPanel lLeft;
	private JPanel jpRRCenter;
	private JComboCheckBox jccb;

	public EpiTabSimulation(Epithelium e, TreePath path,
			ProjectChangedInTab projChanged, ProjectFeatures projectFeatures,
			SimulationEpiClone simEpiClone) {
		super(e, path, projChanged);
		this.projectFeatures = projectFeatures;
		this.simEpiClone = simEpiClone;
	}

	public void initialize() {
		setLayout(new BorderLayout());

		this.iUserBurst = 30;
		this.iCurrSimIter = 0;
		Epithelium clonedEpi = this.epithelium.clone();
		this.simulation = new Simulation(clonedEpi);
		this.jpRight = new JPanel(new BorderLayout());
		this.add(this.jpRight, BorderLayout.CENTER);

		this.lCompON = new ArrayList<String>();
		this.mSelCheckboxes = new HashMap<String, Boolean>();
		this.mNodeID2Checkbox = new HashMap<String, JCheckBox>();
		this.colorButton2Node = new HashMap<JButton, String>();
		for (LogicalModel m : this.epithelium.getEpitheliumGrid().getModelSet()) {
			for (NodeInfo node : m.getNodeOrder()) {
				this.mSelCheckboxes.put(node.getNodeID(), false);
			}
		}
		this.lRight = new GridInformation(
				this.epithelium.getIntegrationFunctions(), this.projectFeatures);

		this.visualGridSimulation = new VisualGridSimulation(
				clonedEpi.getEpitheliumGrid(),
				this.epithelium.getComponentFeatures(), this.lCompON,
				this.lRight);
		this.jpRight.add(this.visualGridSimulation, BorderLayout.CENTER);

		JPanel jpButtons = new JPanel(new BorderLayout());
		JPanel jpButtonsC = new JPanel();
		jpButtons.add(jpButtonsC, BorderLayout.CENTER);

		this.jbRewind = ButtonFactory
				.getImageNoBorder("media_rewind-26x24.png");
		this.jbRewind
				.setToolTipText("Go back to the beginning of the simulation");
		this.jbRewind.setEnabled(false);
		this.jbRewind.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				simulationRewind();
			}
		});
		jpButtonsC.add(this.jbRewind);
		this.jbBack = ButtonFactory
				.getImageNoBorder("media_step_back-24x24.png");
		this.jbBack.setToolTipText("Go back one step");
		this.jbBack.setEnabled(false);
		this.jbBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				simulationStepBack();
			}
		});
		jpButtonsC.add(this.jbBack);
		this.jbForward = ButtonFactory
				.getImageNoBorder("media_step_forward-24x24.png");
		this.jbForward.setToolTipText("Go forward one step");
		this.jbForward.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				simulationStepFwr();
			}
		});
		jpButtonsC.add(this.jbForward);
		JTextField jtSteps = new JTextField("" + this.iUserBurst);
		jtSteps.setToolTipText("Define the number of steps of a burst");
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
		this.jbFastFwr = ButtonFactory
				.getImageNoBorder("media_fast_forward-26x24.png");
		this.jbFastFwr.setToolTipText("Go forward a burst of 'n' steps");
		this.jbFastFwr.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				simulationFastFwr();
			}
		});
		jpButtonsC.add(this.jbFastFwr);

		JPanel jpButtonsR = new JPanel();
		JButton jbClone = ButtonFactory.getNoMargins("Clone");
		jbClone.setToolTipText("Create a new Epithelium with initial conditions as the current grid");
		jbClone.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cloneEpiWithCurrGrid();
			}
		});
		jpButtonsR.add(jbClone);

		// Button to save an image from the simulated grid
		JButton jbPicture = ButtonFactory
				.getImageNoBorder("fotography-24x24.png");
		jbPicture.setToolTipText("Save the image of the current grid to file");
		jbPicture.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveEpiGrid2File();
			}
		});
		jpButtonsR.add(jbPicture);

		// Button to save all simulated grid images
		JButton jbSaveAll = ButtonFactory
				.getImageNoBorder("fotography-mult-31x24.png");
		jbSaveAll
				.setToolTipText("Save all the simulation grids into different files");
		jbSaveAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveAllEpiGrid2File();
			}
		});

		jpButtonsR.add(jbSaveAll);

		jpButtons.add(jpButtonsR, BorderLayout.LINE_END);

		JPanel jpButtonsL = new JPanel();
		this.jlStep = new JLabel("Iteration: " + this.iCurrSimIter);
		jpButtonsL.add(this.jlStep);
		this.jlStable = new JLabel("Stable!");
		this.jlStable.setForeground(Color.RED);
		this.setGridGUIStable(false);
		jpButtonsL.add(this.jlStable);

		jpButtons.add(jpButtonsL, BorderLayout.LINE_START);
		this.jpRight.add(jpButtons, BorderLayout.SOUTH);

		this.lLeft = new JPanel(new BorderLayout());

		JPanel rrTop = new JPanel();
		rrTop.setLayout(new BoxLayout(rrTop, BoxLayout.Y_AXIS));

		// Model combobox
		List<LogicalModel> modelList = new ArrayList<LogicalModel>(
				this.epithelium.getEpitheliumGrid().getModelSet());
		JCheckBox[] items = new JCheckBox[modelList.size()];
		for (int i = 0; i < modelList.size(); i++) {
			items[i] = new JCheckBox(
					this.projectFeatures.getModelName(modelList.get(i)));
			items[i].setSelected(false);
		}
		this.jccb = new JComboCheckBox(items);
		rrTop.add(this.jccb);

		// Select/Deselect buttons
		JPanel rrTopSel = new JPanel(new FlowLayout());
		JButton jbSelectAll = new JButton("SelectAll");
		jbSelectAll.setMargin(new Insets(0, 0, 0, 0));
		jbSelectAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (String nodeID : lPresentComps) {
					if (mNodeID2Checkbox.containsKey(nodeID)) {
						mNodeID2Checkbox.get(nodeID).setSelected(true);
					}
					mSelCheckboxes.put(nodeID, true);
					if (!lCompON.contains(nodeID))
						lCompON.add(nodeID);
				}
				visualGridSimulation.paintComponent(visualGridSimulation
						.getGraphics());
			}
		});
		rrTopSel.add(jbSelectAll);
		JButton jbDeselectAll = new JButton("DeselectAll");
		jbDeselectAll.setMargin(new Insets(0, 0, 0, 0));
		jbDeselectAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (String nodeID : lPresentComps) {
					if (mNodeID2Checkbox.containsKey(nodeID)) {
						mNodeID2Checkbox.get(nodeID).setSelected(false);
					}
					mSelCheckboxes.put(nodeID, false);
					lCompON.remove(nodeID);
				}
				visualGridSimulation.paintComponent(visualGridSimulation
						.getGraphics());
			}
		});
		rrTopSel.add(jbDeselectAll);
		rrTop.add(rrTopSel);

		rrTop.setBorder(BorderFactory.createTitledBorder("Display options"));
		this.lLeft.add(rrTop, BorderLayout.NORTH);

		this.jpRRCenter = new JPanel();
		this.jpRRCenter.setLayout(new BoxLayout(jpRRCenter, BoxLayout.Y_AXIS));
		JScrollPane jscroll = new JScrollPane(this.jpRRCenter);
		jscroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.lLeft.add(jscroll, BorderLayout.CENTER);
		this.jccb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboCheckBox jccb = (JComboCheckBox) e.getSource();
				jccb.updateSelected();
				updateComponentList(jccb.getSelectedItems());
			}
		});

		JPanel jpLeftAggreg = new JPanel(new BorderLayout());
		jpLeftAggreg.add(this.lLeft, BorderLayout.LINE_START);
		jpLeftAggreg.add(this.lRight, BorderLayout.LINE_END);

		this.add(jpLeftAggreg, BorderLayout.LINE_START);
		updateComponentList(this.jccb.getSelectedItems());
		this.isInitialized = true;
	}

	/**
	 * 
	 */
	// get current simulation step
	private void saveEpiGrid2File() {
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(new EpilogFileFilter("png"));
		if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			String file = fc.getSelectedFile().getAbsolutePath();
			String ext = "PNG";
			file += (file.endsWith(ext) ? "" : "." + ext);
			FileIO.writeEpitheliumGrid2File(file, this.visualGridSimulation,
					ext);
		}
	}

	// get all the simulation steps
	private void saveAllEpiGrid2File() {
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(new EpilogFileFilter("png"));
		if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			String file = fc.getSelectedFile().getAbsolutePath();
			String ext = "PNG";
			file += (file.endsWith(ext) ? "" : "." + ext);
			for (int i = 0; i <= this.iCurrSimIter; i++) {
				String file_name = file.replace(".", "_" + i + ".");
				EpitheliumGrid grid = this.simulation.getGridAt(i);
				this.visualGridSimulation.setEpitheliumGrid(grid);
				FileIO.writeEpitheliumGrid2File(file_name,
						this.visualGridSimulation, ext);
			}
		}
	}

	private void cloneEpiWithCurrGrid() {
		this.simEpiClone.cloneEpithelium(this.epithelium,
				this.simulation.getGridAt(this.iCurrSimIter));
	}

	private void simulationRewind() {
		this.iCurrSimIter = 0;
		this.jlStep.setText("Iteration: " + this.iCurrSimIter);
		EpitheliumGrid firstGrid = this.simulation.getGridAt(this.iCurrSimIter);
		this.visualGridSimulation.setEpitheliumGrid(firstGrid);
		setGridGUIStable(false);
		this.jbRewind.setEnabled(false);
		this.jbBack.setEnabled(false);
		this.jbForward.setEnabled(true);
		this.jbFastFwr.setEnabled(true);
		// Re-Paint
		this.repaint();
	}

	private void simulationStepBack() {
		if (this.iCurrSimIter == 0) {
			return;
		}
		EpitheliumGrid prevGrid = this.simulation
				.getGridAt(--this.iCurrSimIter);
		this.jlStep.setText("Iteration: " + this.iCurrSimIter);
		this.visualGridSimulation.setEpitheliumGrid(prevGrid);
		setGridGUIStable(false);
		if (this.iCurrSimIter == 0) {
			this.jbRewind.setEnabled(false);
			this.jbBack.setEnabled(false);
		}
		this.jbForward.setEnabled(true);
		this.jbFastFwr.setEnabled(true);
		// Re-Paint
		this.repaint();
	}

	private void setGridGUIStable(boolean stable) {
		if (stable) {
			this.jlStable.setText("Stable!");
			this.jbForward.setEnabled(false);
			this.jbFastFwr.setEnabled(false);
		} else {
			this.jlStable.setText("           ");
		}
	}

	private void simulationStepFwr() {
		EpitheliumGrid nextGrid = this.simulation
				.getGridAt(this.iCurrSimIter + 1);
		if (this.simulation.isStableAt(this.iCurrSimIter + 1)) {
			setGridGUIStable(true);
		} else {
			this.iCurrSimIter++;
			this.visualGridSimulation.setEpitheliumGrid(nextGrid);
			this.jlStep.setText("Iteration: " + this.iCurrSimIter);
		}
		this.jbRewind.setEnabled(true);
		this.jbBack.setEnabled(true);
		// Re-Paint
		this.repaint();
	}

	private void simulationFastFwr() {
		EpitheliumGrid nextGrid = this.simulation.getGridAt(this.iCurrSimIter);
		for (int i = 0; i < this.iUserBurst; i++) {
			nextGrid = this.simulation.getGridAt(this.iCurrSimIter + 1);
			if (this.simulation.isStableAt(this.iCurrSimIter + 1)) {
				setGridGUIStable(true);
				break;
			}
			this.iCurrSimIter++;
		}
		this.visualGridSimulation.setEpitheliumGrid(nextGrid);
		this.jlStep.setText("Iteration: " + this.iCurrSimIter);
		this.jbRewind.setEnabled(true);
		this.jbBack.setEnabled(true);
		// Re-Paint
		this.repaint();
	}

	private void getCompMiniPanel(JPanel jp, GridBagConstraints gbc, int y,
			String nodeID) {
		gbc.gridy = y;
		gbc.gridx = 0;
		gbc.anchor = GridBagConstraints.WEST;
		JCheckBox jcb = this.mNodeID2Checkbox.get(nodeID);
		if (jcb == null) {
			jcb = new JCheckBox(nodeID, mSelCheckboxes.get(nodeID));
			jcb.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JCheckBox jcb = (JCheckBox) e.getSource();
					mSelCheckboxes.put(jcb.getText(), jcb.isSelected());
					if (jcb.isSelected()) {
						lCompON.add(jcb.getText());
					} else {
						lCompON.remove(jcb.getText());
					}
					visualGridSimulation.paintComponent(visualGridSimulation
							.getGraphics());
				}
			});
			this.mNodeID2Checkbox.put(nodeID, jcb);
		}
		jp.add(jcb, gbc);
		gbc.gridx = 1;
		JButton jbColor = new JButton();
		jbColor.setBackground(this.epithelium.getComponentFeatures()
				.getNodeColor(nodeID));
		jbColor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setNewColor((JButton) e.getSource());
			}
		});
		jp.add(jbColor, gbc);
		this.colorButton2Node.put(jbColor, nodeID);
	}

	private void setNewColor(JButton jb) {
		String nodeID = this.colorButton2Node.get(jb);
		Color newColor = JColorChooser.showDialog(jb, "Color chooser - "
				+ nodeID, jb.getBackground());
		if (newColor != null
				&& !newColor.equals(projectFeatures.getNodeColor(nodeID))) {
			jb.setBackground(newColor);
			this.epithelium.getComponentFeatures().setNodeColor(nodeID,
					newColor);
			this.projChanged.setChanged(this);
			this.visualGridSimulation.paintComponent(this.visualGridSimulation
					.getGraphics());
		}
	}

	private void updateComponentList(List<String> items) {
		this.jpRRCenter.removeAll();
		this.lCompON.clear();
		this.colorButton2Node.clear();

		List<LogicalModel> lModels = new ArrayList<LogicalModel>();
		for (String modelName : items) {
			lModels.add(this.projectFeatures.getModel(modelName));
		}
		this.lPresentComps = new ArrayList<String>();

		// Proper components
		JPanel jpRRCTop = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 4, 0);
		jpRRCTop.setBorder(BorderFactory
				.createTitledBorder("Proper components"));
		List<NodeInfo> lProper = new ArrayList<NodeInfo>(this.epithelium
				.getComponentFeatures().getModelsNodeInfos(lModels, false));
		Collections.sort(lProper, ObjectComparator.NODE_INFO);
		int y = 0;
		for (NodeInfo node : lProper) {
			String nodeID = node.getNodeID();
			this.lPresentComps.add(nodeID);
			if (mSelCheckboxes.get(nodeID)) {
				this.lCompON.add(nodeID);
			}
			this.getCompMiniPanel(jpRRCTop, gbc, y, nodeID);
			y++;
		}
		this.jpRRCenter.add(jpRRCTop);

		// Input components
		JPanel jpRRCBottom = new JPanel(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 4, 0);
		jpRRCBottom.setBorder(BorderFactory
				.createTitledBorder("Input components"));
		List<NodeInfo> lInputs = new ArrayList<NodeInfo>(this.epithelium
				.getComponentFeatures().getModelsNodeInfos(lModels, true));
		Collections.sort(lInputs, ObjectComparator.NODE_INFO);
		y = 0;
		for (NodeInfo node : lInputs) {
			String nodeID = node.getNodeID();
			this.lPresentComps.add(nodeID);
			if (mSelCheckboxes.get(nodeID)) {
				this.lCompON.add(nodeID);
			}
			this.getCompMiniPanel(jpRRCBottom, gbc, y, nodeID);
			y++;
		}
		this.jpRRCenter.add(jpRRCBottom);
		this.visualGridSimulation.paintComponent(this.visualGridSimulation
				.getGraphics());
		this.jpRRCenter.revalidate();
		this.jpRRCenter.repaint();
	}

	@Override
	public boolean canClose() {
		return true;
	}

	private boolean hasChangedEpithelium() {
		return !this.simulation.getEpithelium().equals(this.epithelium);
	}

	@Override
	public void notifyChange() {
		if (!this.isInitialized)
			return;
		if (this.hasChangedEpithelium()) {
			JTextPane jtp = new JTextPane();
			jtp.setContentType("text/html");
			jtp.setText("<html><body style=\"background-color:#ffbebe\">"
					+ "You have some <b>changed definitions</b> for this Epithelium.<br/>"
					+ "This simulation is therefore <b>no longer valid</b>!<br/>"
					+ "You can still perform a clone/screenshot of particular interations.<br/>"
					+ "Please <b>close/re-open it</b>, to have an updated Simulation Tab."
					+ "</body></html>");
			this.jpRight.add(jtp, BorderLayout.NORTH);
		} else {
			for (int i = 0; i < this.jpRight.getComponentCount(); i++) {
				Component c = this.jpRight.getComponent(i);
				if (c instanceof JTextPane) {
					this.jpRight.remove(i);
					break;
				}
			}
		}
		this.updateComponentList(this.jccb.getSelectedItems());
	}
}