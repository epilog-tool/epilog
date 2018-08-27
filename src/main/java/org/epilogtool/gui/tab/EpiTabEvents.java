package org.epilogtool.gui.tab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.TreePath;

import org.epilogtool.common.Txt;
import org.epilogtool.core.Epithelium;
import org.epilogtool.gui.EpiGUI.TabChangeNotifyProj;
import org.epilogtool.gui.tab.EpiTabDefinitions.TabProbablyChanged;
import org.epilogtool.gui.widgets.StochasticsSlider;


public class EpiTabEvents extends EpiTabDefinitions {


	private static final long serialVersionUID = 4263444740319547502L;
	
	private JPanel jpTriggerDivision;
	private JPanel jpTriggerDeath;
	private TabProbablyChanged tpc;
	
	private JPanel jpPatternDivision;
	private JPanel jpPatternDeath;
	private JPanel jpNewState;
	
	private JPanel auxDivisionPanel;
	private JPanel auxDeathPanel;
	
	private JPanel jpAlphaDivision;
	private JScrollPane jspAlphaDivision;
	private JSlider jAlphaSlideDivision;
	private JLabel jAlphaLabelValueDivision;
	private int alphaDivisionMin;
	private int alphaDivisionMax;
	
	private JPanel jpAlphaDeath;
	private JScrollPane jspAlphaDeath;
	private JSlider jAlphaSlideDeath;
	private JLabel jAlphaLabelValueDeath;
	private int alphaDeathMin;
	private int alphaDeathMax;


	
	private String trigerOrder;

	public EpiTabEvents(Epithelium e, TreePath path, TabChangeNotifyProj tabChanged) {
		super(e, path, tabChanged);
	}

	/**
	 * Creates the EventsPanel, the first time the tab is created.
	 * 
	 */
	public void initialize() {

		this.center.setLayout(new BoxLayout(this.center, BoxLayout.Y_AXIS));
		this.tpc = new TabProbablyChanged();
		
		this.jpPatternDeath = new JPanel();
		this.jpPatternDivision = new JPanel();
	
		this.auxDivisionPanel = new JPanel(new BorderLayout());
		this.auxDeathPanel = new JPanel(new BorderLayout());
		
		
		///PATTERN DIVISON
		
		this.jpPatternDivision.add(new JLabel("Pattern"));
		JTextField pattern = new JTextField();
		pattern.setColumns(30);
		this.jpPatternDivision.add(pattern);
		
		//New cell State Division
		
		this.jpNewState = new JPanel();
		this.jpNewState.add(new JLabel("New Cell State"));
		JTextField state = new JTextField();
		state.setColumns(30);
		
	
		////////////////////////////////////////////////////////////////// ALPHA SLIDER DIVISION
		
		// Alpha asynchronism panel
		this.alphaDivisionMin = 0;
		this.alphaDivisionMax = 100;
		int alphaDivisionStep = 10;
		
		this.jpAlphaDivision = new JPanel(new BorderLayout());
		
		this.jpAlphaDivision.setPreferredSize(new Dimension(400, 100));
		this.jpAlphaDivision.setMaximumSize(new Dimension(5000, 100));
		this.jpAlphaDivision.setMinimumSize(new Dimension(100, 100));
		this.jspAlphaDivision = new JScrollPane(this.jpAlphaDivision);
		
		JPanel jpAlphaInfoDivision = new JPanel(new BorderLayout());
//		jpAlphaInfoDivision.add(new JLabel(Txt.get("s_TAB_ALPHA_CURR")), BorderLayout.LINE_START);
		jpAlphaInfoDivision.add(new JLabel("Division probability: "), BorderLayout.LINE_START);
		
		this.jAlphaLabelValueDivision = new JLabel("--");
		jpAlphaInfoDivision.add(this.jAlphaLabelValueDivision, BorderLayout.CENTER);
		jpAlphaDivision.add(jpAlphaInfoDivision, BorderLayout.CENTER);

		jAlphaSlideDivision = new JSlider(JSlider.HORIZONTAL, alphaDivisionMin, alphaDivisionMax, alphaDivisionMax);
		jAlphaSlideDivision.setName("Division");
		jAlphaSlideDivision.setValue(50);
		jAlphaSlideDivision.setMajorTickSpacing(alphaDivisionStep);
		jAlphaSlideDivision.setMinorTickSpacing(1);
		jAlphaSlideDivision.setPaintTicks(true);
		jAlphaSlideDivision.setPaintLabels(true);
		Hashtable<Integer, JLabel> labelTableDivision = new Hashtable<Integer, JLabel>();
		for (int i = alphaDivisionMin; i <= alphaDivisionMax; i += alphaDivisionStep) {
			labelTableDivision.put(new Integer(i), new JLabel("" + ((float) i / alphaDivisionMax)));
		}
		jAlphaSlideDivision.setLabelTable(labelTableDivision);
	
		jAlphaSlideDivision.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider slide = (JSlider) e.getSource();
				updateSliderValues(slide);
				tpc.setChanged();
			}
		});
		
		updateAlpha(this.jAlphaSlideDivision.getValue(), this.jAlphaLabelValueDivision, this.alphaDivisionMin, this.alphaDivisionMax);
		jpAlphaDivision.add(jAlphaSlideDivision, BorderLayout.SOUTH);
		
