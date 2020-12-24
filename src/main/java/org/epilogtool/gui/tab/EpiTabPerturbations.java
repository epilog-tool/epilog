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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.tree.TreePath;

import org.colomoto.biolqm.LogicalModel;
import org.colomoto.biolqm.NodeInfo;
import org.colomoto.biolqm.modifier.perturbation.LogicalModelPerturbation;
import org.colomoto.biolqm.modifier.perturbation.FixedValuePerturbation;
import org.colomoto.biolqm.modifier.perturbation.MultiplePerturbation;
import org.colomoto.biolqm.modifier.perturbation.RangePerturbation;
import org.epilogtool.common.ObjectComparator;
import org.epilogtool.common.Txt;
import org.epilogtool.core.Epithelium;
import org.epilogtool.core.EpitheliumGrid;
import org.epilogtool.core.EpitheliumPerturbations;
import org.epilogtool.gui.EpiGUI.TabChangeNotifyProj;
import org.epilogtool.gui.color.ColorUtils;
import org.epilogtool.gui.widgets.GridInformation;
import org.epilogtool.gui.widgets.JComboWideBox;
import org.epilogtool.gui.widgets.VisualGridPerturbation;
import org.epilogtool.io.ButtonFactory;
import org.epilogtool.notification.NotificationManager;
import org.epilogtool.project.Project;

public class EpiTabPerturbations extends EpiTabDefinitions {
	private static final long serialVersionUID = -1795100027288146018L;

	private VisualGridPerturbation visualGridPerturb;
	private EpitheliumGrid epiGridClone;
	private EpitheliumPerturbations epiPerturbClone;

	private LogicalModel selModel;
	private Map<String, LogicalModelPerturbation> mID2AP;
	private Map<LogicalModelPerturbation, JCheckBox> mAP2Checkbox;
	private Map<LogicalModelPerturbation, JRadioButton> mAP2RadioButton;
	private Map<LogicalModelPerturbation, JButton> mAP2JButton;
	private ButtonGroup jrbGroup;

	private JPanel jpCenter;
	private JComboBox<String> jcbComps;
	private JComboBox<Byte> jcbMinVal;
	private JComboBox<Byte> jcbMaxVal;
	private JScrollPane jspRBColor;
	private JPanel jpRBColor;
	private JPanel lTop;
	private GridInformation gridInfo;
	private TabProbablyChanged tpc;

	private byte maxValue;

	public EpiTabPerturbations(Epithelium e, TreePath path, TabChangeNotifyProj tabChanged) {
		super(e, path, tabChanged);
	}

	public void initialize() {
		this.center.setLayout(new BorderLayout());

		this.jspRBColor = new JScrollPane();
		this.jspRBColor.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.jspRBColor.setBorder(BorderFactory.createEmptyBorder());

		this.jpRBColor = new JPanel(new GridBagLayout());
		this.jspRBColor.setViewportView(this.jpRBColor);
		this.mID2AP = new HashMap<String, LogicalModelPerturbation>();
		this.mAP2Checkbox = new HashMap<LogicalModelPerturbation, JCheckBox>();
		this.mAP2RadioButton = new HashMap<LogicalModelPerturbation, JRadioButton>();
		this.mAP2JButton = new HashMap<LogicalModelPerturbation, JButton>();
		this.jrbGroup = new ButtonGroup();
		this.selModel = null;

		this.epiGridClone = this.epithelium.getEpitheliumGrid().clone();
		this.epiPerturbClone = this.epithelium.getEpitheliumPerturbations().clone();

		this.gridInfo = new GridInformation(this.epithelium.getIntegrationFunctions());

		this.tpc = new TabProbablyChanged();
		this.visualGridPerturb = new VisualGridPerturbation(this.epithelium.getEpitheliumGrid().getX(),
				this.epithelium.getEpitheliumGrid().getY(), this.epithelium.getEpitheliumGrid().getTopology(),
				this.epiGridClone, epiPerturbClone.getPerturbation2Color(), this.gridInfo, this.tpc);
		this.center.add(this.visualGridPerturb, BorderLayout.CENTER);

		// Perturbation creation Panel
		JPanel left = new JPanel(new BorderLayout());

		// Model selection Panel
		this.lTop = new JPanel(new FlowLayout());
		List<LogicalModel> modelList = new ArrayList<LogicalModel>(this.epithelium.getEpitheliumGrid().getModelSet());
		JComboBox<String> jcbSBML = this.newModelCombobox(modelList);
		this.lTop.add(jcbSBML);
		this.lTop.setBorder(BorderFactory.createTitledBorder(Txt.get("s_MODEL_SELECT")));
		left.add(this.lTop, BorderLayout.NORTH);

		this.jpCenter = new JPanel(new BorderLayout());
		this.jpCenter.setBorder(BorderFactory.createTitledBorder(Txt.get("s_TAB_PERTURB_LIST")));
		left.add(jpCenter, BorderLayout.CENTER);

		JPanel jpLeftAggreg = new JPanel(new BorderLayout());
		jpLeftAggreg.add(left, BorderLayout.LINE_START);
		jpLeftAggreg.add(this.gridInfo, BorderLayout.LINE_END);

		this.center.add(jpLeftAggreg, BorderLayout.LINE_START);
		LogicalModel m = Project.getInstance().getProjectFeatures().getModel((String) jcbSBML.getSelectedItem());
		updatePanelsWithModel(m);
		this.isInitialized = true;
	}

