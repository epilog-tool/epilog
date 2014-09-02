package org.ginsim.epilog.gui.tab;

import java.awt.BorderLayout;
import java.awt.Color;
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
import java.util.Comparator;
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
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.tree.TreePath;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;
import org.colomoto.logicalmodel.perturbation.AbstractPerturbation;
import org.ginsim.epilog.ProjectModelFeatures;
import org.ginsim.epilog.Simulation;
import org.ginsim.epilog.core.Epithelium;
import org.ginsim.epilog.core.EpitheliumGrid;
import org.ginsim.epilog.gui.EpiGUI.SimulationEpiClone;
import org.ginsim.epilog.gui.color.ColorUtils;
import org.ginsim.epilog.gui.widgets.JComboCheckBox;
import org.ginsim.epilog.gui.widgets.VisualGridSimulation;
import org.ginsim.epilog.io.ButtonFactory;
import org.ginsim.epilog.io.FileIO;

public class EpiTabSimulation extends EpiTab {
	private static final long serialVersionUID = 1394895739386499680L;

	private VisualGridSimulation visualGridSimulation;
	private Simulation simulation;
	private Map<String, Boolean> mSelCheckboxes;
	private Map<String, JCheckBox> mNodeID2Checkbox;
	private ProjectModelFeatures modelFeatures;
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
	private JPanel jpLeftAggreg;
	private GridComponentValues lRight;
	private JPanel lLeft;
	private JSplitPane jspRRCenter;

	public EpiTabSimulation(Epithelium e, TreePath path,
			ProjectModelFeatures modelFeatures, SimulationEpiClone simEpiClone) {
		super(e, path);
		this.modelFeatures = modelFeatures;
		this.simEpiClone = simEpiClone;
	}

	public void initialize() {
		setLayout(new BorderLayout());

		this.iUserBurst = 30;
		this.iCurrSimIter = 0;
		this.simulation = new Simulation(this.epithelium);
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
		this.lRight = new GridComponentValues();

		this.visualGridSimulation = new VisualGridSimulation(this.epithelium
				.getEpitheliumGrid().getX(), this.epithelium
				.getEpitheliumGrid().getY(), this.epithelium
				.getEpitheliumGrid().getTopology(),
				this.epithelium.getComponentFeatures(),
				this.epithelium.getEpitheliumGrid(), this.lCompON, this.lRight);
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

		jpButtons.add(jpButtonsR, BorderLayout.LINE_END);

		JPanel jpButtonsL = new JPanel();
		this.jlStep = new JLabel("Step: " + this.iCurrSimIter);
		jpButtonsL.add(this.jlStep);
		this.jlStable = new JLabel("Stable!");
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
			items[i] = new JCheckBox(this.modelFeatures.getName(modelList
					.get(i)));
			items[i].setSelected(false);
		}
		JComboCheckBox jccb = new JComboCheckBox(items);
		rrTop.add(jccb);

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

