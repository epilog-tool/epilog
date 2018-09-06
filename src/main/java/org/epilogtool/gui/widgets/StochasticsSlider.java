package org.epilogtool.gui.widgets;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.epilogtool.common.Txt;

public class StochasticsSlider extends JPanel {

	private final int SLIDER_MIN = 0;
	private  int SLIDER_MAX;
	private final int SLIDER_STEP = 10;
	
	private JPanel jpAlpha;
	private JScrollPane jspAlpha;
	private JSlider jAlphaSlide;
	private JLabel jAlphaLabelValue;
	

	public StochasticsSlider(){
		
		this.SLIDER_MAX = 100;
		
		// Alpha asynchronism panel
		this.jpAlpha = new JPanel(new BorderLayout());
		
		this.jpAlpha.setPreferredSize(new Dimension(400, 100));
		this.jpAlpha.setMaximumSize(new Dimension(5000, 100));
		this.jpAlpha.setMinimumSize(new Dimension(100, 100));
		this.jspAlpha = new JScrollPane(this.jpAlpha);
		this.jspAlpha.setBorder(BorderFactory.createEmptyBorder());
		this.jpAlpha.setBorder(BorderFactory.createTitledBorder(Txt.get("s_TAB_ALPHA_TITLE")));

		// JSlider for alpha-asynchronism
		this.generateAlphaSlider();
		System.out.println(this.SLIDER_MAX);
	}
	
	
	public void setSliderMax(int max) {
		this.SLIDER_MAX = max;
	}
	
	public int getSliderMax(int max) {
		return this.SLIDER_MAX;
	}
	
	private void generateAlphaSlider() {
		JPanel jpAlphaInfo = new JPanel(new BorderLayout());
		jpAlphaInfo.add(new JLabel(Txt.get("s_TAB_ALPHA_CURR")), BorderLayout.LINE_START);
		this.jAlphaLabelValue = new JLabel("--");
		jpAlphaInfo.add(this.jAlphaLabelValue, BorderLayout.CENTER);
		jpAlpha.add(jpAlphaInfo, BorderLayout.CENTER);

		this.jAlphaSlide = new JSlider(JSlider.HORIZONTAL, this.SLIDER_MIN, this.SLIDER_MAX, this.SLIDER_MAX);
		this.jAlphaSlide.setMajorTickSpacing(this.SLIDER_STEP);
		this.jAlphaSlide.setMinorTickSpacing(1);
		this.jAlphaSlide.setPaintTicks(true);
		this.jAlphaSlide.setPaintLabels(true);
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		for (int i = this.SLIDER_MIN; i <= this.SLIDER_MAX; i += this.SLIDER_STEP) {
			labelTable.put(new Integer(i), new JLabel("" + ((float) i / this.SLIDER_MAX)));
		}
		this.jAlphaSlide.setLabelTable(labelTable);
		this.jAlphaSlide.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider slide = (JSlider) e.getSource();
//				tpc.setChanged();
			}
		});
		jpAlpha.add(this.jAlphaSlide, BorderLayout.SOUTH);
	}

}