package org.epilogtool.gui.tab;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.tree.TreePath;

import org.colomoto.biolqm.LogicalModel;
import org.epilogtool.common.Txt;
import org.epilogtool.core.Epithelium;
import org.epilogtool.core.EpitheliumEvents;
import org.epilogtool.core.EpitheliumUpdateSchemeIntra;
import org.epilogtool.core.ModelCellularEvent;
import org.epilogtool.gui.EpiGUI.TabChangeNotifyProj;
import org.epilogtool.gui.widgets.JComboWideBox;
import org.epilogtool.gui.widgets.SliderPanel;
import org.epilogtool.project.Project;


public class EpiTabEvents extends EpiTabDefinitions {


	private static final long serialVersionUID = 4263444740319547502L;
	
	private LogicalModel selModel;
//	private ModelCellularEvent mce;
	
	private JPanel jpTriggerDivision;
	private JPanel jpTriggerDeath;
	
	private JPanel jpDivision;
	private JPanel jpDeath;
	private JPanel jpRight;
	
	private TabProbablyChanged tpc;
	
	private JPanel jpPatternDivision;
	private JPanel jpPatternDeath;
	private JPanel jpNewState;
	
	private JPanel auxDivisionPanel;
	private JPanel auxDeathPanel;
	
	private JPanel modelSelectionPanel;
	
	private EpitheliumEvents epiEventClone;
	
	private Map<LogicalModel, List<SliderPanel>> mModel2lsSPanel;
	
	private Map<String, JRadioButton> mName2JRBdivision;
	private Map<String, JRadioButton> mName2JRBdeath;
	
	

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
		
		List<LogicalModel> modelList = new ArrayList<LogicalModel>(this.epithelium.getEpitheliumGrid().getModelSet());
		
		
		for (LogicalModel model: modelList) {
			List<SliderPanel> sp = new ArrayList<SliderPanel>();
			SliderPanel spSliderDeath = new SliderPanel(Txt.get("s_TAB_EVE_DEATH_PROBABILITY"),Txt.get("s_TAB_EVE_DEATH"), this);
			sp.add(spSliderDeath);
			SliderPanel spSliderDivision = new SliderPanel(Txt.get("s_TAB_EVE_DIVISION_PROBABILITY"),Txt.get("s_TAB_EVE_DIVISION"), this);
			
			sp.add(spSliderDivision);
			mModel2lsSPanel.put(model, sp);
		}
		
		for (LogicalModel model: modelList) {
			List<SliderPanel> sp = mModel2lsSPanel.get(model);
			sp.get(1).setValue(this.epithelium.getEpitheliumEvents().getDivisionValue(model));
			sp.get(0).setValue(this.epithelium.getEpitheliumEvents().getMCE(model).getDeathValue());
		}
		
		this.epiEventClone = this.epithelium.getEpitheliumEvents().clone();
		
		this.tpc = new TabProbablyChanged();
		
		this.jpPatternDeath = new JPanel();
		this.jpPatternDivision = new JPanel();
		
		this.jpTriggerDivision = new JPanel(new BorderLayout());
		this.jpTriggerDeath = new JPanel(new BorderLayout());
		
		this.auxDivisionPanel = new JPanel(new BorderLayout());
		this.auxDeathPanel = new JPanel(new BorderLayout());
		
		
		this.jpRight = new JPanel();
		
		///LEFT PANEL
		JPanel left = new JPanel();
	
	
		////Model selection jcomboCheckBox

		this.modelSelectionPanel = new JPanel();
		modelSelectionPanel.setBorder(BorderFactory.createTitledBorder(Txt.get("s_MODEL_SELECT")));
		
		JComboBox<String> jcbSBML = this.newModelCombobox(modelList);
		modelSelectionPanel.add(jcbSBML);
		jcbSBML.setSelectedIndex(0);
		left.add(modelSelectionPanel);
		this.selModel = Project.getInstance().getProjectFeatures().getModel((String) jcbSBML.getSelectedItem());
//		this.mce = this.epiEventClone.getMCE(this.selModel);
		
		
		
	
		///Right PANEL
		jpRight.setLayout(new BoxLayout(jpRight, BoxLayout.Y_AXIS));
		
