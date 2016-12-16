package org.epilogtool.gui.tab;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
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
import org.epilogtool.core.cellDynamics.CellularEvent;
import org.epilogtool.core.cellDynamics.ModelHeritableNodes;
import org.epilogtool.core.cellDynamics.ModelPattern;
import org.epilogtool.core.cellDynamics.TopologyEventManager;
import org.epilogtool.gui.EpiGUI.ProjChangeNotifyTab;
import org.epilogtool.gui.EpiGUI.TabChangeNotifyProj;
import org.epilogtool.gui.widgets.JComboWideBox;
import org.epilogtool.io.ButtonFactory;
import org.epilogtool.project.ComponentPair;
import org.epilogtool.project.ProjectFeatures;

public class EpiTabGridDynamics extends EpiTabDefinitions {
	private static final long serialVersionUID = 4613661342531014915L;
	private final int JTF_WIDTH = 30;
	
	private TopologyEventManager eventManager;
	private ModelHeritableNodes modelHeritableNodes;
	private CellularEvent eventType;
	
	private String activeModel;
	private JPanel jpTopModelSelection;
	private JPanel jpModelSelection;
	private JPanel jpCenter;
	private JPanel jpCellDivision;
	private JPanel jpCellDeath;
	private Map<String, JCheckBox> mString2CheckBox;

	public EpiTabGridDynamics(Epithelium e, TreePath path,
			ProjChangeNotifyTab projChanged, TabChangeNotifyProj tabChanged,
			ProjectFeatures projectFeatures) {
		super(e, path, projChanged, tabChanged, projectFeatures);
	}
	
