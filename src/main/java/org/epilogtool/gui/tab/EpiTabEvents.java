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
import org.epilogtool.core.ModelCellularEvent;
import org.epilogtool.gui.EpiGUI.TabChangeNotifyProj;
import org.epilogtool.gui.widgets.JComboWideBox;
import org.epilogtool.gui.widgets.SliderPanel;
import org.epilogtool.project.Project;


public class EpiTabEvents extends EpiTabDefinitions {


	private static final long serialVersionUID = 4263444740319547502L;
	
	private LogicalModel selModel;
	private ModelCellularEvent mce;
	
	private JPanel jpTriggerDivision;
	private JPanel jpTriggerDeath;
	
	private TabProbablyChanged tpc;
	
	private JPanel jpPatternDivision;
	private JPanel jpPatternDeath;
	private JPanel jpNewState;
	
	private JPanel auxDivisionPanel;
	private JPanel auxDeathPanel;
	
	private Epithelium epiClone;
	

	public EpiTabEvents(Epithelium e, TreePath path, TabChangeNotifyProj tabChanged) {
		super(e, path, tabChanged);
	}

	/**
	 * Creates the EventsPanel, the first time the tab is created.
	 * 
	 */
	public void initialize() {
	
		this.center.setLayout(new BorderLayout());
		Epithelium epiClone = this.epithelium.clone();
		
		
		List<LogicalModel> modelList = new ArrayList<LogicalModel>(this.epithelium.getEpitheliumGrid().getModelSet());
		for (LogicalModel model: modelList) {
			
			SliderPanel spSliderDeath = new SliderPanel(Txt.get("s_TAB_EVE_DEATH_PROBABILITY"),Txt.get("s_TAB_EVE_DEATH"),this);
			SliderPanel spSliderDivision = new SliderPanel(Txt.get("s_TAB_EVE_DIVISION_PROBABILITY"),Txt.get("s_TAB_EVE_DIVISION"),this);
			ModelCellularEvent mce = new ModelCellularEvent(model,spSliderDeath,spSliderDivision);
			epiClone.getEpitheliumEvents().setModel2MCE(model, mce);
		}
		
		
		this.tpc = new TabProbablyChanged();
		
		this.jpPatternDeath = new JPanel();
		this.jpPatternDivision = new JPanel();
		
		this.jpTriggerDivision = new JPanel(new BorderLayout());
		this.jpTriggerDeath = new JPanel(new BorderLayout());
		
		this.auxDivisionPanel = new JPanel(new BorderLayout());
		this.auxDeathPanel = new JPanel(new BorderLayout());
		
		
		///LEFT PANEL
		JPanel left = new JPanel();
	
		////Model selection jcomboCheckBox

		JPanel modelSelectionPanel = new JPanel();
		modelSelectionPanel.setBorder(BorderFactory.createTitledBorder(Txt.get("s_MODEL_SELECT")));
		
		
		JComboBox<String> jcbSBML = this.newModelCombobox(modelList);
		modelSelectionPanel.add(jcbSBML);
		jcbSBML.setSelectedIndex(0);
		left.add(modelSelectionPanel);
		this.selModel = Project.getInstance().getProjectFeatures().getModel((String) jcbSBML.getSelectedItem());
		this.mce = this.epiClone.getEpitheliumEvents().getMCE(this.selModel);
	
		///Right PANEL
		JPanel right = new JPanel();
		right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
		
		///DIVISON PANEL
		JPanel jpDivision = getDivisionPanel(this.selModel);
				
		//DEATH PANEL
		JPanel jpDeath = getDeathPanel(this.selModel);

		right.add(jpDivision);
		right.add(jpDeath);
		this.center.add(left, BorderLayout.LINE_START);
		this.center.add(right, BorderLayout.CENTER);
		this.isInitialized = true;
	}
	
	
	protected JPanel getDeathPanel (LogicalModel m) {
		
		JPanel jpDeath = new JPanel();
		JPanel jpTriggerDeathOptions = new JPanel();
		
		Map<String, JRadioButton> triggerDeath2Radio = new HashMap<String, JRadioButton>();
		
		jpDeath.setBorder(BorderFactory.createTitledBorder(Txt.get("s_TAB_EVE_DEATH")));
		ButtonGroup groupDeath = new ButtonGroup();
		
		JLabel jLTriggerDeath = new JLabel(Txt.get("s_TAB_EVE_TRIGGER")+": ") ;
		jpTriggerDeathOptions.add(jLTriggerDeath);
	
		List<String> triggerOptions = new ArrayList<String>();
		triggerOptions.add(Txt.get("s_TAB_EVE_TRIGGER_NONE"));
		triggerOptions.add(Txt.get("s_TAB_EVE_TRIGGER_RANDOM"));
		triggerOptions.add(Txt.get("s_TAB_EVE_TRIGGER_PATTERN"));
		
		for (String triggerOption: triggerOptions) {
			JRadioButton jrb = new JRadioButton(triggerOption);
			jrb.setName(triggerOption);
			if (triggerOption.equals(Txt.get("s_TAB_EVE_TRIGGER_NONE")))
				jrb.setSelected(true);
			triggerDeath2Radio.put(triggerOption,jrb);
				jrb.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						updateTriggerDeath(jrb);
					}
				});
				jpTriggerDeathOptions.add(jrb);
				groupDeath.add(jrb);
		}
		
		this.jpTriggerDeath.add(jpTriggerDeathOptions,BorderLayout.NORTH);
		jpDeath.add(this.jpTriggerDeath, BorderLayout.NORTH);
		
		
		///PATTERN
		
		this.jpPatternDeath.add(new JLabel(Txt.get("s_TAB_EVE_TRIGGER_PATTERN")));
		JTextField patternDeath = new JTextField();
		patternDeath.setColumns(30);
		this.jpPatternDeath.add(jpPatternDeath.add(patternDeath));
		
		
		//Random
		updateAlpha(this.mce.getDeathValue(), this.mce.getDeathLabel(), this.mce.getDeathMin(), this.mce.getDeathMax());
		
		return jpDeath;
	}

	protected JPanel getDivisionPanel (LogicalModel m) {
		
		Map<String, JRadioButton> triggerDivision2Radio = new HashMap<String, JRadioButton>();
		
		JPanel jpDivision = new JPanel();
		JPanel jpTriggerDivisionOptions = new JPanel();
		jpDivision.setBorder(BorderFactory.createTitledBorder(Txt.get("s_TAB_EVE_DIVISION")));
		
		//TRIGGER PANEL (division)
		
		ButtonGroup groupDivision = new ButtonGroup();
		
		JLabel jLTriggerDivision = new JLabel(Txt.get("s_TAB_EVE_TRIGGER") + ": ") ;
		jpTriggerDivisionOptions.add(jLTriggerDivision);
		
		List<String> triggerOptions = new ArrayList<String>();
		triggerOptions.add(Txt.get("s_TAB_EVE_TRIGGER_NONE"));
		triggerOptions.add(Txt.get("s_TAB_EVE_TRIGGER_RANDOM"));
		triggerOptions.add(Txt.get("s_TAB_EVE_TRIGGER_PATTERN"));
	
		for (String triggerOption: triggerOptions) {
			JRadioButton jrb = new JRadioButton(triggerOption);
			jrb.setName(triggerOption);
			if (triggerOption.equals(Txt.get("s_TAB_EVE_TRIGGER_NONE")))
				jrb.setSelected(true);
			triggerDivision2Radio.put(triggerOption,jrb);
				jrb.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						updateTriggerDivision(jrb);
					}
				});
				jpTriggerDivisionOptions.add(jrb);
				groupDivision.add(jrb);
		}
		
		this.jpTriggerDivision.add(jpTriggerDivisionOptions,BorderLayout.NORTH);
		jpDivision.add(this.jpTriggerDivision, BorderLayout.NORTH);
	
		//pattern PANEL (division)
		
		this.jpPatternDivision.add(new JLabel(Txt.get("s_TAB_EVE_TRIGGER_PATTERN")));	
		JTextField patternDivision = new JTextField();
		patternDivision.setColumns(30);
		this.jpPatternDivision.add(jpPatternDivision.add(patternDivision));
		
		
		//New cell State Division
		
		this.jpNewState = new JPanel();
		this.jpNewState.add(new JLabel(Txt.get("s_TAB_EVE_DIVISON_NEWCELLSTATE")));
		JTextField state = new JTextField();
		state.setColumns(30);
		this.jpNewState.add(state);
		
	
		////////////////////////////////////////////////////////////////// ALPHA SLIDER DIVISION
		
		
		updateAlpha(this.mce.getDivisionValue(), this.mce.getDivisionLabel(), this.mce.getDivisionMin(), this.mce.getDivisionMax());
		this.jpPatternDivision.add(this.mce.getDivisionSlider(), BorderLayout.SOUTH);
		
		return jpDivision;
	}
	
	protected void updateTriggerDivision(JRadioButton jrb) {
		
		tpc.setChanged();
		this.auxDivisionPanel.removeAll();
		String str = jrb.getName();
		
		if (str == Txt.get("s_TAB_EVE_TRIGGER_RANDOM")) {
			this.auxDivisionPanel.add(this.mce.getDivisionSlider(),BorderLayout.SOUTH);
			this.epithelium.getEpitheliumEvents().setDivisionTrigger(this.selModel,Txt.get("s_TAB_EVE_TRIGGER_RANDOM"));
		}

		else if (str == Txt.get("s_TAB_EVE_TRIGGER_PATTERN")) {
			this.auxDivisionPanel.add(this.jpPatternDivision, BorderLayout.CENTER);
			this.epithelium.getEpitheliumEvents().setDivisionTrigger(this.selModel, Txt.get("s_TAB_EVE_TRIGGER_PATTERN"));
			if (this.epithelium.getEpitheliumEvents().getDivisionOption().equals(Txt.get("s_TAB_EVE_DIVISON_NEWCELLSTATE_PREDEFINED"))) {
				this.auxDivisionPanel.add(this.jpNewState, BorderLayout.SOUTH);
			}
		}
		else {
			this.epithelium.getEpitheliumEvents().setDivisionTrigger(this.selModel,Txt.get("s_TAB_EVE_TRIGGER_NONE"));
		}
		
		this.jpTriggerDivision.add(this.auxDivisionPanel, BorderLayout.SOUTH);
		this.repaint();
		this.jpTriggerDivision.repaint();
		this.revalidate();
		this.jpTriggerDivision.revalidate();
	}
	
	protected void updateTriggerDeath(JRadioButton jrb) {
		
		tpc.setChanged();
		this.auxDeathPanel.removeAll();
		String str = jrb.getName();
		
		if (str == Txt.get("s_TAB_EVE_TRIGGER_RANDOM")) {
			this.auxDeathPanel.add(this.mce.getDeathSlider(),BorderLayout.SOUTH);
			this.epithelium.getEpitheliumEvents().setDeathTrigger(this.selModel,Txt.get("s_TAB_EVE_TRIGGER_RANDOM"));
		}

		else if (str == Txt.get("s_TAB_EVE_TRIGGER_PATTERN")) {
			this.auxDeathPanel.add(this.jpPatternDeath, BorderLayout.CENTER);
			this.epithelium.getEpitheliumEvents().setDeathTrigger(this.selModel,Txt.get("s_TAB_EVE_TRIGGER_PATTERN"));;
		}
		else {
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
		tpc.setChanged();
		
		if (this.mce.getDivisionValue() + this.mce.getDeathValue()>100) {
			if (slider.getName().equals(Txt.get("s_TAB_EVE_DIVISION"))){
				this.mce.setDeathValue(100-(int) this.mce.getDivisionValue());
			}
			else if (slider.getName().equals(Txt.get("s_TAB_EVE_DEATH"))){
				this.mce.setDivisionValue(100-(int) this.mce.getDeathValue());
			}
//		}
		}
		float valueDivision = (float) this.mce.getDivisionValue() / 100;
		float valueDeath = (float) this.mce.getDeathValue() / 100;
		
		this.mce.setDivisionText(""+valueDivision);
		this.mce.setDeathText(""+valueDeath);
	}

	private void updateAlpha(float sliderValue, JLabel  jAlphaLabelValue, int  SLIDER_MIN, int  SLIDER_MAX) {
		float value = (float) sliderValue / SLIDER_MAX;
//		this.updateSchemeInter.setAlpha(value);
		String sTmp = "" + value;
		if (sliderValue == SLIDER_MIN) {
			sTmp += " " + Txt.get("s_TAB_ALPHA_ASYNC");
		} else if (sliderValue == SLIDER_MAX) {
			sTmp += " " + Txt.get("s_TAB_ALPHA_SYNC");
		}
		jAlphaLabelValue.setText(sTmp);
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
		//TODO
		
		if (this.epithelium.getEpitheliumEvents().getDivisionProbability(this.selModel)!=(this.epiClone.getEpitheliumEvents().getMCE(this.selModel).getDivisionValue()))
				return true;
		
		if (this.epithelium.getEpitheliumEvents().getDeathProbability(this.selModel)!=(this.epiClone.getEpitheliumEvents().getMCE(this.selModel).getDeathValue()))
			return true;
		
		if (!this.epithelium.getEpitheliumEvents().getMCE(this.selModel).getDeathPattern().equals(this.epiClone.getEpitheliumEvents().getMCE(this.selModel).getDeathPattern()))
			return true;
	
	if (!this.epithelium.getEpitheliumEvents().getDivisionPattern(this.selModel).equals(this.epiClone.getEpitheliumEvents().getDivisionPattern(this.selModel)))
		return true;
	
		
	
		if (!this.epithelium.getEpitheliumEvents().getDeathTrigger(this.selModel).equals(this.epiClone.getEpitheliumEvents().getDeathTrigger(this.selModel)))
				return true;
		
		if (!this.epithelium.getEpitheliumEvents().getDivisionTrigger(this.selModel).equals(this.epiClone.getEpitheliumEvents().getDivisionTrigger(this.selModel)))
			return true;
		

		
		return false;
	}

	@Override
	public void applyChange() {
		// TODO Auto-generated method stub
		
	}

	private JComboBox<String> newModelCombobox(List<LogicalModel> modelList) {
		System.out.println(epiClone);
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

	protected void updatePanelsWithModel(LogicalModel m) {
		
		this.selModel = m;
		System.out.println("LM" + m);
		System.out.println("epi" + epiClone);
		this.mce = this.epiClone.getEpitheliumEvents().getMCE(this.selModel);
//		
		// TODO Auto-generated method stub

		
	}
}
