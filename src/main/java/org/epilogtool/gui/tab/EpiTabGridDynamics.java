package org.epilogtool.gui.tab;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.tree.TreePath;

import org.colomoto.logicalmodel.LogicalModel;
import org.epilogtool.core.Epithelium;
import org.epilogtool.core.cellDynamics.CellTrigger;
import org.epilogtool.core.cellDynamics.ModelDynamics;
import org.epilogtool.core.cellDynamics.EpitheliumDynamics;
import org.epilogtool.core.cellDynamics.TriggerPattern;
import org.epilogtool.gui.EpiGUI.EpiTabChanged;
import org.epilogtool.gui.EpiGUI.ProjectChangedInTab;
import org.epilogtool.gui.widgets.JComboWideBox;
import org.epilogtool.io.ButtonFactory;
import org.epilogtool.project.ProjectFeatures;

public class EpiTabGridDynamics extends EpiTabDefinitions {
	private static final long serialVersionUID = 4613661342531014915L;
	private final int JTF_WIDTH = 30;
	
	private EpitheliumDynamics epiTriggerManager;
	private String activeModel;
	private String triggerType;
	private JPanel jpTop;
	private JPanel jpRTop;
	private JPanel jpLTop;
	private JPanel jpRBottom;

	public EpiTabGridDynamics(Epithelium e, TreePath path,
			ProjectChangedInTab projChanged, EpiTabChanged tabChanged,
			ProjectFeatures projectFeatures) {
		super(e, path, projChanged, tabChanged, projectFeatures);
	}
	
