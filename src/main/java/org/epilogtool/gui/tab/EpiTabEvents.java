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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.tree.TreePath;

import org.antlr.runtime.RecognitionException;
import org.colomoto.biolqm.LogicalModel;
import org.colomoto.biolqm.NodeInfo;
import org.epilogtool.common.Txt;
import org.epilogtool.core.Epithelium;
import org.epilogtool.core.EpitheliumEvents;
import org.epilogtool.core.ModelCellularEvent;
import org.epilogtool.function.FSpecification;
import org.epilogtool.function.FunctionExpression;
import org.epilogtool.gui.EpiGUI.TabChangeNotifyProj;
import org.epilogtool.gui.color.ColorUtils;
import org.epilogtool.gui.widgets.JComboWideBox;
import org.epilogtool.gui.widgets.SliderPanel;
import org.epilogtool.mdd.MDDUtils;
import org.epilogtool.project.Project;

public class EpiTabEvents extends EpiTabDefinitions {

	private static final long serialVersionUID = 4263444740319547502L;

	private LogicalModel selModel;

	private JPanel jpTriggerDivisionParameters;
	private JPanel jpTriggerDeathParameters;

	private JPanel jpRight;

	private TabProbablyChanged tpc;

	private JPanel jpPatternDivision;
	private JPanel jpPatternDeath;
	private JPanel jpNewState;

	private JPanel auxDivisionPanel;
	private JPanel auxDeathPanel;
	private JPanel jpDivisionAction;

	private JPanel modelSelectionPanel;

	private JPanel jpComponentNewState;

	private EpitheliumEvents epiEventClone;

	private Map<LogicalModel, List<SliderPanel>> mModel2lsSPanel;

	private Map<String, JRadioButton> mName2JRBdivision;
	private Map<String, JRadioButton> mName2JRBdeath;

	private JTextField jtfPatternDeath;
	private JTextField jtfPatternDivision;

	private JComboBox<String> jcbDivisionAlgorithm;
	private JComboBox<String> jcbDeathAlgorithm;
	private JComboBox<Integer> jcbDivisionRange;

	private int mddDeath;
	private int mddDivision;

	public EpiTabEvents(Epithelium e, TreePath path, TabChangeNotifyProj tabChanged) {
		super(e, path, tabChanged);
	}

	/**
	 * Creates the EventsPanel, the first time the tab is created.
	 * 
	 */
	public void initialize() {

		this.center.setLayout(new BorderLayout());
		this.mModel2lsSPanel = new HashMap<LogicalModel, List<SliderPanel>>();
		this.mName2JRBdeath = new HashMap<String, JRadioButton>();
		this.mName2JRBdivision = new HashMap<String, JRadioButton>();

		this.jtfPatternDeath = new JTextField();
		this.jtfPatternDivision = new JTextField();

		this.jcbDivisionAlgorithm = new JComboBox<String>();

		this.jcbDivisionRange = new JComboBox<Integer>();

		this.jpDivisionAction = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(); 
		this.jpDivisionAction.setEnabled(false);
		this.jpNewState = new JPanel();

		List<LogicalModel> modelList = new ArrayList<LogicalModel>(this.epithelium.getEpitheliumGrid().getModelSet());

		for (LogicalModel model : modelList) {
			List<SliderPanel> sp = new ArrayList<SliderPanel>();
			SliderPanel spSliderDeath = new SliderPanel(Txt.get("s_TAB_EVE_DEATH_PROBABILITY"),
					Txt.get("s_TAB_EVE_DEATH"), this);
			sp.add(spSliderDeath);
			SliderPanel spSliderDivision = new SliderPanel(Txt.get("s_TAB_EVE_DIVISION_PROBABILITY"),
					Txt.get("s_TAB_EVE_DIVISION"), this);

			sp.add(spSliderDivision);
			mModel2lsSPanel.put(model, sp);
		}

		for (LogicalModel model : modelList) {
			List<SliderPanel> sp = mModel2lsSPanel.get(model);
			sp.get(1).getSlider()
			.setValue((int) (this.epithelium.getEpitheliumEvents().getMCE(model).getDivisionValue() * 100));
			sp.get(0).getSlider()
			.setValue((int) (this.epithelium.getEpitheliumEvents().getMCE(model).getDeathValue() * 100));
		}

		this.epiEventClone = this.epithelium.getEpitheliumEvents().clone();

		this.tpc = new TabProbablyChanged();

		this.jpPatternDeath = new JPanel();
		this.jpPatternDivision = new JPanel();

		this.jpTriggerDivisionParameters = new JPanel(new BorderLayout());
		this.jpTriggerDeathParameters = new JPanel(new BorderLayout());

		this.auxDivisionPanel = new JPanel(new BorderLayout());
		this.auxDeathPanel = new JPanel(new BorderLayout());
		this.mddDeath = 0;
		this.mddDivision = 0;

		this.jpRight = new JPanel(new BorderLayout());

		 //// *********************************
	    ////   MODEL SELECTION PANEL
	   ////  *********************************

		this.modelSelectionPanel = new JPanel();
		modelSelectionPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),Txt.get("s_MODEL_SELECT")));

		JComboBox<String> jcbSBML = this.newModelCombobox(modelList);
		modelSelectionPanel.add(jcbSBML);
		jcbSBML.setSelectedIndex(0);
		this.center.add(modelSelectionPanel, BorderLayout.NORTH);
		this.selModel = Project.getInstance().getProjectFeatures().getModel((String) jcbSBML.getSelectedItem());


		/// Right PANEL
		jpRight.setLayout(new BoxLayout(jpRight, BoxLayout.Y_AXIS));

		 //// *********************************
	    ////   DIVISION PANEL
	   ////  *********************************

		JPanel jpDivision = new JPanel(new BorderLayout());
		jpDivision.setPreferredSize(new Dimension(400,100));
		jpDivision.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),Txt.get("s_TAB_EVE_DIVISION")));
		
		JPanel jpTriggerDivisionOptions = new JPanel(new GridBagLayout());
		GridBagConstraints gbcDivisionTrigger = new GridBagConstraints();
		gbcDivisionTrigger.anchor = GridBagConstraints.WEST;
		jpTriggerDivisionOptions.setBorder(BorderFactory.createTitledBorder(Txt.get("s_TAB_EVE_TRIGGER")));
		
		this.jpTriggerDivisionParameters.setBorder(BorderFactory.createTitledBorder("Trigger definition"));
		
		JPanel jpAlgorithmDivisionOptions = new JPanel(new GridBagLayout());
		GridBagConstraints gbcAlgorithmDivisionOptions = new GridBagConstraints();
		gbcAlgorithmDivisionOptions.anchor = GridBagConstraints.WEST;
		jpAlgorithmDivisionOptions.setBorder(BorderFactory.createTitledBorder("Algorithm"));
		
		this.jpDivisionAction.setBorder(BorderFactory.createTitledBorder("Algorithm definition"));

		JPanel jpDivisionNorth = new JPanel(new BorderLayout());
		JPanel jpDivisionCenter = new JPanel(new BorderLayout());
		JPanel jpDivisionAux = new JPanel(new BorderLayout());
		jpDivisionAux.add(jpDivisionNorth, BorderLayout.NORTH);
		jpDivisionAux.add(jpDivisionCenter, BorderLayout.CENTER);
		
		this.jpComponentNewState = getComponentsPanel();
		this.jpComponentNewState.setBorder(BorderFactory.createTitledBorder(Txt.get("s_TAB_EVE_NEW_DIVISION_STATE")));
		JScrollPane jsLeftCenter = new JScrollPane(this.jpComponentNewState);
		jsLeftCenter.setBorder(BorderFactory.createEmptyBorder());
		jsLeftCenter.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		jpDivision.add(jpDivisionAux, BorderLayout.WEST);
		jpDivision.add(jsLeftCenter, BorderLayout.CENTER);
		
		jpDivisionNorth.add(jpTriggerDivisionOptions,BorderLayout.WEST);
		jpDivisionNorth.add(jpTriggerDivisionParameters,BorderLayout.CENTER);
		
		jpDivisionCenter.add(jpAlgorithmDivisionOptions,BorderLayout.WEST);
		jpDivisionCenter.add(this.jpDivisionAction,BorderLayout.CENTER);


		 //// ************************************************************
	    ////   DIVISION TRiGGER OPTIONS PANEL (jpTriggerDivisionOptions)
	   ////  *************************************************************
		
		Map<String, JRadioButton> triggerDivision2Radio = new HashMap<String, JRadioButton>();
		ButtonGroup groupDivision = new ButtonGroup();