	private JComboBox<String> newModelCombobox(List<LogicalModel> modelList) {
		// Model selection list
		String[] saSBML = new String[modelList.size()];
		for (int i = 0; i < modelList.size(); i++) {
			saSBML[i] = Project.getInstance().getProjectFeatures().getModelName(modelList.get(i));
		}
		JComboBox<String> jcb = new JComboWideBox<String>(saSBML);
		jcb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unchecked")
				JComboBox<String> jcb = (JComboBox<String>) e.getSource();
				LogicalModel m = Project.getInstance().getProjectFeatures().getModel((String) jcb.getSelectedItem());
				updatePanelsWithModel(m);
			}
		});
		return jcb;
	}

	private void updatePanelsWithModel(LogicalModel m) {
		this.selModel = m;
		this.visualGridPerturb.setModel(this.selModel);
		this.mAP2Checkbox.clear();
		this.mAP2RadioButton.clear();
		for (JButton jb : this.mAP2JButton.values())
			this.jrbGroup.remove(jb);
		this.mAP2JButton.clear();

		// Center Panel
		this.jpCenter.removeAll();
		this.jpCenter.repaint();

		// Perturbation list Panel
		JPanel jpPerturbTop = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 0;
		gbc.gridx = 0;
		gbc.anchor = GridBagConstraints.WEST;
		jpPerturbTop.add(new JLabel(Txt.get("s_TAB_PERTURB_NODE")), gbc);

		Set<String> sProper = Project.getInstance().getProjectFeatures().getModelNodeIDs(this.selModel, false);

		String[] saProper = new String[sProper.size()];
		int i = 0;
		for (String nodeID : sProper)
			saProper[i++] = nodeID;
		this.jcbComps = new JComboBox<String>(saProper);
		this.jcbComps.setToolTipText(this.jcbComps.getSelectedItem().toString());
		jcbComps.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unchecked")
				JComboBox<String> jcb = (JComboBox<String>) e.getSource();
				String nodeID = (String) jcb.getSelectedItem();
				jcb.setToolTipText(nodeID);
				updateMinMaxValues(nodeID);
			}
		});
		gbc.gridx = 1;
		jpPerturbTop.add(jcbComps, gbc);
		gbc.gridy = 1;
		gbc.gridx = 0;
		gbc.anchor = GridBagConstraints.WEST;
		jpPerturbTop.add(new JLabel(Txt.get("s_TAB_PERTURB_MIN_V")), gbc);
		jcbMinVal = new JComboBox<Byte>();
		gbc.gridx = 1;
		jpPerturbTop.add(jcbMinVal, gbc);
		gbc.gridy = 2;
		gbc.gridx = 0;
		gbc.anchor = GridBagConstraints.WEST;
		jpPerturbTop.add(new JLabel(Txt.get("s_TAB_PERTURB_MAX_V")), gbc);
		jcbMaxVal = new JComboBox<Byte>();
		gbc.gridx = 1;
		jpPerturbTop.add(jcbMaxVal, gbc);
		updateMinMaxValues(saProper[0]);
		JPanel jpTmp = new JPanel(new FlowLayout());

		// Create
		JButton jbCreate = ButtonFactory.getNoMargins(Txt.get("s_TAB_PERTURB_CREATE"));
		jbCreate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String nodeID = (String) jcbComps.getSelectedItem();
				NodeInfo node = Project.getInstance().getProjectFeatures().getNodeInfo(nodeID);
				byte min = (Byte) jcbMinVal.getSelectedItem();
				byte max = (Byte) jcbMaxVal.getSelectedItem();
				LogicalModelPerturbation ap;

				if (max < min) {

					jcbMaxVal.setSelectedItem(min);
					jcbMaxVal.repaint();
					max = min;
					ap = new FixedValuePerturbation(node, min);
				} else if (min == max) {
					ap = new FixedValuePerturbation(node, min);
				} else {
					ap = new RangePerturbation(node, min, max);
				}

				if (!mID2AP.containsKey(ap.toString())) {
					epiPerturbClone.addPerturbation(ap);
					mID2AP.put(ap.toString(), ap);
					Color c = epiPerturbClone.getPerturbationColor(ap);
					if (c == null) {
						c = ColorUtils.random();
					}
					addColor2MarkPanel(ap, c);
					tpc.setChanged();
					repaintAPColorsPanel();
				}
			}
		});
		jpTmp.add(jbCreate);
		// Delete

		JButton jbDelete = ButtonFactory.getNoMargins(Txt.get("s_TAB_PERTURB_DELETE"));
		jbDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				List<LogicalModelPerturbation> lTmp = new ArrayList<LogicalModelPerturbation>(mAP2Checkbox.keySet());
				for (LogicalModelPerturbation ap : lTmp) {
					JCheckBox jcb = mAP2Checkbox.get(ap);
					if (jcb.isSelected()) {
						if (hasCellGridClone(ap)) {
							NotificationManager.warning("EpiTabPerturbations",
									"Some cells are still assigned with the selected perturbation");
							NotificationManager.dispatchDialogWarning(false, false);
						} else {
							mAP2RadioButton.remove(ap);
							jrbGroup.remove(mAP2JButton.get(ap));
							mAP2JButton.remove(ap);
							mAP2Checkbox.remove(ap);
							mID2AP.remove(ap.toString());
							epiPerturbClone.delPerturbation(ap);
							tpc.setChanged();
							repaintAPColorsPanel();
						

						}
						return;
					}
				}
			}
		});
		jpTmp.add(jbDelete);
		gbc.gridy = 3;
		gbc.gridx = 0;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		jpPerturbTop.add(jpTmp, gbc);

		JButton jbMultiple = ButtonFactory.getNoMargins(Txt.get("s_TAB_PERTURB_MULT_BU"));
		jbMultiple.setToolTipText(Txt.get("s_TAB_PERTURB_MULT_BU_DESC"));
		jbMultiple.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				List<LogicalModelPerturbation> lAPs = new ArrayList<LogicalModelPerturbation>();
				for (LogicalModelPerturbation ap : mAP2Checkbox.keySet()) {
					if (mAP2Checkbox.get(ap).isSelected()) {
						lAPs.add(ap);
					}
					mAP2Checkbox.get(ap).setSelected(false);
				}
				if (lAPs.size() > 1) {
					// TODO: create personalized message saying why it is not possible to create
					// this multiple perturbation (only one
					Collections.sort(lAPs, ObjectComparator.ABSTRACT_PERTURB);
					List<LogicalModelPerturbation> lAPsClean = new ArrayList<LogicalModelPerturbation>();
					for (int i = 0; i < lAPs.size(); i++) {
						boolean sub = false;
						String component_A = lAPs.get(i).toString().split(" ")[0];
						for (int j = i + 1; j < lAPs.size(); j++) {
							String component_B = lAPs.get(j).toString().split(" ")[0];
							if (component_A.equals(component_B)) {
								sub = true;
								break;
							}
						}
						if (!sub) {
							lAPsClean.add(lAPs.get(i));
						}
					}
					MultiplePerturbation<LogicalModelPerturbation> mulap = new MultiplePerturbation<LogicalModelPerturbation>(
							lAPsClean);
					if (lAPsClean.size() != lAPs.size()) {
						NotificationManager.warning("EpiTabPerturbations", Txt.get("s_TAB_PERTURB_COMB"));
						NotificationManager.dispatchDialogWarning(false, false);
					} else if (mID2AP.containsKey(mulap.toString())) {
						NotificationManager.warning("EpiTabPerturbations", Txt.get("s_TAB_PERTURB_REPEAT"));
						NotificationManager.dispatchDialogWarning(false, false);
					} else {
						epiPerturbClone.addPerturbation(mulap);
						mID2AP.put(mulap.toString(), mulap);
						Color c = epiPerturbClone.getPerturbationColor(mulap);
						if (c == null) {
							c = ColorUtils.random();
						}
						addColor2MarkPanel(mulap, c);
						tpc.setChanged();
						repaintAPColorsPanel();
					}
				}
			}
		});
		gbc.gridy = 4;
		jpPerturbTop.add(new JSeparator(SwingConstants.HORIZONTAL));
		gbc.gridy = 5;
		jpPerturbTop.add(jbMultiple, gbc);

		this.jpCenter.add(jpPerturbTop, BorderLayout.NORTH);
		
			for (LogicalModelPerturbation ap : epiPerturbClone.getAllCreatedPerturbations()) {
				this.mID2AP.put(ap.toString(), ap);
		}
		this.jpCenter.add(this.jspRBColor, BorderLayout.CENTER);

		JPanel jpColorApplyClear = new JPanel(new FlowLayout());
		JButton jbApplyAll = ButtonFactory.getNoMargins("Apply all");
		jbApplyAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				visualGridPerturb.applyDataToAll();
			}
		});
		jpColorApplyClear.add(jbApplyAll);
		JPanel jpColorRect = new JPanel(new FlowLayout());
		JToggleButton jtbRectFill = new JToggleButton("Rectangle fill", false);
		jtbRectFill.setMargin(new Insets(0, 0, 0, 0));
		jtbRectFill.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				JToggleButton jtb = (JToggleButton) e.getSource();
				visualGridPerturb.isRectangleFill(jtb.isSelected());
			}
		});
		jpColorRect.add(jtbRectFill);

		JPanel jpColorTop = new JPanel(new BorderLayout());
		jpColorTop.setBorder(BorderFactory.createTitledBorder("Apply selection"));
		jpColorTop.add(jpColorApplyClear, BorderLayout.PAGE_START);
		jpColorTop.add(jpColorRect, BorderLayout.PAGE_END);

		this.jpCenter.add(jpColorTop, BorderLayout.PAGE_END);

		

			for (LogicalModelPerturbation ap : this.epiPerturbClone.getVisiblePerturbations(this.selModel)) {
				Color c = this.epiPerturbClone.getPerturbationColor(ap);
				this.addColor2MarkPanel(ap, c);
			}
		this.repaintAPColorsPanel();

		// Re-Paint
		this.getParent().repaint();
	}

	private boolean hasCellGridClone(LogicalModelPerturbation ap) {
		for (int x = 0; x < this.epiGridClone.getX(); x++) {
			for (int y = 0; y < this.epiGridClone.getY(); y++) {
				LogicalModelPerturbation cellAP = this.epiGridClone.getPerturbation(x, y);
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
		for (LogicalModelPerturbation ap : this.epiPerturbClone.getVisiblePerturbations(this.selModel)) {
			if (this.epiPerturbClone.getAllCreatedPerturbations() == null)
				continue;
			gbc.gridy = y;
			gbc.gridx = 0;
			gbc.ipadx = 5;
			gbc.anchor = GridBagConstraints.WEST;
			this.jpRBColor.add(this.mAP2Checkbox.get(ap), gbc);
			gbc.gridx = 1;
			this.jpRBColor.add(new JLabel(ap.toString()), gbc);
			gbc.gridx = 2;
			this.jpRBColor.add(this.mAP2JButton.get(ap), gbc);
			gbc.gridx = 3;
			this.jpRBColor.add(this.mAP2RadioButton.get(ap), gbc);
			y++;
		}
		// Clear cell
		gbc.gridy = y;
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridwidth = 2;
		this.jpRBColor.add(new JLabel("Clear cell"), gbc);
		JRadioButton jrDel = new JRadioButton();
		jrDel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				visualGridPerturb.setSelAbsPerturb(null);
			}
		});
		gbc.gridx = 3;
		this.jpRBColor.add(jrDel, gbc);
		// TODO jrDel.setSelected is not setting selected abstract perturbations
		// to null
		this.jrbGroup.add(jrDel);
		jrDel.setSelected(true);
		this.visualGridPerturb.setSelAbsPerturb(null);
		this.jpRBColor.revalidate();
		this.jpRBColor.repaint();
	}

	private void addColor2MarkPanel(LogicalModelPerturbation ap, Color c) {

		// JCheckbox
		JCheckBox jcb = new JCheckBox();
		jcb.setToolTipText(ap.toString());
		this.mAP2Checkbox.put(ap, jcb);
		// JRadioButton
		JRadioButton jrb = new JRadioButton();
		jrb.setToolTipText(ap.toString());
		jrb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JRadioButton jrb = (JRadioButton) e.getSource();
				LogicalModelPerturbation ap = mID2AP.get(jrb.getToolTipText());
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
				setNewColor(jb);
			}
		});
		this.mAP2JButton.put(ap, jbColor);
		this.epiPerturbClone.addPerturbationColor(ap, c);
	}

	/**
	 * Changes the color associated with a perturbation. PV: Tab knows that it
	 * changed if the new color exists and it is different from the previous color.
	 * 
	 * @param jb
	 */
	private void setNewColor(JButton jb) {
		String apID = jb.getToolTipText();
		Color newColor = JColorChooser.showDialog(jb, "Color chooser - " + apID, jb.getBackground());
		if (newColor != null && newColor != jb.getBackground()) {
			jb.setBackground(newColor);
			this.tpc.setChanged();
			LogicalModelPerturbation ap = mID2AP.get(apID);
			epiPerturbClone.addPerturbationColor(ap, newColor);
			
			visualGridPerturb.paintComponent(visualGridPerturb.getGraphics());
		}
	}

	private void updateMinMaxValues(String nodeID) {
		jcbMinVal.removeAllItems();
		jcbMaxVal.removeAllItems();
		//TODO: Bug here
		this.maxValue = Project.getInstance().getProjectFeatures().getNodeInfo(nodeID).getMax();
		for (byte b = 0; b <= this.maxValue; b++) {
			jcbMinVal.addItem(b);
			jcbMaxVal.addItem(b);
		}
	}

	@Override
	protected void buttonReset() {
		// Reset modifications on perturbation grid clone
		EpitheliumGrid grid = this.epithelium.getEpitheliumGrid();
		for (int x = 0; x < grid.getX(); x++) {
			for (int y = 0; y < grid.getY(); y++) {
				LogicalModelPerturbation apOrig = grid.getPerturbation(x, y);
				this.epiGridClone.setPerturbation(x, y, apOrig);
			}
		}
		// Reset modifications on ModelPerturbations
		this.epiPerturbClone = this.epithelium.getEpitheliumPerturbations().clone();
		updatePanelsWithModel(this.selModel);
	}

	@Override
	protected void buttonAccept() {
		
		// Check modifications on EpitheliumPerturbations
		EpitheliumPerturbations epOrig = this.epithelium.getEpitheliumPerturbations();
		epOrig.delAllPerturbations();
		epOrig.delAllPerturbationsColors();
		for (LogicalModelPerturbation ap: this.epiPerturbClone.getAllCreatedPerturbations()) {
			epOrig.addPerturbation(ap);
			epOrig.addPerturbationColor(ap, this.epiPerturbClone.getPerturbationColor(ap));
		}
		
		// Check modifications on perturbation grid clone
		EpitheliumGrid grid = this.epithelium.getEpitheliumGrid();
		for (int x = 0; x < grid.getX(); x++) {
			for (int y = 0; y < grid.getY(); y++) {
				LogicalModelPerturbation apClone = this.epiGridClone.getPerturbation(x, y);
				grid.setPerturbation(x, y, apClone);			}
		}
		
		}

	@Override
	protected boolean isChanged() {
		// TODO: Isn't it enough to use this.tpc.isChanged() as return?
		// Check modifications on perturbation grid clone
		EpitheliumGrid grid = this.epithelium.getEpitheliumGrid();
		for (int x = 0; x < grid.getX(); x++) {
			for (int y = 0; y < grid.getY(); y++) {
				LogicalModelPerturbation apClone = this.epiGridClone.getPerturbation(x, y);
				if (apClone != null && grid.getPerturbation(x, y) == null
						|| apClone == null && grid.getPerturbation(x, y) != null || apClone != null
								&& grid.getPerturbation(x, y) != null && !grid.getPerturbation(x, y).equals(apClone)) {
					return true;
				}
			}
		}

		return this.tpc.isChanged();
	}

	@Override
	public void applyChange() {
		List<LogicalModel> modelList = new ArrayList<LogicalModel>(this.epithelium.getEpitheliumGrid().getModelSet());

		this.lTop.removeAll();
		this.lTop.add(this.newModelCombobox(modelList));
		
		// Update grid
		EpitheliumGrid grid = this.epithelium.getEpitheliumGrid();
		for (int x = 0; x < grid.getX(); x++) {
			for (int y = 0; y < grid.getY(); y++) {
				if (!grid.getModel(x, y).equals(this.epiGridClone.getModel(x, y))) {
					this.epiGridClone.setModel(x, y, grid.getModel(x, y));
					this.epiGridClone.setPerturbation(x, y, grid.getPerturbation(x, y));
				}
			}
		}
		if (this.selModel!=null) this.updatePanelsWithModel(this.selModel);
		else this.updatePanelsWithModel(modelList.get(0));
		
	}
}