		//DIVISION_PANEL
		
		this.jpDivision = new JPanel();
		JPanel jpTriggerDivisionOptions = new JPanel();
		
		this.jpDivision.setBorder(BorderFactory.createTitledBorder(Txt.get("s_TAB_EVE_DIVISION")));
		Map<String, JRadioButton> triggerDivision2Radio = new HashMap<String, JRadioButton>();
		
		//TRIGGERS OPTION
		
		ButtonGroup groupDivision = new ButtonGroup();
		
		JLabel jlTriggerDivision = new JLabel(Txt.get("s_TAB_EVE_TRIGGER") + ": ") ;
		jpTriggerDivisionOptions.add(jlTriggerDivision);
		
		List<String> triggerOptions = new ArrayList<String>();
		triggerOptions.add(Txt.get("s_TAB_EVE_TRIGGER_NONE"));
		triggerOptions.add(Txt.get("s_TAB_EVE_TRIGGER_RANDOM"));
		triggerOptions.add(Txt.get("s_TAB_EVE_TRIGGER_PATTERN"));
	
		for (String triggerOption: triggerOptions) {
			JRadioButton jrb = new JRadioButton(triggerOption);
			this.mName2JRBdivision.put(triggerOption, jrb);
			jrb.setName(triggerOption);
//			if (triggerOption.equals(Txt.get("s_TAB_EVE_TRIGGER_NONE")))
//				jrb.setSelected(true);
			triggerDivision2Radio.put(triggerOption,jrb);
				jrb.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						updateTriggerDivision(jrb.getName(), true);
					}
				});
				jpTriggerDivisionOptions.add(jrb);
				groupDivision.add(jrb);
		}
		this.updateTriggerDivision(this.epiEventClone.getMCE(this.selModel).getDivisionTrigger(), true);
		
		
		this.jpTriggerDivision.add(jpTriggerDivisionOptions,BorderLayout.NORTH);
		this.jpDivision.add(this.jpTriggerDivision, BorderLayout.NORTH);
		
		//TRIGGERS SOUTH PANEL: PATTERN
		
		this.jpPatternDivision.add(new JLabel(Txt.get("s_TAB_EVE_TRIGGER_PATTERN")));	
		JTextField jtfPatternDivision = new JTextField();
		jtfPatternDivision.setColumns(30);
		this.jpPatternDivision.add(jtfPatternDivision);
		
		//New cell State Division
		
		this.jpNewState = new JPanel();
		this.jpNewState.add(new JLabel(Txt.get("s_TAB_EVE_DIVISON_NEWCELLSTATE")));
		JTextField state = new JTextField();
		state.setColumns(30);
		this.jpNewState.add(state);
		
		updateTriggerDivision(this.epiEventClone.getMCE(this.selModel).getDivisionTrigger(), true);
		
		//TRIGGERS SOUTH PANEL: RANDOM
		
		SliderPanel spDivision = this.mModel2lsSPanel.get(selModel).get(1);
		updateAlpha(spDivision.getValue(), spDivision.getLabel(), spDivision.getMin(), spDivision.getMax());
		
		this.jpDivision.repaint();
		this.jpDivision.revalidate();

		
		//DEATH PANEL
		
		this.jpDeath = new JPanel();
		JPanel jpTriggerDeathOptions = new JPanel();
		
		this.jpDeath.setBorder(BorderFactory.createTitledBorder(Txt.get("s_TAB_EVE_DEATH")));
		Map<String, JRadioButton> triggerDeath2Radio = new HashMap<String, JRadioButton>();
		
		//TRIGGERS OPTION
		
		ButtonGroup groupDeath = new ButtonGroup();
		
		JLabel jlTriggerDeath = new JLabel(Txt.get("s_TAB_EVE_TRIGGER") + ": ") ;
		jpTriggerDeathOptions.add(jlTriggerDeath);
	
		for (String triggerOption: triggerOptions) {
			JRadioButton jrb = new JRadioButton(triggerOption);
			this.mName2JRBdeath.put(triggerOption, jrb);
			jrb.setName(triggerOption);
			triggerDeath2Radio.put(triggerOption,jrb);
				jrb.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						updateTriggerDeath(jrb.getName(), true);
					}
				});
				jpTriggerDeathOptions.add(jrb);
				groupDeath.add(jrb);
		}
		this.updateTriggerDeath(this.epiEventClone.getMCE(this.selModel).getDeathTrigger(), true);
	
		this.jpTriggerDeath.add(jpTriggerDeathOptions,BorderLayout.NORTH);
		this.jpDeath.add(this.jpTriggerDeath, BorderLayout.NORTH);
		
		//TRIGGERS SOUTH PANEL: PATTERN
		
		this.jpPatternDeath.add(new JLabel(Txt.get("s_TAB_EVE_TRIGGER_PATTERN")));	
		JTextField jtfPatternDeath = new JTextField();
		jtfPatternDeath.setColumns(30);
		this.jpPatternDeath.add(jtfPatternDeath);
		
		updateTriggerDeath(this.epiEventClone.getMCE(this.selModel).getDeathTrigger(), true);
		
		//TRIGGERS SOUTH PANEL: RANDOM
		SliderPanel spDeath = this.mModel2lsSPanel.get(selModel).get(0);
		updateAlpha(spDeath.getValue(), spDeath.getLabel(), spDeath.getMin(), spDeath.getMax());
		
		this.jpDeath.repaint();
		this.jpDeath.revalidate();
		
		//GROUP ALL PANELS
		this.jpRight.add(this.jpDivision);
		this.jpRight.add(this.jpDeath);
		this.center.add(left, BorderLayout.LINE_START);
		this.center.add(this.jpRight, BorderLayout.CENTER);
		this.isInitialized = true;
	}
	

	
	protected void updateTriggerDivision(String string, boolean control) {
		
		if (this.isInitialized && control)
				tpc.setChanged();
		
		this.auxDivisionPanel.removeAll();
		String str = string;
		
		if (str.equals(Txt.get("s_TAB_EVE_TRIGGER_RANDOM"))) {
			this.mName2JRBdivision.get(Txt.get("s_TAB_EVE_TRIGGER_RANDOM")).setSelected(true);
			this.auxDivisionPanel.add(this.mModel2lsSPanel.get(this.selModel).get(1),BorderLayout.SOUTH);
			this.epiEventClone.setDivisionTrigger(this.selModel,Txt.get("s_TAB_EVE_TRIGGER_RANDOM"));
		}

		else if (str.equals(Txt.get("s_TAB_EVE_TRIGGER_PATTERN"))) {
			this.mName2JRBdivision.get(Txt.get("s_TAB_EVE_TRIGGER_PATTERN")).setSelected(true);
			this.auxDivisionPanel.add(this.jpPatternDivision, BorderLayout.CENTER);
			this.epiEventClone.setDivisionTrigger(this.selModel, Txt.get("s_TAB_EVE_TRIGGER_PATTERN"));
			if (this.epiEventClone.getDivisionOption().equals(Txt.get("s_TAB_EVE_DIVISON_NEWCELLSTATE_PREDEFINED"))) {
				this.auxDivisionPanel.add(this.jpNewState, BorderLayout.SOUTH);
			}
		}
		else {
			this.mName2JRBdivision.get(Txt.get("s_TAB_EVE_TRIGGER_NONE")).setSelected(true);
			this.epiEventClone.setDivisionTrigger(this.selModel,Txt.get("s_TAB_EVE_TRIGGER_NONE"));
		}
		
		this.jpTriggerDivision.add(this.auxDivisionPanel, BorderLayout.SOUTH);
		this.repaint();
		this.jpTriggerDivision.repaint();
		this.revalidate();
		this.jpTriggerDivision.revalidate();
	}
	
	protected void updateTriggerDeath(String str, boolean control) {
		
		if (this.isInitialized && control)
			tpc.setChanged();
		this.auxDeathPanel.removeAll();
		
		if (str.equals(Txt.get("s_TAB_EVE_TRIGGER_RANDOM"))) {
			this.mName2JRBdeath.get(Txt.get("s_TAB_EVE_TRIGGER_RANDOM")).setSelected(true);
			this.auxDeathPanel.add(this.mModel2lsSPanel.get(this.selModel).get(0),BorderLayout.SOUTH);
			this.epiEventClone.setDeathTrigger(this.selModel,Txt.get("s_TAB_EVE_TRIGGER_RANDOM"));
		}

		else if (str.equals(Txt.get("s_TAB_EVE_TRIGGER_PATTERN"))) {
			this.mName2JRBdeath.get(Txt.get("s_TAB_EVE_TRIGGER_PATTERN")).setSelected(true);
			this.auxDeathPanel.add(this.jpPatternDeath, BorderLayout.CENTER);
			this.epiEventClone.setDeathTrigger(this.selModel,Txt.get("s_TAB_EVE_TRIGGER_PATTERN"));;
		}
		else {
			this.mName2JRBdeath.get(Txt.get("s_TAB_EVE_TRIGGER_NONE")).setSelected(true);
			this.epithelium.getEpitheliumEvents().setDeathTrigger(this.selModel,Txt.get("s_TAB_EVE_TRIGGER_NONE"));
		}
		
		this.jpTriggerDeath.add(this.auxDeathPanel, BorderLayout.SOUTH);

		
		this.repaint();
		this.jpTriggerDeath.repaint();
		this.revalidate();
		this.jpTriggerDeath.revalidate();
	}

	// ---------------------------------------------------------------------------
	// End initialize

	public void updateSliderValues(JSlider slider) {
		if (this.isInitialized)
			tpc.setChanged();
		
		SliderPanel spDeath = this.mModel2lsSPanel.get(this.selModel).get(0);
		SliderPanel spDivision = this.mModel2lsSPanel.get(this.selModel).get(1);
		
		if (spDeath.getValue() + spDivision.getValue()>100) {
			if (slider.getName().equals(Txt.get("s_TAB_EVE_DIVISION"))){
				spDeath.setValue(100-(int) spDivision.getValue());
			}
			else if (slider.getName().equals(Txt.get("s_TAB_EVE_DEATH"))){
				spDivision.setValue(100-(int) spDeath.getValue());
			}
//		}
		}
		
		float valueDivision = (float) spDivision.getValue() / 100;
		float valueDeath = (float) spDeath.getValue() / 100;
		
		
		spDeath.setText(""+valueDeath);
		spDivision.setText(""+valueDivision);
		
		
		
		this.epiEventClone.getMCE(this.selModel).setDeathValue(valueDeath);
		this.epiEventClone.getMCE(this.selModel).setDivisionValue(valueDivision);
		updateAlpha(spDeath.getValue(), spDeath.getLabel(), spDeath.getMin(), spDeath.getMax());
		updateAlpha(spDivision.getValue(), spDivision.getLabel(), spDivision.getMin(), spDivision.getMax());

	}
	
	private void updateAlpha(float sliderValue, JLabel  jAlphaLabelValue, int  SLIDER_MIN, int  SLIDER_MAX) {
		float value = (float) sliderValue / SLIDER_MAX;
		String sTmp = "" + value;
		jAlphaLabelValue.setText(sTmp);
	}
	
	
	@Override
	protected void buttonReset() {
		
		List<LogicalModel> modelList = new ArrayList<LogicalModel>(this.epithelium.getEpitheliumGrid().getModelSet());
		for (LogicalModel model: modelList) {
		ModelCellularEvent mce = this.epithelium.getEpitheliumEvents().getMCE(model);
		
		this.epiEventClone.setDeathTrigger(model, mce.getDeathTrigger());
		this.epiEventClone.setDivisionTrigger(model, mce.getDivisionTrigger());
		this.epiEventClone.setDeathValue(model, (int) mce.getDeathValue());
		this.epiEventClone.setDivisionValue(model, (int) mce.getDivisionValue());
		this.epiEventClone.setDeathPattern(model, mce.getDeathPattern());
		this.epiEventClone.setDivisionPattern(model, mce.getDivisionPattern());
		//TODO
		this.epiEventClone.setDivisionNewState(model, null);

		}
	}


	@Override
	protected void buttonAccept() {
		
		List<LogicalModel> modelList = new ArrayList<LogicalModel>(this.epithelium.getEpitheliumGrid().getModelSet());
		for (LogicalModel model: modelList) {
		ModelCellularEvent  mce = this.epiEventClone.getMCE(model);
		
//		System.out.println("1 " + mce.getDeathTrigger());
//		System.out.println("1 " + mce.getDivisionTrigger());
//		System.out.println("1 " + mce.getDeathValue());
//		System.out.println("1 " + mce.getDivisionValue());
//		
//		
//		System.out.println("2 " + this.epithelium.getEpitheliumEvents().getMCE(model).getDeathTrigger());
//		System.out.println("2 " + this.epithelium.getEpitheliumEvents().getMCE(model).getDivisionTrigger());
//		System.out.println("2 " + this.epithelium.getEpitheliumEvents().getMCE(model).getDeathValue());
//		System.out.println("2 " + this.epithelium.getEpitheliumEvents().getMCE(model).getDivisionValue());
//		
		
		this.epithelium.getEpitheliumEvents().setDeathTrigger(model, mce.getDeathTrigger());
		this.epithelium.getEpitheliumEvents().setDivisionTrigger(model, mce.getDivisionTrigger());
		this.epithelium.getEpitheliumEvents().setDeathValue(model, (float) mce.getDeathValue());
		this.epithelium.getEpitheliumEvents().setDivisionValue(model, (float) mce.getDivisionValue());
		this.epithelium.getEpitheliumEvents().setDeathPattern(model, mce.getDeathPattern());
		this.epithelium.getEpitheliumEvents().setDivisionPattern(model, mce.getDivisionPattern());
		//TODO
		this.epithelium.getEpitheliumEvents().setDivisionNewState(model, null);

		
//		System.out.println("3 " + this.epithelium.getEpitheliumEvents().getMCE(model).getDeathTrigger());
//		System.out.println("3 " + this.epithelium.getEpitheliumEvents().getMCE(model).getDivisionTrigger());
//		System.out.println("3 " + this.epithelium.getEpitheliumEvents().getMCE(model).getDeathValue());
//		System.out.println("3 " + this.epithelium.getEpitheliumEvents().getMCE(model).getDivisionValue());
		}
	
	}

	@Override
	protected boolean isChanged() {

		if (this.epithelium.getEpitheliumEvents().getDivisionValue(this.selModel)!=(this.epiEventClone.getMCE(this.selModel).getDivisionValue()))
				return true;
		
		if (this.epithelium.getEpitheliumEvents().getDeathValue(this.selModel)!=(this.epiEventClone.getMCE(this.selModel).getDeathValue()))
			return true;
		
		if (!this.epithelium.getEpitheliumEvents().getMCE(this.selModel).getDeathPattern().equals(this.epiEventClone.getMCE(this.selModel).getDeathPattern()))
			return true;
	
	if (!this.epithelium.getEpitheliumEvents().getDivisionPattern(this.selModel).equals(this.epiEventClone.getDivisionPattern(this.selModel)))
		return true;
	
		
	
		if (!this.epithelium.getEpitheliumEvents().getDeathTrigger(this.selModel).equals(this.epiEventClone.getDeathTrigger(this.selModel)))
				return true;
		
		if (!this.epithelium.getEpitheliumEvents().getDivisionTrigger(this.selModel).equals(this.epiEventClone.getDivisionTrigger(this.selModel)))
			return true;
		

		
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
		if (this.selModel!=null)
		{
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
	}

	public boolean isInitialized() {
		return this.isInitialized;
	}
}