//		JLabel jlTriggerDivision = new JLabel(Txt.get("s_TAB_EVE_TRIGGER") + ": ");
//		jpTriggerDivisionOptions.add(jlTriggerDivision);

		List<String> triggerOptions = new ArrayList<String>();
		triggerOptions.add(Txt.get("s_TAB_EVE_TRIGGER_NONE"));
		triggerOptions.add(Txt.get("s_TAB_EVE_TRIGGER_RANDOM"));
		triggerOptions.add(Txt.get("s_TAB_EVE_TRIGGER_PATTERN"));

		for (String triggerOption : triggerOptions) {
			JRadioButton jrb = new JRadioButton(triggerOption);
			this.mName2JRBdivision.put(triggerOption, jrb);
			jrb.setName(triggerOption);
			triggerDivision2Radio.put(triggerOption, jrb);
			jrb.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JRadioButton jrb = (JRadioButton) e.getSource();
					updateTriggerDivision(jrb.getName(), true);
				}
			});
			groupDivision.add(jrb);
			gbcDivisionTrigger.gridy ++;
			jpTriggerDivisionOptions.add(jrb,gbcDivisionTrigger);
		}
		this.updateTriggerDivision(this.epiEventClone.getMCE(this.selModel).getDivisionTrigger(), true);
	

		 //// ************************************************************
	    ////   DIVISION TRiGGER PARAMETERS PANEL (this.jpTriggerDivisionParameters)
	   ////  *************************************************************

		this.jpPatternDivision.add(new JLabel(Txt.get("s_TAB_EVE_TRIGGER_PATTERN")));
		this.jtfPatternDivision.setText(this.epiEventClone.getMCE(this.selModel).getDivisionPattern());
		this.jtfPatternDivision.setColumns(30);
		this.jtfPatternDivision.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				JTextField jtf = (JTextField) e.getSource();
				validateDivisionPattern(jtf);
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		this.validateDivisionPattern(this.jtfPatternDivision);
		this.jpPatternDivision.add(this.jtfPatternDivision);

		updateTriggerDivision(this.epiEventClone.getMCE(this.selModel).getDivisionTrigger(), true);

		// TRIGGERS SOUTH PANEL: RANDOM

		SliderPanel spDivision = this.mModel2lsSPanel.get(selModel).get(1);
		updateAlpha(spDivision.getValue(), spDivision.getLabel(), spDivision.getMin(), spDivision.getMax());
		

		 //// ************************************************************
	    ////   DIVISION ALGORITHM OPTIONS PANEL (jpAlgorithmDivisionOptions)
	   ////  *************************************************************

		//DIVISION RANGE

		JLabel jlDivisionRange = new JLabel("Division range: ");
		gbcAlgorithmDivisionOptions.gridy = 0;
		gbcAlgorithmDivisionOptions.gridx = 0;
		jpAlgorithmDivisionOptions.add(jlDivisionRange,gbcAlgorithmDivisionOptions);
		gbcAlgorithmDivisionOptions.gridy++;
		jpAlgorithmDivisionOptions.add(this.jcbDivisionRange,gbcAlgorithmDivisionOptions);
		gbcAlgorithmDivisionOptions.gridy++;

		for (int d = 1; d <= Math.max(this.epithelium.getEpitheliumGrid().getX(),
				this.epithelium.getEpitheliumGrid().getY()); d++) {
			jcbDivisionRange.addItem(d);
		}
		this.jcbDivisionRange.setSelectedItem(this.epiEventClone.getMCE(this.selModel).getDivisionRange());
		this.jcbDivisionRange.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unchecked")
				JComboBox jcb = (JComboBox) e.getSource();
				updateDivisionRange(jcb.getSelectedItem());
			}
		});

		this.jcbDivisionRange.repaint();
		this.jcbDivisionRange.revalidate();

		// DIVISION ALGORITHM

		JLabel jlDivisionAction = new JLabel("Division algoritm: ");
		jpAlgorithmDivisionOptions.add(jlDivisionAction, gbcAlgorithmDivisionOptions);
		gbcAlgorithmDivisionOptions.gridy++;
		jpAlgorithmDivisionOptions.add(this.jcbDivisionAlgorithm, gbcAlgorithmDivisionOptions);
		gbcAlgorithmDivisionOptions.gridy++;

		this.jcbDivisionAlgorithm.addItem(Txt.get("s_TAB_EVE_ALGORITHM_RANDOM"));
		this.jcbDivisionAlgorithm.addItem(Txt.get("s_TAB_EVE_ALGORITHM_MINIMUM_DISTANCE"));
		this.jcbDivisionAlgorithm.addItem(Txt.get("s_TAB_EVE_ALGORITHM_COMPRESSION"));
		this.jcbDivisionAlgorithm.addItem(Txt.get("s_TAB_EVE_ALGORITHM_N-SHORTEST_DISTANCE"));
		this.jcbDivisionAlgorithm.setSelectedItem(this.epiEventClone.getMCE(this.selModel).getDivisionAlgorithm());

		this.jcbDivisionAlgorithm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unchecked")
				JComboBox jcb = (JComboBox) e.getSource();
				updateDivisionAction(jcb.getSelectedItem());
			}
		});

		 //// ************************************************************
	    ////   DIVISION ALGORITHM PARAMETERS PANEL (jpAlgorithmDivisionParameters)
	   ////  *************************************************************

		//Neighbours influence RANGE

		JPanel jpNeiRange = new JPanel();
		JLabel jlNeighboursRange = new JLabel("Neighbours range: ");
		jpNeiRange.add(jlNeighboursRange);

		JComboBox<Integer> jcbNeighboursRange = new JComboBox<Integer>();

		for (int d = 1; d <= Math.max(this.epithelium.getEpitheliumGrid().getX(),
				this.epithelium.getEpitheliumGrid().getY()); d++) {
			jcbNeighboursRange.addItem(d);
		}
		jcbNeighboursRange.setSelectedItem(this.epiEventClone.getMCE(this.selModel).getNeighboursRange());

		jcbNeighboursRange.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unchecked")
				JComboBox jcb = (JComboBox) e.getSource();
				updateNeighboursRange((int) jcb.getSelectedItem());
			}
		});

		jpNeiRange.add(jcbNeighboursRange);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		this.jpDivisionAction.add(jpNeiRange,gbc);

		jcbNeighboursRange.repaint();
		jcbNeighboursRange.revalidate();

		//Compression parameter
		
		JPanel jpCompressionParamenter = new JPanel();
		JLabel jlCompressionParameter = new JLabel("Compression parameter");
		jpCompressionParamenter.add(jlCompressionParameter);
		
		JComboBox<Float> jcbCompressionParameter = new JComboBox<Float>();
		for (int d = 0; d <= 10; d++) {
			jcbCompressionParameter.addItem((float) (d/10.0));
		}
		jcbCompressionParameter.setSelectedItem(this.epiEventClone.getMCE(this.selModel).getCompressionParameter());

		jcbCompressionParameter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unchecked")
				JComboBox jcb = (JComboBox) e.getSource();
				updateCompressionParameter((float) jcb.getSelectedItem());
			}
		});
		jpCompressionParamenter.add(jcbCompressionParameter);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		this.jpDivisionAction.add(jpCompressionParamenter,gbc);
		
		//Minimum Distance
		
		JPanel jpMinimiumDistance = new JPanel();
		JLabel jlMinimumDistance = new JLabel("Minimum distance");
		jpMinimiumDistance.add(jlMinimumDistance);
		
		JComboBox<Integer> jcbMinimumDistance = new JComboBox<Integer>();
		
		for (int d = 1; d <= Math.max(this.epithelium.getEpitheliumGrid().getX(),
				this.epithelium.getEpitheliumGrid().getY()); d++) {
			jcbMinimumDistance.addItem(d);
		}
		jcbMinimumDistance.setSelectedItem(this.epiEventClone.getMCE(this.selModel).getCompressionParameter());

		jcbMinimumDistance.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unchecked")
				JComboBox jcb = (JComboBox) e.getSource();
				updateMinimumDistance((float) jcb.getSelectedItem());
			}
		});
		jpMinimiumDistance.add(jcbMinimumDistance);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		this.jpDivisionAction.add(jpMinimiumDistance,gbc);
	

		////
		updateDivisionAction(this.epiEventClone.getMCE(this.selModel).getDivisionAlgorithm());


		jpDivision.repaint();
		jpDivision.revalidate();

		
		 //// *********************************
	    ////   DEATH PANEL
	   ////  *********************************

		JPanel jpDeath = new JPanel(new BorderLayout());
		jpDeath.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),Txt.get("s_TAB_EVE_DEATH")));
		
		JPanel jpTriggerDeathOptions = new JPanel(new GridBagLayout());
		GridBagConstraints gbcDeathTrigger = new GridBagConstraints();
		gbcDeathTrigger.anchor = GridBagConstraints.WEST;
		jpTriggerDeathOptions.setBorder(BorderFactory.createTitledBorder(Txt.get("s_TAB_EVE_TRIGGER")));
		
		this.jpTriggerDeathParameters.setBorder(BorderFactory.createTitledBorder("Trigger definition"));
		jpDeath.setPreferredSize(new Dimension(100,40));
		jpDeath.add(jpTriggerDeathOptions, BorderLayout.WEST);
		jpDeath.add(this.jpTriggerDeathParameters, BorderLayout.CENTER);
		
		 //// ************************************************************
	    ////   DEATH TRiGGER OPTIONS PANEL (jpTriggerDeathOptions)
	   ////  *************************************************************
		
		Map<String, JRadioButton> triggerDeath2Radio = new HashMap<String, JRadioButton>();
		ButtonGroup groupDeath = new ButtonGroup();

		for (String triggerOption : triggerOptions) {
			JRadioButton jrb = new JRadioButton(triggerOption);
			this.mName2JRBdeath.put(triggerOption, jrb);
			jrb.setName(triggerOption);
			triggerDeath2Radio.put(triggerOption, jrb);
			jrb.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JRadioButton jrb = (JRadioButton) e.getSource();
					updateTriggerDeath(jrb.getName(), true);
				}
			});
			groupDeath.add(jrb);
			gbcDeathTrigger.gridy ++;
			jpTriggerDeathOptions.add(jrb,gbcDeathTrigger);
		}
		this.updateTriggerDeath(this.epiEventClone.getMCE(this.selModel).getDeathTrigger(), true);
	
		
		 //// ************************************************************
	    ////   DIVISION TRiGGER PARAMETERS PANEL (this.jpTriggerDivisionParameters)
	   ////  *************************************************************

		this.jpPatternDeath.add(new JLabel(Txt.get("s_TAB_EVE_TRIGGER_PATTERN")));
		this.jtfPatternDeath.setText(this.epiEventClone.getMCE(this.selModel).getDeathPattern());
		this.jtfPatternDeath.setColumns(30);
		this.jtfPatternDeath.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				JTextField jtf = (JTextField) e.getSource();
				validateDeathPattern(jtf);
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		this.validateDeathPattern(this.jtfPatternDeath);
		this.jpPatternDeath.add(this.jtfPatternDeath);

		updateTriggerDeath(this.epiEventClone.getMCE(this.selModel).getDeathTrigger(), true);

		// TRIGGERS SOUTH PANEL: RANDOM
		SliderPanel spDeath = this.mModel2lsSPanel.get(selModel).get(0);
		updateAlpha(spDeath.getValue(), spDeath.getLabel(), spDeath.getMin(), spDeath.getMax());

		jpDeath.repaint();
		jpDeath.revalidate();

		// ************ Group all panels
		this.jpRight.add(jpDeath, BorderLayout.NORTH);
		this.jpRight.add(jpDivision, BorderLayout.CENTER);
		this.center.add(this.jpRight, BorderLayout.CENTER);
		this.isInitialized = true;
	}


	protected void updateMinimumDistance(float selectedItem) {
		// TODO Auto-generated method stub
		
	}

	private void updateCompressionParameter(float selectedItem) {
		if (this.isInitialized)
			this.tpc.setChanged();
		this.epiEventClone.getMCE(this.selModel).setCompressionParameter(selectedItem);
	}

	private void updateNeighboursRange(int selectedItem) {
		if (this.isInitialized)
			this.tpc.setChanged();
		this.epiEventClone.getMCE(this.selModel).setNeighboursRange(selectedItem);
	}

	private JPanel getComponentsPanel() {

		JPanel jpComponents = new JPanel(new GridBagLayout());
		GridBagConstraints gbcComponents = new GridBagConstraints();
		gbcComponents.anchor = GridBagConstraints.WEST;
		
		int index = 0; 
		for (NodeInfo node: this.selModel.getComponents()) {
			if (!node.isInput()) {
				gbcComponents.gridy = index;
				getCompMiniPanel(jpComponents,gbcComponents,index,node);
				index++;
			}
		}
		return jpComponents;
	}

	private void getCompMiniPanel(JPanel jp, GridBagConstraints gbc, int y, NodeInfo node) {
		
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridx = 0;
		

		//// Add Component Name

		JLabel name = new JLabel(node.getNodeID());
		jp.add(name,gbc);
		
		gbc.gridx = 1;

		//// Add jcombobox
		JComboBox<Byte> jcb = new JComboBox<Byte>();
		jcb.setName(node.getNodeID());
		jcb.setToolTipText(node.getNodeID());

		for (byte i=0; i<=node.getMax();i++) {
			jcb.addItem(i);
		}
		//		jcb.addItem("F");
		//		jcb.addItem("*");

		jcb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unchecked")
				JComboBox<String> jcb = (JComboBox<String>) e.getSource();
				updateNewState(node, (Byte) jcb.getSelectedItem());
			}
		});


		byte[] state = this.epiEventClone.getMCE(this.selModel).getNewCellState();
		//		System.out.println(Arrays.toString(state));
		for (int i = 0; i<this.selModel.getComponents().size();i++) {
			if (this.selModel.getComponents().get(i).equals(node)) {
				jcb.setSelectedItem(state[i]);
				break;
			}
		}
		//		System.out.println(Arrays.toString( this.epiEventClone.getMCE(this.selModel).getNewCellState()));
		jp.add(jcb,gbc);
	}

	protected void updateNewState(NodeInfo node, Byte selectedItem) {

		//		System.out.println(Arrays.toString(this.epithelium.getEpitheliumEvents().getMCE(this.selModel).getNewCellState()));
		//		System.out.println(Arrays.toString(this.epiEventClone.getMCE(this.selModel).getNewCellState()));
		//		
		//		System.out.println("here");
		if (this.isInitialized)
			this.tpc.setChanged();
		byte[] state = this.epiEventClone.getMCE(this.selModel).getNewCellState();
		for (int i = 0; i<this.selModel.getComponents().size();i++) {
			if (this.selModel.getComponents().get(i).equals(node)) {
				//				System.out.println(selectedItem);
				state[i]=selectedItem;
				break;
			}
		}
		this.epiEventClone.getMCE(this.selModel).setNewCellState(state);

		//		System.out.println(Arrays.toString(this.epithelium.getEpitheliumEvents().getMCE(this.selModel).getNewCellState()));
		//		System.out.println(Arrays.toString(this.epiEventClone.getMCE(this.selModel).getNewCellState()));
	}

	protected void updateDivisionRange(Object selectedItem) {
		if (this.isInitialized)
			this.tpc.setChanged();
		this.epiEventClone.getMCE(this.selModel).setDivisionRange((int) selectedItem);
	}

	protected void updateDeathAction(Object object) {
		if (this.isInitialized)
			this.tpc.setChanged();
		this.epiEventClone.getMCE(this.selModel).setDeathAlgorithm((String) object);
	}

	protected void updateDivisionAction(Object object) {
		if (this.isInitialized)
			tpc.setChanged();

		this.epiEventClone.getMCE(this.selModel).setDivisionAlgorithm((String) object);

	
		if (((String) object).equals((Txt.get("s_TAB_EVE_ALGORITHM_COMPRESSION")))) {
			this.jpDivisionAction.getComponent(2).setEnabled(false);
		this.jpDivisionAction.getComponent(0).setEnabled(true);
		this.jpDivisionAction.getComponent(1).setEnabled(true);
		}
		if (((String) object).equals((Txt.get("s_TAB_EVE_ALGORITHM_N-SHORTEST_DISTANCE"))))
		{
			this.jpDivisionAction.getComponent(2).setEnabled(true);
			this.jpDivisionAction.getComponent(0).setEnabled(false);
			this.jpDivisionAction.getComponent(1).setEnabled(false);
		}
		else {
			for (int i= 0; i<this.jpDivisionAction.getComponentCount(); i++)
			this.jpDivisionAction.getComponent(i).setEnabled(false);
		}

	}

	protected void validateDeathPattern(JTextField jtf) {
		if (this.isInitialized)
			tpc.setChanged();

		try {
			FunctionExpression fExpression = FSpecification.parse(jtf.getText());
			this.mddDeath = MDDUtils.getInstance().getModelMDDs(this.selModel).getMDD(fExpression, "");
			//			System.out.println("Events.Death: div["+this.mddDivision + "] death[" + this.mddDeath+"]");

			if (MDDUtils.getInstance().getModelMDDs(this.selModel).and(this.mddDeath, this.mddDivision) == 1) {
				jtf.setBackground(Color.YELLOW);
			} else {
				this.epiEventClone.getMCE(this.selModel).setDeathPattern(jtf.getText());
				jtf.setBackground(Color.WHITE);
			}
		} catch (RecognitionException re) {
			jtf.setBackground(ColorUtils.LIGHT_RED);
		} catch (RuntimeException re) {
			jtf.setBackground(ColorUtils.LIGHT_RED);
		}
	}

	protected void validateDivisionPattern(JTextField jtf) {
		if (this.isInitialized)
			tpc.setChanged();

		try {
			FunctionExpression fExpression = FSpecification.parse(jtf.getText());
			this.mddDivision = MDDUtils.getInstance().getModelMDDs(this.selModel).getMDD(fExpression, "");
			//			System.out.println("Events.Div: div["+this.mddDivision + "] death[" + this.mddDeath+"]");
			if (MDDUtils.getInstance().getModelMDDs(this.selModel).and(this.mddDivision, this.mddDeath) == 1) {
				jtf.setBackground(Color.YELLOW);
			} else {
				this.epiEventClone.getMCE(this.selModel).setDivisionPattern(jtf.getText());
				jtf.setBackground(Color.WHITE);
			}
		} catch (RecognitionException re) {
			jtf.setBackground(ColorUtils.LIGHT_RED);
		} catch (RuntimeException re) {
			jtf.setBackground(ColorUtils.LIGHT_RED);
		}
	}

	protected void updateTriggerDivision(String string, boolean control) {

		if (this.isInitialized && control)
			tpc.setChanged();
		
		this.auxDivisionPanel.removeAll();
		String str = string;

		if (str.equals(Txt.get("s_TAB_EVE_TRIGGER_RANDOM"))) {
			this.mName2JRBdivision.get(Txt.get("s_TAB_EVE_TRIGGER_RANDOM")).setSelected(true);
			this.auxDivisionPanel.add(this.mModel2lsSPanel.get(this.selModel).get(1), BorderLayout.SOUTH);
			this.epiEventClone.setDivisionTrigger(this.selModel, Txt.get("s_TAB_EVE_TRIGGER_RANDOM"));
		}

		else if (str.equals(Txt.get("s_TAB_EVE_TRIGGER_PATTERN"))) {
			this.mName2JRBdivision.get(Txt.get("s_TAB_EVE_TRIGGER_PATTERN")).setSelected(true);
			this.auxDivisionPanel.add(this.jpPatternDivision, BorderLayout.CENTER);
			this.epiEventClone.setDivisionTrigger(this.selModel, Txt.get("s_TAB_EVE_TRIGGER_PATTERN"));
			if (this.epiEventClone.getDivisionOption().equals(Txt.get("s_TAB_EVE_DIVISON_NEWCELLSTATE_PREDEFINED"))) {
				this.auxDivisionPanel.add(this.jpNewState, BorderLayout.SOUTH);
			}
		} else {
			this.mName2JRBdivision.get(Txt.get("s_TAB_EVE_TRIGGER_NONE")).setSelected(true);
			this.epiEventClone.setDivisionTrigger(this.selModel, Txt.get("s_TAB_EVE_TRIGGER_NONE"));
			this.jpDivisionAction.setEnabled(false);
		}

		this.jpTriggerDivisionParameters.add(this.auxDivisionPanel, BorderLayout.SOUTH);
		this.repaint();
		this.jpTriggerDivisionParameters.repaint();
		this.revalidate();
		this.jpTriggerDivisionParameters.revalidate();
	}

	protected void updateTriggerDeath(String str, boolean control) {

		if (this.isInitialized && control)
			tpc.setChanged();
		this.auxDeathPanel.removeAll();

		if (str.equals(Txt.get("s_TAB_EVE_TRIGGER_RANDOM"))) {
			this.mName2JRBdeath.get(Txt.get("s_TAB_EVE_TRIGGER_RANDOM")).setSelected(true);
			this.auxDeathPanel.add(this.mModel2lsSPanel.get(this.selModel).get(0), BorderLayout.SOUTH);
			this.epiEventClone.setDeathTrigger(this.selModel, Txt.get("s_TAB_EVE_TRIGGER_RANDOM"));

		}

		else if (str.equals(Txt.get("s_TAB_EVE_TRIGGER_PATTERN"))) {
			this.mName2JRBdeath.get(Txt.get("s_TAB_EVE_TRIGGER_PATTERN")).setSelected(true);
			this.auxDeathPanel.add(this.jpPatternDeath, BorderLayout.CENTER);
			this.epiEventClone.setDeathTrigger(this.selModel, Txt.get("s_TAB_EVE_TRIGGER_PATTERN"));
			;
		} else {
			this.mName2JRBdeath.get(Txt.get("s_TAB_EVE_TRIGGER_NONE")).setSelected(true);
			this.epiEventClone.setDeathTrigger(this.selModel, Txt.get("s_TAB_EVE_TRIGGER_NONE"));
		}

		this.jpTriggerDeathParameters.add(this.auxDeathPanel, BorderLayout.SOUTH);

		this.repaint();
		this.jpTriggerDeathParameters.repaint();
		this.revalidate();
		this.jpTriggerDeathParameters.revalidate();
	}

	// ---------------------------------------------------------------------------
	// End initialize

	public void updateSliderValues(JSlider slider) {
		if (this.isInitialized)
			tpc.setChanged();

		SliderPanel spDeath = this.mModel2lsSPanel.get(this.selModel).get(0);
		SliderPanel spDivision = this.mModel2lsSPanel.get(this.selModel).get(1);

		if (spDeath.getValue() + spDivision.getValue() > 100) {
			if (slider.getName().equals(Txt.get("s_TAB_EVE_DIVISION"))) {
				spDeath.setValue((int) (100 - spDivision.getValue()));
			} else if (slider.getName().equals(Txt.get("s_TAB_EVE_DEATH"))) {
				spDivision.setValue((int) (100 - spDeath.getValue()));
			}
			// }
		}

		float valueDivision = (float) spDivision.getValue() / 100;
		float valueDeath = (float) spDeath.getValue() / 100;

		spDeath.setText("" + valueDeath);
		spDivision.setText("" + valueDivision);

		this.epiEventClone.getMCE(this.selModel).setDeathValue(valueDeath);
		this.epiEventClone.getMCE(this.selModel).setDivisionValue(valueDivision);
		updateAlpha(spDeath.getValue(), spDeath.getLabel(), spDeath.getMin(), spDeath.getMax());
		updateAlpha(spDivision.getValue(), spDivision.getLabel(), spDivision.getMin(), spDivision.getMax());

	}

	private void updateAlpha(float sliderValue, JLabel jAlphaLabelValue, int SLIDER_MIN, int SLIDER_MAX) {
		float value = (float) sliderValue / SLIDER_MAX;
		String sTmp = "" + value;
		jAlphaLabelValue.setText(sTmp);
	}

	@Override
	protected void buttonReset() {

		List<LogicalModel> modelList = new ArrayList<LogicalModel>(this.epithelium.getEpitheliumGrid().getModelSet());
		for (LogicalModel model : modelList) {
			ModelCellularEvent mce = this.epithelium.getEpitheliumEvents().getMCE(model);

			this.epiEventClone.setDeathTrigger(model, mce.getDeathTrigger());
			this.epiEventClone.setDivisionTrigger(model, mce.getDivisionTrigger());
			this.epiEventClone.setDeathValue(model, (int) mce.getDeathValue());
			this.epiEventClone.setDivisionValue(model, (int) mce.getDivisionValue());
			this.epiEventClone.setDeathPattern(model, mce.getDeathPattern());
			this.epiEventClone.setDivisionPattern(model, mce.getDivisionPattern());
			this.epiEventClone.getMCE(model).setDeathAlgorithm(mce.getDeathAlgorithm());
			this.epiEventClone.getMCE(model).setDivisionAlgorithm(mce.getDivisionAlgorithm());
			this.epiEventClone.getMCE(model).setDivisionRange(mce.getDivisionRange());
			this.epiEventClone.setDivisionNewState(model, mce.getNewCellState());
			this.epiEventClone.getMCE(model).setNeighboursRange(mce.getNeighboursRange());
			this.epiEventClone.getMCE(model).setCompressionParameter(mce.getCompressionParameter());

		}

		updateJRBDivisionTrigger();
		updateJRBDeathTrigger();
		updateJCBDivisionAlgorithm();
		updateJCBDeathAlgorithm();
		updateJCBDivisionRange();

		// Make sure that the slider presents the old values
		List<SliderPanel> sp = mModel2lsSPanel.get(this.selModel);
		sp.get(1).getSlider()
		.setValue((int) (this.epithelium.getEpitheliumEvents().getMCE(this.selModel).getDivisionValue() * 100));
		sp.get(0).getSlider()
		.setValue((int) (this.epithelium.getEpitheliumEvents().getMCE(this.selModel).getDeathValue() * 100));

		// Make sure that the Pattern is reset
		this.jtfPatternDivision.setText(this.epiEventClone.getMCE(this.selModel).getDivisionPattern());
		this.jtfPatternDeath.setText(this.epiEventClone.getMCE(this.selModel).getDeathPattern());

		// Repaint
		this.getParent().repaint();
	}

	private void updateJCBDivisionRange() {
		int divRange = this.epithelium.getEpitheliumEvents().getMCE(this.selModel).getDivisionRange();

		for (int i = 0; i < this.jcbDivisionRange.getItemCount(); i++) {
			if (divRange == this.jcbDivisionRange.getItemAt(i))
				this.jcbDivisionAlgorithm.setSelectedIndex(i);
		}

	}

	private void updateJCBDivisionAlgorithm() {

		String divAlg = this.epithelium.getEpitheliumEvents().getMCE(this.selModel).getDivisionAlgorithm();

		for (int i = 0; i < this.jcbDivisionAlgorithm.getItemCount(); i++) {
			if (divAlg != null && divAlg.equals(this.jcbDivisionAlgorithm.getItemAt(i)))
				this.jcbDivisionAlgorithm.setSelectedIndex(i);
		}
	}

	private void updateJCBDeathAlgorithm() {

		String deathAlg = this.epithelium.getEpitheliumEvents().getMCE(this.selModel).getDeathAlgorithm();

		for (int i = 0; i < this.jcbDeathAlgorithm.getItemCount(); i++) {
			if (deathAlg != null && deathAlg.equals(this.jcbDeathAlgorithm.getItemAt(i)))
				this.jcbDeathAlgorithm.setSelectedIndex(i);
		}
	}

	private void updateJRBDeathTrigger() {
		String deathTrig = this.epithelium.getEpitheliumEvents().getMCE(this.selModel).getDeathTrigger();
		for (String s : this.mName2JRBdeath.keySet()) {
			if (s.equals(deathTrig)) {
				this.mName2JRBdeath.get(s).setSelected(true);
				updateTriggerDeath(deathTrig, false);
			}
		}
	}

	private void updateJRBDivisionTrigger() {
		String divisionTrig = this.epithelium.getEpitheliumEvents().getMCE(this.selModel).getDivisionTrigger();
		for (String s : this.mName2JRBdivision.keySet()) {
			if (s.equals(divisionTrig)) {
				this.mName2JRBdivision.get(s).setSelected(true);
				updateTriggerDivision(divisionTrig, false);
			}
		}
	}

	//Accepts the change on the project
	@Override
	protected void buttonAccept() {

		List<LogicalModel> modelList = new ArrayList<LogicalModel>(this.epithelium.getEpitheliumGrid().getModelSet());
		for (LogicalModel model : modelList) {
			ModelCellularEvent mce = this.epiEventClone.getMCE(model);

			//			System.out.println("buttonAccept");

			// System.out.println("1 " + mce.getDeathTrigger());
			// System.out.println("1 " + mce.getDivisionTrigger());
			// System.out.println("1 " + mce.getDeathValue());
			// System.out.println("1 " + mce.getDivisionValue());
			//
			//
			// System.out.println("2 " +
			// this.epithelium.getEpitheliumEvents().getMCE(model).getDeathTrigger());
			// System.out.println("2 " +
			// this.epithelium.getEpitheliumEvents().getMCE(model).getDivisionTrigger());
			// System.out.println("2 " +
			// this.epithelium.getEpitheliumEvents().getMCE(model).getDeathValue());
			// System.out.println("2 " +
			// this.epithelium.getEpitheliumEvents().getMCE(model).getDivisionValue());

			this.epithelium.getEpitheliumEvents().setDeathTrigger(model, mce.getDeathTrigger());
			this.epithelium.getEpitheliumEvents().setDivisionTrigger(model, mce.getDivisionTrigger());
			this.epithelium.getEpitheliumEvents().setDeathValue(model, (float) mce.getDeathValue());
			this.epithelium.getEpitheliumEvents().setDivisionValue(model, (float) mce.getDivisionValue());
			this.epithelium.getEpitheliumEvents().setDeathPattern(model, mce.getDeathPattern());
			this.epithelium.getEpitheliumEvents().setDivisionPattern(model, mce.getDivisionPattern());
			this.epithelium.getEpitheliumEvents().getMCE(model).setDeathAlgorithm(mce.getDeathAlgorithm());
			this.epithelium.getEpitheliumEvents().getMCE(model).setDivisionAlgorithm(mce.getDivisionAlgorithm());
			this.epithelium.getEpitheliumEvents().getMCE(model).setDivisionRange(mce.getDivisionRange());

			// System.out.println("1" + mce.getDeathAlgorithm());
			// System.out.println("2" +
			// this.epithelium.getEpitheliumEvents().getMCE(model).getDeathAlgorithm());
			// TODO
			this.epithelium.getEpitheliumEvents().setDivisionNewState(model, mce.getNewCellState());
			this.epithelium.getEpitheliumEvents().getMCE(model).setNeighboursRange(mce.getNeighboursRange());
			this.epithelium.getEpitheliumEvents().getMCE(model).setCompressionParameter(mce.getCompressionParameter());

			// System.out.println("3 " +
			// this.epithelium.getEpitheliumEvents().getMCE(model).getDeathTrigger());
			// System.out.println("3 " +
			// this.epithelium.getEpitheliumEvents().getMCE(model).getDivisionTrigger());
			// System.out.println("3 " +
			// this.epithelium.getEpitheliumEvents().getMCE(model).getDeathValue());
			// System.out.println("3 " +
			// this.epithelium.getEpitheliumEvents().getMCE(model).getDivisionValue());
		}

	}

	@Override
	protected boolean isChanged() {

		//		System.out.println(Arrays.toString(this.epithelium.getEpitheliumEvents().getMCE(this.selModel).getNewCellState()));
		//		System.out.println(Arrays.toString(this.epiEventClone.getMCE(this.selModel).getNewCellState()));

		//		System.out.println("Ischanged");

		if (this.epithelium.getEpitheliumEvents()
				.getDivisionValue(this.selModel) != (this.epiEventClone.getMCE(this.selModel).getDivisionValue())) {
			return true;
		}

		if (this.epithelium.getEpitheliumEvents()
				.getDeathValue(this.selModel) != (this.epiEventClone.getMCE(this.selModel).getDeathValue())) {
			return true;
		}

		if (!this.epithelium.getEpitheliumEvents().getMCE(this.selModel).getDeathPattern()
				.equals(this.epiEventClone.getMCE(this.selModel).getDeathPattern())) {
			return true;
		}

		if (!this.epithelium.getEpitheliumEvents().getDivisionPattern(this.selModel)
				.equals(this.epiEventClone.getDivisionPattern(this.selModel))) {
			return true;
		}

		if (!this.epithelium.getEpitheliumEvents().getDeathTrigger(this.selModel)
				.equals(this.epiEventClone.getDeathTrigger(this.selModel))) {
			return true;
		}

		if (!this.epithelium.getEpitheliumEvents().getDivisionTrigger(this.selModel)
				.equals(this.epiEventClone.getDivisionTrigger(this.selModel))) {
			return true;
		}

		if (!this.epithelium.getEpitheliumEvents().getMCE(this.selModel).getDivisionAlgorithm()
				.equals(this.epiEventClone.getMCE(this.selModel).getDivisionAlgorithm())) {
			return true;
		}

		if (!this.epithelium.getEpitheliumEvents().getMCE(this.selModel).getDeathAlgorithm()
				.equals(this.epiEventClone.getMCE(this.selModel).getDeathAlgorithm())) {
			return true;
		}

		if (this.epithelium.getEpitheliumEvents().getMCE(this.selModel)
				.getDivisionRange() != (this.epiEventClone.getMCE(this.selModel).getDivisionRange())) {
			return true;
		}
		if (this.epithelium.getEpitheliumEvents().getMCE(this.selModel).getNewCellState()
				!= this.epiEventClone.getMCE(this.selModel).getNewCellState()) {
			return true;
		}


		return false;
	}

	@Override
	public void applyChange() {
		// TODO Auto-generated method stub
		List<LogicalModel> modelList = new ArrayList<LogicalModel>(this.epithelium.getEpitheliumGrid().getModelSet());

		this.epiEventClone = this.epithelium.getEpitheliumEvents().clone();

		this.modelSelectionPanel.removeAll();
		this.modelSelectionPanel.add(this.newModelCombobox(modelList));
		this.selModel = modelList.get(0);

	}

	private JComboBox<String> newModelCombobox(List<LogicalModel> modelList) {

		// Model selection list
		String[] saSBML = new String[modelList.size()];
		for (int i = 0; i < modelList.size(); i++) {
			saSBML[i] = Project.getInstance().getProjectFeatures().getModelName(modelList.get(i));
		}
		JComboBox<String> jcb = new JComboWideBox<String>(saSBML);
		if (this.selModel != null) {
			jcb.setSelectedItem(this.selModel);
		}
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

	protected void updatePanelsWithModel(LogicalModel m) {

		this.selModel = m;
		ModelCellularEvent mce = this.epiEventClone.getMCE(this.selModel);

		if (mName2JRBdivision.containsKey(mce.getDivisionTrigger())) {
			this.mName2JRBdivision.get(mce.getDivisionTrigger()).setSelected(true);
			updateTriggerDivision(mce.getDivisionTrigger(), false);
		}
		if (mName2JRBdeath.containsKey(mce.getDeathTrigger())) {
			this.mName2JRBdeath.get(mce.getDeathTrigger()).setSelected(true);
			updateTriggerDeath(mce.getDeathTrigger(), false);
		}

		SliderPanel spDeath = this.mModel2lsSPanel.get(selModel).get(0);
		updateAlpha(spDeath.getValue(), spDeath.getLabel(), spDeath.getMin(), spDeath.getMax());
		SliderPanel spDivision = this.mModel2lsSPanel.get(selModel).get(1);
		updateAlpha(spDivision.getValue(), spDivision.getLabel(), spDivision.getMin(), spDivision.getMax());

		this.validateDivisionPattern(this.jtfPatternDivision);
		this.validateDeathPattern(this.jtfPatternDivision);
	}
	
	public boolean isInitialized() {
		return this.isInitialized;
	}
}