	@Override
	public void initialize() {
		
		this.epiTriggerManager = this.epithelium.getEpitheliumTriggerManager().clone();
		
		this.center.setLayout(new BorderLayout());
		
		this.jpTop = new JPanel(new BorderLayout());
		this.center.add(jpTop, BorderLayout.NORTH);
		
		this.jpLTop = new JPanel();
		this.jpLTop.setBorder(BorderFactory
				.createTitledBorder("Model selection"));
		this.jpTop.add(jpLTop, BorderLayout.WEST);
		
		this.jpRTop = new JPanel();
		this.jpRTop.setBorder(BorderFactory.createTitledBorder("Event type"));
		this.jpTop.add(jpRTop, BorderLayout.CENTER);
		
		this.jpRBottom = new JPanel(new BorderLayout());
		this.center.add(jpRBottom, BorderLayout.CENTER);
		
		List<LogicalModel> modelList = new ArrayList<LogicalModel>(
				this.epithelium.getEpitheliumGrid().getModelSet());
		JComboBox<String> jcbSBML = this.newModelCombobox(modelList);
		this.jpLTop.add(jcbSBML);
		this.activeModel = (String) jcbSBML.getSelectedItem();
		
		this.getTriggerEventPanel();
		this.updateModelDynamicsPanel();
	
		JButton jbAdd = ButtonFactory.getNoMargins("+");
		jbAdd.setToolTipText("Add a new pattern");
		jbAdd.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					addPattern();
				}
			});
		this.jpRBottom.add(jbAdd, BorderLayout.NORTH);
		
		this.isInitialized = true;
		
		
	}
	
	private JComboBox<String> newModelCombobox(List<LogicalModel> modelList) {
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
				updateModelDynamicsPanel();
				// Re-Paint
				getParent().repaint();
			}
		});
		return jcb;
	}
	
	private void getTriggerEventPanel() {
		ButtonGroup group = new ButtonGroup();
		JRadioButton jrbProliferation = new JRadioButton("Proliferation");
		jrbProliferation.setSelected(true);
		this.triggerType = "Proliferation";
		this.jpRTop.add(jrbProliferation);
		jrbProliferation.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				triggerType = "Proliferation";
				updateModelDynamicsPanel();
			}
		});
		group.add(jrbProliferation);
		JRadioButton jrbApoptosis = new JRadioButton("Apoptosis");
		this.jpRTop.add(jrbApoptosis);
		jrbApoptosis.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				triggerType = "Apoptosis";
				updateModelDynamicsPanel();
			}
		});
		group.add(jrbApoptosis);
	}
	
	private void updateModelDynamicsPanel() {
		this.jpRBottom.removeAll();
		
		JButton jbAdd = ButtonFactory.getNoMargins("+");
		jbAdd.setToolTipText("Add a new pattern");
		jbAdd.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					addPattern();
				}
			});
		this.jpRBottom.add(jbAdd, BorderLayout.NORTH);
		
		JPanel jpPatternPanel = new JPanel(new GridBagLayout());
		this.jpRBottom.add(jpPatternPanel);
		
		LogicalModel m = this.epithelium.getProjectFeatures().getModel(this.activeModel);
		List<TriggerPattern> patternList = this.epiTriggerManager
		.getTriggerManager(m)
		.getTriggerPatterns(CellTrigger.string2CellTrigger(this.triggerType));
		GridBagConstraints gbc = new GridBagConstraints();
		for (int i = 0; i < patternList.size(); i ++) {
			gbc.gridy = i;
			gbc.ipady = 5;
			gbc.insets.bottom = 5;
			gbc.gridx = 0;
			gbc.anchor = GridBagConstraints.WEST;
			
			JButton jbRemove = ButtonFactory.getNoMargins("X");
			jbRemove.setToolTipText("Remove this pattern");
			jbRemove.setActionCommand("" + i);
			jbRemove.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JButton jbRemove = (JButton) e.getSource();
					removePattern(Integer.parseInt(jbRemove.getActionCommand()));
				}
			});
			jpPatternPanel.add(jbRemove, gbc);
			
			gbc.gridx = 1;
			JTextField jtf = new JTextField(patternList.get(i).getPatternExpression());
			jtf.setToolTipText("" + i);
			
			jtf.addKeyListener(new KeyListener() {
				@Override
				public void keyTyped(KeyEvent e) {
				}

				@Override
				public void keyReleased(KeyEvent e) {
					JTextField jtf = (JTextField) e.getSource();
					setPatternExpression(Integer.parseInt(jtf.getToolTipText()), 
							jtf.getText());
				}

				@Override
				public void keyPressed(KeyEvent e) {
				}
			});
			jtf.setColumns(this.JTF_WIDTH);
			jpPatternPanel.add(jtf, gbc);
		}
		this.getParent().repaint();
	}
	
	private void setPatternExpression(int i, String expression) {
		LogicalModel m = this.epithelium.getProjectFeatures().getModel(this.activeModel);
		CellTrigger trigger = CellTrigger.string2CellTrigger(this.triggerType);
		this.epiTriggerManager.getTriggerManager(m).getTriggerPatterns(trigger).get(i).setPatternExpression(expression, m);
	}
	
	private void addPattern() {
		LogicalModel m = this.epithelium.getProjectFeatures().getModel(this.activeModel);
		CellTrigger trigger = CellTrigger.string2CellTrigger(this.triggerType);
		this.epiTriggerManager.getTriggerManager(m).getTriggerPatterns(trigger).add(new TriggerPattern());
		this.updateModelDynamicsPanel();
	}
	
	private void removePattern(int i) {
		LogicalModel m = this.epithelium.getProjectFeatures().getModel(this.activeModel);
		CellTrigger trigger = CellTrigger.string2CellTrigger(this.triggerType);
		this.epiTriggerManager.getTriggerManager(m).getTriggerPatterns(trigger).remove(i);
		this.updateModelDynamicsPanel();
	}

	@Override
	protected void buttonReset() {
		this.epiTriggerManager = this.epithelium.getEpitheliumTriggerManager().clone();
		this.updateModelDynamicsPanel();
		this.getParent().repaint();
		
	}

	@Override
	protected void buttonAccept() {
		for (LogicalModel m : this.epiTriggerManager.getModelSet()) {
			List<TriggerPattern> tmpList = new ArrayList<TriggerPattern>();
			for (CellTrigger trigger : this.epiTriggerManager.getTriggerManager(m).getCellTriggerSet()) {
				if (this.epiTriggerManager.getTriggerManager(m).getTriggerPatterns(trigger)
						.equals(this.epithelium.getEpitheliumTriggerManager().getTriggerManager(m).getTriggerPatterns(trigger))) {
					tmpList.addAll(this.epiTriggerManager.getTriggerManager(m).getTriggerPatterns(trigger));
					continue;
				}
				for (TriggerPattern pattern : this.epiTriggerManager.getTriggerManager(m).getTriggerPatterns(trigger)) {
					if (pattern.getPatternExpression()==null
							|| !(pattern.isExpressionValid(pattern.getPatternExpression(), m))) {
						this.callParsingError();
						return;
					}
					pattern.setComputedPattern(m);
					tmpList.add(pattern);
				}
			}
			boolean overlap = false;
			for (int i = 0; i < tmpList.size()-1; i ++) {
				for (int j = i + 1; j < tmpList.size(); j++) {
					if (tmpList.get(i).overlaps(tmpList.get(j))) {
						overlap = true;
					}
				}
			}
			if (overlap==true) {
				this.callOverLappingError();
				return;
			}
		}
		this.epithelium.setEpitheliumTriggerManager(this.epiTriggerManager.clone());
	}
	
	private void callParsingError() {
		JPanel jpErrorLog = new JPanel(new BorderLayout());
		this.jpRBottom.add(jpErrorLog, BorderLayout.SOUTH);
		JTextPane jtpErrorLog = new JTextPane();
		jtpErrorLog.setContentType("text/html");
		jtpErrorLog.setText("<html><body style=\"background-color:#ffbebe\">"
				+ "You have defined invalid patterns.<br/>"
				+ "It is not possible to save the specified settings."
				+ "</body></html>");
		jpErrorLog.add(jtpErrorLog, BorderLayout.CENTER);
		JButton jbOK = ButtonFactory.getNoMargins("OK");
		jbOK.setToolTipText("Dismiss this warning");
		jbOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateModelDynamicsPanel();
			}
		});
		jpErrorLog.add(jbOK, BorderLayout.EAST);
		this.getParent().repaint();
	}
	
	private void callOverLappingError() {
		JPanel jpOverlapLog = new JPanel(new BorderLayout());
		this.jpRBottom.add(jpOverlapLog, BorderLayout.SOUTH);
		JTextPane jtpOverlapLog = new JTextPane();
		jtpOverlapLog.setContentType("text/html");
		jtpOverlapLog.setText("<html><body style=\"background-color:#ffbebe\">"
				+ "You have defined overlapping patterns.<br/>"
				+ "It is not possible to save the specified settings."
				+ "</body></html>");
		jpOverlapLog.add(jtpOverlapLog, BorderLayout.CENTER);
		JButton jbOK = ButtonFactory.getNoMargins("OK");
		jbOK.setToolTipText("Dismiss this warning");
		jbOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateModelDynamicsPanel();
			}
		});
		jpOverlapLog.add(jbOK, BorderLayout.EAST);
		this.getParent().repaint();
	}

	@Override
	protected boolean isChanged() {
		return !(this.epiTriggerManager.equals(this.epithelium.getEpitheliumTriggerManager()));
	}

	@Override
	public void applyChange() {
		List<LogicalModel> modelList = new ArrayList<LogicalModel>(
				this.epithelium.getEpitheliumGrid().getModelSet());
		for (LogicalModel m : modelList) {
			if (this.epithelium.getEpitheliumTriggerManager().hasModel(m)
					&& !this.epiTriggerManager.hasModel(m)) {
				this.epiTriggerManager.addTriggerManager(m, 
						this.epithelium
						.getEpitheliumTriggerManager()
						.getTriggerManager(m).clone());
			}
		}
		this.jpLTop.removeAll();
		this.jpRBottom.removeAll();
		this.jpLTop.add(this.newModelCombobox(modelList));
		this.activeModel = this.projectFeatures.getModelName(modelList.get(0));
		this.updateModelDynamicsPanel();
		
	}

}