//		
//		this.generateAlphaSlider("Division", this.jpAlphaDivision, this.jAlphaLabelValueDivision, this.jAlphaSlideDivision, this.alphaDivisionMin, this.alphaDivisionMax, this.alphaDivisionMax);

		////////////////////////////////////////////////////////////////// Random SLIDER DIVISION
		
		// Alpha asynchronism panel
//		this.randomDivisionMin = 0;
//		this.randomDivisionMax = 100;
//		
//		this.jpRandomDivision = new JPanel(new BorderLayout());
//		
//		this.jpRandomDivision.setPreferredSize(new Dimension(400, 100));
//		this.jpRandomDivision.setMaximumSize(new Dimension(5000, 100));
//		this.jpRandomDivision.setMinimumSize(new Dimension(100, 100));
//		this.jspRandomDivision = new JScrollPane(this.jpRandomDivision);
//		
//		this.generateAlphaSlider(this.jpRandomDivision, jRandomLabelValueDivision, jRandomSlideDivision, randomDivisionMin, randomDivisionMax, randomDivisionMax);
//	
		
		///PATTERN DEATH
		
		this.jpPatternDeath.add(new JLabel("Pattern"));
		JTextField patternDeath = new JTextField();
		patternDeath.setColumns(30);
		this.jpPatternDeath.add(patternDeath);
		
		
		//////////////////////////////////////////////////////////////////ALPHA SLIDER DEATH
		
		// Alpha asynchronism panel
		this.alphaDeathMin = 0;
		this.alphaDeathMax = 100;
		
		this.jpAlphaDeath = new JPanel(new BorderLayout());
		
		this.jpAlphaDeath.setPreferredSize(new Dimension(400, 100));
		this.jpAlphaDeath.setMaximumSize(new Dimension(5000, 100));
		this.jpAlphaDeath.setMinimumSize(new Dimension(100, 100));
		this.jspAlphaDeath = new JScrollPane(this.jpAlphaDeath);
		
		JPanel jpAlphaInfoDeath = new JPanel(new BorderLayout());
//		jpAlphaInfoDeath.add(new JLabel(Txt.get("s_TAB_ALPHA_CURR")), BorderLayout.LINE_START);
		jpAlphaInfoDeath.add(new JLabel("Death probability: "), BorderLayout.LINE_START);
		
		this.jAlphaLabelValueDeath = new JLabel("--");
		jpAlphaInfoDeath.add(this.jAlphaLabelValueDeath, BorderLayout.CENTER);
		jpAlphaDeath.add(jpAlphaInfoDeath, BorderLayout.CENTER);

		jAlphaSlideDeath = new JSlider(JSlider.HORIZONTAL, alphaDeathMin, alphaDeathMax, alphaDeathMax);
		jAlphaSlideDeath.setName("Death");
		jAlphaSlideDeath.setValue(50);
		jAlphaSlideDeath.setMajorTickSpacing(alphaDivisionStep);
		jAlphaSlideDeath.setMinorTickSpacing(1);
		jAlphaSlideDeath.setPaintTicks(true);
		jAlphaSlideDeath.setPaintLabels(true);
		Hashtable<Integer, JLabel> labelTableDeath = new Hashtable<Integer, JLabel>();
		for (int i = alphaDeathMin; i <= alphaDeathMax; i += alphaDivisionStep) {
			labelTableDeath.put(new Integer(i), new JLabel("" + ((float) i / alphaDeathMax)));
		}
		jAlphaSlideDeath.setLabelTable(labelTableDeath);
		jAlphaSlideDeath.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider slide = (JSlider) e.getSource();
				updateSliderValues(slide);
				tpc.setChanged();
			}
		});
		
		updateAlpha(this.jAlphaSlideDeath.getValue(), this.jAlphaLabelValueDeath, this.alphaDeathMin, this.alphaDeathMax);
		jpAlphaDeath.add(jAlphaSlideDeath, BorderLayout.SOUTH);
		
		////////////////////////////////////////////////////////////////// Random SLIDER DEATH
		
		// Alpha asynchronism panel
