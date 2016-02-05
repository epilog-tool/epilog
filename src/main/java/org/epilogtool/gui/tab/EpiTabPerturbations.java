package org.epilogtool.gui.tab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.tree.TreePath;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;
import org.colomoto.logicalmodel.perturbation.AbstractPerturbation;
import org.colomoto.logicalmodel.perturbation.FixedValuePerturbation;
import org.colomoto.logicalmodel.perturbation.MultiplePerturbation;
import org.colomoto.logicalmodel.perturbation.RangePerturbation;
import org.epilogtool.core.Epithelium;
import org.epilogtool.core.EpitheliumCell;
import org.epilogtool.core.EpitheliumGrid;
import org.epilogtool.core.EpitheliumPerturbations;
import org.epilogtool.core.ModelPerturbations;
import org.epilogtool.gui.EpiGUI.EpiTabChanged;
import org.epilogtool.gui.EpiGUI.ProjectChangedInTab;
import org.epilogtool.gui.color.ColorUtils;
import org.epilogtool.gui.widgets.JComboWideBox;
import org.epilogtool.gui.widgets.VisualGridPerturbation;
import org.epilogtool.io.ButtonFactory;
import org.epilogtool.project.ProjectFeatures;

public class EpiTabPerturbations extends EpiTabDefinitions {
	private static final long serialVersionUID = -1795100027288146018L;

	private VisualGridPerturbation visualGridPerturb;
	private EpitheliumCell[][] cellGridClone;
	private EpitheliumPerturbations epiPerturbClone;

	private Map<AbstractPerturbation, Color> colorMapClone;
	private LogicalModel selModel;
	private Map<String, AbstractPerturbation> mID2AP;
	private Map<AbstractPerturbation, JRadioButton> mAP2RadioButton;
	private Map<AbstractPerturbation, JButton> mAP2JButton;
	private ButtonGroup jrbGroup;

	private JPanel jpCenter;
	private JComboBox<String> jcbComps;
	private JComboBox<Byte> jcbMinVal;
	private JComboBox<Byte> jcbMaxVal;
	private JList<AbstractPerturbation> jlPerturb;
	private JScrollPane jspRBColor;
	private JPanel jpRBColor;
	private JPanel lTop;

	public EpiTabPerturbations(Epithelium e, TreePath path, ProjectChangedInTab projChanged, EpiTabChanged tabChanged,
			ProjectFeatures projectFeatures) {
		super(e, path, projChanged, tabChanged, projectFeatures);
	}

	public void initialize() {
		this.center.setLayout(new BorderLayout());

		this.jspRBColor = new JScrollPane();
		this.jspRBColor.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.jpRBColor = new JPanel(new GridBagLayout());
		this.jspRBColor.setViewportView(this.jpRBColor);
		this.colorMapClone = new HashMap<AbstractPerturbation, Color>();
		this.mID2AP = new HashMap<String, AbstractPerturbation>();
		this.mAP2RadioButton = new HashMap<AbstractPerturbation, JRadioButton>();
		this.mAP2JButton = new HashMap<AbstractPerturbation, JButton>();
		this.jrbGroup = new ButtonGroup();
		this.selModel = null;

		EpitheliumGrid grid = this.epithelium.getEpitheliumGrid();
		this.cellGridClone = new EpitheliumCell[grid.getX()][grid.getY()];
		for (int x = 0; x < this.cellGridClone.length; x++) {
			for (int y = 0; y < this.cellGridClone[0].length; y++) {
				this.cellGridClone[x][y] = grid.cloneEpitheliumCellAt(x, y);
			}
		}
		this.epiPerturbClone = this.epithelium.getEpitheliumPerturbations().clone();

		this.visualGridPerturb = new VisualGridPerturbation(this.epithelium.getEpitheliumGrid().getX(),
				this.epithelium.getEpitheliumGrid().getY(), this.epithelium.getEpitheliumGrid().getTopology(),
				this.cellGridClone, colorMapClone);
		this.center.add(this.visualGridPerturb, BorderLayout.CENTER);

		// Perturbation creation Panel
		JPanel left = new JPanel(new BorderLayout());

		// Model selection Panel
		this.lTop = new JPanel(new FlowLayout());
		List<LogicalModel> modelList = new ArrayList<LogicalModel>(this.epithelium.getEpitheliumGrid().getModelSet());
		JComboBox<String> jcbSBML = this.newModelCombobox(modelList);
		this.lTop.add(jcbSBML);
		this.lTop.setBorder(BorderFactory.createTitledBorder("Model selection"));
		left.add(this.lTop, BorderLayout.NORTH);

		this.jpCenter = new JPanel(new BorderLayout());
		left.add(jpCenter, BorderLayout.CENTER);
		this.center.add(left, BorderLayout.LINE_START);
		LogicalModel m = this.projectFeatures.getModel((String) jcbSBML.getSelectedItem());
		updatePanelsWithModel(m);
		this.isInitialized = true;
	}

