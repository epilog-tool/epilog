package pt.igc.nmd.epilogue;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class TextPanel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JButton textButton;
	
	public TextPanel(){
		init();
	}

	private JPanel init() {

		textButton = new JButton("Work in Progress");
		add(textButton);
		return this;
	}
	
}