//		this.randomDeathMin = 0;
//		this.randomDeathMax = 100;
//		
//		this.jpRandomDeath = new JPanel(new BorderLayout());
//		
//		this.jpRandomDeath.setPreferredSize(new Dimension(400, 100));
//		this.jpRandomDeath.setMaximumSize(new Dimension(5000, 100));
//		this.jpRandomDeath.setMinimumSize(new Dimension(100, 100));
//		this.jspRandomDeath = new JScrollPane(this.jpRandomDeath);
//		
//		this.generateAlphaSlider(this.jpRandomDeath, this.jRandomLabelValueDeath, this.jRandomSlideDeath, this.randomDeathMin, this.randomDeathMax, this.randomDeathMax);
	
		
		this.jpTriggerDivision = new JPanel(new BorderLayout());
		this.jpTriggerDeath = new JPanel(new BorderLayout());
		
		Map<String, JRadioButton> triggerDeath2Radio = new HashMap<String, JRadioButton>();
		Map<String, JRadioButton> triggerDivision2Radio = new HashMap<String, JRadioButton>();
		
		
		
		////DIVISION
		
		JPanel jpDivision = new JPanel();
		JPanel jpTriggerDivisionOptions = new JPanel();
		
		jpDivision.setBorder(BorderFactory.createTitledBorder("Division"));
		ButtonGroup groupDivision = new ButtonGroup();
		
		JLabel jLTriggerDivision = new JLabel("Trigger: ") ;
		jpTriggerDivisionOptions.add(jLTriggerDivision);
		
		List<String> triggerOptions = new ArrayList<String>();
		triggerOptions.add("None");
		triggerOptions.add("Random");
		triggerOptions.add("Pattern");
	
		for (String triggerOption: triggerOptions) {
			JRadioButton jrb = new JRadioButton(triggerOption);
			jrb.setName(triggerOption);
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
		
		
		jpTriggerDivision.add(jpTriggerDivisionOptions,BorderLayout.NORTH);
		jpDivision.add(jpTriggerDivision);
		//Event
		
		this.center.add(jpDivision);
		
		////Death
		
		JPanel jpDeath = new JPanel();
		JPanel jpTriggerDeathOptions = new JPanel();
		
		jpDeath.setBorder(BorderFactory.createTitledBorder("Death"));
		ButtonGroup groupDeath = new ButtonGroup();
		
		JLabel jLTriggerDeath = new JLabel("Trigger: ") ;
		jpTriggerDeathOptions.add(jLTriggerDeath);
	
		for (String triggerOption: triggerOptions) {
			JRadioButton jrb = new JRadioButton(triggerOption);
			jrb.setName(triggerOption);
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
		//Event
		
		this.center.add(jpDeath);
		
		this.isInitialized = true;
	}
	
	
	protected void updateTriggerOrder(JRadioButton jrb) {
		this.trigerOrder = (jrb.getName());
		
	}

	protected void updateTriggerDivision(JRadioButton jrb) {
		
		this.auxDivisionPanel.removeAll();
		String str = jrb.getName();
		
		if (str == "Random") {
			this.auxDivisionPanel.add(this.jpAlphaDivision,BorderLayout.SOUTH);
		}

		if (str == "Pattern") {
			this.auxDivisionPanel.add(this.jpPatternDivision, BorderLayout.CENTER);
			System.out.println(this.epithelium.getEpitheliumEvents().getNewCellState());
			if (this.epithelium.getEpitheliumEvents().getNewCellState().equals("Predefined")) {
				this.auxDivisionPanel.add(this.jpNewState, BorderLayout.SOUTH);
			}
		}
		
		this.jpTriggerDivision.add(this.auxDivisionPanel, BorderLayout.SOUTH);
		this.repaint();
		this.jpTriggerDivision.repaint();
		this.revalidate();
		this.jpTriggerDivision.revalidate();
	}
	
	protected void updateTriggerDeath(JRadioButton jrb) {
		
		this.auxDeathPanel.removeAll();
		String str = jrb.getName();
		
		if (str == "Random") {
			this.auxDeathPanel.add(this.jpAlphaDeath,BorderLayout.SOUTH);
		}

		if (str == "Pattern") {
			this.auxDeathPanel.add(this.jpPatternDeath, BorderLayout.CENTER);
//			this.auxDeathPanel.add(this.jpAlphaDeath,BorderLayout.SOUTH);
		}
		
		this.jpTriggerDeath.add(this.auxDeathPanel, BorderLayout.SOUTH);

		
		this.repaint();
		this.jpTriggerDeath.repaint();
		this.revalidate();
		this.jpTriggerDeath.revalidate();
	}

	// ---------------------------------------------------------------------------
	// End initialize

	protected void updateSliderValues(JSlider jAlphaSlide) {
		// TODO Auto-generated method stub
//		if (this.trigerOrder.equals("Random order")) {
		if (this.jAlphaSlideDivision.getValue() + this.jAlphaSlideDeath.getValue()>100) {
			if (jAlphaSlide.getName().equals("Division")){
				this.jAlphaSlideDeath.setValue(100-this.jAlphaSlideDivision.getValue());
			}
			else if (jAlphaSlide.getName().equals("Death")){
				this.jAlphaSlideDivision.setValue(100-this.jAlphaSlideDeath.getValue());
			}
//		}
		}
		float valueDivision = (float) this.jAlphaSlideDivision.getValue() / 100;
		float valueDeath = (float) this.jAlphaSlideDeath.getValue() / 100;
		
		this.jAlphaLabelValueDivision.setText(""+valueDivision);
		this.jAlphaLabelValueDeath.setText(""+valueDeath);
	}

	private void updateAlpha(int sliderValue, JLabel  jAlphaLabelValue, int  SLIDER_MIN, int  SLIDER_MAX) {
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void applyChange() {
		// TODO Auto-generated method stub
		
	}


}
