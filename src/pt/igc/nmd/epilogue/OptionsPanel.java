package pt.igc.nmd.epilogue;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;



public class OptionsPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private RollOver rollOver = null;
	private RunStopButton runButton = null;
	private JButton stepButton = null;
	private IterationLabel iterationLabel = null;

	public OptionsPanel() {
		init();
	}

	private JPanel init() {

		rollOver = new RollOver();
		runButton= new RunStopButton();
		stepButton = new JButton("Step");
		iterationLabel = new IterationLabel();
		
		add(rollOver);
		add(runButton);
		
		add(iterationLabel);
		
		//Step Button
		stepButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Simulation.step();
			System.out.println("step");
			}
		});
		add(stepButton);
		
		return this;
	}
}
