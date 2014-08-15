package org.ginsim.epilog.gui.tab;

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
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.tree.TreePath;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;
import org.colomoto.logicalmodel.perturbation.AbstractPerturbation;
import org.colomoto.logicalmodel.perturbation.FixedValuePerturbation;
import org.colomoto.logicalmodel.perturbation.RangePerturbation;
import org.ginsim.epilog.ProjectModelFeatures;
import org.ginsim.epilog.core.Epithelium;
import org.ginsim.epilog.core.EpitheliumCell;
import org.ginsim.epilog.core.EpitheliumGrid;
import org.ginsim.epilog.core.ModelPerturbations;
import org.ginsim.epilog.gui.widgets.VisualGridPerturbation;
import org.ginsim.epilog.io.ButtonFactory;

public class EpiTabPerturbations extends EpiTabDefinitions {
	private static final long serialVersionUID = -1795100027288146018L;

	private VisualGridPerturbation visualGridPerturb;
	private EpitheliumCell[][] cellGridClone;
	private Map<AbstractPerturbation, Color> colorMapClone;

	private JPanel jpCenter;
	private JComboBox<String> jcbComps;
	private JComboBox<Byte> jcbMinVal;
	private JComboBox<Byte> jcbMaxVal;
	private JList<AbstractPerturbation> jlPerturb;

	public EpiTabPerturbations(Epithelium e, TreePath path,
			ProjectModelFeatures modelFeatures) {
		super(e, path, modelFeatures);
	}

