package org.epilogtool.gui.tab;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.tree.TreePath;

import org.colomoto.logicalmodel.LogicalModel;
import org.colomoto.logicalmodel.NodeInfo;
import org.epilogtool.common.ObjectComparator;
import org.epilogtool.core.Epithelium;
import org.epilogtool.core.cellDynamics.CellularEvent;
import org.epilogtool.core.cellDynamics.ModelHeritableNodes;
import org.epilogtool.core.cellDynamics.ModelEventExpression;
import org.epilogtool.core.cellDynamics.ModelEventManager;
import org.epilogtool.gui.EpiGUI.ProjChangeNotifyTab;
import org.epilogtool.gui.EpiGUI.TabChangeNotifyProj;
import org.epilogtool.gui.widgets.JComboWideBox;
import org.epilogtool.project.ComponentPair;
import org.epilogtool.project.ProjectFeatures;

public class EpiTabGridDynamics extends EpiTabDefinitions {
	private static final long serialVersionUID = 4613661342531014915L;
	private final int JTF_WIDTH = 30;
	
	private ModelEventManager eventManager;
	private ModelHeritableNodes modelHeritableNodes;
	private CellularEvent eventType;
	private TabProbablyChanged tpc;
	
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
		
		this.eventManager = this.epithelium.getModelEventManager().clone();
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
		this.jpCellDeath.setVisible(false);
		this.jpCellDeath.setBorder(BorderFactory.createTitledBorder("Cell death"));
		this.jpCenter.add(this.jpCellDeath, BorderLayout.CENTER);
		
		List<LogicalModel> modelList = new ArrayList<LogicalModel>(
				this.epithelium.getEpitheliumGrid().getModelSet());
		JComboBox<String> jcbSBML = this.newModelCombobox(modelList);
		this.jpModelSelection.add(jcbSBML);
		this.activeModel = (String) jcbSBML.getSelectedItem();
		this.updateModelDynamicsPanel();
		this.getParent().repaint();
		this.tpc = new TabProbablyChanged();
		this.isInitialized = true;
		
		
	}
	
	private JComboBox<String> newModelCombobox(List<LogicalModel> modelList) {
		String[] saSBML = new String[modelList.size()];
		for (int i = 0; i < modelList.size(); i++) {
			saSBML[i] = this.projectFeatures.getModelName(modelList.get(i));
		}

		JComboBox<String> jcb = new JComboWideBox<String>(saSBML);
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
		
		JPanel jpCellDivisionPattern = new JPanel(new GridBagLayout());
		this.jpCellDivision.add(jpCellDivisionPattern, BorderLayout.CENTER);
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.gridy = 0;
		gbc.ipady = 5;
		gbc.insets.bottom = 5;
		gbc.gridx = 0;
		gbc.anchor = GridBagConstraints.WEST;
		jpCellDivisionPattern.add(new JLabel("Trigger "), gbc);
		gbc.gridx = 1;
		String text = (!this.eventManager.getModelEvents(m).contains(CellularEvent.PROLIFERATION)) ? "" 
				: this.eventManager.getModelEventExpression(m, CellularEvent.PROLIFERATION).getExpression();
		JTextField jtf = new JTextField(text);
		jtf.setToolTipText("Cell division trigger");
			
		jtf.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				eventType = CellularEvent.PROLIFERATION;
				JTextField jtf = (JTextField) e.getSource();
				setExpression(jtf.getText());
				tpc.setChanged();
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		jtf.setColumns(this.JTF_WIDTH);
		jpCellDivisionPattern.add(jtf, gbc);
	}
	
	private void updateCellDeathPanel() {
		this.jpCellDeath.removeAll();
		this.jpCellDeath.setVisible(false);
		JPanel jpHiddenPanel = new JPanel(new BorderLayout());
		jpHiddenPanel.setPreferredSize(new Dimension(170, 200));
		jpCellDeath.add(jpHiddenPanel, BorderLayout.WEST);
		
		JPanel jpPatternPanel = new JPanel(new GridBagLayout());
		this.jpCellDeath.add(jpPatternPanel, BorderLayout.CENTER);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 0;
		gbc.ipady = 5;
		gbc.insets.bottom = 5;
		gbc.gridx = 0;
		gbc.anchor = GridBagConstraints.WEST;
		jpPatternPanel.add(new JLabel("Trigger"), gbc);
		gbc.gridx = 1;
		gbc.gridx = 1;
		//String text = (this.eventManager.getModelEvents(m).isEmpty()) ? 
				//"" : this.eventManager.getModelEvents(m)
				//.get(CellularEvent.APOPTOSIS).getExpression();
		JTextField jtf = new JTextField("");
		jtf.setToolTipText("Cell death trigger");
			
		jtf.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				eventType = CellularEvent.APOPTOSIS;
				JTextField jtf = (JTextField) e.getSource();
				setExpression(jtf.getText());
				tpc.setChanged();
			}
			@Override
			public void keyPressed(KeyEvent e) {
				}
		});
		jtf.setColumns(this.JTF_WIDTH);
		jpPatternPanel.add(jtf, gbc);
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
					tpc.setChanged();
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
	
	private void setExpression(String expression) {
		LogicalModel m = this.epithelium.getProjectFeatures().getModel(this.activeModel);
		if (expression == null || expression.trim().length() == 0) {
			this.eventManager.removeCellularEvent(m, this.eventType);
			return;
		}
		if (!this.eventManager.hasModel(m)) {
			this.eventManager.addModel(m);
		}
		this.eventManager.setModelEventExpression(m, this.eventType, new ModelEventExpression(expression));
	}
	
	private boolean validateExpression(JTextField jtf) {
		LogicalModel m = this.epithelium.getProjectFeatures().getModel(this.activeModel);
		if (jtf.getText() == null || jtf.getText().trim().length() == 0) {
			jtf.setBackground(Color.WHITE);
			return true;
		}
		return true;
	}

	@Override
	protected void buttonReset() {
		this.eventManager = this.epithelium.getModelEventManager().clone();
		this.modelHeritableNodes = this.epithelium.getModelHeritableNodes().clone();
		this.updateModelDynamicsPanel();
		
	}

	@Override
	protected void buttonAccept() {
		this.epithelium.setModelEventManager(this.eventManager.clone());
		this.epithelium.setModelHeritableNode(this.modelHeritableNodes.clone());
	}

	@Override
	protected boolean isChanged() {
		return (!(this.eventManager.equals(this.epithelium.getModelEventManager()))
				|| !(this.modelHeritableNodes.equals(this.epithelium.getModelHeritableNodes())));
	}

	@Override
	public void applyChange() {
		List<LogicalModel> modelList = new ArrayList<LogicalModel>(
				this.epithelium.getEpitheliumGrid().getModelSet());
		for (LogicalModel m : modelList) {
			for (CellularEvent event : this.eventManager.getModelEvents(m)) {
				this.epithelium.getModelEventManager()
				.setModelEventExpression(m, event, 
						this.eventManager.getModelEventExpression(m, event));
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