		this.jspRRCenter = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		this.lLeft.add(jspRRCenter, BorderLayout.CENTER);
		jccb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboCheckBox jccb = (JComboCheckBox) e.getSource();
				updateComponentList(jccb.getSelectedItems());
			}
		});

		this.jpLeftAggreg = new JPanel(new BorderLayout());
		jpLeftAggreg.add(this.lLeft, BorderLayout.LINE_START);
		jpLeftAggreg.add(this.lRight, BorderLayout.LINE_END);

		this.add(this.jpLeftAggreg, BorderLayout.LINE_START);
		updateComponentList(jccb.getSelectedItems());
	}

	
	
	
	private void saveEpiGrid2File() {
		JFileChooser fc = new JFileChooser();
		if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			String file = fc.getSelectedFile().getAbsolutePath();
			file += (file.endsWith(".png") ? "" : ".png");
			FileIO.writeEpitheliumGrid2File(
					this.simulation.getGridAt(this.iCurrSimIter), file,
					this.visualGridSimulation);
		}
	}

	private void cloneEpiWithCurrGrid() {
		this.simEpiClone.cloneEpithelium(this.epithelium,
				this.simulation.getGridAt(this.iCurrSimIter));
	}

	private void simulationRewind() {
		this.iCurrSimIter = 0;
		this.jlStep.setText("Step: " + this.iCurrSimIter);
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
		this.jlStep.setText("Step: " + this.iCurrSimIter);
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
			this.jlStable.setForeground(Color.RED);
			this.jbForward.setEnabled(false);
			this.jbFastFwr.setEnabled(false);
		} else {
			this.jlStable.setForeground(this.jlStep.getBackground());
		}
	}

	private void simulationStepFwr() {
		EpitheliumGrid prevGrid = this.simulation.getGridAt(this.iCurrSimIter);
		EpitheliumGrid nextGrid = this.simulation
				.getGridAt(this.iCurrSimIter + 1);
		if (!nextGrid.equals(prevGrid)) {
			this.visualGridSimulation.setEpitheliumGrid(nextGrid);
			this.jlStep.setText("Step: " + ++this.iCurrSimIter);
		} else {
			setGridGUIStable(true);
		}
		this.jbRewind.setEnabled(true);
		this.jbBack.setEnabled(true);
		// Re-Paint
		this.repaint();
	}

	private void simulationFastFwr() {
		EpitheliumGrid prevGrid = this.simulation.getGridAt(this.iCurrSimIter);
		EpitheliumGrid nextGrid = prevGrid;
		for (int i = 0; i < this.iUserBurst; i++) {
			nextGrid = this.simulation.getGridAt(this.iCurrSimIter + 1);
			if (nextGrid.equals(prevGrid)) {
				setGridGUIStable(true);
				break;
			}
			prevGrid = nextGrid;
			this.iCurrSimIter++;
		}
		this.visualGridSimulation.setEpitheliumGrid(nextGrid);
		this.jlStep.setText("Step: " + this.iCurrSimIter);
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
		if (newColor != null) {
			jb.setBackground(newColor);
			this.epithelium.getComponentFeatures().setNodeColor(nodeID,
					newColor);
			this.visualGridSimulation.paintComponent(this.visualGridSimulation
					.getGraphics());
		}
	}

	private void updateComponentList(List<String> items) {
		this.jspRRCenter.removeAll();
		this.lCompON.clear();
		this.colorButton2Node.clear();

		List<LogicalModel> lModels = new ArrayList<LogicalModel>();
		for (String modelName : items) {
			lModels.add(this.modelFeatures.getModel(modelName));
		}
		this.lPresentComps = new ArrayList<String>();

		// Proper components
		JPanel jpRRCTop = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(1, 5, 1, 0);
		jpRRCTop.setBorder(BorderFactory
				.createTitledBorder("Proper components"));
		List<String> lProper = new ArrayList<String>(this.epithelium
				.getComponentFeatures().getModelsComponents(lModels, false));
		Collections.sort(lProper, new Comparator<String>() {
			public int compare(String s1, String s2) {
				return s1.compareToIgnoreCase(s2);
			}
		});
		int y = 0;
		for (String nodeID : lProper) {
			this.lPresentComps.add(nodeID);
			if (mSelCheckboxes.get(nodeID)) {
				this.lCompON.add(nodeID);
			}
			this.getCompMiniPanel(jpRRCTop, gbc, y, nodeID);
			y++;
		}
		this.jspRRCenter.add(jpRRCTop);

		// Input components
		JPanel jpRRCBottom = new JPanel(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.insets = new Insets(1, 5, 1, 0);
		jpRRCBottom.setBorder(BorderFactory
				.createTitledBorder("Input components"));
		List<String> lInputs = new ArrayList<String>(this.epithelium
				.getComponentFeatures().getModelsComponents(lModels, true));
		Collections.sort(lInputs, new Comparator<String>() {
			public int compare(String s1, String s2) {
				return s1.compareToIgnoreCase(s2);
			}
		});
		List<String> lEnvInputCompsFromSelectedModels = new ArrayList<String>();
		for (String nodeID : lInputs) {
			if (!this.epithelium.isIntegrationComponent(nodeID)) {

				lEnvInputCompsFromSelectedModels.add(nodeID);
			}
		}
		y = 0;
		for (String nodeID : lEnvInputCompsFromSelectedModels) {
			this.lPresentComps.add(nodeID);
			if (mSelCheckboxes.get(nodeID)) {
				this.lCompON.add(nodeID);
			}
			this.getCompMiniPanel(jpRRCBottom, gbc, y, nodeID);
			y++;
		}
		this.jspRRCenter.add(jpRRCBottom);
		this.visualGridSimulation.paintComponent(this.visualGridSimulation
				.getGraphics());
	}

	@Override
	public boolean canClose() {
		return true;
	}

	
	//Panel with information about the cell. It is activated when the mouse is pressed over the grid.
	public class GridComponentValues extends JPanel {
		private static final long serialVersionUID = -1449994132920814592L;

		public GridComponentValues() {
			this.setLayout(new GridBagLayout());
			this.setBorder(BorderFactory.createTitledBorder("Grid information"));
			this.updateValues(0, 0, null);
		}

		private void minimalSpace(GridBagConstraints gbc, int y) {
			gbc.gridy = y;
			gbc.gridx = 0;
			gbc.gridwidth = 2;
			JLabel jlTmp = new JLabel("Perturbation:      ");
			jlTmp.setForeground(this.getBackground());
			this.add(jlTmp, gbc);
		}
		
		public void updateValues(int posX, int posY, EpitheliumGrid grid) {
			// TODO: LogicalModel m, AbstractPerturbation ap, byte[] state) {
			this.removeAll();
			JLabel jlTmp;
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.fill = GridBagConstraints.HORIZONTAL;
			int y = 0;

			// Separation
			this.minimalSpace(gbc, y);
			if (grid != null) {
				List<String> lAllNodeIDs = new ArrayList<String>(epithelium
						.getComponentFeatures().getComponents());
				Collections.sort(lAllNodeIDs, new Comparator<String>() {
					public int compare(String s1, String s2) {
						return s1.compareToIgnoreCase(s2);
					}
				});
				LogicalModel m = grid.getModel(posX, posY);

				// Cell Position
				gbc.gridy = ++y;
				gbc.gridx = 0;
				gbc.gridwidth = 2;
				gbc.anchor = GridBagConstraints.WEST;
				jlTmp = new JLabel("Cell: " + posX + "," + posY);
				this.add(jlTmp, gbc);

				// Separation
				this.minimalSpace(gbc, ++y);

				// Cell Model
				gbc.gridy = ++y;
				gbc.gridx = 0;
				gbc.gridwidth = 2;
				gbc.anchor = GridBagConstraints.WEST;
				jlTmp = new JLabel("Model:");
				this.add(jlTmp, gbc);
				gbc.gridy = ++y;
				gbc.gridx = 0;
				gbc.gridwidth = 2;
				jlTmp = new JLabel("  " + modelFeatures.getName(m));
				this.add(jlTmp, gbc);

				// Separation
				this.minimalSpace(gbc, ++y);

				// Perturbations
				gbc.gridy = ++y;
				gbc.gridx = 0;
				gbc.gridwidth = 2;
				gbc.anchor = GridBagConstraints.WEST;
				jlTmp = new JLabel("Perturbation:");
				this.add(jlTmp, gbc);
				gbc.gridy = ++y;
				gbc.gridx = 0;
				gbc.gridwidth = 2;
				AbstractPerturbation ap = grid.getPerturbation(posX, posY);
				jlTmp = new JLabel("  "
						+ ((ap == null) ? "none" : ap.toString()));
				this.add(jlTmp, gbc);

				// Separation
				this.minimalSpace(gbc, ++y);

				// Proper values
				gbc.gridy = ++y;
				gbc.gridx = 0;
				gbc.gridwidth = 1;
				jlTmp = new JLabel("Proper:");
				this.add(jlTmp, gbc);
				gbc.gridwidth = 1;
				for (String nodeID : lAllNodeIDs) {
					if (epithelium.getComponentFeatures().getNodeInfo(nodeID)
							.isInput())
						continue;
					gbc.gridy = ++y;
					gbc.gridx = 0;
					gbc.anchor = GridBagConstraints.WEST;
					jlTmp = new JLabel(nodeID + " ");
					this.add(jlTmp, gbc);
					int index = grid.getNodeIndex(posX, posY, nodeID);
					if (index < 0)
						continue;
					gbc.gridx = 1;
					jlTmp = new JLabel(": "
							+ grid.getCellState(posX, posY)[index]);
					this.add(jlTmp, gbc);
				}

				// Separation
				this.minimalSpace(gbc, ++y);

				// Input values
				gbc.gridy = ++y;
				gbc.gridx = 0;
				gbc.gridwidth = 1;
				jlTmp = new JLabel("Input:");
				this.add(jlTmp, gbc);
				gbc.gridwidth = 1;
				for (String nodeID : lAllNodeIDs) {
					if (!epithelium.getComponentFeatures().getNodeInfo(nodeID)
							.isInput()
							|| epithelium.isIntegrationComponent(nodeID))
						continue;
					gbc.gridy = ++y;
					gbc.gridx = 0;
					gbc.anchor = GridBagConstraints.WEST;
					jlTmp = new JLabel(nodeID + " ");
					this.add(jlTmp, gbc);
					int index = grid.getNodeIndex(posX, posY, nodeID);
					if (index < 0)
						continue;
					gbc.gridx = 1;
					jlTmp = new JLabel(": "
							+ grid.getCellState(posX, posY)[index]);
					this.add(jlTmp, gbc);
				}

				gbc.anchor = GridBagConstraints.PAGE_END;
				// Separation
				this.minimalSpace(gbc, ++y);
			}

			gbc.weighty = 1.0;
			gbc.anchor = GridBagConstraints.PAGE_END;
			gbc.gridy = ++y;
			gbc.gridx = 0;
			gbc.gridwidth = 2;
			jlTmp = new JLabel(" ");
			this.add(jlTmp, gbc);

			// Repaint
			// TODO: this.setSize(this.getPreferredSize());
			this.revalidate();
			this.repaint();
		}
	}
}