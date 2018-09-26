package org.epilogtool.gui.widgets;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.epilogtool.common.Txt;
import org.epilogtool.gui.tab.EpiTab;
import org.epilogtool.gui.tab.EpiTabEvents;


public class SliderPanel extends JPanel {

	private static final long serialVersionUID = 6740978217286113280L;
	
	private JScrollPane jspProb;
	private JSlider jsProb;
	private JLabel jlValue;
	private int probMin;
	private int probMax;
	
	
	private EpiTabEvents epiTab;
	
	private String name;

	public SliderPanel(String borderName, String name, EpiTabEvents epiTab) {
		
		this.setLayout(new BorderLayout());
		this.probMin = 0;
		this.probMax = 100;
		int probstep = 10;
		
		this.epiTab = epiTab;
		this.name = name;
		
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(400, 100));
		this.setMaximumSize(new Dimension(5000, 100));
		this.setMinimumSize(new Dimension(100, 100));
		this.jspProb = new JScrollPane(this);
		
		JPanel jpInfo = new JPanel(new BorderLayout());
		jpInfo.add(new JLabel(borderName + ": "), BorderLayout.LINE_START);
		
		
		this.jlValue = new JLabel("--");
		
		jpInfo.add(this.jlValue, BorderLayout.CENTER);
		this.add(jpInfo, BorderLayout.CENTER);

		this.jsProb = new JSlider(JSlider.HORIZONTAL, probMin, probMax, probMax);
		this.jsProb.setName(this.name);
		this.jsProb.setValue(50);
		this.jsProb.setMajorTickSpacing(probstep);
		this.jsProb.setMinorTickSpacing(1);
		this.jsProb.setPaintTicks(true);
		this.jsProb.setPaintLabels(true);
		Hashtable<Integer, JLabel> labelTableDeath = new Hashtable<Integer, JLabel>();
		for (int i = this.probMin; i <= this.probMax; i += probstep) {
			labelTableDeath.put(new Integer(i), new JLabel("" + ((float) i / this.probMax)));
		}
		this.jsProb.setLabelTable(labelTableDeath);
		this.jsProb.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider slide = (JSlider) e.getSource();
				updateSliderValues(slide);
				
//				tpc.setChanged();
			}
		});
		
//		updateAlpha(this.jAlphaSlideDeath.getValue(), this.jAlphaLabelValueDeath, this.alphaDeathMin, this.alphaDeathMax);
		this.add(jsProb, BorderLayout.SOUTH);

	}
	
	protected void updateSliderValues(JSlider slide) {
		if (!this.epiTab.isInitialized()) // FIXME
			return;
		if (this.name.equals(Txt.get("s_TAB_EVE_DEATH")) ||  (this.name.equals(Txt.get("s_TAB_EVE_DIVISION"))))
			((EpiTabEvents) this.epiTab).updateSliderValues(slide);
	}

	public float getValue() {
		return this.jsProb.getValue();
	}
	
	public JLabel getLabel() {
		// TODO Auto-generated method stub
		return this.jlValue;
	}

	public int getMax() {
		// TODO Auto-generated method stub
		return this.probMax;
	}

	public int getMin() {
		// TODO Auto-generated method stub
		return this.probMin;
	}

	public void setValue(float f) {
		this.jsProb.setValue((int) (f*100));
	}

	public void setText(String string) {
		// TODO Auto-generated method stub
		this.jlValue.setText(string);
	}


	public String getName() {
		return this.jsProb.getName();
	}

public void setEpiTab (EpiTabEvents epiTab) {
	this.epiTab = epiTab;
}



}