	public void initialize() {
		this.center.setLayout(new BorderLayout());

		this.colorMapClone = new HashMap<AbstractPerturbation, Color>();

		EpitheliumGrid grid = this.epithelium.getEpitheliumGrid();
		this.cellGridClone = new EpitheliumCell[grid.getX()][grid.getY()];
		for (int x = 0; x < this.cellGridClone.length; x++) {
			for (int y = 0; y < this.cellGridClone[0].length; y++) {
				this.cellGridClone[x][y] = grid.cloneEpitheliumCellAt(x, y);
			}
		}

		this.visualGridPerturb = new VisualGridPerturbation(this.epithelium
				.getEpitheliumGrid().getX(), this.epithelium
				.getEpitheliumGrid().getY(), this.epithelium
				.getEpitheliumGrid().getTopology(), this.cellGridClone,
				colorMapClone);
		this.center.add(this.visualGridPerturb, BorderLayout.CENTER);

		// Perturbation creation Panel
		JPanel left = new JPanel(new BorderLayout());

		// Model selection Panel
		JPanel lTop = new JPanel(new FlowLayout());
		List<LogicalModel> modelList = new ArrayList<LogicalModel>(
				this.epithelium.getEpitheliumGrid().getModelSet());
		String[] saSBML = new String[modelList.size()];
		for (int i = 0; i < modelList.size(); i++) {
			saSBML[i] = this.modelFeatures.getName(modelList.get(i));
		}
		JComboBox<String> jcbSBML = new JComboBox<String>(saSBML);
		jcbSBML.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unchecked")
				JComboBox<String> jcb = (JComboBox<String>) e.getSource();
				updatePanelsWithModel((String) jcb.getSelectedItem());
			}
		});
		lTop.add(jcbSBML);
		lTop.setBorder(BorderFactory.createTitledBorder("Model selection"));
		left.add(lTop, BorderLayout.NORTH);

		this.jpCenter = new JPanel(new BorderLayout());
		left.add(jpCenter, BorderLayout.CENTER);
		this.center.add(left, BorderLayout.LINE_START);
		updatePanelsWithModel((String) jcbSBML.getSelectedItem());
	}

	private void updatePanelsWithModel(String sModel) {
		LogicalModel m = this.modelFeatures.getModel(sModel);
		this.visualGridPerturb.setModel(m);

		// Center Panel
		this.jpCenter.removeAll();

		// Perturbation list Panel
		JPanel jpPerturbList = new JPanel();
		jpPerturbList.setBorder(BorderFactory
				.createTitledBorder("Perturbation list"));
		JPanel jpPerturbTop = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 0;
		gbc.gridx = 0;
		gbc.anchor = GridBagConstraints.WEST;
		jpPerturbTop.add(new JLabel("Component:"), gbc);
		Set<String> sProper = this.epithelium.getComponentFeatures()
				.getModelComponents(m, false);
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
				NodeInfo node = epithelium.getComponentFeatures().getNodeInfo(
						nodeID);
				byte min = (Byte) jcbMinVal.getSelectedItem();
				byte max = (Byte) jcbMaxVal.getSelectedItem();
				AbstractPerturbation ap;
				if (min == max)
					ap = new FixedValuePerturbation(node, min);
				else
					ap = new RangePerturbation(node, min, max);
				DefaultListModel<AbstractPerturbation> lm = (DefaultListModel<AbstractPerturbation>) jlPerturb
						.getModel();
				if (!lm.contains(ap))
					lm.addElement(ap);
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
				for (int p = 0; p < lm.getSize(); p++) {
					lm.removeElementAt(p);
				}
			}
		});
		jpTmp.add(jbDelete);
		gbc.gridy = 3;
		gbc.gridx = 0;
		gbc.gridwidth = 2;
		jpPerturbTop.add(jpTmp, gbc);

		jpPerturbList.add(jpPerturbTop, BorderLayout.NORTH);

		DefaultListModel<AbstractPerturbation> dlmAPs = new DefaultListModel<AbstractPerturbation>();
		for (AbstractPerturbation ap : this.epithelium.getPerturbations(m)
				.getAllPerturbations()) {
			dlmAPs.addElement(ap);
		}
		JList<AbstractPerturbation> jlPerturbCenter = new JList<AbstractPerturbation>(
				dlmAPs);
		jlPerturbCenter
				.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		// jlPerturbCenter.setVisibleRowCount(-1);

		jpPerturbList.add(jlPerturbCenter, BorderLayout.CENTER);

		jpTmp = new JPanel(new FlowLayout());
		JButton jbMultiple = ButtonFactory.getNoMargins("Create Multiple");
		jbMultiple.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

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

			}
		});
		jpAddDel.add(jbAdd);
		JButton jbDel = ButtonFactory.getNoMargins("<-");
		jbDel.setAlignmentY(CENTER_ALIGNMENT);
		jbDel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});
		jpAddDel.add(jbDel);
		jpAddDel.add(Box.createVerticalGlue());

		// Color Marking Panel
		JPanel jpColorMark = new JPanel(new BorderLayout());
		jpColorMark.setBorder(BorderFactory
				.createTitledBorder("Mark cells with"));

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
				visualGridPerturb.applyDataToAll();
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

		JPanel jpColorTop = new JPanel(new BorderLayout());
		jpColorTop.add(jpColorApplyClear, BorderLayout.PAGE_START);
		jpColorTop.add(jpColorRect, BorderLayout.CENTER);
		jpColorMark.add(jpColorTop, BorderLayout.PAGE_END);

		jpCenter.add(jpPerturbList, BorderLayout.LINE_START);
		jpCenter.add(jpAddDel, BorderLayout.CENTER);
		jpCenter.add(jpColorMark, BorderLayout.LINE_END);

		// Re-Paint
		this.getParent().repaint();
	}

	private void updateMinMaxValues(String nodeID) {
		jcbMinVal.removeAllItems();
		jcbMaxVal.removeAllItems();
		byte max = epithelium.getComponentFeatures().getNodeInfo(nodeID)
				.getMax();
		for (byte b = 0; b <= max; b++) {
			jcbMinVal.addItem(b);
			jcbMaxVal.addItem(b);
		}
	}

	@Override
	protected void buttonReset() {
		// Check modifications on perturbation grid clone
		EpitheliumGrid grid = this.epithelium.getEpitheliumGrid();
		for (int x = 0; x < this.cellGridClone.length; x++) {
			for (int y = 0; y < this.cellGridClone[0].length; y++) {
				AbstractPerturbation apOrig = grid.getPerturbation(x, y);
				this.cellGridClone[x][y].setPerturbation(apOrig);
			}
		}
	}

	@Override
	protected void buttonAccept() {
		// Check modifications on perturbation grid clone
		EpitheliumGrid grid = this.epithelium.getEpitheliumGrid();
		for (int x = 0; x < this.cellGridClone.length; x++) {
			for (int y = 0; y < this.cellGridClone[0].length; y++) {
				AbstractPerturbation apClone = this.cellGridClone[x][y]
						.getPerturbation();
				grid.setPerturbation(x, y, apClone);
			}
		}
	}

	@Override
	protected boolean isChanged() {
		// Check modifications on perturbation grid clone
		EpitheliumGrid grid = this.epithelium.getEpitheliumGrid();
		for (int x = 0; x < this.cellGridClone.length; x++) {
			for (int y = 0; y < this.cellGridClone[0].length; y++) {
				AbstractPerturbation apClone = this.cellGridClone[x][y]
						.getPerturbation();
				if (apClone != grid.getPerturbation(x, y)
						&& !grid.getPerturbation(x, y).equals(apClone)) {
					return true;
				}
			}
		}
		return false;
	}
}
