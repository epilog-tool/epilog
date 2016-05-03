package org.epilogtool.gui.tab;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.tree.TreePath;

import org.colomoto.logicalmodel.LogicalModel;
import org.epilogtool.core.Epithelium;
import org.epilogtool.core.cellDynamics.CellTrigger;
import org.epilogtool.core.cellDynamics.EpitheliumTriggerManager;
import org.epilogtool.gui.EpiGUI.EpiTabChanged;
import org.epilogtool.gui.EpiGUI.ProjectChangedInTab;
import org.epilogtool.gui.widgets.JComboWideBox;
import org.epilogtool.project.ProjectFeatures;

public class EpiTabCellDynamics extends EpiTabDefinitions {
	
	private EpitheliumTriggerManager cellStatusManager;
	private String activeModel;
	private JPanel jpNLBottom;
	private JPanel jpNRTop;
	private JPanel jpNRBottom;
	private JPanel jpNLTop;

	private final int JTF_WIDTH = 30;

	public EpiTabCellDynamics(Epithelium e, TreePath path,
			ProjectChangedInTab projChanged, EpiTabChanged tabChanged,
			ProjectFeatures projectFeatures) {
		super(e, path, projChanged, tabChanged, projectFeatures);
	}

	@Override
	public void initialize() {
		this.center.setLayout(new BorderLayout());
		this.cellStatusManager = this.epithelium.getCellStatusManager().clone();

		// North Panel
		JPanel jpNorth = new JPanel(new BorderLayout());
		JPanel jpNLeft = new JPanel(new BorderLayout());
		jpNorth.add(jpNLeft, BorderLayout.LINE_START);

		// Model selection list
		List<LogicalModel> modelList = new ArrayList<LogicalModel>(
				this.epithelium.getEpitheliumGrid().getModelSet());
		JComboBox<String> jcbSBML = this.newModelComboBox(modelList);
		this.jpNLTop = new JPanel();
		this.jpNLTop.setBorder(BorderFactory
				.createTitledBorder("Model selection"));
		this.jpNLTop.add(jcbSBML);
		jpNLeft.add(this.jpNLTop, BorderLayout.NORTH);
		
	}
	
	private JComboBox<String> newModelComboBox(List<LogicalModel> modelList) {
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
				activeModel = (String) jcb.getSelectedItem();
				// Re-Paint
				getParent().repaint();
			}
		});
		return jcb;
	}
	
	private void updateModelStatusManager() {
		this.jpNRTop.removeAll();
		this.jpNRBottom.removeAll();
		ButtonGroup group = new ButtonGroup();
		JRadioButton jrModelProliferation = new JRadioButton("Proliferation");
		jrModelProliferation.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				paintModelProliferation();
				// Re-Paint
				getParent().repaint();
			}
		});
		group.add(jrModelProliferation);
		this.jpNRTop.add(jrModelProliferation);
		
		JRadioButton jrModelApoptosis = new JRadioButton("Apoptosis");
		jrModelApoptosis.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//paintModelApoptosis();
				// Re-Paint
				getParent().repaint();
			}
		});
		group.add(jrModelApoptosis);
		this.jpNRTop.add(jrModelApoptosis);
		LogicalModel m = this.epithelium.getProjectFeatures().getModel(
				this.activeModel);

	}
	
	private void paintModelProliferation() {
		this.jpNRBottom.removeAll();
		LogicalModel m = this.projectFeatures.getModel(this.activeModel);
		List<String[]> patterns = new ArrayList<String[]>(this.cellStatusManager
				.getCellStatusManager(m)
				.getTrigger2PatternMap()
				.get(CellTrigger.PROLIFERATION));
		GridBagConstraints gbc = new GridBagConstraints();
		for (int i = 0; i < patterns.size(); i++) {
			gbc.gridy = i;
			gbc.ipady = 5;
			gbc.insets.bottom = 5;
			gbc.gridx = 0;
			gbc.anchor = GridBagConstraints.WEST;
			gbc.gridx = 1;
			JTextField jtf = new JTextField(Arrays.toString(patterns.get(i)));
			jtf.addKeyListener(new KeyListener() {
				@Override
				public void keyTyped(KeyEvent e) {
				}

				@Override
				public void keyReleased(KeyEvent e) {
				}

				@Override
				public void keyPressed(KeyEvent e) {
				}
			});
			jtf.setColumns(this.JTF_WIDTH);
			this.jpNRBottom.add(jtf, gbc);
		}

	}

	@Override
	protected void buttonReset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void buttonAccept() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean isChanged() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void applyChange() {
		// TODO Auto-generated method stub
		
	}

}