	private JComboBox<String> newModelCombobox(List<LogicalModel> modelList) {
		// Model selection list
		String[] saSBML = new String[modelList.size()];
		for (int i = 0; i < modelList.size(); i++) {
			saSBML[i] = this.projectFeatures.getModelName(modelList.get(i));
		}
		JComboBox<String> jcb = new JComboWideBox(saSBML);
		jcb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unchecked")
				JComboBox<String> jcb = (JComboBox<String>) e.getSource();
				LogicalModel m = projectFeatures.getModel((String) jcb.getSelectedItem());
				updatePanelsWithModel(m);
			}
		});
		return jcb;
	}

	private void updatePanelsWithModel(LogicalModel m) {
		this.selModel = m;
		this.visualGridPerturb.setModel(this.selModel);
		this.mAP2RadioButton.clear();
		for (JButton jb : this.mAP2JButton.values())
			this.jrbGroup.remove(jb);
		this.mAP2JButton.clear();

		// Center Panel
		this.jpCenter.removeAll();

		// Perturbation list Panel
		JPanel jpPerturbList = new JPanel(new BorderLayout());
		jpPerturbList.setBorder(BorderFactory.createTitledBorder("Perturbation list"));
		JPanel jpPerturbTop = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 0;
		gbc.gridx = 0;
		gbc.anchor = GridBagConstraints.WEST;
		jpPerturbTop.add(new JLabel("Component:"), gbc);

		Set<String> sProper = this.epithelium.getProjectFeatures().getModelComponents(this.selModel, false);

		String[] saProper = new String[sProper.size()];
		int i = 0;
		for (String nodeID : sProper)
			saProper[i++] = nodeID;
		this.jcbComps = new JComboBox<String>(saProper);
		jcbComps.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unchecked")
				JComboBox<String> jcb = (JComboBox<String>) e.getSource();
				String nodeID = (String) jcb.getSelectedItem();
				updateMinMaxValues(nodeID);
			}
		});
		gbc.gridx = 1;
		jpPerturbTop.add(jcbComps, gbc);
		gbc.gridy = 1;
		gbc.gridx = 0;
		gbc.anchor = GridBagConstraints.WEST;
		jpPerturbTop.add(new JLabel("Min value:"), gbc);
		jcbMinVal = new JComboBox<Byte>();
		gbc.gridx = 1;
		jpPerturbTop.add(jcbMinVal, gbc);
		gbc.gridy = 2;
		gbc.gridx = 0;
		gbc.anchor = GridBagConstraints.WEST;
		jpPerturbTop.add(new JLabel("Max value:"), gbc);
		jcbMaxVal = new JComboBox<Byte>();
		gbc.gridx = 1;
		jpPerturbTop.add(jcbMaxVal, gbc);
		updateMinMaxValues(saProper[0]);
		JPanel jpTmp = new JPanel(new FlowLayout());
		JButton jbCreate = ButtonFactory.getNoMargins("Create");
		jbCreate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String nodeID = (String) jcbComps.getSelectedItem();
				NodeInfo node = epithelium.getProjectFeatures().getNodeInfo(nodeID, selModel);
				byte min = (Byte) jcbMinVal.getSelectedItem();
				byte max = (Byte) jcbMaxVal.getSelectedItem();
				AbstractPerturbation ap;
				if (max < min)
					return;
				else if (min == max)
					ap = new FixedValuePerturbation(node, min);
				else
					ap = new RangePerturbation(node, min, max);
				DefaultListModel<AbstractPerturbation> lm = (DefaultListModel<AbstractPerturbation>) jlPerturb
						.getModel();
				if (!lm.contains(ap)) {
					lm.addElement(ap);
					epiPerturbClone.addPerturbation(selModel, ap);
					mID2AP.put(ap.toString(), ap);
				}
			}
		});
		jpTmp.add(jbCreate);
		JButton jbDelete = ButtonFactory.getNoMargins("Delete");
		jbDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] pos = jlPerturb.getSelectedIndices();
				if (pos == null || pos.length == 0)
					return;
				DefaultListModel<AbstractPerturbation> lm = (DefaultListModel<AbstractPerturbation>) jlPerturb
						.getModel();
				int[] selIndex = jlPerturb.getSelectedIndices();
				List<Integer> lOkIndex = new ArrayList<Integer>();
				for (int i = selIndex.length - 1; i >= 0; i--) {
					AbstractPerturbation ap = lm.getElementAt(selIndex[i]);
					if (!hasCellGridClone(ap)) {
						lOkIndex.add(selIndex[i]);
					}
				}
				for (int i = 0; i < lOkIndex.size(); i++) {
					AbstractPerturbation ap = lm.getElementAt(lOkIndex.get(i));
					epiPerturbClone.delPerturbation(selModel, ap);
					lm.removeElementAt(lOkIndex.get(i));
					mID2AP.remove(ap.toString());
					mAP2RadioButton.remove(ap);
				}
				repaintAPColorsPanel();
			}
		});
		jpTmp.add(jbDelete);
		gbc.gridy = 3;
		gbc.gridx = 0;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		jpPerturbTop.add(jpTmp, gbc);

		jpPerturbList.add(jpPerturbTop, BorderLayout.NORTH);

		DefaultListModel<AbstractPerturbation> dlmAPs = new DefaultListModel<AbstractPerturbation>();

		ModelPerturbations mp = this.epiPerturbClone.getModelPerturbations(this.selModel);
		if (mp != null)
			for (AbstractPerturbation ap : mp.getAllPerturbations()) {
				dlmAPs.addElement(ap);
				this.mID2AP.put(ap.toString(), ap);
			}
		this.jlPerturb = new JList<AbstractPerturbation>(dlmAPs);
		this.jlPerturb.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		jpPerturbList.add(this.jlPerturb, BorderLayout.CENTER);

		jpTmp = new JPanel(new FlowLayout());
		JButton jbMultiple = ButtonFactory.getNoMargins("Create Multiple");
		jbMultiple.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] selIndex = jlPerturb.getSelectedIndices();
				if (selIndex == null || selIndex.length <= 1)
					return;
				DefaultListModel<AbstractPerturbation> lm = (DefaultListModel<AbstractPerturbation>) jlPerturb
						.getModel();
				List<AbstractPerturbation> lAPs = new ArrayList<AbstractPerturbation>();
				for (int i = 0; i < selIndex.length; i++) {
					lAPs.add(lm.getElementAt(selIndex[i]));
				}
				MultiplePerturbation<AbstractPerturbation> mp = new MultiplePerturbation<AbstractPerturbation>(lAPs);
				if (!lm.contains(mp)) {
					lm.addElement(mp);
					epiPerturbClone.addPerturbation(selModel, mp);
					mID2AP.put(mp.toString(), mp);
				}
			}
		});
		jpPerturbList.add(jbMultiple, BorderLayout.SOUTH);

		// Add / Del buttons Panel
		Box jpAddDel = Box.createVerticalBox();
		jpAddDel.add(Box.createVerticalGlue());
		JButton jbAdd = ButtonFactory.getNoMargins("->");
		jbAdd.setAlignmentY(CENTER_ALIGNMENT);
		jbAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] selIndex = jlPerturb.getSelectedIndices();
				if (selIndex == null || selIndex.length == 0)
					return;
				DefaultListModel<AbstractPerturbation> lm = (DefaultListModel<AbstractPerturbation>) jlPerturb
						.getModel();
				for (int i = 0; i < selIndex.length; i++) {
					AbstractPerturbation ap = lm.getElementAt(selIndex[i]);
					ModelPerturbations mp = epiPerturbClone.getModelPerturbations(selModel);
					Color c = mp.getPerturbationColor(ap);
					if (c == null) {
						c = ColorUtils.random();
					}
					addColor2MarkPanel(ap, c);
				}
				repaintAPColorsPanel();
			}
		});
		jpAddDel.add(jbAdd);
		JButton jbDel = ButtonFactory.getNoMargins("<-");
		jbDel.setAlignmentY(CENTER_ALIGNMENT);
		jbDel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				List<AbstractPerturbation> lTmp = new ArrayList<AbstractPerturbation>(mAP2RadioButton.keySet());
				for (AbstractPerturbation ap : lTmp) {
					JRadioButton jrb = mAP2RadioButton.get(ap);
					if (jrb.isSelected() && !hasCellGridClone(ap)) {
						mAP2RadioButton.remove(ap);
						jrbGroup.remove(mAP2JButton.get(ap));
						mAP2JButton.remove(ap);
						colorMapClone.remove(ap);
						epiPerturbClone.getModelPerturbations(selModel).delPerturbationColor(ap);
						repaintAPColorsPanel();
						return;
					}
				}
			}
		});
		jpAddDel.add(jbDel);
		jpAddDel.add(Box.createVerticalGlue());

		// Color Marking Panel
		JPanel jpColorMark = new JPanel(new BorderLayout());

		if (mp != null) {
			for (AbstractPerturbation ap : mp.getAllPerturbations()) {
				Color c = mp.getPerturbationColor(ap);
				if (c == null)
					continue;
				this.addColor2MarkPanel(ap, c);
			}
		}
		this.repaintAPColorsPanel();

		JPanel jpColorApplyClear = new JPanel(new FlowLayout());
		JButton jbApplyAll = ButtonFactory.getNoMargins("Apply All");
		jbApplyAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				visualGridPerturb.applyDataToAll();
			}
		});
		jpColorApplyClear.add(jbApplyAll);
		JButton jbClearAll = ButtonFactory.getNoMargins("Clear All");
		jbClearAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				visualGridPerturb.clearDataFromAll();
			}
		});
		jpColorApplyClear.add(jbClearAll);
		JPanel jpColorRect = new JPanel(new FlowLayout());
		JToggleButton jtbRectFill = new JToggleButton("Rectangle Fill", false);
		jtbRectFill.setMargin(new Insets(0, 0, 0, 0));
		jtbRectFill.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				JToggleButton jtb = (JToggleButton) e.getSource();
				visualGridPerturb.isRectangleFill(jtb.isSelected());
			}
		});
		jpColorRect.add(jtbRectFill);

		this.jspRBColor.setBorder(BorderFactory.createTitledBorder("Select to mark cells"));
		jpColorMark.add(this.jspRBColor, BorderLayout.CENTER);
		JPanel jpColorTop = new JPanel(new BorderLayout());
		jpColorTop.setBorder(BorderFactory.createTitledBorder("Apply selection"));
		jpColorTop.add(jpColorApplyClear, BorderLayout.PAGE_START);
		jpColorTop.add(jpColorRect, BorderLayout.CENTER);
		jpColorMark.add(jpColorTop, BorderLayout.PAGE_END);

		jpCenter.add(jpPerturbList, BorderLayout.LINE_START);
		jpCenter.add(jpAddDel, BorderLayout.CENTER);
		jpCenter.add(jpColorMark, BorderLayout.LINE_END);

		// Re-Paint
		this.getParent().repaint();
	}

	private boolean hasCellGridClone(AbstractPerturbation ap) {
		for (int x = 0; x < this.cellGridClone.length; x++) {
			for (int y = 0; y < this.cellGridClone[0].length; y++) {
				AbstractPerturbation cellAP = this.cellGridClone[x][y].getPerturbation();
				if (cellAP != null && cellAP.equals(ap))
					return true;
			}
		}
		return false;
	}

	private void repaintAPColorsPanel() {
		this.jpRBColor.removeAll();
		GridBagConstraints gbc = new GridBagConstraints();
		int y = 0;
		for (AbstractPerturbation ap : this.colorMapClone.keySet()) {
			if (this.epiPerturbClone.getModelPerturbations(this.selModel) == null || 
					this.epiPerturbClone.getModelPerturbations(this.selModel).getPerturbationColor(ap) == null)
				continue;
			gbc.gridy = y;
			gbc.gridx = 0;
			gbc.anchor = GridBagConstraints.WEST;
			this.jpRBColor.add(this.mAP2RadioButton.get(ap), gbc);
			gbc.gridx = 1;
			this.jpRBColor.add(this.mAP2JButton.get(ap), gbc);
			y++;
		}
		JRadioButton jrDel = new JRadioButton("Clear cell");
		jrDel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				visualGridPerturb.setSelAbsPerturb(null);
			}
		});
		this.jrbGroup.add(jrDel);
		gbc.gridy = y;
		gbc.gridx = 0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridwidth = 2;
		this.jpRBColor.add(jrDel, gbc);
		//TODO jrDel.setSelected is not setting selected abstract perturbations to null
		jrDel.setSelected(true);
		this.visualGridPerturb.setSelAbsPerturb(null);
		this.jpRBColor.revalidate();
		this.jpRBColor.repaint();
	}

	private void addColor2MarkPanel(AbstractPerturbation ap, Color c) {
		// JRadioButton
		JRadioButton jrb = new JRadioButton(ap.toString());
		jrb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JRadioButton jrb = (JRadioButton) e.getSource();
				AbstractPerturbation ap = mID2AP.get(jrb.getText());
				visualGridPerturb.setSelAbsPerturb(ap);
			}
		});
		this.jrbGroup.add(jrb);
		this.mAP2RadioButton.put(ap, jrb);
		// JButton Color
		JButton jbColor = new JButton();
		jbColor.setToolTipText(ap.toString());
		jbColor.setBackground(c);
		jbColor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JButton jb = (JButton) e.getSource();
				String apID = jb.getToolTipText();
				Color newColor = JColorChooser.showDialog(jb, "Color chooser - " + apID, jb.getBackground());
				if (newColor != null) {
					jb.setBackground(newColor);
					AbstractPerturbation ap = mID2AP.get(apID);
					colorMapClone.put(ap, newColor);
					epiPerturbClone.getModelPerturbations(selModel).addPerturbationColor(ap, newColor);
					visualGridPerturb.paintComponent(visualGridPerturb.getGraphics());
				}
			}
		});
		this.mAP2JButton.put(ap, jbColor);
		this.colorMapClone.put(ap, c);
		this.epiPerturbClone.getModelPerturbations(this.selModel).addPerturbationColor(ap, c);
	}

	private void updateMinMaxValues(String nodeID) {
		jcbMinVal.removeAllItems();
		jcbMaxVal.removeAllItems();
		byte max = epithelium.getProjectFeatures().getNodeInfo(nodeID, selModel).getMax();
		for (byte b = 0; b <= max; b++) {
			jcbMinVal.addItem(b);
			jcbMaxVal.addItem(b);
		}
	}

	@Override
	protected void buttonReset() {
		// Reset modifications on perturbation grid clone
		EpitheliumGrid grid = this.epithelium.getEpitheliumGrid();
		for (int x = 0; x < this.cellGridClone.length; x++) {
			for (int y = 0; y < this.cellGridClone[0].length; y++) {
				AbstractPerturbation apOrig = grid.getPerturbation(x, y);
				this.cellGridClone[x][y].setPerturbation(apOrig);
			}
		}
		// Reset modifications on ModelPerturbations
		this.epiPerturbClone = this.epithelium.getEpitheliumPerturbations().clone();
		updatePanelsWithModel(this.selModel);
	}

	@Override
	protected void buttonAccept() {
		// Check modifications on perturbation grid clone
		EpitheliumGrid grid = this.epithelium.getEpitheliumGrid();
		for (int x = 0; x < this.cellGridClone.length; x++) {
			for (int y = 0; y < this.cellGridClone[0].length; y++) {
				AbstractPerturbation apClone = this.cellGridClone[x][y].getPerturbation();
				grid.setPerturbation(x, y, apClone);
			}
		}
		// Check modifications on ModelPerturbations
		EpitheliumPerturbations epOrig = this.epithelium.getEpitheliumPerturbations();
		// Remove all ModelPerturbations
		for (LogicalModel m : new ArrayList<LogicalModel>(epOrig.getModelSet()))
			epOrig.removeModel(m);
		// Add the new ones
		for (LogicalModel m : this.epiPerturbClone.getModelSet()) {
			epOrig.addModelPerturbation(m, this.epiPerturbClone.getModelPerturbations(m).clone());
		}
	}

	@Override
	protected boolean isChanged() {
		// Check modifications on perturbation grid clone
		EpitheliumGrid grid = this.epithelium.getEpitheliumGrid();
		for (int x = 0; x < this.cellGridClone.length; x++) {
			for (int y = 0; y < this.cellGridClone[0].length; y++) {
				AbstractPerturbation apClone = this.cellGridClone[x][y].getPerturbation();
				if (apClone != null && grid.getPerturbation(x, y) == null
						|| apClone == null && grid.getPerturbation(x, y) != null || apClone != null
								&& grid.getPerturbation(x, y) != null && !grid.getPerturbation(x, y).equals(apClone)) {
					return true;
				}
			}
		}
		// Check modifications on ModelPerturbations
		return !this.epithelium.getEpitheliumPerturbations().equals(this.epiPerturbClone);
	}

	@Override
	public void applyChange() {
		List<LogicalModel> modelList = new ArrayList<LogicalModel>(this.epithelium.getEpitheliumGrid().getModelSet());
		EpitheliumPerturbations newPerturbs = new EpitheliumPerturbations();
		for (LogicalModel m : modelList) {
			if (this.epiPerturbClone.hasModel(m)) {
				// Already exists
				newPerturbs.addModelPerturbation(m, this.epiPerturbClone.getModelPerturbations(m));
			} else {
				// Adds a new one
				newPerturbs.addModel(m);
			}
		}
		this.epiPerturbClone = newPerturbs;
		this.lTop.removeAll();
		this.lTop.add(this.newModelCombobox(modelList));
		// Update grid
		EpitheliumGrid grid = this.epithelium.getEpitheliumGrid();
		for (int x = 0; x < this.cellGridClone.length; x++) {
			for (int y = 0; y < this.cellGridClone[0].length; y++) {
				if (!grid.getModel(x, y).equals(this.cellGridClone[x][y].getModel())) {
					this.cellGridClone[x][y] = grid.cloneEpitheliumCellAt(x, y);
				}
			}
		}
		this.updatePanelsWithModel(modelList.get(0));
	}
}