	@Override
	public void initialize() {
		
		this.eventManager = this.epithelium.getTopologyEventManager().clone();
		this.modelHeritableNodes = this.epithelium.getModelHeritableNodes().clone();
		
		this.center.setLayout(new BorderLayout());
		
		this.jpTopModelSelection = new JPanel(new BorderLayout());
		this.center.add(jpTopModelSelection, BorderLayout.NORTH);
		
		this.jpModelSelection = new JPanel();
		this.jpModelSelection.setBorder(BorderFactory
				.createTitledBorder("Model selection"));
		this.jpTopModelSelection.add(jpModelSelection, BorderLayout.WEST);
		
		this.jpCenter = new JPanel(new BorderLayout());
		this.center.add(jpCenter, BorderLayout.CENTER);
		
		this.jpCellDivision = new JPanel(new BorderLayout());
		this.jpCellDivision.setBorder(BorderFactory.createTitledBorder("Cell division"));
		this.jpCenter.add(this.jpCellDivision, BorderLayout.NORTH);
		
		this.jpCellDeath = new JPanel(new BorderLayout());
		this.jpCellDeath.setBorder(BorderFactory.createTitledBorder("Cell death"));
		this.jpCenter.add(this.jpCellDeath, BorderLayout.CENTER);
		
		List<LogicalModel> modelList = new ArrayList<LogicalModel>(
				this.epithelium.getEpitheliumGrid().getModelSet());
		JComboBox<String> jcbSBML = this.newModelCombobox(modelList);
		this.jpModelSelection.add(jcbSBML);
		this.activeModel = (String) jcbSBML.getSelectedItem();
		this.updateModelDynamicsPanel();
		this.getParent().repaint();
		
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
	
	private void updateCellDivisionPanel() {
		this.jpCellDivision.removeAll();
		LogicalModel m = this.epithelium.getProjectFeatures().getModel(this.activeModel);
		
		JPanel jpHeritableComponents = new JPanel(new GridBagLayout());  
		GridBagConstraints gbcMHN = new GridBagConstraints();
		
		List<String> modelNodesList = new ArrayList<String>(this.mString2CheckBox.keySet());
		Collections.sort(modelNodesList, ObjectComparator.STRING);
		int i;
		for (i = 0; i < modelNodesList.size(); i++) {
			String node = modelNodesList.get(i);
			gbcMHN.gridy = i;
			gbcMHN.gridx = 0;
			gbcMHN.insets.bottom = 5;
			gbcMHN.gridx = 0;
			gbcMHN.anchor = GridBagConstraints.WEST;
			jpHeritableComponents.add(this.mString2CheckBox.get(node), gbcMHN);
			if (this.modelHeritableNodes.isHeritableNode(m, node)) {
				this.mString2CheckBox.get(node).setSelected(true);
			} else {
				this.mString2CheckBox.get(node).setSelected(false);
			}
		}
		JScrollPane jspHeritableComponents = new JScrollPane(jpHeritableComponents);
		jspHeritableComponents.setBorder(BorderFactory.createTitledBorder("Inheritable Traits"));
		jspHeritableComponents.setPreferredSize(new Dimension(Math.max(170, jpHeritableComponents.getWidth()), Math.min(200, i*50)));
		jspHeritableComponents.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		jpCellDivision.add(jspHeritableComponents, BorderLayout.WEST);
		
		JButton jbAdd = ButtonFactory.getNoMargins("+");
		jbAdd.setToolTipText("Add a new Cell Division trigger");
		jbAdd.setPreferredSize(new Dimension(25, 25));
		jbAdd.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					eventType = CellularEvent.PROLIFERATION;
					addPattern();
				}
			});
		JPanel jpAdd = new JPanel(new BorderLayout());
		jpAdd.add(jbAdd, BorderLayout.EAST);
		this.jpCellDivision.add(jpAdd, BorderLayout.PAGE_START);
		
		JPanel jpCellDivisionPattern = new JPanel(new GridBagLayout());
		this.jpCellDivision.add(jpCellDivisionPattern, BorderLayout.CENTER);
		
		List<ModelPattern> patternList = this.eventManager
		.getModelManager(m)
		.getModelEventPatterns(CellularEvent.PROLIFERATION);
		GridBagConstraints gbc = new GridBagConstraints();
		for (int j = 0; j < patternList.size(); j ++) {
			gbc.gridy = j;
			gbc.ipady = 5;
			gbc.insets.bottom = 5;
			gbc.gridx = 0;
			gbc.anchor = GridBagConstraints.WEST;
			
			JButton jbRemove = ButtonFactory.getNoMargins("X");
			jbRemove.setToolTipText("Remove this Cell Division trigger");
			jbRemove.setPreferredSize(new Dimension(25, 19));
			jbRemove.setActionCommand("" + j);
			jbRemove.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					eventType = CellularEvent.PROLIFERATION;
					JButton jbRemove = (JButton) e.getSource();
					removePattern(Integer.parseInt(jbRemove.getActionCommand()));
				}
			});
			jpCellDivisionPattern.add(jbRemove, gbc);
			
			gbc.gridx = 1;
			JTextField jtf = new JTextField(patternList.get(j).getPatternExpression());
			jtf.setToolTipText("" + j);
			
			jtf.addKeyListener(new KeyListener() {
				@Override
				public void keyTyped(KeyEvent e) {
				}

				@Override
				public void keyReleased(KeyEvent e) {
					eventType = CellularEvent.PROLIFERATION;
					JTextField jtf = (JTextField) e.getSource();
					setPatternExpression(Integer.parseInt(jtf.getToolTipText()), 
							jtf.getText());
				}

				@Override
				public void keyPressed(KeyEvent e) {
				}
			});
			jtf.setColumns(this.JTF_WIDTH);
			jpCellDivisionPattern.add(jtf, gbc);
		}
	}
	
	private void updateCellDeathPanel() {
		this.jpCellDeath.removeAll();
		
		JPanel jpHiddenPanel = new JPanel(new BorderLayout());
		jpHiddenPanel.setPreferredSize(new Dimension(170, 200));
		jpCellDeath.add(jpHiddenPanel, BorderLayout.WEST);
		
		LogicalModel m = this.epithelium.getProjectFeatures().getModel(this.activeModel);
		JButton jbAdd = ButtonFactory.getNoMargins("+");
		jbAdd.setPreferredSize(new Dimension(25, 25));
		jbAdd.setToolTipText("Add a new Cell Death trigger");
		jbAdd.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					eventType = CellularEvent.APOPTOSIS;
					addPattern();
				}
			});
		JPanel jpAdd = new JPanel(new BorderLayout());
		jpAdd.add(jbAdd, BorderLayout.EAST);
		this.jpCellDeath.add(jpAdd, BorderLayout.PAGE_START);
		
		JPanel jpPatternPanel = new JPanel(new GridBagLayout());
		this.jpCellDeath.add(jpPatternPanel, BorderLayout.CENTER);
		
		List<ModelPattern> patternList = this.eventManager
		.getModelManager(m)
		.getModelEventPatterns(CellularEvent.APOPTOSIS);
		GridBagConstraints gbc = new GridBagConstraints();
		for (int i = 0; i < patternList.size(); i ++) {
			gbc.gridy = i;
			gbc.ipady = 5;
			gbc.insets.bottom = 5;
			gbc.gridx = 0;
			gbc.anchor = GridBagConstraints.WEST;
			
			JButton jbRemove = ButtonFactory.getNoMargins("X");
			jbRemove.setPreferredSize(new Dimension(25, 19));
			jbRemove.setToolTipText("Remove this Cell Death trigger");
			jbRemove.setActionCommand("" + i);
			jbRemove.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					eventType = CellularEvent.APOPTOSIS;
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
					eventType = CellularEvent.APOPTOSIS;
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
	}
	
	private void updateHeritableNodes() {
		this.mString2CheckBox = new HashMap<String, JCheckBox>();
		LogicalModel m = this.epithelium.getProjectFeatures().getModel(this.activeModel);
		for (NodeInfo node : m.getNodeOrder()) {
			if (this.epithelium.getIntegrationComponentPairs().contains(new ComponentPair(m, node))) continue;
			JCheckBox jcheckb = new JCheckBox(node.getNodeID(), false);
			jcheckb.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JCheckBox jcb = (JCheckBox) e.getSource();
					String nodeID = jcb.getText();
					if (jcb.isSelected()) {
						selectHeritableNode(nodeID);
					} else {
						removeHeritableNode(nodeID);
					}
				}
			});
			this.mString2CheckBox.put(node.getNodeID(), jcheckb);
		}
	}
	
	private void selectHeritableNode(String node) {
		LogicalModel m = this.epithelium.getProjectFeatures().getModel(this.activeModel);
		this.modelHeritableNodes.addNode(m, node);
	}
	
	private void removeHeritableNode(String node) {
		LogicalModel m = this.epithelium.getProjectFeatures().getModel(this.activeModel);
		this.modelHeritableNodes.removeNode(m, node);
	}
	
	private void updateModelDynamicsPanel() {
		this.updateHeritableNodes();
		this.updateCellDivisionPanel();
		this.updateCellDeathPanel();
		this.getParent().repaint();
	}
	
	private void setPatternExpression(int i, String expression) {
		LogicalModel m = this.epithelium.getProjectFeatures().getModel(this.activeModel);
		this.eventManager.getModelManager(m).getModelEventPatterns(this.eventType).get(i).setPatternExpression(expression);
	}
	
	private void addPattern() {
		LogicalModel m = this.epithelium.getProjectFeatures().getModel(this.activeModel);
		this.eventManager.getModelManager(m).getModelEventPatterns(this.eventType).add(new ModelPattern());
		this.updateModelDynamicsPanel();
	}
	
	private void removePattern(int i) {
		LogicalModel m = this.epithelium.getProjectFeatures().getModel(this.activeModel);
		this.eventManager.getModelManager(m).getModelEventPatterns(this.eventType).remove(i);
		this.updateModelDynamicsPanel();
	}

	@Override
	protected void buttonReset() {
		this.eventManager = this.epithelium.getTopologyEventManager().clone();
		this.modelHeritableNodes = this.epithelium.getModelHeritableNodes().clone();
		this.updateModelDynamicsPanel();
		
	}

	@Override
	protected void buttonAccept() {
		for (LogicalModel m : this.eventManager.getModelSet()) {
			List<ModelPattern> tmpList = new ArrayList<ModelPattern>();
			for (CellularEvent trigger : this.eventManager.getModelManager(m).getCellularEventSet()) {
				if (this.eventManager.getModelManager(m).getModelEventPatterns(trigger)
						.equals(this.epithelium.getTopologyEventManager().getModelManager(m).getModelEventPatterns(trigger))) {
					tmpList.addAll(this.eventManager.getModelManager(m).getModelEventPatterns(trigger));
					continue;
				}
				for (ModelPattern pattern : this.eventManager.getModelManager(m).getModelEventPatterns(trigger)) {
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
		this.epithelium.setTopologyEventManager(this.eventManager.clone());
		this.epithelium.setModelHeritableNode(this.modelHeritableNodes.clone());
	}
	
	private void callParsingError() {
		JPanel jpErrorLog = new JPanel(new BorderLayout());
		this.jpCenter.add(jpErrorLog, BorderLayout.SOUTH);
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
		this.jpCenter.add(jpOverlapLog, BorderLayout.SOUTH);
		JTextPane jtpOverlapLog = new JTextPane();
		jtpOverlapLog.setContentType("text/html");
		jtpOverlapLog.setText("<html><body style=\"background-color:#ffbebe\">"
				+ "You have defined concurrent triggers.<br/>"
				+ "It is not possible to save the specified settings."
				+ "</body></html>");
		jpOverlapLog.add(jtpOverlapLog, BorderLayout.CENTER);
		JButton jbOK = ButtonFactory.getNoMargins("OK");
		jbOK.setPreferredSize(new Dimension(40, 40));
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
		return (!(this.eventManager.equals(this.epithelium.getTopologyEventManager()))
				|| !(this.modelHeritableNodes.equals(this.epithelium.getModelHeritableNodes())));
	}

	@Override
	public void applyChange() {
		List<LogicalModel> modelList = new ArrayList<LogicalModel>(
				this.epithelium.getEpitheliumGrid().getModelSet());
		for (LogicalModel m : modelList) {
			if (this.epithelium.getTopologyEventManager().hasModel(m)
					&& !this.eventManager.hasModel(m)) {
				this.eventManager.addModelManager(m, 
						this.epithelium
						.getTopologyEventManager()
						.getModelManager(m).clone());
			}
			if (this.epithelium.getModelHeritableNodes().hasModel(m) && !this.modelHeritableNodes.hasModel(m)) {
				this.modelHeritableNodes.addModel(m);
			}
		}
		this.jpModelSelection.removeAll();
		this.jpModelSelection.add(this.newModelCombobox(modelList));
		this.activeModel = this.projectFeatures.getModelName(modelList.get(0));
		this.updateModelDynamicsPanel();
		this.getParent().repaint();
		
	}

}
