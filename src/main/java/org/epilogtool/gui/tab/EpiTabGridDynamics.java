package org.epilogtool.gui.tab;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.TreePath;

import org.colomoto.logicalmodel.LogicalModel;
import org.epilogtool.core.Epithelium;
import org.epilogtool.core.cellDynamics.CellularEvent;
import org.epilogtool.core.cellDynamics.TopologyEventManager;
import org.epilogtool.core.cellDynamics.ModelPattern;
import org.epilogtool.gui.EpiGUI.EpiTabChanged;
import org.epilogtool.gui.EpiGUI.ProjectChangedInTab;
import org.epilogtool.gui.widgets.JComboWideBox;
import org.epilogtool.io.ButtonFactory;
import org.epilogtool.project.ProjectFeatures;

public class EpiTabGridDynamics extends EpiTabDefinitions {
	private static final long serialVersionUID = 4613661342531014915L;
	private final int JTF_WIDTH = 30;
	
	private TopologyEventManager epiTriggerManager;
	private String activeModel;
	private String triggerType;
	private JPanel jpTop;
	private JPanel jpRTop;
	private JPanel jpLTop;
	private JPanel jpRBottom;
	
	private final int SLIDER_MIN = 0;
	private final int SLIDER_MAX = 100;
	private final int SLIDER_STEP = 10;

	public EpiTabGridDynamics(Epithelium e, TreePath path,
			ProjectChangedInTab projChanged, EpiTabChanged tabChanged,
			ProjectFeatures projectFeatures) {
		super(e, path, projChanged, tabChanged, projectFeatures);
	}
	
	@Override
	public void initialize() {
		
		this.epiTriggerManager = this.epithelium.getTopologyEventManager().clone();
		
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
		
		this.callTriggerEventPanel();
		this.updateModelDynamicsPanel();
		
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
	
	private void callTriggerEventPanel() {
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
		
		LogicalModel m = this.epithelium.getProjectFeatures().getModel(this.activeModel);
				
		JButton jbAdd = ButtonFactory.getNoMargins("+");
		jbAdd.setToolTipText("Add a new pattern");
		jbAdd.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					addPattern();
				}
			});
		this.jpRBottom.add(jbAdd, BorderLayout.WEST);
		
		JPanel jpPatternPanel = new JPanel(new GridBagLayout());
		this.jpRBottom.add(jpPatternPanel);
		
		List<ModelPattern> patternList = this.epiTriggerManager
		.getModelManager(m)
		.getModelEventPatterns(CellularEvent.string2Event(this.triggerType));
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
		CellularEvent trigger = CellularEvent.string2Event(this.triggerType);
		this.epiTriggerManager.getModelManager(m).getModelEventPatterns(trigger).get(i).setPatternExpression(expression);
	}
	
	private void addPattern() {
		LogicalModel m = this.epithelium.getProjectFeatures().getModel(this.activeModel);
		CellularEvent trigger = CellularEvent.string2Event(this.triggerType);
		this.epiTriggerManager.getModelManager(m).getModelEventPatterns(trigger).add(new ModelPattern());
		this.updateModelDynamicsPanel();
	}
	
	private void removePattern(int i) {
		LogicalModel m = this.epithelium.getProjectFeatures().getModel(this.activeModel);
		CellularEvent trigger = CellularEvent.string2Event(this.triggerType);
		this.epiTriggerManager.getModelManager(m).getModelEventPatterns(trigger).remove(i);
		this.updateModelDynamicsPanel();
	}

	@Override
	protected void buttonReset() {
		this.epiTriggerManager = this.epithelium.getTopologyEventManager().clone();
		this.updateModelDynamicsPanel();
		this.getParent().repaint();
		
	}

	@Override
	protected void buttonAccept() {
		for (LogicalModel m : this.epiTriggerManager.getModelSet()) {
			List<ModelPattern> tmpList = new ArrayList<ModelPattern>();
			for (CellularEvent trigger : this.epiTriggerManager.getModelManager(m).getCellularEventSet()) {
				if (this.epiTriggerManager.getModelManager(m).getModelEventPatterns(trigger)
						.equals(this.epithelium.getTopologyEventManager().getModelManager(m).getModelEventPatterns(trigger))) {
					tmpList.addAll(this.epiTriggerManager.getModelManager(m).getModelEventPatterns(trigger));
					continue;
				}
				for (ModelPattern pattern : this.epiTriggerManager.getModelManager(m).getModelEventPatterns(trigger)) {
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
		this.epithelium.setTopologyEventManager(this.epiTriggerManager.clone());
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
		return !(this.epiTriggerManager.equals(this.epithelium.getTopologyEventManager()));
	}

	@Override
	public void applyChange() {
		List<LogicalModel> modelList = new ArrayList<LogicalModel>(
				this.epithelium.getEpitheliumGrid().getModelSet());
		for (LogicalModel m : modelList) {
			if (this.epithelium.getTopologyEventManager().hasModel(m)
					&& !this.epiTriggerManager.hasModel(m)) {
				this.epiTriggerManager.addModelManager(m, 
						this.epithelium
						.getTopologyEventManager()
						.getModelManager(m).clone());
			}
		}
		this.jpLTop.removeAll();
		this.jpRBottom.removeAll();
		this.jpLTop.add(this.newModelCombobox(modelList));
		this.activeModel = this.projectFeatures.getModelName(modelList.get(0));
		this.updateModelDynamicsPanel();
		
	}

}
